package com.elec_coen_390.uvme;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class NotificationChannelsClass extends Application {
    public static final String CHANNEL_1_ID ="channel1";
    public static final String CHANNEL_2_ID ="channel2";
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
            channel1.setDescription("High UVI Reading ");


            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Channel 2",
                    NotificationManager.IMPORTANCE_LOW
            ); // we can look into importance levels for notifications.
            channel1.setDescription("Sunglasses Reminder ");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }
    }
}
