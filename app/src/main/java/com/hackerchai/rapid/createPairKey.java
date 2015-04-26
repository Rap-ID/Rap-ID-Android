package com.hackerchai.rapid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;


public class createPairKey extends ActionBarActivity {
    SharedPreferences createPair;
    SharedPreferences.Editor editor;
    TextView tv;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            // 创建TextView,为了设置StatusBar的颜色
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            // enable status bar tint
            tintManager.setStatusBarTintEnabled(true);
            // enable navigation bar tint
            tintManager.setStatusBarTintColor(Color.parseColor("#4285f4"));
        }
        else
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_create_pair_key);

        setTitle("创建配对密钥");
        createPair = getSharedPreferences("userAuth",MODE_PRIVATE);
        int pair_key;
        if(createPair.getInt("PAIR_KEY", 0)==-1)
        {
            int random[] = new int[10];
            for (int i = 0; i <= 3; i++) {
                random[i] = (int) (Math.random() * 8)+1;
            }
            pair_key=random[0]*1000+random[1]*100+random[2]*10+random[3];
            String pair =Integer.toString(pair_key);
            Log.d(pair,"pair key");
            editor=createPair.edit();
            editor.putInt("PAIR_KEY",pair_key);
            editor.commit();
            tv=(TextView)findViewById(R.id.auth);
            tv.setText(pair);
        }
        else
        {
            Toast.makeText(this, "已经创建", Toast.LENGTH_LONG).show();
            finish();
        }
        com.gc.materialdesign.views.ButtonRectangle auth =(com.gc.materialdesign.views.ButtonRectangle)findViewById(R.id.authButton);
        auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backAuth =new Intent(createPairKey.this,networkExecute.class);
                startActivity(backAuth);
                finish();
            }
        });

    }



}
