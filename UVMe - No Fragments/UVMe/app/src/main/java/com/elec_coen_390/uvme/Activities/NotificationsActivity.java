package com.elec_coen_390.uvme.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;

import android.widget.ToggleButton;

import com.elec_coen_390.uvme.Activities.MainActivity;
import com.elec_coen_390.uvme.Activities.MoreActivity;
import com.elec_coen_390.uvme.Activities.ProfileActivity;
import com.elec_coen_390.uvme.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NotificationsActivity extends AppCompatActivity {


    private ToggleButton toggleButtonUVILevelAlert, toggleButtonSunglassesAlert, toggleButtonSunburnAlert;

    public static String PREFS = "toggle_prefs";
    public static String UVI_LEVEL_ALERT_STATUS = "uvi_level_alert_on";
    public static String SUNGLASSES_ALERT_STATUS = "sunglasses_alert_on";
    public static String SUNBURN_ALERT_STATUS = "sunburn_alert_on";

    boolean uvi_level_alert_status, sunglasses_alert_status, sunburn_alert_status;

    public static SharedPreferences togglePreferences;
    public static SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_notifications);

        toggleButtonUVILevelAlert = (ToggleButton) findViewById(R.id.toggleButtonUVILevelAlert);
        toggleButtonSunglassesAlert = (ToggleButton) findViewById(R.id.toggleButtonSunglassesAlert);
        toggleButtonSunburnAlert = (ToggleButton) findViewById(R.id.toggleButtonSunburnAlert);

        togglePreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        editor = getSharedPreferences(PREFS, MODE_PRIVATE).edit();

        uvi_level_alert_status = togglePreferences.getBoolean(UVI_LEVEL_ALERT_STATUS, true);
        sunglasses_alert_status = togglePreferences.getBoolean(SUNGLASSES_ALERT_STATUS, true);
        sunburn_alert_status = togglePreferences.getBoolean(SUNBURN_ALERT_STATUS, true);


        toggleButtonUVILevelAlert.setChecked(uvi_level_alert_status);
        toggleButtonSunglassesAlert.setChecked(sunglasses_alert_status);
        toggleButtonSunburnAlert.setChecked(sunburn_alert_status);

        setupBottomNavigationListener();

        setupToggleButtons();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setupToggleButtons() {


        toggleButtonUVILevelAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(UVI_LEVEL_ALERT_STATUS, isChecked);
                editor.apply();
            }
        });

        toggleButtonSunglassesAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(SUNGLASSES_ALERT_STATUS, isChecked);
                editor.apply();
            }
        });

        toggleButtonSunburnAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(SUNBURN_ALERT_STATUS, isChecked);
                editor.apply();
            }
        });

    }

    private void setupBottomNavigationListener() {

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        // Menu items are left unselected
        bottomNavigationView.getMenu().getItem(0).setCheckable(false);
        bottomNavigationView.getMenu().getItem(1).setCheckable(false);
        bottomNavigationView.getMenu().getItem(2).setCheckable(false);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.action_home:
                        goToMainActivity();
                        break;

                    case R.id.action_profile:
                        goToProfileActivity();
                        break;

                    case R.id.action_more:
                        goToMoreActivity();
                        break;
                }
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        goToMoreActivity();
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
    protected void goToMainActivity() {
        Intent intentMain = new Intent(this, MainActivity.class);
        intentMain.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intentMain);
    }
}