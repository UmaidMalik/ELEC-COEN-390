package com.example.uvme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    // setup for buttons Profile UI
    private EditText editTextPersonName;
    private EditText editTextAge;
    private Button editButton;
    private Button saveButton;
    private Button addProfileButton;
    private Spinner eyeColourSpinner;
    private Spinner skinToneSpinner;
    private Spinner profileSelectSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        saveButton = findViewById(R.id.saveButton);




        noEdits(); // default to no edits
        Intent intent = getIntent(); // lets us go back and forth from app to app
    }
    @Override
    protected void onResume() {
        super.onResume();
        // this is where to save the button information
        SharedPreferences sharedPreferences = getSharedPreferences("profile-sharedPrefences", Context.MODE_PRIVATE); // open a fille
        String name =sharedPreferences.getString("name-sharedPrefences",null);
        String age =sharedPreferences.getString("age-sharedPrefences",null);

        if (name == null) {
            loadData(); // if name is empty, load the data
        }
        else {
            // sets the name from user input and saves it in sharedPrefene files
            editTextPersonName.setText(name);
            editTextAge.setText(age);

        }
    }
    protected void noEdits(){ // function is used to display users information, edit mode is engaged when selected
       // setup
        editTextPersonName = findViewById(R.id.editTextPersonName);
        editTextAge = findViewById(R.id.editTextAge);
        editButton = findViewById(R.id.editButton);
        addProfileButton = findViewById(R.id.addProfileButton);
        eyeColourSpinner= findViewById(R.id.eyeColourSpinner);
        skinToneSpinner=findViewById(R.id.skinToneSpinner);
        profileSelectSpinner=findViewById(R.id.profileSelectSpinner);
        Spinner profileSwitchSpinner = findViewById(R.id.profileSelectSpinner);
        profileSwitchSpinner.getBackground().setColorFilter(getResources().getColor(R.color.font_lightblue), PorterDuff.Mode.SRC_ATOP);
         // this needs to be changed. future uses will get users created profiles from database.
        ArrayList<String> listOfNames =new ArrayList<>(); // list of strings for users created profiles.
        listOfNames.add("Salar Jaberi");
        listOfNames.add("kelly");
        listOfNames.add("Saag");
        listOfNames.add("Umaid");
        listOfNames.add("Parker");
        ArrayAdapter<String> profileAdapter = new ArrayAdapter<String>(this,R.layout.color_spinner_layour,listOfNames);
        profileAdapter.setDropDownViewResource(R.layout.sprinner_dropdown_layout);
        profileSwitchSpinner.setAdapter(profileAdapter);
        profileSwitchSpinner.setOnItemSelectedListener(this);
        // enable modes for noedits
        editTextAge.setEnabled(false);
        editTextPersonName.setEnabled(false);
        saveButton.setCursorVisible(true);
        saveButton.setEnabled(false);
        addProfileButton.setEnabled(true);
        editButton.setEnabled(true);
        eyeColourSpinner.setEnabled(false);
        skinToneSpinner.setEnabled(false);
        profileSelectSpinner.setEnabled(true);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Edits(); // sends to the edit function
                Toast.makeText(getApplicationContext(),"EDIT MODE",Toast.LENGTH_LONG).show();
            }
        });

    }
    // function is used for editing users information
    protected void Edits(){
        // setup
        editTextPersonName = findViewById(R.id.editTextPersonName);
        editTextAge = findViewById(R.id.editTextAge);
        editButton = findViewById(R.id.editButton);
        addProfileButton = findViewById(R.id.addProfileButton);

        // spinner setup and setting the drop down icon to light blue
        Spinner eyeColor = findViewById(R.id.eyeColourSpinner);
        Spinner skinTone = findViewById(R.id.skinToneSpinner);
        eyeColourSpinner.getBackground().setColorFilter(getResources().getColor(R.color.font_lightblue), PorterDuff.Mode.SRC_ATOP);
        skinToneSpinner.getBackground().setColorFilter(getResources().getColor(R.color.font_lightblue), PorterDuff.Mode.SRC_ATOP);

        // adapter used for eyeColor, gets information for eyecolor options in string xml file
        ArrayAdapter<CharSequence> eyeColorAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.eyeColor,
                R.layout.color_spinner_layour);
        eyeColorAdapter.setDropDownViewResource(R.layout.sprinner_dropdown_layout); // layout created to accomodate color sequence
        eyeColor.setAdapter(eyeColorAdapter);
        eyeColor.setOnItemSelectedListener(this);

        // adapter used for skin Tone, gets information for eyecolor options in string xml file
        ArrayAdapter<CharSequence> skinToneAdapter = ArrayAdapter.createFromResource(
                this,
                R.array. skinTone,
                R.layout.color_spinner_layour);
        skinToneAdapter.setDropDownViewResource(R.layout.sprinner_dropdown_layout);
        skinTone.setAdapter(skinToneAdapter);
        skinTone.setOnItemSelectedListener(this);

        // this needs to be changed. future uses will get users created profiles from database.


        editTextAge.setEnabled(true);
        editTextPersonName.setEnabled(true);
        saveButton.setEnabled(true);
        addProfileButton.setEnabled(true);
        editButton.setEnabled(true);
        eyeColourSpinner.setEnabled(true);
        skinToneSpinner.setEnabled(true);
        profileSelectSpinner.setEnabled(false);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name =editTextPersonName.getText().toString();
                String age = editTextAge.getText().toString();

                SharedPreferences sharedPreferences = getSharedPreferences("profile-sharedPrefences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name-sharedPrefences",name);
                editor.putString("age-sharedPrefences",age);

                editor.apply(); // saved into the file

                noEdits(); // after user saved information it is sent back to the No edit mode.
                Toast.makeText(getApplicationContext(),"SAVED",Toast.LENGTH_LONG).show();
            }
        });
    }
    // override functions needed to output the Spinners information with their given locations.
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String text= adapterView.getItemAtPosition(position).toString();
        //Toast.makeText(adapterView.getContext(),text,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    public void loadData() // takes the data from savebutton and loads it back into text box
    {
        SharedPreferences sharedPreferences = getSharedPreferences("profile-sharedPrefences",MODE_PRIVATE);
    }
}