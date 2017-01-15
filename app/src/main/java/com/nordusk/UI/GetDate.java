package com.nordusk.UI;

import android.os.AsyncTask;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by NeeloyG on 1/13/2017.
 */

public class GetDate extends AsyncTask<Void,Void,Void> {
    private String date;

    public GetDate(){}

    @Override
    protected Void doInBackground(Void... params) {
        date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        onContentListParserListner.OnSuccess(date);
    }


    private OnContentListSchedules onContentListParserListner;

    public OnContentListSchedules getOnContentListParserListner() {
        return onContentListParserListner;
    }

    public void setOnContentListParserListner(OnContentListSchedules onContentListParserListner) {
        this.onContentListParserListner = onContentListParserListner;
    }

    public interface OnContentListSchedules {
        public void OnSuccess(String date);

    }
}
