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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nordusk.R;
import com.nordusk.UI.AddCounter;
import com.nordusk.UI.AddDistributer;
import com.nordusk.UI.CreateNoticeManager;
import com.nordusk.UI.ListCounterDistributorByManagerTerritory;
import com.nordusk.UI.ListManagerTarget;
import com.nordusk.UI.ListOrderVsBill;
import com.nordusk.UI.Listattendance;
import com.nordusk.UI.ListattendanceHourlyReport;
import com.nordusk.UI.MapsActivity;
import com.nordusk.UI.MapsActivityContractorDistributor;
import com.nordusk.UI.createTarget.DialogTargetCreate;
import com.nordusk.UI.dialogTracker.DialogAddTracker;
import com.nordusk.UI.orderCreate.ActivityOrderCreate;
import com.nordusk.UI.orderLIst.ActivityOrderList;
import com.nordusk.UI.targetList.ActivityTargetList;
import com.nordusk.UI.orderLIst.ActivityOrderList;
import com.nordusk.utility.Prefs;
import com.nordusk.utility.Util;
import com.nordusk.webservices.AddManagerTargetAsync;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by DELL on 17-11-2016.
 */
public class GridDashboardAdapterManager extends BaseAdapter {

    private int[] img_ids = {
            R.mipmap.ic_counter, R.mipmap.ic_distributor,
            R.mipmap.ic_track, R.mipmap.ic_teritory,
            R.mipmap.ic_create_target, R.mipmap.ic_target_list,
            R.mipmap.ic_attendance, R.mipmap.ic_order_vs_bill,
            R.mipmap.ic_daily_track, R.mipmap.ic_notice};
    private String[] options_dashboard;

    private AppCompatActivity mContext;
    private LayoutInflater layoutInflater;
    private Prefs mPrefs;
    private SimpleDateFormat dateFormatter;
    ArrayList<String> name_list = new ArrayList<String>();
    ArrayList<String> territory_list = new ArrayList<String>();


