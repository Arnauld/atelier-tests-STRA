package mycompany.data;

import mycompany.catalogue.model.ProduitCatalogue;
import mycompany.client.model.Client;
import mycompany.programme_fidelite.model.ActionRecompensee;
import mycompany.vente_de_produits.model.Commande;

import java.util.List;

/**
 * Created by fmaury on 20/05/15.
 */
public interface IDataBase {

    List<ProduitCatalogue> getProduits();

    ProduitCatalogue getProduit(String reference);

    void changeStock(String referenceProduit, int newStock);

    Integer getStock(String reference);

    Client getClient(String email);

    void save(Commande operationDeVente);

    void save(ActionRecompensee action);
}
