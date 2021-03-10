package com.elec_coen_390.uvme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


     Button button;
     int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getSupportActionBar().hide();


        setContentView(R.layout.activity_main);

        TextView title = (TextView) findViewById(R.id.activityMain);
        title.setText("Main/Home Activity");


        button = (Button) findViewById(R.id.button);
        ImageView ic_sun = (ImageView) findViewById(R.id.ic_sun);

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

        });


        /*
         ic_sun.setImageResource(R.drawable.ic_sunlight_default_level1_lightblue);
         ic_sun.setImageResource(R.drawable.ic_sunlight_level2);
         ic_sun.setImageResource(R.drawable.ic_sunlight_level3);
         ic_sun.setImageResource(R.drawable.ic_sunlight_level4);
         ic_sun.setImageResource(R.drawable.ic_sunlight_level5);
         */

        setupBottomNavigationListener();





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