package com.elec_coen_390.uvme.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.elec_coen_390.uvme.Activities.MainActivity;
import com.elec_coen_390.uvme.Activities.MoreActivity;
import com.elec_coen_390.uvme.Activities.ProfileActivity;
import com.elec_coen_390.uvme.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static com.elec_coen_390.uvme.R.layout.activity_uv_display_mode;

public class UVDisplayModeActivity extends AppCompatActivity {


    private ToggleButton toggleButtonUVDisplayMode;

    public static String UV_MODE_PREFS = "uv_mode_prefs";
    public static String UV_MODE_STATUS = "uvi_level_alert_on";
    public static String UV_MODE_REFRESH_SETTING = "uv_mode_refresh_setting";
    public static String UV_MODE_REFRESH_NEVER = "uv_mode_refresh_never";

    boolean uv_mode_status;
    int refresh_cycle_time;
    boolean isRefreshSetToNever;

    public static SharedPreferences toggleUVModePreferences;
    public static SharedPreferences.Editor editor;

    private Spinner spinnerUVRefresh;

    private int t_15_seconds = 15;
    private int t_30_seconds = 30;
    private int t_1_minute = 60;
    private int t_5_minutes = 300;
    private int t_15_minutes = 900;
    private int t_30_minutes = 1800;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(activity_uv_display_mode);

        toggleButtonUVDisplayMode = (ToggleButton) findViewById(R.id.toggleButtonUVDisplayMode);
        spinnerUVRefresh = (Spinner) findViewById(R.id.spinnerUVRefresh);


        toggleUVModePreferences = getSharedPreferences(UV_MODE_PREFS, MODE_PRIVATE);
        editor = getSharedPreferences(UV_MODE_PREFS, MODE_PRIVATE).edit();

        uv_mode_status = toggleUVModePreferences.getBoolean(UV_MODE_STATUS, true);
        toggleButtonUVDisplayMode.setChecked(uv_mode_status);


        //refresh_cycle_time = toggleUVModePreferences.getInt(UV_MODE_REFRESH_SETTING, 60); // default 60 seconds / 1 minute

        //isRefreshSetToNever = toggleUVModePreferences.getBoolean(UV_MODE_REFRESH_NEVER, false);

        toggleButton();
        setupUVRefreshSpinner();
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

    private void setupUVRefreshSpinner() {
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.spinnerRefreshTimes, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUVRefresh.setAdapter(spinnerAdapter);
        spinnerUVRefresh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.light_blue));


                switch (i) {
                    case 0:
                        editor.putInt(UV_MODE_REFRESH_SETTING, t_15_seconds);
                        editor.putBoolean(UV_MODE_REFRESH_NEVER, false);
                        editor.apply();
                        break;
                    case 1:
                        editor.putInt(UV_MODE_REFRESH_SETTING, t_30_seconds);
                        editor.putBoolean(UV_MODE_REFRESH_NEVER, false);
                        editor.apply();
                        break;
                    case 2:
                        editor.putInt(UV_MODE_REFRESH_SETTING, t_1_minute);
                        editor.putBoolean(UV_MODE_REFRESH_NEVER, false);
                        editor.apply();
                        break;
                    case 3:
                        editor.putInt(UV_MODE_REFRESH_SETTING, t_5_minutes);
                        editor.putBoolean(UV_MODE_REFRESH_NEVER, false);
                        editor.apply();
                        break;
                    case 4:
                        editor.putInt(UV_MODE_REFRESH_SETTING, t_15_minutes);
                        editor.putBoolean(UV_MODE_REFRESH_NEVER, false);
                        editor.apply();
                        break;
                    case 5:
                        editor.putInt(UV_MODE_REFRESH_SETTING, t_30_minutes);
                        editor.putBoolean(UV_MODE_REFRESH_NEVER, false);
                        editor.apply();
                        break;
                    case 6:
                        editor.putBoolean(UV_MODE_REFRESH_NEVER, true);
                        editor.apply();
                        break;
                    default:
                        break;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
