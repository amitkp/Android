package com.nordusk.webservices.rest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by gouravkundu on 26/07/16.
 */
public interface RestCallback {

    String BASE_URL = "http://dynamicsglobal.net/app/";

    interface OrderListCallback{
        @GET
        Call<ResponseBody> onOrderListReceive(@Url String url);
    }

    interface OrderCreateCallback{
        @GET
        Call<ResponseBody> onCreateOrder(@Url String url);
    }

    interface AddTerritoryCallback{
        @GET
        Call<ResponseBody> onAddTerritory(@Url String url);
    }

    interface TargetCreateCallback{
        @GET
        Call<ResponseBody> onCreateTarget(@Url String url);
    }

    interface TargetListCallback{
        @GET
        Call<ResponseBody> onTargetListFetch(@Url String url);
    }

    interface ProductListCallback{
        @GET("http://dynamicsglobal.net/app/product_list.php")
        Call<ResponseBody> getProductList();

    interface OrderListCallback{
        @GET
        Call<ResponseBody> onOrderListReceive(@Url String url);
    }

}}

