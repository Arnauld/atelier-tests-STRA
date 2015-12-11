package mycompany.pk.dao;

import mycompany.pk.model.CategorieProduit;
import mycompany.pk.model.ProduitCatalogue;
import mycompany.pk.utils.commerce.Prix;
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
public class ProduitCatalogueSQL {
    public static class SQLMapper implements ResultSetMapper<ProduitCatalogue> {
        public ProduitCatalogue map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
            return new ProduitCatalogue(
                    resultSet.getString("reference"),
                    resultSet.getString("libelle"),
                    new Prix(resultSet.getLong("prixAchat")),
                    resultSet.getBoolean("actif"),
                    new Prix(resultSet.getLong("marge")),
                    CategorieProduit.valueOf(resultSet.getString("categorieProduit")));
        }
    }

    @BindingAnnotation(SQLBinder.SQLBinderFactory.class)
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER})
    public @interface SQLBinder {
        public static class SQLBinderFactory implements BinderFactory {
            public Binder build(Annotation annotation) {
                return new Binder<SQLBinder, ProduitCatalogue>() {
                    public void bind(SQLStatement q, SQLBinder bind, ProduitCatalogue produit) {
                        q.bind("reference", produit.getReference());
                        q.bind("libelle", produit.getLibelle());
                        q.bind("prixAchat", produit.getPrixAchat().toCents());
                        q.bind("actif", produit.isActif());
                        q.bind("marge", produit.getMarge().toCents());
                        q.bind("categorieProduit", produit.getCategorieProduit().name());
                    }
                };
            }
        }
    }
}
