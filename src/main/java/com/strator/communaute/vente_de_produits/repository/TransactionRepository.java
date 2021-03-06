package com.strator.communaute.vente_de_produits.repository;

import com.strator.communaute.data.IDataBase;
import com.strator.communaute.vente_de_produits.model.Commande;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by cafetux on 12/05/2015.
 */
@Repository
public class TransactionRepository {

    @Autowired
    private IDataBase database;

    public void save(Commande operationDeVente)
    {
        database.save(operationDeVente);
        System.out.println(operationDeVente);
    }

}
