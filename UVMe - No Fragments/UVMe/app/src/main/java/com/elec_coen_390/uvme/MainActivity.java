package com.elec_coen_390.uvme;

import android.app.Notification;
import android.app.NotificationManager;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.util.ArrayList;


import java.util.Calendar;
public class MainActivity extends AppCompatActivity {


    DatabaseHelper db;
    AlertDialog.Builder builder;
    public static final String TAG = "Main Activity";

    private TextView textViewUVIndex;
    private TextView textViewUVI;
    private ImageView ic_sun;
    private float uvIndex = 0.00f;

    private int batteryLevel = 17;
    private TextView textViewBatteryLevel;

    private BluetoothLeService mBluetoothLeService;
    private ArrayList<BluetoothGattService> mBluetoothGattServices = new ArrayList<BluetoothGattService>() ;



    private NotificationManagerCompat notificationManagerCompat;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        //*************
/*
        db = new DatabaseHelper(this);
        db.insertUV(uvIndex, Calendar.getInstance());

 */
        //*************

        notificationManagerCompat = NotificationManagerCompat.from(this);




        builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);

        TextView title = (TextView) findViewById(R.id.activityMain);
        title.setText("Message");


        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);


        if (firstStart) {
            Log.d(TAG, "Enter a Statement");
            builder.setTitle(R.string.terms_of_services);
            builder.setMessage(R.string.warning_label)
                    .setCancelable(true)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Toast.makeText(getApplicationContext(), R.string.ok_terms_of_service,
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            finish();
                            Toast.makeText(getApplicationContext(), R.string.cancel_terms_of_service,
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle("Terms of Services");
            alert.show();


            //alert.getWindow().setBackgroundDrawable(new ColorDrawable(0xFF0B1320)); // midnight_blue?
            alert.getWindow().setBackgroundDrawable(new ColorDrawable(0xFF1C3F60)); // or secondary_blue?

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstStart", false); // Set to false so that it will only appear once when accepted
            editor.apply();


        }
        this.getSupportActionBar().hide();
        setupBottomNavigationListener();
        textViewUVIndex = (TextView) findViewById(R.id.textViewUVIndex);
        ic_sun = (ImageView) findViewById(R.id.ic_sun);

        textViewBatteryLevel = (TextView) findViewById(R.id.textViewBatteryLevel);

        textViewUVIndex.setText(String.valueOf(UVSensorData.getUVIntensity()));

        textViewBatteryLevel.setText(String.valueOf(BatteryData.getBatteryLevel()));

       textViewUVI = (TextView) findViewById(R.id.textViewUVI);

        //textViewUVI.setText(String.valueOf(batteryLevel));

        startSunUIThread(getCurrentFocus());
        startUVIndexThread(getCurrentFocus());
        startNotificationsThread(getCurrentFocus());

    }



    private void updateSunColor() {
        uvIndex = UVSensorData.getUVIntensity();
        if (uvIndex < 3)
            ic_sun.setImageResource(R.drawable.ic_sunlight_default_level1_lightblue);
        else if (uvIndex >= 3 && uvIndex < 6)
            ic_sun.setImageResource(R.drawable.ic_sunlight_level2);
        else if (uvIndex >= 6 && uvIndex < 8)
            ic_sun.setImageResource(R.drawable.ic_sunlight_level3);
        else if (uvIndex >= 8 && uvIndex < 11)
            ic_sun.setImageResource(R.drawable.ic_sunlight_level4);
        else
            ic_sun.setImageResource(R.drawable.ic_sunlight_level5);
    }

    private void displayUVSensorData(String uvIndex) {
        textViewUVIndex.setText(uvIndex);
    }


    private void startSunUIThread(View view) {
        RunnableSunColor runnableSunColor = new RunnableSunColor();
        new Thread(runnableSunColor).start();
    }

    private void startUVIndexThread(View view) {
        RunnableUVIndex runnableUVIndex = new RunnableUVIndex();
        new Thread(runnableUVIndex).start();
    }

    private void startNotificationsThread(View v) {
        RunnableNotification runnableNotification = new RunnableNotification();
        new Thread(runnableNotification).start();
    }

    private class RunnableNotification implements Runnable {
        @Override
        public void run() {
            while (true) {

                runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void run() {
                        notificationFunction(UVSensorData.getUVIntensity());

                    }
                });

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class RunnableSunColor implements Runnable {





        @Override
        public void run() {
            while (true) {
                Log.d(TAG, "startThread: " + uvIndex);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateSunColor();
                        //*************
                       // db.insertUV(uvIndex, Calendar.getInstance());
                        //*************
                    }
                });

                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class RunnableUVIndex implements Runnable {


        @Override
        public void run() {
            while (true) {



                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textViewUVIndex.setText(String.valueOf(UVSensorData.getUVIntensity()));
                        //displayBatteryLevel(String.valueOf(BatteryData.getBatteryLevel()));
                        //stringBatteryLevel = textViewBatteryLevel.getText();
                        //batteryLevel = Integer.parseInt((String) stringBatteryLevel);
                        //textViewUVI.setText(String.valueOf(batteryLevel));
                        textViewUVI.setText(String.valueOf(BatteryData.getBatteryLevel()));

                    }
                });

                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        //***********************************
        notificationFunction(uvIndex);

    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {

            }
            else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {

            }
            else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                //displayUVSensorData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA_VALUE_UV_INDEX));
                displayBatteryLevel(intent.getStringExtra(BluetoothLeService.EXTRA_DATA_VALUE_BATTERY_LEVEL));
            }
        }
    };

    private void displayBatteryLevel(String stringExtra) {
        textViewBatteryLevel.setText(stringExtra);
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    private void setupBottomNavigationListener() {

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1); // bottom navigation menu index item {0(Profile),1(Home),2(More)}
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_profile:
                        goToProfileActivity();
                        break;

                    case R.id.action_more:
                        goToMoreActivity();
                        break;

                    default:

                }
                return false;
            }
        });
    }

    protected void goToProfileActivity() {
        Intent intentProfile = new Intent(this, ProfileActivity.class);
        startActivity(intentProfile);
        finish();
    }

    protected void goToMoreActivity() {
        Intent intentMore = new Intent(this, MoreActivity.class);
        startActivity(intentMore);
        finish();
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void notificationFunction(Float data){


        if(data >= 6 && data < 9) {
            sendChannel1();
        }
        else if (data >= 9){
            sendChannel2();
        }
    }

    public void sendChannel1(){
Notification notification=new NotificationCompat.Builder(this,NotificationChannelsClass.CHANNEL_1_ID).setSmallIcon(R.drawable.ic_smile)
        .setContentTitle("Channel 1 Test")
        .setContentText("Please work")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
        .build();
notificationManagerCompat.notify(1,notification);
    }

    public void sendChannel2(){
        Notification notification=new NotificationCompat.Builder(this,NotificationChannelsClass.CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_sad)
                .setContentTitle("Channel 2 Test")
                .setContentText("Please work")
                .setPriority(NotificationCompat.PRIORITY_LOW)

                .build();
        notificationManagerCompat.notify(2,notification);
    }
}