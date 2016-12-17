package com.nordusk.UI.dialogTracker;

import android.content.Context;
import android.text.TextUtils;

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
 * Created by gouravkundu on 12/12/16.
 */

public class AddTrackerImpl implements AddTrackerPresenter.OnUserInteractionListener, Callback<ResponseBody> {

    private AddTrackerPresenter.OnNotifyUiListener mInteractor;
    private WeakReference<Context> contextWeakReference;

    private String userId = "";

    public AddTrackerImpl(AddTrackerPresenter.OnNotifyUiListener mInteractor,
                          WeakReference<Context> contextWeakReference) {
        this.mInteractor = mInteractor;
        this.contextWeakReference = contextWeakReference;
        this.userId = new Prefs(contextWeakReference.get()).getString("userid", "");
    }

    @Override
    public void onDialogDismiss() {
        this.mInteractor = null;
        this.contextWeakReference = null;
    }

    @Override
    public void onAddTracker() {
        if (!TextUtils.isEmpty(mInteractor.getLocation())) {
            Retrofit mRetrofit = WebApiClient.getClient(new WeakReference<Context>(contextWeakReference.get()));
            RestCallback.AddTerritoryCallback mLoginCallback = mRetrofit.create(RestCallback.AddTerritoryCallback.class);

            try {
                String url = " http://dynamicsglobal.net/app/territory_add.php?name="+mInteractor.getLocation()+"&created_by="+userId;
                Call<ResponseBody> mCall = mLoginCallback.onAddTerritory(url);
                mCall.enqueue(this);
                mInteractor.showLoader();
            } catch (Exception e) {
                e.printStackTrace();
                mInteractor.onTrackerAddFailure();
            }
        }else{
            mInteractor.onErrorMessageReceive(R.string.please_add_location);
        }

    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (mInteractor != null) {
            mInteractor.hideLoader();
            if (response.code() == 200) {
                mInteractor.onTrackerAddSuccessfull();
            }else{
                mInteractor.onTrackerAddFailure();
            }
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        if (mInteractor != null) {
            mInteractor.hideLoader();
            if (t instanceof UnknownHostException) {
                mInteractor.onErrorMessageReceive(R.string.txt_CheckNetwork);
            }
        }

    }
}
