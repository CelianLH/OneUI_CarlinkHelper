package com.baidu.BaiduMap.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.baidu.BaiduMap.Bean.AppInfo;
import com.baidu.BaiduMap.ConstantData.CarLinkData;

import java.util.ArrayList;
import java.util.List;

public class ApplicationUtil {
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

    public static List<ApplicationInfo> getALl(Context context){
        final PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent,0);

        List<ApplicationInfo> apps = new ArrayList<>();
        for (ResolveInfo resolveInfo : resolveInfoList) {
            ApplicationInfo appInfo = resolveInfo.activityInfo.applicationInfo;
            apps.add(appInfo);
        }

        return apps;

    }

    public static List<ApplicationInfo> getSystem(Context context){

        List<ApplicationInfo> apps = getALl(context);
        for (ApplicationInfo applicationInfo : apps) {
            if ((applicationInfo.flags & (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP
                    | ApplicationInfo.FLAG_SYSTEM)) != 0 ) {
                apps.add(applicationInfo);
            }
        }
        return apps;

    }

    public static List<ApplicationInfo> getUser(Context context){

        List<ApplicationInfo> apps = getALl(context);
        for (ApplicationInfo applicationInfo : apps) {
            if ((applicationInfo.flags & (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP
                    | ApplicationInfo.FLAG_SYSTEM)) == 0 ) {
                apps.add(applicationInfo);
            }
        }
        return apps;

    }

    public static List<ApplicationInfo> getMusic(Context context){

        List<ApplicationInfo> allApps = getALl(context);
        List<ApplicationInfo> audioApps = getAudio(context);
        List<ApplicationInfo> musicApps = new ArrayList<>();

        for (ApplicationInfo applicationInfo : allApps) {
            for(ApplicationInfo applicationInfo1 : audioApps){
                if (applicationInfo.packageName.equals(applicationInfo1.packageName)){
                    musicApps.add(applicationInfo);
                    break;
                }
            }
        }
        return musicApps;

    }
    public static String getName(Context context,String pkgName){
        String name;
        PackageManager pm = context.getPackageManager();
        try {
            name = pm.getApplicationLabel(pm.getApplicationInfo(pkgName, PackageManager.GET_META_DATA)).toString();
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            name = "";
        }
        return name;

    }

    public static ArrayList<AppInfo> getLauncherInfoList(Context context){
        PackageManager pm = context.getPackageManager();
        ArrayList<AppInfo> appInfoArrayList = new ArrayList<>();
        ArrayList<String> stringArrayList = CarLinkData.getArrayList(context,CarLinkData.sp_launcher_apps);
        for (String string: stringArrayList){
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

    public static List<ApplicationInfo> getAudio(Context context){
        final PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent();
        intent.setDataAndType(Uri.parse("file://"),"audio/mp3");

        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent,0);

        List<ApplicationInfo> apps = new ArrayList<>();
        for (ResolveInfo resolveInfo : resolveInfoList) {
            ApplicationInfo appInfo = resolveInfo.activityInfo.applicationInfo;
            apps.add(appInfo);
        }

        return apps;

    }
}
