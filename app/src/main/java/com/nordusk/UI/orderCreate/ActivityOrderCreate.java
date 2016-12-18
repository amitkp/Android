package com.nordusk.UI.orderCreate;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.nordusk.R;
import com.nordusk.UI.AddDistributer;
import com.nordusk.UI.ListCounterDistributor;
import com.nordusk.UI.helper.ViewProductItemCreate;
import com.nordusk.adapter.CounterDistributorListAdapter;
import com.nordusk.pojo.DataDistributor;
import com.nordusk.pojo.DataProducts;
import com.nordusk.utility.Prefs;
import com.nordusk.utility.Util;
import com.nordusk.webservices.HttpConnectionUrl;
import com.nordusk.webservices.ListContractorDistributorAsync;
import com.nordusk.webservices.ParentId;
import com.nordusk.webservices.PrimePatnerAsync;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gouravkundu on 11/12/16.
 */

public class ActivityOrderCreate extends AppCompatActivity implements OrderCreatePresenter.OnNotifyUiListener, View.OnClickListener {

    private WeakReference<Context> contextWeakReference;
    private OrderCreatePresenter.OnUserInteractionListener mPresenter;

    private EditText et_orderName;
    private LinearLayout ll_container;

    private Button btn_save;

    private ProgressDialog mDialog;

