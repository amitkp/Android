package com.nordusk.UI.helper;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nordusk.R;
import com.nordusk.pojo.DataOrder;
import com.nordusk.pojo.DataOrderItem;

/**
 * Created by gouravkundu on 11/12/16.
 */

public class ViewOrderitem extends FrameLayout {

    private LinearLayout ll;
    private TextView tv_name, tv_amount, tv_date;

    private DataOrderItem mItem;

    public ViewOrderitem(Context context) {
        super(context);
        initView();
    }

    public ViewOrderitem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ViewOrderitem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ViewOrderitem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_order_item, this, true);
        ll = (LinearLayout) findViewById(R.id.ll);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_amount = (TextView) findViewById(R.id.tv_amount);
    }

    public void setupView(DataOrder mDataOrder) {
        tv_name.setText(mDataOrder.getName());
        tv_date.setText(getStringFromResource(R.string.date) + ":" + mDataOrder.getDateTime());
        tv_amount.setText(getStringFromResource(R.string.rs) + mDataOrder.getTotalPrice());

        ll.removeAllViews();

        for (int i = 0; i < mDataOrder.getItems().size(); i++) {
            mItem = mDataOrder.getItems().get(i);

            View mView = LayoutInflater.from(getContext()).inflate(R.layout.view_item, ll, false);
            ((TextView) mView.findViewById(R.id.tv_prodName)).setText(mItem.getItemName());
            ((TextView) mView.findViewById(R.id.tv_prodQty)).setText(getStringFromResource(R.string.qty) + ":" +
                    mItem.getItemQuantity());
            ((TextView) mView.findViewById(R.id.tv_prodPrice)).setText(getStringFromResource(R.string.rs) +
                    mItem.getItemPrice());

            ll.addView(mView, i);
        }
    }

    private String getStringFromResource(@StringRes int resId) {
        return getContext().getResources().getString(resId);
    }
}
