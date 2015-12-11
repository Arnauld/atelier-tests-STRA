package mycompany.client.repository;

import com.google.gson.Gson;
import mycompany.pk.dao.ClientsMagasin;
import mycompany.pk.model.Client;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientsMagasinIntegrationTest {

    @Test
    public void should_retrive_client() {
        Client client = ClientsMagasin.retrieveByEmail("inactif@monsite.fr");
        System.out.println("ClientsMagasinIntegrationTest.should_retrive_client(" + new Gson().toJson(client));
        assertThat(client).isNotNull();
    }
}