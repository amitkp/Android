package com.nordusk.webservices;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.JsonElement;
import com.nordusk.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by DELL on 17-11-2016.
 */
public class ListCounterDistributorByManagerTerritoryAsync extends AsyncTask<Void, Void, Void> {

    private Activity context;
    private boolean isTimeOut = false;

    private String responsecode = "";
    private String totalCount = "";
    private String responseMessage = "";
    private ProgressDialog mpProgressDialog;
    private JSONObject jsonObject = null;
    private String sp_id = "", type = "";
    private String territory_id = "";
    private ArrayList<List> arrayList = new ArrayList<List>();


    public ListCounterDistributorByManagerTerritoryAsync(Activity context, String sp_id, String type, String territory_id) {
        this.context = context;
        mpProgressDialog = new ProgressDialog(context);
        mpProgressDialog.setMessage("Loading..");
        mpProgressDialog.show();
        this.sp_id = sp_id;
        this.type = type;
        this.territory_id = territory_id;
        mpProgressDialog.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {


            String[] responsedata = HttpConnectionUrl.post(context, context.getResources().getString(R.string.base_url) + "list_by_territory.php?" +
                    "sales_persion=" + sp_id + "&terittory_id=" + territory_id + "&type=" + type + "", jsonObject);
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
//            onContentListParserListner.OnSuccess(arrayList);
            onContentListParserListner.OnSuccess(arrayList, totalCount);

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
                totalCount = jsonObject.getString("total");
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        List list = new List();
                        list.setId(HttpConnectionUrl.getJSONKeyvalue(object, "id"));
                        list.setMobile(HttpConnectionUrl.getJSONKeyvalue(object, "mobile"));
                        list.setLatitude(HttpConnectionUrl.getJSONKeyvalue(object, "latitude"));
                        list.setLongitude(HttpConnectionUrl.getJSONKeyvalue(object, "longitude"));
                        list.setAddress(HttpConnectionUrl.getJSONKeyvalue(object, "address"));
                        list.setTerritory(HttpConnectionUrl.getJSONKeyvalue(object, "territory"));
                        list.setName(HttpConnectionUrl.getJSONKeyvalue(object, "name"));
                        list.setCreated_at(HttpConnectionUrl.getJSONKeyvalue(object, "created_at"));
                        arrayList.add(list);

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
        void OnSuccess(ArrayList<List> arrayList, String totalCount);

        void OnError(String str_err);

        void OnConnectTimeout();


    }
}