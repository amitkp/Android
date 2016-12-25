package com.nordusk.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nordusk.R;
import com.nordusk.adapter.CustomAutoCompleteAdapter;
import com.nordusk.pojo.DataDistributor;
import com.nordusk.utility.Util;
import com.nordusk.webservices.AddCounterAsync;
import com.nordusk.webservices.HttpConnectionUrl;
import com.nordusk.webservices.ParentId;
import com.nordusk.webservices.ParentIdAsync;
import com.nordusk.webservices.PrimePatnerAsync;
import com.nordusk.webservices.TerritoryAsync;
import com.nordusk.webservices.rest.EditCounterDistributorAsync;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AddDistributer extends AppCompatActivity implements LocationListener {

    private EditText edt_countername, edt_counteraddress, edt_counterownername, edt_dob, edt_mobileno, edt_emailid, edt_aniversary,
            edt_bankname, edt_accno, edt_ifsccode, edt_countersize;
    private Button submit;
    private TextView txt_counterlocation_press, txt_current_loc, textView_imgselect;
    private static int REQUEST_LOCATION = 2;

    private boolean press_current_loc = false;
    private boolean adress_set = false;
    private String lat = "", longitude = "";
    String complete_address = "";
    private SimpleDateFormat dateFormatter;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private ImageView img_pic;
    private Bitmap bm;
    private ArrayList<ParentId> tempParentIds = new ArrayList<>();
    private ArrayList<ParentId> auto_territory = new ArrayList<>();
    private AutoCompleteTextView auto_text, auto_text_territory;
    private RadioGroup rd_type;
    private String type = "2";
    private String call_from = "";
    private Bundle bundle = null;
    private DataDistributor dataDistributor = new DataDistributor();
    private String id="";
    private String territory_id="";
    String parentId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adddistributer);


