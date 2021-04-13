package com.elec_coen_390.uvme;


public class Methods {

    public void setColorTheme(){

        switch (Constant.color){
            case 0xffF44336:
                Constant.theme = R.style.AppTheme_red;
                break;
            case 0xffE91E63:
                Constant.theme = R.style.AppTheme_pink;
                break;
            case 0xff673AB7:
                Constant.theme = R.style.AppTheme_violet;
                break;

            case 0xff4CAF50:
                Constant.theme = R.style.AppTheme_green;
                break;

        }
    }
}
