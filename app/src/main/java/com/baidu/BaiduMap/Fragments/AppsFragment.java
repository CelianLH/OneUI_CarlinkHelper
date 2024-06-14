package com.baidu.BaiduMap.Fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.util.SeslMisc;
import androidx.apppickerview.widget.AppPickerView;
import androidx.preference.DropDownPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SeslSwitchPreferenceScreen;
import androidx.preference.SwitchPreferenceCompat;

import com.baidu.BaiduMap.ConstantData.CarLinkData;
import com.baidu.BaiduMap.Dialog.AppSelectDialog;
import com.baidu.BaiduMap.R;
import com.baidu.BaiduMap.Utils.ApplicationUtil;
import com.baidu.BaiduMap.Utils.Theme.NightModeUtils;
import com.baidu.BaiduMap.Utils.Theme.ThemeMode;
import com.baidu.BaiduMap.base.FragmentInfo;

import dev.oneuiproject.oneui.preference.HorizontalRadioPreference;
import dev.oneuiproject.oneui.preference.TipsCardPreference;
import dev.oneuiproject.oneui.preference.internal.PreferenceRelatedCard;
import dev.oneuiproject.oneui.utils.PreferenceUtils;
import dev.oneuiproject.oneui.widget.Toast;

public class AppsFragment extends PreferenceFragmentCompat
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
        setPreferencesFromResource(R.xml.settings_apps, rootKey);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
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

        EditTextPreference iconSizeText = findPreference(CarLinkData.sp_launcher_icon_size);

        if (iconSizeText != null) {
            iconSizeText.setOnBindEditTextListener(
                    editText -> editText.setInputType(InputType.TYPE_CLASS_NUMBER ));
        }
        SeslSwitchPreferenceScreen dockMusic = findPreference(CarLinkData.sp_dock_music);
        boolean enabled = dockMusic.isChecked();
        dockMusic.setSummary(ApplicationUtil.getName(mContext,CarLinkData.getStringFromList(mContext,CarLinkData.sp_dock_music_pkg)));
        dockMusic.seslSetSummaryColor(getColoredSummaryColor(enabled));
        dockMusic.setOnPreferenceClickListener(this);
        dockMusic.setOnPreferenceChangeListener(this);

        SeslSwitchPreferenceScreen dockMap = findPreference(CarLinkData.sp_dock_map);
        enabled = dockMap.isChecked();
        dockMap.setSummary(ApplicationUtil.getName(mContext,CarLinkData.getStringFromList(mContext,CarLinkData.sp_dock_map_pkg)));
        dockMap.seslSetSummaryColor(getColoredSummaryColor(enabled));
        dockMap.setOnPreferenceClickListener(this);
        dockMap.setOnPreferenceChangeListener(this);

        SeslSwitchPreferenceScreen dockAny = findPreference(CarLinkData.sp_dock_any);
        enabled = dockAny.isChecked();
        dockAny.setSummary(ApplicationUtil.getName(mContext,CarLinkData.getStringFromList(mContext,CarLinkData.sp_dock_any_pkg)));
        dockAny.seslSetSummaryColor(getColoredSummaryColor(enabled));
        dockAny.setOnPreferenceClickListener(this);
        dockAny.setOnPreferenceChangeListener(this);

        PreferenceScreen appLauncher = findPreference(CarLinkData.sp_launcher_apps);
        appLauncher.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        AppSelectDialog appSelectDialog;
        switch (key){
            case CarLinkData.sp_dock_music:
                appSelectDialog = new AppSelectDialog(AppPickerView.TYPE_LIST_RADIOBUTTON,CarLinkData.sp_dock_music_pkg,this);
                appSelectDialog.setCancelable(true);
                appSelectDialog.show(getActivity().getSupportFragmentManager(),"");
                return true;
            case CarLinkData.sp_dock_map:
                appSelectDialog = new AppSelectDialog(AppPickerView.TYPE_LIST_RADIOBUTTON,CarLinkData.sp_dock_map_pkg,this);
                appSelectDialog.setCancelable(true);
                appSelectDialog.show(getActivity().getSupportFragmentManager(),"");
                return true;
            case CarLinkData.sp_dock_any:
                appSelectDialog = new AppSelectDialog(AppPickerView.TYPE_LIST_RADIOBUTTON,CarLinkData.sp_dock_any_pkg,this);
                appSelectDialog.setCancelable(true);
                appSelectDialog.show(getActivity().getSupportFragmentManager(),"");
                return true;
            case CarLinkData.sp_launcher_apps:
                appSelectDialog = new AppSelectDialog(AppPickerView.TYPE_LIST_CHECKBOX,CarLinkData.sp_launcher_apps,this);
                appSelectDialog.setCancelable(true);
                appSelectDialog.show(getActivity().getSupportFragmentManager(),"");
                return true;
        }

        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Boolean enabled;
        switch (preference.getKey()) {
            case CarLinkData.sp_dock_music:
                enabled = (Boolean) newValue;
                preference.seslSetSummaryColor(getColoredSummaryColor(enabled));
                return true;
            case CarLinkData.sp_dock_map:
                enabled = (Boolean) newValue;
                preference.seslSetSummaryColor(getColoredSummaryColor(enabled));
                return true;
            case CarLinkData.sp_dock_any:
                enabled = (Boolean) newValue;
                preference.seslSetSummaryColor(getColoredSummaryColor(enabled));
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
        if (mRelativeLinkCard == null) {
            mRelativeLinkCard = PreferenceUtils.createRelatedCard(mContext);
            mRelativeLinkCard.addButton("This", null)
                    .addButton("That", null)
                    .addButton("There", null)
                    .show(this);
        }
    }
}
