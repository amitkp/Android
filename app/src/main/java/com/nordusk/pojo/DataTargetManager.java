package com.nordusk.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by gouravkundu on 16/12/16.
 */

public class DataTargetManager {

    private String id;
    private String manager_Id;
    private String sp_Id;
    private String month;
    private String amount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getManager_Id() {
        return manager_Id;
    }

    public void setManager_Id(String manager_Id) {
        this.manager_Id = manager_Id;
    }

    public String getSp_Id() {
        return sp_Id;
    }

    public void setSp_Id(String sp_Id) {
        this.sp_Id = sp_Id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getTarget_achived() {
        return target_achived;
    }

    public void setTarget_achived(String target_achived) {
        this.target_achived = target_achived;
    }

    private String created_at;
    private String target_achived;


}
