package mycompany.programme_fidelite;

import mycompany.data.bus.EventBus;

import java.io.Serializable;
import java.util.Date;

/**
 * Les actions utilisateurs rentrant en compte dans el calcu ldes points fidelités
 */
public class ActionRecompensee implements Serializable {


    public enum TypeRecompense {
        ARTICLE, PALIER_COMMANDE, PALIER_ANNUEL;
    }


    private String clientEmail;
    private String referenceCommande;
    private int pointsGagnes;
    private Date dateAction;
    private TypeRecompense type;

    public ActionRecompensee(String clientEmail, String referenceCommande, int pointsGagnes, Date dateAction, TypeRecompense type) {
        this.clientEmail = clientEmail;
        this.referenceCommande = referenceCommande;
        this.pointsGagnes = pointsGagnes;
        this.dateAction = dateAction;
        this.type = type;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public int getPointsGagnes() {
        return pointsGagnes;
    }

    public Date getDateAction() {
        return dateAction;
    }

    public String getReferenceCommande() {
        return referenceCommande;
    }

    public TypeRecompense getType() {
        return type;
    }

    public void send() {
        EventBus.publish(this);
    }
}
