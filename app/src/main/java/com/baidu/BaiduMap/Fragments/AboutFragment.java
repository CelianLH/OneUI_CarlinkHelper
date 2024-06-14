package com.baidu.BaiduMap.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.baidu.BaiduMap.Dialog.AppSelectDialog;
import com.baidu.BaiduMap.R;
import com.baidu.BaiduMap.Utils.DonateUtils;
import com.baidu.BaiduMap.Utils.Language.MultiLanguages;
import com.baidu.BaiduMap.Utils.ToastUtil;
import com.baidu.BaiduMap.base.FragmentInfo;

import java.util.Locale;

import dev.oneuiproject.oneui.preference.internal.PreferenceRelatedCard;

public class AboutFragment extends Fragment {
    private Context mContext;
    private View view;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();//清除原有菜单选项
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about, container, false);
        AppCompatButton btnDev = view.findViewById(R.id.developer_btn);
        AppCompatButton btnGit = view.findViewById(R.id.github_btn);
        btnDev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setData(Uri.parse("coolmarket://u/657119"));
                    mContext.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtil.showToast(mContext, "自动跳转失败,请先安装酷安APP");
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://www.coolapk.com/u/657119"));
                    mContext.startActivity(intent);
                }
            }
        });
        btnGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("https://github.com/CelianLH/OneUI_CarlinkHelper"));
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        //setRelativeLinkCard();
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
//            SettingsActivity.mBinding.tabsTabsBg.setVisibility(View.GONE);
//            SettingsActivity.mBinding.toolbarLayout.setNavigationButtonVisible(true);
        }
    }
}
