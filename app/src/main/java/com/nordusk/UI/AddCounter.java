package com.nordusk.UI;

import android.app.Activity;
import android.app.AlertDialog;
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
import com.nordusk.utility.Util;
import com.nordusk.webservices.AddCounterAsync;
import com.nordusk.webservices.ParentId;
import com.nordusk.webservices.ParentIdAsync;
import com.nordusk.webservices.UserTrace;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddCounter extends AppCompatActivity implements LocationListener {

    private EditText edt_countername, edt_counteraddress, edt_counterownername, edt_dob, edt_mobileno, edt_emailid, edt_territory, edt_aniversary,
            edt_bankname, edt_accno, edt_ifsccode, edt_countersize;
    private Button submit;
    private TextView txt_counterlocation_press, txt_current_loc;
    private static int REQUEST_LOCATION = 2;

    private boolean press_current_loc = false;
    private boolean adress_set=false;
    private String lat = "", longitude = "";
    String complete_address = "";
    private SimpleDateFormat dateFormatter;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private ImageView img_pic;
    private TextView txt_select;
    private Bitmap bm;
    private AutoCompleteTextView auto_text;
    private ArrayList<ParentId> tempParentIds = new ArrayList<>();
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcounterprofile);
        ParentIdfetch();
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

    private void setAutoTextAdapter() {


        CustomAutoCompleteAdapter customerAdapter = new CustomAutoCompleteAdapter(this, R.layout.custom_auto, tempParentIds);
        auto_text.setAdapter(customerAdapter);
    }

    private void ParentIdfetch() {

        ParentIdAsync parentIdAsync = new ParentIdAsync(AddCounter.this);
        parentIdAsync.setOnContentListParserListner(new ParentIdAsync.OnContentListSchedules() {
            @Override
            public void OnSuccess(ArrayList<ParentId> arrayList) {

                tempParentIds = arrayList;

                setAutoTextAdapter();
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

        edt_countername = (EditText) findViewById(R.id.counterdtls_edtxt_name);
        edt_counteraddress = (EditText) findViewById(R.id.counterdtls_edtxt_address);
        edt_counterownername = (EditText) findViewById(R.id.counterdtls_edtxt_ownername);
        edt_dob = (EditText) findViewById(R.id.counterdtls_edtxt_dob);

        edt_mobileno = (EditText) findViewById(R.id.counterdtls_edtxt_mobilenumber);
        edt_emailid = (EditText) findViewById(R.id.counterdtls_edtxt_emailid);
        edt_territory = (EditText) findViewById(R.id.counterdtls_edtxt_territory);

        edt_aniversary = (EditText) findViewById(R.id.counterdtls_edtxt_anniversary);

        txt_counterlocation_press = (TextView) findViewById(R.id.txt_courentlocation);
        txt_current_loc = (TextView) findViewById(R.id.txt_courentownerdetails);
        submit = (Button) findViewById(R.id.counterprofile_btn_submit);
        img_pic = (ImageView) findViewById(R.id.image_counter);
        txt_select = (TextView) findViewById(R.id.textView_imgselect);

        edt_bankname = (EditText) findViewById(R.id.counterdtls_edtxt_bankname);
        edt_accno = (EditText) findViewById(R.id.counterdtls_edtxt_bankaccno);
        edt_ifsccode = (EditText) findViewById(R.id.counterdtls_edtxt_ifsccode);
        edt_countersize = (EditText) findViewById(R.id.counterdtls_countersize);
        auto_text = (AutoCompleteTextView) findViewById(R.id.auto_text);


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

                if (press_current_loc) {

                    if(!adress_set)
                    txt_current_loc.setText(complete_address);
                    if(!TextUtils.isEmpty(complete_address) && complete_address.length()>0)
                        adress_set=true;
                }

//                        Toast.makeText(MapsActivity.this, complete_address, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }


//    public void onRequestPermissionsResult(int requestCode,
//                                           String[] permissions,
//                                           int[] grantResults) {
//        if (requestCode == REQUEST_LOCATION) {
//
//            if (grantResults.length == 1
//                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // We can now safely use the API we requested access to
//
//            } else {
//                // Permission was denied or request was cancelled
//                finish();
//            }
//
//
//        } else {
//            // Permission was denied or request was cancelled
//        }
//    }


    private void validateInputs() {

        if (!TextUtils.isEmpty(edt_countername.getText().toString().trim())) {
            if (press_current_loc) {
                if (!TextUtils.isEmpty(edt_mobileno.getText().toString().trim())) {
                    // if(!TextUtils.isEmpty(auto_text.getText().toString().trim())) {

                    String parentId = "";
                    if (auto_text.getText().toString().trim() != null && auto_text.getText().toString().trim().length() > 0) {
                        String[] separated = auto_text.getText().toString().trim().split("-");
                        parentId = separated[1].toString();
                    }
                    String path="";
                    if(filePath!=null) {
                        path = getPath(filePath);
                    }

                    AddCounterAsync addCounterAsync = new AddCounterAsync(AddCounter.this, "1",
                            edt_countername.getText().toString().trim().replaceAll(" ",""), edt_mobileno.getText().toString().trim(),
                            lat, longitude, complete_address, edt_emailid.getText().toString().trim(), edt_bankname.getText().toString().trim(),
                            edt_accno.getText().toString().trim(), edt_ifsccode.getText().toString().trim(), edt_countersize.getText().toString().trim(),
                            parentId, path.trim().replaceAll(" ",""),null);
                    addCounterAsync.setOnContentListParserListner(new AddCounterAsync.OnContentListSchedules() {
                        @Override
                        public void OnSuccess(String responsecode) {
                            Toast.makeText(AddCounter.this, responsecode, Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void OnError(String str_err) {
                            Toast.makeText(AddCounter.this, str_err, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void OnConnectTimeout() {

                        }
                    });

                    addCounterAsync.execute();
                    //  }
//                    else
//                    {
//                        Toast.makeText(AddCounter.this, "Please enter Parent Id", Toast.LENGTH_SHORT).show();
//                    }
                } else
                    Toast.makeText(AddCounter.this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(AddCounter.this, "Please press on current location", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(AddCounter.this, "Please enter counter name", Toast.LENGTH_SHORT).show();

    }


    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AddCounter.this);
        builder.setTitle("Add Photo!");
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
        filePath=data.getData();
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
        filePath=data.getData();
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

}
