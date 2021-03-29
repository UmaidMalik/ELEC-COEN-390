package com.elec_coen_390.uvme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import android.widget.ToggleButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NotificationsActivity extends AppCompatActivity {


    private ToggleButton toggleButtonUVILevelAlert, toggleButtonBurnRiskAlert, toggleButtonSunglassesAlert;

    public static boolean UVI_LEVEL_ALERT_STATE = true, BURN_RISK_ALERT_STATE = true, SUNGLASSES_ALERT = true;

    private static String PREFS = "toggle_prefs";
    private static String UVI_LEVEL_ALERT_STATUS = "uvi_level_alert_on";
    private static String BURN_RISK_ALERT_STATUS = "burn_risk_alert_on";

    boolean uvi_level_alert_status, burn_risk_alert_status;

    static SharedPreferences togglePreferences;
    static SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_notifications);

        toggleButtonUVILevelAlert = (ToggleButton) findViewById(R.id.toggleButtonUVILevelAlert);
        toggleButtonBurnRiskAlert = (ToggleButton) findViewById(R.id.toggleButtonBurnRiskAlert);
        toggleButtonSunglassesAlert = (ToggleButton) findViewById(R.id.toggleButtonSunglassesAlert);

        togglePreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        editor = getSharedPreferences(PREFS, MODE_PRIVATE).edit();

        uvi_level_alert_status = togglePreferences.getBoolean(UVI_LEVEL_ALERT_STATUS, true);
        burn_risk_alert_status = togglePreferences.getBoolean(BURN_RISK_ALERT_STATUS, true);


        toggleButtonUVILevelAlert.setChecked(uvi_level_alert_status);
        toggleButtonBurnRiskAlert.setChecked(burn_risk_alert_status);


        setupBottomNavigationListener();

        setupToggleButtons();

    }

    private void setupToggleButtons() {




        toggleButtonUVILevelAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                UVI_LEVEL_ALERT_STATE = isChecked;
                editor.putBoolean(UVI_LEVEL_ALERT_STATUS, isChecked);
                editor.apply();
                toggleButtonUVILevelAlert.setChecked(isChecked);

            }
        });

        toggleButtonBurnRiskAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                BURN_RISK_ALERT_STATE = isChecked;
                editor.putBoolean(BURN_RISK_ALERT_STATUS, isChecked);
                editor.apply();
                toggleButtonBurnRiskAlert.setChecked(isChecked);
            }
        });

        toggleButtonSunglassesAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SUNGLASSES_ALERT = true;
                } else {
                    SUNGLASSES_ALERT = false;
                }

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

    /*
    private class CustomAdapter extends BaseAdapter {



        @Override
        public int getCount() {
            return notificationNames.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View moreView = getLayoutInflater().inflate(R.layout.activity_notifications_row_data, null);

            TextView iconTitle = moreView.findViewById(R.id.textViewIconTitle);
            ToggleButton toggleButton = moreView.findViewById(R.id.simpleToggleButton);
            //ImageView iconImage = moreView.findViewById(R.id.imageViewIconImage);

            iconTitle.setText(notificationNames[i]);
            //iconImage.setImageResource(iconImages[i]);

            return moreView;
        }
    }

     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToMoreActivity();
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
    protected void goToMainActivity() {
        Intent intentMain = new Intent(this, MainActivity.class);
        startActivity(intentMain);
        finish();
    }




}

