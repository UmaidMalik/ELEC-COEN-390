package com.elec_coen_390.uvme;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MoreActivity extends AppCompatActivity {

    private ListView listView;
    String[] iconNames = {"Notifications", "UV History", "UV Sensor", "Info", "Themes"};
    int[] iconImages = {R.drawable.ic_notifications, R.drawable.ic_uv_history, R.drawable.ic_uv_sensor, R.drawable.ic_info, R.drawable.ic_themes};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getSupportActionBar().hide();

        setContentView(R.layout.activity_more);

        setupListView();

        setupBottomNavigationListener();
    }

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

            iconTitle.setText(iconNames[i]);
            iconImage.setImageResource(iconImages[i]);

            return moreView;
        }
    }

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
                        startActivity(intent);
                        finish();
                        break;
                    case 1:
                        intent = new Intent(getApplicationContext(), UVHistoryActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case 2:
                        intent = new Intent(getApplicationContext(), UVSensorActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case 3:
                        intent = new Intent(getApplicationContext(), InfoActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case 4:
                        intent = new Intent(getApplicationContext(), ThemesActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                }
            }
        });
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToMainActivity();
    }

    protected void goToProfileActivity() {
        Intent intentProfile = new Intent(this, ProfileActivity.class);
        startActivity(intentProfile);
        finish();
    }

    protected void goToMainActivity() {
        Intent intentMain = new Intent(this, MainActivity.class);
        startActivity(intentMain);
        finish();
    }

}

