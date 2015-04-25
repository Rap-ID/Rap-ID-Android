package com.hackerchai.rapid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
    TextView label;
    Button stop;
    SharedPreferences delPair;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_network_execute);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setStatusBarTintColor(Color.parseColor("#4285f4"));
        setTitle("服务运行中");

        label =(TextView)findViewById(R.id.textView);
        label.setText("授权服务启动中....");
        checkPairkey =getSharedPreferences("userAuth",MODE_PRIVATE);
        if(checkPairkey.getInt("PAIR_KEY", 0)==-1)
        {
            Intent getPairKey =new Intent(networkExecute.this,createPairKey.class);
            startActivity(getPairKey);
        }
        else
        {

            Intent sendBroadcst= new Intent(networkExecute.this,udpService.class);
            Intent startTcpService =new Intent(networkExecute.this,tcpService.class);
            Intent startAuthService = new Intent(networkExecute.this,authService.class);

            startService(sendBroadcst);
            startService(startTcpService);
            startService(startAuthService);
            Toast.makeText(networkExecute.this,"WiAuth授权服务开始",Toast.LENGTH_LONG).show();
            label.setText("WiAuth授权服务已开始");

        }
        stop=(Button)findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent killSendBroadcst= new Intent(networkExecute.this,udpService.class);
                Intent killStartTcpService =new Intent(networkExecute.this,tcpService.class);
                Intent killStartAuthService = new Intent(networkExecute.this,authService.class);

                stopService(killSendBroadcst);
                stopService(killStartAuthService);
                stopService(killStartTcpService);
                label.setText("WiAuth授权服务已关闭");
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
