package mycompany.ng.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Un produit vendu (dto)
 */
@Entity
@Table(name = "PRODUIT_VENDUS")
public class ProduitVendu {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @Column(name = "REFERENCE")
    private String reference;

    @Column(name = "LIBELLE")
    private String libelle;

    @Column(name = "prix_paye")
    private BigDecimal prixPaye;

    public ProduitVendu() {
    }

    public ProduitVendu(String reference, String libelle, BigDecimal prixPaye) {
        this.reference = reference;
        this.libelle = libelle;
        this.prixPaye = prixPaye;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public BigDecimal getPrixPaye() {
        return prixPaye;
    }

    public void setPrixPaye(BigDecimal prixPaye) {
        this.prixPaye = prixPaye;
    }

    @Override
    public String toString() {
        return "ProduitVendu{" +
                "reference='" + reference + '\'' +
                ", libelle='" + libelle + '\'' +
                ", prixPaye=" + prixPaye +
                '}';
    }
}
