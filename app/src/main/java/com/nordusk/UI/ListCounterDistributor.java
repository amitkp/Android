package com.nordusk.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.nordusk.R;
import com.nordusk.adapter.CounterDistributorListAdapter;
import com.nordusk.pojo.DataDistributor;
import com.nordusk.utility.Prefs;
import com.nordusk.webservices.HttpConnectionUrl;
import com.nordusk.webservices.ListContractorDistributorAsync;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ListCounterDistributor extends AppCompatActivity {

    private ListView list_counter_dis;
    private Prefs mpPrefs;
    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_counter_distributor);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateDataFetch();
    }

    private void populateDataFetch() {
        mpPrefs = new Prefs(this);
        String userId = "";
        String designation = "";
        userId = mpPrefs.getString("userid", "");
        if (getIntent().getStringExtra("type") != null && getIntent().getStringExtra("type").length() > 0)
            type = getIntent().getStringExtra("type");
        designation = "1";

        populateData(userId, type, designation);
    }

    private void populateData(String userId, final String type, String designation) {        if (HttpConnectionUrl.isNetworkAvailable(ListCounterDistributor.this)) {
        ListContractorDistributorAsync lisLtContractorDistributorAsync = new ListContractorDistributorAsync(ListCounterDistributor.this, userId, type, designation);
        lisLtContractorDistributorAsync.setOnContentListParserListner(new ListContractorDistributorAsync.OnContentListSchedules() {

            @Override
            public void onResponseData(String response) {
                Log.i(getClass().getSimpleName(), "onResponseData: " + response);
                JsonElement jelement = new JsonParser().parse(response);
                JsonObject mJson = jelement.getAsJsonObject();
                JsonArray mNewArray = mJson.getAsJsonArray("list");
                Gson mGson = new Gson();
                Type listType = new TypeToken<List<DataDistributor>>() {
                }.getType();
                ArrayList<DataDistributor> mListDistributor = mGson.fromJson(mNewArray.toString(), listType);
                Log.i(getClass().getSimpleName(), "onResponseData: " + mListDistributor.size());

                CounterDistributorListAdapter counterDistributorListAdapter=new CounterDistributorListAdapter(ListCounterDistributor.this,mListDistributor,type);
                list_counter_dis.setAdapter(counterDistributorListAdapter);


            }

            @Override
            public void OnSuccess(ArrayList<com.nordusk.webservices.List> arrayList) {



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
                Toast.makeText(ListCounterDistributor.this, str_err, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnConnectTimeout() {

            }
        });
        lisLtContractorDistributorAsync.execute();
    } else {
        Toast.makeText(ListCounterDistributor.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
    }




    }

    private void initView() {

        list_counter_dis=(ListView)findViewById(R.id.listView_counr_dis);
    }
}
