package com.nordusk.UI.orderCreate;

import android.support.annotation.StringRes;

import com.nordusk.pojo.DataProducts;

import java.util.ArrayList;

/**
 * Created by gouravkundu on 11/12/16.
 */

public interface OrderCreatePresenter {
    interface OnUserInteractionListener{
        void onActivityDestroyed();
        void fetchProductList();
        void onAddProductClick();
        void createOrder(int childCount, Double lat, Double lon);
    }
    interface OnNotifyUiListener{
        void onAddProductItem(ArrayList<DataProducts> mListProducts);
        String getProductDesc(int childPos);
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
