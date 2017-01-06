package com.nordusk.webservices;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.nordusk.R;
import com.nordusk.UI.Dashboard;
import com.nordusk.utility.Prefs;
import com.nordusk.utility.Util;

import org.json.JSONObject;

/**
 * Created by DELL on 17-11-2016.
 */
public class LoginAsync extends AsyncTask<Void, Void, Void> {

    private Activity context;
    private boolean isTimeOut = false;

    private String responsecode = "";
    private String responseMessage = "";
    private ProgressDialog mpProgressDialog;
    private JSONObject jsonObject = null;
    private String Username="",Password="";
    private String deviceId="";
    private Double lat,lon;


    public LoginAsync(Activity context, String username,String password, JSONObject jsonObject, Double lat, Double lon) {
        this.context = context;

        this.jsonObject = jsonObject;
        mpProgressDialog = new ProgressDialog(context);
        mpProgressDialog.setMessage("Logging in..");
        mpProgressDialog.show();
        this.Username=username;
        this.Password=password;
        this.deviceId= Util.getDeviceId(context);
        this.lat = lat;
        this.lon = lon;

        mpProgressDialog.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {

            if(lat != null && lat != 0.0 && lon != null && lon != 0.0) {
                String url = context.getResources().getString(R.string.base_url) + context.getResources().getString(R.string.loginasync_url) + "username=" + Username + "&password=" + Password + "&deviceId=" + deviceId + "&latitude" + lat + "&longitude" + lon + "";
                Log.e("Login URL",url);
                String[] responsedata = HttpConnectionUrl.post(context, url, jsonObject);
                isTimeOut = (!TextUtils.isEmpty(responsedata[0]) && responsedata[0].equals(HttpConnectionUrl.RESPONSECODE_REQUESTSUCCESS)) ? false : true;
                if (!isTimeOut && !TextUtils.isEmpty(responsedata[1])) {
                    parseResponseData(responsedata[1]);
                }
            }else{
                Log.e("Login","Unable to get Lat and Lon");
                onContentListParserListner.OnError("Unable to get GPS Data");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (mpProgressDialog != null && mpProgressDialog.isShowing())
            mpProgressDialog.dismiss();
        if (isTimeOut) {
            if (onContentListParserListner != null) {
                onContentListParserListner.OnConnectTimeout();
            }
        }
            if (onContentListParserListner != null && responsecode.equalsIgnoreCase("200")) {
                onContentListParserListner.OnSuccess(responseMessage);

        } else {
                if(responseMessage!=null && responseMessage.length()>0){
                    onContentListParserListner.OnError(responseMessage);
                }else {
                    HttpConnectionUrl.serverErrorMessage(context, responsecode);
                }
        }
    }

    private void parseResponseData(String response) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            responsecode = jsonObject.getString("response_code");
            if (jsonObject != null && responsecode.equalsIgnoreCase("200")) {
                responseMessage = HttpConnectionUrl.getJSONKeyvalue(jsonObject, "response_msg");
//                mobile_number = HttpConnectionUrl.getJSONKeyvalue(jsonObject, "result");

                Prefs my_prefs = new Prefs(context);
                my_prefs.setString("userid",HttpConnectionUrl.getJSONKeyvalue(jsonObject, "userid"));
                my_prefs.setString("name", HttpConnectionUrl.getJSONKeyvalue(jsonObject, "name"));
                my_prefs.setString("mobile_no", HttpConnectionUrl.getJSONKeyvalue(jsonObject, "mobile_no"));
                my_prefs.setString("designation", HttpConnectionUrl.getJSONKeyvalue(jsonObject, "designation"));
                my_prefs.setString("username", HttpConnectionUrl.getJSONKeyvalue(jsonObject, "username"));





            }
            else
            {
                responseMessage = HttpConnectionUrl.getJSONKeyvalue(jsonObject, "response_msg");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private OnContentListSchedules onContentListParserListner;

    public OnContentListSchedules getOnContentListParserListner() {
        return onContentListParserListner;
    }

    public void setOnContentListParserListner(OnContentListSchedules onContentListParserListner) {
        this.onContentListParserListner = onContentListParserListner;
    }

    public interface OnContentListSchedules {
        public void OnSuccess(String responsecode);

        public void OnError(String str_err);

        public void OnConnectTimeout();
    }
}