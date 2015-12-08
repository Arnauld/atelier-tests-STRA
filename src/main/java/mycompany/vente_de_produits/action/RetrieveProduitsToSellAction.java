package mycompany.vente_de_produits.action;

import mycompany.client.repository.ClientsMagasin;
import mycompany.utils.commerce.Prix;
import mycompany.catalogue.model.ProduitCatalogue;
import mycompany.catalogue.repository.CatalogueProduits;
import mycompany.client.exception.UserInactifException;
import mycompany.client.model.AccountType;
import mycompany.client.model.Client;
import mycompany.utils.math.Percentage;
import mycompany.vente_de_produits.model.ProduitAVendre;
import mycompany.vente_de_produits.model.TvaType;
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
    private CatalogueProduits produitsRepository;

    @Autowired
    private ClientsMagasin clientsMagasin;

    /**
     * Renvoie les produits à vendre et leurs caractéristiques (pour un client donné)
     * @param clientEmail l'email de l'utilisateur (qui l'identifie)
     * @return la liste des produits à vendre
     */
    public List<ProduitAVendre> retrieveAllFor(String clientEmail) {

        Client client = clientsMagasin.retrieveByEmail(clientEmail);
        if(!client.isActif()){
            throw new UserInactifException(client);
        }

        List<ProduitCatalogue> catalogue = produitsRepository.retrieveAll();

        ArrayList<ProduitAVendre> produitAVendres = new ArrayList<ProduitAVendre>();
        for (ProduitCatalogue produitCatalogue : catalogue) {
            if(produitCatalogue.isActif()){
                Prix prixDeVenteHT =produitCatalogue.getPrixAchat().plus(produitCatalogue.getMarge());
                TvaType typeTva;
                switch(produitCatalogue.getCategorieProduit()){

                    case PRESERVATIF:
                        typeTva= TvaType.REDUITE;
                        break;
                    case MEDICAMENT_NON_REMBOURSABLE:
                        typeTva= TvaType.INTERMEDIAIRE;
                        break;
                    case MEDICAMENT_REMBOURSABLE:
                        typeTva= TvaType.PARTICULIERE;
                        break;
                    case LIVRE:
                        typeTva= TvaType.REDUITE;
                        break;
                    case EQUIPEMENT_PERSONNE_DEPENDANTE:
                        typeTva= TvaType.REDUITE;
                        break;
                    case HYGIENE_DENTAIRE:
                        typeTva= TvaType.NORMALE;
                        break;
                    case CONFISERIE:
                        typeTva= TvaType.NORMALE;
                        break;
                    default:
                        throw new IllegalArgumentException("no TVA type found for product of type "+produitCatalogue.getCategorieProduit());
                }
                Percentage tvaRate;
                switch (typeTva){

                    case NORMALE:
                        tvaRate=new Percentage(20);
                        break;
                    case INTERMEDIAIRE:
                        tvaRate=new Percentage(10);
                        break;
                    case REDUITE:
                        tvaRate=new Percentage(5.5);
                        break;
                    case PARTICULIERE:
                        tvaRate=new Percentage(2.5);
                        break;
                    default:
                        throw new IllegalArgumentException("no TVA rate found for this TvaType "+typeTva);
                }
                Prix prixDeVenteTTC = prixDeVenteHT.increaseBy(tvaRate);
                double discountInPercentage = 0.0;
                if(client.getAccountType()== AccountType.PLATINIUM){
                    discountInPercentage+=5;
                }
                Prix prixAPayer = prixDeVenteTTC.decreaseBy(new Percentage(discountInPercentage));
                produitAVendres.add(new ProduitAVendre(produitCatalogue.getReference(),produitCatalogue.getLibelle(),prixDeVenteHT,prixDeVenteTTC,prixAPayer));
            }
        }

        return produitAVendres;
    }


}
