package com.elec_coen_390.uvme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ToggleButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

//test test

public class NotificationsActivity extends AppCompatActivity {

    private ListView listView;
    public String[] notificationNames = {"Burn Risk Alert", "UVI Level Alert", "Sunglasses Alert"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_notifications);

        setupListView();

        setupBottomNavigationListener();

        TextView title = findViewById(R.id.textViewNotificationsActivity);
        title.setText("Notifications");

    }
    private void setupListView() {
        listView = findViewById(R.id.listViewNotificationsActivity);

        NotificationsActivity.CustomAdapter customAdapter = new NotificationsActivity.CustomAdapter();
        listView.setAdapter(customAdapter);

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }); */
    }

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

    private class CustomAdapter extends BaseAdapter {

//        @Override
//        public int getCount() {
//            return iconImages.length;
//        }

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