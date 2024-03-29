package com.elec_coen_390.uvme.Services.Notifications;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.elec_coen_390.uvme.Activities.NotificationsActivity;
import com.elec_coen_390.uvme.Activities.ProfileActivity;
import com.elec_coen_390.uvme.R;
import com.elec_coen_390.uvme.UVSensorData;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

public class NotificationsService extends Service {

    /**
     * Notifications service will run in the background
     *
     * Notifications are displayed depending on the uv index level
     *
     *
     */

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

    SharedPreferences prefseye;
    private int id_eye;

    private final int EYE_TYPE_BLUE = 0;
    private final int EYE_TYPE_BROWN = 1;
    private final int EYE_TYPE_GREEN = 2;
    private final int EYE_TYPE__HAZEL = 3;

    private int minutesToBurn = 0;
    private float uvMax = 0;



    private Calendar calendar;
    private Calendar calendarNext;
    private Calendar calendarCheck;

    @Override
    public void onCreate() {
        super.onCreate();
        calendar = Calendar.getInstance();
        calendarNext = Calendar.getInstance();
        calendarNext.add(Calendar.MINUTE, 1);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        notificationManagerCompat = NotificationManagerCompat.from(this);

        togglePreferences = getSharedPreferences(NotificationsActivity.PREFS, MODE_PRIVATE);
        startNotificationsThread();
        startMaxUVThread();
        startResetMaxUVThread();



        prefsSkin = getSharedPreferences(ProfileActivity.prefNameSkin, MODE_PRIVATE);
        id_skin = prefsSkin.getInt("last_val_skin", 0);

        prefseye = getSharedPreferences(ProfileActivity.prefNameEye, MODE_PRIVATE);
        id_eye = prefseye.getInt("last_val_eye", 0);


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

    private void startResetMaxUVThread() {
        new Thread(runnableResetMaxUV).start();
    }

    Runnable runnableNotification = new Runnable() {


        @RequiresApi(api = Build.VERSION_CODES.O)
        public void run() {

            setupNotificationPreferences();
            sunglassesReminder();
            reduceRiskOFBurnNotification(UVSensorData.getUVIntensity());
            sunburnNotification();

            handler.postDelayed(this, 50);
        }
    };

    Runnable runnableResetMaxUV = new Runnable() {

        public void run() {


            calendarCheck = Calendar.getInstance();
           if (calendarCheck.get(Calendar.HOUR_OF_DAY) == calendarNext.get(Calendar.HOUR_OF_DAY)
                    && calendarCheck.get(Calendar.MINUTE)  == calendarNext.get(Calendar.MINUTE) && sunburn_alert_status) {
                sendBurnTimeoutNotification();
                uvMax = 0;
            }


            handler.postDelayed(this,  2000);
        }
    };

    private void startMaxUVThread() {
        new Thread(runnableUpdateMaxUV).start();
    }

    Runnable runnableUpdateMaxUV = new Runnable() {

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void run() {

            maxUV();

            handler.postDelayed(this, 200);
        }
    };


    public void setupNotificationPreferences() {

        prefsSkin = getSharedPreferences(ProfileActivity.prefNameSkin, MODE_PRIVATE);
        id_skin = prefsSkin.getInt("last_val_skin", 0);
        prefseye = getSharedPreferences(ProfileActivity.prefNameEye, MODE_PRIVATE);

        id_eye = prefseye.getInt("last_val_eye", 0);
        togglePreferences = getSharedPreferences(NotificationsActivity.PREFS, MODE_PRIVATE);

        uvi_level_alert_status = togglePreferences.getBoolean(NotificationsActivity.UVI_LEVEL_ALERT_STATUS, true);
        sunglasses_alert_status = togglePreferences.getBoolean(NotificationsActivity.SUNGLASSES_ALERT_STATUS, true);
        sunburn_alert_status = togglePreferences.getBoolean(NotificationsActivity.SUNBURN_ALERT_STATUS, true);
    }

    // this function is used to alert user if they are exposed to a high level of UVI!
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void reduceRiskOFBurnNotification(Float data){


        if (data >= 5 && data < 8 && uvi_level_alert_status) {
            sendToChannel(R.drawable.ic_extreme_condition,
                    "UVI LEVEL ALERT!",
                    "You Are Exposed To: " + UVSensorData.getUVIntensity() + "\n Long Exposure Term May Affect Health",
                    NotificationCompat.PRIORITY_LOW,
                    NotificationCompat.CATEGORY_MESSAGE,
                    NotificationChannelsClass.CHANNEL_1_ID, 1);

        } if (data >= 8 && data < 20 && uvi_level_alert_status) {
            sendToChannel(R.drawable.ic_extreme_condition,
                    "HIGH UVI LEVEL ALERT!!!",
                    "You are exposed to a DANGEROUS level of UV Radiation:" + UVSensorData.getUVIntensity() + "\nStay out of sunlight!",
                    NotificationCompat.PRIORITY_LOW,
                    NotificationCompat.CATEGORY_MESSAGE,
                    NotificationChannelsClass.CHANNEL_1_ID, 1);
        }
    }

    public void sendBurnTimeoutNotification() {
        sendToChannel(R.drawable.ic_extreme_condition,
                "WARNING: Sun Exposure Limit Reached",
                "If you have been outside since " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE)
                        + ", then you have reached your UV exposure limit.",
                NotificationCompat.PRIORITY_HIGH,
                NotificationCompat.CATEGORY_ALARM,
                NotificationChannelsClass.CHANNEL_4_ID, 4
        );


    }

