package com.nordusk.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nordusk.R;
import com.nordusk.adapter.CustomAutoCompleteAdapter;
import com.nordusk.pojo.DataDistributor;
import com.nordusk.utility.FileUtils;
import com.nordusk.utility.GPSTracker;
import com.nordusk.utility.Prefs;
import com.nordusk.utility.Util;
import com.nordusk.webservices.AddCounterAsync;
import com.nordusk.webservices.ParentId;
import com.nordusk.webservices.ParentIdAsync;
import com.nordusk.webservices.TerritoryAsync;
import com.nordusk.webservices.UserTrace;
import com.nordusk.webservices.rest.EditCounterDistributorAsync;
import com.nordusk.webservices.rest.RestCallback;
import com.nordusk.webservices.rest.WebApiClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;


import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
/*import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;*/

public class AddCounter extends AppCompatActivity {

    private EditText edt_countername, edt_counterownername, edt_dob, edt_mobileno,
            edt_emailid, edt_territory, edt_aniversary, edt_bankname, edt_accno, edt_ifsccode, edt_countersize;
    //    private EditText edt_counteraddress;
    private Button submit;
    //    private TextView txt_counterlocation_press, txt_current_loc;
    private static int REQUEST_LOCATION = 2;

    //    private boolean press_current_loc = false;
    private boolean adress_set = false;
    Double lat = 0.0, lon = 0.0;
    Location location = null;
    String complete_address = "";
    private SimpleDateFormat dateFormatter;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private ImageView img_pic;
    private TextView txt_select;
    private Bitmap bm;

    private ArrayList<ParentId> tempParentIds = new ArrayList<>();
    private ArrayList<ParentId> auto_territory = new ArrayList<>();
    private AutoCompleteTextView auto_text, auto_text_territory;
    private Uri filePath;
    private String call_from = "";
    private Bundle bundle = null;
    private DataDistributor dataDistributor = new DataDistributor();
    private String id = "";
    private String territory_id = "";
    String parentId = "";
    private boolean imageChanged = false;
    private ProgressDialog mpProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcounterprofile);
        bundle = getIntent().getExtras();
        if (bundle != null) {
            HashMap<String, DataDistributor> hashmap = (HashMap<String, DataDistributor>) bundle.getSerializable("value");
            if (hashmap != null && hashmap.size() > 0) {
                dataDistributor = hashmap.get("value");

            }
        }
        call_from = getIntent().getStringExtra("from");


        ParentIdfetch();
        TerritoryListFetch();
        initView();

        fetchData();
        setListener();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Check Permissions Now

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);

            } else {


            }
        }

        if (!call_from.equalsIgnoreCase("edit")) {
            //To setup location manager
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        LocationManager.requestLocationUpdates(String provider, long minTime, float minDistance, LocationListener listener);
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 5, this);
        }
    }

    private void fetchData() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        ImageLoader imageLoader = ImageLoader.getInstance();
        if (dataDistributor.getImage() != null)
            imageLoader.displayImage(dataDistributor.getImage(), img_pic);
        if (dataDistributor.getName() != null)
            edt_countername.setText(dataDistributor.getName());
//        if (dataDistributor.getAddress() != null)
//            edt_counteraddress.setText(dataDistributor.getAddress());
        if (dataDistributor.getMobile() != null)
            edt_mobileno.setText(dataDistributor.getMobile());
//        if (dataDistributor.getTerritory() != null)
//            auto_text_territory.setText(dataDistributor.getTerritory());
        if (dataDistributor.getDob() != null)
            edt_dob.setText(dataDistributor.getDob());
        if (dataDistributor.getEmail() != null)
            edt_emailid.setText(dataDistributor.getEmail());
        if (dataDistributor.getAnniversary() != null)
            edt_aniversary.setText(dataDistributor.getAnniversary());
        if (dataDistributor.getBankName() != null)
            edt_bankname.setText(dataDistributor.getBankName());
        if (dataDistributor.getAccountNo() != null)
            edt_accno.setText(dataDistributor.getAccountNo());
        if (dataDistributor.getIfscCode() != null)
            edt_ifsccode.setText(dataDistributor.getIfscCode());
        if (dataDistributor.getCounterSize() != null)
            edt_countersize.setText(dataDistributor.getCounterSize());
        id = dataDistributor.getId();


        if(dataDistributor.getParrentId()!=null){
           parentId=dataDistributor.getParrentId();
            for(int i=0;i<tempParentIds.size();i++){
                if(parentId.equalsIgnoreCase(tempParentIds.get(i).getId())){
                    auto_text.setText(tempParentIds.get(i).getName());
                }
            }
        }


