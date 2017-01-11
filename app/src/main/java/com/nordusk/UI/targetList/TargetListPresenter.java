package com.nordusk.UI.targetList;

import android.content.Context;
import android.support.annotation.StringRes;

import com.nordusk.pojo.DataTarget;

import java.util.ArrayList;

/**
 * Created by gouravkundu on 16/12/16.
 */

public interface TargetListPresenter {
    interface OnUserInteractionListener{
        void onActivityDestroyed();
        void onDateSelect(String date, Context mContext, String id);
    }

    interface OnNotifyUiListener{
        OnAdapterNotifyListener getAdapterImpl();
        void onErrorMsg(@StringRes int resId);
        void onErrorMsg(String msg);
    }

    interface OnAdapterNotifyListener{
        void updateTargetElements(ArrayList<DataTarget> mListTarget, ArrayList<String> additnalData);
    }
}
