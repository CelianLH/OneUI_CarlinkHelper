package com.baidu.BaiduMap.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.baidu.BaiduMap.Bean.AppInfo;
import com.baidu.BaiduMap.ConstantData.CarLinkData;

import java.util.ArrayList;
import java.util.List;

public class ApplicationUtil {

    public static void initAppList(Context context) {
        ArrayList<String> musicList = new ArrayList<>() {{
            add("com.sec.android.app.music");
            add("com.piyush.music");
            add("com.netease.cloudmusic");
            add("com.tencent.qqmusic");
            add("cn.kuwo.player");
            add("com.banqu.samsung.music");
            add("com.android.settings");
        }};
        if (CarLinkData.getArrayList(context, CarLinkData.sp_dock_music_pkg).size() == 0) {
            for (String pkgName : musicList) {
                if (ApplicationUtil.isPackageInstalled(context, pkgName)) {
                    CarLinkData.putArrayList(context, CarLinkData.sp_dock_music_pkg, new ArrayList<>() {{
                        add(pkgName);
                    }});
                    break;
                }
            }
        }

        ArrayList<String> mapList = new ArrayList<>() {{
            add("com.autonavi.minimap");
            add("com.autonavi.amapauto");
            add("com.tencent.map");
            add("com.android.settings");
        }};
        if (CarLinkData.getArrayList(context, CarLinkData.sp_dock_map_pkg).size() == 0) {
            for (String pkgName : mapList) {
                if (ApplicationUtil.isPackageInstalled(context, pkgName)) {
                    CarLinkData.putArrayList(context, CarLinkData.sp_dock_map_pkg, new ArrayList<>() {{
                        add(pkgName);
                    }});
                    break;
                }
            }
        }

        ArrayList<String> anyList = new ArrayList<>() {{
            add("com.samsung.android.dialer");
            add("com.samsung.android.messaging");
            add("com.sec.android.daemonapp");
            add("com.android.settings");
        }};
        if (CarLinkData.getArrayList(context, CarLinkData.sp_dock_any_pkg).size() == 0) {
            for (String pkgName : anyList) {
                if (ApplicationUtil.isPackageInstalled(context, pkgName)) {
                    CarLinkData.putArrayList(context, CarLinkData.sp_dock_any_pkg, new ArrayList<>() {{
                        add(pkgName);
                    }});
                    break;
                }
            }
        }

        ArrayList<String> launcherList = new ArrayList<>() {{
            add("com.sec.android.app.music");
            add("com.netease.cloudmusic");
            add("com.samsung.android.messaging");
            add("com.sec.android.daemonapp");
            add("apps.r.compass");
            add("tv.danmaku.bili");
            add("com.ss.android.ugc.aweme");
            add("mark.via");
            add("com.android.settings");
        }};
        if (CarLinkData.getArrayList(context, CarLinkData.sp_launcher_apps).size() == 0) {
            ArrayList<String> favList = new ArrayList<>();
            for (String pkgName : launcherList) {
                if (ApplicationUtil.isPackageInstalled(context, pkgName)) {
                    favList.add(pkgName);
                }
            }
            CarLinkData.putArrayList(context, CarLinkData.sp_launcher_apps, favList);
        }

    }

