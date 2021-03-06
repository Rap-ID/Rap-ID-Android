package com.hackerchai.rapid.tcpService;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;

import com.hackerchai.rapid.R;
import com.hackerchai.rapid.Thread.SocketServer;

public class tcpService extends Service {
    SharedPreferences getPairKey;
    String username;
    String password;
    public static final int SHOW_RESPONSE = 0;


    private NotificationManager cNotificationManager;
    //常驻通知
    private Notification cNotification;

    public tcpService() {
    }


    @Override
    public void onCreate() {
    getPairKey = getSharedPreferences("userAuth", MODE_PRIVATE);
    username = getPairKey.getString("USER_NAME","");
    TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
    final String ICCID = tm.getSimSerialNumber();
    int pairKey = getPairKey.getInt("PAIR_KEY", 0);
    password =getPairKey.getString("PASSWORD","");
    final String sPairKey = Integer.toString(pairKey);

    final  Handler handler =new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SHOW_RESPONSE:
                        String response = (String) msg.obj;
                        if(response.equals("PAIR")) {
                            updateNotification("配对成功");
                            stopSelf();
                        }
                        else
                        {
                            
                        }
                }
            }
        };
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                //Log.d("recvMsg","Open server....");
                SocketServer ss = new SocketServer(sPairKey,username,password,ICCID,49161,handler,1);
            }
        }).start();

    }

    @Override
    public void onDestroy() {

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
                .setDefaults (Notification.DEFAULT_SOUND)
                .setSmallIcon(R.drawable.ic_launcher);

        mNotificationManager.notify(1, mBuilder.build());

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}





