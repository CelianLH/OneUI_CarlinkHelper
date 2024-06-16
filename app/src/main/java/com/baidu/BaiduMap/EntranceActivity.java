package com.baidu.BaiduMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.baidu.BaiduMap.ConstantData.CarLinkData;
import com.baidu.BaiduMap.Utils.ApplicationUtil;
import com.baidu.BaiduMap.Utils.LocationUtil;
import com.baidu.BaiduMap.Utils.ToastUtil;
import com.baidu.BaiduMap.carlifeapplauncher.adapter.FakeStart;

import java.util.Objects;

public class EntranceActivity extends AppCompatActivity {

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_entrance);
        ApplicationUtil.initAppList(this);
        ApplicationUtil.initSettings(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onResume() {
        super.onResume();
        if (CarLinkData.getStringFromList(this, CarLinkData.sp_dock_map_pkg).equals(CarLinkData.pkgNameAmap)) {
            FakeStart.StartMiniMapBar(this);
        }else {
            FakeStart.Start(this,CarLinkData.getStringFromList(this,CarLinkData.sp_dock_map_pkg));
        }
        //FakeStart.StartMiniMapToContext2(this);
        if (getIntent().getData() != null) {

            String uri = Objects.requireNonNull(getIntent().getData()).toString();
            Log.i("909090", uri);
            if (uri.startsWith("baidumap://hicarmap/direction")) {
                Uri u = Uri.parse(uri);
                String dest = u.getQueryParameter("destination");
                assert dest != null;
                String[] arr = dest.split("\\|");
                String dest_String = arr[0].substring(5);
                Log.i("MAPTESTING", "bar:dest_String:" + dest_String);
                String dest_po = arr[1].substring(7);
                String[] dest_po_arr = dest_po.split(",");
                double[] doubles = LocationUtil.bd09_To_Gcj02(Double.parseDouble(dest_po_arr[0]), Double.parseDouble(dest_po_arr[1]));
                String dest_po_la = Double.toString(doubles[0]);
                Log.i("MAPTESTING", "bar:dest_po_la:" + dest_po_la);
                String dest_po_ln = Double.toString(doubles[1]);
                Log.i("MAPTESTING", "bar:dest_po_ln:" + dest_po_ln);

//                openMapOperation(this,"amapuri://route/plan/?sname=我的位置&did="+ "&dlat=" +
//                        dest_po_la + "&dlon=" + dest_po_ln + "&dname=" + dest_String.replace("name:","") + "&dev= " + 0 + "&t=" + 0+"&sourceApplication="+ getPackageName() );
                //openMapOperation(this,"amapuri://ucar/navi?location="+dest_po_la+","+dest_po_ln+ "&keepStack=1&clearStack=0&sourceApplication=com.oplus.ocar");

                if (CarLinkData.getStringFromList(this, CarLinkData.sp_dock_map_pkg).equals(CarLinkData.pkgNameAmap)) {
                    startActivity(new Intent(null, Uri.parse("amapuri://ucar/navi?location=" + dest_po_la + "," + dest_po_ln + "&keepStack=1&clearStack=0&sourceApplication=com.samsung.car")));
                } else if (CarLinkData.getStringFromList(this, CarLinkData.sp_dock_map_pkg).equals(CarLinkData.pkgNameAmapAuto)) {
                    openMapOperation("androidauto://route?sourceApplication=" + getPackageName() + "&dlat=" +
                            dest_po_la + "&dlon=" + dest_po_ln + "&dname=" + dest_String + "&dev= " + 0 + "&m=" + -1);
                }

            }
            if (uri.startsWith("baidumap://hicarmap/navi?")) {
                Uri u = Uri.parse(uri);
                String dest = u.getQueryParameter("location");
                assert dest != null;
                String[] arr = dest.split(",");
                double[] doubles = LocationUtil.bd09_To_Gcj02(Double.parseDouble(arr[0]), Double.parseDouble(arr[1]));
                String dest_po_la = Double.toString(doubles[0]);
                Log.i("MAPTESTING", "bar:dest_po_la:" + dest_po_la);
                String dest_po_ln = Double.toString(doubles[1]);
                Log.i("MAPTESTING", "bar:dest_po_ln:" + dest_po_ln);

//
                if (CarLinkData.getStringFromList(this, CarLinkData.sp_dock_map_pkg).equals(CarLinkData.pkgNameAmap)) {
                    startActivity(new Intent(null, Uri.parse("amapuri://ucar/navi?location=" + dest_po_la + "," + dest_po_ln + "&keepStack=1&clearStack=0&sourceApplication=com.samsung.car")));
                } else if (CarLinkData.getStringFromList(this, CarLinkData.sp_dock_map_pkg).equals(CarLinkData.pkgNameAmapAuto)) {
                    openMapOperation("androidauto://navi?lat=" + dest_po_la + "&lon=" + dest_po_ln + "&dev=1&style=2&sourceApplication=" + getPackageName());

                }
            }
            if (uri.startsWith("baidumap://hicarmap/navi/common?addr=home")) {
                if (CarLinkData.getStringFromList(this, CarLinkData.sp_dock_map_pkg).equals(CarLinkData.pkgNameAmap)) {
                    startActivity(new Intent(null, Uri.parse("amapuri://ucar/navi/common?addr=home&coord_type=bd09ll&src=com.samsung.car")));
                } else if (CarLinkData.getStringFromList(this, CarLinkData.sp_dock_map_pkg).equals(CarLinkData.pkgNameAmapAuto)) {
                    openMapOperation("androidauto://navi2SpecialDest?sourceApplication=" + getPackageName() + "&dest=home");
                }
            } else if (uri.startsWith("baidumap://hicarmap/navi/common?addr=company")) {
                if (CarLinkData.getStringFromList(this, CarLinkData.sp_dock_map_pkg).equals(CarLinkData.pkgNameAmap)) {
                    startActivity(new Intent(null, Uri.parse("amapuri://ucar/navi/common?addr=company&coord_type=bd09ll&src=com.samsung.car")));
                } else if (CarLinkData.getStringFromList(this, CarLinkData.sp_dock_map_pkg).equals(CarLinkData.pkgNameAmapAuto)) {
                    openMapOperation("androidauto://navi2SpecialDest?sourceApplication=" + getPackageName() + "&dest=crop");
                }
            }
            setIntent(null);
        }
    }

    private void openMapOperation(String url) {
        //FakeStart.StartMiniMapFunction(context,url);
        Intent intent = new Intent("android.intent.action.VIEW",
                android.net.Uri.parse(url));
        intent.setPackage("com.autonavi.amapauto");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}