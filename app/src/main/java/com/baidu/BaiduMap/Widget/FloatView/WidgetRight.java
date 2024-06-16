package com.baidu.BaiduMap.Widget.FloatView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Insets;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.preference.PreferenceManager;

import com.baidu.BaiduMap.ConstantData.CarLinkData;
import com.baidu.BaiduMap.R;
import com.baidu.BaiduMap.Widget.Music.MusicUI;
import com.baidu.BaiduMap.carlifeapplauncher.adapter.Common;
import com.baidu.BaiduMap.carlifeapplauncher.adapter.FakeStart;

public class WidgetRight {

    private final Context context;
    private WindowManager wm;
    private LinearLayout navbar_wrapper;
    public static View navbar_view;
    public boolean shown = false;
    private int radius;
    public static WidgetRight navBar;

    public static WidgetRight getInstance() {
        return navBar;
    }

    public static WidgetRight createInstance(Context context) {
        if (navBar == null) {
            navBar = new WidgetRight(context);
        }
        return navBar;
    }

    public WidgetRight(Context context) {
        this.context = context;
        this.wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void onStart() {
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowMetrics windowMetrics = wm.getCurrentWindowMetrics();
        WindowInsets windowInsets = windowMetrics.getWindowInsets();
        Insets insets = windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars() | WindowInsets.Type.displayCutout());
        this.radius = (int) (insets.left * 0.55);
        navbar_view = View.inflate(context, R.layout.overlay_right, null);
        int opacity = (int) (Double.parseDouble(PreferenceManager.getDefaultSharedPreferences(context).getString("godmode_opacity", "100")) / 100 * 255);
        String rgb_string = PreferenceManager.getDefaultSharedPreferences(context).getString("godmode_rgb", "55#55#55");
        String[] rgb_array = rgb_string.split("#");
        int trans = Color.argb(opacity, Integer.parseInt(rgb_array[0]), Integer.parseInt(rgb_array[1]), Integer.parseInt(rgb_array[2]));
        navbar_view.setBackgroundColor(trans);
        navbar_view.setFocusable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(insets.left, WindowManager.LayoutParams.MATCH_PARENT,
                2038,
                262184,
                PixelFormat.TRANSLUCENT);

        lp.gravity = Gravity.RIGHT | Gravity.TOP;
        lp.setFitInsetsTypes(5);

        lp.token = null;
        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;

        lp.x = 0;
        lp.y = 0;

        navbar_wrapper = navbar_view.findViewById(R.id.godmodelist_right);

        constructView("navi");
        constructView("music");

        navbar_view.setMinimumHeight(Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("min_height", "0")));
        wm.addView(navbar_view, lp);

        WindowInsetsControllerCompat controllerCompat = ViewCompat.getWindowInsetsController(navbar_view);
        assert controllerCompat != null;
        controllerCompat.hide(WindowInsetsCompat.Type.statusBars());
        controllerCompat.hide(WindowInsetsCompat.Type.navigationBars());
        controllerCompat.hide(WindowInsetsCompat.Type.ime());
        controllerCompat.hide(WindowInsetsCompat.Type.systemBars());

        setVisibility(true);
        CardView constraintLayout1 = (CardView) navbar_wrapper.getChildAt(1);
        LinearLayout.LayoutParams constraintLayoutLayoutParams = (LinearLayout.LayoutParams) constraintLayout1.getLayoutParams();
        constraintLayoutLayoutParams.height = 350;
        constraintLayoutLayoutParams.topMargin = 22;
        constraintLayoutLayoutParams.gravity = Gravity.BOTTOM;
        constraintLayoutLayoutParams.weight = 1;
        constraintLayout1.setLayoutParams(constraintLayoutLayoutParams);

        CardView cardView1 = (CardView) navbar_wrapper.getChildAt(0);
        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) cardView1.getLayoutParams();
        layoutParams1.height = 72;
        layoutParams1.weight = 1;
        cardView1.setLayoutParams(layoutParams1);

        LinearLayout naviBg = navbar_wrapper.findViewById(R.id.widget_right);
