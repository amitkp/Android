package com.nordusk.UI.orderCreate;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nordusk.R;
import com.nordusk.UI.helper.ViewProductItemCreate;

import java.lang.ref.WeakReference;

/**
 * Created by gouravkundu on 11/12/16.
 */

public class ActivityOrderCreate extends AppCompatActivity implements OrderCreatePresenter.OnNotifyUiListener, View.OnClickListener {

    private WeakReference<Context> contextWeakReference;
    private OrderCreatePresenter.OnUserInteractionListener mPresenter;

    private EditText et_orderName;
    private LinearLayout ll_container;

    private Button btn_save;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
        setContentView(R.layout.activity_create_order);
        initView();
        mPresenter.onAddProductClick();
    }

    private void initPresenter() {
        contextWeakReference = new WeakReference<Context>(this);
        mPresenter = new OrderCreatePresenterImpl(this, contextWeakReference);
    }

    private void initView() {
        et_orderName = (EditText) findViewById(R.id.et_orderName);
        ll_container = (LinearLayout) findViewById(R.id.ll_container);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);

        mDialog = new ProgressDialog(contextWeakReference.get());
        mDialog.setCancelable(false);
        mDialog.setMessage(getResources().getString(R.string.please_wait));
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setIndeterminate(true);
    }

    @Override
    public void onAddProductItem() {

        int childCount = ll_container.getChildCount();
        ViewProductItemCreate mView = new ViewProductItemCreate(contextWeakReference.get());
        mView.setPosition(childCount);
        ll_container.addView(mView, childCount);
    }

    @Override
    public String getProductName(int childPos) {
        return ((ViewProductItemCreate) ll_container.getChildAt(childPos)).getProductName();
    }

    @Override
    public String getProductId(int childPos) {
        return "3";
    }

    @Override
    public String getProductPrice(int childPos) {
        return ((ViewProductItemCreate) ll_container.getChildAt(childPos)).getProductPrice();
    }

    @Override
    public String getProductQuantity(int childPos) {
        return ((ViewProductItemCreate) ll_container.getChildAt(childPos)).getProductQuantity();
    }

    @Override
    public String getOrderFor() {
        return "92";
    }

    @Override
    public String getOrderType() {
        return "2";
    }

    @Override
    public void onClick(View view) {
        btn_save.setClickable(false);
        mPresenter.createOrder(ll_container.getChildCount());
    }

    @Override
    public void onCreateOrderSuccessfull() {
        this.finish();
    }

    @Override
    public void onCreateOrderFailure() {
        btn_save.setClickable(true);
    }

    @Override
    public void onShowLoader() {
        if (mDialog != null) {
            mDialog.show();
        }
    }

    @Override
    public void onHideLoader() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    @Override
    public void onErrorMsgReceived(@StringRes int resId) {
        Toast.makeText(contextWeakReference.get(), getResources().getString(resId), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorMsgReceived(String msg) {
        Toast.makeText(contextWeakReference.get(), msg, Toast.LENGTH_SHORT).show();
    }
}
