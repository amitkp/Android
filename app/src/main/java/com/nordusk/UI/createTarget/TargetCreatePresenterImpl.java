package com.nordusk.UI.createTarget;

import android.content.Context;
import android.text.TextUtils;

import com.nordusk.R;
import com.nordusk.utility.Prefs;
import com.nordusk.webservices.rest.RestCallback;
import com.nordusk.webservices.rest.WebApiClient;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by gouravkundu on 16/12/16.
 */

public class TargetCreatePresenterImpl implements TargetCreatePresenter.OnUserInteractionListener,
        Callback<ResponseBody> {

    private TargetCreatePresenter.OnNotifyUiListener mInteractor;
    private String userId = "";

    public TargetCreatePresenterImpl(TargetCreatePresenter.OnNotifyUiListener mInteractor,
                                     Context mContext) {
        this.mInteractor = mInteractor;
        userId = new Prefs(mContext).getString("userid", "");
    }

    @Override
    public void onAddTargetClick(WeakReference<Context> contextWeakReference) {
        if (!TextUtils.isEmpty(mInteractor.getTargetAmount())) {
            if (!TextUtils.isEmpty(mInteractor.getTargetDate())) {
                Retrofit mRetrofit = WebApiClient.getClient(new WeakReference<Context>(contextWeakReference.get()));
                RestCallback.TargetCreateCallback mLoginCallback = mRetrofit.create(RestCallback.TargetCreateCallback.class);
                try {

                    String url = "http://dynamicsglobal.net/app/sp_targer_add.php?userid=" + userId +
                            "&target_date=" + mInteractor.getTargetDate() + "&amount=" +
                            mInteractor.getTargetAmount();

                    Call<ResponseBody> mCall = mLoginCallback.onCreateTarget(url);
                    mCall.enqueue(this);
                    mInteractor.showLoader();
                } catch (Exception e) {
                    e.printStackTrace();
                    mInteractor.hideLoader();
                    mInteractor.onTargetAddFailure();
                }
            } else {
                mInteractor.onTargetAddFailure();
                mInteractor.onErrorMsg(R.string.please_enter_validdate);
                return;
            }
        } else {
            mInteractor.onTargetAddFailure();
            mInteractor.onErrorMsg(R.string.please_enter_amount);
            return;
        }
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (mInteractor != null) {
            mInteractor.hideLoader();
            if (response.code() == 200) {
                mInteractor.onErrorMsg("Target successfully added");
                mInteractor.onTargetAddSuccessFull();
            } else {
                mInteractor.onErrorMsg(response.message());
                mInteractor.onTargetAddFailure();
            }
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        if (mInteractor != null) {
            mInteractor.hideLoader();
            mInteractor.onTargetAddFailure();
            if (t instanceof SocketTimeoutException || t instanceof ConnectException || t instanceof UnknownHostException) {
                mInteractor.onErrorMsg(R.string.txt_CheckNetwork);
            }
        }
    }
}
