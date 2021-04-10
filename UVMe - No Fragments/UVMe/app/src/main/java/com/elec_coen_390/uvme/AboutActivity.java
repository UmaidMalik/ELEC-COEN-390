package com.elec_coen_390.uvme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.elec_coen_390.uvme.R.layout.activity_about;

public class AboutActivity extends AppCompatActivity {

    Button buttonOpenURL;
    TextView textViewQ1;
    TextView textViewA1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(activity_about);

        /*
        buttonOpenURL = (Button) findViewById(R.id.buttonOpenURL);


        buttonOpenURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToURL("https://umaidmalik.github.io/OneHealth/");
            }
        });

         */

        // start of FAQ
        textViewQ1 = (TextView) findViewById(R.id.textViewAbout_Q1);


            textViewQ1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textViewA1.setVisibility(View.VISIBLE);
                }
            });

    }



    private void goToURL(String URL) {
        Uri uri = Uri.parse(URL);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        goToMoreActivity();
    }

    protected void goToMoreActivity() {
        Intent intentMore = new Intent(this, MoreActivity.class);
        intentMore.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intentMore);
    }


    /*
     <Button
    android:id="@+id/buttonOpenURL"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="go to app webpage"
    android:layout_alignParentBottom="true"
    android:layout_marginBottom="60dp"
    android:fontFamily="@font/roboto_light"
    android:textColor="@color/light_blue"
    android:layout_centerInParent="true"/>

     */
}