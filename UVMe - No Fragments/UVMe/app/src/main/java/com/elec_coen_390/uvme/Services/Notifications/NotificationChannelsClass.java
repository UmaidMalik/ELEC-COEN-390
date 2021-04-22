package com.elec_coen_390.uvme.Services.Notifications;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;


/**
 * This class contains the setup for the notification channels
 * 
 */


public class NotificationChannelsClass extends Application {
    public static final String CHANNEL_1_ID ="channel1";
    public static final String CHANNEL_2_ID ="channel2";
    public static final String CHANNEL_3_ID ="channel3";
    public static final String CHANNEL_4_ID ="channel4";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();

    }
    private void  createNotificationChannels(){
        // channel one is used to alert the user if they are exposed to a high level of UVI

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                            NotificationManager.IMPORTANCE_LOW
            );
            channel1.setDescription("UVI Level Alert ");

            // Notification channel 2 used for sunglasses reminder
            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Channel 2",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel2.setDescription("Sunglasses Alert ");

            // Notification channel 3 used for sunburn reminder
            NotificationChannel channel3 = new NotificationChannel(
                    CHANNEL_3_ID,
                    "Channel 3",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel3.setDescription("Sunburn Alert ");

            // Notification channel 4 used for sun exposure limit reached
            NotificationChannel channel4 = new NotificationChannel(
                    CHANNEL_4_ID,
                    "Channel 4",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel3.setDescription("Sunburn Alert ");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
            manager.createNotificationChannel(channel3);
            manager.createNotificationChannel(channel4);
        }
    }
}
