package com.elec_coen_390.uvme.profileAtributes;

public class eyeColor {
    private String meyeColor;
    private int mEyeIcon;

    public eyeColor(String eyeColor,int EyeIcon ){
        meyeColor=eyeColor;
        mEyeIcon=EyeIcon;
    }
    public String getEyeColor(){
        return meyeColor;
    }
    public int getEyeIcon(){
        return mEyeIcon;
    }

}