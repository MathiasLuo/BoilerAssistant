package me.mathiasluo.utils;


import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Locale;

import me.mathiasluo.APP;

/**
 * Created by mathiasluo on 16-3-30.
 */
public class LanguageUtil {
    public final static int LANGUAGE_ENGLISH = 0;
    public final static int LANGUAGE_CHINESE = 1;

    public static final void changeLanguage(int language) {
        Resources resource = APP.getInstance().getResources();
        Configuration configuration = resource.getConfiguration();
        DisplayMetrics metrics = resource.getDisplayMetrics();
        switch (language) {
            case LANGUAGE_CHINESE:
                configuration.locale = Locale.CHINA;
                Log.e("==========>>>>>>>>", "修改成汉语LANGUAGE_CHINESE了");
                break;
            case LANGUAGE_ENGLISH:
                configuration.locale = Locale.ENGLISH;
                break;
            default:
                configuration.locale = Locale.getDefault();
                break;
        }
        APP.getInstance().getResources().updateConfiguration(configuration, metrics);
    }

    public static final void changeLanguage(String language) {
        Log.e("==========>>>>>>>>", language);
        switch (language) {
            case "English":
                changeLanguage(LANGUAGE_ENGLISH);
                break;
            case "汉语":
                changeLanguage(LANGUAGE_CHINESE);
                Log.e("==========>>>>>>>>", "修改成汉语了");
                break;
            default:
                changeLanguage(LANGUAGE_ENGLISH);
                break;
        }
    }
}
