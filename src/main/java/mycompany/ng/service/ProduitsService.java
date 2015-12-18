package mycompany.ng.service;

import mycompany.exception.ProduitInconnuException;
import mycompany.exception.UserInactifException;
import mycompany.ng.model.Commande;
import mycompany.ng.model.LigneCommande;
import mycompany.ng.model.ProduitAVendre;
import mycompany.ng.model.ProduitVendu;
import mycompany.ng.model.TvaType;
import mycompany.ng.model.helper.CommandeHelper;
import mycompany.ng.repository.StockRepository;
import mycompany.ng.repository.TransactionRepository;
import mycompany.pk.dao.ClientsMagasin;
import mycompany.pk.dao.ProduitCatalogueDao;
import mycompany.pk.model.AccountType;
import mycompany.pk.model.CategorieProduit;
import mycompany.pk.model.Client;
import mycompany.pk.model.ProduitCatalogue;
import mycompany.pk.utils.commerce.Prix;
import mycompany.pk.utils.math.Percentage;
import mycompany.programme_fidelite.ActionRecompensee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@Service
public class ProduitsService {

    @Autowired
    private ProduitCatalogueDao catalogueProduits;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProduitCatalogueDao produitsRepository;

    @Autowired
    private ExecutorService async;


    /**
     * Permet d'enregistrer une vente effectuée par un client
     * @param clientEmail l'identifiant (email) du client ayant effectué la vente
     * @param produits les produits vendus
     */
    public void enregistrerUneVenteOuUnRetour(String clientEmail, Map<ProduitVendu,Integer> produits, boolean retour){
        Commande opv;
        int nbArticlesAchetes = 0;
        int nbPaliers10Euros = 0;
        ProduitVendu produitAVendre = null;
        // cumul
        BigDecimal cumulCommande =BigDecimal.ZERO;
        if(clientEmail!=null && produits!=null) {
            Client client = ClientsMagasin.retrieveByEmail(clientEmail);
            if (client.isActif()) {
                if (!retour) {
                    opv = new Commande(clientEmail);
                    for (Map.Entry<ProduitVendu, Integer> produitToSellQuantityEntry : produits.entrySet()) {
                        CommandeHelper.addRerenceProduit(opv, new LigneCommande(produitToSellQuantityEntry.getKey(), produitToSellQuantityEntry.getValue()));
                    }

                    for (LigneCommande ligneCommande : opv.getProduitsAchetes()) {
                        ProduitCatalogue produit = catalogueProduits.get(ligneCommande.getProduit().getReference());
                        if (produit != null) {
                            stockRepository.enleverDuStock(produit, ligneCommande.getQuantite());
                            nbArticlesAchetes += ligneCommande.getQuantite();

                            for (ProduitVendu toSell : produits.keySet()) {
                                if (toSell.getReference().equals(produit.getReference())) {
                                    produitAVendre = toSell;
                                    break;
                                }
                            }
                            cumulCommande = cumulCommande.add(produitAVendre.getPrixPaye().multiply(new BigDecimal(ligneCommande.getQuantite())));
                            while (cumulCommande.compareTo(new BigDecimal("10")) == 1) {
                                nbPaliers10Euros += 1;
                                cumulCommande = cumulCommande.subtract(new BigDecimal("10"));
                            }
                        } else {
                            throw new ProduitInconnuException(ligneCommande.getProduit().getReference());
                        }
                    }

                    final ActionRecompensee action01 = new ActionRecompensee(clientEmail, opv.getReference(), nbArticlesAchetes * 1, new Date(), ActionRecompensee.TypeRecompense.ARTICLE);
                    action01.send();

                    transactionRepository.save(opv);

                    ActionRecompensee action02 = new ActionRecompensee(clientEmail, opv.getReference(), nbPaliers10Euros * 2, new Date(), ActionRecompensee.TypeRecompense.PALIER_COMMANDE);
                    action02.send();

                } else {
                    opv = new Commande(clientEmail);
                    // on ne peut retourner qu'un seul produit
                    Map.Entry<ProduitVendu, Integer> produitRetourne = produits.entrySet().iterator().next();
                    CommandeHelper.addRerenceProduit(opv, new LigneCommande(produitRetourne.getKey(), produitRetourne.getValue()));
                    for (LigneCommande ligneCommande : opv.getProduitsAchetes()) {
                        ProduitCatalogue produit = catalogueProduits.get(ligneCommande.getProduit().getReference());

                        if (produit != null) {
                            stockRepository.ajouterAuStock(produit, ligneCommande.getQuantite());
                        } else {
                            throw new ProduitInconnuException(ligneCommande.getProduit().getReference());
                        }
                    }
                    transactionRepository.save(opv);
                }
            } else {
                throw new UserInactifException(client);
            }
        }
    }

