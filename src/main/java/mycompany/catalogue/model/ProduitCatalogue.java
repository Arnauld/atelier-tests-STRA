package mycompany.catalogue.model;

import mycompany.utils.commerce.Prix;

/**
 * Un produit du catalogue
 */
public class ProduitCatalogue {

    private String reference;
    private String libelle;
    private Prix prixAchat;
    private Prix marge;
    private CategorieProduit categorieProduit;
    private boolean actif;

    public ProduitCatalogue(String reference, String libelle, Prix prixAchat, boolean actif, long margeEnCentimes, CategorieProduit categorieProduit) {
        this.reference = reference;
        this.libelle = libelle;
        this.prixAchat = prixAchat;
        this.actif = actif;
        this.marge = new Prix(margeEnCentimes);
        this.categorieProduit = categorieProduit;
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

    public Prix getPrixAchat() {
        return prixAchat;
    }

    public void setPrixAchat(Prix prixAchat) {
        this.prixAchat = prixAchat;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Prix getMarge() {
        return marge;
    }

    public void setMarge(Prix marge) {
        this.marge = marge;
    }

    public CategorieProduit getCategorieProduit() {
        return categorieProduit;
    }

    public void setCategorieProduit(CategorieProduit categorieProduit) {
        this.categorieProduit = categorieProduit;
    }

    @Override
    public String toString() {
        return "ProduitCatalogue{" +
                "reference='" + reference + '\'' +
                ", libelle='" + libelle + '\'' +
                ", prixAchat=" + prixAchat +
                ", marge=" + marge +
                ", categorieProduit=" + categorieProduit +
                ", actif=" + actif +
                '}';
    }
}
