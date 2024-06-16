package com.baidu.BaiduMap.Widget.Music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.baidu.BaiduMap.MainActivity;

import java.util.Objects;

public class MusicServiceShutdownReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("TTTT", "onReceive: " + intent.getAction());
        if (Objects.requireNonNull(intent.getAction()).equals(Intent.ACTION_SCREEN_OFF)) {
            if (MainActivity.mainActivity == null && MediaSessionConnectionOperator.getInstance(null) != null) {
                MediaSessionConnectionOperator.getInstance(null).disconnect();
            }
        }
    }
}
