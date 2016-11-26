package com.nordusk.UI;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nordusk.R;
import com.nordusk.utility.Util;
import com.nordusk.webservices.AddCounterAsync;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddDistributer extends AppCompatActivity implements LocationListener {

    private EditText edt_countername, edt_counteraddress, edt_counterownername, edt_dob, edt_mobileno, edt_emailid, edt_aniversary;
    private Button submit;
    private TextView txt_counterlocation_press, txt_current_loc;
    private static int REQUEST_LOCATION = 2;

    private boolean press_current_loc = false;
    private String lat = "", longitude = "";
    String complete_address = "";
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adddistributer);


        initView();
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

        //To setup location manager
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        LocationManager.requestLocationUpdates(String provider, long minTime, float minDistance, LocationListener listener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 5, this);
    }

    private void setListener() {

        txt_counterlocation_press.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                press_current_loc = true;
                txt_current_loc.setText(complete_address);
            }
        });

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

                        Util.setDateFromDatePicker(edt_dob, AddDistributer.this, dateFormatter);
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

                        Util.setDateFromDatePicker(edt_aniversary, AddDistributer.this, dateFormatter);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;

                }

                return true;


            }
        });
    }


    private void initView() {

        edt_countername = (EditText) findViewById(R.id.counterdtls_edtxt_name);
        edt_counteraddress = (EditText) findViewById(R.id.counterdtls_edtxt_address);
        edt_counterownername = (EditText) findViewById(R.id.counterdtls_edtxt_ownername);
        edt_dob = (EditText) findViewById(R.id.counterdtls_edtxt_dob);
        edt_aniversary = (EditText) findViewById(R.id.distridtls_edtxt_anniversary);
        edt_mobileno = (EditText) findViewById(R.id.counterdtls_edtxt_mobilenumber);
        edt_emailid = (EditText) findViewById(R.id.counterdtls_edtxt_emailid);

        txt_counterlocation_press = (TextView) findViewById(R.id.txt_courentlocation);
        txt_current_loc = (TextView) findViewById(R.id.txt_courentownerdetails);
        submit = (Button) findViewById(R.id.counterprofile_btn_submit);
    }


    @Override
    public void onLocationChanged(Location location) {
        addressSet(location);
        lat = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void addressSet(Location location) {

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
            // Toast.makeText(AddDistributer.this, address_details, Toast.LENGTH_SHORT).show();


//            procedure two
            try {
                ArrayList<String> addressFragments = new ArrayList<String>();
                Address address = addresses.get(0);
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    addressFragments.add(address.getAddressLine(i));
                }
                complete_address = TextUtils.join(System.getProperty("line.separator"),
                        addressFragments).replaceAll("\\s+", "");

                if (press_current_loc)
                    txt_current_loc.setText(complete_address);

//                        Toast.makeText(MapsActivity.this, complete_address, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }


    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {

            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We can now safely use the API we requested access to

            } else {
                // Permission was denied or request was cancelled
                finish();
            }


        } else {
            // Permission was denied or request was cancelled
        }
    }


    private void validateInputs() {

        if (!TextUtils.isEmpty(edt_countername.getText().toString().trim())) {
            if (press_current_loc) {
                if (!TextUtils.isEmpty(edt_mobileno.getText().toString().trim())) {
                    AddCounterAsync addCounterAsync = new AddCounterAsync(AddDistributer.this, "2", edt_countername.getText().toString().trim(), edt_mobileno.getText().toString().trim(), lat, longitude, complete_address, edt_emailid.getText().toString().trim(), null);
                    addCounterAsync.setOnContentListParserListner(new AddCounterAsync.OnContentListSchedules() {
                        @Override
                        public void OnSuccess(String responsecode) {
                            Toast.makeText(AddDistributer.this, responsecode, Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void OnError(String str_err) {
                            Toast.makeText(AddDistributer.this, str_err, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void OnConnectTimeout() {

                        }
                    });

                    addCounterAsync.execute();
                } else
                    Toast.makeText(AddDistributer.this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(AddDistributer.this, "Please press on current location", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(AddDistributer.this, "Please enter distributor name", Toast.LENGTH_SHORT).show();

    }
}