    public static void initSettings(Context context){
        if(!CarLinkData.getBoolean(context,"Initialized",false)){
            CarLinkData.putBoolean(context,CarLinkData.sp_enable_overlay,true);
            CarLinkData.putBoolean(context,CarLinkData.sp_enable_overlay_right,true);
            CarLinkData.putBoolean(context,CarLinkData.sp_overlay_left_hide_visibility,false);
            CarLinkData.putBoolean(context,CarLinkData.sp_overlay_left_dock_corner_always,false);
            CarLinkData.putBoolean(context,CarLinkData.sp_dock_music,true);
            CarLinkData.putBoolean(context,CarLinkData.sp_dock_map,true);
            CarLinkData.putBoolean(context,CarLinkData.sp_dock_any,true);
            CarLinkData.putBoolean(context,CarLinkData.sp_dock_bixby,true);
            CarLinkData.putBoolean(context,CarLinkData.sp_launcher_name,true);
            CarLinkData.putBoolean(context,CarLinkData.sp_theme_enable_pic_dock,true);
            CarLinkData.putBoolean(context,CarLinkData.sp_theme_enable_pic_launcher,true);
            CarLinkData.putBoolean(context,CarLinkData.sp_theme_navi_color_use_default,true);
            CarLinkData.putString(context,CarLinkData.sp_overlay_right_width,"480");
            CarLinkData.putString(context,CarLinkData.sp_overlay_left_width,"160");
            CarLinkData.putString(context,CarLinkData.sp_launcher_icon_size,"120");
            CarLinkData.putString(context,CarLinkData.sp_theme_dock_pic,"3");
            CarLinkData.putString(context,CarLinkData.sp_theme_launcher_pic,"3");
            CarLinkData.putString(context,CarLinkData.sp_theme_launcher_bg_pic,"3");
            CarLinkData.putInt(context,CarLinkData.sp_launcher_colum,5);
            CarLinkData.putBoolean(context,"Initialized",true);
        }
    }
    public static boolean isPackageInstalled(Context c, String pn) {
        PackageManager packageManager = c.getPackageManager();
        try {
            @SuppressLint("WrongConstant") PackageInfo pi = packageManager.getPackageInfo(pn, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
            if (null != pi) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            //e.printStackTrace();
            return false;
        }
        return false;
    }

    public static List<ApplicationInfo> getInstalledApplications(Context context, String intentType, String categoryType) {
        final PackageManager packageManager = context.getPackageManager();
        Intent intent = intentType == null ? new Intent() : new Intent(intentType);
        if (categoryType != null) {
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
        }
        if (categoryType == Intent.CATEGORY_LAUNCHER) {
            intent.setAction(Intent.ACTION_MAIN);
        }
        //intent.setData(Uri.parse(type));

        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        List<ApplicationInfo> apps = new ArrayList<>();
        for (ResolveInfo resolveInfo : resolveInfoList) {
            ApplicationInfo appInfo = resolveInfo.activityInfo.applicationInfo;
            apps.add(appInfo);
        }

        return apps;
    }

    public static List<ApplicationInfo> getALl(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 0);

        List<ApplicationInfo> apps = new ArrayList<>();
        for (ResolveInfo resolveInfo : resolveInfoList) {
            ApplicationInfo appInfo = resolveInfo.activityInfo.applicationInfo;
            apps.add(appInfo);
        }

        return apps;

    }

    public static List<ApplicationInfo> getSystem(Context context) {

        List<ApplicationInfo> apps = getALl(context);
        for (ApplicationInfo applicationInfo : apps) {
            if ((applicationInfo.flags & (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP
                    | ApplicationInfo.FLAG_SYSTEM)) != 0) {
                apps.add(applicationInfo);
            }
        }
        return apps;

    }

    public static List<ApplicationInfo> getUser(Context context) {

        List<ApplicationInfo> apps = getALl(context);
        for (ApplicationInfo applicationInfo : apps) {
            if ((applicationInfo.flags & (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP
                    | ApplicationInfo.FLAG_SYSTEM)) == 0) {
                apps.add(applicationInfo);
            }
        }
        return apps;

    }

    public static List<ApplicationInfo> getMusic(Context context) {

        List<ApplicationInfo> allApps = getALl(context);
        List<ApplicationInfo> audioApps = getAudio(context);
        List<ApplicationInfo> musicApps = new ArrayList<>();

        for (ApplicationInfo applicationInfo : allApps) {
            for (ApplicationInfo applicationInfo1 : audioApps) {
                if (applicationInfo.packageName.equals(applicationInfo1.packageName)) {
                    musicApps.add(applicationInfo);
                    break;
                }
            }
        }
        return musicApps;

    }

    public static String getName(Context context, String pkgName) {
        String name;
        PackageManager pm = context.getPackageManager();
        try {
            name = pm.getApplicationLabel(pm.getApplicationInfo(pkgName, PackageManager.GET_META_DATA)).toString();
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            name = "";
        }
        return name;

    }

    public static ArrayList<AppInfo> getLauncherInfoList(Context context) {
        PackageManager pm = context.getPackageManager();
        ArrayList<AppInfo> appInfoArrayList = new ArrayList<>();
        ArrayList<String> stringArrayList = CarLinkData.getArrayList(context, CarLinkData.sp_launcher_apps);
        for (String string : stringArrayList) {
            try {
                AppInfo appInfo = new AppInfo();
                ApplicationInfo temp_info = pm.getApplicationInfo(string, 0);
                appInfo.label = temp_info.loadLabel(pm);
                appInfo.packageName = temp_info.packageName;
                appInfo.icon = temp_info.loadIcon(pm);
                appInfoArrayList.add(appInfo);
            } catch (Exception e) {
            }
        }
        return appInfoArrayList;
    }

    public static List<ApplicationInfo> getAudio(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent();
        intent.setDataAndType(Uri.parse("file://"), "audio/mp3");

        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 0);

        List<ApplicationInfo> apps = new ArrayList<>();
        for (ResolveInfo resolveInfo : resolveInfoList) {
            ApplicationInfo appInfo = resolveInfo.activityInfo.applicationInfo;
            apps.add(appInfo);
        }

        return apps;

    }
}
