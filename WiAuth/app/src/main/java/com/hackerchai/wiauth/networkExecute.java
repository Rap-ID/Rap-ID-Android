package com.hackerchai.wiauth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hackerchai.wiauth.tcpService.authService;
import com.hackerchai.wiauth.udpService.udpService;
import com.hackerchai.wiauth.tcpService.tcpService;


public class networkExecute extends ActionBarActivity {
    SharedPreferences checkPairkey;
    TextView label;
    Button stop;
    SharedPreferences delPair;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_execute);
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
                Toast.makeText(networkExecute.this,"WiAuth授权服务已关闭",Toast.LENGTH_LONG).show();
                label.setText("WiAuth授权服务已关闭");
                finish();

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
