package mycompany.ng.repository;

import mycompany.pk.model.ProduitCatalogue;
import mycompany.ng.model.Stock;
import mycompany.ng.model.StockHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Interractions avec la couche persistence des stocks
 */
@Repository
public class StockRepositoryStub implements StockRepository {

    @Autowired
    private StockDao database;

    @Override
    public void ajouterAuStock(ProduitCatalogue produit, int quantite){
        System.out.println("Stock: Ajout de " + quantite + " '" + produit.getLibelle() + "'");
        Stock stock= database.getStock(produit);
        StockHelper.ajouterAuStock(stock, quantite);
        database.update(stock);

        System.out.println("Stock: stock mis à jour pour le produit '" +produit.getLibelle() +"' : "+stock+quantite);
    }

    @Override
    public void enleverDuStock(ProduitCatalogue produit, int quantite){
        System.out.println("Stock: On enleve " + quantite + " '"+produit.getLibelle()+"'");
        Stock stock= database.getStock(produit);
        StockHelper.enleverDuStock(stock, -quantite);
        database.update(stock);

        System.out.println("Stock: stock mis à jour pour le produit '" +produit.getLibelle() +"' : "+stock+quantite);
    }

}
