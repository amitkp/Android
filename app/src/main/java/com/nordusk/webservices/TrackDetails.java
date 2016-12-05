package com.nordusk.webservices;

import java.util.ArrayList;

/**
 * Created by DELL on 04-12-2016.
 */
public class TrackDetails {


    private ArrayList<CounterSet> arr_counterset = new ArrayList<CounterSet>();
    private ArrayList<PointsTraceList> arr_pointsTraceLists = new ArrayList<PointsTraceList>();

    public ArrayList<PointsTraceList> getArr_pointsTraceLists() {
        return arr_pointsTraceLists;
    }

    public void setArr_pointsTraceLists(ArrayList<PointsTraceList> arr_pointsTraceLists) {
        this.arr_pointsTraceLists = arr_pointsTraceLists;
    }


    public ArrayList<CounterSet> getArr_counterset() {
        return arr_counterset;
    }

    public void setArr_counterset(ArrayList<CounterSet> arr_counterset) {
        this.arr_counterset = arr_counterset;
    }


}
