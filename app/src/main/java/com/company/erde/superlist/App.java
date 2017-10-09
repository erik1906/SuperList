package com.company.erde.superlist;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Erik on 08/10/2017.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

    }
}
