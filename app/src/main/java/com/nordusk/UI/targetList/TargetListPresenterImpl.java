package com.nordusk.UI.targetList;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.nordusk.R;
import com.nordusk.pojo.DataOrder;
import com.nordusk.pojo.DataTarget;
import com.nordusk.utility.Prefs;
import com.nordusk.webservices.rest.RestCallback;
import com.nordusk.webservices.rest.WebApiClient;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by gouravkundu on 16/12/16.
 */

public class TargetListPresenterImpl implements TargetListPresenter.OnUserInteractionListener, Callback<ResponseBody> {

    private TargetListPresenter.OnNotifyUiListener mInteractor;
    private String userId = "";
    private boolean idChanged = false;

    public TargetListPresenterImpl(TargetListPresenter.OnNotifyUiListener mInteractor,
                                   Context mContext, String userIdSP) {

        this.mInteractor = mInteractor;
        if(!userIdSP.equals("")) {
            userId = new Prefs(mContext).getString("userid", "");
            Log.e("UserID","Impl Constructor-"+userId);
        }else{
            userId = userIdSP;
            Log.e("UserID","Impl Constructor-"+userId);
            idChanged = true;
        }
    }

    @Override
    public void onActivityDestroyed() {
        this.mInteractor = null;
    }

    @Override
    public void onDateSelect(String date, Context mContext, String id) {
        if (!TextUtils.isEmpty(date)) {
            if(id != null && id.length()>0){
                userId = id;
            }else{
                userId = new Prefs(mContext).getString("userid", "");
            }
            Log.e("UserID","Impl Async-"+userId);
            Retrofit mRetrofit = WebApiClient.getClient(new WeakReference<Context>(mContext));
            RestCallback.TargetListCallback mLoginCallback = mRetrofit.create(RestCallback.TargetListCallback.class);
            try {
                String url = "http://dynamicsglobal.net/app/sp_targer_list.php?userid=" + userId +
                        "&target_month=" + date;
                Log.e("target_url",url);
                Call<ResponseBody> mCall = mLoginCallback.onTargetListFetch(url);
                mCall.enqueue(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (mInteractor != null) {
            if (response.code() == 200) {
                try {
                    parseResponse(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                mInteractor.onErrorMsg(response.message());
            }
        }
    }

    private void parseResponse(String response) {
        JsonElement jelement = new JsonParser().parse(response);
        JsonObject mJson = jelement.getAsJsonObject();
        JsonArray mNewArray = mJson.getAsJsonArray("list");
        JsonElement data = null,data1 = null;
        try{
            data = mJson.get("total_target_achived");
            data1 = mJson.get("total_target");
            Log.e("target_url","Data-"+data.toString()+"::::Data1-"+data1.toString());
        }catch (Exception e){
            Log.e("target_url",""+e.getMessage());
        }

        Gson mGson = new Gson();
        Type listType = new TypeToken<List<DataTarget>>() {
        }.getType();
        if (mNewArray != null) {
            ArrayList<DataTarget> mListTarget = mGson.fromJson(mNewArray.toString(), listType);
            ArrayList<String> additnalData = new ArrayList<>();
            if(null != data && null != data1){
                additnalData.add(data.toString());
                additnalData.add(data1.toString());
            }
            mInteractor.getAdapterImpl().updateTargetElements(mListTarget,additnalData);
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        if (mInteractor != null) {
            if (t instanceof SocketTimeoutException || t instanceof ConnectException || t instanceof UnknownHostException) {
                mInteractor.onErrorMsg(R.string.txt_CheckNetwork);
            }
        }
    }
}
