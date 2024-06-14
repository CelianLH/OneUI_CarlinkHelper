package com.baidu.BaiduMap;

import static android.app.PendingIntent.getActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.baidu.BaiduMap.ConstantData.CarLinkData;
import com.baidu.BaiduMap.Dialog.DonateDialog;
import com.baidu.BaiduMap.Dialog.FirstBootDialog;
import com.baidu.BaiduMap.Fragments.AboutFragment;
import com.baidu.BaiduMap.Fragments.AppsFragment;
import com.baidu.BaiduMap.Fragments.OverlayFragment;
import com.baidu.BaiduMap.Fragments.SettingsFragment;
import com.baidu.BaiduMap.Fragments.ThemeFragment;
import com.baidu.BaiduMap.Utils.ApplicationUtil;
import com.baidu.BaiduMap.Utils.ToastUtil;
import com.baidu.BaiduMap.databinding.ActivitySettingsBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import dev.oneuiproject.oneui.layout.ToolbarLayout;


public class SettingsActivity extends AppCompatActivity {
    private Context mContex;
    public static ActivitySettingsBinding mBinding;
    public static FragmentManager mFragmentManager;
    private ToolbarLayout mToolbarLayout;
    private static final List<Fragment> fragments = new ArrayList<>();
    private static int preFragments = 0;
    private static int homeFragments = 0;
    private boolean booted = false;
    private static boolean isHome = true;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContex = this;
        mBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initFragmentList();
        initFragments();
        initTab();
        mToolbarLayout = mBinding.toolbarLayout;
        ActionBar actionBar = this.getSupportActionBar();
        mBinding.toolbarLayout.setNavigationButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onKeyDown(KeyEvent.KEYCODE_BACK, null);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (BaseApplication.isAfterSetTheme) {
            mBinding.tabsTabs.selectTab(mBinding.tabsTabs.getTabAt(2));
        } else if (!booted) {
            switchFragment(0);
            booted = true;
        }
        if( !CarLinkData.getBoolean(this, "confirmed", false)){
            FirstBootDialog firstBootDialog;
            firstBootDialog = new FirstBootDialog();
            firstBootDialog.setCancelable(false);
            firstBootDialog.show(this.getSupportFragmentManager(),"");
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            switchFragment(3);
            if (mBinding.tabsTabs.getVisibility() == View.VISIBLE) {
                mBinding.tabsTabs.setVisibility(View.GONE);
                mBinding.toolbarLayout.setNavigationButtonVisible(true);
            }
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean ret = false;
        ret = activityParseOnkey(keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (isHome) {
                    super.onKeyDown(keyCode, event);
                    return true;
                } else if (preFragments == 4) {
                    switchFragment(3);
                } else if (preFragments == 3) {
                    switchFragment(homeFragments);
                    if (mBinding.tabsTabs.getVisibility() == View.GONE) {
                        mBinding.tabsTabs.setVisibility(View.VISIBLE);
                        mBinding.toolbarLayout.setNavigationButtonVisible(false);
                    }
                }

        }

        return ret;
    }

    private boolean activityParseOnkey(int keyCode) {
        boolean ret = false;
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                ret = true;
                break;
        }
        return ret;
    }

    private void initFragmentList() {
        ApplicationUtil.initAppList(this);
        fragments.add(new OverlayFragment());
        fragments.add(new AppsFragment());
        fragments.add(new ThemeFragment());
        fragments.add(new SettingsFragment());
        fragments.add(new AboutFragment());
    }

    private void initFragments() {
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        for (Fragment fragment : fragments) {
            if (fragment != null) transaction.add(R.id.main_content, fragment);
        }
        transaction.commit();

        //onDrawerItemSelected(0);
    }

    private void initTab() {
        mBinding.tabsTabs.addTab(mBinding.tabsTabs.newTab().setText(getText(R.string.tab_overlay)));
        mBinding.tabsTabs.addTab(mBinding.tabsTabs.newTab().setText(getText(R.string.tab_apps)));
        mBinding.tabsTabs.addTab(mBinding.tabsTabs.newTab().setText(getText(R.string.tab_theme)));
        mBinding.tabsTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int i = tab.getPosition();
                switch (i) {
                    case 0:
                        switchFragment(0);
                        break;
                    case 1:
                        switchFragment(1);
                        break;
                    case 2:
                        switchFragment(2);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                onTabSelected(tab);
            }
        });
    }

    public static void switchFragment(int position) {
        if (position <= fragments.size() - 1) {
            Fragment newFragment = fragments.get(position);
            if (newFragment != null) {
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                for (Fragment fragment : mFragmentManager.getFragments()) {
                    transaction.hide(fragment);
                }
                transaction.show(newFragment).commit();
                if (position != 4 && position != 3) {
                    homeFragments = position;
                    isHome=true;
                }else {
                    isHome = false;
                }
                if(position==4){
                    preFragments = position;
                }else if (preFragments != 3) {
                    preFragments = position;
                }
            }
        }
    }
}