    public GridDashboardAdapterManager(AppCompatActivity context) {
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
                    selectCounterDistributorDialog("1");

                } else if (position == 1) {
                    selectCounterDistributorDialog("2");

                } else if (position == 2) {
//                        showTrackDialog("all");
//                    selectDialog();
                    showTrackDialog("all");
                } else if (position == 3) {
                    DialogAddTracker mDialog = DialogAddTracker.newInstance();
                    mDialog.show(((AppCompatActivity) mContext).getSupportFragmentManager(),
                            DialogAddTracker.class.getSimpleName());
                } else if (position == 4) {
                    addTarget();
                } else if (position == 5) {
                    showTargetListDialog();
                    //Intent mIntent = new Intent(mContext, ActivityOrderList.class);
                    //mContext.startActivity(mIntent);
                } else if (position == 6) {
                    showAttendanceDialog();
                } else if(position == 7){
                    orderVsBill();
                } else if (position == 8){
                    dailyTrackingReport();
                } else if (position == 9){
                    createNotice();
                }


//                else if (position == 6) {
//                    DialogTargetCreate mDialog = DialogTargetCreate.newInstance();
//                    mDialog.show(mContext.getSupportFragmentManager(), DialogTargetCreate.class.getSimpleName());
//                } else if (position == 7) {
//                    Intent mIntent = new Intent(mContext, ActivityTargetList.class);
//                    mContext.startActivity(mIntent);
//
//                }
            }

        });

        return convertView;
    }


    private void selectDialog() {

        final CharSequence[] items = {"Track Self", "Track Others"};

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Select Tracking");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Util.checkPermission(mContext);

                if (items[item].equals("Track Self")) {

                    if (result)
                        showTrackDialog("sales");

                } else if (items[item].equals("Track Others")) {

                    if (result)
                        showTrackDialog("all");

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectCounterDistributorDialog(final String type) {

        final CharSequence[] items = {"Self", "Others", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Select Type");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Self")) {

                    if (type.equalsIgnoreCase("1")) {
                        Intent intent = new Intent(mContext, MapsActivityContractorDistributor.class);
                        intent.putExtra("type", "1");
                        mContext.startActivity(intent);
                    } else {
                        Intent intent = new Intent(mContext, MapsActivityContractorDistributor.class);
                        intent.putExtra("type", "2");
                        mContext.startActivity(intent);
                    }

                } else if (items[item].equals("Others")) {
                    showDialogOthersCounterDistributor(type);

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void showDialogOthersCounterDistributor(final String type) {
        final AutoCompleteTextView old_password, login_edtxt_emailmobile_territory;

        final Dialog mDialog_SelectSelectAccount = new Dialog(mContext,
                android.R.style.Theme_DeviceDefault_Light_Dialog);
        mDialog_SelectSelectAccount.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = mDialog_SelectSelectAccount.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        mDialog_SelectSelectAccount.setCancelable(true);
        mDialog_SelectSelectAccount
                .setContentView(R.layout.dialog_others_counter);
        mDialog_SelectSelectAccount.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        old_password = (AutoCompleteTextView) mDialog_SelectSelectAccount.findViewById(R.id.login_edtxt_emailmobile);
        login_edtxt_emailmobile_territory = (AutoCompleteTextView) mDialog_SelectSelectAccount.findViewById(R.id.login_edtxt_emailmobile_territory);

        if (name_list.size() < 1) {
            for (int i = 0; i < Util.getUserList().size(); i++) {
                name_list.add(Util.getUserList().get(i).getName());
            }
        }

        if (name_list != null && name_list.size() > 0) {
            ArrayAdapter adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, name_list);
            old_password.setAdapter(adapter);
        }


        if (territory_list.size() < 1) {
            for (int i = 0; i < Util.getTerritoryList().size(); i++) {
                territory_list.add(Util.getTerritoryList().get(i).getName());
            }
        }

        if (territory_list != null && territory_list.size() > 0) {
            ArrayAdapter adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, territory_list);
            login_edtxt_emailmobile_territory.setAdapter(adapter);
        }

        old_password.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String name = "";
                name = parent.getItemAtPosition(position).toString();
                for (int i = 0; i < Util.getUserList().size(); i++) {
                    if (name.equalsIgnoreCase(Util.getUserList().get(i).getName())) {
                        sp_id = Util.getUserList().get(i).getId();
                    }
                }

            }
        });

        login_edtxt_emailmobile_territory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String name = "";
                name = parent.getItemAtPosition(position).toString();
                for (int i = 0; i < Util.getTerritoryList().size(); i++) {
                    if (name.equalsIgnoreCase(Util.getTerritoryList().get(i).getName())) {
                        territory_id = Util.getTerritoryList().get(i).getId();
                    }
                }

            }
        });

        Button login_btn_login = (Button) mDialog_SelectSelectAccount.findViewById(R.id.login_btn_login);
        login_btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (sp_id != null && sp_id.length() > 0 && territory_id != null && territory_id.length() > 0) {
                    Intent intent = new Intent(mContext, ListCounterDistributorByManagerTerritory.class);
                    intent.putExtra("sp_id", sp_id);
                    intent.putExtra("territory_id", territory_id);
                    intent.putExtra("type", type);
                    mContext.startActivity(intent);
                } else {
                    Toast.makeText(mContext, "Please provide all fields", Toast.LENGTH_SHORT).show();
                }


            }
        });


        mDialog_SelectSelectAccount.show();

    }

    public class Holder {
        private ImageView img_icon;
        private TextView txt_option;
    }

    private String userName = "";
    private String territory_id = "";
    private String sp_id = "";

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

    private void addTarget() {

        final EditText et_date, et_amount;
        final AutoCompleteTextView login_edtxt_emailmobile;
        final Button btn_save;

        final Dialog mDialog_SelectSelectAccount = new Dialog(mContext,
                android.R.style.Theme_DeviceDefault_Light_Dialog);
        mDialog_SelectSelectAccount.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = mDialog_SelectSelectAccount.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        mDialog_SelectSelectAccount.setCancelable(true);
        mDialog_SelectSelectAccount
                .setContentView(R.layout.dialog_target_create_manager);
        mDialog_SelectSelectAccount.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        login_edtxt_emailmobile = (AutoCompleteTextView) mDialog_SelectSelectAccount.findViewById(R.id.login_edtxt_emailmobile);
        et_date = (EditText) mDialog_SelectSelectAccount.findViewById(R.id.et_date);
        et_amount = (EditText) mDialog_SelectSelectAccount.findViewById(R.id.et_amount);
        btn_save = (Button) mDialog_SelectSelectAccount.findViewById(R.id.btn_save);


        if (name_list.size() < 1) {
            for (int i = 0; i < Util.getUserList().size(); i++) {
                name_list.add(Util.getUserList().get(i).getName());
            }
        }

        if (name_list != null && name_list.size() > 0) {
            ArrayAdapter adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, name_list);
            login_edtxt_emailmobile.setAdapter(adapter);
        }

        login_edtxt_emailmobile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String name = "";
                name = parent.getItemAtPosition(position).toString();
                for (int i = 0; i < Util.getUserList().size(); i++) {
                    if (name.equalsIgnoreCase(Util.getUserList().get(i).getName())) {
                        sp_id = Util.getUserList().get(i).getId();
                    }
                }

            }
        });

        et_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        Util.setDateFromDatePicker(et_date, mContext, dateFormatter);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;

                }

                return true;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp_id != null && sp_id.length() > 0
                        && et_amount.getText().toString() != null && et_amount.getText().toString().length() > 0
                        && et_date.getText().toString() != null && et_date.getText().toString().length() > 0) {

                    String date = "";
                    date = et_date.getText().toString().substring(0, 7);
                    Log.e("date", date);

                    AddManagerTargetAsync addManagerTargetAsync = new AddManagerTargetAsync(mContext, date, sp_id, et_amount.getText().toString().trim());
                    addManagerTargetAsync.setOnContentListParserListner(new AddManagerTargetAsync.OnContentListSchedules() {
                        @Override
                        public void OnSuccess(String arrayList) {
                            mDialog_SelectSelectAccount.dismiss();
                            Toast.makeText(mContext, arrayList, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void OnError(String str_err) {
                            mDialog_SelectSelectAccount.dismiss();
                            Toast.makeText(mContext, str_err, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void OnConnectTimeout() {
                            Toast.makeText(mContext, "Check your network connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                    addManagerTargetAsync.execute();

                } else {
                    Toast.makeText(mContext, "Please provide all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mDialog_SelectSelectAccount.show();

    }

    private void showTargetListDialog() {
        final AutoCompleteTextView login_edtxt_emailmobile;
        final Button btn_save;

        final Dialog mDialog_SelectSelectAccount = new Dialog(mContext,
                android.R.style.Theme_DeviceDefault_Light_Dialog);
        mDialog_SelectSelectAccount.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = mDialog_SelectSelectAccount.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        mDialog_SelectSelectAccount.setCancelable(true);
        mDialog_SelectSelectAccount
                .setContentView(R.layout.dialog_target_list_by_sp);
        mDialog_SelectSelectAccount.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        login_edtxt_emailmobile = (AutoCompleteTextView) mDialog_SelectSelectAccount.findViewById(R.id.login_edtxt_emailmobile);
        btn_save = (Button) mDialog_SelectSelectAccount.findViewById(R.id.btn_save);


        if (name_list.size() < 1) {
            for (int i = 0; i < Util.getUserList().size(); i++) {
                name_list.add(Util.getUserList().get(i).getName());
            }
        }

        if (name_list != null && name_list.size() > 0) {
            ArrayAdapter adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, name_list);
            login_edtxt_emailmobile.setAdapter(adapter);
        }


        login_edtxt_emailmobile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String name = "";
                name = parent.getItemAtPosition(position).toString();
                for (int i = 0; i < Util.getUserList().size(); i++) {
                    if (name.equalsIgnoreCase(Util.getUserList().get(i).getName())) {
                        sp_id = Util.getUserList().get(i).getId();
                    }
                }

            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp_id != null && sp_id.length() > 0) {
                    Intent mIntent = new Intent(mContext, ListManagerTarget.class);
                    mIntent.putExtra("sp_id", sp_id);
                    mContext.startActivity(mIntent);
                } else {
                    Toast.makeText(mContext, "Please select a sales person", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mDialog_SelectSelectAccount.show();

    }

    private void showAttendanceDialog() {
        final AutoCompleteTextView login_edtxt_emailmobile;
        final Button btn_save;
        final EditText et_date;

        final Dialog mDialog_SelectSelectAccount = new Dialog(mContext,
                android.R.style.Theme_DeviceDefault_Light_Dialog);
        mDialog_SelectSelectAccount.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = mDialog_SelectSelectAccount.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        mDialog_SelectSelectAccount.setCancelable(true);
        mDialog_SelectSelectAccount
                .setContentView(R.layout.dialog_attendance_sp);
        mDialog_SelectSelectAccount.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        et_date = (EditText) mDialog_SelectSelectAccount.findViewById(R.id.et_date);
        login_edtxt_emailmobile = (AutoCompleteTextView) mDialog_SelectSelectAccount.findViewById(R.id.login_edtxt_emailmobile);
        btn_save = (Button) mDialog_SelectSelectAccount.findViewById(R.id.btn_save);


        if (name_list.size() < 1) {
            for (int i = 0; i < Util.getUserList().size(); i++) {
                name_list.add(Util.getUserList().get(i).getName());
            }
        }

        if (name_list != null && name_list.size() > 0) {
            ArrayAdapter adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, name_list);
            login_edtxt_emailmobile.setAdapter(adapter);
        }

        et_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        Util.setDateFromDatePicker(et_date, mContext, dateFormatter);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;

                }

                return true;
            }
        });


        login_edtxt_emailmobile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String name = "";
                name = parent.getItemAtPosition(position).toString();
                for (int i = 0; i < Util.getUserList().size(); i++) {
                    if (name.equalsIgnoreCase(Util.getUserList().get(i).getName())) {
                        sp_id = Util.getUserList().get(i).getId();
                    }
                }

            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp_id != null && sp_id.length() > 0
                        && et_date.getText().toString() != null && et_date.getText().toString().length() > 0) {
                    String date = "";
                    date = et_date.getText().toString().substring(0, 7);
                    Intent intent = new Intent(mContext, Listattendance.class);
                    intent.putExtra("date", date);
                    intent.putExtra("id", sp_id);
                    mContext.startActivity(intent);

                } else {
                    Toast.makeText(mContext, "Please provide all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mDialog_SelectSelectAccount.show();

    }

    private void orderVsBill() {
        final AutoCompleteTextView login_edtxt_emailmobile;
        final Button create_bill;
        final RadioGroup radio_group;
        final RadioButton radio_counter,radio_disrtibutor,radio_prime_partner;

        final Dialog mDialog_SelectSelectAccount = new Dialog(mContext,
                android.R.style.Theme_DeviceDefault_Light_Dialog);
        mDialog_SelectSelectAccount.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = mDialog_SelectSelectAccount.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        mDialog_SelectSelectAccount.setCancelable(true);
        mDialog_SelectSelectAccount
                .setContentView(R.layout.dialog_order_vs_bill_sp);
        mDialog_SelectSelectAccount.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        radio_group = (RadioGroup) mDialog_SelectSelectAccount.findViewById(R.id.radio_group);
        radio_counter = (RadioButton) mDialog_SelectSelectAccount.findViewById(R.id.radio_counter);
        radio_disrtibutor = (RadioButton) mDialog_SelectSelectAccount.findViewById(R.id.radio_distributor);
        radio_prime_partner = (RadioButton) mDialog_SelectSelectAccount.findViewById(R.id.radio_prime_partner);
        login_edtxt_emailmobile = (AutoCompleteTextView) mDialog_SelectSelectAccount.findViewById(R.id.login_edtxt_emailmobile);
        create_bill = (Button) mDialog_SelectSelectAccount.findViewById(R.id.create_bill);


        if (name_list.size() < 1) {
            for (int i = 0; i < Util.getUserList().size(); i++) {
                name_list.add(Util.getUserList().get(i).getName());
            }
        }

        if (name_list != null && name_list.size() > 0) {
            ArrayAdapter adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, name_list);
            login_edtxt_emailmobile.setAdapter(adapter);
        }


        login_edtxt_emailmobile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String name = "";
                name = parent.getItemAtPosition(position).toString();
                for (int i = 0; i < Util.getUserList().size(); i++) {
                    if (name.equalsIgnoreCase(Util.getUserList().get(i).getName())) {
                        sp_id = Util.getUserList().get(i).getId();
                    }
                }

            }
        });


        create_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String custId = null;
                if(radio_counter.isChecked()){
                    Log.d("Radio","Counter");
                    custId = "1";
                } else if (radio_disrtibutor.isChecked()){
                    Log.d("Radio","Distributor");
                    custId = "2";
                } else if (radio_prime_partner.isChecked()){
                    Log.d("Radio","Prime Partner");
                    custId = "3";
                }else
                Log.d("Radio","No button Checked");
                if (sp_id != null && sp_id.length() > 0 && custId != null) {
                    Intent intent = new Intent(mContext, ListOrderVsBill.class);
                    intent.putExtra("id", sp_id);
                    intent.putExtra("custId", custId);
                    mContext.startActivity(intent);

                } else {
                    Toast.makeText(mContext, "Please provide all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mDialog_SelectSelectAccount.show();

    }


    private void dailyTrackingReport() {
        final AutoCompleteTextView login_edtxt_emailmobile;
        final Button btn_save;
        final EditText et_date;

        final Dialog mDialog_SelectSelectAccount = new Dialog(mContext,
                android.R.style.Theme_DeviceDefault_Light_Dialog);
        mDialog_SelectSelectAccount.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = mDialog_SelectSelectAccount.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        mDialog_SelectSelectAccount.setCancelable(true);
        mDialog_SelectSelectAccount
                .setContentView(R.layout.dialog_daily_tracking_report_sp);
        mDialog_SelectSelectAccount.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        et_date = (EditText) mDialog_SelectSelectAccount.findViewById(R.id.et_date);
        login_edtxt_emailmobile = (AutoCompleteTextView) mDialog_SelectSelectAccount.findViewById(R.id.login_edtxt_emailmobile);
        btn_save = (Button) mDialog_SelectSelectAccount.findViewById(R.id.btn_save);


        if (name_list.size() < 1) {
            for (int i = 0; i < Util.getUserList().size(); i++) {
                name_list.add(Util.getUserList().get(i).getName());
            }
        }

        if (name_list != null && name_list.size() > 0) {
            ArrayAdapter adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, name_list);
            login_edtxt_emailmobile.setAdapter(adapter);
        }

        et_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        Util.setDateFromDatePicker(et_date, mContext, dateFormatter);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;

                }

                return true;
            }
        });


        login_edtxt_emailmobile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String name = "";
                name = parent.getItemAtPosition(position).toString();
                for (int i = 0; i < Util.getUserList().size(); i++) {
                    if (name.equalsIgnoreCase(Util.getUserList().get(i).getName())) {
                        sp_id = Util.getUserList().get(i).getId();
                    }
                }

            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp_id != null && sp_id.length() > 0
                        && et_date.getText().toString() != null && et_date.getText().toString().length() > 0) {
                    String date = "";
                    date = et_date.getText().toString();
                    Log.e("Attendance",date);
                    Intent intent = new Intent(mContext, ListattendanceHourlyReport.class);
                    intent.putExtra("date", date);
                    intent.putExtra("id", sp_id);
                    mContext.startActivity(intent);

                } else {
                    Toast.makeText(mContext, "Please provide all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mDialog_SelectSelectAccount.show();

    }

    private void createNotice(){
        Intent intent = new Intent(mContext, CreateNoticeManager.class);
        mContext.startActivity(intent);
    }
}
