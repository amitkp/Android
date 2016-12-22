package com.nordusk.pojo;

/**
 * Created by NeeloyG on 12/22/2016.
 */

public class DataObjectAttendance {
    private String date="";

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

    private String login_time="";
    private String logout_time="";

}
