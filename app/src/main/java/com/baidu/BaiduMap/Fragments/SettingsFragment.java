package com.baidu.BaiduMap.Fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.apppickerview.widget.AppPickerView;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SeslSwitchPreferenceScreen;

import com.baidu.BaiduMap.ConstantData.CarLinkData;
import com.baidu.BaiduMap.Dialog.AppSelectDialog;
import com.baidu.BaiduMap.Dialog.DonateDialog;
import com.baidu.BaiduMap.R;
import com.baidu.BaiduMap.SettingsActivity;
import com.baidu.BaiduMap.Utils.ApplicationUtil;
import com.baidu.BaiduMap.Utils.Language.MultiLanguages;
import com.baidu.BaiduMap.base.FragmentInfo;

import java.util.Locale;

import dev.oneuiproject.oneui.preference.internal.PreferenceRelatedCard;
import dev.oneuiproject.oneui.utils.PreferenceUtils;

public class SettingsFragment extends PreferenceFragmentCompat
        implements FragmentInfo, Preference.OnPreferenceClickListener,
        Preference.OnPreferenceChangeListener {
    private Context mContext;
    private PreferenceRelatedCard mRelativeLinkCard;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
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
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getView().setBackgroundColor(mContext.getColor(R.color.oui_background_color));
        getListView().seslSetLastRoundedCorner(false);
        initPreferences();
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


    @Override
    public int getLayoutResId() {
        return -1;
    }

    @Override
    public int getIconResId() {
        return R.drawable.ic_oui_settings_outline;
    }

    @Override
    public CharSequence getTitle() {
        return "Preferences";
    }

    @Override
    public boolean isAppBarEnabled() {
        return true;
    }

    public void initPreferences() {

        ListPreference listPreference = findPreference("language");
        listPreference.seslSetSummaryColor(getColoredSummaryColor(true));
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String newVal = (String) newValue;
                switch (newVal){
                    case "0":
                        MultiLanguages.setAppLanguage(mContext,MultiLanguages.getSystemLanguage());
                        MultiLanguages.clearAppLanguage(mContext);
                        getActivity().recreate();
                        return true;
                    case "1":
                        MultiLanguages.setAppLanguage(mContext, Locale.ENGLISH);
                        getActivity().recreate();
                        return true;
                    case "2":
                        MultiLanguages.setAppLanguage(mContext,Locale.SIMPLIFIED_CHINESE);
                        getActivity().recreate();
                        return true;
                }
                return false;
            }
        });
        PreferenceScreen donate = findPreference("donate");
        donate.setOnPreferenceClickListener(this);
        PreferenceScreen about = findPreference("about");
        about.setOnPreferenceClickListener(this);

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        switch (key) {
            case "language":
                return true;
            case "about":
                SettingsActivity.switchFragment(4);
                return true;
        }

        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Boolean enabled;
        switch (preference.getKey()) {
            case "language":
                enabled = (Boolean) newValue;
                return true;
        }

        return false;
    }
    private ColorStateList getColoredSummaryColor(boolean enabled) {
        if (enabled) {
            TypedValue colorPrimaryDark = new TypedValue();
            mContext.getTheme().resolveAttribute(
                    R.attr.colorPrimaryDark, colorPrimaryDark, true);

            int[][] states = new int[][] {
                    new int[] {android.R.attr.state_enabled},
                    new int[] {-android.R.attr.state_enabled}
            };
            int[] colors = new int[] {
                    Color.argb(0xff,
                            Color.red(colorPrimaryDark.data),
                            Color.green(colorPrimaryDark.data),
                            Color.blue(colorPrimaryDark.data)),
                    Color.argb(0x4d,
                            Color.red(colorPrimaryDark.data),
                            Color.green(colorPrimaryDark.data),
                            Color.blue(colorPrimaryDark.data))
            };
            return new ColorStateList(states, colors);
        } else {
            TypedValue outValue = new TypedValue();
            mContext.getTheme().resolveAttribute(
                    R.attr.isLightTheme, outValue, true);
            return mContext.getColorStateList(outValue.data != 0
                    ? R.color.sesl_secondary_text_light
                    : R.color.sesl_secondary_text_dark);
        }
    }

}
