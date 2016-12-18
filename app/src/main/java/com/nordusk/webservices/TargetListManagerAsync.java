package com.nordusk.webservices;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.nordusk.R;
import com.nordusk.pojo.DataTargetManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by DELL on 17-11-2016.
 */
public class TargetListManagerAsync extends AsyncTask<Void, Void, Void> {

    private Activity context;
    private boolean isTimeOut = false;

    private String responsecode = "";
    private String responseMessage = "";
    private ProgressDialog mpProgressDialog;
    private JSONObject jsonObject = null;
    private String userId="";
    private ArrayList<DataTargetManager>arrayList=new ArrayList<DataTargetManager>();

    private String dataTobeParsed= "";

    public TargetListManagerAsync(Activity context, String userId) {
        this.context = context;
        mpProgressDialog = new ProgressDialog(context);
        mpProgressDialog.setMessage("Loading..");
        mpProgressDialog.show();
        this.userId=userId;

        mpProgressDialog.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {



            String[] responsedata = HttpConnectionUrl.post(context, context.getResources().getString(R.string.base_url) + "manager_targer_list.php?sp_id="+userId, jsonObject);
            isTimeOut = (!TextUtils.isEmpty(responsedata[0]) && responsedata[0].equals(HttpConnectionUrl.RESPONSECODE_REQUESTSUCCESS)) ? false : true;
            if (!isTimeOut && !TextUtils.isEmpty(responsedata[1])) {
                dataTobeParsed = responsedata[1];
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
                JSONArray jsonArray=jsonObject.getJSONArray("list");
                if(jsonArray!=null && jsonArray.length()>0){
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject object=jsonArray.getJSONObject(i);
                        DataTargetManager list=new DataTargetManager();
                        list.setId(HttpConnectionUrl.getJSONKeyvalue(object, "id"));
                        list.setManager_Id(HttpConnectionUrl.getJSONKeyvalue(object, "manager_id"));
                        list.setSp_Id(HttpConnectionUrl.getJSONKeyvalue(object, "sp_id"));
                        list.setAmount(HttpConnectionUrl.getJSONKeyvalue(object, "amount"));
                        list.setTarget_achived(HttpConnectionUrl.getJSONKeyvalue(object, "target_achived"));
                        list.setCreated_at(HttpConnectionUrl.getJSONKeyvalue(object, "created_at"));
                        list.setMonth(HttpConnectionUrl.getJSONKeyvalue(object,"month"));
                        arrayList.add(list);

                    }
                }
            }
            else
            {
                responseMessage = "No Target found";
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
        void OnSuccess(ArrayList<DataTargetManager> arrayList);

        void OnError(String str_err);

        void OnConnectTimeout();

    }
}