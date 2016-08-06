package cz.master.extern.babyradio.helper;


import android.graphics.Typeface;

import cz.master.extern.babyradio.settings.Utils;

public class Fonts {
    public static Typeface getUbuntuBoldTypeFace() {
        return Typeface.createFromAsset(Utils.context.getAssets(),
                "Fonts/Ubuntu-Bold.ttf");
    }

    public static Typeface getUbuntuLightTypeFace() {
        return Typeface.createFromAsset(Utils.context.getAssets(),
                "Fonts/Ubuntu-Light.ttf");
    }

    public static Typeface getUbuntuRegularTypeFace() {
        return Typeface.createFromAsset(Utils.context.getAssets(),
                "Fonts/Ubuntu-Regular.ttf");
    }
}
