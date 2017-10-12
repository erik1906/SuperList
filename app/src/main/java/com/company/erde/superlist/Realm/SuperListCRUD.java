package com.company.erde.superlist.Realm;

import com.company.erde.superlist.RealModels.SuperList;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Erik on 11/10/2017.
 */

public class SuperListCRUD {

    public static void dropTable (Realm realm){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(SuperList.class);
            }
        });
    }
    public static void insert(Realm realm, final SuperList product){

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Number currentIdNum = realm.where(SuperList.class).max("id");
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

    public static SuperList select (Realm realm, int id){

        return realm.where(SuperList.class).equalTo("id",id).findFirst();
    }
    
    public static RealmResults selectResult (Realm realm, int id){

        return realm.where(SuperList.class).equalTo("id",id).findAll();
    }

    public static void delete (Realm realm, final int id) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                selectResult(realm, id).deleteAllFromRealm();
            }
        });
    }

    public static void update(Realm realm, final SuperList superList){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(superList);
            }
        });
    }

    public static RealmResults selectAll(Realm realm){
        return  realm.where(SuperList.class).findAll();

    }
    public static SuperList selectFirst(Realm realm){
        return  realm.where(SuperList.class).findFirst();

    }

    public static OrderedRealmCollection<SuperList> orderedRealmCollection(Realm realm){
        return  realm.where(SuperList.class).findAll();

    }
}
