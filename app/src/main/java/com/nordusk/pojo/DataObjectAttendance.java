package com.nordusk.pojo;

/**
 * Created by NeeloyG on 12/22/2016.
 */

public class DataObjectAttendance {


    private String id="";
    private String date="";
    private String login_time="";
    private String logout_time="";
    private String login_latitude="";
    private String login_longitutde="";
    private String logout_latitude="";
    private String logout_longitude="";
    private String login_addresss;
    private String logout_address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin_addresss() {
        return login_addresss;
    }

    public void setLogin_addresss(String login_addresss) {
        this.login_addresss = login_addresss;
    }

    public String getLogout_address() {
        return logout_address;
    }

    public void setLogout_address(String logout_address) {
        this.logout_address = logout_address;
    }

    public String getLogin_latitude() {
        return login_latitude;
    }

    public void setLogin_latitude(String login_latitude) {
        this.login_latitude = login_latitude;
    }

    public String getLogin_longitutde() {
        return login_longitutde;
    }

    public void setLogin_longitutde(String login_longitutde) {
        this.login_longitutde = login_longitutde;
    }

    public String getLogout_latitude() {
        return logout_latitude;
    }

    public void setLogout_latitude(String logout_latitude) {
        this.logout_latitude = logout_latitude;
    }

    public String getLogout_longitude() {
        return logout_longitude;
    }

    public void setLogout_longitude(String logout_longitude) {
        this.logout_longitude = logout_longitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLogin_time() {
        return login_time;
    }

    public void setLogin_time(String login_time) {
        this.login_time = login_time;
    }

    public String getLogout_time() {
        return logout_time;
    }

    public void setLogout_time(String logout_time) {
        this.logout_time = logout_time;
    }



}
