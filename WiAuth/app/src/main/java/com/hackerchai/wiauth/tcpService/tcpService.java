package com.hackerchai.wiauth.tcpService;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.hackerchai.wiauth.Thread.SocketServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class tcpService extends Service {
    SharedPreferences getPairKey;


    public tcpService() {
    }


    @Override
    public void onCreate() {
    getPairKey = getSharedPreferences("userAuth", MODE_PRIVATE);
    int pairKey = getPairKey.getInt("PAIR_KEY", 0);
    final String sPairKey = Integer.toString(pairKey);
    new Thread(new Runnable()
     {
            @Override
            public void run()
            {
                //Log.d("recvMsg","Open server....");
                SocketServer ss = new SocketServer(sPairKey);
            }
     }).start();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}




