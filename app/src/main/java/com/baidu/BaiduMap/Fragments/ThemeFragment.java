package com.baidu.BaiduMap.Fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.util.SeslMisc;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SeslSwitchPreferenceScreen;
import androidx.preference.SwitchPreferenceCompat;

import com.baidu.BaiduMap.BaseApplication;
import com.baidu.BaiduMap.R;
import com.baidu.BaiduMap.Utils.HorizontalRadioPreferenceTheme;
import com.baidu.BaiduMap.Utils.Theme.NightModeUtils;
import com.baidu.BaiduMap.Utils.Theme.ThemeMode;
import com.baidu.BaiduMap.base.FragmentInfo;

import com.baidu.BaiduMap.Utils.HorizontalRadioPreference;
import dev.oneuiproject.oneui.preference.TipsCardPreference;
import dev.oneuiproject.oneui.preference.internal.PreferenceRelatedCard;
import dev.oneuiproject.oneui.utils.PreferenceUtils;
import dev.oneuiproject.oneui.widget.Toast;

public class ThemeFragment extends PreferenceFragmentCompat
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
        setPreferencesFromResource(R.xml.settings_theme, rootKey);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initPreferences();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getView().setBackgroundColor(mContext.getColor(R.color.oui_background_color));
        getListView().seslSetLastRoundedCorner(false);
    }

    @Override
    public void onResume() {
        setRelativeLinkCard();
        super.onResume();
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

    private void initPreferences() {

        ThemeMode darkMode = NightModeUtils.getInstance(mContext).getThemeMode();

        HorizontalRadioPreferenceTheme darkModePref = findPreference("dark_mode");
        darkModePref.setOnPreferenceChangeListener(this);
        darkModePref.setDividerEnabled(false);
        darkModePref.setTouchEffectEnabled(false);
        darkModePref.setEnabled(darkMode != ThemeMode.MODE_FOLLOW_SYSTEM);
        darkModePref.setValue(SeslMisc.isLightTheme(mContext) ? "0" : "1");

        SwitchPreferenceCompat autoDarkModePref = findPreference("dark_mode_auto");
        autoDarkModePref.setOnPreferenceChangeListener(this);
        autoDarkModePref.setChecked(darkMode == ThemeMode.MODE_FOLLOW_SYSTEM);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals("key2")) {
            Toast.makeText(mContext, "onPreferenceClick", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        HorizontalRadioPreferenceTheme darkModePref = (HorizontalRadioPreferenceTheme) findPreference("dark_mode");

        switch (preference.getKey()) {
            case "dark_mode":
                    NightModeUtils.getInstance(mContext).setThemeMode( ((String) newValue).equals("0")
                            ? ThemeMode.MODE_ALWAYS_OFF
                            : ThemeMode.MODE_ALWAYS_ON);
                BaseApplication.isAfterSetTheme=true;
                NightModeUtils.getInstance(mContext).applySetting();
                return true;
            case "dark_mode_auto":
                BaseApplication.isAfterSetTheme=true;
                if ((boolean) newValue) {
                    darkModePref.setEnabled(false);
                    NightModeUtils.getInstance(mContext).setThemeMode(ThemeMode.MODE_FOLLOW_SYSTEM);
                    NightModeUtils.getInstance(mContext).applySetting();
                } else {
                    darkModePref.setEnabled(true);
                }
                return true;
            case "key2":
                Boolean enabled = (Boolean) newValue;
                preference.setSummary(enabled ? "Enabled" : "Disabled");
                preference.seslSetSummaryColor(getColoredSummaryColor(enabled));
                return true;
            case "key4":
                String text = (String) newValue;
                preference.seslSetSummaryColor(getColoredSummaryColor(text.length() > 0));
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

    private void setRelativeLinkCard() {
//        if (mRelativeLinkCard == null) {
//            mRelativeLinkCard = PreferenceUtils.createRelatedCard(mContext);
//            mRelativeLinkCard.addButton("This", null)
//                    .addButton("That", null)
//                    .addButton("There", null)
//                    .show(this);
//        }
    }
}
