package com.nordusk.webservices.rest;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gouravkundu on 16/07/16.
 */
public class WebApiClient {

    //TODO Change the url to the web service provided by Sudipta

    private static Retrofit mRetrofit = null;
    public static String uId = "", appKey = "", deviceId = "";

    private static final int CONNECT_TIMEOUT = 30;
    private static final int WRITE_TIMEOUT = 30;
    private static final int READ_TIMEOUT = 30;

    static Gson getInstance() {
        Gson gson = new GsonBuilder().create();

        return gson;
    }

    /**
     * Call this method to get Retrofit instance
     *
     * @param contextWeakReference
     * @return
     */
    public static Retrofit getClient(WeakReference<Context> contextWeakReference) {

        OkHttpClient okHttpClient = new OkHttpClient();

        okHttpClient.newBuilder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);

        mRetrofit = new Retrofit.Builder()
                .baseUrl(RestCallback.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(getInstance()))
                .client(okHttpClient).build();
        return mRetrofit;
    }
}
