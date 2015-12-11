package mycompany.catalogue.repository;

import com.google.gson.Gson;
import mycompany.pk.dao.ProduitCatalogueDao;
import mycompany.pk.model.CategorieProduit;
import mycompany.pk.model.ProduitCatalogue;
import mycompany.data.db.DB;
import mycompany.pk.utils.commerce.Prix;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProduitCatalogueDaoIntegrationTest {

    private Gson gson = new Gson();
    private DB db;

    @Before
    public void setUp() {
        db = DB.getInstance();
        db.connect();
    }
    @After
    public void tearDown() {
        db.disconnect();
    }

    @Test
    public void should_be_able_to_retrieve_all() {
        ProduitCatalogueDao dao = db.open(ProduitCatalogueDao.class);
        for (ProduitCatalogue produitCatalogue : dao.retrieveAll()) {
            System.out.println(gson.toJson(produitCatalogue));
        }
        dao.close();
    }

    @Test
    public void should_be_able_to_insert_and_get_it_back() {
        ProduitCatalogueDao dao = db.open(ProduitCatalogueDao.class);
        String reference = "w" + System.currentTimeMillis();
        ProduitCatalogue produit = new ProduitCatalogue(
                reference, "_libelle", new Prix(170), true, new Prix(50), CategorieProduit.LIVRE);
        dao.insert(produit);
        ProduitCatalogue produitRetrieved = dao.get(reference);
        assertThat(produitRetrieved)
                .isNotNull()
                .isEqualToComparingFieldByField(produit);
        dao.close();
    }

}