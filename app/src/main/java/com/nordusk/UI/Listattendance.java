package com.nordusk.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.nordusk.R;
import com.nordusk.adapter.AdapterAttendance;
import com.nordusk.adapter.AdapterManagerTarget;
import com.nordusk.pojo.DataObjectAttendance;
import com.nordusk.pojo.DataTargetManager;
import com.nordusk.webservices.AttendanceSPAsync;
import com.nordusk.webservices.HttpConnectionUrl;
import com.nordusk.webservices.TargetListManagerAsync;

import java.util.ArrayList;

public class Listattendance extends AppCompatActivity {

    private ListView list_counter_dis;
    private AdapterAttendance adapterManagerTarget;
    private  String sp_id="";
    private String date="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_counter_distributor);
        if(getIntent().getStringExtra("id")!=null && getIntent().getStringExtra("id").length()>0)
            sp_id=getIntent().getStringExtra("id");
        if(getIntent().getStringExtra("date")!=null && getIntent().getStringExtra("date").length()>0)
            date=getIntent().getStringExtra("date");
        initView();
        populateData();
    }



    private void populateData() {
        if (HttpConnectionUrl.isNetworkAvailable(Listattendance.this)) {
            AttendanceSPAsync attendanceSPAsync=new AttendanceSPAsync(Listattendance.this,date,sp_id);
            attendanceSPAsync.setOnContentListParserListner(new AttendanceSPAsync.OnContentListSchedules() {
                @Override
                public void OnSuccess(ArrayList<DataObjectAttendance> arrayList) {
                    if(arrayList!=null && arrayList.size()>0){
                        adapterManagerTarget=new AdapterAttendance(Listattendance.this,arrayList);
                        list_counter_dis.setAdapter(adapterManagerTarget);
                    }else {
                        list_counter_dis.setAdapter(null);
                    }
                }

                @Override
                public void OnError(String str_err) {
                    Toast.makeText(Listattendance.this, str_err, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void OnConnectTimeout() {
                    Toast.makeText(Listattendance.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
                }
            });
            attendanceSPAsync.execute();

        } else {
            Toast.makeText(Listattendance.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
        }


    }

    private void initView() {

        list_counter_dis = (ListView) findViewById(R.id.listView_counr_dis);
        adapterManagerTarget=new AdapterAttendance(Listattendance.this,new ArrayList<DataObjectAttendance>());
        list_counter_dis.setAdapter(adapterManagerTarget);
    }
}
