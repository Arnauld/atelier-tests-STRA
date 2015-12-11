package mycompany.ng.action;

import mycompany.pk.model.CategorieProduit;
import mycompany.pk.dao.ProduitCatalogueDao;
import mycompany.pk.dao.ClientsMagasin;
import mycompany.pk.utils.commerce.Prix;
import mycompany.pk.model.ProduitCatalogue;
import mycompany.exception.UserInactifException;
import mycompany.pk.model.AccountType;
import mycompany.pk.model.Client;
import mycompany.pk.utils.math.Percentage;
import mycompany.ng.model.ProduitAVendre;
import mycompany.ng.model.TvaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Action pour retrouver les produits à vendre
 */
@Component
public class RetrieveProduitsToSellAction {

    @Autowired
    private ProduitCatalogueDao produitsRepository;

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
