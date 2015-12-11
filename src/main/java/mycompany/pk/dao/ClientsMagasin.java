package mycompany.pk.dao;

import mycompany.pk.model.Client;
import mycompany.data.db.DB;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

/**
 * Manipule les clients du Magasin
 */
public class ClientsMagasin {

    public static Client retrieveByEmail(String email) {
        DB db = DB.getInstance().connect();
        ClientDao dao = db.open(ClientDao.class);
        try {
            return dao.get(email);
        } finally {
            dao.close();
        }
    }

    @RegisterMapper(ClientSQL.SQLMapper.class)
    public interface ClientDao {
        @SqlQuery("select * from clients where email = :email")
        Client get(@Bind("email") String email);

        /**
         * close with no args is used to close the connection
         */
        void close();
    }

}
