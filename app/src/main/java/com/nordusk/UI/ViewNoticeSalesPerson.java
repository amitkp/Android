package com.nordusk.UI;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.nordusk.R;
import com.nordusk.adapter.AdapterNoticeBoard;
import com.nordusk.pojo.DataNotice;
import com.nordusk.pojo.DataObjectAttendance;
import com.nordusk.webservices.HttpConnectionUrl;
import com.nordusk.webservices.NoticeBoardSPAsync;

import java.util.ArrayList;

public class ViewNoticeSalesPerson extends AppCompatActivity {

    private ListView list_notice_board;
    private AdapterNoticeBoard adapterNoticeBoard;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = ViewNoticeSalesPerson.this;
        setContentView(R.layout.activity_view_notice_sales_person);
        initView();
        populateData();
    }

    private void populateData() {
        if (HttpConnectionUrl.isNetworkAvailable(context)) {
            NoticeBoardSPAsync noticeBoardSPAsync=new NoticeBoardSPAsync(context);
            noticeBoardSPAsync.setOnContentListParserListner(new NoticeBoardSPAsync.OnContentListSchedules() {
                @Override
                public void OnSuccess(ArrayList<DataNotice> arrayList) {
                    if(arrayList!=null && arrayList.size()>0){
                        adapterNoticeBoard=new AdapterNoticeBoard(context,arrayList);
                        list_notice_board.setAdapter(adapterNoticeBoard);
                    }else {
                        list_notice_board.setAdapter(null);
                    }
                }

                @Override
                public void OnError(String str_err) {
                    Toast.makeText(context, str_err, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void OnConnectTimeout() {
                    Toast.makeText(context, "Please check your network connection", Toast.LENGTH_SHORT).show();
                }
            });
            noticeBoardSPAsync.execute();

        } else {
            Toast.makeText(context, "Please check your network connection", Toast.LENGTH_SHORT).show();
        }


    }

    private void initView() {

        list_notice_board = (ListView) findViewById(R.id.list_notice);
        adapterNoticeBoard=new AdapterNoticeBoard(ViewNoticeSalesPerson.this,new ArrayList<DataNotice>());
        list_notice_board.setAdapter(adapterNoticeBoard);
    }
}
