package mycompany.ng.model.helper;

import mycompany.ng.model.Commande;
import mycompany.ng.model.LigneCommande;
import mycompany.ng.model.ProduitVendu;

import java.util.Iterator;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class CommandeHelper {

    public static void addRerenceProduit(Commande commande, LigneCommande reference) {
        commande.getProduitsAchetes().add(reference);
    }

    public static LigneCommande getLignePourProduit(Commande commande, String produit) {
        LigneCommande ligneCommandeRecherchee = null;
        boolean trouvee = false;
        if(commande!=null) {
            Iterator<LigneCommande> iterator = commande.getProduitsAchetes().iterator();
            while (iterator.hasNext() && !trouvee) {
                LigneCommande ligneCommande = iterator.next();
                if (ligneCommande != null) {
                    ProduitVendu produit1 = ligneCommande.getProduit();
                    if (produit != null && produit1.getReference() != null && produit1.getReference().equals(produit)) {
                        ligneCommandeRecherchee = ligneCommande;
                        trouvee = true;
                    }
                }
            }
        }
        return ligneCommandeRecherchee;

    }
}
