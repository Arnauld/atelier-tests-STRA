package mycompany.ng.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@Configuration
public class DaoSpringConf {

    @Bean
    public EntityManager entityManager() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("mycompany");
        EntityManager em = emf.createEntityManager();
        return em;
    }
}
