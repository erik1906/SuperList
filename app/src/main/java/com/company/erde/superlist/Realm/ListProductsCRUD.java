package com.company.erde.superlist.Realm;

import com.company.erde.superlist.RealModels.ListProducts;
import com.company.erde.superlist.RealModels.Product;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Erik on 08/10/2017.
 */

public class ListProductsCRUD {

    public static void dropTable (Realm realm){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(ListProducts.class);
            }
        });
    }
    public static void insert(Realm realm, final ListProducts listProducts){

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(listProducts);
            }
        });
    }


    public static ListProducts select (Realm realm, int id){

        return realm.where(ListProducts.class).equalTo("id",id).findFirst();
    }
    public static RealmResults selectResult (Realm realm, int id){

        return realm.where(ListProducts.class).equalTo("id",id).findAll();
    }

    public static void delete (Realm realm, final int id) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                selectResult(realm, id).deleteAllFromRealm();
            }
        });
    }

    public static void update(Realm realm, final ListProducts listProducts){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(listProducts);
            }
        });
    }

    public static RealmResults selectAll(Realm realm){
        return  realm.where(ListProducts.class).findAll();

    }
    public static ListProducts selectFirst(Realm realm){
        return  realm.where(ListProducts.class).findFirst();

    }

    public static OrderedRealmCollection<ListProducts> orderedRealmCollection(Realm realm){
        return  realm.where(ListProducts.class).findAll();

    }



}
