package com.example.uvme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.uvme.ui.profileAtributes.eyeAdapter;
import com.example.uvme.ui.profileAtributes.eyeColor;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

// salar testing git
public class MainActivity extends AppCompatActivity {

    private ArrayList<eyeColor> meyeColor;
    private eyeAdapter meyeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        goToProfileActivity();
        initList();
        Spinner spinner= findViewById(R.id.spinnerTest);
        meyeAdapter=new eyeAdapter(this,meyeColor);
        spinner.setAdapter(meyeAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                eyeColor clickedItem = (eyeColor) adapterView.getItemAtPosition(i);
                String clickedEye=clickedItem.getEyeColor();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }
     // function to take us to profile activty using intent
    protected  void goToProfileActivity(){
        Intent intent = new Intent(this, ProfileActivity.class); // where we are then where we wanna go
        startActivity(intent);
    }

    private void initList(){
        meyeColor = new ArrayList<>();
        meyeColor.add(new eyeColor("Blue",R.drawable.ic_eye_blue));
        meyeColor.add(new eyeColor("Green",R.drawable.ic_eye_green));
    }

}