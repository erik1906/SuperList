package com.company.erde.superlist.Real;

import android.util.Log;

import com.company.erde.superlist.RealModels.Product;

import io.realm.Realm;

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

                realm.copyToRealm(product);
            }
        });
    }

    public static void read(Realm realm){
        final Product product = realm.where(Product.class).findFirst();
        Log.d("producto", product.getName()+"si");
    }


}
