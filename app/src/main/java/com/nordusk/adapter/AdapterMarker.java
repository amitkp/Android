package com.nordusk.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.nordusk.UI.helper.ViewMarkerWindow;
import com.nordusk.pojo.DataDistributor;

import java.util.ArrayList;

/**
 * Created by gouravkundu on 11/12/16.
 */

public class AdapterMarker implements GoogleMap.InfoWindowAdapter {

    private Context mContext;
    private ArrayList<DataDistributor> mListPoints;
    private DataDistributor point;

    public AdapterMarker(Context mContext, ArrayList<DataDistributor> mListPoints) {
        this.mContext = mContext;
        this.mListPoints = mListPoints;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        ViewMarkerWindow v = new ViewMarkerWindow(mContext);
        String info = marker.getTitle();
        Gson mGson = new Gson();
        DataDistributor model = mGson.fromJson(info, DataDistributor.class);
        v.setUpView(model);
        return v;
    }

    @Override
    public View getInfoContents(Marker marker) {
//        ViewPins v = new ViewPins(mContext);
//        String info = marker.getTitle();
//        SearchPointItemModel model = new SearchPointItemModel(info);
//        v.setUpView(model);
//        return v;
        return null;
    }

    public void setData() {
        this.point = point;

    }
}