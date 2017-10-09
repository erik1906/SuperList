package com.company.erde.superlist.RealModels;

import java.security.PrivateKey;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Erik on 08/10/2017.
 */

public class Product extends RealmObject {

    @PrimaryKey
    private int id;
    private String name;
    private String photoUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