//        if(CarLinkData.getBoolean(context,CarLinkData.sp_theme_enable_pic_navi,false)){
//            switch (CarLinkData.getString(context,CarLinkData.sp_theme_launcher_pic)){
//                case "0":
//                    naviBg.setBackground(context.getDrawable(R.drawable.inner_0));
//                    break;
//                case "1":
//                    naviBg.setBackground(context.getDrawable(R.drawable.inner_1));
//                    break;
//                case "2":
//                    naviBg.setBackground(context.getDrawable(R.drawable.inner_2));
//                    break;
//                case "3":
//                    naviBg.setBackground(context.getDrawable(R.drawable.inner_3));
//                    break;
//            }
//        } else
        if (!CarLinkData.getBoolean(context, CarLinkData.sp_theme_navi_color_use_default, true)) {
            naviBg.setBackgroundColor(CarLinkData.getInt(context, CarLinkData.sp_theme_color_navi, 0));
        } else {
            naviBg.setBackgroundColor(context.getColor(R.color.navi_bg));
        }

        shown = true;
    }

    private void constructView(String key) {
        switch (key) {
            case "navi":
                navi();
                return;
            case "music":
                music();
                return;
            default:
        }
    }

    private void music() {
        MusicUI musicUI = new MusicUI(context, (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE), navbar_wrapper);
        try {
            //ViewGroup viewGroup = (ViewGroup) navbar_wrapper.getView().findViewById(R.id.viewGroupId);
            //navbar_wrapper.addView(musicUI);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navi() {
        try {
            androidx.cardview.widget.CardView frame = (androidx.cardview.widget.CardView) View.inflate(context, R.layout.overlay_right_navi, null);
            frame.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            LinearLayout home = frame.findViewById(R.id.navi_home);
            LinearLayout company = frame.findViewById(R.id.navi_company);
            LinearLayout cruise = frame.findViewById(R.id.navi_cruise);
            home.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("WrongConstant")
                @Override
                public void onClick(View v) {

                    if (CarLinkData.getStringFromList(context, CarLinkData.sp_dock_map_pkg).equals(CarLinkData.pkgNameAmap)) {
                        FakeStart.StartMiniMapFunction(context, "amapuri://ucar/navi/common?addr=home&coord_type=bd09ll&src=com.samsung.car");
                    } else if (CarLinkData.getStringFromList(context, CarLinkData.sp_dock_map_pkg).equals(CarLinkData.pkgNameAmapAuto)) {
                        context.startActivity(new Intent(null, Uri.parse("androidauto://navi2SpecialDest?sourceApplication=" + context.getPackageName() + "&dest=home")));
                    }
                }
            });
            company.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("WrongConstant")
                @Override
                public void onClick(View v) {
                    if (CarLinkData.getStringFromList(context, CarLinkData.sp_dock_map_pkg).equals(CarLinkData.pkgNameAmap)) {
                        FakeStart.StartMiniMapFunction(context, "amapuri://ucar/navi/common?addr=company&coord_type=bd09ll&src=com.samsung.car");
                    } else if (CarLinkData.getStringFromList(context, CarLinkData.sp_dock_map_pkg).equals(CarLinkData.pkgNameAmapAuto)) {
                        context.startActivity(new Intent(null, Uri.parse("androidauto://navi2SpecialDest?sourceApplication=" + context.getPackageName() + "&dest=crop")));
                    }
                }
            });
            cruise.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("WrongConstant")
                @Override
                public void onClick(View v) {
                    if (CarLinkData.getStringFromList(context, CarLinkData.sp_dock_map_pkg).equals(CarLinkData.pkgNameAmap)) {
                        FakeStart.StartMiniMapFunction(context, "androidamap://openFeature?featureName=openTrafficEdog&clearStack=0&keepStack=1&sourceApplication=com.samsung.car");
                    } else if (CarLinkData.getStringFromList(context, CarLinkData.sp_dock_map_pkg).equals(CarLinkData.pkgNameAmapAuto)) {
                        context.startActivity(new Intent(null, Uri.parse("androidauto://navi2SpecialDest?sourceApplication=" + context.getPackageName() + "&dest=crop")));
                    }
                }
            });
            frame.setFocusable(false);
            navbar_wrapper.addView(frame);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "侧栏助手：无法获取应用", Toast.LENGTH_LONG).show();
        }
    }


    private void setVisibility(Boolean visible) {
        if (visible) {

            WindowManager.LayoutParams lp = (WindowManager.LayoutParams) navbar_view.getLayoutParams();
            //lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            //lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;

            SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(context);
            lp.width = Integer.parseInt(s.getString("overlay_right_width", "480"));
            lp.verticalWeight = 1;
            wm.updateViewLayout(navbar_view, lp);
            Common.setBgRadius(navbar_view, 0);
            navbar_wrapper.setVisibility(View.VISIBLE);


        } else {

            Common.setBgRadius(navbar_view, radius);
            navbar_wrapper.setVisibility(View.GONE);
            WindowManager.LayoutParams lp = (WindowManager.LayoutParams) navbar_view.getLayoutParams();
            lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            wm.updateViewLayout(navbar_view, lp);
        }
    }

    public void onDestroy() {
        if (shown) {
            try {
                wm.removeView(navbar_view);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        shown = false;
        navBar = null;
    }
}
