package com.baidu.BaiduMap.Utils;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public class ToastUtil {
    private static int mDuration = 1000;//toast默认的时间

    private static Toast mToast;
    private static Handler mHandler = new Handler();
    private static Runnable r = new Runnable() {
        public void run() {
            mToast.cancel();
        }
    };

    public static void showToast(Context mContext, String text) {
        showToast(mContext, text, mDuration);
    }

    public static void showToast(Context mContext, int resId) {
        showToast(mContext, mContext.getResources().getString(resId), mDuration);
    }

    public static void showToast(Context mContext, String text, int duration) {

        mHandler.removeCallbacks(r);
        if (mToast != null)
            mToast.setText(text);
        else
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        mHandler.postDelayed(r, duration);

        mToast.show();

    }

    public static void showToast(Context mContext, int resId, int duration) {
        showToast(mContext, mContext.getResources().getString(resId), duration);
    }
}