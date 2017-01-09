package com.nordusk.webservices.rest;

import android.util.Log;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by gouravkundu on 26/07/16.
 */
public interface RestCallback {

    String BASE_URL = "http://dynamicsglobal.net/app/";

    interface OrderListCallback {
        @GET
        Call<ResponseBody> onOrderListReceive(@Url String url);
    }

    interface OrderCreateCallback {
        @GET
        Call<ResponseBody> onCreateOrder(@Url String url);
    }

    interface AddTerritoryCallback {
        @GET
        Call<ResponseBody> onAddTerritory(@Url String url);
    }

    interface TargetCreateCallback {
        @GET
        Call<ResponseBody> onCreateTarget(@Url String url);
    }

    interface TargetListCallback {
        @GET
        Call<ResponseBody> onTargetListFetch(@Url String url);
    }

    interface ProductListCallback {
        @GET("http://dynamicsglobal.net/app/product_list.php")
        Call<ResponseBody> getProductList();

        interface OrderListCallback {
            @GET
            Call<ResponseBody> onOrderListReceive(@Url String url);
        }
    }

    interface AddCounterCallback{
        @Multipart
        @POST("http://dynamicsglobal.net/app/counter_distributer_add.php?")
        Call<ResponseBody> onAddCounterResponse(
                @PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part file);
    }

    interface EditCounterCallback{
        @Multipart
        @POST("http://dynamicsglobal.net/app/counter_distributer_edit.php?")
        Call<ResponseBody> onEditCounterResponse(
                @PartMap() Map<String, RequestBody> partMap, @Part MultipartBody.Part file);
    }
}

