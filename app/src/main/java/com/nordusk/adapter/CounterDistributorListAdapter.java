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
import com.nordusk.pojo.DataDistributor;


import java.util.ArrayList;
import java.util.HashMap;


public class CounterDistributorListAdapter extends BaseAdapter {

    private Activity context;
    private LayoutInflater mInflater;

    private ArrayList<DataDistributor> arr_datacounterdis;
    private String calling_tag = "";
    private  String type="";


    public CounterDistributorListAdapter(Activity context, ArrayList<DataDistributor> arr_datacounterdis, String type) {
        this.context = context;
        this.arr_datacounterdis = arr_datacounterdis;
        this.calling_tag = calling_tag;
        this.type=type;

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
            convertView = mInflater.inflate(R.layout.row_dis_counter, parent, false);
            holder = new Holder();

            holder.btn_edit = (Button) convertView.findViewById(R.id.btn_edit);
            holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);


            holder.btn_call = (Button) convertView.findViewById(R.id.btn_call);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }


        DataDistributor dataDistributor = arr_datacounterdis.get(position);
        if (dataDistributor != null) {
            holder.txt_name.setText(dataDistributor.getName());

        }


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

        holder.btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeCall(arr_datacounterdis.get(position).getMobile());
            }
        });

        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(type.equalsIgnoreCase("2")) {
                    Intent intent = new Intent(context, AddDistributer.class);
                    intent.putExtra("from", "edit");
                    intent.putExtra("name",arr_datacounterdis.get(position).getName());
                    intent.putExtra("address",arr_datacounterdis.get(position).getAddress());
                    intent.putExtra("mobile",arr_datacounterdis.get(position).getMobile());
                    intent.putExtra("territory",arr_datacounterdis.get(position).getTerritory());
                    context.startActivity(intent);
                } else
                {



                    Intent intent = new Intent(context, AddCounter.class);
                    intent.putExtra("from", "edit");
                    intent.putExtra("name",arr_datacounterdis.get(position).getName());
                    intent.putExtra("address",arr_datacounterdis.get(position).getAddress());
                    intent.putExtra("mobile",arr_datacounterdis.get(position).getMobile());
                    intent.putExtra("territory",arr_datacounterdis.get(position).getTerritory());
                    context.startActivity(intent);
                }

            }
        });

        return convertView;
    }

    public class Holder {
        public ImageView iv_collect, iv_tick;
        public Button btn_edit, btn_call;
        private TextView txt_name, tv_address;
        private RelativeLayout rlMain;
    }

    public void makeCall(String mobile) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile));
        context.startActivity(intent);
}




}