package com.nordusk.UI;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nordusk.R;
import com.nordusk.adapter.GridDashboardAdapter;
import com.nordusk.adapter.GridDashboardAdapterAdmin;
import com.nordusk.adapter.GridDashboardAdapterManager;
import com.nordusk.utility.GPSTracker;
import com.nordusk.utility.Prefs;
import com.nordusk.webservices.AdminSPAsync;
import com.nordusk.webservices.ChangepasswordAsync;
import com.nordusk.webservices.HttpConnectionUrl;
import com.nordusk.utility.Util;
import com.nordusk.webservices.ListUserTraceName;
import com.nordusk.webservices.LogoutAsync;
import com.nordusk.webservices.ParentId;
import com.nordusk.webservices.TerritoryAsync;
import com.nordusk.webservices.UserTrace;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Dashboard extends AppCompatActivity {

    private GridView grid_dashboard_item;
    private static int REQUEST_LOCATION = 2;
    private Prefs mPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationEnable();

        mPrefs = new Prefs(Dashboard.this);
        initView();
        populateAutocompleteUserData();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        startService(new Intent(this, LocationUpdateService.class));

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

        // NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        // navigationView.setNavigationItemSelectedListener(this);


    }

    private void locationEnable() {


        Util.enableLocationCommon(Dashboard.this, REQUEST_LOCATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Check Permissions Now

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);

            }
        }
    }


    private void initView() {

        grid_dashboard_item = (GridView) findViewById(R.id.dashboard_grid);
        if (mPrefs.getString("designation", "").equalsIgnoreCase("4") || mPrefs.getString("designation", "").equalsIgnoreCase("3")
                || mPrefs.getString("designation", "").equalsIgnoreCase("2")) {
            grid_dashboard_item.setAdapter(new GridDashboardAdapterManager(Dashboard.this));
            populateterritoryData();

        } else if (mPrefs.getString("designation", "").equalsIgnoreCase("6")) {
            populateadminSPData();
            grid_dashboard_item.setAdapter(new GridDashboardAdapterAdmin(Dashboard.this));
        } else {
            grid_dashboard_item.setAdapter(new GridDashboardAdapter(Dashboard.this));
        }


    }

    private void populateterritoryData() {
        if (HttpConnectionUrl.isNetworkAvailable(Dashboard.this)) {
            TerritoryAsync territoryAsync = new TerritoryAsync(Dashboard.this);
            territoryAsync.setOnContentListParserListner(new TerritoryAsync.OnContentListSchedules() {
                @Override
                public void OnSuccess(ArrayList<ParentId> arrayList) {
                    if (arrayList != null && arrayList.size() > 0)
                        Util.TERRITORY_LIST = arrayList;

                }

                @Override
                public void OnError(String str_err) {
                    Toast.makeText(Dashboard.this, str_err, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void OnConnectTimeout() {
                    Toast.makeText(Dashboard.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
                }
            });
            territoryAsync.execute();
        } else {
            Toast.makeText(Dashboard.this, "Please check your network connection", Toast.LENGTH_SHORT).show();

        }
    }

    private void populateadminSPData() {
        if (HttpConnectionUrl.isNetworkAvailable(Dashboard.this)) {
            AdminSPAsync territoryAsync = new AdminSPAsync(Dashboard.this);
            territoryAsync.setOnContentListParserListner(new AdminSPAsync.OnContentListSchedules() {
                @Override
                public void OnSuccess(ArrayList<ParentId> arrayList) {
                    if (arrayList != null && arrayList.size() > 0)
                        Util.ADMIN_SP_LIST = arrayList;
                }

                @Override
                public void OnError(String str_err) {
                    Toast.makeText(Dashboard.this, str_err, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void OnConnectTimeout() {
                    Toast.makeText(Dashboard.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
                }
            });
            territoryAsync.execute();
        } else {
            Toast.makeText(Dashboard.this, "Please check your network connection", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
        super.onBackPressed();
        //}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            GPSCheck();
        } else if (id == R.id.action_chnge) {

            //Toast.makeText(Dashboard.this, "Under Development", Toast.LENGTH_SHORT).show();
            showValidateUserDialog();

        }

        return super.onOptionsItemSelected(item);
    }

    private void GPSCheck(){
        GPSTracker gpsTracker = new GPSTracker(this);
        if(gpsTracker.canGetLocation()){
            Double lat = gpsTracker.getLatitude();
            Double lon = gpsTracker.getLongitude();
            if(lat != null && lat != 0.0 && lon != null && lon != 0.0) {
                logout(lat,lon);
            } else {

            }
        }else{
            gpsTracker.showSettingsAlert();

        }
    }

    public void logout(Double lat, Double lon) {
        LogoutAsync logoutAsync = new LogoutAsync(Dashboard.this, null, lat, lon);
        logoutAsync.setOnContentListParserListner(new LogoutAsync.OnContentListSchedules() {
            @Override
            public void OnSuccess(String responsecode) {
                Toast.makeText(Dashboard.this, responsecode, Toast.LENGTH_SHORT).show();
                new Prefs(Dashboard.this).clearData();
                startActivity(new Intent(Dashboard.this, Login.class));
                finish();
            }

            @Override
            public void OnError(String str_err) {
                Toast.makeText(Dashboard.this, str_err, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnConnectTimeout() {
                Toast.makeText(Dashboard.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
            }
        });

        logoutAsync.execute();
    }
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }

    public void showValidateUserDialog() {

        final EditText old_password, new_password, cnfrm_password;
        final Dialog mDialog_SelectSelectAccount = new Dialog(Dashboard.this,
                android.R.style.Theme_DeviceDefault_Light_Dialog);
        mDialog_SelectSelectAccount.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = mDialog_SelectSelectAccount.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        mDialog_SelectSelectAccount.setCancelable(true);
        mDialog_SelectSelectAccount
                .setContentView(R.layout.chnge_pswrd);
        mDialog_SelectSelectAccount.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        old_password = (EditText) mDialog_SelectSelectAccount.findViewById(R.id.login_edtxt_emailmobile);
        new_password = (EditText) mDialog_SelectSelectAccount.findViewById(R.id.login_edtxt_pswrd);
        cnfrm_password = (EditText) mDialog_SelectSelectAccount.findViewById(R.id.login_edtxt_cnf_pswrd);


        Button login_btn_login = (Button) mDialog_SelectSelectAccount.findViewById(R.id.login_btn_login);

        login_btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // validate
                if (old_password.getText().toString().trim() != null &&
                        new_password.getText().toString().trim() != null && cnfrm_password.getText().toString().trim() != null) {
                    if (new_password.getText().toString().trim().equalsIgnoreCase(cnfrm_password.getText().toString().trim())) {

                        if (HttpConnectionUrl.isNetworkAvailable(Dashboard.this)) {
                            ChangepasswordAsync changepasswordAsync = new ChangepasswordAsync
                                    (Dashboard.this, mPrefs.getString("userid", ""), old_password.getText().toString().trim(), cnfrm_password.getText().toString().trim());
                            changepasswordAsync.setOnContentListParserListner(new ChangepasswordAsync.OnContentListSchedules() {
                                @Override
                                public void OnSuccess(String message) {
                                    Toast.makeText(Dashboard.this, message, Toast.LENGTH_SHORT).show();
                                    mDialog_SelectSelectAccount.dismiss();
                                }

                                @Override
                                public void OnError(String str_err) {
                                    Toast.makeText(Dashboard.this, str_err, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void OnConnectTimeout() {
                                    Toast.makeText(Dashboard.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
                                }
                            });
                            changepasswordAsync.execute();
                        } else {
                            Toast.makeText(Dashboard.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(Dashboard.this, "New password and confirm password must be same", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(Dashboard.this, "Please provide all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mDialog_SelectSelectAccount.show();
    }

    private void populateAutocompleteUserData() {
        if (HttpConnectionUrl.isNetworkAvailable(Dashboard.this)) {
            ListUserTraceName listUserTraceName = new ListUserTraceName(Dashboard.this, mPrefs.getString("userid", ""));
            listUserTraceName.setOnContentListParserListner(new ListUserTraceName.OnContentListSchedules() {
                @Override
                public void OnSuccess(ArrayList<UserTrace> arrayList) {
                    if (Util.getUserList() != null && Util.getUserList().size() > 0 && arrayList != null && arrayList.size() > 0) {
                        Util.getUserList().clear();
                        Util.setUserList(arrayList);
                    } else {
                        if (arrayList != null && arrayList.size() > 0) {
                            Util.setUserList(arrayList);
                        }
                    }
                }

                @Override
                public void OnError(String str_err) {

                }

                @Override
                public void OnConnectTimeout() {

                }
            });
            listUserTraceName.execute();
        } else {
            Toast.makeText(Dashboard.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
        }

    }

}
