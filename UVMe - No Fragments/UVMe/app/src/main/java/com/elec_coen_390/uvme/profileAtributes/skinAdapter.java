package com.elec_coen_390.uvme.profileAtributes;
// class used to follow MVC structure
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.elec_coen_390.uvme.R;

import java.util.ArrayList;

public class skinAdapter extends ArrayAdapter<skinTone> {
    public skinAdapter(@NonNull Context context, ArrayList<skinTone> skinTones) {
        super(context, 0,skinTones);
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
            convertView=LayoutInflater.from(getContext()).inflate(R.layout.skin_tone_row,parent,false);
        }

        ImageView skintoneImage= convertView.findViewById(R.id.skinToneIcon);
        TextView skinText =convertView.findViewById(R.id.skinAtribute);
        skinTone skin = getItem(position);
        if (skin!=null) {
            skintoneImage.setImageResource(skin.getSkinIcon());
            skinText.setText(skin.getSkinColor());
        }
        return convertView;
    }
}