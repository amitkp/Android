package com.nordusk.UI;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.nordusk.*;
import com.nordusk.R;
import com.nordusk.utility.Prefs;
import com.nordusk.utility.Util;
import com.nordusk.webservices.HttpConnectionUrl;
import com.nordusk.webservices.ListTraceAsync;
import com.nordusk.webservices.PointsTraceList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private PolylineOptions lineOptions = new PolylineOptions();
    private Prefs mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.nordusk.R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(-34, 151);
        // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        LatLng position1 = new LatLng(22.590927, 88.415898);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(position1));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//
//        LatLng position2 = new LatLng(22.591230, 88.419063);
//        LatLng position3 = new LatLng(22.587030, 88.422346);
//        LatLng position4 = new LatLng(22.576668, 88.434491);
//
//
//        points.add(position1);
//        points.add(position2);
//        points.add(position3);
//        points.add(position4);
//        lineOptions.addAll(points);
//        if (LocationUpdateService.points != null && LocationUpdateService.points.size() > 0) {
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(LocationUpdateService.points.get(0)));
//            for (int i = 0; i < LocationUpdateService.points.size(); i++) {
//                lineOptions.add(LocationUpdateService.points.get(i));
//            }
//        }
        mPrefs=new Prefs(MapsActivity.this);
        String date="";
        date= Util.getCurrentDate();
        populateData(mPrefs.getString("mobile_no",""),date);


    }

    private void populateData(String mobile, String date) {
        if (HttpConnectionUrl.isNetworkAvailable(MapsActivity.this)) {
            ListTraceAsync listTraceAsync = new ListTraceAsync(MapsActivity.this, mobile, date);
            listTraceAsync.setOnContentListParserListner(new ListTraceAsync.OnContentListSchedules() {
                @Override
                public void OnSuccess(ArrayList<PointsTraceList> arrayList) {
                    if (arrayList != null && arrayList.size() > 0) {
                        for (int i = 0; i < arrayList.size(); i++) {
                            if (arrayList.get(i).getLatitude() != null && arrayList.get(i).getLongitude() != null) {
                                double lat = 0;
                                double longi = 0;
                                int size = arrayList.size();
                                lat = Double.parseDouble(arrayList.get(i).getLatitude());
                                longi = Double.parseDouble(arrayList.get(i).getLongitude());
                                LatLng latLng = new LatLng(lat, longi);
                                if(i==0)
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                lineOptions.add(latLng);
                            }

                        }

                        lineOptions.width(5);
                        lineOptions.color(Color.RED);
                        if (lineOptions != null) {
                            mMap.addPolyline(lineOptions);
                        }

                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

                    } else {
                        Toast.makeText(MapsActivity.this, "No activity found today", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void OnError(String str_err) {

                }

                @Override
                public void OnConnectTimeout() {

                }
            });
            listTraceAsync.execute();

        } else {
            Toast.makeText(MapsActivity.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
        }


    }


}
