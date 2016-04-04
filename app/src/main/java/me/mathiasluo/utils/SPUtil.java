package me.mathiasluo.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by MathiasLuo on 2016/3/5.
 */
public class SPUtil {

    private final static String DATA = "MUSIC_PATH";
    public final static String NOACESSTOKEN = "0001";

    public final static void putMusicPath(Context context, String musicPath) {
        SharedPreferences share = context.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.putString(DATA, musicPath);
        editor.commit();
    }

    public final static String getMusicPath(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(DATA, NOACESSTOKEN);
        return accessToken;
    }

    public final static void removeMusicPath(Context context) {
        SharedPreferences share = context.getSharedPreferences(DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit();
        editor.putString(DATA, NOACESSTOKEN);
        editor.commit();
    }
}
