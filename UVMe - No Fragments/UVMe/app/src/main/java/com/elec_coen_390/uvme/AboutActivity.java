package com.elec_coen_390.uvme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.elec_coen_390.uvme.R.layout.activity_about;

public class AboutActivity extends AppCompatActivity {

    Button buttonOpenURL;
    TextView textViewQ1;
    TextView textViewA1;

    // New stuff
    ExpandableListViewAdapter listViewAdapter;
    ExpandableListView expandableListView;
    List<String> questionList;
    HashMap<String, List<String>> answerList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(activity_about);


        // About Page
        expandableListView = findViewById(R.id.eListView);
        showList();

        listViewAdapter = new ExpandableListViewAdapter(this, questionList, answerList);
        expandableListView.setAdapter(listViewAdapter);


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

    private void showList() {

        questionList = new ArrayList<String>();
        answerList = new HashMap<String, List<String>>();

        questionList.add("1. How does UVMe work?");
        questionList.add("2. The sun has not set but the app says there's no/little UV, is something wrong??");
        questionList.add("3. It's the middle of the day in winter and the sun is shining strong. UVMe says I'm safe in the sun. What gives?");
        questionList.add("4. Does UVMe work on Apple Devices?");
        questionList.add("5. What percentage of UV light is blocked out by glass?");
        questionList.add("6. Would you please tell and guide me as to how to calculate the UV dose for air pollution treatment?");
        questionList.add("7. What causes absorption in the UV region?");
        questionList.add("8. What is the difference between a black-light and a blue-light?");
        questionList.add("9. When Ultraviolet (UV) radiation is used to treat water, does the water become radioactive?");
        questionList.add("10. In disinfecting water, does the temperature of the water and hence the lamp affect the UV output intensity? Is there an optimum operating temperature for the UV lamp and maximums and minimums thresholds?");
        questionList.add("11. Is it possible for the sun rays to be a danger to the skin even in cloudy weather?");
        questionList.add("12. Would the atmosphere become transparent to UV-C without an ozone layer?");
        questionList.add("13. We keep seeing the terms ‘watts’ and ‘joules’ in descriptions of how much UV is required to disinfect something. What do these terms mean? Are they the same? Why are they important?");

        List <String> answer1 = new ArrayList<>();
        answer1.add("UVMe is an android app that helps you stay safe in the sun by providing you real time UV intensity through a UV sensor. It uses forecast models that power many weather services. UVMe is very easy to understand and use wherever you are in the world.\n" +
                "Moreover, UVMe helps you understand the effect of UV on your skin. For instance, to most people, a UV index of 8 doesn't mean much. Understanding that you could burn in as little as 15 minutes is a lot more practical in terms of knowing the precautions you need to take to protect yourself.\n");


        List <String> answer2 = new ArrayList<>();
        answer2.add("Sunset can occur much later into the evening during summer, particularly in countries close to the poles. However, the amount of UV close to sunset is a lot lower than noon, despite there still being plenty of visible light. The reason is when the sun is close to the horizon, light has to travel through a lot more atmosphere to reach the ground (compared to when it is directly above). By the time it reaches you, the particles in the atmosphere would have scattered most of the UV radiation. This concept actually makes dawn and dusk perfect times to enjoy the sun. There's sunlight, yet you are still safe from getting sunburnt!");

        List <String> answer3 = new ArrayList<>();
        answer3.add("During winter, the sun does not \"rise\" as high up in the sky. Like dusk and dawn, the closeness to the horizon means that a lot of UV is scattered by the atmosphere, giving rise to a lower UV index.");

        List <String> answer4 = new ArrayList<>();
        answer4.add("UVMe is an android-based application and unfortunately, it is currently unavailable for iOS (Apple) users. Thank you for understanding.");


        List <String> answer5 = new ArrayList<>();
        answer5.add("Normal glass (as used in windows) is transparent to UV radiation to a wavelength of about 330 nm (UV-A). The transparency is quite high so almost all UV-A light will pass through glass. Below 330 nm (UV-B and UV-C), almost 100% is blocked by normal glass.\n");

        List <String> answer6 = new ArrayList<>();
        answer6.add("This is a very complex issue. First, you have to be able to calculate the fluence rate (irradiance) distribution within the UV reactor. This requires a sophisticated computer program. Then you must carry out a volume average of the fluence rate (irradiance) over the entire reactor. The residence time of the air in the reactor is given by: volume/(flow rate). The fluence (UV dose) is then the product of the average fluence rate and the residence time.\n" +
                "This calculated UV dose is a \"theoretical maximum\" because it assumes that the air is perfectly mixed (in a radial sense) as it passes through the reactor. This is usually not the case, so the actual fluence (UV dose) will be less than the theoretical maximum.\n" +
                "One can experimentally measure UV dose by using bio-dosimetry. Here a (non-pathogenic) microorganism is infected into the air upstream of the UV reactor and allowed to thoroughly mix in the air stream. Samples are taken of the upstream and downstream air (after mixing). These are then compared with a laboratory (determined using a collimated beam apparatus) fluence (UV dose)-response curve, where the fluences (UV doses) are accurately known.\n");

        List <String> answer7 = new ArrayList<>();
        answer7.add("Most molecules have absorption bands in the UV region. The absorption properties of a molecule are described by the \"absorbance\" (A) defined as\n" +
                "A = log[Eo/El]\n" +
                "where Eo and El are the irradiances incident and transmitted through a cell of depth l cm. The absorbance is also related to the concentration of the substance (let's call it X) by the relation\n" +
                "A = Îµ l [X]\n" +
                "where e is the \"molar absorption coefficient\" (units M-1 cm-1) and [X] is the concentration of X in molar (M) units (molar means mole/L)\n" +
                "When a molecule absorbs light, it is raised to an excited electronic state. In this excited state, it can react (either by dissociation or by reacting with another molecule) - this is called \"photochemistry\"; it can also return to the ground state either by releasing the excess energy as heat or by emitting a photon of light (this is called fluorescence).\n");

        List <String> answer8 = new ArrayList<>();
        answer8.add("A \"blacklight\" is a fluorescent light tube that emits at about 365 nm - this is just below the wavelengths that humans can see, but it is absorbed by most pigments in clothes so that they \"fluorescence\". This is the effect seen in many bars and discos.\n" +
                "I'm not sure what you mean by a \"bluelight\" - perhaps you mean \"germicidal\" low-pressure mercury lamp. They do glow \"blue\", but most of their output is at 254 nm, so DO NOT look directly at such a lamp when it is operating. These lamps are used in air and water disinfection, since the 254 nm light is absorbed by DNA in bacteria and viruses causing their inactivation.\n");

        List <String> answer9 = new ArrayList<>();
        answer9.add("You are justifiably confused about the word \"radiation\". I prefer to use the term \"UV light\" rather than \"UV radiation\" for the very reason that you are confused. Ultraviolet is \"light\" - you can't see it because our eyes are not sensitive to UV; however, it is a form of light with wavelengths beyond the \"violet\" end (hence the term \"ultraviolet\") of the rainbow spectrum.\n" +
                "Since UV is \"light\", it travels through air and water at the speed of light and when the UV source is turned off, the UV is gone. There are no \"residuals\" and the water that has been exposed to UV is the same as it was before exposure, and certainly the water is not \"radioactive\". It is like shining a bright light into a glass of water. I think you would agree that when you turn off the light, the water has not changed.\n" +
                "UV water disinfection units are designed to provide enough \"UV dose\" so that any pathogenic microorganisms in the water are rendered \"inactive\". What happens is that the UV is absorbed by the DNA in microorganisms; the DNA is damaged so that the microorganism cannot reproduce. Cells that cannot reproduce cannot cause disease. The beautiful thing about UV is that it does its job while the water is passing through the unit, but after the water has passed through, it has been \"disinfected\", but its \"water quality\" has not changed.\n");

        List <String> answer10 = new ArrayList<>();
        answer10.add("Yes, the water temperature does affect the UV output of low-pressure UV lamps (not very much for low-pressure high output or medium pressure UV lamps). The optimum operating temperature for a UV lamp operating in the open is about 40 C (104 F). At 20 C (68 F), the output drops to about 50% and to about 10% at 0 C (32 F). When encased in a quartz sleeve with water on the other side, the effects are not so large. The optimum water temperature is about 22 C (71 F) and the output drops to about 80% at 0 C (32 F).");

        List <String> answer11 = new ArrayList<>();
        answer11.add("The sun radiates a broad stream of energy to our planet. Most people think that the bright sunshine we see is the harmful stuff, but that's not quite the truth.\n" +
                "The energy that is most harmful to our skin is ultraviolet and is not visible to our eyes. It is this radiation that can damage our eyes and skin, increasing the chance for skin cancer and wrinkles. Like the visible light, clouds do absorb and scatter some of this ultraviolet radiation. But, even when it's cloudy, enough of this energy comes down to your skin to cause harm.\n" +
                "If you spend a lot of time outdoors, you should protect yourself -- even if it's cloudy.\n" +
                "Although it's not the visible light that does the damage, you can use it to determine your danger from ultraviolet radiation. A good rule of thumb is that if you can see your shadow and its length is shorter than your height, you should avoid exposure to the sun or wear sunscreen with an SPF of at least 30.\n" +
                "If your shadow is not visible, then the odds are that the clouds are thick enough to significantly reduce the amount of UV energy reaching the ground. Short-term exposure won't be harmful in this case, but when outside for a long period, sunscreen is still recommended.\n");

        List <String> answer12 = new ArrayList<>();
        answer12.add("The sun's rays with wavelengths less than 300 nm are blocked by absorption by the ozone in the stratosphere. You can download the extraterrestrial (AM 0) and the standard terrestrial (AM 1.5) spectra from the National Renewable Energy Laboratory Website (http://www.nrel.gov/rredc/). This is why there is so much concern about the reduction in ozone levels caused by ozone depleting compounds (e.g., chlorofluoro hydrocarbons from Freon gases) escaping into the atmosphere.");

        List <String> answer13 = new ArrayList<>();
        answer13.add("Most people seem to be familiar with the term ‘watts' (from light bulbs and electric bills); but probably not the term ‘joules' (a metric measurement term). In short, both are used in measuring energy in any form (e.g., electricity as well as light):\n" +
                "A Watt is a measure of the rate of energy delivery (analogous to gallons-per-minute flow rate for water delivery).\n" +
                "A Joule is a cumulative measure of the total amount of energy delivered (analogous to total gallons of water delivered).\n" +
                "It usually is associated with how much time was needed to deliver the energy.\n" +
                "The way the units work is 1 Joule (J) of energy delivered = delivering 1 Watt (W) of energy for 1 second. In the UV world, we usually measure things in small increments, i.e., thousandths of a Joule or Watt. These are shown as ‘milli-Joules' (i.e., ‘mJ’ or 1/1,000 of a Joule), and milli-Watts (i.e., ‘mW’ or 1/1,000 of a Watt).\n" +
                "Example: 40mJ (cumulative energy) = 10mW delivered for 4 seconds\n");

        answerList.put(questionList.get(0), answer1);
        answerList.put(questionList.get(1), answer2);
        answerList.put(questionList.get(2), answer3);
        answerList.put(questionList.get(3), answer4);
        answerList.put(questionList.get(4), answer5);
        answerList.put(questionList.get(5), answer6);
        answerList.put(questionList.get(6), answer7);
        answerList.put(questionList.get(7), answer8);
        answerList.put(questionList.get(8), answer9);
        answerList.put(questionList.get(9), answer10);
        answerList.put(questionList.get(10), answer11);
        answerList.put(questionList.get(11), answer12);
        answerList.put(questionList.get(12), answer13);


    }
}

