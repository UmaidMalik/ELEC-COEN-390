package com.elec_coen_390.uvme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HelpActivity extends AppCompatActivity {

    Button buttonOpenURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_help);

        buttonOpenURL = (Button) findViewById(R.id.buttonOpenURL);


        buttonOpenURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToURL("https://umaidmalik.github.io/OneHealth/");
            }
        });

    }



    private void goToURL(String URL) {
        Uri uri = Uri.parse(URL);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }


}