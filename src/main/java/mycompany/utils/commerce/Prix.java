package mycompany.utils.commerce;

import mycompany.utils.math.Percentage;

import java.math.BigDecimal;

/**
 * Notion de prix
 */
public class Prix {

    public static final BigDecimal CENT = new BigDecimal("100");
    private BigDecimal prix;

    public Prix(BigDecimal prix){
        this.prix=prix;
    }

    public Prix(long cents){
        this.prix=BigDecimal.valueOf(cents).divide(CENT);
    }

    public Prix plus(Prix montant) {
        return new Prix(prix.add(montant.prix));
    }

    /**
     *
     * @param percentage le pourcentage à appliquer au prix pour l'augmenter
     * @return le prix augmenté de x%
     */
    public Prix increaseBy(Percentage percentage) {
        return new Prix(percentage.applyToIncrease(this.prix));
    }

    /**
     *
     * @param percentage le pourcentage à appliquer au prix pour le diminuer
     * @return le prix diminué de x%
     */
    public Prix decreaseBy(Percentage percentage) {
        return new Prix(percentage.applyToDecrease(this.prix));
    }

    public long toCents(){
        return this.prix.multiply(CENT).longValue();
    }
    @Override
    public String toString() {
        return "Prix{" +
                "prix=" + prix +
                '}';
    }
}
