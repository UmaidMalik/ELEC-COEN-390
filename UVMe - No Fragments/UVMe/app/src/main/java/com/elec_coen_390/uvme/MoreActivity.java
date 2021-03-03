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

public class MoreActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        TextView title = (TextView) findViewById(R.id.activityMore);
        title.setText("More Activity");

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2); // bottom navigation menu index item {0(Profile),1(Home),2(More)}
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.action_home:
                        Intent intentHome = new Intent(MoreActivity.this, MainActivity.class);
                        startActivity(intentHome);
                        break;

                    case R.id.action_profile:
                        Intent intentProfile = new Intent(MoreActivity.this, ProfileActivity.class);
                        startActivity(intentProfile);
                        break;

                    case R.id.action_more:

                        break;
                }
                return false;
            }
        });
    }
}

