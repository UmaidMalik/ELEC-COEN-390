package com.elec_coen_390.uvme.profileAtributes;

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
