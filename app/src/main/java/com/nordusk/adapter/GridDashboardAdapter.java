package com.nordusk.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
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
import com.nordusk.UI.AddCounter;
import com.nordusk.UI.AddDistributer;
import com.nordusk.UI.ListCounterDistributor;
import com.nordusk.UI.MapsActivity;
import com.nordusk.UI.MapsActivityContractorDistributor;
import com.nordusk.UI.ViewNoticeSalesPerson;
import com.nordusk.UI.createTarget.DialogTargetCreate;
import com.nordusk.UI.dialogTracker.DialogAddTracker;
import com.nordusk.UI.orderCreate.ActivityOrderCreate;
import com.nordusk.UI.orderLIst.ActivityOrderList;
import com.nordusk.UI.targetList.ActivityTargetList;
import com.nordusk.UI.orderLIst.ActivityOrderList;
import com.nordusk.utility.Prefs;
import com.nordusk.utility.Util;
import com.nordusk.webservices.ChangepasswordAsync;
import com.nordusk.webservices.HttpConnectionUrl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by DELL on 17-11-2016.
 */
public class GridDashboardAdapter extends BaseAdapter {

    private int[] img_ids = {R.mipmap.ic_counter, R.mipmap.ic_distributor,
            R.mipmap.ic_counter, R.mipmap.ic_distributor,
            R.mipmap.ic_track, R.mipmap.ic_order_create,
            R.mipmap.ic_order_create, R.mipmap.ic_create_target,
            R.mipmap.ic_target_list, R.mipmap.ic_reports};

    private String[] options_dashboard;

    private AppCompatActivity mContext;
    private LayoutInflater layoutInflater;
    private Prefs mPrefs;
    private SimpleDateFormat dateFormatter;
    ArrayList<String> name_list = new ArrayList<String>();
    private String userChoosenTask;


