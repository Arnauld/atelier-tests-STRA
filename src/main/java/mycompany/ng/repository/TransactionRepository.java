package mycompany.ng.repository;

import mycompany.ng.model.Commande;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by cafetux on 12/05/2015.
 */
@Repository
public class TransactionRepository {

    @Autowired
    private CommandeDao database;

    public void save(Commande operationDeVente)
    {
        database.save(operationDeVente);
        System.out.println(operationDeVente);
    }

}
