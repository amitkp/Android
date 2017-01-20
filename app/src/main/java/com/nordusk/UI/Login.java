package com.nordusk.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nordusk.R;
import com.nordusk.utility.Prefs;
import com.nordusk.utility.Util;
import com.nordusk.webservices.HttpConnectionUrl;
import com.nordusk.webservices.LoginAsync;
import com.nordusk.utility.GPSTracker;

public class Login extends AppCompatActivity {

    private Button btn_login;
    private EditText edt_username, edt_password;
    private Double lat,lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginscreen);
        GPSCheck();

    }
    private void GPSCheck(){
        GPSTracker gpsTracker = new GPSTracker(this);
        if(gpsTracker.canGetLocation()){
            lat = gpsTracker.getLatitude();
            lon = gpsTracker.getLongitude();
            if(lat != null && lat != 0.0 && lon != null && lon != 0.0) {
                initView();
                SetListener();
            } else {

            }
        }else{
            gpsTracker.showSettingsAlert();

        }
    }
    private void initView() {
        btn_login = (Button) findViewById(R.id.login_btn_login);
        edt_username = (EditText) findViewById(R.id.login_edtxt_emailmobile);
        edt_password = (EditText) findViewById(R.id.login_edtxt_pswrd);
    }

    private void SetListener() {

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(Login.this, Dashboard.class));

//                if (HttpConnectionUrl.isNetworkAvailable(Login.this)) {
                if (validateInputs())
                    loginAsyncCall();


//                }
//            else {
//                    Toast.makeText(Login.this, getResources().getString(R.string.txt_CheckNetwork), Toast.LENGTH_SHORT).show();
//                }
            }
        });

    }

    private void loginAsyncCall() {

        if(HttpConnectionUrl.isNetworkAvailable(Login.this)) {
            LoginAsync loginAsync = new LoginAsync(Login.this, edt_username.getText().toString().trim(), edt_password.getText().toString().trim(), null, lat, lon);
            loginAsync.setOnContentListParserListner(new LoginAsync.OnContentListSchedules() {
                @Override
                public void OnSuccess(String response_code) {

                    new Prefs(Login.this).setString("UserName",edt_username.getText().toString().trim());
                    new Prefs(Login.this).setString("Password",edt_password.getText().toString().trim());
                    new Prefs(Login.this).setString("LastLogin",Util.getCurrentDate());

                    Toast.makeText(Login.this, response_code, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this, Dashboard.class));
                    finish();

                }

                @Override
                public void OnError(String str_err) {
                    Toast.makeText(Login.this, str_err, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void OnConnectTimeout() {
                    Toast.makeText(Login.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
                }
            });
            loginAsync.execute();
        }else{
            Toast.makeText(Login.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs() {
        boolean validate_status;
        if (!TextUtils.isEmpty(edt_username.getText().toString().trim())) {
            if (!TextUtils.isEmpty(edt_password.getText().toString().trim())) {
                if (edt_password.getText().toString().trim().length() >= 6) {
                    validate_status = true;
                } else {

                    Toast.makeText(Login.this, this.getResources().getString(R.string.txt_passwordLength), Toast.LENGTH_SHORT).show();

                    validate_status = false;
                }
            } else {
                Toast.makeText(Login.this, this.getResources().getString(R.string.ValidateEmptyPwd), Toast.LENGTH_SHORT).show();

                validate_status = false;
            }
        } else {
            Toast.makeText(Login.this, this.getResources().getString(R.string.ValidateEmptyUid), Toast.LENGTH_SHORT).show();

            validate_status = false;
        }

        return validate_status;
    }
}
