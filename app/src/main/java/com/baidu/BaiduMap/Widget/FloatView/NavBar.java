package com.baidu.BaiduMap.Widget.FloatView;

import static android.content.Context.BATTERY_SERVICE;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Insets;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.BaiduMap.ConstantData.CarLinkData;
import com.baidu.BaiduMap.MainActivity;
import com.baidu.BaiduMap.R;
import com.baidu.BaiduMap.Utils.PhoneUtils;
import com.baidu.BaiduMap.Utils.SignalStrengthListener;
import com.baidu.BaiduMap.Utils.ToastUtil;
import com.baidu.BaiduMap.carlifeapplauncher.adapter.Common;
import com.baidu.BaiduMap.carlifeapplauncher.adapter.FakeStart;
import com.baidu.BaiduMap.deeplink.DeepLinkService;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.preference.PreferenceManager;

public class NavBar {
    private static TextView time_View;
    private static TextView battery_View;
    private static ImageView battery_Image;
    private static ImageView mobile1;
    private static ImageView mobile2;
    private static Context context;
    private WindowManager wm;
    private PackageManager pm;

    private static LinearLayout navbar_wrapper;
    private static LinearLayout dockRootView;
    private static CardView dockContainer;
    private View navbar_view;
    public static boolean shown = false;
    public static boolean isCharging = false;
    private int radius;
    private int iconsize;
    private ImageView imageView_home;
    public static NavBar navBar;
    private updateTimeReceiver tiktok;
    private updateBatteryReceiver batteryReceiver;
    private chargeReceiver mChargeReceiver;
    private SignalStrengthListener signalStrengthListener;

    public static MyHandler myHandler;

    public static class MyHandler extends Handler {
        WeakReference<DeepLinkService> mActivity;

        public MyHandler(@NonNull Looper looper, DeepLinkService activity) {
            super(looper);
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            int i = msg.what;
            switch (i) {
                case 0:
                    if (shown && time_View != null) {
                        time_View.setText(getSystemTime());
                    }
                    break;
                case 1:
                    if (shown && battery_View != null) {
                        battery_View.setText(getSystemBattery(context));
                        battery_Image.setImageBitmap(adjustBrightnessPercent(drawableToBitmap(context.getDrawable(R.drawable.battery_capacity)), 150, new Float(Float.parseFloat(getSystemBattery(context).replace("%", "")) / 100)));
                    }
                    break;
                case 3:
                    setRightVisible();
                    break;
                case 4:
                    setRightInvisible();
                    break;
            }
        }
    }

    public static NavBar getInstance() {
        return navBar;
    }

    public static NavBar createInstance(Context context) {
        if (navBar == null) {
            navBar = new NavBar(context);
        }
        return navBar;
    }

    public NavBar(Context context) {
        this.context = context;
        this.wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        this.pm = context.getPackageManager();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void onStart() {
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowMetrics windowMetrics = wm.getCurrentWindowMetrics();
        WindowInsets windowInsets = windowMetrics.getWindowInsets();
        Insets insets = windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars() | WindowInsets.Type.displayCutout());

        this.radius = (int) (insets.left * 0.55);
        this.iconsize = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("godmodeiconsize", "0"));
        if (iconsize == 0) {
            this.iconsize = (int) (insets.left * 0.55);
        }
        isCharging = isCharging(context);

