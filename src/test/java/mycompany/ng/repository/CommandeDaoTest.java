package mycompany.ng.repository;

import mycompany.pk.model.Client;
import mycompany.pk.dao.ClientsMagasin;
import mycompany.ng.model.Commande;
import mycompany.ng.model.LigneCommande;
import mycompany.ng.model.ProduitVendu;
import mycompany.ng.model.helper.CommandeHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:context.xml")
public class CommandeDaoTest {
    static { //runs when the main class is loaded.
        System.setProperty("org.jboss.logging.provider", "slf4j");
    }

    @Autowired
    private CommandeDao commandeDao;

    @Test
    public void test1 () {
        assertThat(true).isTrue();
    }

    @Test
    public void test2 () {
        Client client = ClientsMagasin.retrieveByEmail("platinium@monsite.fr");

        Commande commande = new Commande(client.getEmail());
        CommandeHelper.addRerenceProduit(commande, new LigneCommande(new ProduitVendu("s4v0n", "savon de paris", BigDecimal.TEN), 17));
        CommandeHelper.addRerenceProduit(commande, new LigneCommande(new ProduitVendu("c0t0n", "coton de paris", BigDecimal.TEN), 31));
        commandeDao.save(commande);

        List<Commande> comandesOfClient = commandeDao.getComandesOfClient(client);
        assertThat(comandesOfClient).hasSize(1);
        assertThat(comandesOfClient.get(0).getProduitsAchetes().get(1).getProduit().getReference()).isEqualTo("c0t0n");
    }
}