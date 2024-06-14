package com.baidu.BaiduMap.Utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.CellSignalStrength;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneUtils {

    private static TelephonyManager mTM;
    private static PhoneStateListener mPSL;
    private static int SimSignalStrength = -1;
    public static String getContentBetween(String source, String start, String end) {
        String regex = Pattern.quote(start) + "(.*?)" + Pattern.quote(end);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
    public static int getSiSignalStrength(Context context, int i) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mTM = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).createForSubscriptionId(i);
        }
        mPSL = new PhoneStateListener();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            setListeningSimCard(i);
        }
        if(mTM==null) {
            mTM.listen(mPSL, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS | PhoneStateListener.LISTEN_CELL_INFO | PhoneStateListener.LISTEN_CELL_LOCATION);
        }
        //SimSignalStrength = mTM.getDataState();
        SimSignalStrength = Integer.parseInt(getContentBetween(getContentBetween(mTM.getSignalStrength().toString(),"SignalBarInfo{",",rat"),"Level="," }"));

//        Log.d("sim", "getSiSignalStrength: "+ SimSignalStrength);
        //Log.d("sim", "getcardNum: "+ i);
        //Log.d("sim", mTM.getSignalStrength().toString()+SimSignalStrength);
        return SimSignalStrength;
    }

    public static String getSiSignalType(Context context, int i) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mTM = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).createForSubscriptionId(i);
        }
        mPSL = new PhoneStateListener();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            setListeningSimCard(i);
        }
        mTM.listen(mPSL, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS | PhoneStateListener.LISTEN_CELL_INFO | PhoneStateListener.LISTEN_CELL_LOCATION );
        //SimSignalStrength = mTM.getDataState();
        String type= getContentBetween(mTM.getSignalStrength().toString(),"SignalBarInfo{ ","Level=");
        //Log.d("sim"+"slot"+i, mTM.getSignalStrength().toString());
        //Log.d("sim", type);
        return type;
    }

    private static void setListeningSimCard(int subId) {
        try {
            Field field = PhoneStateListener.class.getDeclaredField("mSubId");
            field.setAccessible(true);
            field.set(mPSL, new Integer(subId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //如何调用获取双卡信号强度
    //NetworkDevice networkDev = new NetworkDevice(mContext);
    //networkDev.getSiSignalStrength(0);//&#x5361;1
    //networkDev.getSiSignalStrength(1);//k2


    //检测双sim卡是否存在
    //type : 0 （卡1），1（卡2）
    public static boolean checkSIMExist(Context context, int type) {
        boolean result = false;
        SubscriptionManager mSubscriptionManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            mSubscriptionManager = SubscriptionManager.from(context);
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //Log.d(sim, "checkSIMExist: false no permission");
            return false;
        }
        SubscriptionInfo sub = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            sub = mSubscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(type);
        }

        if (null != sub) {
            result = true;
        }
        Log.d("sim", "checkSIMExist"+type+ "result: "+result);
        return result;
    }
}