        navbar_view = View.inflate(context, R.layout.overlay_dock, null);
        int opacity = (int) (Double.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getString("godmode_opacity", "100")) / 100 * 255);
        String rgb_string = PreferenceManager.getDefaultSharedPreferences(context).getString("godmode_rgb", "55#55#55");
        String[] rgb_array = rgb_string.split("#");
        int trans = Color.argb(opacity, Integer.parseInt(rgb_array[0]), Integer.parseInt(rgb_array[1]), Integer.parseInt(rgb_array[2]));
        navbar_view.setBackgroundColor(trans);
        navbar_view.setFocusable(false);

        mChargeReceiver = new chargeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        context.registerReceiver(mChargeReceiver, filter);

        tiktok = new updateTimeReceiver();
        context.registerReceiver(tiktok, new IntentFilter(Intent.ACTION_TIME_TICK));


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(insets.left, WindowManager.LayoutParams.MATCH_PARENT,
                2038,
                262184,
                PixelFormat.TRANSLUCENT);

        lp.gravity = Gravity.LEFT | Gravity.TOP;
        lp.setFitInsetsTypes(5);


        lp.windowAnimations = R.style.anim_notification;
        lp.token = null;
        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        lp.x = 0;
        lp.y = 0;
        navbar_wrapper = navbar_view.findViewById(R.id.godmodelist);

        myHandler = new MyHandler(context.getMainLooper(), (DeepLinkService) context);

        dockRootView = navbar_view.findViewById(R.id.dock_root);
        dockContainer = navbar_view.findViewById(R.id.god_container);
        time_View = navbar_view.findViewById(R.id.time);
        LinearLayout dockBack = navbar_view.findViewById(R.id.dock_back);
        if (CarLinkData.getBoolean(context, CarLinkData.sp_theme_enable_pic_dock, true)) {
            switch (CarLinkData.getString(context, CarLinkData.sp_theme_dock_pic)) {
                case "0":
                    dockBack.setBackground(context.getDrawable(R.drawable.dock_hover_0));
                    break;
                case "1":
                    dockBack.setBackground(context.getDrawable(R.drawable.dock_hover_1));
                    break;
                case "2":
                    dockBack.setBackground(context.getDrawable(R.drawable.dock_hover_2));
                    break;
                case "3":
                    dockBack.setBackground(context.getDrawable(R.drawable.dock_hover_3));
                    break;
            }
        } else if (!CarLinkData.getBoolean(context, CarLinkData.sp_theme_dock_color_use_default, true)) {
            dockBack.setBackgroundColor(CarLinkData.getInt(context, CarLinkData.sp_theme_dock_color, 0));
        }


        dockContainer.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 45);
            }
        });
        dockContainer.setClipToOutline(true);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) dockContainer.getLayoutParams();
        layoutParams.setMargins(12, 26, 12, 26);
        dockContainer.setLayoutParams(layoutParams);
        dockRootView.setBackgroundColor(context.getColor(R.color.black));

        battery_View = navbar_view.findViewById(R.id.battery);
        mobile1 = navbar_view.findViewById(R.id.dock_mobile_1);
        mobile2 = navbar_view.findViewById(R.id.dock_mobile_2);
        battery_Image = navbar_view.findViewById(R.id.battery_image);
        time_View.setText(getSystemTime());
        battery_View.setText(getSystemBattery(context));
        time_View.setFocusable(false);
        Common.setBgRadius(time_View, radius);


        battery_Image.setImageBitmap(adjustBrightnessPercent(drawableToBitmap(context.getDrawable(R.drawable.battery_capacity)), 150, new Float(Integer.parseInt(getSystemBattery(context).replace("%", "")) / 100)));
        batteryReceiver = new updateBatteryReceiver();
        context.registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));


        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) dockContainer.getLayoutParams();

        signalStrengthListener = new SignalStrengthListener(context) {
            @Override
            public void onStrengthChanged() {
                super.onStrengthChanged();
                if (PhoneUtils.checkSIMExist(context, 0)) {
                    mobile1.setVisibility(View.VISIBLE);
                    mobile1.setColorFilter(context.getColor(R.color.signal_logo_color));
                    int signal1 = PhoneUtils.getSiSignalStrength(context, 1);

                    if (PhoneUtils.getSiSignalType(context, 1).equals("nr")) {
                        switch (signal1) {
                            case 0:
                                mobile1.setImageDrawable(context.getDrawable(R.drawable.single_sim_5g00));
                                break;
                            case 1:
                                mobile1.setImageDrawable(context.getDrawable(R.drawable.single_sim_5g01));
                                break;
                            case 2:
                                mobile1.setImageDrawable(context.getDrawable(R.drawable.single_sim_5g02));
                                break;
                            case 3:
                                mobile1.setImageDrawable(context.getDrawable(R.drawable.single_sim_5g03));
                                break;
                            case 4:
                                mobile1.setImageDrawable(context.getDrawable(R.drawable.single_sim_5g04));
                                break;
                            case 5:
                                mobile1.setImageDrawable(context.getDrawable(R.drawable.single_sim_5g05));
                                break;
                            default:
                                mobile1.setImageDrawable(context.getDrawable(R.drawable.single_no_sim_wifi));
                                break;
                        }
                    } else if (PhoneUtils.getSiSignalType(context, 1).equals("lte")) {
                        switch (signal1) {
                            case 0:
                                mobile1.setImageDrawable(context.getDrawable(R.drawable.single_sim_4g00));
                                break;
                            case 1:
                                mobile1.setImageDrawable(context.getDrawable(R.drawable.single_sim_4g01));
                                break;
                            case 2:
                                mobile1.setImageDrawable(context.getDrawable(R.drawable.single_sim_4g02));
                                break;
                            case 3:
                                mobile1.setImageDrawable(context.getDrawable(R.drawable.single_sim_4g03));
                                break;
                            case 4:
                                mobile1.setImageDrawable(context.getDrawable(R.drawable.single_sim_4g04));
                                break;
                            case 5:
                                mobile1.setImageDrawable(context.getDrawable(R.drawable.single_sim_4g05));
                                break;
                            default:
                                mobile1.setImageDrawable(context.getDrawable(R.drawable.single_no_sim_wifi));
                                break;
                        }
                    } else {
                        switch (signal1) {
                            case 0:
                                mobile1.setImageDrawable(context.getDrawable(R.drawable.single_sim_00));
                                break;
                            case 1:
                                mobile1.setImageDrawable(context.getDrawable(R.drawable.single_sim_01));
                                break;
                            case 2:
                                mobile1.setImageDrawable(context.getDrawable(R.drawable.single_sim_02));
                                break;
                            case 3:
                                mobile1.setImageDrawable(context.getDrawable(R.drawable.single_sim_03));
                                break;
                            case 4:
                                mobile1.setImageDrawable(context.getDrawable(R.drawable.single_sim_04));
                                break;
                            case 5:
                                mobile1.setImageDrawable(context.getDrawable(R.drawable.single_sim_05));
                                break;
                            default:
                                mobile1.setImageDrawable(context.getDrawable(R.drawable.single_no_sim_wifi));
                                break;
                        }
                    }

                } else {
                    mobile1.setVisibility(View.GONE);
                }
                if (PhoneUtils.checkSIMExist(context, 1)) {
                    mobile2.setVisibility(View.VISIBLE);
                    mobile2.setColorFilter(context.getColor(R.color.signal_logo_color));
                    int signal2 = PhoneUtils.getSiSignalStrength(context, 2);
                    if (PhoneUtils.getSiSignalType(context, 2).equals("nr")) {
                        switch (signal2) {
                            case 0:
                                mobile2.setImageDrawable(context.getDrawable(R.drawable.single_sim_5g00));
                                break;
                            case 1:
                                mobile2.setImageDrawable(context.getDrawable(R.drawable.single_sim_5g01));
                                break;
                            case 2:
                                mobile2.setImageDrawable(context.getDrawable(R.drawable.single_sim_5g02));
                                break;
                            case 3:
                                mobile2.setImageDrawable(context.getDrawable(R.drawable.single_sim_5g03));
                                break;
                            case 4:
                                mobile2.setImageDrawable(context.getDrawable(R.drawable.single_sim_5g04));
                                break;
                            case 5:
                                mobile2.setImageDrawable(context.getDrawable(R.drawable.single_sim_5g05));
                                break;
                            default:
                                mobile2.setImageDrawable(context.getDrawable(R.drawable.single_no_sim_wifi));
                                break;
                        }
                    } else if (PhoneUtils.getSiSignalType(context, 2).equals("lte")) {
                        switch (signal2) {
                            case 0:
                                mobile2.setImageDrawable(context.getDrawable(R.drawable.single_sim_4g00));
                                break;
                            case 1:
                                mobile2.setImageDrawable(context.getDrawable(R.drawable.single_sim_4g01));
                                break;
                            case 2:
                                mobile2.setImageDrawable(context.getDrawable(R.drawable.single_sim_4g02));
                                break;
                            case 3:
                                mobile2.setImageDrawable(context.getDrawable(R.drawable.single_sim_4g03));
                                break;
                            case 4:
                                mobile2.setImageDrawable(context.getDrawable(R.drawable.single_sim_4g04));
                                break;
                            case 5:
                                mobile2.setImageDrawable(context.getDrawable(R.drawable.single_sim_4g05));
                                break;
                            default:
                                mobile2.setImageDrawable(context.getDrawable(R.drawable.single_no_sim_wifi));
                                break;
                        }
                    } else {
                        switch (signal2) {
                            case 0:
                                mobile2.setImageDrawable(context.getDrawable(R.drawable.single_sim_00));
                                break;
                            case 1:
                                mobile2.setImageDrawable(context.getDrawable(R.drawable.single_sim_01));
                                break;
                            case 2:
                                mobile2.setImageDrawable(context.getDrawable(R.drawable.single_sim_02));
                                break;
                            case 3:
                                mobile2.setImageDrawable(context.getDrawable(R.drawable.single_sim_03));
                                break;
                            case 4:
                                mobile2.setImageDrawable(context.getDrawable(R.drawable.single_sim_04));
                                break;
                            case 5:
                                mobile2.setImageDrawable(context.getDrawable(R.drawable.single_sim_05));
                                break;
                            default:
                                mobile2.setImageDrawable(context.getDrawable(R.drawable.single_no_sim_wifi));
                                break;
                        }
                    }
                } else {
                    mobile2.setVisibility(View.GONE);
                }
            }
        };
        signalStrengthListener.startListening();
        time_View.setOnTouchListener(new View.OnTouchListener() {
            private float ox1, oy1;
            private float x1, x2;
            private float y1, y2;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        x1 = motionEvent.getRawX();//得到相对应屏幕左上角的坐标
                        y1 = motionEvent.getRawY();

                        ox1 = motionEvent.getRawX();
                        oy1 = motionEvent.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        x2 = motionEvent.getRawX();
                        y2 = motionEvent.getRawY();

                        if (navbar_wrapper.getVisibility() != View.VISIBLE && CarLinkData.getBoolean(context, CarLinkData.sp_overlay_left_hide_visibility, false) != true) {
                            lp.x += x2 - x1;
                            lp.y += y2 - y1;
                            wm.updateViewLayout(navbar_view, lp);
                        }
                        x1 = x2;
                        y1 = y2;
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = motionEvent.getRawX();
                        y2 = motionEvent.getRawY();
                        double distance = Math.sqrt(Math.abs(ox1 - x2) * Math.abs(ox1 - x2) + Math.abs(oy1 - y2) * Math.abs(oy1 - y2));//两点之间的距离
                        if (distance < 15) {

                            if (navbar_wrapper == null) {
                                return false;
                            }
                            if (navbar_wrapper.getVisibility() == View.VISIBLE) {
                                if (WidgetRight.navbar_view != null) {
                                    setRightInvisible();
                                }
                                ((LinearLayout) dockContainer.getParent()).setBackgroundColor(Color.parseColor("#00000000"));

                                if (CarLinkData.getBoolean(context, CarLinkData.sp_overlay_left_hide_visibility, false)) {
                                    ((LinearLayout) dockContainer.getParent()).setAlpha(0.f);
                                }

                                layoutParams1.height = 80;
                                dockContainer.setLayoutParams(layoutParams1);
                                if (WidgetRight.navbar_view != null) {
                                    if (WidgetRight.navbar_view.getVisibility() == View.INVISIBLE) {
                                        setRightVisible();
                                    }
                                }
                                setVisibility(false);
                            } else {
                                if (WidgetRight.navbar_view != null) {
                                    setRightVisible();
                                }
                                lp.x = 0;
                                lp.y = 0;
                                layoutParams1.height = ViewGroup.LayoutParams.MATCH_PARENT;
                                dockContainer.setLayoutParams(layoutParams1);
                                ((LinearLayout) dockContainer.getParent()).setBackgroundColor(Color.parseColor("#FF000000"));
                                ((LinearLayout) dockContainer.getParent()).setAlpha(1.f);
                                wm.updateViewLayout(navbar_view, lp);
                                setVisibility(true);
                            }
                            return false;
                        }
                }
                return true;
            }
        });

        if (CarLinkData.getBoolean(context, CarLinkData.sp_dock_music, true)) {
            constructView(CarLinkData.getStringFromList(context, CarLinkData.sp_dock_music_pkg));
        }
        if (CarLinkData.getBoolean(context, CarLinkData.sp_dock_map, true)) {
            constructView(CarLinkData.getStringFromList(context, CarLinkData.sp_dock_map_pkg));
        }
        if (CarLinkData.getBoolean(context, CarLinkData.sp_dock_any, true)) {
            constructView(CarLinkData.getStringFromList(context, CarLinkData.sp_dock_any_pkg));
        }

        if (CarLinkData.getBoolean(context, CarLinkData.sp_dock_bixby, true)) {
            constructView("bixby");
        }
        constructView("homeIcon");


        navbar_view.setMinimumHeight(Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getString("min_height", "0")));
        wm.addView(navbar_view, lp);

        WindowInsetsControllerCompat controllerCompat = ViewCompat.getWindowInsetsController(navbar_view);
        controllerCompat.hide(WindowInsetsCompat.Type.statusBars());
        controllerCompat.hide(WindowInsetsCompat.Type.navigationBars());
        controllerCompat.hide(WindowInsetsCompat.Type.ime());
        controllerCompat.hide(WindowInsetsCompat.Type.systemBars());

        setVisibility(true);
        shown = true;
    }

    private void constructView(String key) {
        switch (key) {
            case "bixby":
                bixbyIcon();
                return;
            case "homeIcon":
                homeIcon();
                return;
            default:
                appIcon(key);
        }
    }

    private void appIcon(String pkg) {
        try {
            ApplicationInfo temp_info = pm.getApplicationInfo(pkg, 0);
            LinearLayout frame = (LinearLayout) View.inflate(context, R.layout.godmode_cell, null);
            frame.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, -1, 1));
            ImageView icon = frame.findViewById(R.id.godcellicon);

            icon.setImageDrawable(temp_info.loadIcon(pm));
            ViewGroup.LayoutParams lp2 = icon.getLayoutParams();

            lp2.width = iconsize;
            lp2.height = iconsize;
            icon.setLayoutParams(lp2);
            Common.setBgRadiusWithCutOut(icon, (int) (iconsize * 0.05), (int) (iconsize * 0.35));
            icon.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("WrongConstant")
                @Override
                public void onClick(View v) {
                    imageView_home.setImageResource(R.drawable.ic_home_caruion);
                    setRightInvisible();
                    if (CarLinkData.pkgNameAmap.equals(pkg)) {
                        FakeStart.StartMiniMapToContext2(context);
                    } else if (pkg.equals(CarLinkData.getStringFromList(context, CarLinkData.sp_dock_map_pkg))) {
                        Intent i = new Intent();
                        i.setClass(context, MainActivity.class);
                        i.addFlags(0x10104000);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        context.startActivity(i);
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                FakeStart.Start(context, pkg);
                            }
                        }, 50);
                    } else {
                        FakeStart.Start(context, pkg);

                    }
                }
            });
            frame.setFocusable(false);
            navbar_wrapper.addView(frame);
        } catch (Exception e) {
            ToastUtil.showToast(context, context.getString(R.string.tip_Dock_not_set), 3000);
        }
    }

    private void homeIcon() {
        LinearLayout frame = (LinearLayout) View.inflate(context, R.layout.godmode_cell, null);
        frame.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, -1, 1));
        ImageView icon = frame.findViewById(R.id.godcellicon);
        if (CarLinkData.getIntFromString(context, CarLinkData.sp_dock_home, 0) == 1) {
            icon.setImageResource(R.drawable.ic_home_caruion);
        } else {
            icon.setImageResource(R.drawable.ic_more_app);
        }
        imageView_home = icon;
        double padding = iconsize * 0.16;
        //icon.setPadding((int) padding, (int) padding, (int) padding, (int) padding);
        ViewGroup.LayoutParams lp2 = icon.getLayoutParams();
        lp2.width = iconsize;
        lp2.height = iconsize;
        //icon.setLayoutParams(lp2);
        icon.setBackgroundColor(Color.argb(255, 155, 155, 155));
        Common.setBgRadiusWithCutOut(icon, (int) (iconsize * 0.05), (int) (iconsize * 0.35));
        icon.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                if (WidgetRight.navbar_view != null) {
                    if (WidgetRight.navbar_view.getVisibility() == View.GONE) {
                        setRightVisible();
                        if (CarLinkData.getIntFromString(context, CarLinkData.sp_dock_home, 0) == 1) {
                            icon.setImageResource(R.drawable.ic_home_caruion);
                        } else {
                            icon.setImageResource(R.drawable.ic_more_app);
                        }
                        Intent i = new Intent("carlife.intent.action.openpage");
                        i.setClassName("com.baidu.carlife", "com.baidu.carlife.CarlifeActivity");
                        i.putExtra("pageid", 1);
//                      i.putExtra("from", 96);
                        i.addFlags(0x10104000);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        context.startActivity(i);
                    } else if (WidgetRight.navbar_view.getVisibility() == View.VISIBLE) {
                        if (CarLinkData.getIntFromString(context, CarLinkData.sp_dock_home, 0) == 1) {
                            Intent i = new Intent("carlife.intent.action.openpage");
                            i.setClassName("com.baidu.carlife", "com.baidu.carlife.CarlifeActivity");
                            i.putExtra("pageid", 1);
                            i.addFlags(0x10104000);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            context.startActivity(i);
                        } else {
                            setRightInvisible();
                            icon.setImageResource(R.drawable.ic_home_caruion);
                            Intent i = new Intent();
                            i.setClass(context, MainActivity.class);
                            i.addFlags(0x10104000);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            context.startActivity(i);
                        }
                    }
                }

            }
        });
        frame.setFocusable(false);
        navbar_wrapper.addView(frame);
    }

    private void bixbyIcon() {

        LinearLayout frame = (LinearLayout) View.inflate(context, R.layout.godmode_cell, null);
        frame.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, -1, 1));
        ImageView icon = frame.findViewById(R.id.godcellicon);

        try {
            ApplicationInfo temp_info = pm.getApplicationInfo("com.samsung.android.bixby.agent", 0);
            icon.setImageDrawable(temp_info.loadIcon(pm));
        } catch (Exception e) {
            icon.setImageResource(R.drawable.ic_bixby_carunion);
        }

        double padding = iconsize * 0.16;
        //icon.setPadding((int) padding, (int) padding, (int) padding, (int) padding);
        ViewGroup.LayoutParams lp2 = icon.getLayoutParams();
        lp2.width = iconsize;
        lp2.height = iconsize;
        //icon.setLayoutParams(lp2);

        icon.setBackgroundColor(Color.argb(255, 255, 255, 255));
        Common.setBgRadiusWithCutOut(icon, (int) (iconsize * 0.05), (int) (iconsize * 0.35));
        icon.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                if (WidgetRight.navbar_view != null) {
                    if (WidgetRight.navbar_view.getVisibility() == View.VISIBLE) {
                        setRightInvisible();
                    }
                }
                FakeStart.startBixby(context);
