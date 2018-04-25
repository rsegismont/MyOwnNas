package com.rsegismont.nasrolene.back.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.MediaStore;

import com.rsegismont.nasrolene.back.application.MonApp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Rolène on 16/03/2018.
 */

public class MonUtils {

    public static String getFileNameFromUri(Uri uri){
        String path = MonUtils.getRealPathFromUri(MonApp.getContext(),uri);
        return path.substring(path.lastIndexOf("/")+1);
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentUri, null,
                null, null, null);

        if (cursor == null) { // Source is Dropbox or other similar local smbFiles
            // path
            result = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            try {
                int idx = cursor
                        .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx);
            } catch (Exception e) {
                e.printStackTrace();

                result = "";
            }
            cursor.close();
        }
        return result;
    }

    public static String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH).format(new Date());
    }

    public String getWifiName(Context context) {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (manager.isWifiEnabled()) {
            WifiInfo wifiInfo = manager.getConnectionInfo();
            if (wifiInfo != null) {
                NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
                if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                    return wifiInfo.getSSID();
                }
            }
        }
        return null;
    }
}
