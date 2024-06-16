package com.baidu.BaiduMap.carlifeapplauncher.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Outline;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;

import com.baidu.BaiduMap.Bean.AppInfo;
import com.baidu.BaiduMap.ConstantData.CarLinkData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.preference.PreferenceManager;

public class Common {

    public static AppInfo loadAppInfo(Context context, String pkg) {
        PackageManager pm = context.getPackageManager();
        AppInfo app = new AppInfo();
        try {
            ApplicationInfo temp_info = pm.getApplicationInfo(pkg, 0);
            app.label = temp_info.loadLabel(pm);
            app.packageName = temp_info.packageName;
            app.icon = temp_info.loadIcon(pm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return app;
    }

    public static ArrayList<String> get_favorite_list(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String set = sp.getString("favorite_apps", "[]");
        ArrayList<String> list = new ArrayList<>(new Gson().fromJson(set, new TypeToken<ArrayList<String>>() {
        }.getType()));
        ArrayList<String> result = new ArrayList<>();
        boolean lost = false;
        for (String pkg : list) {
            if (isInstalled(context, pkg)) {
                result.add(pkg);
            } else {
                lost = true;
            }
        }
        if (lost) {
            SharedPreferences.Editor ed = PreferenceManager.getDefaultSharedPreferences(context).edit();
            ed.putString("favorite_apps", new Gson().toJson(result));
            ed.apply();
        }
        return result;
    }

    public static ArrayList<String> app_list(Context context) {
        return CarLinkData.getArrayList(context,CarLinkData.sp_launcher_apps);
    }


    public static void switchLocation(Context context, int targetA, int targetB) {
        ArrayList<String> list = app_list(context);
        if (list.size() > targetA && list.size() > targetB && list.size() > 2) {
            String a = list.get(targetA);
            list.set(targetA, list.get(targetB));
            list.set(targetB, a);

            CarLinkData.putArrayList(context,CarLinkData.sp_launcher_apps,list);
        }

    }

    public static boolean isInstalled(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> installedList = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        Iterator<PackageInfo> iterator = installedList.iterator();
        PackageInfo info;
        String name;
        while (iterator.hasNext()) {
            info =  iterator.next();
            name = info.packageName;
            if (name.equals(packageName)) {
                return true;
            }
        }
        return false;
    }


    public static void setBgRadiusWithCutOut(View layoutContent, int cutout, int radius) {

        //设置圆角大小
        layoutContent.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                //设置矩形
                outline.setRoundRect(cutout, cutout, view.getWidth() - cutout, view.getHeight() - cutout, radius);
                // 可以指定圆形，矩形，圆角矩形，path
                //outline.setOval(0, 0, view.getWidth(), view.getHeight()
            }
        });
        //设置阴影
        //layoutContent.setElevation(10);
        //设置圆角裁切
        layoutContent.setClipToOutline(true);

    }

    public static void setBgRadius(View layoutContent, int radius) {
        //设置圆角大小
        layoutContent.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                //设置矩形
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), radius);
                // 可以指定圆形，矩形，圆角矩形，path
                //outline.setOval(0, 0, view.getWidth(), view.getHeight()
            }
        });
        //设置阴影
        //layoutContent.setElevation(10);
        //设置圆角裁切
        layoutContent.setClipToOutline(true);
    }



    public static void setMargin(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }
    public static String StringFromBundle(Object s)
    {
        if(s==null)
        {
            return "";
        }else
        {
            return  s.toString();
        }
    }
}
