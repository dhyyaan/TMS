package com.takemysaree.models;

import java.io.Serializable;

/**
 * Created by think360user on 24/11/17.
 */

public class F1models implements Serializable {
    private String catid;
    private String productname;
    private String size;
    private String rentprice;

    public String getSellprice() {
        return sellprice;
    }

    public void setSellprice(String sellprice) {
        this.sellprice = sellprice;
    }

    private String sellprice;

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getRentprice() {
        return rentprice;
    }

    public void setRentprice(String rentprice) {
        this.rentprice = rentprice;
    }

    public String getRetailprice() {
        return retailprice;
    }

    public void setRetailprice(String retailprice) {
        this.retailprice = retailprice;
    }

    private String retailprice;




}