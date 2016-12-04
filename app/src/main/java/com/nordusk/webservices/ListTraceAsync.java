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
public class ListTraceAsync extends AsyncTask<Void, Void, Void> {

    private Activity context;
    private boolean isTimeOut = false;

    private String responsecode = "";
    private String responseMessage = "";
    private ProgressDialog mpProgressDialog;
    private JSONObject jsonObject = null;
    private String userId="",type="";
    TrackDetails lcl_trackDetails=new TrackDetails();


    public ListTraceAsync(Activity context, String userId, String type) {
        this.context = context;
        mpProgressDialog = new ProgressDialog(context);
        mpProgressDialog.setMessage("Loading..");
        mpProgressDialog.show();
        this.userId=userId;
        this.type=type;

        mpProgressDialog.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {



            String[] responsedata = HttpConnectionUrl.post(context, context.getResources().getString(R.string.base_url) + "user_trace.php?"+ "mobile="+userId+"&date="+type+"", jsonObject);
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
                onContentListParserListner.OnSuccess(lcl_trackDetails);

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

                TrackDetails trackDetails=new TrackDetails();

                JSONArray jsonArray=jsonObject.getJSONArray("list");
                ArrayList<PointsTraceList> al_pointsTraceList=new ArrayList<PointsTraceList>();
                if(jsonArray!=null && jsonArray.length()>0){
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject object=jsonArray.getJSONObject(i);
                        PointsTraceList list=new PointsTraceList();
                        list.setId(HttpConnectionUrl.getJSONKeyvalue(object, "id"));
                        list.setLatitude(HttpConnectionUrl.getJSONKeyvalue(object, "latitude"));
                        list.setLongitude(HttpConnectionUrl.getJSONKeyvalue(object, "longitude"));
                        al_pointsTraceList.add(list);

                    }
                    trackDetails.setArr_pointsTraceLists(al_pointsTraceList);
                }

                JSONArray jsonArray_counter=jsonObject.getJSONArray("counter");
                ArrayList<CounterSet> al_counterSets=new ArrayList<CounterSet>();
                if(jsonArray_counter!=null && jsonArray_counter.length()>0){
                    for(int i=0;i<jsonArray_counter.length();i++){
                        JSONObject object=jsonArray_counter.getJSONObject(i);
                        CounterSet list_counter=new CounterSet();
                        list_counter.setId(HttpConnectionUrl.getJSONKeyvalue(object, "id"));
                        list_counter.setLatitude(HttpConnectionUrl.getJSONKeyvalue(object, "latitude"));
                        list_counter.setLongitude(HttpConnectionUrl.getJSONKeyvalue(object, "longitude"));
                        al_counterSets.add(list_counter);

                    }
                    trackDetails.setArr_counterset(al_counterSets);
                }




                lcl_trackDetails=trackDetails;
            }
            else
            {
                responseMessage = "No activity found today";
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
        public void OnSuccess(TrackDetails trackDetails);

        public void OnError(String str_err);

        public void OnConnectTimeout();
    }
}