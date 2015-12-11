package mycompany.ng.model;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class StockHelper {
    public static void ajouterAuStock(Stock stock, int quantite) {
        if(quantite<0)
            quantite = -1*quantite;
        int quantity = stock.getQuantity();
        stock.setQuantity(quantity+quantite);
    }
    public static void enleverDuStock(Stock stock, int quantite) {
        if(quantite<0)
            quantite = -1*quantite;
        int quantity = stock.getQuantity();
        stock.setQuantity(quantity-quantite);
    }
}
