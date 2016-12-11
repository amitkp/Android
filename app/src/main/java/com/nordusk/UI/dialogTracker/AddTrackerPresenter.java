package com.nordusk.UI.dialogTracker;

import android.support.annotation.StringRes;

/**
 * Created by gouravkundu on 12/12/16.
 */

public interface AddTrackerPresenter {
    interface OnUserInteractionListener{
        void onDialogDismiss();
        void onAddTracker();
    }
    interface OnNotifyUiListener{
        String getLocation();
        void showLoader();
        void hideLoader();
        void onErrorMessageReceive(@StringRes int resId);
        void onErrorMessageReceive(String msg);
        void onTrackerAddSuccessfull();
        void onTrackerAddFailure();
    }
}
