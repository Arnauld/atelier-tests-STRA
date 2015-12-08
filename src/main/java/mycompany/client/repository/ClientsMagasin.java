package mycompany.client.repository;

import mycompany.client.model.Client;

/**
 * Manipule les clients du Magasin
 */
public interface ClientsMagasin {


    Client retrieveByEmail(String email);

}
