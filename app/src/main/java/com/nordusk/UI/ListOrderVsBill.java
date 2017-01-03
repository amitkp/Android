package com.nordusk.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.nordusk.R;
import com.nordusk.adapter.AdapterOrderVsBill;
import com.nordusk.pojo.DataObjectAttendance;
import com.nordusk.pojo.DataObjectOrderVsBill;
import com.nordusk.webservices.OrderVsBillSPAsync;
import com.nordusk.webservices.HttpConnectionUrl;

import java.util.ArrayList;

public class ListOrderVsBill extends AppCompatActivity {

    private ListView list_counter_dis;
    private AdapterOrderVsBill adapterManagerTarget;
    private  String sp_id="";
    private String custId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_order_vs_bill);
        if(getIntent().getStringExtra("id")!=null && getIntent().getStringExtra("id").length()>0)
            sp_id=getIntent().getStringExtra("id");
        if(getIntent().getStringExtra("custId")!=null && getIntent().getStringExtra("custId").length()>0)
            custId=getIntent().getStringExtra("custId");
        initView();
        populateData();
    }



    private void populateData() {
        if (HttpConnectionUrl.isNetworkAvailable(ListOrderVsBill.this)) {
            OrderVsBillSPAsync orderVsBillSPAsync=new OrderVsBillSPAsync(ListOrderVsBill.this,custId,sp_id);
            orderVsBillSPAsync.setOnContentListParserListner(new OrderVsBillSPAsync.OnContentListSchedules() {
                @Override
                public void OnSuccess(ArrayList<DataObjectOrderVsBill> arrayList) {
                    if(arrayList!=null && arrayList.size()>0){
                        adapterManagerTarget=new AdapterOrderVsBill(ListOrderVsBill.this,arrayList);
                        list_counter_dis.setAdapter(adapterManagerTarget);
                    }else {
                        list_counter_dis.setAdapter(null);
                    }
                }

                @Override
                public void OnError(String str_err) {
                    Toast.makeText(ListOrderVsBill.this, str_err, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void OnConnectTimeout() {
                    Toast.makeText(ListOrderVsBill.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
                }
            });
            orderVsBillSPAsync.execute();

        } else {
            Toast.makeText(ListOrderVsBill.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
        }


    }

    private void initView() {

        list_counter_dis = (ListView) findViewById(R.id.listView_order_vs_bill);
        adapterManagerTarget=new AdapterOrderVsBill(ListOrderVsBill.this,new ArrayList<DataObjectOrderVsBill>());
        list_counter_dis.setAdapter(adapterManagerTarget);
    }
}