//        if (call_from.equalsIgnoreCase("edit")) {
//            press_current_loc = true;
//            txt_counterlocation_press.setVisibility(View.GONE);
//        }


    }

    private void setUpParentAdapter() {
        CustomAutoCompleteAdapter customerAdapter = new CustomAutoCompleteAdapter(this, R.layout.custom_auto, tempParentIds);
        auto_text.setAdapter(customerAdapter);
    }

    private void setAutoTextAdapter() {

        CustomAutoCompleteAdapter customerAdapter_territory = new CustomAutoCompleteAdapter(this, R.layout.custom_auto, auto_territory);
        auto_text_territory.setAdapter(customerAdapter_territory);

        auto_text_territory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // String name = "";
                // name = parent.getItemAtPosition(position).toString();
                for (int i = 0; i < auto_territory.size(); i++) {
                    //if (name.equalsIgnoreCase(auto_territory.get(i).getName())) {
                    territory_id = auto_territory.get(i).getId();
                    //}
                }

            }
        });

    }

    private void TerritoryListFetch() {

        TerritoryAsync territoryAsync = new TerritoryAsync(AddCounter.this);
        territoryAsync.setOnContentListParserListner(new TerritoryAsync.OnContentListSchedules() {
            @Override
            public void OnSuccess(ArrayList<ParentId> arrayList) {

                auto_territory = arrayList;
                if (call_from != null && call_from.equals("edit")) {
                    String[] response = dataDistributor.getTerritory().split("-");
                    if (response.length >= 2) {
                        for (int i = 0; i < auto_territory.size(); i++) {
                            if (response[1].equals(auto_territory.get(i).getId())) {
//                                auto_text_territory.setText(auto_territory.get(i).getName() + "-" + auto_territory.get(i).getId());
                                auto_text_territory.setText(auto_territory.get(i).getName());
                                territory_id = auto_territory.get(i).getId();
                            }
                        }
                    } else if (response.length == 1) {
                        for (int i = 0; i < auto_territory.size(); i++) {
                            if (response[0].equals(auto_territory.get(i).getId())) {
//                                auto_text_territory.setText(auto_territory.get(i).getName() + "-" + auto_territory.get(i).getId());
                                auto_text_territory.setText(auto_territory.get(i).getName());
                                territory_id = auto_territory.get(i).getId();
                            }
                        }
                    }
                }
                setAutoTextAdapter();
            }

            @Override
            public void OnError(String str_err) {

            }

            @Override
            public void OnConnectTimeout() {

            }
        });
        territoryAsync.execute();
    }

    private void ParentIdfetch() {

        ParentIdAsync parentIdAsync = new ParentIdAsync(AddCounter.this);
        parentIdAsync.setOnContentListParserListner(new ParentIdAsync.OnContentListSchedules() {
            @Override
            public void OnSuccess(ArrayList<ParentId> arrayList) {

                tempParentIds = arrayList;
                if (call_from != null && call_from.equalsIgnoreCase("edit")) {
                    for (int i = 0; i < tempParentIds.size(); i++) {
                        if (dataDistributor.getParrentId().equals(tempParentIds.get(i).getId())) {
//                            auto_text.setText(tempParentIds.get(i).getName() + "-" + tempParentIds.get(i).getId());
                            auto_text.setText(tempParentIds.get(i).getName());
                            break;
                        }
                    }
                }
                setUpParentAdapter();
            }

            @Override
            public void OnError(String str_err) {

            }

            @Override
            public void OnConnectTimeout() {

            }
        });
        parentIdAsync.execute();

    }

    private void setListener() {


//        txt_counterlocation_press.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                press_current_loc = true;
//                txt_current_loc.setText(complete_address);
//            }
//        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInputs();
            }
        });


        edt_dob.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                        Util.setDateFromDatePicker(edt_dob, AddCounter.this, dateFormatter);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;

                }

                return true;
            }
        });

        edt_aniversary.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // TODO Auto-generated method stub
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                        Util.setDateFromDatePicker(edt_aniversary, AddCounter.this, dateFormatter);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;

                }

                return true;
            }
        });

        txt_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();
