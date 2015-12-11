package mycompany.ng.repository;

import mycompany.pk.model.Client;
import mycompany.ng.model.Commande;
import mycompany.ng.model.LigneCommande;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@Component
public class CommandeDao {

    @Autowired
    private EntityManager em;

    public void save(Commande commande) {
        em.getTransaction().begin();
        em.persist(commande);
        em.getTransaction().commit();
    }

    public List<Commande> getComandesOfClient(Client commandeOwner) {
        em.getTransaction().begin();
        Query query = em.createQuery("from Commande where clientEmail = :email", Commande.class);
        query.setParameter("email", commandeOwner.getEmail());
        List<Commande> resultList = query.getResultList();
        // on a des soucis, on force le chargement des liasion au cas
        // voir avec Philipe pour la raison de l' astuce
        // serieux Philou tu assures!
        for (Commande commande : resultList) {
            for (LigneCommande ligneCommande : commande.getProduitsAchetes()) {
                ligneCommande.getProduit();
            }
        }
        em.getTransaction().commit();
        return resultList;
    }

}
