package com.nordusk.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by gouravkundu on 11/12/16.
 */

public class DataOrderItem {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("item_id")
    @Expose
    private String itemId;
    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("item_price")
    @Expose
    private String itemPrice;
    @SerializedName("item_quantity")
    @Expose
    private String itemQuantity;
    @SerializedName("approved_quantity")
    @Expose
    private String approvedQuantity;

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The orderId
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     *
     * @param orderId
     * The order_id
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     *
     * @return
     * The itemId
     */
    public String getItemId() {
        return itemId;
    }

    /**
     *
     * @param itemId
     * The item_id
     */
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    /**
     *
     * @return
     * The itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     *
     * @param itemName
     * The item_name
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     *
     * @return
     * The itemPrice
     */
    public String getItemPrice() {
        return itemPrice;
    }

    /**
     *
     * @param itemPrice
     * The item_price
     */
    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    /**
     *
     * @return
     * The itemQuantity
     */
    public String getItemQuantity() {
        return itemQuantity;
    }

    /**
     *
     * @param itemQuantity
     * The item_quantity
     */
    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    /**
     *
     * @return
     * The approvedQuantity
     */
    public String getApprovedQuantity() {
        return approvedQuantity;
    }

    /**
     *
     * @param approvedQuantity
     * The approved_quantity
     */
    public void setApprovedQuantity(String approvedQuantity) {
        this.approvedQuantity = approvedQuantity;
    }

}
