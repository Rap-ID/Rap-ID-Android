package com.hackerchai.wiauth.tcpService;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.hackerchai.wiauth.Thread.SocketServer;

public class authService extends Service {
    SharedPreferences getPairKey;
    String username;
    String password;
    public authService() {
    }
    @Override
    public void onCreate() {
        getPairKey = getSharedPreferences("userAuth", MODE_PRIVATE);
        username = getPairKey.getString("USER_NAME","");
        int pairKey = getPairKey.getInt("PAIR_KEY", 0);
        password =getPairKey.getString("PASSWORD","");
        final String sPairKey = Integer.toString(pairKey);
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                //Log.d("recvMsg","Open server....");
                SocketServer ss = new SocketServer(sPairKey,username,password,49162);
            }
        }).start();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
