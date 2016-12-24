package com.nordusk.UI.orderLIst;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.nordusk.R;
import com.nordusk.pojo.DataDistributor;
import com.nordusk.pojo.DataOrder;
import com.nordusk.utility.Prefs;
import com.nordusk.utility.Util;
import com.nordusk.webservices.rest.RestCallback;
import com.nordusk.webservices.rest.WebApiClient;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by gouravkundu on 11/12/16.
 */

public class OrderListPresenterImpl implements
        OrderListPresenter.OnUserInteractionListener, Callback<ResponseBody> {

    String TAG = OrderListPresenterImpl.class.getSimpleName();

    private WeakReference<Context> contextWeakReference;
    private OrderListPresenter.OnNotifyUiListener mInteractor;

    private String userId = "";
    private ProgressDialog mpProgressDialog;


    public OrderListPresenterImpl(WeakReference<Context> contextWeakReference,
                                  OrderListPresenter.OnNotifyUiListener mInteractor) {
        this.contextWeakReference = contextWeakReference;
        this.mInteractor = mInteractor;
        mpProgressDialog=new ProgressDialog(contextWeakReference.get());
        mpProgressDialog.setMessage("Loading...");
        mpProgressDialog.setCancelable(true);
        mpProgressDialog.show();
        userId = new Prefs(contextWeakReference.get()).getString("userid", "");
    }

    @Override
    public void onActivityDestroyed() {
        this.mInteractor = null;
        this.contextWeakReference = null;
        if(mpProgressDialog!=null&&mpProgressDialog.isShowing())
            mpProgressDialog.dismiss();
    }

    @Override
    public void doFetchOrderList() {
        Retrofit mRetrofit = WebApiClient.getClient(new WeakReference<Context>(contextWeakReference.get()));
        RestCallback.OrderListCallback mLoginCallback = mRetrofit.create(RestCallback.OrderListCallback.class);
        String url="";
        if(new Prefs(contextWeakReference.get()).getString("designation", "").equalsIgnoreCase("6")){
            url = "http://dynamicsglobal.net/app/order_list_admin.php?order_for_type="+Util.ORDER_FOR_TYPE;
        }else{
             url = "http://dynamicsglobal.net/app/order_list.php?created_by=" + userId + "&order_for_type="+Util.ORDER_FOR_TYPE;
        }

        Log.e("order_list",url);
        Call<ResponseBody> mCall = mLoginCallback.onOrderListReceive(url);
        mCall.enqueue(this);
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (mInteractor != null) {
            if (response.code() == 200) {
                try {
                    parseResposne(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                    mInteractor.onErrorMessageReceived(R.string.parseError);
                    if(mpProgressDialog!=null&&mpProgressDialog.isShowing())
                        mpProgressDialog.dismiss();
                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                    mInteractor.onErrorMessageReceived(R.string.parseError);
                    if(mpProgressDialog!=null&&mpProgressDialog.isShowing())
                        mpProgressDialog.dismiss();
                }
            }
        }
    }

    private void parseResposne(String response) throws NullPointerException {
        JsonElement jelement = new JsonParser().parse(response);
        JsonObject mJson = jelement.getAsJsonObject();
        JsonArray mNewArray = mJson.getAsJsonArray("list");
        Gson mGson = new Gson();
        Type listType = new TypeToken<List<DataOrder>>() {
        }.getType();
        ArrayList<DataOrder> mListOrder = mGson.fromJson(mNewArray.toString(), listType);
        mInteractor.getAdapterImpl().updateListElement(mListOrder);
        if(mpProgressDialog!=null&&mpProgressDialog.isShowing())
            mpProgressDialog.dismiss();
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        try {
            mInteractor.onErrorMessageReceived(t.getMessage());
            if(mpProgressDialog!=null&&mpProgressDialog.isShowing())
                mpProgressDialog.dismiss();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }
}
