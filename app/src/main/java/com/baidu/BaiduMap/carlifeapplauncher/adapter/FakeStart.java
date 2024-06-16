package com.baidu.BaiduMap.carlifeapplauncher.adapter;

import static com.baidu.BaiduMap.Widget.FloatView.NavBar.myHandler;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.baidu.BaiduMap.ConstantData.CarLinkData;
import com.baidu.BaiduMap.MiniMapActivity;
import com.baidu.BaiduMap.Widget.FloatView.NavBar;

public class FakeStart {

    public static void Start(Context context, String pkgName) {
        if (pkgName.equals(CarLinkData.pkgNameAmap)) {
            StartMiniMap(context);
        } else {
            StartDefault(context, pkgName);
        }

    }

    @SuppressLint("WrongConstant")
    private static void StartDefault(Context context, String pkgName) {
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(pkgName);
        if (launchIntent != null) {
            try {
                ActivityOptions ao = ActivityOptions.makeBasic();
                ao.setLaunchDisplayId(context.getDisplay().getDisplayId());
                launchIntent.addFlags(0x10104000);
                context.startActivity(launchIntent, ao.toBundle());
            } catch (Exception e) {

            }
        }
    }

    @SuppressLint("WrongConstant")
    public static void StartMiniMapToContext(Context context) {
        Intent i = new Intent();
        i.setClass(context, MiniMapActivity.class);
        ActivityOptions ao = ActivityOptions.makeBasic();
        ao.setLaunchDisplayId(context.getDisplay().getDisplayId());
        i.addFlags(0x10104000);
        context.startActivity(i, ao.toBundle());
    }

    @SuppressLint("WrongConstant")
    public static void StartMiniMapToContext2(Context context) {
        if (CarLinkData.getStringFromList(context,CarLinkData.sp_dock_map_pkg).equals(CarLinkData.pkgNameAmap)) {
            StartMiniMapBar(context);
        }
        Intent i = new Intent();
        i.setClass(context, MiniMapActivity.class);
        ActivityOptions ao = ActivityOptions.makeBasic();
        ao.setLaunchDisplayId(context.getDisplay().getDisplayId());
        i.addFlags(0x10104000);
        context.startActivity(i, ao.toBundle());
//        Timer timer = new Timer();
//        TimerTask task = new TimerTask() {
//            public void run() {
//                StartMiniMapBar(context);
//            }
//        };
//        timer.schedule(task, 500);

    }

    @SuppressLint("WrongConstant")
    public static void StartUcar(Context context, String pkgName) {

        if (pkgName.equals(CarLinkData.pkgNameAmap)) {
            StartMiniMap(context);
            return;
        }

        Intent i = new Intent("com.ucar.intent.action.UCAR", null);
        i.setPackage(pkgName);
        List<ResolveInfo> allApps = context.getPackageManager().queryIntentActivities(i, 0);
        ResolveInfo info = null;
        for (ResolveInfo ri : allApps) {
            info = ri;
            break;
        }
        if (info != null) {
            Intent launchIntent = new Intent("com.ucar.intent.action.UCAR");
            launchIntent.addFlags(0x10104000);
            launchIntent.putExtra("isUcarMode", true);
            ActivityOptions ao = ActivityOptions.makeBasic();
            ao.setLaunchDisplayId(context.getDisplay().getDisplayId());

//            launchIntent.putExtra("isVivoCarLinkMode",true);
            launchIntent.putExtra("isSamsungCarLifeMode", true);
//            launchIntent.putExtra("screenMode",0);
//            launchIntent.putExtra("displayId",context.getDisplay().getDisplayId());

            launchIntent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
            try {
                context.startActivity(launchIntent, ao.toBundle());
                //  Toast.makeText(context, "启动成功", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                //Toast.makeText(context, "启动失败，默认模式启动", Toast.LENGTH_LONG).show();
                StartDefault(context, pkgName);
            }
        } else {
            //Toast.makeText(context, "无适配，默认模式启动", Toast.LENGTH_LONG).show();
            StartDefault(context, pkgName);
        }
    }

