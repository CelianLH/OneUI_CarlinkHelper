package com.baidu.BaiduMap.carlifeapplauncher.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

public class OpenProvider {

    public static boolean isSupported(Context context) {
        ContentResolver cr = context.getContentResolver();
        try {
            Bundle bd = cr.call("com.samsung.android.carlink.carlife.openprovider", "get_connection_status", null, null);
            assert bd != null;
            int status = bd.getInt("connection_status");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isConnected(Context context)
    {
        //return true;
        ContentResolver cr = context.getContentResolver();
        Bundle bd = cr.call("com.samsung.android.carlink.carlife.openprovider", "get_connection_status", null, null);
        assert bd != null;
        int status = bd.getInt("connection_status");
        return status == 1;
    }


}
