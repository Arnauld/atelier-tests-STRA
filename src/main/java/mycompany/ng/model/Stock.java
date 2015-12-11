package mycompany.ng.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@Entity
@Table(name = "STOCKS")
public class Stock {
    @Id
    @Column(name = "REF")
    private String reference;

    @Column(name = "QTY")
    private int quantity;

    public Stock() {
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