//                setRightInvisible();
//                Intent i = new Intent();
//                i.setClass(context, MainActivity.class);
//                i.addFlags(0x10104000);
//                context.startActivity(i);
            }
        });
        frame.setFocusable(false);
        navbar_wrapper.addView(frame);
    }

    private void setVisibility(Boolean visible) {
        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) navbar_view.getLayoutParams();
        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(context);
//        int widthLeft = Integer.parseInt(s.getString("overlay_left_width", "160"));
        int widthLeft = CarLinkData.getIntFromString(context,CarLinkData.sp_overlay_left_width,160);
        lp.width = widthLeft;
        if (visible) {

            lp.height = LinearLayout.LayoutParams.MATCH_PARENT;

            Common.setBgRadius(navbar_view, 0);
            navbar_wrapper.setVisibility(View.VISIBLE);
        } else {
            Common.setBgRadius(navbar_view, radius);
            navbar_wrapper.setVisibility(View.GONE);
            lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        }
        wm.updateViewLayout(navbar_view, lp);
    }


    public class updateTimeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Message msg = Message.obtain();
            msg.what = 0;
            myHandler.sendMessage(msg);
        }
    }

    public class updateBatteryReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Message msg = Message.obtain();
            msg.what = 1;
            myHandler.sendMessage(msg);
        }
    }

    public class chargeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // 处理充电连接事件
            if (action.equals(Intent.ACTION_POWER_CONNECTED)) {
                isCharging = true;
            }

            // 处理充电断开事件
            if (action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
                isCharging = false;
            }
            Message msg = Message.obtain();
            msg.what = 1;
            myHandler.sendMessage(msg);
        }
    }

    public static boolean isCharging(Context context) {
        Intent batteryBroadcast = context.registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        // 0 means we are discharging, anything else means charging
        boolean isCharging = batteryBroadcast.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) != 0;
        return isCharging;
    }

