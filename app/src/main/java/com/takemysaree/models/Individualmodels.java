package com.takemysaree.models;

import java.io.Serializable;

/**
 * Created by think360user on 24/11/17.
 */

public class Individualmodels implements Serializable {
    private String id;
    private String name;
    private String ads;
    private String in_stock;
    private String on_rent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAds() {
        return ads;
    }

    public void setAds(String ads) {
        this.ads = ads;
    }

    public String getIn_stock() {
        return in_stock;
    }

    public void setIn_stock(String in_stock) {
        this.in_stock = in_stock;
    }

    public String getOn_rent() {
        return on_rent;
    }

    public void setOn_rent(String on_rent) {
        this.on_rent = on_rent;
    }

    public String getIs_multi() {
        return is_multi;
    }

    public void setIs_multi(String is_multi) {
        this.is_multi = is_multi;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String is_multi;
    private String image;


}