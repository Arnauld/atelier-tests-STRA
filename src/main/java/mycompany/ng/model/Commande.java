package mycompany.ng.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Une commande, passée par un client.
 * Contient une liste de produits et les quantités vendues
 */
@Entity
@Table(name = "COMMANDES")
public class Commande {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @Column(name = "REF")
    private String reference;

    @Column(name = "REF_CLIENT")
    private String clientEmail;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CMD_DATE")
    private Date dateDeLaTransaction;

    @OneToMany(targetEntity = LigneCommande.class, cascade=CascadeType.ALL)
    private List<LigneCommande> produitsAchetes = new ArrayList<LigneCommande>();

    public Commande() {
    }

    public Commande(String clientEmail) {
        this.reference = UUID.randomUUID().toString().replace("-", "");
        this.clientEmail = clientEmail;
        this.dateDeLaTransaction = new Date();
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public Date getDateDeLaTransaction() {
        return dateDeLaTransaction;
    }

    public void setDateDeLaTransaction(Date dateDeLaTransaction) {
        this.dateDeLaTransaction = dateDeLaTransaction;
    }

    public List<LigneCommande> getProduitsAchetes() {
        return produitsAchetes;
    }

    public void setProduitsAchetes(List<LigneCommande> produitsAchetes) {
        this.produitsAchetes = produitsAchetes;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public String toString() {
        return "OperationDeVente{" +
                "clientEmail='" + clientEmail + '\'' +
                ", dateDeLaTransaction=" + dateDeLaTransaction +
                ", produitsAchetes=" + produitsAchetes +
                '}';
    }
}
