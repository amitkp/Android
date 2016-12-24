package com.nordusk.admin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nordusk.R;
import com.nordusk.utility.Util;
import com.nordusk.webservices.CreateUserAsync;
import com.nordusk.webservices.HttpConnectionUrl;
import com.nordusk.webservices.ManagersAsyncByadmin;
import com.nordusk.webservices.ParentId;
import com.nordusk.webservices.StateAsync;

import java.util.ArrayList;

public class CreateUser extends AppCompatActivity {

    private EditText user_name, user_password, name, mobile;
    private AutoCompleteTextView auto_state, auto_manager;
    private ArrayList<String> state_name_list = new ArrayList<String>();
    private ArrayList<String> manager_name_list = new ArrayList<String>();
    private String state_id, manager_id = "";
    private ArrayList<ParentId> state_list = new ArrayList<ParentId>();
    private ArrayList<ParentId> manager_list = new ArrayList<ParentId>();
    private String level = "";
    private String designation = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user);
        if (getIntent().getStringExtra("designation") != null && getIntent().getStringExtra("designation").length() > 0)
            designation = getIntent().getStringExtra("designation");
        if (getIntent().getStringExtra("level") != null && getIntent().getStringExtra("level").length() > 0)
            level = getIntent().getStringExtra("level");
        initView();
        populateState();

    }

    private void populateManager() {
        if (HttpConnectionUrl.isNetworkAvailable(this)) {
            ManagersAsyncByadmin managersAsyncByadmin = new ManagersAsyncByadmin(CreateUser.this, level);
            managersAsyncByadmin.setOnContentListParserListner(new ManagersAsyncByadmin.OnContentListSchedules() {
                @Override
                public void OnSuccess(ArrayList<ParentId> arrayList) {
                    if (arrayList != null && arrayList.size() > 0) {
                        manager_list = arrayList;
                        for (int i = 0; i < arrayList.size(); i++) {
                            manager_name_list.add(arrayList.get(i).getName());
                            ArrayAdapter adapter = new ArrayAdapter<String>(CreateUser.this, android.R.layout.simple_dropdown_item_1line, manager_name_list);
                            auto_manager.setAdapter(adapter);
                        }
                    }
                }

                @Override
                public void OnError(String str_err) {
                    Toast.makeText(CreateUser.this, str_err, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void OnConnectTimeout() {
                    Toast.makeText(CreateUser.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
                }
            });
            managersAsyncByadmin.execute();
        } else {
            Toast.makeText(this, "Please check your network connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void populateState() {

        if (HttpConnectionUrl.isNetworkAvailable(this)) {
            StateAsync stateAsync = new StateAsync(this);
            stateAsync.setOnContentListParserListner(new StateAsync.OnContentListSchedules() {
                @Override
                public void OnSuccess(ArrayList<ParentId> arrayList) {
                    if (arrayList != null && arrayList.size() > 0) {
                        state_list = arrayList;
                        for (int i = 0; i < arrayList.size(); i++) {
                            state_name_list.add(arrayList.get(i).getName());
                            ArrayAdapter adapter = new ArrayAdapter<String>(CreateUser.this, android.R.layout.simple_dropdown_item_1line, state_name_list);
                            auto_state.setAdapter(adapter);
                        }
                    }

                    populateManager();


                }

                @Override
                public void OnError(String str_err) {
                    Toast.makeText(CreateUser.this, str_err, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void OnConnectTimeout() {
                    Toast.makeText(CreateUser.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
                }
            });
            stateAsync.execute();
        } else {
            Toast.makeText(this, "Please check your network connection", Toast.LENGTH_SHORT).show();
        }
    }


    private void initView() {
        user_name = (EditText) findViewById(R.id.user_name);
        user_password = (EditText) findViewById(R.id.user_password);
        name = (EditText) findViewById(R.id.name);
        mobile = (EditText) findViewById(R.id.mobile);
        auto_state = (AutoCompleteTextView) findViewById(R.id.auto_state);
        auto_manager = (AutoCompleteTextView) findViewById(R.id.auto_manager);

        ArrayAdapter adapter = new ArrayAdapter<String>(CreateUser.this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        auto_state.setAdapter(adapter);

        ArrayAdapter adapterManager = new ArrayAdapter<String>(CreateUser.this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        auto_manager.setAdapter(adapterManager);


        auto_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String name = "";
                name = parent.getItemAtPosition(position).toString();
                for (int i = 0; i < state_list.size(); i++) {
                    if (name.equalsIgnoreCase(state_list.get(i).getName())) {
                        state_id = state_list.get(i).getId();
                    }
                }

            }
        });

        auto_manager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String name = "";
                name = parent.getItemAtPosition(position).toString();
                for (int i = 0; i < manager_list.size(); i++) {
                    if (name.equalsIgnoreCase(manager_list.get(i).getName())) {
                        manager_id = manager_list.get(i).getId();
                    }
                }

            }
        });

        Button counterprofile_btn_submit = (Button) findViewById(R.id.counterprofile_btn_submit);
        counterprofile_btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_name.getText().toString().trim() != null && user_name.getText().toString().trim().length() > 0 &&
                        user_password.getText().toString().trim() != null && user_password.getText().toString().trim().length() > 0
                        && name.getText().toString().trim() != null && name.getText().toString().trim().length() > 0
                        && mobile.getText().toString().trim() != null && mobile.getText().toString().trim().length() > 0) {

                    if (state_id != null && state_id.length() > 0) {
                        if (manager_id != null && manager_id.length() > 0) {

                            if (HttpConnectionUrl.isNetworkAvailable(CreateUser.this)) {
                                String url_part = "";
                                url_part = "username=" + user_name.getText().toString().trim().replaceAll(" ", "%20") +
                                        "&password=" + user_password.getText().toString().trim() +
                                        "&name=" + name.getText().toString().trim().replaceAll(" ", "%20") +
                                        "&mobile_no=" + mobile.getText().toString().trim() +
                                        "&designation_text=SO" + "&designation=" + designation +
                                        "&state=" + state_id + "&manager_id=" + manager_id;

                                CreateUserAsync createUserAsync = new CreateUserAsync(CreateUser.this, url_part);
                                createUserAsync.setOnContentListParserListner(new CreateUserAsync.OnContentListSchedules() {
                                    @Override
                                    public void OnSuccess(String message) {
                                        Toast.makeText(CreateUser.this, message, Toast.LENGTH_SHORT).show();
                                        finish();
                                    }

                                    @Override
                                    public void OnError(String str_err) {
                                        Toast.makeText(CreateUser.this, str_err, Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void OnConnectTimeout() {
                                        Toast.makeText(CreateUser.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                createUserAsync.execute();
                            } else {
                                Toast.makeText(CreateUser.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            Toast.makeText(CreateUser.this, "Please select a manager first", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(CreateUser.this, "Please select a state first", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(CreateUser.this, "Please provide all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
