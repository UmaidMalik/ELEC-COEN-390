package com.example.uvme;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.uvme.ui.profileAtributes.eyeAdapter;
import com.example.uvme.ui.profileAtributes.eyeColor;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    // setup for buttons Profile UI

    private ArrayList<eyeColor> meyeColor;
    private eyeAdapter meyeAdapter;

    private TextView selectedProfileTextView;
    private TextView genderTextView;
    private TextView eyeTextView;
    private TextView skinToneTextView;
    private EditText editTextPersonName;
    private EditText editTextAge;
    private Button editButton;
    private Button saveButton;
    private Button addProfileButton;
    private Spinner eyeColourSpinner;
    private Spinner skinToneSpinner;
    private Spinner genderSpinner;
    private SharedPreferences prefseye;
    private SharedPreferences prefsSkin;
    private SharedPreferences prefsGender;
    private final String prefNameEye = "spinner_value_eye";
    private final String prefNameSkin = "spinner_value_skin";
    private  final String prefNameGender ="spinner_value_gender";
    int id_gender,id_eye,id_skin=0;
    final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        saveButton = findViewById(R.id.saveButton);
        eyeTextView =findViewById(R.id.eyeTextView);
        skinToneTextView=findViewById(R.id.skinToneTextView);
        genderTextView=findViewById(R.id.genderTextView);
        selectedProfileTextView=findViewById(R.id.selectedProfileTextView);
        noEdits(); // default to no edits
        Intent intent = getIntent(); // lets us go back and forth from app to app
    }

    @Override
    protected void onResume() {
        super.onResume();
        // this is where to save the button information
        SharedPreferences sharedPreferences = getSharedPreferences("profile-sharedPrefences", Context.MODE_PRIVATE); // open a fille
        String name =sharedPreferences.getString(getString(R.string.profileName),null);
        String age =sharedPreferences.getString(getString(R.string.profileAge),null);
        String eye =sharedPreferences.getString(getString(R.string.profileEye),null);
        String skin = sharedPreferences.getString(getString(R.string.profileSkin),null);
        String gender = sharedPreferences.getString(getString(R.string.profileGender),null);

       if (name == null) {
            loadData(); // if name is empty, load the data
        }
            else {
                // sets the name from user input and saves it in sharedPrefene files
                editTextPersonName.setText(name);
                editTextAge.setText(age);
                eyeTextView.setText(eye);
                skinToneTextView.setText(skin);
                genderTextView.setText(gender);
                selectedProfileTextView.setText(name);
            }
    }

    @SuppressLint("CutPasteId")
    protected void noEdits(){
        // function is used to display users information, edit mode is engaged when selected
       // setup
        editTextPersonName = findViewById(R.id.editTextPersonName);
        editTextAge = findViewById(R.id.editTextAge);
        editButton = findViewById(R.id.editButton);
        addProfileButton = findViewById(R.id.addProfileButton);
        eyeColourSpinner= findViewById(R.id.eyeColourSpinner);
        skinToneSpinner=findViewById(R.id.skinToneSpinner);
        genderSpinner=findViewById(R.id.genderSpinner);

        // enable modes for noedits
        enableModeNoEdit();
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Edits(); // sends to the edit function
                Toast.makeText(getApplicationContext(),"EDIT MODE",Toast.LENGTH_SHORT).show();
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
        eyeColourSpinner.getBackground().setColorFilter(getResources().getColor(R.color.font_lightblue), PorterDuff.Mode.SRC_ATOP);
        skinToneSpinner.getBackground().setColorFilter(getResources().getColor(R.color.font_lightblue), PorterDuff.Mode.SRC_ATOP);
        genderSpinner.getBackground().setColorFilter(getResources().getColor(R.color.font_lightblue), PorterDuff.Mode.SRC_ATOP);
        ///
            final Spinner genderSpin = (Spinner) findViewById(R.id.genderSpinner);
            ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(ProfileActivity.this,
                    R.array.gender, R.layout.color_spinner_layour);
            genderAdapter.setDropDownViewResource(R.layout.sprinner_dropdown_layout);
            genderSpin.setAdapter(genderAdapter);
            prefsGender = getSharedPreferences(prefNameGender, MODE_PRIVATE);
            id_gender = prefsGender.getInt("last_val_gender", 0);
            genderSpin.setSelection(id_gender);
            genderSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int Genderposition, long arg3) {
                    // TODO Auto-generated method stub
                    prefsGender = getSharedPreferences(prefNameGender, MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefsGender.edit();
                    //---save the values in the EditText view to preferences---
                    editor.putInt("last_val_gender", Genderposition);
                    //---saves the values---
                    editor.apply();
                    genderTextView.setText(String.valueOf(Genderposition + 1));

                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });
            genderTextView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @SuppressLint("SetTextI18n")
                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        int nPosition = Integer.parseInt(s.toString());
                        if (nPosition >= 1 && nPosition <= 12) {
                            if (nPosition == 1) {
                                genderTextView.setText("CHANGE");
                            }
                            if (nPosition == 2) {
                                genderTextView.setText("Male");
                            }
                            if (nPosition == 3) {
                                genderTextView.setText("Female");
                            }
                            genderSpinner.setSelection(nPosition - 1);
                        }
                    } catch (NumberFormatException nfe) {
                        Log.i(TAG, "Unable to find N Position.");
                    }
                }
            });


        ///
         initList();
        final  Spinner eyeColor=(Spinner) findViewById(R.id.eyeColourSpinner);
        meyeAdapter=new eyeAdapter(this,meyeColor);
        eyeColor.setAdapter(meyeAdapter);
        prefseye = getSharedPreferences(prefNameEye, MODE_PRIVATE);
        id_eye=prefseye.getInt("last_val_eye",0);
        eyeColor.setSelection(id_eye);
        eyeColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long arg3) {
                // TODO Auto-generated method stub

                prefseye = getSharedPreferences(prefNameEye, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefseye.edit();
                //---save the values in the EditText view to preferences---
                editor.putInt("last_val_eye", position);
                //---saves the values---
                editor.apply();
                eyeColor clickedItem = (eyeColor) arg0.getItemAtPosition(position);
                String clickedEye=clickedItem.getEyeColor();

                eyeTextView.setText(String.valueOf(position + 1));
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
            }
        });

        eyeTextView.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s)
            {
                try
                {
                    int nPosition = Integer.parseInt(s.toString());
                    if (nPosition >= 1 && nPosition <= 12) {
                           if(nPosition==1){
                               eyeTextView.setText("Default");
                           }
                                if(nPosition==2){
                                    eyeTextView.setText("Blue");
                                }
                                    if(nPosition==3){
                                        eyeTextView.setText("Brown");
                                    }
                                        if(nPosition==4){
                                            eyeTextView.setText("Gray");
                                        }
                                            if(nPosition==5){
                                                eyeTextView.setText("Green");
                                            }
                                                eyeColourSpinner.setSelection(nPosition - 1);
                    }
                }
                catch(NumberFormatException nfe)
                {
                    Log.i(TAG, "Unable to find N Position.");
                }
            }
        });

        final Spinner skinTone=(Spinner) findViewById(R.id.skinToneSpinner);
        ArrayAdapter<CharSequence> skinToneAdapter = ArrayAdapter.createFromResource(
                ProfileActivity.this,
                R.array.skinTone,
                R.layout.color_spinner_layour);
        skinToneAdapter.setDropDownViewResource(R.layout.sprinner_dropdown_layout);
        skinTone.setAdapter(skinToneAdapter);
        prefsSkin = getSharedPreferences(prefNameSkin, MODE_PRIVATE);
        id_skin=prefsSkin.getInt("last_val_skin",0);
        skinTone.setSelection(id_skin);
        skinTone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int pos, long arg3) {
                // TODO Auto-generated method stub
                prefsSkin = getSharedPreferences(prefNameSkin, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefsSkin.edit();
                //---save the values in the EditText view to preferences---
                editor.putInt("last_val_skin", pos);
                //---saves the values---
                editor.apply();
                skinToneTextView.setText(String.valueOf(pos + 1));
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
            }
        });
        skinToneTextView.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i(TAG, "Before Text Change Reached.");
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i(TAG, "On Text Change Reached.");
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable skin)
            {
                try
                {
                    int nPosition = Integer.parseInt(skin.toString());
                    if (nPosition >= 1 && nPosition <= 12) {
                        if(nPosition==1){
                            skinToneTextView.setText("CHANGE");
                        }
                            if(nPosition==2){
                                skinToneTextView.setText("Light");
                            }
                                if(nPosition==3){
                                    skinToneTextView.setText("Medium");
                                }
                                    if(nPosition==4){
                                        skinToneTextView.setText("Dark");
                                    }
                                        skinToneSpinner.setSelection(nPosition - 1);
                    }
                }
                catch(NumberFormatException nfe)
                {
                    Log.i(TAG, "Unable to find N Position.");
                }
            }
        });
        enableModeEdit(); // sets all enable modes.
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String name =editTextPersonName.getText().toString();
               String profile = selectedProfileTextView.getText().toString();
                String age = editTextAge.getText().toString();
                String eye= eyeTextView.getText().toString();
                String skin=skinToneTextView.getText().toString();
                String gender=genderTextView.getText().toString();
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.mainFile), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.profileName),name);
                editor.putString(getString(R.string.profileAge),age);
                editor.putString(getString(R.string.profileEye),eye);
                editor.putString(getString(R.string.profileSkin),skin);
                editor.putString(getString(R.string.profileGender),gender);
                editor.putString("profile-sharedPrefences",profile);
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
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
    public void loadData() // takes the data from savebutton and loads it back into text box
    {
        SharedPreferences sharedPreferences = getSharedPreferences("profile-sharedPrefences",MODE_PRIVATE);
    }
    public void enableModeEdit(){
        editTextAge.setEnabled(true);
        editTextPersonName.setEnabled(true);
        saveButton.setEnabled(true);
        addProfileButton.setEnabled(true);
        editButton.setEnabled(true);
        eyeColourSpinner.setEnabled(true);
        skinToneSpinner.setEnabled(true);

        genderSpinner.setEnabled(true);
        eyeTextView.setEnabled(false);
        eyeTextView.setVisibility(View.INVISIBLE);
        skinToneTextView.setEnabled(false);
        skinToneTextView.setVisibility(View.INVISIBLE);
        genderTextView.setEnabled(false);
        genderTextView.setVisibility(View.INVISIBLE);

    }

    public void enableModeNoEdit(){
        editTextAge.setEnabled(false);
        editTextPersonName.setEnabled(false);
        saveButton.setCursorVisible(true);
        saveButton.setEnabled(false);
        addProfileButton.setEnabled(true);
        editButton.setEnabled(true);
        eyeColourSpinner.setEnabled(false);
        skinToneSpinner.setEnabled(false);
        genderSpinner.setEnabled(false);

        eyeTextView.setEnabled(true);
        skinToneTextView.setEnabled(true);
        genderTextView.setEnabled(true);
    }
    private void initList(){
        meyeColor = new ArrayList<>();
        meyeColor.add(new eyeColor("Default",R.drawable.ic_eye_default));
        meyeColor.add(new eyeColor("Blue",R.drawable.ic_eye_blue));
        meyeColor.add(new eyeColor("Brown",R.drawable.ic_eye_brown));
        meyeColor.add(new eyeColor("Gray",R.drawable.ic_eye_gray));
        meyeColor.add(new eyeColor("Green",R.drawable.ic_eye_green));

    }
}