package com.hackerchai.rapid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hackerchai.rapid.tcpService.authService;
import com.hackerchai.rapid.udpService.udpService;
import com.hackerchai.rapid.tcpService.tcpService;
import com.readystatesoftware.systembartint.SystemBarTintManager;


public class networkExecute extends ActionBarActivity {
    SharedPreferences checkPairkey;


    SharedPreferences delPair;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_execute);
        if(Build.VERSION.SDK_INT >= 19 ) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(Color.parseColor("#4285f4"));
            tintManager.setNavigationBarTintEnabled(true);
            tintManager.setNavigationBarTintColor(Color.parseColor("#4285f4"));
        }
       /* if(Build.VERSION.SDK_INT>=21)
        {

        }
        */
        else
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        setTitle("服务运行中");
        final TextView label;

        label =(TextView)findViewById(R.id.textView);
        checkPairkey =getSharedPreferences("userAuth",MODE_PRIVATE);
        if(checkPairkey.getInt("PAIR_KEY", 0)==-1)
        {
            Intent getPairKey =new Intent(networkExecute.this,createPairKey.class);
            startActivity(getPairKey);
            finish();
        }
        else
        {

            Intent sendBroadcst= new Intent(networkExecute.this,udpService.class);
            Intent startTcpService =new Intent(networkExecute.this,tcpService.class);
            Intent startAuthService = new Intent(networkExecute.this,authService.class);

            startService(sendBroadcst);
            startService(startTcpService);
            startService(startAuthService);
            Toast.makeText(networkExecute.this,"Rap-ID授权服务已开始",Toast.LENGTH_LONG).show();
            label.setText("Rap-ID授权服务已开始");

        }
        com.gc.materialdesign.views.ButtonRectangle stop;
        stop=(com.gc.materialdesign.views.ButtonRectangle)findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent killSendBroadcst= new Intent(networkExecute.this,udpService.class);
                Intent killStartTcpService =new Intent(networkExecute.this,tcpService.class);
                Intent killStartAuthService = new Intent(networkExecute.this,authService.class);

                stopService(killSendBroadcst);
                stopService(killStartAuthService);
                stopService(killStartTcpService);
                label.setText("Rap-ID授权服务已关闭");
                finish();
                System.exit(0);

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_network_execute, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent getPairKey = new Intent (networkExecute.this,LoginActivity.class);
            startActivity(getPairKey);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }
}
