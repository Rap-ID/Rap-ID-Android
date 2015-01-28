package com.hackerchai.wiouth.udpService;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;

import com.hackerchai.wiouth.UdpHelper;

public class udpService extends Service {
    public udpService() {
    }
    UdpHelper udphelper;

    Thread tReceived;
    public void onCreate() {
        WifiManager manager = (WifiManager) this
                .getSystemService(Context.WIFI_SERVICE);
        udphelper = new UdpHelper(manager);


        tReceived = new Thread(udphelper);
        tReceived.start();
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
