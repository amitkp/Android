package com.nordusk.UI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nordusk.R;
import com.nordusk.webservices.CreateNoticeSPAsync;
import com.nordusk.webservices.HttpConnectionUrl;

public class CreateNoticeManager extends AppCompatActivity {

    private TextView txt_title,txt_desc;
    private Button btn_submit;
    private String title="",desc="";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notice_manager);
        this.context = CreateNoticeManager.this;
        initView();
    }

    private void initView(){
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_desc = (TextView) findViewById(R.id.txt_desc);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = txt_title.getText().toString().replaceAll(" ","%20");
                desc = txt_desc.getText().toString().replaceAll(" ","%20");
                if (!title.equals("")) {
                    if (!desc.equals("")) {
                        populateData();
                    }else
                        Toast.makeText(v.getContext(),"Create Description",Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(v.getContext(),"Create Title",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateData(){
        if (HttpConnectionUrl.isNetworkAvailable(CreateNoticeManager.this)) {
            CreateNoticeSPAsync createNoticeSPAsync=new CreateNoticeSPAsync(CreateNoticeManager.this,title,desc);
            createNoticeSPAsync.setOnContentListParserListner(new CreateNoticeSPAsync.OnContentListSchedules() {
                @Override
                public void OnSuccess(String msg){
                    if(msg != null && msg.equals("Notice is successfully added")){
                        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void OnError(String str_err) {
                    Toast.makeText(context,str_err,Toast.LENGTH_SHORT).show();
                }
                @Override
                public void OnConnectTimeout() {
                    Toast.makeText(context,"Please check Internet Connection",Toast.LENGTH_SHORT).show();
                }
            });
            createNoticeSPAsync.execute();

        } else {
            Toast.makeText(CreateNoticeManager.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
        }


    }


}
