package mycompany.ng.action;

import mycompany.exception.ProduitInconnuException;
import mycompany.pk.model.ProduitCatalogue;
import mycompany.pk.dao.ProduitCatalogueDao;
import mycompany.exception.UserInactifException;
import mycompany.pk.model.Client;
import mycompany.pk.dao.ClientsMagasin;
import mycompany.programme_fidelite.ActionRecompensee;
import mycompany.ng.repository.StockRepository;
import mycompany.ng.model.Commande;
import mycompany.ng.model.LigneCommande;
import mycompany.ng.model.ProduitVendu;
import mycompany.ng.model.helper.CommandeHelper;
import mycompany.ng.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * Permet d'enregistrer une vente effectuée par un client
 */
@Component
public class EnregistrerUneVenteAction {

    @Autowired
    private ProduitCatalogueDao catalogueProduits;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private StockRepository stockRepository;

    /**
     * Permet d'enregistrer une vente
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
                    ActionRecompensee action01 = new ActionRecompensee(clientEmail, opv.getReference(), nbArticlesAchetes * 1, new Date(), ActionRecompensee.TypeRecompense.ARTICLE);
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
}
