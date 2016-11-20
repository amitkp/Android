package com.nordusk.utility;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by DELL on 20-11-2016.
 */
public class Util {
    public static  boolean isServiceRunning=false;

    /**
     * get the device IMEI no and save it into shared preference
     */
    public static String getDeviceId(Context context) {
        String android_device_id = "";
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        android_device_id = telephonyManager.getDeviceId();
        return android_device_id;


    }
}