    @SuppressLint("WrongConstant")
    public static void StartCommon(Context context,String pkg) {

        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        mActivityManager.killBackgroundProcesses(pkg);

        Intent i = new Intent(Intent.CATEGORY_LAUNCHER, null);
        i.setPackage(pkg);
        List<ResolveInfo> allApps = context.getPackageManager().queryIntentActivities(i, 0);
        ResolveInfo info = null;
        for (ResolveInfo ri : allApps) {
            info = ri;
            break;
        }
        if (info != null) {
            Intent launchIntent = new Intent(Intent.ACTION_MAIN);
            launchIntent.setFlags(807403520);
            launchIntent.setFlags(0x30200000);
            launchIntent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
            ActivityOptions ao = ActivityOptions.makeBasic();
            ao.setLaunchDisplayId(context.getDisplay().getDisplayId());
            try {
                context.startActivity(launchIntent, ao.toBundle());
                //  Toast.makeText(context, "启动成功", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                //Toast.makeText(context, "启动失败，默认模式启动", Toast.LENGTH_LONG).show();
                StartDefault(context, pkg);
            }
        } else {
            //Toast.makeText(context, "无适配，默认模式启动", Toast.LENGTH_LONG).show();
            StartDefault(context, pkg);
        }
    }

    @SuppressLint("WrongConstant")
    public static void StartMiniMap(Context context) {


        String pkgName = CarLinkData.pkgNameAmap;

//        i(context,pkgName);

        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        mActivityManager.killBackgroundProcesses(pkgName);

        Intent i = new Intent("com.ucar.intent.action.UCAR", null);
        i.setPackage(pkgName);
        List<ResolveInfo> allApps = context.getPackageManager().queryIntentActivities(i, 0);
        ResolveInfo info = null;
        for (ResolveInfo ri : allApps) {
            info = ri;
            break;
        }
        if (info != null) {
            Intent launchIntent = new Intent("com.ucar.intent.action.UCAR");
//            launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                    |Intent.FLAG_ACTIVITY_CLEAR_TOP
//                    |Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            launchIntent.addFlags(0x10104000);

            //launchIntent.putExtra("isHiCarMode", true);
            //launchIntent.putExtra("isHiCar", true);
            //launchIntent.putExtra("isActivityMetricsFirst", true);
            launchIntent.putExtra("isUcarMode", true);
            launchIntent.putExtra("isVivoCarLinkMode", true);
            //launchIntent.putExtra("isSamsungCarLifeMode", true);
            launchIntent.putExtra("screenMode", 2);
            //launchIntent.putExtra("UCarBigMapDisplayId", context.getDisplay().getDisplayId());
            //launchIntent.putExtra("UCarSmallMapDisplayId", context.getDisplay().getDisplayId());
            launchIntent.putExtra("displayId", context.getDisplay().getDisplayId());
            launchIntent.putExtra("isUCarNaviUseVerticalScreenLayout", true);
            //launchIntent.putExtra("isHiCarNaviUseVerticalScreenLayout", true);
            launchIntent.setFlags(807403520);
            launchIntent.setFlags(0x30200000);
            launchIntent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
            ActivityOptions ao = ActivityOptions.makeBasic();
            ao.setLaunchDisplayId(context.getDisplay().getDisplayId());
            try {
                context.startActivity(launchIntent, ao.toBundle());
                //  Toast.makeText(context, "启动成功", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                //Toast.makeText(context, "启动失败，默认模式启动", Toast.LENGTH_LONG).show();
                StartDefault(context, pkgName);
            }
        } else {
            //Toast.makeText(context, "无适配，默认模式启动", Toast.LENGTH_LONG).show();
            StartDefault(context, pkgName);
        }
    }

    @SuppressLint("WrongConstant")
    public static void StartMiniMapFunction(Context context, String uri) {


        String pkgName = CarLinkData.pkgNameAmap;

//        i(context,pkgName);

        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        mActivityManager.killBackgroundProcesses(pkgName);

        Intent i = new Intent("com.ucar.intent.action.UCAR", null);
        i.setPackage(pkgName);
        List<ResolveInfo> allApps = context.getPackageManager().queryIntentActivities(i, 0);
        ResolveInfo info = null;
        for (ResolveInfo ri : allApps) {
            info = ri;
            break;
        }
        if (info != null) {
            Intent launchIntent = new Intent("com.ucar.intent.action.UCAR");
//            launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                    |Intent.FLAG_ACTIVITY_CLEAR_TOP
//                    |Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            launchIntent.addFlags(0x10104000);

            //launchIntent.putExtra("isHiCarMode", true);
            //launchIntent.putExtra("isHiCar", true);

            //launchIntent.putExtra("isActivityMetricsFirst", true);
            launchIntent.putExtra("isUcarMode", true);
            launchIntent.putExtra("isVivoCarLinkMode", true);
            //launchIntent.putExtra("isSamsungCarLifeMode", true);
            launchIntent.putExtra("screenMode", 2);
            launchIntent.putExtra("magicType", 1);
            //launchIntent.putExtra("UCarBigMapDisplayId", context.getDisplay().getDisplayId());
            //launchIntent.putExtra("UCarSmallMapDisplayId", context.getDisplay().getDisplayId());
            launchIntent.putExtra("displayId", context.getDisplay().getDisplayId());
            launchIntent.putExtra("isUCarNaviUseVerticalScreenLayout", true);
            //launchIntent.putExtra("isHiCarNaviUseVerticalScreenLayout", true);
            launchIntent.setFlags(807403520);
            launchIntent.setFlags(0x30200000);
            launchIntent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
            ActivityOptions ao = ActivityOptions.makeBasic();
            ao.setLaunchDisplayId(context.getDisplay().getDisplayId());
            launchIntent.setData(Uri.parse(uri));
            try {
                context.startActivity(launchIntent, ao.toBundle());
                //  Toast.makeText(context, "启动成功", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                //Toast.makeText(context, "启动失败，默认模式启动", Toast.LENGTH_LONG).show();
                StartDefault(context, pkgName);
            }
        } else {
            //Toast.makeText(context, "无适配，默认模式启动", Toast.LENGTH_LONG).show();
            StartDefault(context, pkgName);
        }
    }

