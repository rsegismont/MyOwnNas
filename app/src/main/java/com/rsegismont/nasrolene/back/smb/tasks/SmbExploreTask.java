package com.rsegismont.nasrolene.back.smb.tasks;

import android.os.AsyncTask;

import com.rsegismont.nasrolene.back.eventbus.BusProvider;
import com.rsegismont.nasrolene.back.eventbus.events.SmbFileEvent;
import com.rsegismont.nasrolene.back.smb.SmbAuthentication;

import java.net.MalformedURLException;

import jcifs.smb.SmbFile;

/**
 * Created by Rolène on 04/02/2018.
 */

public class SmbExploreTask extends AsyncTask<String,Void,SmbFile> {
    private String mUrl;

    @Override
    protected SmbFile doInBackground(String... strings) {
        mUrl =strings[0];
        try {
            return new SmbFile(mUrl, SmbAuthentication.getInstance().getAuthToken());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override protected void onPostExecute(SmbFile file) {
        if (!isCancelled() && file != null) {
            BusProvider.getInstance().post(new SmbFileEvent(mUrl,file));
        }
    }
}
