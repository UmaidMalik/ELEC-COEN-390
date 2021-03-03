package com.elec_coen_390.uvme;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView title = (TextView) findViewById(R.id.activityProfile);
        title.setText("Profile Activity");

        setupBottomNavigationListener();

    }

    private void setupBottomNavigationListener() {

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0); // bottom navigation menu index item {0(Profile),1(Home),2(More)}
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.action_home:
                        Intent intentHome = new Intent(ProfileActivity.this, MainActivity.class);
                        startActivity(intentHome);
                        break;

                    case R.id.action_profile:

                        break;

                    case R.id.action_more:
                        Intent intentMore = new Intent(ProfileActivity.this, MoreActivity.class);
                        startActivity(intentMore);
                        break;
                }
                return false;
            }
        });
    }
}
