package com.elec_coen_390.uvme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class ThemesActivity extends AppCompatActivity {


    private Switch aSwitch;
    public static final String MyPreference = "NightModePrefs";
    public static final String Key_IsNightMode = "isNightMode";
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_themes);

        TextView title = (TextView) findViewById(R.id.activityThemes);

        sharedPreferences = getSharedPreferences(MyPreference, MODE_PRIVATE);
        aSwitch = findViewById(R.id.darkmode_switch);
        checkNightModeActivated();

        boolean isChecked;


        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    saveNightModeState(true);
                    recreate();
                } else {

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    saveNightModeState(false);
                    recreate();
                }



            }


        });

    }

    private void saveNightModeState(boolean nightMode) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Key_IsNightMode, nightMode);
        editor.apply();

    }

    public void checkNightModeActivated(){
        if (sharedPreferences.getBoolean(Key_IsNightMode, false)){
            aSwitch.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        }else{
            aSwitch.setChecked(false);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

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
