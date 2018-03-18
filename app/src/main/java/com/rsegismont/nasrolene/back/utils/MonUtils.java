package com.rsegismont.nasrolene.back.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Rolène on 16/03/2018.
 */

public class MonUtils {

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
}
