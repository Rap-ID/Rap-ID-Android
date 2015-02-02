package com.hackerchai.wiauth;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


public class createPairKey extends Activity {
    SharedPreferences createPair;
    SharedPreferences.Editor editor;
    TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pair_key);
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
            tv=(TextView)findViewById(R.id.textView);
            tv.setText(pair);
        }
        else
        {
            Toast.makeText(this, "已经创建", Toast.LENGTH_LONG).show();
        }

    }



}
