package com.elec_coen_390.uvme.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.elec_coen_390.uvme.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * This method displays a list of all the other activities which can be
 * accessed from here { notifications, uv history, uv sensor, uv index
 * info, themes, uv display mode, about, send feedback}
 *
 *
 */

public class MoreActivity extends AppCompatActivity {

    // initialize arrays for the ListView
    private ListView listView;
    String[] iconNames = {"Notifications", "UV History", "UV Sensor", "UV Index Info", "Themes", "UV Display Mode", "About", "Send Feedback"};
    int[] iconImages = {R.drawable.ic_notifications, R.drawable.ic_uv_history, R.drawable.ic_uv_sensor, R.drawable.ic_info, R.drawable.ic_themes, R.drawable.ic_mode, R.drawable.ic_about, R.drawable.ic_baseline_feedback_24};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getSupportActionBar().hide();

        setContentView(R.layout.activity_more);

        setupListView();

        setupBottomNavigationListener();
    }

    // custom list view adapter setup
    private class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return iconImages.length;
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
            View moreView = getLayoutInflater().inflate(R.layout.activity_more_row_data, null);

            TextView iconTitle = moreView.findViewById(R.id.textViewIconTitle);
            ImageView iconImage = moreView.findViewById(R.id.imageViewIconImage);
            iconImage.setImageResource(iconImages[i]);

            iconTitle.setText(iconNames[i]);

            return moreView;
        }
    }

    // setup list view icons , names, and intent
    private void setupListView() {
        listView = (ListView) findViewById(R.id.moreActivityListView);

        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent;
                switch(i) {

                    case 0:
                        intent = new Intent(getApplicationContext(), NotificationsActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getApplicationContext(), UVHistoryActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(getApplicationContext(), UVSensorActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(getApplicationContext(), InfoActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(getApplicationContext(), ThemesActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(getApplicationContext(), UVDisplayModeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        break;
                    case 6:
                        intent = new Intent(getApplicationContext(), AboutActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        break;
                    case 7:
                        intent = new Intent(getApplicationContext(), FeedbackActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    // setup bottom navigation
    private void setupBottomNavigationListener() {

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2); // bottom navigation menu index item {0(Profile),1(Home),2(More)}
        menuItem.setChecked(true);

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

                        break;
                }
                return false;
            }
        });

    }

    // navigation methods
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        goToMainActivity();
    }

    protected void goToProfileActivity() {
        Intent intentProfile = new Intent(this, ProfileActivity.class);
        intentProfile.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intentProfile);
        //finish();
    }

    protected void goToMainActivity() {
        Intent intentMain = new Intent(this, MainActivity.class);
        intentMain.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intentMain);
        //finish();
    }

}

