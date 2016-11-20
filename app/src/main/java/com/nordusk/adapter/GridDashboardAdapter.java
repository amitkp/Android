package com.nordusk.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nordusk.R;
import com.nordusk.UI.AddCounter;
import com.nordusk.UI.AddDistributer;
import com.nordusk.UI.MapsActivity;

/**
 * Created by DELL on 17-11-2016.
 */
public class GridDashboardAdapter extends BaseAdapter{

    private int[] img_ids={R.drawable.store,R.drawable.distributor,R.drawable.placeholder,R.drawable.placeholders,R.drawable.placeholder};
    private String[] options_dashboard;

    private Activity mContext;
    private LayoutInflater layoutInflater;

    public GridDashboardAdapter(Activity context)
    {
        this.mContext=context;
        this.layoutInflater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        options_dashboard=context.getResources().getStringArray(R.array.options_dashboard);
    }


    @Override
    public int getCount() {
        return options_dashboard.length;
    }

    @Override
    public Object getItem(int i) {
        return options_dashboard[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        Holder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_dashboard, viewGroup, false);
            holder = new Holder();
            holder.txt_option = (TextView) convertView.findViewById(R.id.dashboard_txt_item);
            holder.img_icon = (ImageView) convertView.findViewById(R.id.dashboard_img_icon);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.txt_option.setText(options_dashboard[position]);
        holder.img_icon.setImageResource(img_ids[position]);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(position==0){
                    Intent intent=new Intent(mContext,AddCounter.class);
                    mContext.startActivity(intent);
                }

                if(position==1){
                    Intent intent=new Intent(mContext,AddDistributer.class);
                    mContext.startActivity(intent);
                }

                if(position==4){
                    Intent intent=new Intent(mContext,MapsActivity.class);
                   mContext.startActivity(intent);
                }
            }
        });

        return convertView;
    }

    public class Holder
    {
        private ImageView img_icon;
        private TextView txt_option;
    }
}
