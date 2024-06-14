package com.baidu.BaiduMap.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class PreferenceUtil {
    private static String mKey;
    private static SharedPreferences mSharedPreferences;
    PreferenceUtil(Context context,String key){
        mKey = key;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }
    public static boolean getBoolean(){
        return mSharedPreferences.getBoolean(mKey,false);

    }

    public static String getString(){
        return mSharedPreferences.getString(mKey,null);
    }

    public static int getInt(){
        return mSharedPreferences.getInt(mKey,-1);
    }
}
