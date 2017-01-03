package com.nordusk.UI;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.nordusk.*;
import com.nordusk.R;
import com.nordusk.utility.GMapV2Direction;
import com.nordusk.utility.Prefs;
import com.nordusk.utility.Util;
import com.nordusk.webservices.HttpConnectionUrl;
import com.nordusk.webservices.ListTraceAsync;
import com.nordusk.webservices.PointsTraceList;
import com.nordusk.webservices.TrackDetails;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private PolylineOptions lineOptions = new PolylineOptions();
    private Prefs mPrefs;
    private double start_lat, start_long, end_lat, end_long = 0;
    private TextView tv_km;
    private ProgressBar progressBar;
    private RelativeLayout rltv_shortestpath;
    private ImageView task_icon;
    float visited_km=0;
    int counter_visited = 0;
    private ArrayList<PointsTraceList> total_path_list = new ArrayList<PointsTraceList>();
    private boolean isLoaded=false;
    private int total_counters=0;
    GMapV2Direction md = new GMapV2Direction();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_track);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        tv_km = (TextView) findViewById(R.id.tv_km);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        rltv_shortestpath = (RelativeLayout) findViewById(R.id.rltv_shortestpath);
        task_icon = (ImageView) findViewById(R.id.img_task);
        mapFragment.getMapAsync(this);

        setListener();


    }

    private void setListener() {

        rltv_shortestpath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapsActivity.this, "Working Progress", Toast.LENGTH_SHORT).show();
            }
        });

        task_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFrst)
                    Toast.makeText(MapsActivity.this, "You have exceeded your daily request quota for this API.", Toast.LENGTH_SHORT).show();
                if (isLoaded) {
                    selectDialog();
                } else {
                    Toast.makeText(MapsActivity.this, "Please wait.Syncing is in progress...s", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(-34, 151);
        // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        LatLng position1 = new LatLng(22.590927, 88.415898);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(position1));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//
//        LatLng position2 = new LatLng(22.591230, 88.419063);
//        LatLng position3 = new LatLng(22.587030, 88.422346);
//        LatLng position4 = new LatLng(22.576668, 88.434491);
//
//
//        points.add(position1);
//        points.add(position2);
//        points.add(position3);
//        points.add(position4);
//        lineOptions.addAll(points);
//        if (LocationUpdateService.points != null && LocationUpdateService.points.size() > 0) {
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(LocationUpdateService.points.get(0)));
//            for (int i = 0; i < LocationUpdateService.points.size(); i++) {
//                lineOptions.add(LocationUpdateService.points.get(i));
//            }
//        }
        mPrefs = new Prefs(MapsActivity.this);
        String date = "";
        String mobile = "";
        if (getIntent().getStringExtra("mobile") != null && getIntent().getStringExtra("mobile").length() > 0
                && getIntent().getStringExtra("date") != null && getIntent().getStringExtra("date").length() > 0) {
            date = getIntent().getStringExtra("date");
            mobile = getIntent().getStringExtra("mobile");

        } else {
            date = getIntent().getStringExtra("date");
            mobile = mPrefs.getString("username", "");
        }

        populateData(mobile, date);


    }

    private void populateData(String mobile, String date) {
        if (HttpConnectionUrl.isNetworkAvailable(MapsActivity.this)) {
            ListTraceAsync listTraceAsync = new ListTraceAsync(MapsActivity.this, mobile, date);
            listTraceAsync.setOnContentListParserListner(new ListTraceAsync.OnContentListSchedules() {
                @Override
                public void OnSuccess(TrackDetails arrayList) {
                    if (arrayList != null && arrayList.getArr_pointsTraceLists() != null && arrayList.getArr_pointsTraceLists().size() > 0) {
                        for (int i = 0; i < arrayList.getArr_pointsTraceLists().size(); i++) {
                            if (arrayList.getArr_pointsTraceLists().get(i).getLatitude() != null
                                    && arrayList.getArr_pointsTraceLists().get(i).getLongitude() != null
                                     && arrayList.getArr_pointsTraceLists().get(i).getLatitude().length()>0
                                    && arrayList.getArr_pointsTraceLists().get(i).getLongitude().length()>0) {
                                double lat = 0;
                                double longi = 0;
                                int size = arrayList.getArr_pointsTraceLists().size();
                                lat = Double.parseDouble(arrayList.getArr_pointsTraceLists().get(i).getLatitude());
                                longi = Double.parseDouble(arrayList.getArr_pointsTraceLists().get(i).getLongitude());
                                LatLng latLng = new LatLng(lat, longi);
                                if (i == 0) {
                                    start_lat = lat;
                                    start_long = longi;
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                    mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                                } else if (i == arrayList.getArr_pointsTraceLists().size() - 1) {
                                    end_lat = lat;
                                    end_long = longi;
                                    mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                                }

                                lineOptions.add(latLng);
                                PointsTraceList pointsTraceList = new PointsTraceList();
                                pointsTraceList.setLatitude(Double.toString(lat));
                                pointsTraceList.setLongitude(Double.toString(longi));
                                total_path_list.add(pointsTraceList);



                            }

                        }
//                        lineOptions.width(5);
//                        lineOptions.color(Color.RED);
//                        if (lineOptions != null) {
//                            mMap.addPolyline(lineOptions);
//                        }


                    }


                    if (arrayList != null && arrayList.getArr_counterset() != null && arrayList.getArr_counterset().size() > 0)

                    {
                        total_counters=arrayList.getArr_counterset().size();
                        double Filter_end_lat = 0;
                        Filter_end_lat = Double.parseDouble(arrayList.getArr_pointsTraceLists().get(arrayList.getArr_pointsTraceLists().size() - 1).getLatitude());

                        for (int j = 0; j < arrayList.getArr_counterset().size(); j++) {
                            if (arrayList.getArr_counterset().get(j).getLatitude() != null &&
                                    arrayList.getArr_counterset().get(j).getLongitude() != null
                                    && arrayList.getArr_counterset().get(j).getLatitude().length()>0
                                    && arrayList.getArr_counterset().get(j).getLongitude().length()>0) {
                                if (Double.parseDouble(arrayList.getArr_counterset().get(j).getLatitude()) < Filter_end_lat) {
                                    Double lat = Double.parseDouble(arrayList.getArr_counterset().get(j).getLatitude());
                                    Double longi = Double.parseDouble(arrayList.getArr_counterset().get(j).getLongitude());
                                    LatLng latLngchk = new LatLng(lat, longi);
                                    String title = "";
                                    if (arrayList.getArr_counterset().get(j).getName() != null && arrayList.getArr_counterset().get(j).getAddress() != null) {
                                        title = arrayList.getArr_counterset().get(j).getName() + "," + arrayList.getArr_counterset().get(j).getAddress();

                                    }
                                    mMap.addMarker(new MarkerOptions().position(latLngchk).title(title).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                    counter_visited = counter_visited + 1;


                                }

                            }

                        }


                        // Distance distance=new Distance();
                        // distance.execute();


                    } else {
                        Toast.makeText(MapsActivity.this, "No activity found today", Toast.LENGTH_SHORT).show();
                    }

                    if(total_path_list!=null && total_path_list.size()>0) {
                        mpProgressDialog = new ProgressDialog(MapsActivity.this);
                        mpProgressDialog.setMessage("Loading data.Please wait..");
                        mpProgressDialog.setCancelable(false);
                        mpProgressDialog.show();
                        calculateTotalPath();
                    }else{
                        Toast.makeText(MapsActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                    }

                    // Toast.makeText(MapsActivity.this, String.valueOf(short_distance_km), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void OnError(String str_err) {

                }

                @Override
                public void OnConnectTimeout() {

                }
            });
            listTraceAsync.execute();

        } else {
            Toast.makeText(MapsActivity.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
        }


    }



    private int long_path_point = 0;



    private void calculateTotalPath() {
        if (long_path_point < total_path_list.size() - 1) {
            double chng_lat_start = Double.parseDouble(total_path_list.get(long_path_point).getLatitude());
            double chng_lat_end = Double.parseDouble(total_path_list.get(long_path_point + 1).getLatitude());
            double chng_long_start = Double.parseDouble(total_path_list.get(long_path_point).getLongitude());
            double chng_long_end = Double.parseDouble(total_path_list.get(long_path_point + 1).getLongitude());
            long_path_point = long_path_point + 1;
            Distance distance = new Distance(chng_lat_start, chng_lat_end, chng_long_start, chng_long_end);
            distance.execute();




        }else{
            isLoaded=true;
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            visited_km=visited_km/1000;
            if(mpProgressDialog!=null && mpProgressDialog.isShowing())
                mpProgressDialog.dismiss();
        }

    }

    PolylineOptions rectLine = new PolylineOptions().width(3).color(
            Color.RED);
    private ProgressDialog mpProgressDialog;
    private class Distance extends AsyncTask<Void, Void, Void> {
        private String Total_KM = "";
        double start_lat = 0, end_lat = 0, start_long = 0, end_long = 0;


        public Distance(double start_lat, double end_lat, double start_long, double end_long) {

            this.start_lat = start_lat;
            this.end_lat = end_lat;
            this.start_long = start_long;
            this.end_long = end_long;


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
           // Total_KM = getDistanceOnRoad(start_lat, start_long, end_lat, end_long);
            LatLng latLngstart = new LatLng(start_lat
                    , start_long);
            LatLng latLngend = new LatLng(end_lat, end_long);
            Document doc = md.getDocument(latLngstart, latLngend,
                    GMapV2Direction.MODE_DRIVING);
            Log.e("distance",md.getDistanceText(doc));
            if(md.getDistanceText(doc)!=null && md.getDistanceText(doc).length()>0)
            visited_km=visited_km+Integer.parseInt(md.getDistanceText(doc));

            ArrayList<LatLng> directionPoint = md.getDirection(doc);


            for (int i = 0; i < directionPoint.size(); i++) {
                rectLine.add(directionPoint.get(i));
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Polyline polylin = mMap.addPolyline(rectLine);
            calculateTotalPath();

        }
    }

    boolean isFrst = false;

    private String getDistanceOnRoad(double latitude, double longitude,
                                     double prelatitute, double prelongitude) {
        String result_in_kms = "";
        String url = "http://maps.google.com/maps/api/directions/xml?origin="
                + latitude + "," + longitude + "&destination=" + prelatitute
                + "," + prelongitude + "&sensor=false&units=metric&mode=driving";
        String tag[] = {"text"};
        HttpResponse response = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(url);
            response = httpClient.execute(httpPost, localContext);
            InputStream is = response.getEntity().getContent();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            Document doc = builder.parse(is);
            if (doc != null) {
                NodeList nl;
                ArrayList args = new ArrayList();
                for (String s : tag) {
                    nl = doc.getElementsByTagName(s);
                    if (nl.getLength() > 0) {
                        isFrst = false;
                        Node node = nl.item(nl.getLength() - 1);
                        args.add(node.getTextContent());
                    } else {
                        args.add(" - ");
                        if (!isFrst) {
                            isFrst = true;

                        }
                    }

                }
                result_in_kms = String.format("%s", args.get(0));
//                Toast.makeText(MapsActivity.this,result_in_kms,Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result_in_kms;
    }


    private void selectDialog() {
        final TextView tv_total, tv_short, tv_counter;
        final AutoCompleteTextView old_password;

        final Dialog mDialog_SelectSelectAccount = new Dialog(MapsActivity.this,
                android.R.style.Theme_DeviceDefault_Light_Dialog);
        mDialog_SelectSelectAccount.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = mDialog_SelectSelectAccount.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        mDialog_SelectSelectAccount.setCancelable(true);
        mDialog_SelectSelectAccount
                .setContentView(R.layout.dialog_details_tracking);
        mDialog_SelectSelectAccount.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        tv_total = (TextView) mDialog_SelectSelectAccount.findViewById(R.id.tv_total);
        tv_short = (TextView) mDialog_SelectSelectAccount.findViewById(R.id.tv_short);
        tv_counter = (TextView) mDialog_SelectSelectAccount.findViewById(R.id.tv_counter);


        tv_total.setText("" + visited_km + " KM");
        tv_short.setText("" + total_counters);
        tv_counter.setText("" + counter_visited);

        mDialog_SelectSelectAccount.show();

    }

    private void drawProperRoute() {

        //GMapV2Direction md = new GMapV2Direction();

    }


}
