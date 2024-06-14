package com.baidu.BaiduMap;


import android.app.Application;
import android.content.Context;
import android.view.Menu;

import com.baidu.BaiduMap.Utils.Theme.NightModeUtils;
import com.baidu.BaiduMap.Utils.Language.MultiLanguages;

import me.weishu.reflection.Reflection;

public class BaseApplication extends Application {

    private static Context mContext;
    public static Menu mMenu;
    public static Boolean isAfterSetTheme = false;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        NightModeUtils.getInstance(this).applySetting();
        MultiLanguages.init(this);
    }

    /*
     * 获取全局上下文
     */
    public static Context getContext() {
        return mContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(MultiLanguages.attach(base));
        Reflection.unseal(this);
    }
}