//    private static Bitmap drawableToBitmap(Drawable drawable) {
//        //声明将要创建的bitmap
//        Bitmap bitmap = null;
//        //获取图片宽度
//        int width = drawable.getIntrinsicWidth();
//        //获取图片高度
//        int height = drawable.getIntrinsicHeight();
//        //图片位深，PixelFormat.OPAQUE代表没有透明度，RGB_565就是没有透明度的位深，否则就用ARGB_8888。详细见下面图片编码知识。
//        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
//        //创建一个空的Bitmap
//        bitmap = Bitmap.createBitmap(width, height, config);
//        //在bitmap上创建一个画布
//        Canvas canvas = new Canvas(bitmap);
//        //设置画布的范围
//        drawable.setBounds(0, 0, width, height);
//        //将drawable绘制在canvas上
//        drawable.draw(canvas);
//        return bitmap;
//    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap adjustBrightnessPercent(Bitmap src, int brightness, float percent) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        int A, R, G, B;
        int pixel;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixel = src.getPixel(j, i);
                if (j > width * percent) { // 只处理上半部分
                    A = Color.alpha(pixel);
                    R = Color.red(pixel) - brightness;
                    G = Color.green(pixel) - brightness;
                    B = Color.blue(pixel) - brightness;
                    // 确保RGB值不会小于0或者超过255
                    R = R < 0 ? 0 : R > 255 ? 255 : R;
                    G = G < 0 ? 0 : G > 255 ? 255 : G;
                    B = B < 0 ? 0 : B > 255 ? 255 : B;
                    bmOut.setPixel(j, i, Color.argb(A, R, G, B));
                } else {
                    if (isCharging) {
                        A = Color.alpha(pixel);
                        R = 134;
                        G = 255;
                        B = 150;
                        bmOut.setPixel(j, i, Color.argb(A, R, G, B));
                    } else {
                        bmOut.setPixel(j, i, pixel); // 如果是下半部分，保持不变
                    }

                }
            }
        }

        if (isCharging) {
            Bitmap bitmapFlash = drawableToBitmap(context.getResources().getDrawable(com.baidu.BaiduMap.R.drawable.battery_charging));
            int AFlash;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    AFlash = Color.alpha(bitmapFlash.getPixel(j, i));
                    if (AFlash > 0) {
                        A = 255;
                        R = 255;
                        G = 255;
                        B = 255;
                        bmOut.setPixel(j, i, Color.argb(A, R, G, B));
                    }

                }
            }
        }

        return small(bmOut);
    }

    public static Bitmap small(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(0.5f, 0.5f); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }

    @SuppressLint("SimpleDateFormat")
    static DateFormat df = new SimpleDateFormat("HH:mm");

    private static String getSystemTime() {
        long time = System.currentTimeMillis();
        return df.format(time);
    }

    private static String getSystemBattery(Context context) {
        BatteryManager batteryManager = (BatteryManager) context.getSystemService(BATTERY_SERVICE);
        int edgeBattery = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        String per = (edgeBattery) + "%";
        return per;
    }

    public void onDestroy() {
        if (tiktok != null) {
            context.unregisterReceiver(tiktok);
        }
        if (batteryReceiver != null) {
            context.unregisterReceiver(batteryReceiver);
        }
        if (mChargeReceiver != null) {
            context.unregisterReceiver(mChargeReceiver);
        }
        if (signalStrengthListener != null) {
            signalStrengthListener.stopListening();
            signalStrengthListener = null;
        }
        if (shown) {
            try {
                wm.removeView(navbar_view);
            } catch (Exception e) {

            }
        }

        //avoid error
        shown = false;
        navBar = null;
    }

    public static void setRightInvisible() {
        if (WidgetRight.navbar_view != null) {
            if (WidgetRight.navbar_view.getVisibility() == View.VISIBLE) {
                WidgetRight.navbar_view.clearAnimation();
                WidgetRight.navbar_view.setVisibility(View.GONE);
            }

            if (navbar_wrapper.getVisibility() == View.VISIBLE&&!CarLinkData.getBoolean(context, CarLinkData.sp_overlay_left_dock_corner_always, false)) {
//                dockContainer.setOutlineProvider(new ViewOutlineProvider() {
//                    @Override
//                    public void getOutline(View view, Outline outline) {
//                        outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 0);
//                    }
//                });
//                dockContainer.setClipToOutline(true);
//
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) dockContainer.getLayoutParams();
                layoutParams.setMargins(12, 12, 12, 12);
                dockContainer.setLayoutParams(layoutParams);
                switch (CarLinkData.getString(context, CarLinkData.sp_theme_dock_pic)) {
                    case "0":
                        dockRootView.setBackground(context.getDrawable(R.drawable.dock_root_0));
                        break;
                    case "1":
                        dockRootView.setBackground(context.getDrawable(R.drawable.dock_root_1));
                        break;
                    case "2":
                        dockRootView.setBackground(context.getDrawable(R.drawable.dock_root_2));
                        break;
                    case "3":
                        dockRootView.setBackground(context.getDrawable(R.drawable.dock_root_3));
                        break;
                }
            }
        }
    }

    public static void setRightVisible() {
        if (WidgetRight.navbar_view != null) {
            if (WidgetRight.navbar_view.getVisibility() == View.GONE) {
                WidgetRight.navbar_view.clearAnimation();
                WidgetRight.navbar_view.setVisibility(View.VISIBLE);
            }

            if (!CarLinkData.getBoolean(context, CarLinkData.sp_overlay_left_dock_corner_always, false)) {
                dockContainer.setOutlineProvider(new ViewOutlineProvider() {
                    @Override
                    public void getOutline(View view, Outline outline) {
                        outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 45);
                    }
                });
                dockContainer.setClipToOutline(true);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) dockContainer.getLayoutParams();
                layoutParams.setMargins(12, 26, 12, 26);
                dockContainer.setLayoutParams(layoutParams);

                dockRootView.setBackgroundColor(context.getColor(R.color.black));

            }
        }
    }
}
