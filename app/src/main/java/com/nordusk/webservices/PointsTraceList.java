package com.nordusk.webservices;

import java.util.ArrayList;

/**
 * Created by NeeloyG on 11/20/2016.
 */
public class PointsTraceList {
    private String id;

    private String longitude;

    private String user_id;

    private String latitude;

    private String date_time;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", longitude = " + longitude + ", user_id = " + user_id + ", latitude = " + latitude + ", date_time = " + date_time + "]";
    }
}
