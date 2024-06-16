package com.baidu.BaiduMap.deeplink;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;

import com.baidu.BaiduMap.ConstantData.CarLinkData;
import com.baidu.BaiduMap.R;
import com.baidu.BaiduMap.Utils.PreferenceUtil;
import com.baidu.BaiduMap.Utils.ToastUtil;
import com.baidu.BaiduMap.Widget.FloatView.NavBar;
import com.baidu.BaiduMap.Widget.FloatView.WidgetRight;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Objects;

public class DeepLinkService extends AppCompatActivity {
    public static DeepLinkService deepLinkService;
    private Context mContex;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_mini_map);
        deepLinkService = this;
        mContex = this;
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onResume() {
        super.onResume();
        resume();
        Intent i = new Intent("carlife.intent.action.openpage");
        i.setClassName("com.baidu.carlife", "com.baidu.carlife.CarlifeActivity");
        i.putExtra("pageid", 1);
        i.addFlags(0x10104000);
        startActivity(i);
    }

    public static DeepLinkService getInstance() {
        return deepLinkService;
    }

    public void resume() {
        XXPermissions.with(this).permission(Permission.SYSTEM_ALERT_WINDOW).permission(Permission.BIND_NOTIFICATION_LISTENER_SERVICE).permission(Permission.READ_PHONE_STATE).permission(Permission.ACCESS_COARSE_LOCATION).permission(Permission.ACCESS_FINE_LOCATION).request(new OnPermissionCallback() {
            @Override
            public void onGranted(@NonNull List<String> permissions, boolean allGranted) {
                if (NavBar.getInstance() == null && Objects.requireNonNull(mContex.getDisplay()).getDisplayId() != Display.DEFAULT_DISPLAY&& CarLinkData.getBoolean(mContex,CarLinkData.sp_enable_overlay,true)) {
                    NavBar.createInstance(mContex).onStart();
                }
                if (WidgetRight.getInstance() == null && Objects.requireNonNull(mContex.getDisplay()).getDisplayId() != Display.DEFAULT_DISPLAY&& CarLinkData.getBoolean(mContex,CarLinkData.sp_enable_overlay_right,true)) {
                    WidgetRight.createInstance(mContex).onStart();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        destroyAllService();
        super.onDestroy();
    }

    public void destroyAllService() {
        if (NavBar.getInstance() != null) {
            NavBar.getInstance().onDestroy();
        }
        if (WidgetRight.getInstance() != null) {
            WidgetRight.getInstance().onDestroy();
        }
    }
}