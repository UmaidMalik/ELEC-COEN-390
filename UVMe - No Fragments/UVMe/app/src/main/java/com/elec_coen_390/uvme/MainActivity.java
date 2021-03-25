package com.elec_coen_390.uvme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    AlertDialog.Builder builder;
    public static final String TAG = "Main Activity";

  
    // Declaring variable for splash screen

    private static int SPLASH_TIME_OUT = 3000;

    // Current location: Initializing the variable

    /*

    // in string.xml
    <string name="map key" translatable="false">AIzaSyCDhBHjPpQ64TF7NRswV6NnSkTJxU0JVGo</string>


    API key generated: AIzaSyCDhBHjPpQ64TF7NRswV6NnSkTJxU0JVGo

    Button btLocation;
    TextView tvCity, tvUVI, tvTemp;
    FusedLocationProviderClient fusedLocationProviderClient;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        // Welcome screen = 3 s

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent homeIntent = new Intent(MainActivity.this, WelcomeActivity.class);
                startActivity(homeIntent);
                finish();

            }
        }, SPLASH_TIME_OUT);

        /*
        // Current Location: Assigning variable
        btLocation = findViewById(R.id.bt_Location);
        tvCity = findViewById(R.id.tv_City);
        tvUVI = findViewById(R.id.tv_UVI);
        tvTemp = findViewById(R.id.tv_Temp);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);

        btLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check condition

                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    // When permission granted: call method

                    getCurrentLocation();

                } else {

                    // when permission is not granted: Request permission

                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);


                }
            }
        });

        @Override
        public void onRequestPermissionResult ( int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults){
        if (requestCode == 100 && grantResults.length > 0 && (grantResults[0] + grantResults[1] ==

        PackageManager.PERMISSION_GRANTED)){

    // when permission granted: call method

        getCurrentLocation();
    } else{

            // when permission is denied: display error

            Toast.makeText(getApplicationContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();

    }


    */

        builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);

        TextView title = (TextView) findViewById(R.id.activityMain);
        title.setText("<Message to user>");


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
            this.getSupportActionBar().hide();



        }



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                        count = count % 5;

                        switch (count) {

                            case 0:
                                ic_sun.setImageResource(R.drawable.ic_sunlight_default_level1_lightblue);
                                break;
                            case 1:
                                ic_sun.setImageResource(R.drawable.ic_sunlight_level2);
                                break;
                            case 2:
                                ic_sun.setImageResource(R.drawable.ic_sunlight_level3);
                                break;
                            case 3:
                                ic_sun.setImageResource(R.drawable.ic_sunlight_level4);
                                break;
                            case 4:
                                ic_sun.setImageResource(R.drawable.ic_sunlight_level5);
                                break;
                        }

                System.out.println("Count = " + count);
                }

    private void startSunUIThread(View view) {
        RunnableUVIndex runnableUVIndex = new RunnableUVIndex();
        new Thread(runnableUVIndex).start();
    }

    private class RunnableUVIndex implements Runnable {


                Log.d(TAG, "startThread: " + uvIndex);

        setupBottomNavigationListener();


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);



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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

}