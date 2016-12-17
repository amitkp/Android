package com.nordusk.UI;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.nordusk.R;
import com.nordusk.adapter.AdapterMarker;
import com.nordusk.pojo.DataDistributor;
import com.nordusk.utility.Prefs;
import com.nordusk.webservices.HttpConnectionUrl;
import com.nordusk.webservices.List;
import com.nordusk.webservices.ListContractorDistributorAsync;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MapsActivityContractorDistributor extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Prefs mpPrefs;
    private String type = "";

    ArrayList<DataDistributor> mList = new ArrayList<DataDistributor>();
    LatLngBounds.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        builder = new LatLngBounds.Builder();
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
        initMarkerAdapter();
        populateData(userId, type, designation);
        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(-34, 151);
        // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


    }

    private void initMarkerAdapter() {
        mMap.setInfoWindowAdapter(new AdapterMarker(getBaseContext(), mList));
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String title = marker.getTitle();
                Gson mGson = new Gson();
                DataDistributor mData = mGson.fromJson(title, DataDistributor.class);
                marker.hideInfoWindow();
                Log.i(getClass().getSimpleName(), "onInfoWindowClick: " + mData.getAddress());
            }
        });
    }

    private void populateData(String userId, String type, String desig) {

        if (HttpConnectionUrl.isNetworkAvailable(MapsActivityContractorDistributor.this)) {
            ListContractorDistributorAsync lisLtContractorDistributorAsync = new ListContractorDistributorAsync(MapsActivityContractorDistributor.this, userId, type, desig);
            lisLtContractorDistributorAsync.setOnContentListParserListner(new ListContractorDistributorAsync.OnContentListSchedules() {

                @Override
                public void onResponseData(String response) {
                    Log.i(getClass().getSimpleName(), "onResponseData: " + response);
                    JsonElement jelement = new JsonParser().parse(response);
                    JsonObject mJson = jelement.getAsJsonObject();
                    JsonArray mNewArray = mJson.getAsJsonArray("list");
                    Gson mGson = new Gson();
                    Type listType = new TypeToken<java.util.List<DataDistributor>>() {
                    }.getType();
                    ArrayList<DataDistributor> mListDistributor = mGson.fromJson(mNewArray.toString(), listType);
                    Log.i(getClass().getSimpleName(), "onResponseData: " + mListDistributor.size());

                    updateMarkerUi(mListDistributor);
                }

                @Override
                public void OnSuccess(ArrayList<List> arrayList) {
//                    if (arrayList != null && arrayList.size() > 0) {
//
//                        for (int i = 0; i < arrayList.size(); i++) {
//                            if (arrayList.get(i).getLatitude() != null && arrayList.get(i).getLatitude().length() > 0
//                                    && arrayList.get(i).getLongitude() != null && arrayList.get(i).getLongitude().length() > 0) {
//                                double lat = 0;
//                                double longi = 0;
//                                lat = Double.parseDouble(arrayList.get(i).getLatitude());
//                                longi = Double.parseDouble(arrayList.get(i).getLongitude());
//                                LatLng latLng = new LatLng(lat, longi);
//                                String title = "";
//                                if (arrayList.get(i).getName() != null && arrayList.get(i).getAddress() != null) {
//                                    if (arrayList.get(i).getTerritory() != null)
//                                        title = arrayList.get(i).getName() + "," + arrayList.get(i).getAddress() + "," + arrayList.get(i).getTerritory();
//                                    else
//                                        title = arrayList.get(i).getName() + "," + arrayList.get(i).getAddress();
//
//                                }
//
//                                mMap.addMarker(new MarkerOptions().position(latLng).title(title));
//                            }
//                        }
//                        double lat = 0;
//                        double longi = 0;
//                        int size = arrayList.size();
//                        lat = Double.parseDouble(arrayList.get(size - 1).getLatitude());
//                        longi = Double.parseDouble(arrayList.get(size - 1).getLongitude());
//                        LatLng latLng = new LatLng(lat, longi);
//                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//                        mMap.animateCamera(CameraUpdateFactory.zoomTo(5));
//
//                    } else {
//                        Toast.makeText(MapsActivityContractorDistributor.this, "No shops found", Toast.LENGTH_SHORT).show();
//                    }

                }

                @Override
                public void OnError(String str_err) {
                    Toast.makeText(MapsActivityContractorDistributor.this, str_err, Toast.LENGTH_SHORT).show();
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

    private void updateMarkerUi(ArrayList<DataDistributor> mListResponse) {
        Gson mGson = new GsonBuilder().setPrettyPrinting().create();

        this.mList = mListResponse;
        for (int i = 0; i < mList.size(); i++) {
            if(mList.get(i).getLatitude()!=null && mList.get(i).getLatitude().length()>0
                    && mList.get(i).getLongitude()!=null && mList.get(i).getLongitude().length()>0) {
                LatLng mLatLng = new LatLng((Double.parseDouble(mList.get(i).getLatitude())),
                        Double.parseDouble(mList.get(i).getLongitude()));

                Marker marker = mMap.addMarker(new MarkerOptions().position(mLatLng).
                        icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_point)));
                String json = mGson.toJson(mList.get(i));
                marker.setTitle(json);
                marker.showInfoWindow();
                builder.include(marker.getPosition());
            }
        }

        if(builder!=null) {
            LatLngBounds bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
            mMap.moveCamera(cu);
            mMap.animateCamera(cu);
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                @Override
                public boolean onMarkerClick(Marker arg0) {
                    if (arg0.isInfoWindowShown()) {
                        arg0.hideInfoWindow();
                    } else {
                        arg0.showInfoWindow();
                    }
                    return true;
                }
            });
        }
    }


}
