package mycompany.dao;

import com.google.gson.Gson;
import mycompany.ng.model.Event;
import mycompany.ng.repository.DaoSpringConf;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.Date;

public class DaoSpringConfTest {
    static { //runs when the main class is loaded.
        System.setProperty("org.jboss.logging.provider", "slf4j");
    }

    @Test
    public void should() {
        DaoSpringConf conf = new DaoSpringConf();
        EntityManager em = conf.entityManager();
        em.getTransaction().begin();
        em.persist(new Event("Our very first event!", new Date()));
        em.persist(new Event("A follow up event", new Date()));
        em.getTransaction().commit();

        em.getTransaction().begin();
        for (Event event : em.createQuery("select e from Event e", Event.class).getResultList()) {
            System.out.println(">>" + new Gson().toJson(event));
        }
        em.getTransaction().commit();

        em.close();
    }
}