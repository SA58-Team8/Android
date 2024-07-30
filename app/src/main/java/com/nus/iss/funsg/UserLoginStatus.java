package com.nus.iss.funsg;

import android.content.Context;
import android.content.SharedPreferences;

public class UserLoginStatus {
    //this class is used to store login info,to identify user and get preview


    private static final String PREFS_NAME = "UserLoginStatusPrefs";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_IS_PREVIEW = "is_preview";
    public static void saveLoginStatus(Context context, boolean isLoggedIn){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }
    public static boolean isLoggedIn(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }


    public static void savePreviewStatus(Context context, boolean isPreview){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_PREVIEW, isPreview);
        editor.apply();
    }
    public static boolean isPreview(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_IS_PREVIEW, false);
    }
}
