package com.example.Dairy_App.SharedPreferences;

import android.content.Context;

public class DataLocalManager {
    private static final String PREF_IS_CHECKED = "PREF_IS_CHECKED";
    private static final String PREF_PASSCODE = "PREF_PASSCODE";
    private static final String PREF_LOCK_SCREEN_CHECK = "PREF_LOCK_SCREEN_CHECK";
    private static final String PREF_THEME = "PREF_THEME";
    private static DataLocalManager instance;
    private MySharedPreferences mySharedPreferences;

    public static void init(Context context){
        instance = new DataLocalManager();
        instance.mySharedPreferences = new MySharedPreferences(context);
    }

    public static DataLocalManager getInstance(){
        if(instance == null){
            instance = new DataLocalManager();
        }
        return instance;
    }

    public static void setCheckSwitch(int isCheck){
        DataLocalManager.getInstance().mySharedPreferences.putIntValue(PREF_IS_CHECKED,isCheck);
    }

    public static int getCheckSwitch(){
        return DataLocalManager.getInstance().mySharedPreferences.getIntValue(PREF_IS_CHECKED);
    }

    public static void setPasscode(String passcode){
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(PREF_PASSCODE,passcode);
    }

    public static String getPasscode(){
        return DataLocalManager.getInstance().mySharedPreferences.getStringValue(PREF_PASSCODE);
    }

    public static void setCheckLockScreen(boolean isCheckScreen){
        DataLocalManager.getInstance().mySharedPreferences.putBooleanValue(PREF_LOCK_SCREEN_CHECK,isCheckScreen);
    }

    public static boolean getCheckLockScreen(){
        return DataLocalManager.getInstance().mySharedPreferences.getBooleanValue(PREF_LOCK_SCREEN_CHECK);
    }

    public static void setTheme(String theme){
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(PREF_THEME,theme);
    }

    public static String getTheme(){
        return DataLocalManager.getInstance().mySharedPreferences.getStringValue(PREF_THEME);
    }
}
