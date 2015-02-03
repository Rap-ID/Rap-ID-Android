package com.hackerchai.wiauth.udpService;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import com.hackerchai.wiauth.Thread.UdpHelper;


public class udpService extends Service {
    public udpService() {
    }
    UdpHelper udphelper;
    SharedPreferences broadcast;
    String usernameForBroadcast;


    Thread tReceived;
    public void onCreate() {
        WifiManager manager = (WifiManager) this
                .getSystemService(Context.WIFI_SERVICE);
        broadcast=getSharedPreferences("userAuth",MODE_PRIVATE);
        usernameForBroadcast=broadcast.getString("USER_NAME","");

        udphelper = new UdpHelper(manager,usernameForBroadcast);


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
