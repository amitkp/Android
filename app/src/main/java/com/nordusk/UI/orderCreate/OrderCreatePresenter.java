package com.nordusk.UI.orderCreate;

import android.support.annotation.StringRes;

/**
 * Created by gouravkundu on 11/12/16.
 */

public interface OrderCreatePresenter {
    interface OnUserInteractionListener{
        void onActivityDestroyed();
        void onAddProductClick();
        void createOrder(int childCount);
    }
    interface OnNotifyUiListener{
        void onAddProductItem();
        String getProductName(int childPos);
        String getProductId(int childPos);
        String getProductPrice(int childPos);
        String getProductQuantity(int childPos);
        String getOrderFor();
        String getOrderType();
        void onErrorMsgReceived(@StringRes int resId);
        void onErrorMsgReceived(String msg);

        void onCreateOrderSuccessfull();
        void onCreateOrderFailure();
        void onShowLoader();
        void onHideLoader();
    }
}