//        if (bundle != null) {
//            HashMap<String, PayItemDetails> hashmap = (HashMap<String, PayItemDetails>) bundle.getSerializable("value");
//            if (hashmap != null && hashmap.size() > 0) {
//                payItemDetails = hashmap.get("value");
//
//            }
//        }

        bundle = getIntent().getExtras();
        if (bundle != null) {
            HashMap<String, DataDistributor> hashmap = (HashMap<String, DataDistributor>) bundle.getSerializable("value");
            if (hashmap != null && hashmap.size() > 0) {
                dataDistributor = hashmap.get("value");

            }
        }
        call_from = getIntent().getStringExtra("from");


        ParentPatnerfetch();
        TerritoryListFetch();
        initView();
        setData();
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
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 5, this);
        }
    }

    private void setData() {


        if (dataDistributor.getName() != null)
            edt_countername.setText(dataDistributor.getName());
        if (dataDistributor.getAddress() != null)
            edt_counteraddress.setText(dataDistributor.getAddress());
        if (dataDistributor.getMobile() != null)
            edt_mobileno.setText(dataDistributor.getMobile());
        if (dataDistributor.getTerritory() != null)
            auto_text_territory.setText(dataDistributor.getTerritory());
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

        if(dataDistributor.getParrentId()!=null){
            parentId=dataDistributor.getParrentId();
            for(int i=0;i<tempParentIds.size();i++){
                if(parentId.equalsIgnoreCase(tempParentIds.get(i).getId())){
                    auto_text.setText(tempParentIds.get(i).getName());
                }
            }
        }


        id=dataDistributor.getId();




        if (call_from.equalsIgnoreCase("edit")) {
            press_current_loc = true;
            txt_counterlocation_press.setVisibility(View.GONE);
            rd_type.setVisibility(View.GONE);

        }
    }

    private void TerritoryListFetch() {

        TerritoryAsync territoryAsync = new TerritoryAsync(AddDistributer.this);
        territoryAsync.setOnContentListParserListner(new TerritoryAsync.OnContentListSchedules() {
            @Override
            public void OnSuccess(ArrayList<ParentId> arrayList) {

                auto_territory = arrayList;

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

    private void ParentPatnerfetch() {

        PrimePatnerAsync primePatnerAsync = new PrimePatnerAsync(AddDistributer.this);
        primePatnerAsync.setOnContentListParserListner(new PrimePatnerAsync.OnContentListSchedules() {
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
        primePatnerAsync.execute();
    }


    private void setListener() {

        rd_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioDistributor:
                        auto_text.setVisibility(View.VISIBLE);
                        type = "2";

                        edt_countername.setHint("Distributor name");
                        edt_counteraddress.setHint("Distributor address");
                        ((TextView) findViewById(R.id.txt_hdr_counterphoto)).setText("Distributor photo");
                        txt_counterlocation_press.setText("Distributor location : Tap to locate*");
                        txt_current_loc.setText("Distributor location");
                        edt_counterownername.setHint("Distributor owner name");

                        break;
                    case R.id.radioPrimepartner:
                        auto_text.setVisibility(View.GONE);
                        type = "3";

                        edt_countername.setHint("Prime partner name");
                        edt_counteraddress.setHint("Prime partner address");
                        ((TextView) findViewById(R.id.txt_hdr_counterphoto)).setText("Prime partner photo");
                        txt_counterlocation_press.setText("Prime partner location : Tap to locate*");
                        txt_current_loc.setText("Prime partner location");
                        edt_counterownername.setHint("Prime partner owner name");

                        break;

                }
            }
        });

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

                if (HttpConnectionUrl.isNetworkAvailable(AddDistributer.this)) {
                    validateInputs();
                } else {
                    Toast.makeText(AddDistributer.this, getResources().getString(R.string.txt_CheckNetwork), Toast.LENGTH_SHORT).show();
                }
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


        textView_imgselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }


    private void initView() {
        auto_text_territory = (AutoCompleteTextView) findViewById(R.id.distridtls_edtxt_territory);
        edt_countername = (EditText) findViewById(R.id.counterdtls_edtxt_name);
        edt_counteraddress = (EditText) findViewById(R.id.counterdtls_edtxt_address);
        edt_counterownername = (EditText) findViewById(R.id.counterdtls_edtxt_ownername);
        edt_dob = (EditText) findViewById(R.id.counterdtls_edtxt_dob);
        edt_aniversary = (EditText) findViewById(R.id.distridtls_edtxt_anniversary);
        edt_mobileno = (EditText) findViewById(R.id.dis_edtxt_mobilenumber);
        edt_emailid = (EditText) findViewById(R.id.distributor_edtxt_emailid);

        txt_counterlocation_press = (TextView) findViewById(R.id.txt_courentlocation);
        txt_current_loc = (TextView) findViewById(R.id.txt_courentownerdetails);
        submit = (Button) findViewById(R.id.counterprofile_btn_submit);
        if (call_from.equalsIgnoreCase("edit"))
            submit.setText("Update");
        else
            submit.setText("Add");

        edt_bankname = (EditText) findViewById(R.id.distributor_edtxt_bankname);
        edt_accno = (EditText) findViewById(R.id.distributor_edtxt_bankaccno);
        edt_ifsccode = (EditText) findViewById(R.id.distributor_edtxt_ifsccode);
        edt_countersize = (EditText) findViewById(R.id.distributor_countersize);

        img_pic = (ImageView) findViewById(R.id.image_distributor);
        textView_imgselect = (TextView) findViewById(R.id.textView_imgselect);
        auto_text = (AutoCompleteTextView) findViewById(R.id.auto_text);

        rd_type = (RadioGroup) findViewById(R.id.radiotype);
    }

    private void setAutoTextAdapter() {


        CustomAutoCompleteAdapter customerAdapter = new CustomAutoCompleteAdapter(this, R.layout.custom_auto, tempParentIds);
        auto_text.setAdapter(customerAdapter);

        CustomAutoCompleteAdapter customerAdapter_territory = new CustomAutoCompleteAdapter(this, R.layout.custom_auto, auto_territory);
        auto_text_territory.setAdapter(customerAdapter_territory);

        auto_text_territory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               // String name = "";
                //name = parent.getItemAtPosition(position).toString();
                for (int i = 0; i < auto_territory.size(); i++) {
                   // if (name.equalsIgnoreCase(auto_territory.get(i).getName())) {
                        territory_id = auto_territory.get(i).getId();
                   // }
                }

            }
        });


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

                if (press_current_loc) {
                    Toast.makeText(AddDistributer.this, complete_address, Toast.LENGTH_SHORT).show();
                    if (!adress_set)
                        txt_current_loc.setText(complete_address);
                    if (!TextUtils.isEmpty(complete_address) && complete_address.length() > 0)
                        adress_set = true;
                }

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
                    if(!TextUtils.isEmpty(auto_text_territory.getText().toString().trim())) {
                        if (!TextUtils.isEmpty(edt_dob.getText().toString().trim())) {

                            //  if (!TextUtils.isEmpty(auto_text.getText().toString().trim())) {


                            if (auto_text.getText().toString().trim() != null && auto_text.getText().toString().trim().length() > 0) {
                                String[] separated = auto_text.getText().toString().trim().split("-");
                                parentId = separated[1].toString();
                            }
                            if (call_from.equalsIgnoreCase("edit"))

                            {

                                for (int i = 0; i < auto_territory.size(); i++) {
                                    if (auto_text_territory.getText().toString().trim().equalsIgnoreCase(auto_territory.get(i).getName())) {
                                        territory_id = auto_territory.get(i).getId();
                                    }
                                }
                                EditCounterDistributorAsync editCounterAsync = new EditCounterDistributorAsync(AddDistributer.this,
                                        type, edt_countername.getText().toString().trim().replaceAll(" ", "%20"),
                                        edt_mobileno.getText().toString().trim(), lat, longitude, edt_counteraddress.getText().toString().trim().replaceAll(" ", "%20"),
                                        edt_emailid.getText().toString().trim(), edt_bankname.getText().toString().trim(),
                                        edt_accno.getText().toString().trim(), edt_ifsccode.getText().toString().trim(),
                                        edt_countersize.getText().toString().trim(), parentId, "", territory_id, edt_aniversary.getText().toString(), edt_dob.getText().toString(), id, null);
                                editCounterAsync.setOnContentListParserListner(new EditCounterDistributorAsync.OnContentListSchedules() {
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

                                editCounterAsync.execute();
//                    } else {
//                        Toast.makeText(AddDistributer.this, "Please enter Prime partner", Toast.LENGTH_SHORT).show();
//                    }
                            } else {

                                if(complete_address!=null && complete_address.length()>0)
                                    complete_address=complete_address.replaceAll(" ","%20");

                                AddCounterAsync addCounterAsync = new AddCounterAsync(AddDistributer.this, type,
                                        edt_countername.getText().toString().trim().replaceAll(" ", "%20"),
                                        edt_mobileno.getText().toString().trim(), lat, longitude, complete_address,
                                        edt_emailid.getText().toString().trim(), edt_bankname.getText().toString().trim(),
                                        edt_accno.getText().toString().trim(), edt_ifsccode.getText().toString().trim(),
                                        edt_countersize.getText().toString().trim(), parentId, "", territory_id, edt_aniversary.getText().toString(), edt_dob.getText().toString(), null);
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
//                    } else {
//                        Toast.makeText(AddDistributer.this, "Please enter Prime partner", Toast.LENGTH_SHORT).show();
//                    }
                            }
                        }
                        else
                            Toast.makeText(AddDistributer.this, "Please enter date of birth", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(AddDistributer.this, "Please enter territory", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(AddDistributer.this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(AddDistributer.this, "Please press on current location", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(AddDistributer.this, "Please enter distributor name", Toast.LENGTH_SHORT).show();

    }


    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AddDistributer.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Util.checkPermission(AddDistributer.this);

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

        img_pic.setImageBitmap(bm);
    }
}
