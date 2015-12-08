package mycompany.programme_fidelite;

import mycompany.data.IDataBase;
import mycompany.programme_fidelite.model.ActionRecompensee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Gere les points fidelités des utilisateurs
 */
@Repository
public class FideliteDao {

    @Autowired
    private IDataBase database;


    public void save(ActionRecompensee action){
        database.save(action);
        System.out.println("Programme fidelité ("+action.getClientEmail()+") :"+ action.getPointsGagnes()+" pts winned ("+action.getType()+")");
    }

}
