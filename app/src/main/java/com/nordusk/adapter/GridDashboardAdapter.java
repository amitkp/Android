package com.nordusk.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nordusk.R;

/**
 * Created by DELL on 17-11-2016.
 */
public class GridDashboardAdapter extends BaseAdapter{

    private int[] img_ids={R.drawable.store,R.drawable.distributor,R.drawable.placeholder,R.drawable.placeholders};
    private String[] options_dashboard;

    private Context mContext;
    private LayoutInflater layoutInflater;

    public GridDashboardAdapter(Context context)
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
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
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

        return convertView;
    }

    public class Holder
    {
        private ImageView img_icon;
        private TextView txt_option;
    }
}
