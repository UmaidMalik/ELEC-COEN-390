package com.elec_coen_390.uvme.Activities;

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
import android.view.WindowManager;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.elec_coen_390.uvme.BatteryData;
import com.elec_coen_390.uvme.Services.BluetoothLE.BluetoothLeService;
import com.elec_coen_390.uvme.Services.Database.DatabaseService;
import com.elec_coen_390.uvme.Services.Notifications.NotificationsService;
import com.elec_coen_390.uvme.R;
import com.elec_coen_390.uvme.UVSensorData;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This activity shows the weather (location, temperature, feels like temperature, humidity, pressure, wind speed, and current weather icon)
 * Contains an EditText for the user to enter their city
 * Shows the ON/OFF state of the sensor
 * Shows the current battery level of the sensor
 * Displays the UV index
 * Contains the reset/refresh icon button. Note: only visible when "Display max UV intensity" is set to ON in the UVDisplayMode Activity
 * Reset/refresh button set the UV max intensity read to be set back to 0.0
 * The sun icon at center changes color (lightblue; yellow; red-yellow; red; violet) based of the UV index level
 */

public class MainActivity extends AppCompatActivity {


    AlertDialog.Builder builder;
    public static final String TAG = "Main Activity";

    private TextView textViewUVIndex;
    private ImageView ic_sun;
    private float uvIndex = 0;
    float maxUV = 0;

    private float batteryLevel = 0;
    private TextView textViewBatteryLevel;

    private BluetoothLeService mBluetoothLeService;
    private ArrayList<BluetoothGattService> mBluetoothGattServices = new ArrayList<BluetoothGattService>() ;

    private String city;
    EditText editTextCitySearch;
    ToggleButton buttonCitySearch;
    ImageView imageViewWeather;
    TextView textViewTemperature, textViewCity, textViewCountry, textViewWind, textViewFeelsLike, textViewHumidity, textViewPressure;

    private ImageView imageViewSensor;
    private ImageView imageViewSensorBattery;

    private TextView uvIndexStatusMessage;
    private TextView textViewSensorState;

    SharedPreferences prefs;

    SharedPreferences toggleUVModePreferences;
    boolean uv_mode_status = false;
    int refresh_cycle_time = 60;
    boolean isRefreshSetToNever = false;
    ToggleButton toggleButtonRefresh;
    TextView textViewRefresh;

    ToggleButton buttonMoreInfo;

    boolean isBatteryConnected = false;

    private int backButtonCount = 0;