    public GridDashboardAdapter(AppCompatActivity context) {
        this.mContext = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        options_dashboard = context.getResources().getStringArray(R.array.options_dashboard);
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
                    Intent intent = new Intent(mContext, AddCounter.class);
                    intent.putExtra("from", "add");
                    mContext.startActivity(intent);
                } else if (position == 1) {
                    Intent intent = new Intent(mContext, AddDistributer.class);
                    intent.putExtra("from", "add");
                    mContext.startActivity(intent);
                } else if (position == 2) {
                    //Intent intent = new Intent(mContext, MapsActivityContractorDistributor.class);
                    //.putExtra("type", "1");
                    // mContext.startActivity(intent);
                    selectDialog("counter");
                } else if (position == 3) {

                    selectDialog("distributor");


                } else if (position == 4) {
                    if (mPrefs.getString("designation", "").equalsIgnoreCase("2")) {
                        showTrackDialog("all");
                    } else {
                        Toast.makeText(mContext, "Tracking not available for sales persons", Toast.LENGTH_SHORT).show();
                    }
                } else if (position == 5) {

                    selectDialogForOrderCreate();

                } else if (position == 6) {
                    selectDialogForOrderList();

                } else if (position == 7) {
                    DialogTargetCreate mDialog = DialogTargetCreate.newInstance();
                    mDialog.show(mContext.getSupportFragmentManager(), DialogTargetCreate.class.getSimpleName());
                } else if (position == 8) {
                    Intent mIntent = new Intent(mContext, ActivityTargetList.class);
                    mContext.startActivity(mIntent);

                } else if (position == 9) {
                    Intent mIntent = new Intent(mContext, ViewNoticeSalesPerson.class);
                    mContext.startActivity(mIntent);

                }
            }

        });

        return convertView;
    }

    private void selectDialog(final String call_tag) {

        final CharSequence[] items = {"Listview", "Mapview"};

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("View Filter");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
//                boolean result = Util.checkPermission(mContext);

                if (items[item].equals("Listview")) {

//                    if (result)
                    if (call_tag.equalsIgnoreCase("distributor")) {
                        Intent intent = new Intent(mContext, ListCounterDistributor.class);
                        intent.putExtra("type", "2");
                        mContext.startActivity(intent);
                    } else {
                        Intent intent = new Intent(mContext, ListCounterDistributor.class);
                        intent.putExtra("type", "1");
                        mContext.startActivity(intent);
                    }

                } else if (items[item].equals("Mapview")) {

//                    if (result)
                    if (call_tag.equalsIgnoreCase("distributor")) {
                        Intent intent = new Intent(mContext, MapsActivityContractorDistributor.class);
                        intent.putExtra("type", "2");
                        mContext.startActivity(intent);
                    } else {
                        Intent intent = new Intent(mContext, MapsActivityContractorDistributor.class);
                        intent.putExtra("type", "1");
                        mContext.startActivity(intent);
                    }


                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectDialogForOrderCreate() {

        final CharSequence[] items = {"Counter", "Distributor", "Prime Partner"};

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("View Filter");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
//                boolean result = Util.checkPermission(mContext);

                if (items[item].equals("Counter")) {

//                    if (result)
                    Intent mIntent = new Intent(mContext, ActivityOrderCreate.class);
                    Util.ORDER_FOR_TYPE = "1";
                    Util.ORDER_FOR = "";
                    mContext.startActivity(mIntent);


                } else if (items[item].equals("Distributor")) {

//                    if (result)
                    Intent mIntent = new Intent(mContext, ActivityOrderCreate.class);
                    Util.ORDER_FOR_TYPE = "2";
                    Util.ORDER_FOR = "";
                    mContext.startActivity(mIntent);


                } else if (items[item].equals("Prime Partner")) {
                    Intent mIntent = new Intent(mContext, ActivityOrderCreate.class);
                    Util.ORDER_FOR_TYPE = "3";
                    Util.ORDER_FOR = "";
                    mContext.startActivity(mIntent);
                }
            }
        });
        builder.show();
    }

    private void selectDialogForOrderList() {

        final CharSequence[] items = {"Counter", "Distributor", "Prime Partner"};

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("View Filter");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
//                boolean result = Util.checkPermission(mContext);

                if (items[item].equals("Counter")) {

//                    if (result)
                    Util.ORDER_FOR_TYPE = "1";
                    Intent mIntent = new Intent(mContext, ActivityOrderList.class);
                    mContext.startActivity(mIntent);


                } else if (items[item].equals("Distributor")) {

//                    if (result)
                    Util.ORDER_FOR_TYPE = "2";
                    Intent mIntent = new Intent(mContext, ActivityOrderList.class);
                    mContext.startActivity(mIntent);


                } else if (items[item].equals("Prime Partner")) {
                    Util.ORDER_FOR_TYPE = "3";
                    Intent mIntent = new Intent(mContext, ActivityOrderList.class);
                    mContext.startActivity(mIntent);
                }
            }
        });
        builder.show();
    }


    public class Holder {
        private ImageView img_icon;
        private TextView txt_option;
    }

    private String userName = "";

    public void showTrackDialog(final String tag) {

        final EditText new_password;
        final AutoCompleteTextView old_password;

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

        old_password = (AutoCompleteTextView) mDialog_SelectSelectAccount.findViewById(R.id.login_edtxt_emailmobile);
        if (name_list.size() < 1) {
            for (int i = 0; i < Util.getUserList().size(); i++) {
                name_list.add(Util.getUserList().get(i).getName());
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
                for (int i = 0; i < Util.getUserList().size(); i++) {
                    if (name.equalsIgnoreCase(Util.getUserList().get(i).getName())) {
                        userName = Util.getUserList().get(i).getUsername();
                    }
                }

            }
        });


        new_password = (EditText) mDialog_SelectSelectAccount.findViewById(R.id.login_edtxt_pswrd);

        if (tag.equalsIgnoreCase("sales")) {
            old_password.setVisibility(View.GONE);
        }


        Button login_btn_login = (Button) mDialog_SelectSelectAccount.findViewById(R.id.login_btn_login);

        login_btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // validate

                if (tag.equalsIgnoreCase("all")) {


                    if (userName != null && userName.length() > 0
                            &&
                            new_password.getText().toString().trim() != null && new_password.getText().toString().trim().length() > 0) {
                        Intent intent = new Intent(mContext, MapsActivity.class);
                        intent.putExtra("mobile", userName);
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
