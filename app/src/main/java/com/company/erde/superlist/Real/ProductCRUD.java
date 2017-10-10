package com.company.erde.superlist.Real;

import android.util.Log;

import com.company.erde.superlist.RealModels.Product;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Erik on 08/10/2017.
 */

public class ProductCRUD {

    public static void insert(Realm realm){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Product product = new Product();
                product.setId(1);
                product.setName("Cantsun");
                product.setPhotoUrl("no hay");
                product.setPrice(13.00f);

                realm.copyToRealm(product);
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
