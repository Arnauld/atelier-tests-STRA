package mycompany.data;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import mycompany.catalogue.model.CategorieProduit;
import mycompany.catalogue.model.ProduitCatalogue;
import mycompany.client.model.AccountType;
import mycompany.client.model.Client;
import mycompany.programme_fidelite.model.ActionRecompensee;
import mycompany.utils.commerce.Prix;
import mycompany.vente_de_produits.model.Commande;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Les données de l'appli stubbée
 */
@Repository
public class HardCodedDataBase implements IDataBase {


    private Map<String,ProduitCatalogue> produitCatalogues = Maps.newConcurrentMap();
    private Map<String,Integer> stockProduits = Maps.newConcurrentMap();
    private List<ActionRecompensee> actionsFidelite = Lists.newArrayList();
    private Map<String,Client> clients = Maps.newConcurrentMap();
    private Multimap<String,Commande> commandes = ArrayListMultimap.create();

    {

        add(produit("71e8e7e5c52a45f682cd23021931d4ea", "Durex feeling sensual x10", 1045, true, 400, PRESERVATIF),0);
        add(produit("b8ec935decba42bfb4551b357ef04be1", "Durex Play", 750, true, 150, PRESERVATIF),5);
        add(produit("fc7e3ef376054109ac34db53ec56961e", "Doliprane", 790, true, 100, MEDICAMENT_NON_REMBOURSABLE),6);
        add(produit("2ed4a086cee94a8496816d11b9790916", "Efferalgan", 860, true, 90, MEDICAMENT_NON_REMBOURSABLE),3);
        add(produit("113346dde361414fb2cdc7d41fc6805f", "Actiranox", 1050, false, 321, MEDICAMENT_REMBOURSABLE),2);
        add(produit("dde361414fb2cd113346c7341fc6805f", "Colgate fraisheur Plus", 265, false, 132, HYGIENE_DENTAIRE),10);
        add(produit("61645fb2113346dde3cdc7d41fc6805f", "Bequilles", 5050, false, 3000, EQUIPEMENT_PERSONNE_DEPENDANTE),5);
        add(produit("1fc6846dde361645fb2cd05f1133c7d4", "Se soigner avec les plantes (des pieds)", 1050, true, 400, LIVRE),2);
        add(produit("8e5cfaee57c942a8b8304bfdb68b6014", "Pastilles vichy", 1260, true, 705, CONFISERIE),9);

        clients.put(createInactifUser().getEmail(),createInactifUser());
        clients.put(createStandartClient().getEmail(),createStandartClient());
        clients.put(createVipUser().getEmail(),createVipUser());
    }

    private void add(ProduitCatalogue produit, int stock) {
        produitCatalogues.put(produit.getReference(), produit);
        stockProduits.put(produit.getReference(),stock);
    }

    private ProduitCatalogue produit(String reference, String libelle, long prixAchatCentime, boolean actif, long marge, CategorieProduit categorie) {
       return new ProduitCatalogue(reference,libelle,new Prix(prixAchatCentime),actif,marge,categorie);
    }

    @Override
    public List<ProduitCatalogue> getProduits() {
        return new ArrayList(produitCatalogues.values());
    }

    @Override
    public ProduitCatalogue getProduit(String reference) {
        return produitCatalogues.get(reference);
    }

    @Override
    public void changeStock(String referenceProduit, int newStock){
        stockProduits.put(referenceProduit,newStock);
    }

    @Override
    public Integer getStock(String reference){
        return stockProduits.get(reference);
    }

    @Override
    public Client getClient(String email){
        return clients.get(email);
    }

    @Override
    public void save(Commande commande) {
       commandes.put(commande.getClientEmail(),commande);
    }

    @Override
    public void save(ActionRecompensee action) {
        actionsFidelite.add(action);
    }


    private Client createStandartClient() {
        Client client = new Client("normal@monsite.fr","LeGrand","Paul", AccountType.STANDART, new DateTime().minusDays(8));
        return client;
    }
    private Client createVipUser() {
        Client client = new Client("platinium@monsite.fr","Lagare","Jean", AccountType.PLATINIUM, new DateTime().minusDays(4));
        return client;
    }

    private Client createInactifUser() {
        Client client = new Client("inactif@monsite.fr","Durand","Marc", AccountType.STANDART);
        return client;
    }


}
