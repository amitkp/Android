package com.nordusk.webservices;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.nordusk.R;
import com.nordusk.UI.Dashboard;

import org.json.JSONObject;

/**
 * Created by DELL on 20-11-2016.
 */
public class LogoutAsync extends AsyncTask<Void, Void, Void> {

    private Activity context;
    private boolean isTimeOut = false;

    private String responsecode = "";
    private String responseMessage = "";
    private String response_msg = "";
    private ProgressDialog mpProgressDialog;
    private JSONObject jsonObject = null;


    public LogoutAsync(Activity context, JSONObject jsonObject) {
        this.context = context;

        this.jsonObject = jsonObject;
        mpProgressDialog = new ProgressDialog(context);
        mpProgressDialog.setMessage("Logging in..");
       
        mpProgressDialog.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {



           // String[] responsedata = HttpConnectionUrl.post(context, context.getResources().getString(R.string.base_url) + context.getResources().getString(R.string.loginasync_url)+ "userId="+ new Prefs(context).getString("userid","")+"", jsonObject);
            String[] responsedata = HttpConnectionUrl.post(context, context.getResources().getString(R.string.base_url) + context.getResources().getString(R.string.logoutasync_url)+ "userId="+"", jsonObject);
            isTimeOut = (!TextUtils.isEmpty(responsedata[0]) && responsedata[0].equals(HttpConnectionUrl.RESPONSECODE_REQUESTSUCCESS)) ? false : true;
            if (!isTimeOut && !TextUtils.isEmpty(responsedata[1])) {
                parseResponseData(responsedata[1]);
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
        } else if (responsecode.equals(HttpConnectionUrl.RESPONSECODE_SUCCESS)) {
            if (onContentListParserListner != null) {
                onContentListParserListner.OnSuccess(responsecode);
            }
        } else if (responsecode.equals(HttpConnectionUrl.RESPONSECODE_INVALIDCREDENTIAL)) {
            onContentListParserListner.OnError(responseMessage);
        } else if (responsecode.equals(HttpConnectionUrl.RESPONSECODE_ERROR)) {
            onContentListParserListner.OnError(responseMessage);
        } else {
            HttpConnectionUrl.serverErrorMessage(context, responsecode);
        }
    }

    private void parseResponseData(String response) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            responsecode = jsonObject.getString("response_code");
            if (jsonObject != null && responsecode.equalsIgnoreCase("200")) {
//                mobile_number = HttpConnectionUrl.getJSONKeyvalue(jsonObject, "result");



                context.startActivity(new Intent(context, Dashboard.class));



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