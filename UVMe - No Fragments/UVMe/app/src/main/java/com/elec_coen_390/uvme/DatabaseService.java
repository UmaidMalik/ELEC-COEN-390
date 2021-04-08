package com.elec_coen_390.uvme;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseService extends Service {
    private static final String LOG_TAG = "DatabaseService";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Your logical code here

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
        Log.i(LOG_TAG, "In onDestroy");
    }




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
