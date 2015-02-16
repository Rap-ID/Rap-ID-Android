package com.hackerchai.wiauth.tcpService;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.hackerchai.wiauth.R;
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

    public void updateNotification(String text) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("通知")
                .setContentText(text)
                .setTicker("WiAuth通知:" + text)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setOngoing(false)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.drawable.ic_launcher);
        mNotificationManager.notify(1, mBuilder.build());

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
