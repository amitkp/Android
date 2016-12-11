package com.nordusk.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gouravkundu on 11/12/16.
 */

public class DataOrder {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("total_price")
    @Expose
    private String totalPrice;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("items")
    @Expose
    private List<DataOrderItem> items = null;

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The totalPrice
     */
    public String getTotalPrice() {
        if (totalPrice != null) {
            return totalPrice;
        } else {
            return "";
        }
    }

    /**
     * @param totalPrice The total_price
     */
    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * @return The dateTime
     */
    public String getDateTime() {
        if (dateTime != null) {
            return dateTime;
        } else {
            return "";
        }
    }

    /**
     * @param dateTime The date_time
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * @return The name
     */
    public String getName() {
        if (name != null) {
            return name;
        } else {
            return "";
        }
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The items
     */
    public List<DataOrderItem> getItems() {
        if (items != null) {
            return items;
        }
        else {
            return new ArrayList<DataOrderItem>();
        }
    }

    /**
     * @param items The items
     */
    public void setItems(List<DataOrderItem> items) {
        this.items = items;
    }

}
