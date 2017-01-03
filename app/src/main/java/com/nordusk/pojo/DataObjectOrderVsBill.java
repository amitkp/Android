package com.nordusk.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NeeloyG on 12/22/2016.
 */

public class DataObjectOrderVsBill {
    private String totalPrice="";
    private String date="";
    private String name="";
    private DataObjectOrderVsBillDetails details;
    private List<DataObjectOrderVsBillDetails> listDetails;

    public DataObjectOrderVsBill(){
        if(listDetails == null){
            listDetails = new ArrayList<DataObjectOrderVsBillDetails>();
        }
    }

    public void setListDetails(DataObjectOrderVsBillDetails details){
        listDetails.add(details);
    }

    public List<DataObjectOrderVsBillDetails> getListDetails(){
        return listDetails;
    }
    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
