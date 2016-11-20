package com.nordusk.UI;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.nordusk.R;
import com.nordusk.utility.Prefs;
import com.nordusk.webservices.HttpConnectionUrl;
import com.nordusk.webservices.List;
import com.nordusk.webservices.ListContractorDistributorAsync;

import java.util.ArrayList;

public class MapsActivityContractorDistributor extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Prefs mpPrefs;
    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

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
        mpPrefs = new Prefs(this);
        String userId = "";
        String designation = "";
        userId = mpPrefs.getString("userid", "");
        if (getIntent().getStringExtra("type") != null && getIntent().getStringExtra("type").length() > 0)
            type = getIntent().getStringExtra("type");
        designation = "1";
        populateData(userId, type, designation);
        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(-34, 151);
        // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


    }

    private void populateData(String userId, String type, String desig) {

        if (HttpConnectionUrl.isNetworkAvailable(MapsActivityContractorDistributor.this)) {
            ListContractorDistributorAsync lisLtContractorDistributorAsync = new ListContractorDistributorAsync(MapsActivityContractorDistributor.this, userId, type, desig);
            lisLtContractorDistributorAsync.setOnContentListParserListner(new ListContractorDistributorAsync.OnContentListSchedules() {
                @Override
                public void OnSuccess(ArrayList<List> arrayList) {
                    if (arrayList != null && arrayList.size() > 0) {

                        for (int i = 0; i < arrayList.size(); i++) {
                            if (arrayList.get(i).getLatitude() != null && arrayList.get(i).getLatitude().length() > 0
                                    && arrayList.get(i).getLongitude() != null && arrayList.get(i).getLongitude().length() > 0) {
                                double lat = 0;
                                double longi = 0;
                                lat = Double.parseDouble(arrayList.get(i).getLatitude());
                                longi = Double.parseDouble(arrayList.get(i).getLongitude());
                                LatLng latLng = new LatLng(lat, longi);
                                String title = "";
                                if (arrayList.get(i).getName() != null && arrayList.get(i).getAddress() != null) {
                                    if (arrayList.get(i).getTerritory() != null)
                                        title = arrayList.get(i).getName() + "," + arrayList.get(i).getAddress() + "," + arrayList.get(i).getTerritory();
                                    else
                                        title = arrayList.get(i).getName() + "," + arrayList.get(i).getAddress();

                                }

                                mMap.addMarker(new MarkerOptions().position(latLng).title(title));
                            }
                        }
                        double lat = 0;
                        double longi = 0;
                        int size=arrayList.size();
                        lat = Double.parseDouble(arrayList.get(size-1).getLatitude());
                        longi = Double.parseDouble(arrayList.get(size-1).getLongitude());
                        LatLng latLng = new LatLng(lat, longi);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(5));

                    } else {
                        Toast.makeText(MapsActivityContractorDistributor.this, "No shops found", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void OnError(String str_err) {

                }

                @Override
                public void OnConnectTimeout() {

                }
            });
            lisLtContractorDistributorAsync.execute();
        } else {
            Toast.makeText(MapsActivityContractorDistributor.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
        }

    }


}
