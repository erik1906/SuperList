package com.company.erde.superlist.Realm;

import com.company.erde.superlist.RealModels.History;
import com.company.erde.superlist.RealModels.ListProducts;
import com.company.erde.superlist.RealModels.Product;
import com.company.erde.superlist.RealModels.SuperList;

import java.util.Calendar;
import java.util.Date;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Erik on 13/10/2017.
 */

public class HistoryCRUD {


    public static void dropTable (Realm realm){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(History.class);
            }
        });
    }
    public static void insert(Realm realm, final SuperList superList){

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                History history = new History();
                android.text.format.DateFormat df = new android.text.format.DateFormat();
                String date = (String) df.format("yyyy-MM-dd", new Date());
//comment

                history.setName(superList.getName());
                history.setProducts(superList.getProducts());
                history.setId(superList.getId());
                history.setTotal(superList.getTotal());
                history.setDate(date);
                history.setProductCount(superList.getProductCount());
                realm.copyToRealmOrUpdate(history);
                        //dgdfgsdfgsdg
                // as/
                // /dfdfssg
                //rdghd
            }
        });
    }




    public static History select (Realm realm, int id){

        return realm.where(History.class).equalTo("id",id).findFirst();
    }

    public static RealmResults selectResult (Realm realm, int id){

        return realm.where(History.class).equalTo("id",id).findAll();
    }

    public static void delete (Realm realm, final int id) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                selectResult(realm, id).deleteAllFromRealm();
            }
        });
    }

    public static void update(Realm realm, final History superList){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(superList);
            }
        });
    }
    public static void updateTotal(Realm realm, final History superList, final float total){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                superList.setTotal(total);
                realm.copyToRealmOrUpdate(superList);
            }
        });
    }

    public static RealmResults selectAll(Realm realm){
        return  realm.where(History.class).findAll();

    }
    public static History selectFirst(Realm realm){
        return  realm.where(History.class).findFirst();

    }

    public static OrderedRealmCollection<ListProducts> orderedRealmCollectionProduct(Realm realm, int id){
        return  select(realm,id).getProducts().where().equalTo("listId",id).findAll();

    }
    public static OrderedRealmCollection<History> orderedRealmCollection(Realm realm){
        return realm.where(History.class).findAll();

    }



}
