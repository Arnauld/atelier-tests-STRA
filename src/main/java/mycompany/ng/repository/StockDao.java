package mycompany.ng.repository;

import mycompany.pk.model.ProduitCatalogue;
import mycompany.ng.model.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@Component
public class StockDao {

    @Autowired
    private EntityManager em;

    public void update(Stock stock) {
        em.getTransaction().begin();
        em.persist(stock);
        em.getTransaction().commit();
    }

    public Stock getStock(ProduitCatalogue produitCatalogue) {
        em.getTransaction().begin();
        Query query = em.createQuery("select e from Stock e where e.reference =?", Stock.class);
        query.setParameter(0, produitCatalogue.getReference());
        Stock result = (Stock) query.getSingleResult();
        em.getTransaction().commit();
        return result;
    }
}
