package com.nordusk.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nordusk.R;
import com.nordusk.UI.ListCounterDistributorByManagerTerritory;
import com.nordusk.UI.ListManagerTarget;
import com.nordusk.UI.Listattendance;
import com.nordusk.UI.MapsActivity;
import com.nordusk.UI.MapsActivityContractorDistributor;
import com.nordusk.UI.dialogTracker.DialogAddTracker;
import com.nordusk.UI.orderLIst.ActivityOrderList;
import com.nordusk.admin.CreateUser;
import com.nordusk.admin.ListCounterDistributorPrimePartnerAdmin;
import com.nordusk.utility.Prefs;
import com.nordusk.utility.Util;
import com.nordusk.webservices.AddManagerTargetAsync;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by DELL on 17-11-2016.
 */
public class GridDashboardAdapterAdmin extends BaseAdapter {

    private int[] img_ids = {R.mipmap.ic_counter, R.mipmap.ic_distributor,
            R.mipmap.ic_view_prime_partner,
            R.mipmap.ic_target_list, R.mipmap.ic_create_user};
    private String[] options_dashboard;

    private AppCompatActivity mContext;
    private LayoutInflater layoutInflater;
    private Prefs mPrefs;
    private SimpleDateFormat dateFormatter;
    ArrayList<String> name_list = new ArrayList<String>();
    private String sp_id = "";


