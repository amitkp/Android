package com.nordusk.UI;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.nordusk.R;
import com.nordusk.adapter.AdapterAttendance;
import com.nordusk.adapter.AdapterManagerTarget;
import com.nordusk.pojo.DataObjectAttendance;
import com.nordusk.pojo.DataTargetManager;
import com.nordusk.utility.GPSTracker;
import com.nordusk.webservices.AttendanceSPAsync;
import com.nordusk.webservices.HttpConnectionUrl;
import com.nordusk.webservices.TargetListManagerAsync;

import java.util.ArrayList;

public class Listattendance extends AppCompatActivity {

    private ListView list_counter_dis;
    private AdapterAttendance adapterManagerTarget;
    private  String sp_id="";
    private String date="";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_counter_distributor);
        if(getIntent().getStringExtra("id")!=null && getIntent().getStringExtra("id").length()>0)
            sp_id=getIntent().getStringExtra("id");
        if(getIntent().getStringExtra("date")!=null && getIntent().getStringExtra("date").length()>0)
            date=getIntent().getStringExtra("date");
        context = Listattendance.this;
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

                        /*
                        method to set address from lat lon
                         */

                        ProgressDialog mpProgressDialog = new ProgressDialog(Listattendance.this);
                        mpProgressDialog.setMessage("Fetching Address..");
                        mpProgressDialog.show();
                        //mpProgressDialog.setCancelable(true);
                        GPSTracker gpsTracker = new GPSTracker(context);
                        Double loginlat, loginlon;
                        Double logoutlat, logoutlon;

                        for (int position = 0; position < arrayList.size(); position++) {
                            DataObjectAttendance dp = arrayList.get(position);

                            if (dp.getLogin_latitude() == null || dp.getLogin_latitude().isEmpty() ||
                                    dp.getLogin_longitutde() == null || dp.getLogin_longitutde().isEmpty()) {
                                dp.setLogin_addresss("");
                                Log.e("GPS", "Date-" + dp.getDate() + "Null Data");
                            } else {

                                loginlat = Double.parseDouble(dp.getLogin_latitude());
                                loginlon = Double.parseDouble(dp.getLogin_longitutde());

                                if (90.0 >= loginlat && loginlat >= -90.0 && 180.0 >= loginlon && loginlon >= -180.0) {
                                    dp.setLogin_addresss(gpsTracker.addressSet(loginlat, loginlon));
                                    Log.e("GPS", "Date-" + dp.getDate() + "Login-" + dp.getLogin_addresss());
                                } else {
                                    dp.setLogin_addresss("");
                                    Log.e("GPS", "Date-" + dp.getDate() + "Invalid Lat-" + loginlat + ":Lon-" + loginlon);
                                }
                                // gpsTracker = null;
                            }

                            if (dp.getLogout_latitude() == null || dp.getLogout_latitude().isEmpty()
                                    || dp.getLogout_longitude() == null || dp.getLogout_longitude().isEmpty()) {
                                dp.setLogout_address("");
                                Log.e("GPS", "Date-" + dp.getDate() + "Null Data");
                            } else {

                                logoutlat = Double.parseDouble(dp.getLogout_latitude());
                                logoutlon = Double.parseDouble(dp.getLogout_longitude());
                                if (90.0 >= logoutlat && logoutlat >= -90.0 && 180.0 >= logoutlon && logoutlon >= -180.0) {
                                    dp.setLogout_address(gpsTracker.addressSet(logoutlat, logoutlon));
                                    Log.e("GPS", "Date-" + dp.getDate() + "Logout" + dp.getLogout_address());
                                } else {
                                    dp.setLogout_address("");
                                    Log.e("GPS", "Date-" + dp.getDate() + "Invalid Lat-" + logoutlat + ":Lon-" + logoutlon);
                                }
                            }
                            dp = null;
                            if (position == arrayList.size() - 1 && mpProgressDialog != null && mpProgressDialog.isShowing())
                                mpProgressDialog.dismiss();
                        }


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
