package mycompany.pk.dao;

import mycompany.data.db.DB;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@Configuration
public class SpringConf {
    @Bean
    public ProduitCatalogueDao produitCatalogueDao() {
        return DB.getInstance().connect().open(ProduitCatalogueDao.class);
    }
}
