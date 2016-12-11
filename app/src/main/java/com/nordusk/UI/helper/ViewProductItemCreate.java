package com.nordusk.UI.helper;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.nordusk.R;

/**
 * Created by gouravkundu on 11/12/16.
 */

public class ViewProductItemCreate extends FrameLayout {

    private int pos;
    private EditText et_productName, et_productQty, et_productPrice;

    public ViewProductItemCreate(Context context) {
        super(context);
        initView();
    }

    public ViewProductItemCreate(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ViewProductItemCreate(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ViewProductItemCreate(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_product_item_create, this, true);

        et_productName = (EditText) findViewById(R.id.et_productName);
        et_productQty = (EditText) findViewById(R.id.et_productQty);
        et_productPrice = (EditText) findViewById(R.id.et_productPrice);
    }

    public void setPosition(int pos) {
        this.pos = pos;
    }

    public String getProductName(){
        if (TextUtils.isEmpty(et_productName.getText().toString())) {
            return "0";
        }else{
            return et_productName.getText().toString().trim();
        }
    }

    public String getProductPrice(){
        if (TextUtils.isEmpty(et_productPrice.getText().toString())) {
            return "0";
        }else{
            return et_productPrice.getText().toString();
        }
    }

    public String getProductQuantity(){
        if (TextUtils.isEmpty(et_productQty.getText().toString())) {
            return "0";
        }else{
            return et_productQty.getText().toString();
        }
    }
}