    public GridDashboardAdapterAdmin(AppCompatActivity context) {
        this.mContext = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        options_dashboard = context.getResources().getStringArray(R.array.options_dashboard_admin);
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
                    daiogselectSp("0");

                } else if (position == 1) {
                    daiogselectSp("1");

                } else if (position == 2) {
                    daiogselectSp("2");

                } else if (position == 3) {
                    selectCounterDistributorDialog();
                } else if (position == 4) {
                    selectCreateUserDialog();
                }
            }

        });

        return convertView;
    }


    private void selectCounterDistributorDialog() {

        final CharSequence[] items = {"Counter", "Distributor", "Prime Partner"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Select Type");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Counter")) {
                    daiogAdminSp("1");

                } else if (items[item].equals("Distributor")) {
                    daiogAdminSp("2");

                } else if (items[item].equals("Prime Partner")) {
                    daiogAdminSp("3");
                }
            }
        });
        builder.show();
    }

    private void selectCreateUserDialog() {

        final CharSequence[] items = {"Sales Person", "Manager"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Select Type");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Sales Person")) {
                    Intent intent = new Intent(mContext, CreateUser.class);
                    intent.putExtra("designation", "1");
                    intent.putExtra("level", "2");
                    mContext.startActivity(intent);

                } else if (items[item].equals("Manager")) {
                    Intent intent = new Intent(mContext, CreateUser.class);
                    intent.putExtra("designation", "2");
                    intent.putExtra("level", "3");
                    mContext.startActivity(intent);


                }
            }
        });
        builder.show();
    }

    private void daiogAdminSp(final String type) {
        final AutoCompleteTextView old_password;
        final Dialog mDialog_SelectSelectAccount = new Dialog(mContext,
                android.R.style.Theme_DeviceDefault_Light_Dialog);
        mDialog_SelectSelectAccount.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = mDialog_SelectSelectAccount.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        mDialog_SelectSelectAccount.setCancelable(true);
        mDialog_SelectSelectAccount
                .setContentView(R.layout.dialog_admin_sp);
        mDialog_SelectSelectAccount.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        old_password = (AutoCompleteTextView) mDialog_SelectSelectAccount.findViewById(R.id.login_edtxt_emailmobile);

        if (name_list.size() < 1) {
            for (int i = 0; i < Util.getAdminSpList().size(); i++) {
                name_list.add(Util.getAdminSpList().get(i).getName());
            }
        }

        if (name_list != null && name_list.size() > 0) {
            ArrayAdapter adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, name_list);
            old_password.setAdapter(adapter);
        }




        old_password.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String name = "";
                name = parent.getItemAtPosition(position).toString();
                for (int i = 0; i < Util.getAdminSpList().size(); i++) {
                    if (name.equalsIgnoreCase(Util.getAdminSpList().get(i).getName())) {
                        sp_id = Util.getAdminSpList().get(i).getId();
                    }
                }

            }
        });


        Button login_btn_login = (Button) mDialog_SelectSelectAccount.findViewById(R.id.login_btn_login);
        login_btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (sp_id != null && sp_id.length() > 0 ) {
                    Intent intent = new Intent(mContext, ActivityOrderList.class);
                    Util.SP_ID=sp_id;
                    Util.ORDER_FOR_TYPE = type;
                    mContext.startActivity(intent);
                } else {
                    Toast.makeText(mContext, "Please select a sales person first", Toast.LENGTH_SHORT).show();
                }

            }
        });


        mDialog_SelectSelectAccount.show();

    }

    public class Holder {
        private ImageView img_icon;
        private TextView txt_option;
    }

    private void daiogselectSp(final String type) {
        final AutoCompleteTextView old_password;
        final Dialog mDialog_SelectSelectAccount = new Dialog(mContext,
                android.R.style.Theme_DeviceDefault_Light_Dialog);
        mDialog_SelectSelectAccount.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = mDialog_SelectSelectAccount.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        mDialog_SelectSelectAccount.setCancelable(true);
        mDialog_SelectSelectAccount
                .setContentView(R.layout.dialog_admin_sp);
        mDialog_SelectSelectAccount.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        old_password = (AutoCompleteTextView) mDialog_SelectSelectAccount.findViewById(R.id.login_edtxt_emailmobile);

        if (name_list.size() < 1) {
            for (int i = 0; i < Util.getAdminSpList().size(); i++) {
                name_list.add(Util.getAdminSpList().get(i).getName());
            }
        }

        if (name_list != null && name_list.size() > 0) {
            ArrayAdapter adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, name_list);
            old_password.setAdapter(adapter);
        }




        old_password.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String name = "";
                name = parent.getItemAtPosition(position).toString();
                for (int i = 0; i < Util.getAdminSpList().size(); i++) {
                    if (name.equalsIgnoreCase(Util.getAdminSpList().get(i).getName())) {
                        sp_id = Util.getAdminSpList().get(i).getId();
                    }
                }

            }
        });


        Button login_btn_login = (Button) mDialog_SelectSelectAccount.findViewById(R.id.login_btn_login);
        login_btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (sp_id != null && sp_id.length() > 0 ) {
                   if(type.equalsIgnoreCase("0")){
                       Intent intent = new Intent(mContext, ListCounterDistributorPrimePartnerAdmin.class);
                       intent.putExtra("type", "1");
                       intent.putExtra("sp_id", sp_id);
                       mContext.startActivity(intent);
                   }else if(type.equalsIgnoreCase("1")){
                       Toast.makeText(mContext,"Calling id type 2",Toast.LENGTH_SHORT).show();
                       Intent intent = new Intent(mContext, ListCounterDistributorPrimePartnerAdmin.class);
                       intent.putExtra("type", "2");
                       intent.putExtra("sp_id", sp_id);
                       mContext.startActivity(intent);
                   }else if(type.equalsIgnoreCase("2")){
                       Toast.makeText(mContext,"Calling id type 3",Toast.LENGTH_SHORT).show();
                       Intent intent = new Intent(mContext, ListCounterDistributorPrimePartnerAdmin.class);
                       intent.putExtra("type", "3");
                       intent.putExtra("sp_id", sp_id);
                       mContext.startActivity(intent);
                   }
                } else {
                    Toast.makeText(mContext, "Please select a sales person first", Toast.LENGTH_SHORT).show();
                }

            }
        });


        mDialog_SelectSelectAccount.show();

    }






}
