package com.nordusk.UI.helper;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.nordusk.R;
import com.nordusk.UI.orderCreate.ArrayAdapterProduct;
import com.nordusk.pojo.DataProducts;

import java.util.ArrayList;

/**
 * Created by gouravkundu on 11/12/16.
 */

public class ViewProductItemCreate extends FrameLayout {

    private int pos;
    private EditText et_productQty, et_productPrice;
    private Spinner spinner;

    ArrayList<DataProducts> mListProducts = new ArrayList<DataProducts>();

    public ViewProductItemCreate(Context context, ArrayList<DataProducts> mListProducts ) {
        super(context);
        this.mListProducts = mListProducts;
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

        spinner = (Spinner) findViewById(R.id.spinner);
        et_productQty = (EditText) findViewById(R.id.et_productQty);
        et_productPrice = (EditText) findViewById(R.id.et_productPrice);

        spinner.setAdapter(new ArrayAdapterProduct(getContext(), R.layout.layout_spinner,  mListProducts));
    }

    public void setPosition(int pos) {
        this.pos = pos;
    }

    public String getProductName(){
        return mListProducts.get(spinner.getSelectedItemPosition()).getName();
    }

    public String getProductPrice(){
        if (TextUtils.isEmpty(et_productPrice.getText().toString())) {
            return "0";
        }else{
            return et_productPrice.getText().toString();
        }
    }

    public String getProductQuantity(){
        return mListProducts.get(spinner.getSelectedItemPosition()).getId();
    }
}
