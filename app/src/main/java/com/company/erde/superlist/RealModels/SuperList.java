package com.company.erde.superlist.RealModels;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Erik on 11/10/2017.
 */

public class SuperList extends RealmObject {

    @PrimaryKey
    private int id;
    private String name;
    private float total;
    private RealmList<ListProducts> products;

    public int getId() {
        return id;
    }


    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
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

    public RealmList<ListProducts>  getProducts() {
        return products;
    }

    public void setProducts(RealmList<ListProducts> products) {
        this.products = products;
    }
}
