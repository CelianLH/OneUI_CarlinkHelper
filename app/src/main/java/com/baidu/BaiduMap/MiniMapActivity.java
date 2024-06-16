package com.baidu.BaiduMap;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.baidu.BaiduMap.ConstantData.CarLinkData;
import com.baidu.BaiduMap.Utils.ApplicationUtil;
import com.baidu.BaiduMap.carlifeapplauncher.adapter.FakeStart;
import com.baidu.BaiduMap.carlifeapplauncher.adapter.OpenProvider;
import com.baidu.BaiduMap.deeplink.DeepLinkService;

import java.util.Objects;

public class MiniMapActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_mini_map);
        ApplicationUtil.initAppList(this);
        ApplicationUtil.initSettings(this);
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onResume() {
        super.onResume();
        if(Objects.equals(CarLinkData.getStringFromList(this, CarLinkData.sp_dock_map_pkg), CarLinkData.pkgNameAmap)){
            FakeStart.StartMiniMap(this);
        }else {
            FakeStart.StartCommon(this,CarLinkData.getStringFromList(this,CarLinkData.sp_dock_map_pkg));
        }



        if (OpenProvider.isConnected(this)) {
            if (DeepLinkService.getInstance() == null) {
                String uri = "banqumusic://deeplinkservice/";
                Uri parse = Uri.parse("samsungcarlink://link/----" + uri);
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(parse);
                intent.addFlags(0x10104000);
                startActivity(intent);
            }else {
                DeepLinkService.getInstance().resume();
            }
        }
    }

}