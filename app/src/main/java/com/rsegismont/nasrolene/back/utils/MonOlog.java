package com.rsegismont.nasrolene.back.utils;

import android.util.Log;

public class MonOlog {

    private static boolean  logEnabled = true;

    private static String getLogId(Object caller){
        return "["+caller.getClass().getName().toString()+"]";
    }


    public static void logDebug(Object caller,String message){
        if(logEnabled) {
            Log.e(getLogId(caller), message);
        }
    }

    public static void logInfo(Object caller,String message){
        if(logEnabled) {
            Log.i(getLogId(caller), message);
        }
    }

}
