package com.baidu.BaiduMap.deeplink;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.baidu.BaiduMap.carlifeapplauncher.adapter.FakeStart;

public class Deeplink extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String uri = getIntent().getData().toString();
        uri = uri.substring(20);
        String[] data = uri.split("/");
        String pkg = data[0];
        FakeStart.Start(this, pkg);
        finish();
    }
}