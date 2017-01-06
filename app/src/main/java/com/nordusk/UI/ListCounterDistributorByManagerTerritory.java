package com.nordusk.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.nordusk.R;
import com.nordusk.adapter.CounterDistributorListAdapter;
import com.nordusk.adapter.CounterDistributorListAdapterByManagerTerritory;
import com.nordusk.pojo.DataDistributor;
import com.nordusk.utility.Prefs;
import com.nordusk.webservices.HttpConnectionUrl;
import com.nordusk.webservices.ListContractorDistributorAsync;
import com.nordusk.webservices.ListCounterDistributorByManagerTerritoryAsync;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ListCounterDistributorByManagerTerritory extends AppCompatActivity {

    private ListView list_counter_dis;
    private Prefs mpPrefs;
    private String type = "";
    private String sp_id, territory_id = "";
    private CounterDistributorListAdapterByManagerTerritory counterDistributorListAdapterByManagerTerritory;
    private TextView txt_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_counter_distributor);
        initView();
        populateData();
    }


    private void populateData() {
        if (HttpConnectionUrl.isNetworkAvailable(ListCounterDistributorByManagerTerritory.this)) {
            ListCounterDistributorByManagerTerritoryAsync listCounterDistributorByManagerTerritory=new ListCounterDistributorByManagerTerritoryAsync(this,sp_id,type,territory_id);
            listCounterDistributorByManagerTerritory.setOnContentListParserListner(new ListCounterDistributorByManagerTerritoryAsync.OnContentListSchedules() {
                @Override
                public void OnSuccess(ArrayList<com.nordusk.webservices.List> arrayList, String total) {
                    if(total!=null && total.length()>0)
                        txt_total.setText("Total Count :"+total);

                    if(arrayList!=null && arrayList.size()>0){
                        counterDistributorListAdapterByManagerTerritory=new CounterDistributorListAdapterByManagerTerritory(ListCounterDistributorByManagerTerritory.this,arrayList,type);
                        list_counter_dis.setAdapter(counterDistributorListAdapterByManagerTerritory);
                    }
                }

                @Override
                public void OnError(String str_err) {
                    Toast.makeText(ListCounterDistributorByManagerTerritory.this, str_err, Toast.LENGTH_SHORT).show();
                    list_counter_dis.setAdapter(null);
                }

                @Override
                public void OnConnectTimeout() {
                    Toast.makeText(ListCounterDistributorByManagerTerritory.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
                }

            });
            listCounterDistributorByManagerTerritory.execute();

        } else {
            Toast.makeText(ListCounterDistributorByManagerTerritory.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
        }


    }

    private void initView() {

        txt_total = (TextView) findViewById(R.id.txt_total);
        list_counter_dis = (ListView) findViewById(R.id.listView_counr_dis);
        counterDistributorListAdapterByManagerTerritory=new CounterDistributorListAdapterByManagerTerritory(this,new ArrayList<com.nordusk.webservices.List>(),type);
        list_counter_dis.setAdapter(counterDistributorListAdapterByManagerTerritory);

        if (getIntent().getStringExtra("sp_id") != null && getIntent().getStringExtra("sp_id").length() > 0)
            sp_id = getIntent().getStringExtra("sp_id");
        if (getIntent().getStringExtra("territory_id") != null && getIntent().getStringExtra("territory_id").length() > 0)
            territory_id = getIntent().getStringExtra("territory_id");
        if (getIntent().getStringExtra("type") != null && getIntent().getStringExtra("type").length() > 0)
            type = getIntent().getStringExtra("type");
    }
}
