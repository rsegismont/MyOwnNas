package com.rsegismont.nasrolene.back.smb.jobs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.rsegismont.nasrolene.back.application.MonApp;
import com.rsegismont.nasrolene.back.eventbus.BusProvider;
import com.rsegismont.nasrolene.back.eventbus.events.SmbFileEvent;
import com.rsegismont.nasrolene.back.smb.SmbAuthentication;
import com.rsegismont.nasrolene.back.utils.MonUtils;
import com.rsegismont.nasrolene.ui.notification.NotificationGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

/**
 * Created by Rolène on 18/03/2018.
 */

public class SmbUploadJob extends JobIntentService {
    /**
     * Unique job ID for this service.
     */
    static final int JOB_ID = 1000;
    private static final String EXTRA_URI_LIST = "EXTRA_URI_LIST" ;

    /**
     * Convenience method for enqueuing work in to this service.
     */
    public static void enqueueWork(Context context,ArrayList<Uri> uriToUpload) {
        final Intent work = new Intent();
        work.putParcelableArrayListExtra(EXTRA_URI_LIST,uriToUpload);
        enqueueWork(context, SmbUploadJob.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(Intent intent) {

       ArrayList<Uri> imagesToUpload = intent.getParcelableArrayListExtra(EXTRA_URI_LIST);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(MonApp.getContext());

        List<SmbFile> toReturn = new ArrayList<>();
        final String storagePath = SmbAuthentication.getInstance().getUrl()+SmbAuthentication.getInstance().getPhotoPath()+ MonUtils.getCurrentTimeStamp()+"/";
        NotificationCompat.Builder notifEnded = NotificationGenerator.generateEndNotification(notificationManager);
        try {
            new SmbFile(storagePath, SmbAuthentication.getInstance().getAuthToken()).mkdirs();
            for(Uri uri : imagesToUpload) {



                SmbFile file = getfile(notificationManager,uri,storagePath);
                if(file != null){
                    toReturn.add(file);
                    notifEnded.setContentTitle(toReturn.size()+" fichiers sauvegardés");
                    notificationManager.notify(NotificationGenerator.NOTIF_RESULT_ID,notifEnded.build());

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        BusProvider.getInstance().post(new SmbFileEvent(toReturn));
    }

    private SmbFile getfile(NotificationManagerCompat notificationManager, Uri uri, String storagePath){
        Log.e("DEBUG",""+uri);
        String path = MonUtils.getRealPathFromUri(MonApp.getContext(),uri);


        File file = new File(path);
        final long fileSize = file.length();
        String filename=path.substring(path.lastIndexOf("/")+1);
        String url = storagePath+filename;
        NotificationCompat.Builder notif = NotificationGenerator.generateUploadNotification(notificationManager,filename);

        SmbFile smb = null;
        try {
            smb = new SmbFile(url, SmbAuthentication.getInstance().getAuthToken());
            OutputStream sops = smb.getOutputStream();

            FileInputStream fis = new FileInputStream(path);

            // Transfer bytes from in to out
            int SbufferSize = 5096;

            byte[] Sb = new byte[SbufferSize];
            int progress = 0;
            int SnoOfBytes = 0;
            int totalBytesWrites = 0;
            while ((SnoOfBytes = fis.read(Sb)) != -1){
                sops.write(Sb, 0, SnoOfBytes);
                totalBytesWrites += SnoOfBytes;
                int newProgress = (int) (totalBytesWrites*100/fileSize);
                if(newProgress!=progress){
                    progress=newProgress;
                    Log.e("DEBUG","send="+totalBytesWrites+" size="+fileSize+" progress ="+progress);
                    notif.setProgress(100, progress,false);
                    notificationManager.notify(NotificationGenerator.NOTIF_PROGRESS_ID,notif.build());
                }


            }
            notificationManager.cancel(NotificationGenerator.NOTIF_PROGRESS_ID);


            sops.close();
            fis.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SmbException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  smb;
    }



}
