package com.nordusk.UI.orderCreate;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.nordusk.R;
import com.nordusk.pojo.DataOrder;
import com.nordusk.pojo.DataProducts;
import com.nordusk.utility.Prefs;
import com.nordusk.utility.Util;
import com.nordusk.webservices.rest.RestCallback;
import com.nordusk.webservices.rest.WebApiClient;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.net.UnknownHostException;
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

public class OrderCreatePresenterImpl implements OrderCreatePresenter.OnUserInteractionListener, Callback<ResponseBody> {

    private OrderCreatePresenter.OnNotifyUiListener mInteractor;
    private WeakReference<Context> contextWeakReference;

    private String userId = "";
    private String TAG = OrderCreatePresenterImpl.class.getSimpleName();

    ArrayList<DataProducts> mListOrder = new ArrayList<DataProducts>();

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
    public void fetchProductList() {
        mInteractor.onShowLoader();
        Retrofit mRetrofit = WebApiClient.getClient(new WeakReference<Context>(contextWeakReference.get()));
        RestCallback.ProductListCallback mLoginCallback = mRetrofit.create(RestCallback.ProductListCallback.class);
        Call<ResponseBody> mCall = mLoginCallback.getProductList();
        mCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (mInteractor != null) {
                    mInteractor.onHideLoader();
                    if (response.code() == 200) {
                        try {
                            JsonElement jelement = new JsonParser().parse(response.body().string());
                            JsonObject mJson = jelement.getAsJsonObject();
                            JsonArray mNewArray = mJson.getAsJsonArray("list");
                            Gson mGson = new Gson();
                            Type listType = new TypeToken<List<DataProducts>>() {
                            }.getType();
                            mListOrder = mGson.fromJson(mNewArray.toString(), listType);
                            onAddProductClick();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
mInteractor.onHideLoader();
            }
        });
    }

    @Override
    public void onAddProductClick() {
        mInteractor.onAddProductItem(mListOrder);
    }

    @Override
    public void createOrder(int childCount, Double lat, Double lon) {

        Retrofit mRetrofit = WebApiClient.getClient(new WeakReference<Context>(contextWeakReference.get()));
        RestCallback.OrderCreateCallback mLoginCallback = mRetrofit.create(RestCallback.OrderCreateCallback.class);

        try {
            String url = "http://dynamicsglobal.net/app/order_create.php?created_by=" + userId +
                    "&product_name=" + getProductNames(childCount).replaceAll(" ","%20") + "&product_id=" +
                    getProductId(childCount) + "&price=" + getProductPrice(childCount) + "&quantity=" +
                    getProductQty(childCount) + "&order_for=" + Util.ORDER_FOR + "&order_for_type=" + Util.ORDER_FOR_TYPE
                    +"&latitude=" + lat + "&longitude=" + lon;

            Log.e("url",url);

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
                try {
                    JsonElement jelement = new JsonParser().parse(response.body().string());
                    JsonObject mJson = jelement.getAsJsonObject();
                    Gson gson = new Gson();
                    String json = gson.toJson(mJson);
                    JSONObject jsonObject=new JSONObject(json);
                    if(jsonObject.getString("response_code").toString().equalsIgnoreCase("200")){
                        mInteractor.onErrorMsgReceived(R.string.order_created);
                        mInteractor.onCreateOrderSuccessfull();
                    }else{
                        mInteractor.onErrorMsgReceived(jsonObject.getString("response_msg").toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else{

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
