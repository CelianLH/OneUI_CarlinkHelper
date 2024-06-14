package com.baidu.BaiduMap.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SeslProgressBar;
import androidx.appcompat.widget.SwitchCompat;
import androidx.apppickerview.widget.AppPickerView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.baidu.BaiduMap.ConstantData.CarLinkData;
import com.baidu.BaiduMap.Fragments.AppsFragment;
import com.baidu.BaiduMap.R;
import com.baidu.BaiduMap.SettingsActivity;
import com.baidu.BaiduMap.Utils.ApplicationUtil;
import com.baidu.BaiduMap.Utils.PreferenceUtil;
import com.baidu.BaiduMap.Utils.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dev.oneuiproject.oneui.widget.Toast;

public class AppSelectDialog extends DialogFragment implements AppPickerView.OnBindListener, AdapterView.OnItemSelectedListener {
    private Context mContext;
    private AppPickerView mAppPickerView;
    private AppPickerView mAppPickerView_selected;
    private SeslProgressBar mProgress;
    private TextView mTextView;
    private List<Boolean> mItems = new ArrayList<>();
    private int mPickerType;
    private String mPreferenceKey;

    private int mCheckedPosition = -1;
    private ArrayList<String> installedAppSet = new ArrayList<>();
    private ArrayList<String> selectedAppSet = new ArrayList<>();
    private AppsFragment mAppsFragment;

    private DialogListener dialogListener = new DialogListener() {
        @Override
        public void onResult() {

        }
    };

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    public AppSelectDialog(int pickerType, String preferenceKey, AppsFragment appsFragment) {
        mPickerType = pickerType;
        mPreferenceKey = preferenceKey;
        mAppsFragment = appsFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContext = getActivity();
        View root_view = getLayoutInflater().inflate(R.layout.dialog_appselect, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setTitle(R.string.title_dialog_pick_app)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                                CarLinkData.putArrayList(mContext, mPreferenceKey, selectedAppSet);

                        mAppsFragment.initPreferences();
                    }
                })
                .setView(root_view);
        mProgress = root_view.findViewById(R.id.apppicker_progress);
        mAppPickerView = root_view.findViewById(R.id.apppicker_list);
        mTextView = root_view.findViewById(R.id.apppicker_list_tip_text);
        mAppPickerView_selected = root_view.findViewById(R.id.apppicker_list_type);
        mAppPickerView.setItemAnimator(null);
        mAppPickerView.seslSetSmoothScrollEnabled(true);
        mAppPickerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        refreshListView();
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void showProgressCircle(boolean show) {
        mProgress.setVisibility(show ? View.VISIBLE : View.GONE);
        mAppPickerView.setVisibility(show ? View.GONE : View.VISIBLE);
    }


    private void refreshListView() {
        showProgressCircle(true);
        new Thread() {
            @Override
            public void run() {
                requireActivity().runOnUiThread(() -> {
                    installedAppSet.clear();
                    mItems.clear();
                    mCheckedPosition = -1;
                    installedAppSet
                            = new ArrayList<>(getInstalledPackageNameUnmodifiableSet());
                    mAppPickerView.setAppPickerView(mPickerType,
                            installedAppSet, AppPickerView.ORDER_ASCENDING_IGNORE_CASE);
                    mItems.clear();
                    ArrayList<String> stringArrayList = new ArrayList<>(CarLinkData.getArrayList(mContext, mPreferenceKey));
                    int size = installedAppSet.size();
                    installedAppSet.clear();
                    for(int i=0;i<=size-1;i++ ){
                        installedAppSet.add(mAppPickerView.getAppLabelInfo(i).getPackageName());
                    }
                    int i = 0;
                    for (String string : installedAppSet) {
                        if (stringArrayList.contains(string)) {
                            mItems.add(Boolean.TRUE);
                            if(mPickerType==AppPickerView.TYPE_LIST_RADIOBUTTON){
                                mCheckedPosition=i;
                            }
                        } else {
                            mItems.add(Boolean.FALSE);
                        }
                        i+=1;
                    }
                    mAppPickerView.setOnBindListener(AppSelectDialog.this);
                    showProgressCircle(false);
                    refreshSelectedListView();
                });
            }
        }.start();
    }

    private void refreshSelectedListView() {
        new Thread() {
            @Override
            public void run() {
                requireActivity().runOnUiThread(() -> {
                    if (selectedAppSet.size() == 0) {
                        selectedAppSet
                                = new ArrayList<>(CarLinkData.getArrayList(mContext, mPreferenceKey));
                    }
                    if (selectedAppSet.size() == 0) {
                        mTextView.setVisibility(View.VISIBLE);
                    } else {
                        mTextView.setVisibility(View.GONE);
                    }
                    mAppPickerView_selected.setAppPickerView(AppPickerView.TYPE_GRID,
                            selectedAppSet, AppPickerView.ORDER_ASCENDING_IGNORE_CASE);
                });
            }
        }.start();
    }

    private Set<String> getInstalledPackageNameUnmodifiableSet() {
        HashSet<String> set = new HashSet<>();
        for (ApplicationInfo appInfo : ApplicationUtil.getALl(mContext)) {
            set.add(appInfo.packageName);
        }
        return Collections.unmodifiableSet(set);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onBindViewHolder(AppPickerView.ViewHolder holder, int position, String packageName) {
        switch (mPickerType) {
            case AppPickerView.TYPE_LIST, AppPickerView.TYPE_GRID: {
                holder.getItem().setOnClickListener(view -> {
                });
            }
            break;

            case AppPickerView.TYPE_LIST_CHECKBOX: {
                CheckBox checkBox = holder.getCheckBox();
                checkBox.setChecked(mItems.get(position));
                checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        mItems.set(position, isChecked);
                        selectedAppSet.add(mAppPickerView.getAppLabelInfo(position).getPackageName());
                    } else {
                        int i = 0;
                        for (String string : selectedAppSet) {
                            if (string.equals(mAppPickerView.getAppLabelInfo(position).getPackageName())) {
                                selectedAppSet.remove(i);
                                break;
                            } else {
                                i += 1;
                            }
                        }
                    }
                    refreshSelectedListView();
                });
                break;
            }


            case AppPickerView.TYPE_LIST_RADIOBUTTON: {
                RadioButton radioButton = holder.getRadioButton();
                radioButton.setChecked(mItems.get(position));

                final boolean[] f = {false};
                holder.getItem().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        f[0]=true;
                        if(holder.getRadioButton().isChecked()){
                            holder.getRadioButton().setChecked(false);
                        }else {
                            holder.getRadioButton().setChecked(true);
                        }
                    }
                });
                radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked  && f[0]) {

                        if ((mCheckedPosition != position)&& (mCheckedPosition!=-1)) {
                            mItems.set(mCheckedPosition, false);
                            mAppPickerView.refreshUI(mCheckedPosition);
                            mItems.set(position, true);
                            mCheckedPosition = position;
                            selectedAppSet.clear();
                            selectedAppSet.add(installedAppSet.get(position));
                            refreshSelectedListView();
                        }


                    }
                });
                break;
            }


        }
    }


    public interface DialogListener {
        void onResult();
    }
}
