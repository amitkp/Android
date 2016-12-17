package com.nordusk.UI.approveOrder;

import android.os.Bundle;
import android.support.annotation.StringRes;

import com.nordusk.pojo.DataOrderItem;

/**
 * Created by gouravkundu on 13/12/16.
 */

public interface ApproveOrderPresenter {
    interface OnUserInteractionListener{
        void onActivityDestroyed();
        void getOrderDetailsFromBundle(Bundle mBundle, String key);
        void populateViewAgainstOrder();
    }
    interface OnUiNotifyListener{
        void onErrorMessage(@StringRes int resId);
        void onErrorMessage(String msg);
        void updateUiAgainstOrder(String name, String date, String amount);
        void updateUiAgainstProducts(DataOrderItem mItem);
    }
}
