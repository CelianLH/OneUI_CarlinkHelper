package com.baidu.BaiduMap.Widget.Music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.baidu.BaiduMap.MainActivity;

public class MusicServiceNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals("kill_MusicService")) {
                if (MainActivity.mainActivity == null && MediaSessionConnectionOperator.getInstance(null) != null) {
                    MediaSessionConnectionOperator.getInstance(null).disconnect();
                }
            }
        }
    }
}
