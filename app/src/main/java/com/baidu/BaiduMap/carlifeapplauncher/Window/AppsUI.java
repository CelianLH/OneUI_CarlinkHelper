package com.baidu.BaiduMap.carlifeapplauncher.Window;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.BaiduMap.ConstantData.CarLinkData;
import com.baidu.BaiduMap.R;
import com.baidu.BaiduMap.databinding.AppsBinding;
import com.baidu.BaiduMap.RecyclerAdapter.FullAppListAdapter;
import com.baidu.BaiduMap.RecyclerAdapter.FullAppListAdapterMode;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AppsUI {
    private RecyclerView favodrawer;
    private AppsBinding binding;
    private ConstraintLayout constraintLayout;

    public AppsUI(Context context, LayoutInflater layoutInflater, ViewGroup root) {

        binding = AppsBinding.inflate(layoutInflater);
        root.addView(binding.getRoot());

        favodrawer = binding.appdrawer;
        constraintLayout = binding.launcherBack;
        if(CarLinkData.getBoolean(context,CarLinkData.sp_theme_enable_pic_launcher,true)){
            Drawable drawable = context.getDrawable(R.drawable.inner_0);;
            switch (CarLinkData.getString(context,CarLinkData.sp_theme_launcher_pic)){
                case "0":
                    drawable = context.getDrawable(R.drawable.inner_0);
                    break;
                case "1":
                    drawable = context.getDrawable(R.drawable.inner_1);
                    break;
                case "2":
                    drawable = context.getDrawable(R.drawable.inner_2);
                    break;
                case "3":
                    drawable = context.getDrawable(R.drawable.inner_3);
                    break;
            }
            if(!CarLinkData.getBoolean(context,CarLinkData.sp_overlay_left_dock_corner_always,false)){
                assert drawable != null;
                drawable.setAlpha(30);
            }
            constraintLayout.setBackground(drawable);
        } else if (!CarLinkData.getBoolean(context,CarLinkData.sp_theme_launcher_color_use_default,true)) {
            constraintLayout.setBackgroundColor(CarLinkData.getInt(context,CarLinkData.sp_theme_color_launcher,0));
        }
        FullAppListAdapter favadapter;

            favadapter = new FullAppListAdapter(context);


        favodrawer.setAdapter(favadapter);

        int col = CarLinkData.getInt(context,CarLinkData.sp_launcher_colum,5);
        favodrawer.setLayoutManager(new GridLayoutManager(context, col, RecyclerView.VERTICAL, false));

        if (favadapter.getItemCount() == 0) {
            binding.appdrawer.setVisibility(View.GONE);
            binding.textView13.setVisibility(View.VISIBLE);
        } else {
            binding.appdrawer.setVisibility(View.VISIBLE);
            binding.textView13.setVisibility(View.GONE);
        }
    }

}
