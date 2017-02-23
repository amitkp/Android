package com.nordusk.UI;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.nordusk.R;
import com.nordusk.adapter.AdapterHourlyAttendance;
import com.nordusk.pojo.DataObjectAttendance;
import com.nordusk.utility.Prefs;
import com.nordusk.webservices.AttendanceHourlySPAsync;
import com.nordusk.webservices.HttpConnectionUrl;
import com.nordusk.webservices.InsertDailyTraceAsync;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListattendanceHourlyReport extends AppCompatActivity {

    private ListView list_counter_dis;
    private AdapterHourlyAttendance adapterManagerTarget;
    private  String sp_id="";
    private String userName="";
    private String date="";
    private  Prefs my_prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_counter_distributor);
        if(getIntent().getStringExtra("id")!=null && getIntent().getStringExtra("id").length()>0)
            sp_id=getIntent().getStringExtra("id");
        if(getIntent().getStringExtra("userName")!=null && getIntent().getStringExtra("userName").length()>0)
            userName=getIntent().getStringExtra("userName");
        if(getIntent().getStringExtra("date")!=null && getIntent().getStringExtra("date").length()>0)
            date=getIntent().getStringExtra("date");
         my_prefs = new Prefs(this);
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
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    JSONObject jsonObject = new JSONObject();
                                    JSONArray jsonArray = new JSONArray();
                                    jsonObject.put("userId", sp_id);
                                    jsonObject.put("userName", userName);
                                    List<DataObjectAttendance> list = adapterManagerTarget.getUpdateList();
                                    if (list != null && list.size() > 0) {
                                        for (int i = 0; i < list.size(); i++) {
                                            JSONObject jsonObject1 = new JSONObject();
                                            jsonObject1.put("date_time", isNull(list.get(i).getDate()));
                                            jsonObject1.put("address", isNull(list.get(i).getLogin_addresss()));
                                            jsonObject1.put("latitude", isNull(list.get(i).getLogin_latitude()));
                                            jsonObject1.put("longitude", isNull(list.get(i).getLogin_longitutde()));
                                            jsonArray.put(i, jsonObject1);
                                        }
                                    }
                                    jsonObject.put("list", jsonArray);
                                    InsertDailyTraceAsync insertDailyTraceAsync = new InsertDailyTraceAsync(ListattendanceHourlyReport.this, jsonObject);
                                    insertDailyTraceAsync.setOnContentListParserListner(new InsertDailyTraceAsync.OnContentListSchedules() {
                                        @Override
                                        public void OnSuccess(String message) {

                                        }

                                        @Override
                                        public void OnError(String str_err) {

                                        }

                                        @Override
                                        public void OnConnectTimeout() {

                                        }
                                    });
                                    insertDailyTraceAsync.execute();

                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        },15000);
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

    private String isNull(String data){
        if(data == null)
            return "";
        else
            return data;
    }
    private void initView() {

        list_counter_dis = (ListView) findViewById(R.id.listView_counr_dis);
        adapterManagerTarget=new AdapterHourlyAttendance(ListattendanceHourlyReport.this,new ArrayList<DataObjectAttendance>());
        list_counter_dis.setAdapter(adapterManagerTarget);
    }
}
