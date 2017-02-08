package com.nordusk.UI.orderLIst;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.nordusk.R;
import com.nordusk.UI.helper.VerticalSpaceItemDecoration;

import java.lang.ref.WeakReference;

/**
 * Created by gouravkundu on 11/12/16.
 */

public class ActivityOrderList extends AppCompatActivity implements  OrderListPresenter.OnNotifyUiListener{

    private WeakReference<Context> contextWeakReference;
    private OrderListPresenter.OnUserInteractionListener mPresenter;

    private RecyclerView rv;
    private AdapterOrderList mAdapter;

    private void initPresenter() {
        contextWeakReference = new WeakReference<Context>(this);
        mPresenter = new OrderListPresenterImpl(contextWeakReference, this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
        setContentView(R.layout.activity_order_list);
        initView();
        mPresenter.doFetchOrderList();
    }

    private void initView() {

        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(contextWeakReference.get(), LinearLayoutManager.VERTICAL, false));

        rv.addItemDecoration(new VerticalSpaceItemDecoration(5));

        mAdapter = new AdapterOrderList(mPresenter,this);
        rv.setAdapter(mAdapter);
    }

    @Override
    public OrderListPresenter.OnAdapterNotifyListener getAdapterImpl() {
        return mAdapter;
    }

    @Override
    public void onErrorMessageReceived(@StringRes int resId) {

    }

    @Override
    public void onErrorMessageReceived(String msg) {

    }
}
