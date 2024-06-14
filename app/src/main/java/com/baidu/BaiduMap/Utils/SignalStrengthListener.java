package com.baidu.BaiduMap.Utils;
import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

public class SignalStrengthListener {

    private final TelephonyManager telephonyManager;
    private final PhoneStateListener phoneStateListener;

    public SignalStrengthListener(Context context) {
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                super.onSignalStrengthsChanged(signalStrength);
                int asu = signalStrength.getGsmSignalStrength(); // 对于GSM网络
                onStrengthChanged();
                // 对于CDMA网络可以使用
                // int asu = signalStrength.getCdmaDbm();
                // int evdoDbm = signalStrength.getEvdoDbm();
                // int evdoSnr = signalStrength.getEvdoSnr();
                // int lteDbm = signalStrength.getLteSignalStrength();
                // int lteRsrp = signalStrength.getLteRsrp();
                // 处理信号强度变化
                // asu值通常用于计算信号强度，0表示无信号，31表示最好的信号
                // 根据实际情况转换为可读的信号强度值
            }
        };
    }

    public void startListening() {
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    public void stopListening() {
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
    }

    public void onStrengthChanged(){}
}