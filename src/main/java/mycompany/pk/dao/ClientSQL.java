package mycompany.pk.dao;

import mycompany.pk.model.AccountType;
import mycompany.pk.model.Client;
import org.skife.jdbi.v2.SQLStatement;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.sqlobject.Binder;
import org.skife.jdbi.v2.sqlobject.BinderFactory;
import org.skife.jdbi.v2.sqlobject.BindingAnnotation;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class ClientSQL {
    public static class SQLMapper implements ResultSetMapper<Client> {
        public Client map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
            return new Client(
                    resultSet.getString("email"),
                    resultSet.getString("nom"),
                    resultSet.getString("prenom"),
                    AccountType.valueOf(resultSet.getString("accountType")),
                    resultSet.getTimestamp("activationDate"));
        }
    }

    @BindingAnnotation(SQLBinder.SQLBinderFactory.class)
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER})
    public @interface SQLBinder {
        public static class SQLBinderFactory implements BinderFactory {
            public Binder build(Annotation annotation) {
                return new Binder<SQLBinder, Client>() {
                    public void bind(SQLStatement q, SQLBinder bind, Client client) {
                        q.bind("email", client.getEmail());
                        q.bind("nom", client.getNom());
                        q.bind("prenom", client.getThePrenom());
                        q.bind("accountType", client.isActif());
                        q.bind("activationDate", client.getActivationDate());
                    }
                };
            }
        }
    }
}
