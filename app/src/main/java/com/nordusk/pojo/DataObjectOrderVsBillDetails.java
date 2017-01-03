package com.nordusk.pojo;

/**
 * Created by NeeloyG on 12/22/2016.
 */

public class DataObjectOrderVsBillDetails {

    private String name="";
    private String price="";
    private String quantity="";
    private String apprQuantity="";

    public String getApprQuantity() {
        return apprQuantity;
    }

    public void setApprQuantity(String apprQuantity) {
        this.apprQuantity = apprQuantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }


}
