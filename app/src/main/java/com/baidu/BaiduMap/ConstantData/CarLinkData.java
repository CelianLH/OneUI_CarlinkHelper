package com.baidu.BaiduMap.ConstantData;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public final class CarLinkData {
    public static final String sp_enable_overlay = "enable_overlay";
    public static final String sp_enable_overlay_right = "enable_overlay_right";
    public static final String sp_overlay_right_width = "overlay_right_width";
    public static final String sp_overlay_left_width = "overlay_left_width";
    public static final String sp_overlay_left_hide_visibility = "overlay_left_hide_visibility";
    public static final String sp_overlay_left_dock_corner_always = "overlay_left_dock_corner_always";
    public static final String isSimulateStartEnabled = "isSimulateStartEnabled";
    public static final String sp_dock_music = "dock_music";
    public static final String sp_dock_music_pkg = "dock_music_pkg";
    public static final String sp_dock_map = "dock_map";
    public static final String sp_dock_map_pkg = "dock_map_pkg";
    public static final String sp_dock_any = "dock_any";
    public static final String sp_dock_any_pkg = "dock_any_pkg";
    public static final String sp_dock_bixby = "dock_bixby";
    public static final String sp_dock_home = "dock_home";
    public static final String pkgNameAmap = "com.autonavi.minimap";
    public static final String pkgNameAmapAuto = "com.autonavi.amapauto";
    public static final String sp_launcher_apps = "launcher_apps";
    public static final String sp_launcher_colum = "launcher_colum";
    public static final String sp_launcher_name = "launcher_name";
    public static final String sp_launcher_icon_size = "launcher_icon_size";
    public static final String sp_theme_dock_color = "theme_dock_color";
    public static final String sp_theme_dock_color_use_default = "theme_dock_color_use_default";
    public static final String sp_theme_enable_pic_dock = "theme_enable_pic_dock";
    public static final String sp_theme_dock_pic = "theme_dock_pic";
    public static final String sp_theme_color_launcher = "theme_color_launcher";
    public static final String sp_theme_launcher_color_use_default = "theme_launcher_color_use_default";
    public static final String sp_theme_enable_pic_launcher = "theme_enable_pic_launcher";
    public static final String sp_theme_launcher_pic = "theme_launcher_pic";
    public static final String sp_theme_launcher_bg_pic = "theme_launcher_bg_pic";
    public static final String sp_theme_color_navi = "theme_color_navi";
    public static final String sp_theme_navi_color_use_default = "theme_navi_color_use_default";
    public static final String sp_theme_enable_pic_navi = "theme_enable_pic_navi";
    public static final String sp_theme_navi_pic = "theme_navi_pic";
    public static Boolean isOverlayEnabled = true;
    public static Boolean isOverlayRightEnabled = true;
    public static Boolean enabled_dock_map = true;
    public static Boolean enabled_dock_music = true;
    public static Boolean enabled_dock_any = true;
    public static Boolean enabled_dock_bixby = true;
    public static Boolean enabled_app_home = true;
    public static String pkgName_dock_music;
    public static String pkgName_dock_map;
    public static String pkgName_dock_any;
    public static int overlay_right_width = 480;
    public static int overlay_left_width = 160;
    public static int dock_home_mode = 0;
    public static int appLauncherColum = 5;
    public static int appLauncherIconSize = 120;
    public static Boolean theme_enable_pic_dock = true;
    public static Boolean theme_enable_pic_launcher = true;
    public static int theme_dock_color;
    public static int theme_color_launcher;
    public static String theme_dock_pic;
    public static String theme_launcher_pic;
    public static ArrayList<String> arrayListApp = null;

    public void init(Context context) {
        isOverlayEnabled = getBoolean(context, sp_enable_overlay, true);
        isOverlayRightEnabled = getBoolean(context, sp_enable_overlay_right, true);
        enabled_dock_music = getBoolean(context, sp_dock_music, true);
        enabled_dock_map = getBoolean(context, sp_dock_map, true);
        enabled_dock_any = getBoolean(context, sp_dock_any, true);
        enabled_dock_bixby = getBoolean(context, sp_dock_bixby, true);
        enabled_app_home = getBoolean(context, sp_launcher_name, true);
        pkgName_dock_music = getString(context, sp_dock_music_pkg, "");
        pkgName_dock_map = getString(context, sp_dock_map_pkg, "");
        pkgName_dock_any = getString(context, sp_dock_any_pkg, "");
        overlay_right_width = getInt(context, sp_overlay_right_width, 480);
        overlay_left_width = getInt(context, sp_overlay_left_width, 160);
        dock_home_mode = getInt(context, sp_dock_home, 0);
        appLauncherColum = getInt(context, sp_launcher_colum, 5);
        appLauncherIconSize = getInt(context, sp_launcher_icon_size, 120);
        theme_enable_pic_dock = getBoolean(context, sp_theme_enable_pic_dock, true);
        theme_enable_pic_launcher = getBoolean(context, sp_theme_enable_pic_launcher, true);
        theme_dock_color = getInt(context, sp_theme_dock_color, 0);
        theme_color_launcher = getInt(context, sp_theme_color_launcher, 0);
        theme_dock_pic = getString(context, sp_theme_dock_pic, "0");
        theme_launcher_pic = getString(context, sp_theme_launcher_pic, "0");
        arrayListApp = getArrayList(context, sp_launcher_apps);
    }

    public static String getString(Context context, String key, String defaultVal) {
        return getSharedPreferences(context).getString(key, defaultVal);
    }

    public static boolean getBoolean(Context context, String key, boolean defaultVal) {
        return getSharedPreferences(context).getBoolean(key, defaultVal);
    }

    public static int getInt(Context context, String key, int defaultVal) {
        return getSharedPreferences(context).getInt(key, defaultVal);
    }
    public static int getIntFromString(Context context, String key, int defaultVal) {
        if(getString(context,key).equals("")){
            return defaultVal;
        }
        return Integer.parseInt(getString(context,key));
    }

    public static ArrayList<String> getArrayList(Context context, String key) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        String dataString = sharedPreferences.getString(key, "");
        ArrayList<String> stringArrayList = new ArrayList<>();
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        if (gson.fromJson(dataString, type) != null) {
            stringArrayList.addAll(gson.fromJson(dataString, type));
        }
        return stringArrayList;
    }

    public static void putArrayList(Context context, String key, ArrayList<String> list) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
    }

    public static void putBoolean(Context context, String key, boolean val) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, val);
        editor.apply();
    }

    public static void putString(Context context, String key, String val) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, val);
        editor.apply();
    }
    public static void putInt(Context context, String key, int val) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, val);
        editor.apply();
    }

    public static String getString(Context context, String key){
        return getSharedPreferences(context).getString(key,"");
    }

    public static String getStringFromList(Context context, String key){
        ArrayList<String> stringArrayList = getArrayList(context,key);
        String str = "";
        if(stringArrayList.size()!=0){
            str = stringArrayList.get(0);
        }
        return str;
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(context.getPackageName()+"_preferences", Context.MODE_PRIVATE);
    }

}