//                try {
//                    executeMultipartPost();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

            }
        });
    }

    private void initView() {
        auto_text_territory = (AutoCompleteTextView) findViewById(R.id.counterdtls_edtxt_territory);
        edt_countername = (EditText) findViewById(R.id.counterdtls_edtxt_name);
//        edt_counteraddress = (EditText) findViewById(R.id.counterdtls_edtxt_address);
        edt_counterownername = (EditText) findViewById(R.id.counterdtls_edtxt_ownername);
        edt_dob = (EditText) findViewById(R.id.counterdtls_edtxt_dob);

        edt_mobileno = (EditText) findViewById(R.id.counterdtls_edtxt_mobilenumber);
        edt_emailid = (EditText) findViewById(R.id.counterdtls_edtxt_emailid);


        edt_aniversary = (EditText) findViewById(R.id.counterdtls_edtxt_anniversary);

//        txt_counterlocation_press = (TextView) findViewById(R.id.txt_courentlocation);
//        txt_current_loc = (TextView) findViewById(R.id.txt_courentownerdetails);
        submit = (Button) findViewById(R.id.counterprofile_btn_submit);
        img_pic = (ImageView) findViewById(R.id.image_counter);
        txt_select = (TextView) findViewById(R.id.textView_imgselect);

        edt_bankname = (EditText) findViewById(R.id.counterdtls_edtxt_bankname);
        edt_accno = (EditText) findViewById(R.id.counterdtls_edtxt_bankaccno);
        edt_ifsccode = (EditText) findViewById(R.id.counterdtls_edtxt_ifsccode);
        edt_countersize = (EditText) findViewById(R.id.counterdtls_countersize);
        auto_text = (AutoCompleteTextView) findViewById(R.id.auto_text);
        if (call_from.equalsIgnoreCase("edit")) {
            submit.setText("Update");


        } else
            submit.setText("Add");

        // Initialize AutoCompleteTextView values


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Util.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

//    @Override
//    public void onLocationChanged(Location location) {
//        addressSet(location);
//        lat = String.valueOf(location.getLatitude());
//        longitude = String.valueOf(location.getLongitude());
//    }

//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {

//    }

    /*private void addressSet(Location location) {

        String address_details = "", addressone = "", addresstwo = "", city = "", state = "", country = "", postalcode = "", knownname = "";


        try {
            Geocoder gcd = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 5);

//            procedure one


            if (addresses.size() > 0)

//            ApplicationClassUpiTest.getSharedPreference().setLocation(addresses.get(0).getAddressLine(0) + addresses.get(0).getAddressLine(1) + (addresses.get(0).getPostalCode() == null ? "" : ", " + addresses.get(0).getPostalCode()));
//                Toast.makeText(getApplicationContext(), "Your Location is - " + addresses.get(0).getAddressLine(0) + addresses.get(0).getAddressLine(1) + (addresses.get(0).getPostalCode() == null ? "" : ", " + addresses.get(0).getPostalCode()), Toast.LENGTH_LONG).show();

                addressone = (addresses.get(0).getAddressLine(0) == null ? "" : "" + addresses.get(0).getAddressLine(0));
            addresstwo = (addresses.get(0).getAddressLine(1) == null ? "" : "" + addresses.get(0).getAddressLine(1));
            city = (addresses.get(0).getLocality() == null ? "" : "" + addresses.get(0).getLocality());
            state = (addresses.get(0).getAdminArea() == null ? "" : "" + addresses.get(0).getAdminArea());
            country = (addresses.get(0).getCountryName() == null ? "" : "" + addresses.get(0).getCountryName());
            postalcode = (addresses.get(0).getPostalCode() == null ? "" : "" + addresses.get(0).getPostalCode());
            knownname = (addresses.get(0).getFeatureName() == null ? "" : "" + addresses.get(0).getFeatureName());

            address_details = addressone + addresstwo + city + state + country + postalcode + knownname;
            // Toast.makeText(AddCounter.this, address_details, Toast.LENGTH_SHORT).show();


//            procedure two
            try {
                ArrayList<String> addressFragments = new ArrayList<String>();
                Address address = addresses.get(0);
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    addressFragments.add(address.getAddressLine(i));
                }
                complete_address = TextUtils.join(System.getProperty("line.separator"),
                        addressFragments).replaceAll("\\s+", "");

//                if (press_current_loc) {
//
//                    if (!adress_set)
//                        txt_current_loc.setText(complete_address);
//                    if (!TextUtils.isEmpty(complete_address) && complete_address.length() > 0)
//                        adress_set = true;
//                }

//                        Toast.makeText(MapsActivity.this, complete_address, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }*/


    private void validateInputs() {

        if (!TextUtils.isEmpty(edt_countername.getText().toString().trim())) {
//            if (press_current_loc) {
            if (!TextUtils.isEmpty(edt_mobileno.getText().toString().trim())) {
                if (!TextUtils.isEmpty(auto_text_territory.getText().toString().trim())) {
                    if (!TextUtils.isEmpty(edt_dob.getText().toString().trim())) {
                        if (!TextUtils.isEmpty(auto_text.getText().toString().trim())) {

//                                if (!TextUtils.isEmpty(txt_current_loc.getText().toString().trim())) {

                            if (!TextUtils.isEmpty(edt_countersize.getText().toString().trim())) {


                                if (auto_text.getText().toString().trim() != null && auto_text.getText().toString().trim().length() > 0) {
                                    String validation = auto_text.getText().toString();
                                    for(int iter = 0; iter < tempParentIds.size();iter++){
                                        if (validation.trim().equalsIgnoreCase(tempParentIds.get(iter).getName())) {
                                            parentId = tempParentIds.get(iter).getId();
                                        }
                                    }

                                }
                                String path = "";
                                if (filePath != null) {
                                    path = getPath(filePath);
                                }

                                GPSTracker gpsTracker = new GPSTracker(AddCounter.this);

                                if (gpsTracker.canGetLocation()) {
                                    location = gpsTracker.getLocation();
                                    if (null != location) {
                                        lat = location.getLatitude();
                                        lon = location.getLongitude();
                                        complete_address = gpsTracker.addressSet(lat, lon).replaceAll(" ", "%20");
                                    }
                                    Log.e("GPS", "AddCounter-" + complete_address);
                                } else {
                                    gpsTracker.showSettingsAlert();
                                    Log.e("GPS", "AddCounter-Unable to get GPS");
                                }

                                if (call_from.equalsIgnoreCase("edit"))

                                {

                                    for (int i = 0; i < auto_territory.size(); i++) {
                                        if (auto_text_territory.getText().toString().trim().equalsIgnoreCase(auto_territory.get(i).getName())) {
                                            territory_id = auto_territory.get(i).getId();
                                        }
                                    }
//                                    populateRetroEditDetails();
                                    populateAsyncEditDetails();
                                } else {
//                                    retrofitAddCounter();
                                    asyncAddCounter();
                                }
                            } else {
                                Toast.makeText(AddCounter.this, "Please enter counter size", Toast.LENGTH_SHORT).show();
                            }

//                                } else {
//                                    Toast.makeText(AddCounter.this, "Please enter current location", Toast.LENGTH_SHORT)
//                                            .show();
//                                }
                        } else {
                            Toast.makeText(AddCounter.this, "Please enter Parent Id", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AddCounter.this, "Please enter date of birth", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddCounter.this, "Please enter territory", Toast.LENGTH_SHORT).show();
                }

            } else
                Toast.makeText(AddCounter.this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
//            } else
//                Toast.makeText(AddCounter.this, "Please press on current location", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(AddCounter.this, "Please enter counter name", Toast.LENGTH_SHORT).show();

    }

    private void asyncAddCounter(){

        mpProgressDialog = new ProgressDialog(AddCounter.this);
        mpProgressDialog.setMessage("Adding Counter..");
        mpProgressDialog.show();
        mpProgressDialog.setCancelable(true);

        AddCounterAsync addCounterAsync = new AddCounterAsync(AddCounter.this, "1",
                edt_countername.getText().toString().trim().replaceAll(" ", ""), edt_mobileno.getText().toString().trim(),
                lat.toString(), lon.toString(), complete_address, edt_emailid.getText().toString().trim(), edt_bankname.getText().toString().trim(),
                edt_accno.getText().toString().trim(), edt_ifsccode.getText().toString().trim(), edt_countersize.getText().toString().trim(),
                parentId, null, territory_id, edt_aniversary.getText().toString(), edt_dob.getText().toString(), null);
        addCounterAsync.setOnContentListParserListner(new AddCounterAsync.OnContentListSchedules() {
            @Override
            public void OnSuccess(String responsecode) {
                if (mpProgressDialog != null && mpProgressDialog.isShowing())
                    mpProgressDialog.dismiss();
                Toast.makeText(AddCounter.this, responsecode, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void OnError(String str_err) {
                if (mpProgressDialog != null && mpProgressDialog.isShowing())
                    mpProgressDialog.dismiss();
                Toast.makeText(AddCounter.this, str_err, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnConnectTimeout() {
                if (mpProgressDialog != null && mpProgressDialog.isShowing())
                    mpProgressDialog.dismiss();

            }
        });

        addCounterAsync.execute();
    }

    /*private void retrofitAddCounter(){

            mpProgressDialog = new ProgressDialog(AddCounter.this);
            mpProgressDialog.setMessage("Adding Counter..");
            mpProgressDialog.show();
            mpProgressDialog.setCancelable(false);

//                                        if (complete_address != null && complete_address.length() > 0)
//                                            complete_address = complete_address.replaceAll(" ", "%20");



            Retrofit mRetrofit = WebApiClient.getClient(new WeakReference<Context>(getBaseContext()));
            RestCallback.AddCounterCallback mAddCounterCallback = mRetrofit.create(RestCallback.AddCounterCallback.class);

            MultipartBody.Part body = null;
            if (imageChanged) {
                try {

                    body = prepareFilePart("image", filePath);
                } catch (Exception e) {
                    Log.e("Image", "Null image");
                    e.printStackTrace();
                }
            }
            HashMap<String, RequestBody> map = new HashMap<>();
            RequestBody mBodyType = createPartFromString("1");
            map.put("type", mBodyType);
            RequestBody userId = createPartFromString(new Prefs(AddCounter.this).getString("userid", ""));
            map.put("userId", userId);
            RequestBody mBody = createPartFromString(edt_countername.getText().toString().trim().replaceAll(" ", "%20"));
            map.put("name", mBody);
            RequestBody mBodyTerritory = createPartFromString(territory_id);
            map.put("territory", mBodyTerritory);
            RequestBody mBodyMobile = createPartFromString(edt_mobileno.getText().toString().trim());
            map.put("mobile", mBodyMobile);
            map.put("alternative_mobile", mBodyMobile);
            RequestBody mBodyLay = createPartFromString(lat.toString());
            map.put("latitude", mBodyLay);
            RequestBody mBodyLng = createPartFromString(lon.toString());
            map.put("longitude", mBodyLng);
            RequestBody mBodyAddress = createPartFromString(complete_address);
            map.put("address", mBodyAddress);
            RequestBody mBodyEmail = createPartFromString(edt_emailid.getText().toString().trim());
            map.put("email", mBodyEmail);
            RequestBody mBodyBank = createPartFromString(edt_bankname.getText().toString().trim());
            map.put("bank_name", mBodyBank);
            RequestBody mBodyAccount = createPartFromString(edt_accno.getText().toString().trim());
            map.put("account_no", mBodyAccount);
            RequestBody mBodyIfsc = createPartFromString(edt_ifsccode.getText().toString().trim());
            map.put("ifsc_code", mBodyIfsc);
            RequestBody mBodyCounter = createPartFromString(edt_countersize.getText().toString().trim());
            map.put("counter_size", mBodyCounter);
            RequestBody mBodyParent = createPartFromString(parentId);
            map.put("parrent_id", mBodyParent);
            RequestBody mBodyDob = createPartFromString(edt_dob.getText().toString().trim());
            map.put("dob", mBodyDob);
            RequestBody mBodyAniversary = createPartFromString(edt_aniversary.getText().toString().trim());
            map.put("anniversary", mBodyAniversary);


            Call<ResponseBody> mCall = mAddCounterCallback.onAddCounterResponse(map, body);
            Log.e("URL", "AddCounterQuery-" + mCall.request().url().query());
            Log.e("URL", "AddCounterENCQuery-" + mCall.request().url().encodedQuery());
            mCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (mpProgressDialog != null && mpProgressDialog.isShowing())
                        mpProgressDialog.dismiss();
                    try {
                        Log.i("", "onResponse: ");
                        Log.e("URL", "AddCounterQuery-" + call.request().url().query());
                        Log.e("URL", "AddCounterENCQuery-" + call.request().url().encodedQuery());
                        if (response.code() == 200) {
                            Toast.makeText(AddCounter.this, "Success", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception npe) {
                        npe.printStackTrace();
                    }
                    AddCounter.this.finish();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (mpProgressDialog != null && mpProgressDialog.isShowing())
                        mpProgressDialog.dismiss();
                    try {
                        Log.i("", "onResponse: ");
                        Log.e("URL", "AddCounterQuery-" + call.request().url().query());
                        Log.e("URL", "AddCounterENCQuery-" + call.request().url().encodedQuery());
                        if (t instanceof SocketTimeoutException || t instanceof
                                ConnectException || t instanceof UnknownHostException) {
                            Log.e("TimeOut", t.getMessage());
                        }
                    } catch (Exception npe) {
                        npe.printStackTrace();
                    }
                    AddCounter.this.finish();
                }
            });
    }*/


    private void populateAsyncEditDetails(){


        if(complete_address!=null && complete_address.length()>0)
            complete_address=complete_address.replaceAll(" ","%20");

        mpProgressDialog = new ProgressDialog(AddCounter.this);
        mpProgressDialog.setMessage("Adding Counter..");
        mpProgressDialog.show();
        mpProgressDialog.setCancelable(true);

        EditCounterDistributorAsync editCounterAsync = new EditCounterDistributorAsync(AddCounter.this, "1",
                edt_countername.getText().toString().trim().replaceAll(" ", "%20"), edt_mobileno.getText().toString().trim(),
                lat.toString(), lon.toString(), complete_address, edt_emailid.getText().toString().trim(),
                edt_bankname.getText().toString().trim(), edt_accno.getText().toString().trim(), edt_ifsccode.getText().toString().trim(),
                edt_countersize.getText().toString().trim(), parentId, "", territory_id, edt_aniversary.getText().toString(),
                edt_dob.getText().toString(), id, null);
        editCounterAsync.setOnContentListParserListner(new EditCounterDistributorAsync.OnContentListSchedules() {
            @Override
            public void OnSuccess(String responsecode) {
                if (mpProgressDialog != null && mpProgressDialog.isShowing())
                    mpProgressDialog.dismiss();
                Toast.makeText(AddCounter.this, responsecode, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void OnError(String str_err) {
                if (mpProgressDialog != null && mpProgressDialog.isShowing())
                    mpProgressDialog.dismiss();
                Toast.makeText(AddCounter.this, str_err, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnConnectTimeout() {
                if (mpProgressDialog != null && mpProgressDialog.isShowing())
                    mpProgressDialog.dismiss();
                Toast.makeText(AddCounter.this, "Timed Out", Toast.LENGTH_SHORT).show();
            }
        });

        editCounterAsync.execute();

    }

    /*private void populateRetroEditDetails() {
        mpProgressDialog = new ProgressDialog(AddCounter.this);
        mpProgressDialog.setMessage("Updating Counter..");
        mpProgressDialog.show();
        mpProgressDialog.setCancelable(false);

        Retrofit mRetrofit = WebApiClient.getClient(new WeakReference<Context>(getBaseContext()));
        RestCallback.EditCounterCallback mEditCounterCallback = mRetrofit.create(RestCallback.EditCounterCallback.class);
        HashMap<String, RequestBody> map = new HashMap<>();
        MultipartBody.Part body = null;
        if (imageChanged) {
            try {
                body = prepareFilePart("image", filePath);
                RequestBody new_image = createPartFromString("1");
                map.put("new_image", new_image);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            RequestBody new_image = createPartFromString("0");
            map.put("new_image", new_image);
        }

        RequestBody mBodyType = createPartFromString("1");
        map.put("type", mBodyType);
        RequestBody userId = createPartFromString(new Prefs(AddCounter.this).getString("userid", ""));
        map.put("userId", userId);*//*
        RequestBody id = createPartFromString(dataDistributor.getId());
        map.put("id", id);
        RequestBody mBody = createPartFromString(edt_countername.getText().toString().trim().replaceAll(" ", "%20"));
        map.put("name", mBody);
        RequestBody mBodyTerritory = createPartFromString(territory_id);
        map.put("territory", mBodyTerritory);
        RequestBody mBodyMobile = createPartFromString(edt_mobileno.getText().toString().trim());
        map.put("mobile", mBodyMobile);
        RequestBody mBodyLay = createPartFromString(lat);
        map.put("latitude", mBodyLay);
        RequestBody mBodyLng = createPartFromString(longitude);
        map.put("longitde", mBodyLng);*//*
        RequestBody mBodyAddress = createPartFromString(dataDistributor.getAddress().toString().replaceAll(" ", "%20"));
        map.put("address", mBodyAddress);
        RequestBody mBodyEmail = createPartFromString(edt_emailid.getText().toString
                ().trim());
        map.put("email", mBodyEmail);
        RequestBody mBodyBank = createPartFromString(edt_bankname.getText().toString
                ().trim());
        map.put("bank_name", mBodyBank);

        RequestBody mBodyAccount = createPartFromString(edt_accno.getText().toString().trim());
        map.put("account_no", mBodyAccount);

        RequestBody mBodyIfsc = createPartFromString(edt_ifsccode.getText().toString
                ().trim());
        map.put("ifsc_code", mBodyIfsc);

        RequestBody mBodyCounter = createPartFromString(edt_countersize.getText().toString().trim());
        map.put("counter_size", mBodyCounter);

        RequestBody mBodyParent = createPartFromString(parentId);
        map.put("parrent_id", mBodyParent);


        RequestBody mBodyDob = createPartFromString(edt_dob.getText().toString().trim
                ());
        map.put("dob", mBodyDob);

        RequestBody mBodyAniversary = createPartFromString(edt_aniversary.getText().toString().trim
                ());
        map.put("anniversary", mBodyAniversary);


        Call<ResponseBody> mCall = mEditCounterCallback.onEditCounterResponse(map, body);
        mCall.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (mpProgressDialog != null && mpProgressDialog.isShowing())
                    mpProgressDialog.dismiss();
                try {
                    Log.i("", "onResponse: ");
                    if (response.code() == 200) {
                        Toast.makeText(AddCounter.this, "Success", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                AddCounter.this.finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (mpProgressDialog != null && mpProgressDialog.isShowing())
                    mpProgressDialog.dismiss();
                try {
                    Log.i("", "onResponse: ");
                    if (t instanceof SocketTimeoutException || t instanceof
                            ConnectException || t instanceof UnknownHostException) {
                        Log.e("TimeOut", t.getMessage());
                    }
                } catch (Exception npe) {
                    npe.printStackTrace();
                }
                AddCounter.this.finish();
            }
        });
    }*/

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AddCounter.this);
        builder.setTitle("Add Photo!");
        imageChanged = true;
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Util.checkPermission(AddCounter.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    imageChanged = false;
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        filePath = data.getData();
        img_pic.setImageBitmap(thumbnail);
        bm = thumbnail;
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        filePath = data.getData();
        img_pic.setImageBitmap(bm);
    }


//    public void executeMultipartPost() throws Exception {
//        try {
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            bm.compress(Bitmap.CompressFormat.JPEG, 75, bos);
//            byte[] data = bos.toByteArray();
//            HttpClient httpClient = new DefaultHttpClient();
//            HttpPost postRequest = new HttpPost(
//                    "http://10.0.2.2/cfc/iphoneWebservice.cfc?returnformat=json&amp;method=testUpload");
//            String fileName = String.format("File_%d.png",new Date().getTime());
//            ByteArrayBody bab = new ByteArrayBody(data, fileName);
//            // File file= new File("/mnt/sdcard/forest.png");
//            // FileBody bin = new FileBody(file);
//            MultipartEntity reqEntity = new MultipartEntity(
//                    HttpMultipartMode.BROWSER_COMPATIBLE);
//            reqEntity.addPart("uploaded", bab);
//            reqEntity.addPart("photoCaption", new StringBody("sfsdfsdf"));
//            postRequest.setEntity(reqEntity);
//            HttpResponse response = httpClient.execute(postRequest);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(
//                    response.getEntity().getContent(), "UTF-8"));
//            String sResponse;
//            StringBuilder s = new StringBuilder();
//
//            while ((sResponse = reader.readLine()) != null) {
//                s = s.append(sResponse);
//            }
//            System.out.println("Response: " + s);
//        } catch (Exception e) {
//            // handle exception here
//            Log.e(e.getClass().getName(), e.getMessage());
//        }
//    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getApplicationContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getApplicationContext().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    public static final String MULTIPART_FORM_DATA = "multipart/form-data";

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), descriptionString);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri path) throws URISyntaxException {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(this, path);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
