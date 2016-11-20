package com.nordusk.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class Prefs {

    private Context context = null;


    public Prefs(Context context) {
        this.context = context;
    }

    public String getString(String key, String def) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String s = prefs.getString(key, def);
        return s;
    }

    public void setString(String key, String val) {
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor e = prefs.edit();
            e.putString(key, val);
            e.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearData(){
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor e = prefs.edit();
            e.clear().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
