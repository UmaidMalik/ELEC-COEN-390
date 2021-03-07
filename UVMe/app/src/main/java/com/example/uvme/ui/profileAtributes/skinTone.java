package com.example.uvme.ui.profileAtributes;
public class skinTone {
    private final String mskinTone;
    private final int mSkinIcon;
    public skinTone(String skinTone,int SkinIcon ){
        mskinTone=skinTone;
        mSkinIcon=SkinIcon;
    }
    public String getSkinColor(){
        return mskinTone;
    }
    public int getSkinIcon(){
        return mSkinIcon;
    }


}
