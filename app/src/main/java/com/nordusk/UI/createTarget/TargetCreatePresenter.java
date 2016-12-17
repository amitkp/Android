package com.nordusk.UI.createTarget;

import android.content.Context;
import android.support.annotation.StringRes;

import java.lang.ref.WeakReference;

/**
 * Created by gouravkundu on 16/12/16.
 */

public interface TargetCreatePresenter {

    interface OnUserInteractionListener{

        void onAddTargetClick(WeakReference<Context> contextWeakReference);
    }
    interface OnNotifyUiListener{
        String getTargetAmount();
        String getTargetDate();
        void showLoader();
        void hideLoader();
        void onTargetAddSuccessFull();
        void onTargetAddFailure();
        void onErrorMsg(@StringRes int resId);
        void onErrorMsg(String msg);
    }
}
