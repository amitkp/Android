package com.nordusk.webservices.rest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.nordusk.R;
import com.nordusk.pojo.DataProduct;
import com.nordusk.webservices.HttpConnectionUrl;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by DELL on 20-12-2016.
 */
public class ProductListAsync extends AsyncTask<Void, Void, Void> {

    private Context context;
    private boolean isTimeOut = false;

    private String responsecode = "";
    private String responseMessage = "";
    private ProgressDialog mpProgressDialog;
    private JSONObject jsonObject = null;
    private String str_cat="";

    private ArrayList<DataProduct> arrayList = new ArrayList<>();


    public ProductListAsync(Context context,String cat) {
        this.context = context;
        this.str_cat=cat;
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


            String[] responsedata = HttpConnectionUrl.post(context, context.getResources().getString(R.string.base_url) + "product_list.php?"+ "category=" + str_cat + "", jsonObject);
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


//                        ParentId list = new ParentId();
//                        list.setId(HttpConnectionUrl.getJSONKeyvalue(object, "id"));
//                        list.setName(HttpConnectionUrl.getJSONKeyvalue(object, "name"));

                        DataProduct dp=new DataProduct();
                        dp.setId(HttpConnectionUrl.getJSONKeyvalue(object, "id"));
                        dp.setSku(HttpConnectionUrl.getJSONKeyvalue(object, "sku"));
                        dp.setDescription(HttpConnectionUrl.getJSONKeyvalue(object, "description"));
                        dp.setUnit(HttpConnectionUrl.getJSONKeyvalue(object, "unit"));
                        dp.setCategory(HttpConnectionUrl.getJSONKeyvalue(object, "category"));

                        arrayList.add(dp);

                    }
                }


            } else {
                responseMessage = "No parentid found";
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
        public void OnSuccess(ArrayList<DataProduct> arrayList);

        public void OnError(String str_err);

        public void OnConnectTimeout();
    }
}

