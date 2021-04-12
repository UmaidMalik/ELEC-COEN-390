package com.elec_coen_390.uvme;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import java.math.*;

public class NotificationsService extends Service {

    private static final String LOG_TAG = "NotificationsService";

    final Handler handler = new Handler();
    private NotificationManagerCompat notificationManagerCompat;

    SharedPreferences togglePreferences;
    boolean uvi_level_alert_status = false, sunglasses_alert_status = false, sunburn_alert_status = false;

    SharedPreferences prefsSkin;
    private int id_skin;

    private final int SKIN_TYPE_PALE = 0;
    private final int SKIN_TYPE_FAIR = 1;
    private final int SKIN_TYPE_MEDIUM = 2;
    private final int SKIN_TYPE_OLIVE = 3;
    private final int SKIN_TYPE_BROWN = 4;
    private final int SKIN_TYPE_BLACK = 5;

    private int minutesToBurn = 0;
    private float uvMax = 0;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        notificationManagerCompat = NotificationManagerCompat.from(this);

        togglePreferences = getSharedPreferences(NotificationsActivity.PREFS, MODE_PRIVATE);
        startNotificationsThread();
        startMaxUVThread();

        prefsSkin = getSharedPreferences(ProfileActivity.prefNameSkin, MODE_PRIVATE);
        id_skin = prefsSkin.getInt("last_val_skin", 0);


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

            setupNotificationPreferences();
            reduceRiskOFBurnNotification(UVSensorData.getUVIntensity());
            sunburnNotification();

            handler.postDelayed(this, 200);
        }
    };

    private void startMaxUVThread() {
        new Thread(runnableUpdateMaxUV).start();
    }

    Runnable runnableUpdateMaxUV = new Runnable() {

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void run() {
            maxUV();
            handler.postDelayed(this, 5);
        }
    };


    public void setupNotificationPreferences() {
        togglePreferences = getSharedPreferences(NotificationsActivity.PREFS, MODE_PRIVATE);
        id_skin = prefsSkin.getInt("last_val_skin", 0);
        uvi_level_alert_status = togglePreferences.getBoolean(NotificationsActivity.UVI_LEVEL_ALERT_STATUS, true);
        sunglasses_alert_status = togglePreferences.getBoolean(NotificationsActivity.SUNGLASSES_ALERT_STATUS, true);
        sunburn_alert_status = togglePreferences.getBoolean(NotificationsActivity.SUNBURN_ALERT_STATUS, true);
    }

    // this function is used to alert user if they are exposed to a high level of UVI!
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void reduceRiskOFBurnNotification(Float data){


        if (data >= 5 && data < 8 && uvi_level_alert_status) {
            sendToChannel(R.drawable.ic_sunlight_level3,
                    "UVI LEVEL ALERT!",
                    "You Are Exposed To: " + UVSensorData.getUVIntensity() + "\n Long Exposure Term May Affect Health",
                    NotificationChannelsClass.CHANNEL_1_ID, 1);

        } if (data >= 8 && data < 20 && uvi_level_alert_status) {
            sendToChannel(R.drawable.ic_sunlight_level5,
                    "HIGH UVI LEVEL ALERT!!!",
                    "You are exposed to a DANGEROUS level of UV Radiation:" + UVSensorData.getUVIntensity() + "\nStay out of sunlight!",
                    NotificationChannelsClass.CHANNEL_1_ID, 1);
        }
    }

    public void sunburnNotification(){

        if (uvMax >= 3)
        switch(id_skin) {

            case SKIN_TYPE_PALE:
                minutesToBurn = (int) (76.533 * (Math.pow(uvMax, -1.073)));
                break;
            case SKIN_TYPE_FAIR:
                minutesToBurn = (int) (94.097 * (Math.pow(uvMax, -1.676)));
                break;
            case SKIN_TYPE_MEDIUM:
                minutesToBurn = (int) (198.63 * (Math.pow(uvMax, -0.9907)));
                break;
            case SKIN_TYPE_OLIVE:
                minutesToBurn = (int) (302.19 * (Math.pow(uvMax, -1.0059)));
                break;
            case SKIN_TYPE_BROWN:
                minutesToBurn = (int) (396.33 * (Math.pow(uvMax, -0.9955)));
                break;
            case SKIN_TYPE_BLACK:
                minutesToBurn = (int) (502.12 * (Math.pow(uvMax, -1.0023)));
                break;
        }

        if (sunburn_alert_status) {
            sendToChannel(R.drawable.ic_sunlight_level5,
                    "SUNBURN ALERT!!!",
                    "You can stay outside for a maximum of " + minutesToBurn + " minutes!",
                    NotificationChannelsClass.CHANNEL_3_ID, 3);
        }
    }




    public void sendToChannel(int drawableID, String contentTitle, String contentText, final String notificationChannel , int id) {

        Notification notification=new NotificationCompat.Builder(this, notificationChannel).setSmallIcon(drawableID)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(0xFFB1D4E0)
                .build();
        notificationManagerCompat.notify( id, notification);
    }


    // find the max UV and saves in database.
    protected void maxUV() {
        if (uvMax < UVSensorData.getUVIntensity()) {
            uvMax = UVSensorData.getUVIntensity();
        }
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
