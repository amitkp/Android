package com.nordusk.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nordusk.R;
import com.nordusk.UI.AddCounter;
import com.nordusk.UI.AddDistributer;
import com.nordusk.UI.orderCreate.ActivityOrderCreate;
import com.nordusk.admin.ListCounterDistributorPrimePartnerAdmin;
import com.nordusk.pojo.DataDistributor;
import com.nordusk.utility.Util;
import com.nordusk.webservices.HttpConnectionUrl;
import com.nordusk.webservices.List;
import com.nordusk.webservices.ParentId;
import com.nordusk.webservices.StockDistributorAsync;

import java.util.ArrayList;
import java.util.HashMap;


public class CounterDistributorListAdapterByManagerTerritory extends BaseAdapter {

    private Activity context;
    private LayoutInflater mInflater;

    private ArrayList<List> arr_datacounterdis;
    private String type = "";

    public CounterDistributorListAdapterByManagerTerritory(Activity context, ArrayList<List> arr_datacounterdis,String type) {
        this.context = context;
        this.arr_datacounterdis = arr_datacounterdis;
        this.type = type;
        this.mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arr_datacounterdis.size();
    }

    @Override
    public Object getItem(int position) {
        return arr_datacounterdis.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Holder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_dis_counter_by_manager_territory, parent, false);
            holder = new Holder();

            holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
            holder.txt_address = (TextView) convertView.findViewById(R.id.txt_address);
            holder.txt_mobile = (TextView) convertView.findViewById(R.id.txt_mobile);
            holder.txt_creation_date = (TextView) convertView.findViewById(R.id.txt_creation_date);
            holder.btn_stock = (Button) convertView.findViewById(R.id.btn_stock);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        if (type.equalsIgnoreCase("2")) {
            holder.btn_stock.setVisibility(View.VISIBLE);
        } else if (type.equalsIgnoreCase("3")) {
            holder.btn_stock.setVisibility(View.VISIBLE);
        } else {
            holder.btn_stock.setVisibility(View.GONE);
        }


        final List dataDistributor = arr_datacounterdis.get(position);
        if (dataDistributor != null) {
            if (dataDistributor.getName() != null)
                holder.txt_name.setText("Name : " + dataDistributor.getName());
            if (dataDistributor.getAddress() != null)
                holder.txt_address.setText("Address : " + dataDistributor.getAddress());
            if (dataDistributor.getMobile() != null)
                holder.txt_mobile.setText("Mobile : " + dataDistributor.getMobile());
            if (dataDistributor.getCreated_at() != null)
                holder.txt_creation_date.setText("Creation Date : " + dataDistributor.getCreated_at());
        }

        holder.btn_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HttpConnectionUrl.isNetworkAvailable(context)) {
                    StockDistributorAsync stockDistributorAsync = null;
                    if (type.equalsIgnoreCase("2")) {
                        stockDistributorAsync = new StockDistributorAsync(context, dataDistributor.getId(), "stock_distributor.php?");
                    } else if (type.equalsIgnoreCase("3")) {
                        stockDistributorAsync = new StockDistributorAsync(context, dataDistributor.getId(), "stock_pp.php?");
                    }
                    if(null != stockDistributorAsync) {
                        stockDistributorAsync.setOnContentListParserListner(new StockDistributorAsync.OnContentListSchedules() {
                            @Override
                            public void OnSuccess(ArrayList<ParentId> arrayList) {

                                if (arrayList != null && arrayList.size() > 0)
                                    daiogselectSp(arrayList);
                            }

                            @Override
                            public void OnError(String str_err) {
                                Toast.makeText(context, str_err, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void OnConnectTimeout() {
                                Toast.makeText(context, "Please check your network connection", Toast.LENGTH_SHORT).show();
                            }
                        });
                        stockDistributorAsync.execute();
                    }
                }else {
                    Toast.makeText(context, "Please check your network connection", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return convertView;
    }

    public class Holder {
        private TextView txt_name, txt_address, txt_mobile, txt_creation_date;
        private Button btn_stock;
    }

    public void makeCall(String mobile) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile));
        context.startActivity(intent);
    }

    private void daiogselectSp(ArrayList<ParentId> arrayList) {
        ListView lv_stock;
        final Dialog mDialog_SelectSelectAccount = new Dialog(context,
                android.R.style.Theme_DeviceDefault_Light_Dialog);
        mDialog_SelectSelectAccount.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = mDialog_SelectSelectAccount.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        mDialog_SelectSelectAccount.setCancelable(true);
        mDialog_SelectSelectAccount
                .setContentView(R.layout.dialog_stock);
        mDialog_SelectSelectAccount.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        lv_stock = (ListView) mDialog_SelectSelectAccount.findViewById(R.id.lv_stock);

        AdapterStock adapterStock = new AdapterStock(context, arrayList);
        lv_stock.setAdapter(adapterStock);


        mDialog_SelectSelectAccount.show();

    }


}