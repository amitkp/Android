package com.nordusk.UI.orderCreate;

import android.content.Context;
import android.util.Log;

import com.nordusk.R;
import com.nordusk.utility.Prefs;
import com.nordusk.webservices.rest.RestCallback;
import com.nordusk.webservices.rest.WebApiClient;

import java.lang.ref.WeakReference;
import java.net.UnknownHostException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by gouravkundu on 11/12/16.
 */

public class OrderCreatePresenterImpl implements OrderCreatePresenter.OnUserInteractionListener, Callback<ResponseBody> {

    private OrderCreatePresenter.OnNotifyUiListener mInteractor;
    private WeakReference<Context> contextWeakReference;

    private String userId = "";
    private String TAG = OrderCreatePresenterImpl.class.getSimpleName();

    public OrderCreatePresenterImpl(OrderCreatePresenter.OnNotifyUiListener mInteractor, WeakReference<Context> contextWeakReference) {
        this.mInteractor = mInteractor;
        this.contextWeakReference = contextWeakReference;
        userId = new Prefs(contextWeakReference.get()).getString("userid", "");
    }

    @Override
    public void onActivityDestroyed() {
        this.mInteractor = null;
        this.contextWeakReference = null;
    }

    @Override
    public void onAddProductClick() {
        mInteractor.onAddProductItem();
    }

    @Override
    public void createOrder(int childCount) {

        Retrofit mRetrofit = WebApiClient.getClient(new WeakReference<Context>(contextWeakReference.get()));
        RestCallback.OrderCreateCallback mLoginCallback = mRetrofit.create(RestCallback.OrderCreateCallback.class);

        try {
            String url = "http://dynamicsglobal.net/app/order_list.php?created_by=" + userId +
                    "&product_name=" + getProductNames(childCount) + "&product_id=" +
                    getProductId(childCount) + "&price=" + getProductPrice(childCount) + "&quantity=" +
                    getProductQty(childCount) + "&order_for=" + mInteractor.getOrderFor() + "&order_for_type=" + mInteractor.getOrderType();

            Call<ResponseBody> mCall = mLoginCallback.onCreateOrder(url);
            mCall.enqueue(this);
            mInteractor.onShowLoader();
        } catch (Exception e) {
            e.printStackTrace();
            mInteractor.onCreateOrderFailure();
        }
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        Log.i(TAG, "onResponse: " + response.code());
        if (mInteractor != null) {
            mInteractor.onHideLoader();
            mInteractor.onCreateOrderFailure();
            if (response.code() == 200) {
                mInteractor.onErrorMsgReceived(R.string.order_created);
                mInteractor.onCreateOrderSuccessfull();
            }else{
                mInteractor.onErrorMsgReceived(response.message());
            }
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {

        Log.i(TAG, "onResponse: " + t.getMessage());
        if (mInteractor != null) {
            if (t instanceof UnknownHostException) {
                mInteractor.onErrorMsgReceived(R.string.txt_CheckNetwork);
            }
            mInteractor.onHideLoader();
            mInteractor.onCreateOrderFailure();
        }
    }

    private String getProductNames(int count) {
        StringBuilder mProductName = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (i != 0) {
                mProductName.append("|");
            }
            mProductName.append(mInteractor.getProductName(i));
        }
        return mProductName.toString();
    }

    private String getProductId(int count) {
        StringBuilder mBuilder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (i != 0) {
                mBuilder.append("|");
            }
            mBuilder.append(mInteractor.getProductId(i));
        }
        return mBuilder.toString();
    }

    private String getProductPrice(int count) {
        StringBuilder mBuilder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (i != 0) {
                mBuilder.append("|");
            }
            mBuilder.append(mInteractor.getProductPrice(i));
        }
        return mBuilder.toString();
    }

    private String getProductQty(int count) {
        StringBuilder mBuilder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (i != 0) {
                mBuilder.append("|");
            }
            mBuilder.append(mInteractor.getProductQuantity(i));
        }
        return mBuilder.toString();
    }


}
