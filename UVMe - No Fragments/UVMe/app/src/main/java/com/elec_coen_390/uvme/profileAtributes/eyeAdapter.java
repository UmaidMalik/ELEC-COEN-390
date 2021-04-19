package com.elec_coen_390.uvme.profileAtributes;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.elec_coen_390.uvme.R;

import java.util.ArrayList;
// class used to follow MVC structure

public class eyeAdapter extends ArrayAdapter<eyeColor> {
    public eyeAdapter(@NonNull Context context, ArrayList<eyeColor> eyeColors) {
        super(context, 0,eyeColors);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initview(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initview(position,convertView,parent);
    }
    // saving the position of the spinner
    private View initview(int position,View convertView,ViewGroup parent){
        if (convertView==null){
            convertView=LayoutInflater.from(getContext()).inflate(R.layout.eye_color_row,parent,false);
        }
        ImageView imageViewEyeColor= convertView.findViewById(R.id.eyeColorIcon); // used to find the eyecolor icon
        TextView eyeText =convertView.findViewById(R.id.eyeAtribute);
        eyeColor eye = getItem(position); // saves position

        if (eye!=null) {
            imageViewEyeColor.setImageResource(eye.getEyeIcon());
            eyeText.setText(eye.getEyeColor());
        }
        return convertView;
    }
}