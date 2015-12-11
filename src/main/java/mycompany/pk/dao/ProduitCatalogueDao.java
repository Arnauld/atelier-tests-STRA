package mycompany.pk.dao;

import mycompany.pk.model.ProduitCatalogue;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

/**
 * Le catalogue de produits
 */
@RegisterMapper(ProduitCatalogueSQL.SQLMapper.class)
public interface ProduitCatalogueDao {

    @SqlUpdate("INSERT INTO produit_catalogues (reference, libelle, prixAchat, marge, categorieProduit, actif)\n" +
            "VALUES (:reference, :libelle, :prixAchat, :marge, :categorieProduit, :actif)")
    void insert(@ProduitCatalogueSQL.SQLBinder ProduitCatalogue produit);

    /**
     * Retrouve un produit du catalogue en fonction de sa reference
     * @param reference la reference du produit à retrouver
     * @return le produit catalogue correspondant à la reference
     */
    @SqlQuery("select * from produit_catalogues where reference = :reference")
    ProduitCatalogue get(@Bind("reference") String reference);

    /**
     * Permet de retrouver tout le catalogue
     * @return la liste des produits du catalogue
     */
    @SqlQuery("select * from produit_catalogues")
    List<ProduitCatalogue> retrieveAll();

    /**
     * close with no args is used to close the connection
     */
    void close();

}
