package com.elec_coen_390.uvme;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import java.util.Calendar;

import androidx.annotation.Nullable;

public class DatabaseService extends Service {

    private static final String LOG_TAG = "DatabaseService";

    float maxUVDatabase = 0;
    Calendar calendar;
    int minute = 0;

    DatabaseHelper db;

    final Handler handler = new Handler();


    @Override
    public void onCreate() {
        super.onCreate();



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Your logical code here
        db = new DatabaseHelper(this);
        db.getWritableDatabase();
        startUVIndexThread();
        startDatabaseThread();

        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        //When remove app from background then start it again
        startService(new Intent(this, DatabaseService.class));

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

    // function that reads values from database and saves it.
    // taking in UV sensor data and time.
    private void startDatabaseThread(/*View view*/) {
        new Thread(runnableDatabase).start();
    }

    Runnable runnableDatabase = new Runnable() {



        public void run() {

            calendar = Calendar.getInstance();
            minute = calendar.get(Calendar.SECOND);

            if (UVSensorData.getUVIntensity() > 0.5 ) {

                db.insertUV(UVSensorData.getUVIntensity(), calendar);
                if (minute % 5 == 0) {
                    db.insertUVMax(maxUVDatabase, calendar);
                    maxUVDatabase = 0;
                }

            }
            handler.postDelayed(this, 1000);
        }
    };

    // find the max UV and saves in database.
    protected void databaseUVMax() {
        if (maxUVDatabase < UVSensorData.getUVIntensity()) {
            maxUVDatabase = UVSensorData.getUVIntensity();
        }
    }

    private void startUVIndexThread(/*View view */) {
        new Thread(runnableUVMax).start();
    }

    Runnable runnableUVMax = new Runnable() {


        public void run() {

            databaseUVMax();

            handler.postDelayed(this, 5);
        }
    };

}
