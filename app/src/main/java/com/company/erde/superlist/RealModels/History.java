package com.company.erde.superlist.RealModels;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Erik on 13/10/2017.
 */

public class History extends RealmObject{
    @PrimaryKey
    private int id;
    private String name;
    private String date;
    private float total;
    private int productCount;
    private RealmList<ListProducts> products;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public RealmList<ListProducts> getProducts() {
        return products;
    }

    public void setProducts(RealmList<ListProducts> products) {
        this.products = products;
    }
}
