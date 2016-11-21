package com.nordusk.webservices;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.nordusk.R;
import com.nordusk.utility.Prefs;

import org.json.JSONObject;

/**
 * Created by DELL on 20-11-2016.
 */
public class AddCounterAsync extends AsyncTask<Void, Void, Void>

    {

        private Activity context;
        private boolean isTimeOut = false;

        private String responsecode = "";
        private String responseMessage = "";
        private ProgressDialog mpProgressDialog;
        private JSONObject jsonObject = null;

        private String type="",countername="",mobile="",lattitude="",longitude="",address="",email="";



        public AddCounterAsync(Activity context,String type,String countername,String mobile,String lattitude,String longitude,String address,String email, JSONObject jsonObject) {
        this.context = context;
            this.type=type;
            this.countername=countername;
            this.mobile=mobile;
            this.lattitude=lattitude;
            this.longitude=longitude;
            this.address=address;
            this.email=email;

        this.jsonObject = jsonObject;
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





//            http://dynamicsglobal.net/app/counter_distributer_add.php?"type="+1+"&name="+Counter1+"&address="+saltlake+"&territory="+kolkata+"&anniversary="+2015-11-21+"&dob="+1989-05-08+"&mobile="+9674970045+"&alternative_mobile="+9674970046+"&email="+samotosh@gmail.com+"&latitude="+100+"&longitude="+101+"&userId="+10


            String[] responsedata = HttpConnectionUrl.post(context, context.getResources().getString(R.string.base_url) + context.getResources().getString(R.string.addcounter_url)+ "type="+type+"&name="+countername+"&address="+address+"&territory="+address+"&anniversary="+"2015-11-21"+"&dob="+"1989-05-08"+"&mobile="+mobile+"&alternative_mobile="+mobile+"&email="+email+"&latitude="+lattitude+"&longitude="+longitude+"&userId="+new Prefs(context).getString("userid","")+"", jsonObject);
            //String[] responsedata = HttpConnectionUrl.post(context, context.getResources().getString(R.string.base_url) + context.getResources().getString(R.string.logoutasync_url)+ "userId="+"", jsonObject);
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
//                mobile_number = HttpConnectionUrl.getJSONKeyvalue(jsonObject, "result");


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
        public void OnSuccess(String responsecode);

        public void OnError(String str_err);

        public void OnConnectTimeout();
    }
}
