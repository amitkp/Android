package com.nordusk.webservices;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.nordusk.R;
import com.nordusk.pojo.DataObjectAttendance;
import com.nordusk.utility.Prefs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by DELL on 16-12-2016.
 */
public class AttendanceHourlySPAsync extends AsyncTask<Void, Void, Void> {

    private Context context;
    private boolean isTimeOut = false;

    private String responsecode = "";
    private String responseMessage = "";
    private ProgressDialog mpProgressDialog;
    private JSONObject jsonObject = null;
    private String date,sp_id="";
    private Prefs my_prefs;
    private ArrayList<DataObjectAttendance>arrayList=new ArrayList<DataObjectAttendance>();



    public AttendanceHourlySPAsync(Context context, String date, String sp_id) {
        this.context = context;
        this.date=date;
        this.sp_id=sp_id;
         my_prefs = new Prefs(context);
        mpProgressDialog = new ProgressDialog(context);
        mpProgressDialog.setMessage("Loading..");
        mpProgressDialog.show();


        mpProgressDialog.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {

            String url;
            url = context.getResources().getString(R.string.base_url) + context.getResources().getString(R.string.attendance_hourly_url) +"userId="+sp_id+"&date="+date;
            String[] responsedata = HttpConnectionUrl.post(context, url, jsonObject);
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
            onContentListParserListner.OnSuccess(arrayList);
        } else {
            if (responseMessage != null && responseMessage.length() > 0) {
                onContentListParserListner.OnError(responseMessage);
            } else {
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
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        DataObjectAttendance dp = new DataObjectAttendance();
                        dp.setId(HttpConnectionUrl.getJSONKeyvalue(object, "user_id"));
                        dp.setLogin_latitude(HttpConnectionUrl.getJSONKeyvalue(object, "latitude"));
                        dp.setLogin_longitutde(HttpConnectionUrl.getJSONKeyvalue(object, "longitude"));
                        dp.setDate(HttpConnectionUrl.getJSONKeyvalue(object, "date_time"));

                        arrayList.add(dp);
                    }
                }
            } else {
                responseMessage = "No data found";
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
        public void OnSuccess(ArrayList<DataObjectAttendance> arrayList);

        public void OnError(String str_err);

        public void OnConnectTimeout();
    }
}
