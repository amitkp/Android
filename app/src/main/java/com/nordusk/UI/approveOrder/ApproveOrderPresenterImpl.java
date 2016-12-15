package com.nordusk.UI.approveOrder;

import android.content.Context;
import android.os.Bundle;

import com.google.gson.Gson;
import com.nordusk.pojo.DataOrder;

import java.lang.ref.WeakReference;

/**
 * Created by gouravkundu on 13/12/16.
 */

public class ApproveOrderPresenterImpl implements ApproveOrderPresenter.OnUserInteractionListener {

    private ApproveOrderPresenter.OnUiNotifyListener mInteractor;
    private WeakReference<Context> contextWeakReference;

    private String userId = "";

    private DataOrder mDataOrder;

    public ApproveOrderPresenterImpl(ApproveOrderPresenter.OnUiNotifyListener mInteractor, WeakReference<Context> contextWeakReference) {
        this.mInteractor = mInteractor;
        this.contextWeakReference = contextWeakReference;
    }

    @Override
    public void onActivityDestroyed() {
        this.mInteractor = null;
        this.contextWeakReference = null;
    }

    @Override
    public void getOrderDetailsFromBundle(Bundle mBundle, String key) {
        if (mBundle != null) {
            String orderJson = mBundle.getString(key);
            Gson mGson = new Gson();
            mDataOrder = mGson.fromJson(orderJson, DataOrder.class);
        }
    }

    @Override
    public void populateViewAgainstOrder() {
        if (mDataOrder != null && mInteractor != null) {

        }
    }
}
