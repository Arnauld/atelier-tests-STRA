package mycompany.stocks_produits.repository;

import mycompany.catalogue.model.ProduitCatalogue;
import mycompany.data.IDataBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Interractions avec la couche persistence des stocks
 */
@Repository
public class StockRepositoryStub implements StockRepository {

    @Autowired
    private IDataBase database;

    @Override
    public void ajouterAuStock(ProduitCatalogue produit, int quantite){
        System.out.println("Stock: Ajout de " + quantite + " '"+produit.getLibelle()+"'");
        int stock= database.getStock(produit.getReference());
        int newStock = stock + quantite;
        database.changeStock(produit.getReference(), newStock);

        System.out.println("Stock: stock mis à jour pour le produit '" +produit.getLibelle() +"' : "+stock+quantite);
    }

    @Override
    public void enleverDuStock(ProduitCatalogue produit, int quantite){
        System.out.println("Stock: On enleve " + quantite + " '"+produit.getLibelle()+"'");
        int stock= database.getStock(produit.getReference());
        int newStock = stock - quantite;
        database.changeStock(produit.getReference(), newStock);

        System.out.println("Stock: stock mis à jour pour le produit '" +produit.getLibelle() +"' : "+stock+quantite);
    }

}
