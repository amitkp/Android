package com.nordusk.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nordusk.R;
import com.nordusk.UI.AddCounter;
import com.nordusk.UI.AddDistributer;
import com.nordusk.UI.orderCreate.ActivityOrderCreate;
import com.nordusk.pojo.DataDistributor;
import com.nordusk.webservices.List;

import java.util.ArrayList;
import java.util.HashMap;


public class CounterDistributorListAdapterByManagerTerritory extends BaseAdapter {

    private Activity context;
    private LayoutInflater mInflater;

    private ArrayList<List> arr_datacounterdis;
    private String type = "";


    public CounterDistributorListAdapterByManagerTerritory(Activity context, ArrayList<List> arr_datacounterdis) {
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

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }


        List dataDistributor = arr_datacounterdis.get(position);
        if (dataDistributor != null) {
            if (dataDistributor.getName() != null)
                holder.txt_name.setText("Name : " + dataDistributor.getName());
            if (dataDistributor.getAddress() != null)
                holder.txt_address.setText("Address : " + dataDistributor.getAddress());
            if (dataDistributor.getMobile() != null)
                holder.txt_mobile.setText("Mobile : " + dataDistributor.getMobile());

        }


        return convertView;
    }

    public class Holder {
        private TextView txt_name, txt_address, txt_mobile;
    }

    public void makeCall(String mobile) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile));
        context.startActivity(intent);
    }


}