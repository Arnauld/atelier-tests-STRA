package com.strator.communaute.client.repository;

import com.strator.communaute.client.model.Client;
import com.strator.communaute.data.IDataBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Un stub renvoyant des clients prédéfinis
 */
@Repository
public class StubClientsMagasin implements ClientsMagasin{

    @Autowired
    private IDataBase database;

    @Override
    public Client retrieveByEmail(String email) {
        return database.getClient(email);
    }

}
