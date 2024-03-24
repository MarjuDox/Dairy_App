package com.example.Dairy_App.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {
    private static final String MY_SHARE_PREFERENCES = "MY_SHARE_PREFERENCES";

    private Context mContext;

    public MySharedPreferences(Context mContext){
        this.mContext = mContext;
    }


    public void putIntValue(String key , int value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key,value);
        editor.apply();
    }

    public int getIntValue(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key,0);
    }
    public void putStringValue(String key , String value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public String getStringValue(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

    public void putBooleanValue(String key , boolean value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }

    public boolean getBooleanValue(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key,false);
    }
}
