package com.company.erde.superlist.Realm;

import com.company.erde.superlist.RealModels.Product;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Erik on 08/10/2017.
 */

public class ProductCRUD {

    public static void dropTable (Realm realm){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(Product.class);
            }
        });
    }
    public static void insert(Realm realm, final Product product){

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Number currentIdNum = realm.where(Product.class).max("id");
                int nextId;
                if(currentIdNum == null) {
                    nextId = 1;
                } else {
                    nextId = currentIdNum.intValue() + 1;
                }
                product.setId(nextId);
                realm.copyToRealm(product);
            }
        });
    }
   /* public static void insert2(Realm realm, final int id){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Product product = new Product();
                product.setId(id);
                product.setName("Cantsun");
                product.setPhotoUrl("no hay");
                product.setPrice(13.00f);

                realm.copyToRealm(product);
            }
        });
    }*/

    public static Product select (Realm realm, int id){

        return realm.where(Product.class).equalTo("id",id).findFirst();
    }
    public static RealmResults selectResult (Realm realm, int id){

        return realm.where(Product.class).equalTo("id",id).findAll();
    }

    public static void delete (Realm realm, final int id) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                selectResult(realm, id).deleteAllFromRealm();
            }
        });
    }

    public static void update(Realm realm, final Product product){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(product);
            }
        });
    }

    public static RealmResults selectAll(Realm realm){
        return  realm.where(Product.class).findAll();

    }
    public static Product selectFirst(Realm realm){
        return  realm.where(Product.class).findFirst();

    }

    public static OrderedRealmCollection<Product> orderedRealmCollection(Realm realm){
        return  realm.where(Product.class).findAll();

    }


}
