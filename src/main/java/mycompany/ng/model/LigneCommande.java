package mycompany.ng.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Une operation de vente pour un produit donné
 */
@Entity
@Table(name = "LIGNE_COMMANDES")
public class LigneCommande {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @OneToOne(cascade= CascadeType.ALL)
    private ProduitVendu produit;

    @Column(name = "QTY")
    private int quantite;

    public LigneCommande() {
    }

    public LigneCommande(ProduitVendu produit, int quantite) {
        this.produit = produit;
        this.quantite = quantite;
    }

    public ProduitVendu getProduit() {
        return produit;
    }

    public void setProduit(ProduitVendu produit) {
        this.produit = produit;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    @Override
    public String toString() {
        return "LigneCommande{" +
                "'" + produit + '\'' +
                " x " + quantite +
                '}';
    }
}
