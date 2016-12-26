package com.nordusk.admin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.nordusk.R;
import com.nordusk.adapter.CounterDistributorListAdapterByManagerTerritory;
import com.nordusk.utility.Prefs;
import com.nordusk.webservices.HttpConnectionUrl;
import com.nordusk.webservices.ListCounterDistributorByManagerTerritoryAsync;
import com.nordusk.webservices.ListCounterDistributorPrimePartnerAdminAsync;

import java.util.ArrayList;

public class ListCounterDistributorPrimePartnerAdmin extends AppCompatActivity {

    private ListView list_counter_dis;
    private Prefs mpPrefs;
    private String type = "";
    private String sp_id="";
    private CounterDistributorListAdapterByManagerTerritory counterDistributorListAdapterByManagerTerritory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_counter_distributor);
        initView();
        populateData();
    }


    private void populateData() {
        if (HttpConnectionUrl.isNetworkAvailable(ListCounterDistributorPrimePartnerAdmin.this)) {
            ListCounterDistributorPrimePartnerAdminAsync listCounterDistributorByManagerTerritory=new ListCounterDistributorPrimePartnerAdminAsync(this,sp_id,type);
            listCounterDistributorByManagerTerritory.setOnContentListParserListner(new ListCounterDistributorPrimePartnerAdminAsync.OnContentListSchedules() {
                @Override
                public void OnSuccess(ArrayList<com.nordusk.webservices.List> arrayList) {

                    if(arrayList!=null && arrayList.size()>0){
                        counterDistributorListAdapterByManagerTerritory=new CounterDistributorListAdapterByManagerTerritory(ListCounterDistributorPrimePartnerAdmin.this,arrayList,type);
                        list_counter_dis.setAdapter(counterDistributorListAdapterByManagerTerritory);
                    }
                }

                @Override
                public void OnError(String str_err) {
                    Toast.makeText(ListCounterDistributorPrimePartnerAdmin.this, str_err, Toast.LENGTH_SHORT).show();
                    list_counter_dis.setAdapter(null);
                }

                @Override
                public void OnConnectTimeout() {
                    Toast.makeText(ListCounterDistributorPrimePartnerAdmin.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
                }

            });
            listCounterDistributorByManagerTerritory.execute();

        } else {
            Toast.makeText(ListCounterDistributorPrimePartnerAdmin.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
        }


    }

    private void initView() {

        list_counter_dis = (ListView) findViewById(R.id.listView_counr_dis);
        counterDistributorListAdapterByManagerTerritory=new CounterDistributorListAdapterByManagerTerritory(this,new ArrayList<com.nordusk.webservices.List>(),type);
        list_counter_dis.setAdapter(counterDistributorListAdapterByManagerTerritory);

        if (getIntent().getStringExtra("sp_id") != null && getIntent().getStringExtra("sp_id").length() > 0)
            sp_id = getIntent().getStringExtra("sp_id");
        if (getIntent().getStringExtra("type") != null && getIntent().getStringExtra("type").length() > 0)
            type = getIntent().getStringExtra("type");
    }
}
