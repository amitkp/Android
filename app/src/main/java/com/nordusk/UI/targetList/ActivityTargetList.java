package com.nordusk.UI.targetList;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.nordusk.R;
import com.nordusk.UI.helper.VerticalSpaceItemDecoration;
import com.nordusk.pojo.DataTarget;

import java.util.ArrayList;
import java.util.Calendar;

import static java.security.AccessController.getContext;

/**
 * Created by gouravkundu on 16/12/16.
 */

public class ActivityTargetList extends AppCompatActivity
        implements View.OnClickListener, TargetListPresenter.OnNotifyUiListener, DatePickerDialog.OnDateSetListener {

   // private TargetListPresenter.OnUserInteractionListener mPresenter;

    private AdapterTarget mAdapter;

    private TextView tv_date,total_target,total_target_achieved;
    private RecyclerView rv;
    private String sp_id;
    private TargetListPresenter.OnUserInteractionListener mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target_list);
        if(getIntent().getStringExtra("id")!=null && getIntent().getStringExtra("id").length()>0)
            sp_id=getIntent().getStringExtra("id");
        if(sp_id != null && !sp_id.equals("")){
            mPresenter = new TargetListPresenterImpl(this, getBaseContext(),sp_id);
            Log.e("UserID","ActivityTargetList-"+sp_id);
        }else {
            mPresenter = new TargetListPresenterImpl(this, getBaseContext(), "");
            Log.e("UserID","ActivityTargetList-"+sp_id);
        }
        initView();
    }

    private void initView() {

        tv_date = (TextView) findViewById(R.id.tv_date);
        total_target_achieved = (TextView) findViewById(R.id.total_target_achieved);
        total_target = (TextView) findViewById(R.id.total_target);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new AdapterTarget(mPresenter,total_target,total_target_achieved);
        rv.setAdapter(mAdapter);
        rv.addItemDecoration(new VerticalSpaceItemDecoration(5));
        tv_date.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        showDatePickerDialog();
    }

    private void showDatePickerDialog() {
        Calendar date = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, R.style.MyDialogTheme, this,
                date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Log.i("Response", "onDateSet: " + i + i1 + i2);
        tv_date.setText("Selected Date - "+i + "-" + (i1 + 1) + "-" + i2);
        total_target.setText("");
        total_target_achieved.setText("");
        getAdapterImpl().updateTargetElements(new ArrayList<DataTarget>(), new ArrayList<String>());
        mPresenter.onDateSelect(i + "-" + (i1 + 1), getBaseContext(),sp_id);
    }

    @Override
    public TargetListPresenter.OnAdapterNotifyListener getAdapterImpl() {
        return mAdapter;
    }

    @Override
    public void onErrorMsg(@StringRes int resId) {
        onErrorMsg(getBaseContext().getResources().getString(resId));
    }

    @Override
    public void onErrorMsg(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
