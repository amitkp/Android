package com.nordusk.UI.addCounter;

import android.support.annotation.StringRes;

/**
 * Created by gouravkundu on 25/12/16.
 */

public interface AddCounterPresenter {
    interface OnUserInteractionListener{
        String getCounterName();
        String getCounterAddress();
    }
    interface OnNotifyUiListener{
        void onErrorReceived(@StringRes int resId);
        void onErrorResceived(String msg);
    }
}
