package com.elec_coen_390.uvme.Services.Database;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.elec_coen_390.uvme.UVSensorData;

import java.util.Calendar;

import androidx.annotation.Nullable;

public class DatabaseService extends Service {

    private static final String LOG_TAG = "DatabaseService";

    float maxUVDatabase = 0;
    float maxUVDatabaseMINUTE = 0;
    Calendar calendar;
    int second = 0;
    int minute = 0;
    float sum = 0;
    float avg = 0;
    int count = 0;

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
    private void startDatabaseThread() {
        new Thread(runnableDatabase).start();
    }

    Runnable runnableDatabase = new Runnable() {



        public void run() {

            calendar = Calendar.getInstance();
            second = calendar.get(Calendar.SECOND);
            minute = calendar.get(Calendar.MINUTE);

            if (maxUVDatabase > 0.5) {

                db.insertUV(UVSensorData.getUVIntensity(), calendar);
                // every 5 seconds uv is stored
                if (second % 5 == 0) {

                    db.insertUVMax(maxUVDatabase, calendar);

                    sum += maxUVDatabase;
                    maxUVDatabase = 0;
                    count++;
                } // every minute the uv is stored
            }
                if (second % 60 == 0 && maxUVDatabaseMINUTE > 0.5) {
                    avg = sum/count;
                    db.insertUVGraph(maxUVDatabaseMINUTE, avg, calendar);
                    sum = 0;
                    count = 0;
                    maxUVDatabaseMINUTE = 0;
                }

             // this run method repeats every second
            handler.postDelayed(this, 1000);
        }
    };

    // find the max UV and saves in database.
    protected void databaseUVMax() {
        if (maxUVDatabase < UVSensorData.getUVIntensity()) {
            maxUVDatabase = UVSensorData.getUVIntensity();
        }
        if (maxUVDatabaseMINUTE < UVSensorData.getUVIntensity()) {
            maxUVDatabaseMINUTE = UVSensorData.getUVIntensity();
        }
    }

    private void startUVIndexThread() {
        new Thread(runnableUVMax).start();
    }

    Runnable runnableUVMax = new Runnable() {


        public void run() {

            databaseUVMax();

            handler.postDelayed(this, 5);
        }
    };

}