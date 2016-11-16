package com.nordusk.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nordusk.MainActivity;
import com.nordusk.R;

public class Login extends AppCompatActivity {

    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginscreen);

        initView();
        SetListener();
    }

    private void initView() {
        btn_login=(Button)findViewById(R.id.login_btn_login);
    }

    private void SetListener() {

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, MainActivity.class));
            }
        });

    }
}
