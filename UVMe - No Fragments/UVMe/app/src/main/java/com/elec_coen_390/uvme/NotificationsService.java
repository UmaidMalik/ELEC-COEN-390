package com.elec_coen_390.uvme;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationsService extends Service {

    private static final String LOG_TAG = "NotificationsService";

    final Handler handler = new Handler();
    private NotificationManagerCompat notificationManagerCompat;

    SharedPreferences togglePreferences;
    boolean uvi_level_alert_status = false, sunglasses_alert_status = false, sunburn_alert_status = false;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        notificationManagerCompat = NotificationManagerCompat.from(this);

        togglePreferences = getSharedPreferences(NotificationsActivity.PREFS, MODE_PRIVATE);
        startNotificationsThread();


        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        //When remove app from background then start it again
        startService(new Intent(this, NotificationsService.class));

        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "In on Destroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startNotificationsThread() {
        new Thread(runnableNotification).start();
    }


    Runnable runnableNotification = new Runnable() {


        @RequiresApi(api = Build.VERSION_CODES.O)
        public void run() {

            reduceRiskOFBurnNotification(UVSensorData.getUVIntensity());

            handler.postDelayed(this, 200);
        }
    };


    // this function is used to alert user if they are exposed to a high level of UVI!
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void reduceRiskOFBurnNotification(Float data){

        togglePreferences = getSharedPreferences(NotificationsActivity.PREFS, MODE_PRIVATE);
        uvi_level_alert_status = togglePreferences.getBoolean(NotificationsActivity.UVI_LEVEL_ALERT_STATUS, true);
        sunglasses_alert_status = togglePreferences.getBoolean(NotificationsActivity.SUNGLASSES_ALERT_STATUS, true);
        sunburn_alert_status = togglePreferences.getBoolean(NotificationsActivity.SUNBURN_ALERT_STATUS, true);

        if (data >= 5 && data < 8 && uvi_level_alert_status) {
            sendToChannel(R.drawable.ic_sunlight_level3,
                    "SUNBURN ALERT!",
                    "You Are Exposed To: " + UVSensorData.getUVIntensity() + "\n Long Exposure Term May Affect Health",
                    NotificationChannelsClass.CHANNEL_1_ID, 1);

        } if (data >= 8 && data < 20 && uvi_level_alert_status) {
            sendToChannel(R.drawable.ic_sunlight_level5,
                    "SUNBURN ALERT!!!",
                    "You are exposed to a DANGEROUS level of UV Radiation:" + UVSensorData.getUVIntensity() + "\nStay out of sunlight!",
                    NotificationChannelsClass.CHANNEL_1_ID, 1);
        }
    }


    public void sendToChannel(int drawableID, String contentTitle, String contentText, final String notificationChannel , int id) {

        Notification notification=new NotificationCompat.Builder(this, notificationChannel).setSmallIcon(drawableID)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManagerCompat.notify( id, notification);
    }

    public void sendChannel2EyeColorBlue(){
        Notification notification=new NotificationCompat.Builder(this,NotificationChannelsClass.CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_sunglasses)
                .setContentTitle("Hey Blue Eyes")
                .setContentText("UV is Moderate, get your shades on!")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
        notificationManagerCompat.notify(2,notification);
    }
    public void sendChannel2EyeColorGreen(){
        Notification notification=new NotificationCompat.Builder(this,NotificationChannelsClass.CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_sunglasses)
                .setContentTitle("Hey Green Eyes")
                .setContentText("UV is Moderate, get your shades on!")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
        notificationManagerCompat.notify(2,notification);
    }
    public void sendChannel2EyeColorBrown(){
        Notification notification=new NotificationCompat.Builder(this,NotificationChannelsClass.CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_sunglasses)
                .setContentTitle("Hey Brown Eyes")
                .setContentText("UV is Moderate, get your shades on!")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
        notificationManagerCompat.notify(2,notification);
    }
    public void sendChannel2EyeColorHazel(){
        Notification notification=new NotificationCompat.Builder(this,NotificationChannelsClass.CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_sunglasses)
                .setContentTitle("Hey Hazel Eyes")
                .setContentText("UV is Moderate, get your shades on!")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
        notificationManagerCompat.notify(2,notification);
    }


}
