package com.nordusk.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.nordusk.R;
import com.nordusk.adapter.AdapterHourlyAttendance;
import com.nordusk.pojo.DataObjectAttendance;
import com.nordusk.webservices.AttendanceHourlySPAsync;
import com.nordusk.webservices.HttpConnectionUrl;

import java.util.ArrayList;

public class ListattendanceHourlyReport extends AppCompatActivity {

    private ListView list_counter_dis;
    private AdapterHourlyAttendance adapterManagerTarget;
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
        if (HttpConnectionUrl.isNetworkAvailable(ListattendanceHourlyReport.this)) {
            AttendanceHourlySPAsync attendanceSPAsync=new AttendanceHourlySPAsync(ListattendanceHourlyReport.this,date,sp_id);
            attendanceSPAsync.setOnContentListParserListner(new AttendanceHourlySPAsync.OnContentListSchedules() {
                @Override
                public void OnSuccess(ArrayList<DataObjectAttendance> arrayList) {
                    if(arrayList!=null && arrayList.size()>0){
                        adapterManagerTarget=new AdapterHourlyAttendance(ListattendanceHourlyReport.this,arrayList);
                        list_counter_dis.setAdapter(adapterManagerTarget);
                    }else {
                        list_counter_dis.setAdapter(null);
                    }
                }

                @Override
                public void OnError(String str_err) {
                    Toast.makeText(ListattendanceHourlyReport.this, str_err, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void OnConnectTimeout() {
                    Toast.makeText(ListattendanceHourlyReport.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
                }
            });
            attendanceSPAsync.execute();

        } else {
            Toast.makeText(ListattendanceHourlyReport.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
        }


    }

    private void initView() {

        list_counter_dis = (ListView) findViewById(R.id.listView_counr_dis);
        adapterManagerTarget=new AdapterHourlyAttendance(ListattendanceHourlyReport.this,new ArrayList<DataObjectAttendance>());
        list_counter_dis.setAdapter(adapterManagerTarget);
    }
}