    public void sunglassesReminder(){
        //switch case, output depending on the eye color.
        if(sunglasses_alert_status && UVSensorData.getUVIntensity() >= 2) {
            switch (id_eye) {
                case EYE_TYPE_BLUE:
                    sendToChannel(R.drawable.ic_sunglasses,
                            "SUNGLASSES ALERT!!!\n Hey Blue Eyes",
                            "UV is +2, get your shades on!",
                            NotificationCompat.PRIORITY_LOW,
                            NotificationCompat.CATEGORY_MESSAGE,
                            NotificationChannelsClass.CHANNEL_2_ID, 2);
                    break;
                case EYE_TYPE_BROWN:
                    sendToChannel(R.drawable.ic_sunglasses,
                            "SUNGLASSES ALERT!!!\n Hey Green Eyes",
                            "UV is +2, get your shades on!",
                            NotificationCompat.PRIORITY_LOW,
                            NotificationCompat.CATEGORY_MESSAGE,
                            NotificationChannelsClass.CHANNEL_2_ID, 2);
                    break;
                case EYE_TYPE_GREEN:
                    sendToChannel(R.drawable.ic_sunglasses,
                            "SUNGLASSES ALERT!!!\n Hey Brown Eyes",
                            "UV is +2, get your shades on!",
                            NotificationCompat.PRIORITY_LOW,
                            NotificationCompat.CATEGORY_MESSAGE,
                            NotificationChannelsClass.CHANNEL_2_ID, 2);

                    break;
                case EYE_TYPE__HAZEL:
                    sendToChannel(R.drawable.ic_sunglasses,
                            "SUNGLASSES ALERT!!!\n Hey Hazel Eyes",
                            "UV is +2, get your shades on!",
                            NotificationCompat.PRIORITY_LOW,
                            NotificationCompat.CATEGORY_MESSAGE,
                            NotificationChannelsClass.CHANNEL_2_ID, 2);
                    break;

            }

        }
    }

    public void sunburnNotification(){

        if (uvMax >= 2 && sunburn_alert_status && UVSensorData.getUVIntensity() > uvMax) {
            switch (id_skin) {

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




                calendar = Calendar.getInstance();
                calendarNext = Calendar.getInstance();
                calendarNext.add(Calendar.MINUTE, minutesToBurn);
                sendToChannel(R.drawable.ic_sunlight_level5,
                        "SUNBURN ALERT!! UV: " + uvMax,
                        "Exposure time limit: " + minutesToBurn + " minutes" +
                        " Time: " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) +
                        " Till: " + calendarNext.get(Calendar.HOUR_OF_DAY) + ":" + calendarNext.get(Calendar.MINUTE),
                        NotificationCompat.PRIORITY_HIGH,
                        NotificationCompat.CATEGORY_MESSAGE,
                        NotificationChannelsClass.CHANNEL_3_ID, 3);

        }

    }

    public void sendToChannel(int drawableID, String contentTitle, String contentText, final int priority, final String message, final String notificationChannel , int id) {

        Notification notification=new NotificationCompat.Builder(this, notificationChannel).setSmallIcon(drawableID)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setPriority(priority) // NotificationCompat.PRIORITY_HIGH
                .setCategory(message) // NotificationCompat.CATEGORY_MESSAGE
                .build();
        notificationManagerCompat.notify( id, notification);
    }


    // find the max UV and saves in database.
    protected void maxUV() {
        if (uvMax < UVSensorData.getUVIntensity()) {
            uvMax = UVSensorData.getUVIntensity();
        }
    }





}