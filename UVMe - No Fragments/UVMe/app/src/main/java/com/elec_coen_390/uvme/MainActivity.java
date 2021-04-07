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
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
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
    private float uvIndex = 0;
    float maxUV = 0;

    float maxUVDatabase = 0;
    Calendar calendar;
    int minute = 0;

    private float batteryLevel = 0;
    private TextView textViewBatteryLevel;

    private BluetoothLeService mBluetoothLeService;
    private ArrayList<BluetoothGattService> mBluetoothGattServices = new ArrayList<BluetoothGattService>() ;




    private NotificationManagerCompat notificationManagerCompat;

    private String city;
    EditText editTextCitySearch;
    ToggleButton buttonCitySearch;
    ImageView imageViewWeather;
    TextView textViewTemperature, textViewCity, textViewCountry, textViewWind, textViewFeelsLike, textViewHumidity, textViewPressure;

    private ImageView imageViewSensor;
    private ImageView imageViewSensorBattery;

    private TextView uvIndexStatusMessage;
    private TextView textViewSensorState;

    SharedPreferences togglePreferences;
    boolean uvi_level_alert_status = false, burn_risk_alert_status = false;

    SharedPreferences prefs;

    SharedPreferences toggleUVModePreferences;
    boolean uv_mode_status = false;
    int refresh_cycle_time = 60;
    boolean isRefreshSetToNever = false;
    ToggleButton toggleButtonRefresh;
    TextView textViewRefresh;

    ToggleButton buttonMoreInfo;


    final Handler handler = new Handler();
    final Handler handler2 = new Handler();


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); // this is to prevent the keyboard from opening on startup since there is a EditText in this activity;)
        notificationManagerCompat = NotificationManagerCompat.from(this);

        editTextCitySearch = (EditText) findViewById(R.id.editTextCitySearch);
        buttonCitySearch = (ToggleButton) findViewById(R.id.buttonCitySearch);
        imageViewWeather = (ImageView) findViewById(R.id.imageViewWeather);
        textViewTemperature = (TextView) findViewById(R.id.textViewTemperature);
        textViewCity = (TextView) findViewById(R.id.textViewCity);
        textViewCountry = (TextView) findViewById(R.id.textViewCountry);
        textViewFeelsLike = (TextView) findViewById(R.id.textViewFeelsLike);

        textViewSensorState = (TextView) findViewById(R.id.textViewSensorState);
        imageViewSensorBattery = (ImageView) findViewById(R.id.imageViewSensorBattery);
        textViewWind = (TextView) findViewById(R.id.textViewWind);
        textViewHumidity = (TextView) findViewById(R.id.textViewHumidity);
        textViewPressure = (TextView) findViewById(R.id.textViewPressure);
        toggleButtonRefresh = (ToggleButton) findViewById(R.id.toggleButtonRefresh);
        textViewRefresh = (TextView) findViewById(R.id.textViewRefresh);
        buttonMoreInfo = (ToggleButton) findViewById(R.id.buttonMoreInfo);

        setupCitySearchButton();



        builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);

        uvIndexStatusMessage = (TextView) findViewById(R.id.uvIndexStatusMessage);
        uvIndexStatusMessage.setText("");

        db = new DatabaseHelper(this);
        db.getWritableDatabase();

        prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        String location = prefs.getString("CITY", "");
        editTextCitySearch.setText(location);
        editTextCitySearch.setVisibility(View.INVISIBLE);
        FindWeather(); // initial call on startup



        if (firstStart) {
            Log.d(TAG, "Enter a Statement");
            builder.setTitle(R.string.terms_of_services);
            builder.setMessage(R.string.warning_label)
                    .setCancelable(false)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Toast.makeText(getApplicationContext(), R.string.ok_terms_of_service,
                                    Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean("firstStart", false); // Set to false so that it will only appear once when accepted
                            editor.apply();
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

        imageViewSensor = (ImageView) findViewById(R.id.imageViewSensor);


        startSunUIThread(getCurrentFocus());
        startUVIndexThread(getCurrentFocus());
        startNotificationsThread(getCurrentFocus());
        startResetMaxUVThread(getCurrentFocus());
        startDatabaseThread(getCurrentFocus());

        setupRefreshButton();
        setupMoreButton();

    }

    private void setupCitySearchButton() {
        editTextCitySearch.setEnabled(false);

        buttonCitySearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FindWeather();
            }
        });

        buttonCitySearch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editTextCitySearch.setEnabled(isChecked);
                if(isChecked) {
                    editTextCitySearch.setVisibility(View.VISIBLE);
                }
                else {
                    editTextCitySearch.setVisibility(View.INVISIBLE);

                    city = editTextCitySearch.getText().toString();
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("CITY", city);
                    editor.apply();
                }
            }
        });
    }

    // function that reads values from database and saves it.
    // taking in UV sensor data and time.
    private void startDatabaseThread(View view) {
        new Thread(runnableDatabase).start();
    }

    Runnable runnableDatabase = new Runnable() {



        public void run() {

            calendar = Calendar.getInstance();
            minute = calendar.get(Calendar.SECOND);

            if (UVSensorData.getUVIntensity() > 0.5  /** && min % 5 == 0) **/ ) {

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


    private void setupRefreshButton() {
        toggleButtonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maxUV = 0;
            }
        });
    }

    private void setupMoreButton() {
        buttonMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }

    // function that updates the SunColor depending on the level of UV
    private void updateSunColor() {
        uv_mode_status = toggleUVModePreferences.getBoolean(UVDisplayModeActivity.UV_MODE_STATUS, false);
        if (uv_mode_status)
            uvIndex = maxUV;
        else
            uvIndex = UVSensorData.getUVIntensity();

        if (uvIndex < 1) {
            ic_sun.setImageResource(R.drawable.ic_sunlight_default_level1_lightblue);
            buttonMoreInfo.setVisibility(View.INVISIBLE);
            uvIndexStatusMessage.setText("");
        }
        else if (uvIndex >= 1 && uvIndex < 3) {
            ic_sun.setImageResource(R.drawable.ic_sunlight_default_level1_lightblue);
            buttonMoreInfo.setVisibility(View.VISIBLE);
            uvIndexStatusMessage.setText(R.string.you_are_safe);
        }
        else if (uvIndex >= 3 && uvIndex < 6) {
            ic_sun.setImageResource(R.drawable.ic_sunlight_level2);
            buttonMoreInfo.setVisibility(View.VISIBLE);
            uvIndexStatusMessage.setText(R.string.take_precaution);
        }
        else if (uvIndex >= 6 && uvIndex < 8) {
            ic_sun.setImageResource(R.drawable.ic_sunlight_level3);
            buttonMoreInfo.setVisibility(View.VISIBLE);
            uvIndexStatusMessage.setText(R.string.protection_required);
        }
        else if (uvIndex >= 8 && uvIndex < 11) {
            ic_sun.setImageResource(R.drawable.ic_sunlight_level4);
            buttonMoreInfo.setVisibility(View.VISIBLE);
            uvIndexStatusMessage.setText(R.string.extra_protection_required);
        }
        else if (uvIndex > 11){
            ic_sun.setImageResource(R.drawable.ic_sunlight_level5);
            buttonMoreInfo.setVisibility(View.VISIBLE);
            uvIndexStatusMessage.setText(R.string.take_full_precaution);
        }
    }

    private void updateBatteryLevelIcon() {
        batteryLevel = BatteryData.getBatteryLevel();
        if (batteryLevel > 80f)
            imageViewSensorBattery.setImageResource(R.drawable.ic_battery_level_5);
        else if (batteryLevel <= 80 && batteryLevel > 55)
            imageViewSensorBattery.setImageResource(R.drawable.ic_battery_level_4);
        else if (batteryLevel <= 55 && batteryLevel >= 40)
            imageViewSensorBattery.setImageResource(R.drawable.ic_battery_level_3);
        else if (batteryLevel < 40 && batteryLevel >= 15)
            imageViewSensorBattery.setImageResource(R.drawable.ic_battery_level_2);
        else if (batteryLevel < 15)
            imageViewSensorBattery.setImageResource(R.drawable.ic_battery_level_1);
    }

    private void displayUVSensorData(float uvIndex) {
        textViewUVIndex.setText(String.valueOf(uvIndex));
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

    private void startResetMaxUVThread(View view) {
        RunnableResetMaxUV runnableResetMaxUV = new RunnableResetMaxUV();
        new Thread(runnableResetMaxUV).start();
    }

    private void setRefreshButtonVisibility(boolean condition) {
        if (condition) {
            toggleButtonRefresh.setVisibility(View.VISIBLE);
            textViewRefresh.setVisibility(View.VISIBLE);
        }
        else {
            toggleButtonRefresh.setVisibility(View.INVISIBLE);
            textViewRefresh.setVisibility(View.INVISIBLE);
        }
    }



    private class RunnableNotification implements Runnable {
        @Override
        public void run() {
            while (true) {

                runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void run() {
                        reduceRiskOFBurnNotification(UVSensorData.getUVIntensity());

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
                        updateBatteryLevelIcon();
                    }
                });

                try {
                    Thread.sleep(2000);
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
                        uv_mode_status = toggleUVModePreferences.getBoolean(UVDisplayModeActivity.UV_MODE_STATUS, false);
                        if(uv_mode_status) {
                            displayUVMaxMode();
                        }
                        else displayUVSensorData(UVSensorData.getUVIntensity());

                        setRefreshButtonVisibility(uv_mode_status);
                        textViewBatteryLevel.setText(String.valueOf((int)BatteryData.getBatteryLevel()) + "%");
                        databaseUVMax();
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

    private class RunnableResetMaxUV implements Runnable {



        @Override
        public void run() {
            while (true) {



                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refresh_cycle_time = toggleUVModePreferences.getInt(UVDisplayModeActivity.UV_MODE_REFRESH_SETTING, 60);
                        isRefreshSetToNever = toggleUVModePreferences.getBoolean(UVDisplayModeActivity.UV_MODE_REFRESH_NEVER, false);
                        if (!isRefreshSetToNever) {
                            maxUV = 0;
                        }

                    }
                });


                try {
                    Thread.sleep(refresh_cycle_time * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
           }
        }
    }


    protected void displayUVMaxMode() {
        if (maxUV < UVSensorData.getUVIntensity()) {
            maxUV = UVSensorData.getUVIntensity();
        }
        textViewUVIndex.setText(String.valueOf(maxUV));

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        //***********************************
        togglePreferences = getSharedPreferences(NotificationsActivity.PREFS, MODE_PRIVATE);
        uvi_level_alert_status = togglePreferences.getBoolean(NotificationsActivity.UVI_LEVEL_ALERT_STATUS, true);
        burn_risk_alert_status = togglePreferences.getBoolean(NotificationsActivity.BURN_RISK_ALERT_STATUS, true);

        toggleUVModePreferences = getSharedPreferences(UVDisplayModeActivity.UV_MODE_PREFS, MODE_PRIVATE);
        uv_mode_status = toggleUVModePreferences.getBoolean(UVDisplayModeActivity.UV_MODE_STATUS, false);
        refresh_cycle_time = toggleUVModePreferences.getInt(UVDisplayModeActivity.UV_MODE_REFRESH_SETTING, 60);
        isRefreshSetToNever = toggleUVModePreferences.getBoolean(UVDisplayModeActivity.UV_MODE_REFRESH_NEVER, false);

    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                updateSensorConnectionState(true);
            }
            else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                updateSensorConnectionState(false);
            }
            else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                //displayUVSensorData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA_VALUE_UV_INDEX));
                updateSensorConnectionState(true);
                //displayBatteryLevel(intent.getStringExtra(BluetoothLeService.EXTRA_DATA_VALUE_BATTERY_LEVEL) + "%");

            }
        }
    };


    private void updateSensorConnectionState(boolean condition) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (condition) {
                    imageViewSensor.setImageResource(R.drawable.ic_sensor_on);
                    textViewSensorState.setText("Sensor ON");
                }
                else {
                    imageViewSensor.setImageResource(R.drawable.ic_sensor_off);
                    textViewSensorState.setText("Sensor OFF");
                    BluetoothLeService.mBluetoothGatt.close();
                    BluetoothLeService.mBluetoothGatt = null;
                }
            }
        });
    }

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
        intentProfile.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intentProfile);
    }

    protected void goToMoreActivity() {
        Intent intentMore = new Intent(this, MoreActivity.class);
        intentMore.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intentMore);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }


    // this function is used to alert user if they are exposed to a high level of UVI!
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void reduceRiskOFBurnNotification(Float data){

        uvi_level_alert_status = togglePreferences.getBoolean(NotificationsActivity.UVI_LEVEL_ALERT_STATUS, true);
        burn_risk_alert_status = togglePreferences.getBoolean(NotificationsActivity.BURN_RISK_ALERT_STATUS, true);
        if (data >= 5 && data < 8 && uvi_level_alert_status) {
            sendToChannel(R.drawable.ic_sunlight_level3,
                    "SUNBURN ALERT!",
                    "You Are Exposed To: " + UVSensorData.getUVIntensity() + "\n Long Exposure Term May Affect Health",
                    NotificationChannelsClass.CHANNEL_1_ID, 1);

        } if (data >= 8 && data < 20 && burn_risk_alert_status) {
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

    public void FindWeather()
    {
        city = prefs.getString("CITY", "Enter City Name");
        //city = editTextCitySearch.getText().toString();
        String url ="http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=462f445106adc1d21494341838c10019&units=metric";
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //find temperature
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject object = jsonObject.getJSONObject("main");
                            double temp = object.getDouble("temp");
                            textViewTemperature.setText(temp+"°C");
                            textViewTemperature.setTextSize(48);

                            //find country
                            JSONObject object8 = jsonObject.getJSONObject("sys");
                            String count = object8.getString("country");
                            textViewCountry.setText(count);

                            //find city
                            String city = jsonObject.getString("name");
                            textViewCity.setText(city+",");

                            //find icon
                            JSONArray jsonArray = jsonObject.getJSONArray("weather");
                            JSONObject obj = jsonArray.getJSONObject(0);
                            String icon = obj.getString("icon");
                            Picasso.get().load("http://openweathermap.org/img/wn/"+icon+"@2x.png").into(imageViewWeather);

                            //find wind speed
                            JSONObject object9 = jsonObject.getJSONObject("wind");
                            String wind_find = object9.getString("speed");
                            textViewWind.setText(wind_find+" km/h");

                            //find feels
                            JSONObject object13 = jsonObject.getJSONObject("main");
                            double feels_find = object13.getDouble("feels_like");
                            textViewFeelsLike.setText("feels like " + feels_find+" °C");

                            //find pressure
                            JSONObject object7 = jsonObject.getJSONObject("main");
                            String pressure_find = object7.getString("pressure");
                            textViewPressure.setText(pressure_find+"  hPa");

                            //find humidity
                            JSONObject object4 = jsonObject.getJSONObject("main");
                            int humidity_find = object4.getInt("humidity");
                            textViewHumidity.setText(humidity_find+"  %");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);

    }
}