package com.nordusk.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nordusk.R;
import com.nordusk.UI.AddCounter;
import com.nordusk.UI.AddDistributer;
import com.nordusk.UI.MapsActivity;
import com.nordusk.UI.MapsActivityContractorDistributor;
import com.nordusk.utility.Prefs;
import com.nordusk.utility.Util;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by DELL on 17-11-2016.
 */
public class GridDashboardAdapterManager extends BaseAdapter {

    private int[] img_ids = {R.drawable.placeholder, R.drawable.placeholders, R.drawable.placeholder};
    private String[] options_dashboard;

    private Activity mContext;
    private LayoutInflater layoutInflater;
    private Prefs mPrefs;
    private SimpleDateFormat dateFormatter;


    public GridDashboardAdapterManager(Activity context) {
        this.mContext = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        options_dashboard = context.getResources().getStringArray(R.array.options_dashboard_manager);
        mPrefs = new Prefs(context);
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

                if (position == 0) {
                    Intent intent = new Intent(mContext, MapsActivityContractorDistributor.class);
                    intent.putExtra("type", "1");
                    mContext.startActivity(intent);
                } else if (position == 1) {
                    Intent intent = new Intent(mContext, MapsActivityContractorDistributor.class);
                    intent.putExtra("type", "2");
                    mContext.startActivity(intent);
                } else if (position == 2) {
                        showTrackDialog("all");

                }
            }
        });

        return convertView;
    }

    public class Holder {
        private ImageView img_icon;
        private TextView txt_option;
    }

    public void showTrackDialog(final String tag) {

        final EditText old_password, new_password;
        final Dialog mDialog_SelectSelectAccount = new Dialog(mContext,
                android.R.style.Theme_DeviceDefault_Light_Dialog);
        mDialog_SelectSelectAccount.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = mDialog_SelectSelectAccount.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        mDialog_SelectSelectAccount.setCancelable(true);
        mDialog_SelectSelectAccount
                .setContentView(R.layout.dialog_track);
        mDialog_SelectSelectAccount.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        old_password = (EditText) mDialog_SelectSelectAccount.findViewById(R.id.login_edtxt_emailmobile);
        new_password = (EditText) mDialog_SelectSelectAccount.findViewById(R.id.login_edtxt_pswrd);

        if(tag.equalsIgnoreCase("sales")){
            old_password.setVisibility(View.GONE);
        }


        Button login_btn_login = (Button) mDialog_SelectSelectAccount.findViewById(R.id.login_btn_login);

        login_btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // validate

                if (tag.equalsIgnoreCase("all")) {


                    if (old_password.getText().toString().trim() != null && old_password.getText().toString().trim().length() > 0
                            &&
                            new_password.getText().toString().trim() != null && new_password.getText().toString().trim().length() > 0) {
                        Intent intent = new Intent(mContext, MapsActivity.class);
                        intent.putExtra("mobile", old_password.getText().toString().trim().replaceAll("\\s+", ""));
                        intent.putExtra("date", new_password.getText().toString().trim());
                        intent.putExtra("tag", tag);
                        mContext.startActivity(intent);
                    } else {
                        Toast.makeText(mContext, "Please provide all fields", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    if (new_password.getText().toString().trim() != null && new_password.getText().toString().trim().length() > 0) {

                        Intent intent = new Intent(mContext, MapsActivity.class);
                        intent.putExtra("date", new_password.getText().toString().trim());
                        intent.putExtra("tag", tag);
                        mContext.startActivity(intent);
                    }
                }
            }
        });


        new_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                        Util.setDateFromDatePicker(new_password, mContext, dateFormatter);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;

                }

                return true;
            }
        });

        mDialog_SelectSelectAccount.show();
    }


}
