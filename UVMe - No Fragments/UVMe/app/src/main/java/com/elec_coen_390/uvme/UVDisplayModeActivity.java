package com.elec_coen_390.uvme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.elec_coen_390.uvme.R.layout.activity_uv_display_mode;

public class UVDisplayModeActivity extends AppCompatActivity {


    private ToggleButton toggleButtonUVDisplayMode;

    public static String UV_MODE_PREFS = "uv_mode_prefs";
    public static String UV_MODE_STATUS = "uvi_level_alert_on";

    boolean uv_mode_status;

    public static SharedPreferences toggleUVModePreferences;
    public static SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(activity_uv_display_mode);

        toggleButtonUVDisplayMode = (ToggleButton) findViewById(R.id.toggleButtonUVDisplayMode);


        toggleUVModePreferences = getSharedPreferences(UV_MODE_PREFS, MODE_PRIVATE);
        editor = getSharedPreferences(UV_MODE_PREFS, MODE_PRIVATE).edit();

        uv_mode_status = toggleUVModePreferences.getBoolean(UV_MODE_STATUS, true);


        toggleButtonUVDisplayMode.setChecked(uv_mode_status);


        toggleButton();
        setupBottomNavigationListener();
    }

    private void toggleButton() {
        toggleButtonUVDisplayMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                editor.putBoolean(UV_MODE_STATUS, isChecked);
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
