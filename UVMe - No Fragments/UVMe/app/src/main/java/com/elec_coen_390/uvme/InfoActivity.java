package com.elec_coen_390.uvme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
public class InfoActivity extends AppCompatActivity {

    private ListView listView;
    private String[] indexLevels = {"0-2", "3-5", "6-7", "8-10", "11+"};
    private String[] indexColorMeanings = {"Low", "Moderate", "High", "Very High", "Extreme"};
    private String[] listOfProtections = {"Minimal sun protection required for normal activity." +
                                            "Wear sunglasses on bright days. If outside for more than one hour, cover up and use sunscreen." +
                                            "Reflection off snow can nearly double UV strength, so wear sunglasses and apply sunscreen on your face.",


                                            "Take precaution by covering up, and wearing a hat, sunglasses and sunscreen, especially if you will be outside for 30 minutes or more." +
                                            "Look for shade near midday when the sun is strongest.",

                                            "Protection required - UV damages the skin and can cause sunburn." +
                                                    "Reduce time in the sun between 11 a.m. and 3 p.m. and take full precaution by seeking shade, covering up exposed skin, wearing a hat and sunglasses, and applying sunscreen.",

                                            "Extra precaution required - unprotected skin will be damaged and can burn quickly." +
                                                    "Avoid the sun between 11 a.m. and 3 p.m. and seek shade, cover up, and wear a hat, sunglasses and sunscreen.",

                                            "Values of 11 or more are very rare in Canada. However, the UV Index can reach 14 or higher in the tropics and southern U.S." +
                                                    "Take full precaution. Unprotected skin will be damaged and can burn in minutes. Avoid the sun between 11 a.m. and 3 p.m., cover up, and wear a hat, sunglasses and sunscreen." +
                                                    "Don’t forget that white sand and other bright surfaces reflect UV and increase UV exposure." };
    private int[] colorImages = {R.drawable.ic_low, R.drawable.ic_moderate, R.drawable.ic_high, R.drawable.ic_veryhigh, R.drawable.ic_extreme};

    private TextView textViewInfoURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        this.getSupportActionBar().hide();



        setupListView();

        setupBottomNavigationListener();

        setupInfoURL();


    }

    private void setupInfoURL() {
        textViewInfoURL = (TextView) findViewById(R.id.textViewInfoURL);
        textViewInfoURL.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setupListView() {
        listView = (ListView) findViewById(R.id.moreActivityListView);

        InfoActivity.CustomAdapter customAdapter = new InfoActivity.CustomAdapter();
        listView.setAdapter(customAdapter);
    }

    private class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return colorImages.length;
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
            View moreView = getLayoutInflater().inflate(R.layout.activity_info_row_data, null);

            TextView uvIndex = moreView.findViewById(R.id.textViewUVIndex);
            TextView colorMeaning = moreView.findViewById(R.id.textViewColorMeaning);
            TextView recommendedProtections = moreView.findViewById(R.id.textViewRecommendedProtections);
            ImageView colorImage = moreView.findViewById(R.id.imageViewColorImage);

            uvIndex.setText(indexLevels[i]);
            colorMeaning.setText(indexColorMeanings[i]);
            recommendedProtections.setText(listOfProtections[i]);
            colorImage.setImageResource(colorImages[i]);

            return moreView;
        }
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

                    case R.id.action_profile:
                        item.setCheckable(true);
                        goToProfileActivity();
                        return true;
                        //break;

                    case R.id.action_more:
                        item.setCheckable(true);
                        goToMoreActivity();
                        return true;
                        //break;

                    case R.id.action_home:
                        item.setCheckable(true);
                        goToMainActivity();
                        return true;
                        //break;

                    default:

                }
                return false;
            }
        });
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





