package com.nordusk.UI.orderLIst;

import android.support.annotation.IntegerRes;
import android.support.annotation.StringRes;

import com.nordusk.pojo.DataOrder;
import com.nordusk.webservices.List;

import java.util.ArrayList;

/**
 * Created by gouravkundu on 11/12/16.
 */

public interface OrderListPresenter {
    interface OnUserInteractionListener{
        void onActivityDestroyed();
        void doFetchOrderList();
    }
    interface OnNotifyUiListener{

        OnAdapterNotifyListener getAdapterImpl();

        void onErrorMessageReceived(@StringRes int resId);
        void onErrorMessageReceived(String msg);
    }
    interface OnAdapterNotifyListener{
        void updateListElement(ArrayList<DataOrder> mListOrder);
    }
}
