package com.nordusk.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by gouravkundu on 11/12/16.
 */

public class DataDistributor implements Serializable{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("territory")
    @Expose
    private String territory;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("anniversary")
    @Expose
    private String anniversary;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("alternative_mobile")
    @Expose
    private String alternativeMobile;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("parrent_id")
    @Expose
    private String parrentId;
    @SerializedName("bank_name")
    @Expose
    private String bankName;
    @SerializedName("account_no")
    @Expose
    private String accountNo;
    @SerializedName("ifsc_code")
    @Expose
    private String ifscCode;
    @SerializedName("counter_size")
    @Expose
    private String counterSize;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

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
     * @return The type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type
     */
    public void setType(String type) {
        this.type = type;
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
     * @return The address
     */
    public String getAddress() {
        if (address != null) {
            return address;
        } else {
            return "";
        }
    }

    /**
     * @param address The address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return The territory
     */
    public String getTerritory() {
        return territory;
    }

    /**
     * @param territory The territory
     */
    public void setTerritory(String territory) {
        this.territory = territory;
    }

    /**
     * @return The dob
     */
    public String getDob() {
        return dob;
    }

    /**
     * @param dob The dob
     */
    public void setDob(String dob) {
        this.dob = dob;
    }

    /**
     * @return The anniversary
     */
    public String getAnniversary() {
        return anniversary;
    }

    /**
     * @param anniversary The anniversary
     */
    public void setAnniversary(String anniversary) {
        this.anniversary = anniversary;
    }

    /**
     * @return The mobile
     */
    public String getMobile() {
        if (mobile != null) {
            return mobile;
        } else {
            return "";
        }
    }

    /**
     * @param mobile The mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return The alternativeMobile
     */
    public String getAlternativeMobile() {
        return alternativeMobile;
    }

    /**
     * @param alternativeMobile The alternative_mobile
     */
    public void setAlternativeMobile(String alternativeMobile) {
        this.alternativeMobile = alternativeMobile;
    }

    /**
     * @return The email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return The latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * @param latitude The latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * @return The longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * @param longitude The longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * @return The createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy The created_by
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return The parrentId
     */
    public String getParrentId() {
        return parrentId;
    }

    /**
     * @param parrentId The parrent_id
     */
    public void setParrentId(String parrentId) {
        this.parrentId = parrentId;
    }

    /**
     * @return The bankName
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * @param bankName The bank_name
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    /**
     * @return The accountNo
     */
    public String getAccountNo() {
        return accountNo;
    }

    /**
     * @param accountNo The account_no
     */
    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    /**
     * @return The ifscCode
     */
    public String getIfscCode() {
        return ifscCode;
    }

    /**
     * @param ifscCode The ifsc_code
     */
    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    /**
     * @return The counterSize
     */
    public String getCounterSize() {
        return counterSize;
    }

    /**
     * @param counterSize The counter_size
     */
    public void setCounterSize(String counterSize) {
        this.counterSize = counterSize;
    }

    /**
     * @return The image
     */
    public String getImage() {
        if (image != null) {
            return image;
        } else {
            return "";
        }
    }


    /**
     * @param image The image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * @return The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @param updatedAt The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
