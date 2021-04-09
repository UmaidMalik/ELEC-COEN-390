package com.elec_coen_390.uvme;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class NotificationChannelsClass extends Application {
    public static final String CHANNEL_1_ID ="channel1";
    public static final String CHANNEL_2_ID ="channel2";
    public static final String CHANNEL_3_ID ="channel3";

    @Override
    public void onCreate() {
        super.onCreate();
        creatNotificationChannels();

    }
    private void  creatNotificationChannels(){
        // channel one is used to alert the user if they are exposed to a high level of UVI
        // NOTIF 2.2 Reduce the risk of getting burnt.
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                            NotificationManager.IMPORTANCE_HIGH
            ); // we can look into importance levels for notifications.
            channel1.setDescription("UVI Level Alert ");

            // Notification channel 2 used for sunglasses reminder
            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Channel 2",
                    NotificationManager.IMPORTANCE_LOW
            ); // we can look into importance levels for notifications.
            channel2.setDescription("Sunglasses Alert ");

            // Notification channel 3 used for sunglasses reminder
            NotificationChannel channel3 = new NotificationChannel(
                    CHANNEL_3_ID,
                    "Channel 3",
                    NotificationManager.IMPORTANCE_HIGH
            ); // we can look into importance levels for notifications.
            channel3.setDescription("Sunburn Alert ");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
            manager.createNotificationChannel(channel3);
        }
    }
}
