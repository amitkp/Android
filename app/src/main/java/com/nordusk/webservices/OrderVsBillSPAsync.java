package com.nordusk.webservices;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.nordusk.R;
import com.nordusk.pojo.DataObjectOrderVsBill;
import com.nordusk.pojo.DataObjectOrderVsBillDetails;
import com.nordusk.utility.Prefs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by DELL on 16-12-2016.
 */
public class OrderVsBillSPAsync extends AsyncTask<Void, Void, Void> {

    private Context context;
    private boolean isTimeOut = false;

    private String responsecode = "";
    private String responseMessage = "";
    private ProgressDialog mpProgressDialog;
    private JSONObject jsonObject = null;
    private String sp_id,castId="";
    private Prefs my_prefs;
    private ArrayList<DataObjectOrderVsBill> arrayList=new ArrayList<DataObjectOrderVsBill>();

    public OrderVsBillSPAsync(Context context, String sp_id, String castId) {
        this.context = context;
        this.castId=castId;
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


            String[] responsedata = HttpConnectionUrl.post(context, context.getResources().getString(R.string.base_url) + "order_bill_report.php?"+"created_by="+castId+"&order_for_type="+sp_id, jsonObject);
            Log.d("Order",context.getResources().getString(R.string.base_url) + "order_bill_report.php?"+"created_by="+castId+"&order_for_type="+sp_id);
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
                Log.d("Order","jsonArray-"+jsonArray.toString());

                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Log.d("Order","object-"+object.toString());
                        DataObjectOrderVsBill dp = new DataObjectOrderVsBill();
                        dp.setDate(HttpConnectionUrl.getJSONKeyvalue(object, "date_time"));
                        dp.setTotalPrice(HttpConnectionUrl.getJSONKeyvalue(object, "total_price"));
                        dp.setName(HttpConnectionUrl.getJSONKeyvalue(object, "order_for_text"));
                        JSONArray detailArray = object.getJSONArray("items");
                        Log.d("Order","detailArray-"+detailArray.toString());

                        if (detailArray != null && detailArray.length() > 0) {
                            for (int j = 0; j < detailArray.length(); j++) {
                                JSONObject datailObject = detailArray.getJSONObject(j);
                                DataObjectOrderVsBillDetails detailDp = new DataObjectOrderVsBillDetails();
                                detailDp.setName(HttpConnectionUrl.getJSONKeyvalue(datailObject, "item_name"));
                                detailDp.setPrice(HttpConnectionUrl.getJSONKeyvalue(datailObject, "item_price"));
                                detailDp.setQuantity(HttpConnectionUrl.getJSONKeyvalue(datailObject, "item_quantity"));
                                detailDp.setApprQuantity(HttpConnectionUrl.getJSONKeyvalue(datailObject, "approved_quantity"));
                                dp.setListDetails(detailDp);
                            }
                        }
                        //Add logic to print listDetails
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
        public void OnSuccess(ArrayList<DataObjectOrderVsBill> arrayList);

        public void OnError(String str_err);

        public void OnConnectTimeout();
    }
}