/*
OLD STUFF:


 <TextView
        android:id="@+id/textViewAbout_Q1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:fontFamily="@font/roboto_light"
        android:textColor="@color/light_blue"
        android:paddingStart="15dp"
        android:layout_below="@+id/textViewAbout_FAQ"
        android:text="@string/q_1"/>

    <TextView
        android:id="@+id/textViewAbout_A1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:fontFamily="@font/roboto_thin"
        android:textColor="@color/light_blue"
        android:paddingStart="15dp"
        android:paddingEnd="15dp"
        android:layout_below="@+id/textViewAbout_Q1"
        android:text="@string/ans_1"
        android:visibility="visible"/>

    <TextView
        android:id="@+id/textViewAbout_Q2"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:fontFamily="@font/roboto_light"
        android:textColor="@color/light_blue"
        android:paddingStart="15dp"
        android:layout_below="@+id/textViewAbout_A1"
        android:text="@string/q_2"/>

    <TextView
        android:id="@+id/textViewAbout_A2"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:fontFamily="@font/roboto_thin"
        android:textColor="@color/light_blue"
        android:paddingStart="15dp"
        android:paddingEnd="15dp"
        android:layout_below="@+id/textViewAbout_Q2"
        android:text="@string/ans_2"
        android:visibility="visible"/>

    <TextView
        android:id="@+id/textViewAbout_Q3"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:fontFamily="@font/roboto_light"
        android:textColor="@color/light_blue"
        android:paddingStart="15dp"
        android:layout_below="@+id/textViewAbout_A2"
        android:text="@string/q_3"/>

    <TextView
        android:id="@+id/textViewAbout_A3"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:fontFamily="@font/roboto_thin"
        android:textColor="@color/light_blue"
        android:paddingStart="15dp"
        android:paddingEnd="15dp"
        android:layout_below="@+id/textViewAbout_Q3"
        android:text="@string/ans_3"
        android:visibility="visible"/>

    <TextView
        android:id="@+id/textViewAbout_Q4"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:fontFamily="@font/roboto_light"
        android:textColor="@color/light_blue"
        android:paddingStart="15dp"
        android:layout_below="@+id/textViewAbout_A3"
        android:text="@string/q_4"/>

    <TextView
        android:id="@+id/textViewAbout_A4"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:fontFamily="@font/roboto_thin"
        android:textColor="@color/light_blue"
        android:paddingStart="15dp"
        android:paddingEnd="15dp"
        android:layout_below="@+id/textViewAbout_Q4"
        android:text="@string/ans_4"
        android:visibility="visible"/>

    <TextView
        android:id="@+id/textViewAbout_Q5"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:fontFamily="@font/roboto_light"
        android:textColor="@color/light_blue"
        android:paddingStart="15dp"
        android:layout_below="@+id/textViewAbout_A4"
        android:text="@string/q_5"/>

    <TextView
        android:id="@+id/textViewAbout_A5"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:fontFamily="@font/roboto_thin"
        android:textColor="@color/light_blue"
        android:paddingStart="15dp"
        android:paddingEnd="15dp"
        android:layout_below="@+id/textViewAbout_Q5"
        android:text="@string/ans_5"
        android:visibility="visible"/>

    <TextView
        android:id="@+id/textViewAbout_Q6"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:fontFamily="@font/roboto_light"
        android:textColor="@color/light_blue"
        android:paddingStart="15dp"
        android:layout_below="@+id/textViewAbout_A5"
        android:text="@string/q_6"/>


    <TextView
        android:id="@+id/textViewAbout_A6"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:fontFamily="@font/roboto_thin"
        android:textColor="@color/light_blue"
        android:paddingStart="15dp"
        android:paddingEnd="15dp"
        android:layout_below="@+id/textViewAbout_Q6"
        android:text="@string/ans_6"
        android:visibility="visible"/>

    <TextView
        android:id="@+id/textViewAbout_Q7"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:fontFamily="@font/roboto_light"
        android:textColor="@color/light_blue"
        android:paddingStart="15dp"
        android:layout_below="@+id/textViewAbout_A6"
        android:text="@string/q_7"/>

    <TextView
        android:id="@+id/textViewAbout_A7"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:fontFamily="@font/roboto_thin"
        android:textColor="@color/light_blue"
        android:paddingStart="15dp"
        android:paddingEnd="15dp"
        android:layout_below="@+id/textViewAbout_Q7"
        android:text="@string/ans_7"
        android:visibility="visible"/>

    <TextView
        android:id="@+id/textViewAbout_Q8"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:fontFamily="@font/roboto_light"
        android:textColor="@color/light_blue"
        android:paddingStart="15dp"
        android:layout_below="@+id/textViewAbout_A7"
        android:text="@string/q_8"/>

    <TextView
        android:id="@+id/textViewAbout_A8"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:fontFamily="@font/roboto_thin"
        android:textColor="@color/light_blue"
        android:paddingStart="15dp"
        android:paddingEnd="15dp"
        android:layout_below="@+id/textViewAbout_Q8"
        android:text="@string/ans_8"
        android:visibility="visible"/>


    <TextView
        android:id="@+id/textViewAbout_Q9"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:fontFamily="@font/roboto_light"
        android:textColor="@color/light_blue"
        android:paddingStart="15dp"
        android:layout_below="@+id/textViewAbout_A8"
        android:text="@string/q_9"/>

    <TextView
        android:id="@+id/textViewAbout_A9"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:fontFamily="@font/roboto_thin"
        android:textColor="@color/light_blue"
        android:paddingStart="15dp"
        android:paddingEnd="15dp"
        android:layout_below="@+id/textViewAbout_Q9"
        android:text="@string/ans_9"
        android:visibility="visible"/>

    <TextView
        android:id="@+id/textViewAbout_Q10"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:fontFamily="@font/roboto_light"
        android:textColor="@color/light_blue"
        android:paddingStart="15dp"
        android:layout_below="@+id/textViewAbout_A9"
        android:text="@string/q_10"/>

    <TextView
        android:id="@+id/textViewAbout_A10"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:fontFamily="@font/roboto_thin"
        android:textColor="@color/light_blue"
        android:paddingStart="15dp"
        android:paddingEnd="15dp"
        android:layout_below="@+id/textViewAbout_Q10"
        android:text="@string/ans_10"
        android:visibility="visible"/>

    <TextView
        android:id="@+id/textViewAbout_Q11"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:fontFamily="@font/roboto_light"
        android:textColor="@color/light_blue"
        android:paddingStart="15dp"
        android:layout_below="@+id/textViewAbout_A10"
        android:text="@string/q_11"/>

    <TextView
        android:id="@+id/textViewAbout_A11"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:fontFamily="@font/roboto_thin"
        android:textColor="@color/light_blue"
        android:paddingStart="15dp"
        android:paddingEnd="15dp"
        android:layout_below="@+id/textViewAbout_Q11"
        android:text="@string/ans_11"
        android:visibility="visible"/>

    <TextView
        android:id="@+id/textViewAbout_Q12"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:fontFamily="@font/roboto_light"
        android:textColor="@color/light_blue"
        android:paddingStart="15dp"
        android:layout_below="@+id/textViewAbout_A11"
        android:text="@string/q_12"/>

    <TextView
        android:id="@+id/textViewAbout_A12"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:fontFamily="@font/roboto_thin"
        android:textColor="@color/light_blue"
        android:paddingStart="15dp"
        android:paddingEnd="15dp"
        android:layout_below="@+id/textViewAbout_Q12"
        android:text="@string/ans_12"
        android:visibility="visible"/>

    <TextView
        android:id="@+id/textViewAbout_Q13"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:fontFamily="@font/roboto_light"
        android:textColor="@color/light_blue"
        android:paddingStart="15dp"
        android:layout_below="@+id/textViewAbout_A12"
        android:text="@string/q_13"/>

    <TextView
        android:id="@+id/textViewAbout_A13"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:fontFamily="@font/roboto_thin"
        android:textColor="@color/light_blue"
        android:paddingStart="15dp"
        android:paddingEnd="15dp"
        android:layout_below="@+id/textViewAbout_Q13"
        android:text="@string/ans_13"
        android:visibility="visible"/>

 <TextView
        android:id="@+id/textViewAbout_AboutUVMe"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:fontFamily="@font/roboto_light"
        android:textColor="@color/light_blue"
        android:paddingStart="15dp"
        android:layout_below="@+id/textViewAbout_A13"
        android:textSize="36sp"
        android:text="About UVMe"/>


    <TextView
        android:id="@+id/textViewAbout_AboutUVMe_Info1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:fontFamily="@font/roboto_light"
        android:textColor="@color/light_blue"
        android:paddingStart="15dp"
        android:layout_below="@+id/textViewAbout_AboutUVMe"
        android:text="@string/AboutUVMe1"/>

    <TextView
        android:id="@+id/textViewAbout_AboutUVMe_Info2"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:fontFamily="@font/roboto_light"
        android:textColor="@color/light_blue"
        android:paddingStart="15dp"
        android:paddingEnd="15dp"
        android:layout_below="@+id/textViewAbout_AboutUVMe_Info1"
        android:text="@string/AboutUVMe"/>

    <TextView
        android:id="@+id/textViewAbout_ContactUs"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:fontFamily="@font/roboto_light"
        android:textColor="@color/light_blue"
        android:paddingStart="15dp"
        android:layout_below="@+id/textViewAbout_AboutUVMe_Info2"
        android:textSize="36sp"
        android:text="Contact Us"/>

    <TextView
        android:id="@+id/textViewAbout_AboutUVMe_ContactInfo"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:fontFamily="@font/roboto_light"
        android:textColor="@color/light_blue"
        android:paddingStart="15dp"
        android:layout_below="@+id/textViewAbout_ContactUs"
        android:text="@string/ContactUs"/>






 */
