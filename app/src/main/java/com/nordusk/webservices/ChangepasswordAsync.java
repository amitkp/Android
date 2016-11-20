package com.nordusk.webservices;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.nordusk.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by DELL on 17-11-2016.
 */
public class ChangepasswordAsync extends AsyncTask<Void, Void, Void> {

    private Activity context;
    private boolean isTimeOut = false;

    private String responsecode = "";
    private String responseMessage = "";
    private ProgressDialog mpProgressDialog;
    private JSONObject jsonObject = null;
    private String userId="",type="";
    private String desgination="";


    public ChangepasswordAsync(Activity context, String userId, String type, String desgination) {
        this.context = context;
        mpProgressDialog = new ProgressDialog(context);
        mpProgressDialog.setMessage("Loading..");
        mpProgressDialog.show();
        this.userId=userId;
        this.type=type;
        this.desgination= desgination;

        mpProgressDialog.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {



            String[] responsedata = HttpConnectionUrl.post(context, context.getResources().getString(R.string.base_url) + "password_reset.php?"+ "userId="+userId+"&currPass="+type+"&newPass="+ desgination+"", jsonObject);
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
        public void OnSuccess(String message);

        public void OnError(String str_err);

        public void OnConnectTimeout();
    }
}