    /**
     * Renvoie les produits à vendre et leurs caractéristiques (pour un client donné)
     * @param clientEmail l'email de l'utilisateur (qui l'identifie)
     * @return la liste des produits à vendre
     */
    public List<ProduitAVendre> retrieveAllFor(String clientEmail) {
        Prix prixDeVenteHT;
        Prix prixDeVenteTTC;
        Prix prixAPayer;
        TvaType typeTva;
        Percentage tvaRate;
        ArrayList<ProduitAVendre> produitAVendres;
        double discountInPercentage;
        Client client = ClientsMagasin.retrieveByEmail(clientEmail);
        List<ProduitCatalogue> catalogue = produitsRepository.retrieveAll();
        if(client.isActif()) {
            produitAVendres = new ArrayList<ProduitAVendre>();
            for (ProduitCatalogue produitCatalogue : catalogue) {
                if (produitCatalogue.isActif()) {
                    prixDeVenteHT = produitCatalogue.getPrixAchat().plus(produitCatalogue.getMarge());
                    CategorieProduit categorieProduit = produitCatalogue.getCategorieProduit();
                    if(categorieProduit!=null) {
                        switch (categorieProduit) {
                            case PRESERVATIF:
                                typeTva = TvaType.REDUITE;
                                break;
                            case MEDICAMENT_NON_REMBOURSABLE:
                                typeTva = TvaType.INTERMEDIAIRE;
                                break;
                            case MEDICAMENT_REMBOURSABLE:
                                typeTva = TvaType.PARTICULIERE;
                                break;
                            case LIVRE:
                                typeTva = TvaType.REDUITE;
                                break;
                            case EQUIPEMENT_PERSONNE_DEPENDANTE:
                                typeTva = TvaType.REDUITE;
                                break;
                            case HYGIENE_DENTAIRE:
                                typeTva = TvaType.NORMALE;
                                break;
                            case CONFISERIE:
                                typeTva = TvaType.NORMALE;
                                break;
                            default:
                                typeTva = null;
                        }
                        if(typeTva!=null) {
                            switch (typeTva) {

                                case NORMALE:
                                    tvaRate = new Percentage(20);
                                    break;
                                case INTERMEDIAIRE:
                                    tvaRate = new Percentage(10);
                                    break;
                                case REDUITE:
                                    tvaRate = new Percentage(5.5);
                                    break;
                                case PARTICULIERE:
                                    tvaRate = new Percentage(2.5);
                                    break;
                                default:
                                    tvaRate= null;
                            }
                            if(tvaRate!=null) {
                                prixDeVenteTTC = prixDeVenteHT.increaseBy(tvaRate);
                                discountInPercentage = 0.0;
                                if (client.getAccountType() == AccountType.PLATINIUM) {
                                    discountInPercentage += 5;
                                }
                                prixAPayer = prixDeVenteTTC.decreaseBy(new Percentage(discountInPercentage));
                                produitAVendres.add(new ProduitAVendre(produitCatalogue.getReference(), produitCatalogue.getLibelle(), prixDeVenteHT, prixDeVenteTTC, prixAPayer));
                            }
                            else {
                                throw new IllegalArgumentException("no TVA rate found for this TvaType " + typeTva);
                            }
                        }
                        else {
                            throw new IllegalArgumentException("no TVA type found for product of type " + produitCatalogue.getCategorieProduit());
                        }
                    }
                }
            }
        }
        else {
            throw new UserInactifException(client);
        }

        return produitAVendres;
    }

}