    private boolean firstStart;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); // this is to prevent the keyboard from opening on startup since there is a EditText in this activity;)

        setupFindViewById();


        setupCitySearchButton();


        builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);

        uvIndexStatusMessage = (TextView) findViewById(R.id.uvIndexStatusMessage);
        uvIndexStatusMessage.setText("");

        prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        firstStart = prefs.getBoolean("firstStart", true); // saved status of the disclaimer page is returned
        String location = prefs.getString("CITY", ""); // saved location of the city is returned
        editTextCitySearch.setText(location);
        editTextCitySearch.setVisibility(View.INVISIBLE);
        FindWeather(); // initial api call on startup

        setupDisclaimer();

        this.getSupportActionBar().hide();

        setupBottomNavigationListener();


        textViewUVIndex.setText(String.valueOf(UVSensorData.getUVIntensity()));

        displayBatteryLevel(String.valueOf(BatteryData.getBatteryLevel()));




        startSunUIThread(getCurrentFocus());
        startUVIndexThread(getCurrentFocus());
        startResetMaxUVThread(getCurrentFocus());

        startDatabaseService();
        startNotificationsService();

        setupRefreshButton();
        setupMoreButton();

    }

    // assign front-end ids to the back-end
    private void setupFindViewById() {
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

        textViewBatteryLevel = (TextView) findViewById(R.id.textViewBatteryLevel);
        imageViewSensor = (ImageView) findViewById(R.id.imageViewSensor);

        textViewUVIndex = (TextView) findViewById(R.id.textViewUVIndex);
        ic_sun = (ImageView) findViewById(R.id.ic_sun);
    }

    // starts the notifications service upon creating of main activity
    private void startNotificationsService() {
        Intent startIntent = new Intent(MainActivity.this, NotificationsService.class);
        startService(startIntent);
    }

    // starts the database service upon creating of main activity
    private void startDatabaseService() {
        Intent startIntent = new Intent(MainActivity.this, DatabaseService.class);
        startService(startIntent);
    }

    // setup the city search button as a toggle
    // Toggles the state of the EditText as being writable or hidden
    // Saves the city upon clicking
    // Runs the method FindWeather() which will call on the Weather API
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

    // The button used for resetting the maxUV read back to 0
    private void setupRefreshButton() {
        toggleButtonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maxUV = 0;
            }
        });
    }

    // The button displayed next to the UV level message
    // Upon clicking, will send the user to the the Info Activity
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

    // Disclaimer page setup
    // Shows upon first time apps startup
    private void setupDisclaimer() {
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
    }

    // method that updates the sun Color depending on the level of UV
    // displays message based on the current UV index level
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

    // displays the current battery level of the sensor device
    private void updateBatteryLevelIcon() {
        batteryLevel = BatteryData.getBatteryLevel();
        if (!isBatteryConnected) {
            imageViewSensorBattery.setImageResource(R.drawable.ic_battery_level_1);
            displayBatteryLevel("0");
        } else {
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
    }

    // displays the UV index by sending data to a TextView
    private void displayUVSensorData(float uvIndex) {
        textViewUVIndex.setText(String.valueOf(uvIndex));
    }

    // creates and starts a thread of the runnable sun ui object
    private void startSunUIThread(View view) {
        RunnableSunColor runnableSunColor = new RunnableSunColor();
        new Thread(runnableSunColor).start();
    }

    // creates and starts a thread of the runnable uv index object
    private void startUVIndexThread(View view) {
        RunnableUVIndex runnableUVIndex = new RunnableUVIndex();
        new Thread(runnableUVIndex).start();
    }

    // creates and starts a thread of the runnable reset max uv object
    private void startResetMaxUVThread(View view) {
        RunnableResetMaxUV runnableResetMaxUV = new RunnableResetMaxUV();
        new Thread(runnableResetMaxUV).start();
    }

    // method used for setting the visibility of the refresh/reset button
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

    // runnable used for updating the sun color, uv index message, and the battery level icon
    // sleeps for 2000 millis
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

    // runnable used for updating the uv index level display, and the battery level data (0 to 100%)
    // sleeps for 5 millis
    // updates the refresh button visibility
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

                        if (isBatteryConnected) {
                            displayBatteryLevel((int) BatteryData.getBatteryLevel() + "%");
                        }
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


    // runnable used for automatically resetting max UV index being displayed
    // sleeps for (user inputted reset time) * 1000 millis
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

    // used if UV Max Mode is ON
    // determines the max uv by reading the current UV level
    // and displays it at the center of the sun icon
    protected void displayUVMaxMode() {
        if (maxUV < UVSensorData.getUVIntensity()) {
            maxUV = UVSensorData.getUVIntensity();
        }
        textViewUVIndex.setText(String.valueOf(maxUV));

    }

    // receive the shared preferences upon resuming this activity
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        //***********************************
        toggleUVModePreferences = getSharedPreferences(UVDisplayModeActivity.UV_MODE_PREFS, MODE_PRIVATE);
        uv_mode_status = toggleUVModePreferences.getBoolean(UVDisplayModeActivity.UV_MODE_STATUS, false);
        refresh_cycle_time = toggleUVModePreferences.getInt(UVDisplayModeActivity.UV_MODE_REFRESH_SETTING, 60);
        isRefreshSetToNever = toggleUVModePreferences.getBoolean(UVDisplayModeActivity.UV_MODE_REFRESH_NEVER, false);

    }

    // updates the sensor connection state drawable icon to ON/OFF
    // changes to ON if data is available
    // changes to OFF if disconnected
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
                updateSensorConnectionState(true);

            }
        }
    };

    // sets the image drawable, and TextView
    private void updateSensorConnectionState(boolean condition) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (condition) {
                    imageViewSensor.setImageResource(R.drawable.ic_sensor_on);
                    textViewSensorState.setText("Sensor ON");
                    isBatteryConnected = true;

                }
                else {
                    imageViewSensor.setImageResource(R.drawable.ic_sensor_off);
                    textViewSensorState.setText("Sensor OFF");
                    BluetoothLeService.mBluetoothGatt.close();
                    BluetoothLeService.mBluetoothGatt = null;
                    isBatteryConnected = false;
                }
            }
        });
    }

    // sets the battery level TextView
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

    // bottom navigation ui setup
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

    // Navigation methods
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

        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }


    // Weather Method
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
                            textViewTemperature.setTextSize(44);

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
                            textViewFeelsLike.setText("Feels like: " + feels_find+" °C");

                            //find pressure
                            JSONObject object7 = jsonObject.getJSONObject("main");
                            String pressure_find = object7.getString("pressure");
                            textViewPressure.setText("Pressure: " + pressure_find+" hPa");

                            //find humidity
                            JSONObject object4 = jsonObject.getJSONObject("main");
                            int humidity_find = object4.getInt("humidity");
                            textViewHumidity.setText("Humidity: " + humidity_find+"%");


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