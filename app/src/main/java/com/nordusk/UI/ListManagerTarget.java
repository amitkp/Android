package com.nordusk.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.nordusk.adapter.AdapterManagerTarget;
import com.nordusk.adapter.CounterDistributorListAdapter;
import com.nordusk.pojo.DataDistributor;
import com.nordusk.pojo.DataTargetManager;
import com.nordusk.utility.Prefs;
import com.nordusk.webservices.HttpConnectionUrl;
import com.nordusk.webservices.ListContractorDistributorAsync;
import com.nordusk.webservices.TargetListManagerAsync;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ListManagerTarget extends AppCompatActivity {

    private ListView list_counter_dis;
    private AdapterManagerTarget adapterManagerTarget;
    private  String sp_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_counter_distributor);
        if(getIntent().getStringExtra("sp_id")!=null && getIntent().getStringExtra("sp_id").length()>0)
            sp_id=getIntent().getStringExtra("sp_id");
        initView();
        populateData();
    }



    private void populateData() {
        if (HttpConnectionUrl.isNetworkAvailable(ListManagerTarget.this)) {
            TargetListManagerAsync targetListManagerAsync=new TargetListManagerAsync(ListManagerTarget.this,sp_id);
            targetListManagerAsync.setOnContentListParserListner(new TargetListManagerAsync.OnContentListSchedules() {
                @Override
                public void OnSuccess(ArrayList<DataTargetManager> arrayList) {

                    if(arrayList!=null && arrayList.size()>0){
                        adapterManagerTarget=new AdapterManagerTarget(ListManagerTarget.this,arrayList);
                        list_counter_dis.setAdapter(adapterManagerTarget);
                    }else {
                        list_counter_dis.setAdapter(null);
                    }

                }

                @Override
                public void OnError(String str_err) {
                    Toast.makeText(ListManagerTarget.this, str_err, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void OnConnectTimeout() {
                    Toast.makeText(ListManagerTarget.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
                }

            });
            targetListManagerAsync.execute();

        } else {
            Toast.makeText(ListManagerTarget.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
        }


    }

    private void initView() {

        list_counter_dis = (ListView) findViewById(R.id.listView_counr_dis);
        adapterManagerTarget=new AdapterManagerTarget(ListManagerTarget.this,new ArrayList<DataTargetManager>());
        list_counter_dis.setAdapter(adapterManagerTarget);
    }
}