    private AutoCompleteTextView login_edtxt_emailmobile;
    ArrayList<String> name_list = new ArrayList<String>();
    private ArrayList<DataDistributor> mListDistributor=new ArrayList<DataDistributor>();
    private Prefs mpPrefs;
    private ArrayList<ParentId> tempParentIds=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
        setContentView(R.layout.activity_create_order);
        initView();
        mPresenter.fetchProductList();
    }

    private void initPresenter() {
        contextWeakReference = new WeakReference<Context>(this);
        mPresenter = new OrderCreatePresenterImpl(this, contextWeakReference);
    }

    private void initView() {
        et_orderName = (EditText) findViewById(R.id.et_orderName);
        ll_container = (LinearLayout) findViewById(R.id.ll_container);
        btn_save = (Button) findViewById(R.id.btn_save);
        login_edtxt_emailmobile = (AutoCompleteTextView) findViewById(R.id.login_edtxt_emailmobile);
        btn_save.setOnClickListener(this);

        mDialog = new ProgressDialog(contextWeakReference.get());
        mDialog.setCancelable(false);
        mDialog.setMessage(getResources().getString(R.string.please_wait));
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setIndeterminate(true);

        mpPrefs = new Prefs(this);
        String userId = "";
        String designation = "";
        userId = mpPrefs.getString("userid", "");
        designation=mpPrefs.getString("designation","");
        if(Util.ORDER_FOR_TYPE.equalsIgnoreCase("3")){
            ParentPatnerfetch();
        }else{
            populateData(userId,Util.ORDER_FOR_TYPE,designation);
        }

        login_edtxt_emailmobile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = "";
                name = parent.getItemAtPosition(position).toString();
                if(Util.ORDER_FOR_TYPE.equalsIgnoreCase("3")){
                    for (int i = 0; i < tempParentIds.size(); i++) {
                        if (name.equalsIgnoreCase(tempParentIds.get(i).getName())) {
                            Util.ORDER_FOR = tempParentIds.get(i).getId();
                        }
                    }
                }else{
                    for (int i = 0; i < mListDistributor.size(); i++) {
                        if (name.equalsIgnoreCase(mListDistributor.get(i).getName())) {
                            Util.ORDER_FOR = mListDistributor.get(i).getId();
                        }
                    }
                }


            }
        });


    }

    @Override
    public void onAddProductItem(ArrayList<DataProducts> mListProducts) {

        int childCount = ll_container.getChildCount();
        ViewProductItemCreate mView = new ViewProductItemCreate(contextWeakReference.get(), mListProducts);
        mView.setPosition(childCount);
        ll_container.addView(mView, childCount);
    }

    @Override
    public String getProductName(int childPos) {
        return ((ViewProductItemCreate) ll_container.getChildAt(childPos)).getProductName();
    }

    @Override
    public String getProductId(int childPos) {
        return "3";
    }

    @Override
    public String getProductPrice(int childPos) {
        return ((ViewProductItemCreate) ll_container.getChildAt(childPos)).getProductPrice();
    }

    @Override
    public String getProductQuantity(int childPos) {
        return ((ViewProductItemCreate) ll_container.getChildAt(childPos)).getProductQuantity();
    }

    @Override
    public String getOrderFor() {
        return "92";
    }

    @Override
    public String getOrderType() {
        return "2";
    }

    @Override
    public void onClick(View view) {

        if(Util.ORDER_FOR!=null && Util.ORDER_FOR.length()>0){
            btn_save.setClickable(false);
            mPresenter.createOrder(ll_container.getChildCount());
        }else{
            Toast.makeText(ActivityOrderCreate.this, "Please select order for", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_order_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.m_add) {
            mPresenter.onAddProductClick();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOrderSuccessfull() {
        this.finish();
    }

    @Override
    public void onCreateOrderFailure() {
        btn_save.setClickable(true);
    }

    @Override
    public void onShowLoader() {
        if (mDialog != null) {
            mDialog.show();
        }
    }

    @Override
    public void onHideLoader() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    @Override
    public void onErrorMsgReceived(@StringRes int resId) {
        Toast.makeText(contextWeakReference.get(), getResources().getString(resId), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorMsgReceived(String msg) {
        Toast.makeText(contextWeakReference.get(), msg, Toast.LENGTH_SHORT).show();
    }

    private void populateData(String userId, final String type, String designation) {
        if (HttpConnectionUrl.isNetworkAvailable(ActivityOrderCreate.this)) {
            ListContractorDistributorAsync lisLtContractorDistributorAsync = new ListContractorDistributorAsync(ActivityOrderCreate.this, userId, type, designation);
            lisLtContractorDistributorAsync.setOnContentListParserListner(new ListContractorDistributorAsync.OnContentListSchedules() {

                @Override
                public void onResponseData(String response) {
                    Log.i(getClass().getSimpleName(), "onResponseData: " + response);
                    JsonElement jelement = new JsonParser().parse(response);
                    JsonObject mJson = jelement.getAsJsonObject();
                    JsonArray mNewArray = mJson.getAsJsonArray("list");
                    Gson mGson = new Gson();
                    Type listType = new TypeToken<List<DataDistributor>>() {
                    }.getType();
                    mListDistributor = mGson.fromJson(mNewArray.toString(), listType);
                    Log.i(getClass().getSimpleName(), "onResponseData: " + mListDistributor.size());

                    if (name_list.size() < 1) {
                        for (int i = 0; i < mListDistributor.size(); i++) {
                            name_list.add(mListDistributor.get(i).getName());
                        }
                    }

                    if (name_list != null && name_list.size() > 0) {
                        ArrayAdapter adapter = new ArrayAdapter<String>(ActivityOrderCreate.this, android.R.layout.simple_dropdown_item_1line, name_list);
                        login_edtxt_emailmobile.setAdapter(adapter);
                    }


                }

                @Override
                public void OnSuccess(ArrayList<com.nordusk.webservices.List> arrayList) {


//                    if (arrayList != null && arrayList.size() > 0) {
//
//                        for (int i = 0; i < arrayList.size(); i++) {
//                            if (arrayList.get(i).getLatitude() != null && arrayList.get(i).getLatitude().length() > 0
//                                    && arrayList.get(i).getLongitude() != null && arrayList.get(i).getLongitude().length() > 0) {
//                                double lat = 0;
//                                double longi = 0;
//                                lat = Double.parseDouble(arrayList.get(i).getLatitude());
//                                longi = Double.parseDouble(arrayList.get(i).getLongitude());
//                                LatLng latLng = new LatLng(lat, longi);
//                                String title = "";
//                                if (arrayList.get(i).getName() != null && arrayList.get(i).getAddress() != null) {
//                                    if (arrayList.get(i).getTerritory() != null)
//                                        title = arrayList.get(i).getName() + "," + arrayList.get(i).getAddress() + "," + arrayList.get(i).getTerritory();
//                                    else
//                                        title = arrayList.get(i).getName() + "," + arrayList.get(i).getAddress();
//
//                                }
//
//                                mMap.addMarker(new MarkerOptions().position(latLng).title(title));
//                            }
//                        }
//                        double lat = 0;
//                        double longi = 0;
//                        int size = arrayList.size();
//                        lat = Double.parseDouble(arrayList.get(size - 1).getLatitude());
//                        longi = Double.parseDouble(arrayList.get(size - 1).getLongitude());
//                        LatLng latLng = new LatLng(lat, longi);
//                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//                        mMap.animateCamera(CameraUpdateFactory.zoomTo(5));
//
//                    } else {
//                        Toast.makeText(MapsActivityContractorDistributor.this, "No shops found", Toast.LENGTH_SHORT).show();
//                    }

                }

                @Override
                public void OnError(String str_err) {
                    Toast.makeText(ActivityOrderCreate.this, str_err, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void OnConnectTimeout() {

                }
            });
            lisLtContractorDistributorAsync.execute();
        } else {
            Toast.makeText(ActivityOrderCreate.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
        }


    }

    private void ParentPatnerfetch() {

        PrimePatnerAsync primePatnerAsync=new PrimePatnerAsync(ActivityOrderCreate.this);
        primePatnerAsync.setOnContentListParserListner(new PrimePatnerAsync.OnContentListSchedules() {
            @Override
            public void OnSuccess(ArrayList<ParentId> arrayList) {

                tempParentIds = arrayList;

                if (name_list.size() < 1) {
                    for (int i = 0; i < tempParentIds.size(); i++) {
                        name_list.add(tempParentIds.get(i).getName());
                    }
                }

                if (name_list != null && name_list.size() > 0) {
                    ArrayAdapter adapter = new ArrayAdapter<String>(ActivityOrderCreate.this, android.R.layout.simple_dropdown_item_1line, name_list);
                    login_edtxt_emailmobile.setAdapter(adapter);
                }
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
}
