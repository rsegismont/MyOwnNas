package com.rsegismont.nasrolene.ui.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.rsegismont.nasrolene.R;
import com.rsegismont.nasrolene.back.application.MonApp;

/**
 * Created by Rolène on 17/03/2018.
 */

public class NotificationGenerator {

    public static final int NOTIF_PROGRESS_ID = 1;
    public static final int NOTIF_RESULT_ID = 3;

    public static final String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

    @TargetApi(Build.VERSION_CODES.O)
    public static void createNotificationChannel(){
        NotificationManager notificationManager =
                (NotificationManager) MonApp.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
          NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Mes Notifications", NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
    }

    public static NotificationCompat.Builder  generateUploadNotification(NotificationManagerCompat notificationManager,String fileName){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MonApp.getContext().getApplicationContext(), NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_file_upload_black_24dp)
                .setTicker("Sauvegarde en cours d'un fichier")
                //     .setPriority(NotificationGenerator.PRIORITY_MAX)
                .setContentTitle(fileName)
                .setSubText("Sauvegarde en cours d'un fichier").
                setProgress(100,0,false)
                .setContentInfo("Info");

        return notificationBuilder;

    }

    public static NotificationCompat.Builder  generateEndNotification(NotificationManagerCompat notificationManager){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MonApp.getContext().getApplicationContext(), NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_file_download_black_24dp)
                .setTicker("Fichiers sauvegardées")

                //     .setPriority(NotificationGenerator.PRIORITY_MAX)

                .setContentInfo("Info");

        return notificationBuilder;

    }


}