    @SuppressLint("WrongConstant")
    public static void StartMiniMapBar(Context context) {
        if(NavBar.getInstance()!=null){
            Message msg = Message.obtain();
            msg.what = 4;
            myHandler.sendMessage(msg);
        }
        String pkgName = CarLinkData.getStringFromList(context,CarLinkData.sp_dock_map_pkg);
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        mActivityManager.killBackgroundProcesses(pkgName);

        Intent i = new Intent("com.ucar.intent.action.UCAR", null);
        i.setPackage(pkgName);
        List<ResolveInfo> allApps = context.getPackageManager().queryIntentActivities(i, 0);
        ResolveInfo info = null;
        for (ResolveInfo ri : allApps) {
            info = ri;
            break;
        }
        if (info != null) {
            Intent launchIntent = new Intent("com.ucar.intent.action.UCAR");
            launchIntent.setClass(context, MiniMapActivity.class);
//            launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                    |Intent.FLAG_ACTIVITY_CLEAR_TOP
//                    |Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            launchIntent.addFlags(0x10104000);

            //launchIntent.putExtra("isHiCarMode", true);
            //launchIntent.putExtra("isHiCar", true);
            //launchIntent.putExtra("isActivityMetricsFirst", true);
            launchIntent.putExtra("isUcarMode", true);
            launchIntent.putExtra("isVivoCarLinkMode", true);
            //launchIntent.putExtra("isSamsungCarLifeMode", true);
            launchIntent.putExtra("screenMode", 1);
            //launchIntent.putExtra("UCarBigMapDisplayId", context.getDisplay().getDisplayId());
            //launchIntent.putExtra("UCarSmallMapDisplayId", context.getDisplay().getDisplayId());
            launchIntent.putExtra("displayId", context.getDisplay().getDisplayId());
            launchIntent.putExtra("isUCarNaviUseVerticalScreenLayout", true);
            //launchIntent.putExtra("isHiCarNaviUseVerticalScreenLayout", true);
            launchIntent.setFlags(807403520);
            launchIntent.setFlags(0x30200000);
            //launchIntent.setData(Uri.parse("amapuriucar://ucar/rootCarProjectionMap?sourceApplication=com.vivo.ucar&keepStack=1&clearStack=0"));
            launchIntent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
            ActivityOptions ao = ActivityOptions.makeBasic();
            ao.setLaunchDisplayId(context.getDisplay().getDisplayId());
            //launchIntent.addFlags(0x10104000);
            try {
                context.startActivity(launchIntent, ao.toBundle());
            } catch (Exception e) {
                StartDefault(context, pkgName);
            }
        } else {
            StartDefault(context, pkgName);
        }
    }

    public static void startBixby(Context context) {
        Intent intent = new Intent(RecognizerIntent.ACTION_WEB_SEARCH);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        context.startActivity(intent);
    }

    public static void i(Context context, String str) {
        try {
            Class<?> cls = Class.forName("android.app.ActivityManager");
            cls.getMethod("forceStopPackage", String.class).invoke(context.getSystemService(cls), str);
        } catch (Exception e) {
            Log.i("AppStartUtil", "killProcess:", e);
        }
    }


    @SuppressLint("WrongConstant")
    public static void StartUsingDeepLink(Context context, String packagename, String task) {
        if (OpenProvider.isSupported(context)) {

            if (OpenProvider.isConnected(context)) {
                String uri = "banqumusic://tasker/" + packagename + "/" + task;
                Uri parse = Uri.parse("samsungcarlink://link/----" + uri);
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(parse);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(0x10104000);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "未连接车机", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, "服务版本不支持", Toast.LENGTH_LONG).show();
        }
    }
}

