package com.hackerchai.rapid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hackerchai.rapid.Thread.HttpUtil;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A login screen that offers login via email/password and via Google+ sign in.
 * <p/>
 * ************ IMPORTANT SETUP NOTES: ************
 * In order for Google+ sign in to work with your app, you must first go to:
 * https://developers.google.com/+/mobile/android/getting-started#step_1_enable_the_google_api
 * and follow the steps in "Step 1" to create an OAuth 2.0 client for your package.
 *
 */

public class LoginActivity extends ActionBarActivity {
    String userNameValue;
    String passwordValue;
    SharedPreferences sp;
    String ICCID;
    private String token_url;
    private String tokenContent;
    private String getToken;




    public static final int SHOW_RESPONSE = 0;
    private Handler handler =new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;

                        Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
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
        setContentView(R.layout.activity_login);

        setTitle("登录");
        sp = this.getSharedPreferences("userAuth", MODE_PRIVATE);
        com.gc.materialdesign.views.ButtonRectangle logButton =(com.gc.materialdesign.views.ButtonRectangle)findViewById(R.id.log_in_button);
        final EditText username =(EditText)findViewById(R.id.username);
        final EditText password =(EditText)findViewById(R.id.password);
        TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        ICCID = tm.getSimSerialNumber();

        logButton.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             userNameValue = username.getText().toString();
                                             passwordValue = password.getText().toString();
                                             if (!username.equals("") && !passwordValue.equals("")) {
                                             new Thread(new Runnable() {
                                                 @Override
                                                 public void run() {
                                                     HttpUtil token = new HttpUtil();
                                                     token_url ="https://rapid.cotr.me/api/login";
                                                     //token.setUrl(token_url);
                                                     Map<String, String> params = new HashMap<String, String>();
                                                     params.put("username", userNameValue);
                                                     params.put("password", passwordValue);
                                                     params.put("iccid",ICCID);

                                                     try {
                                                         tokenContent = token.post(token_url,null,params);
                                                         getToken = parseTokenWithJson(tokenContent);
                                                         Log.d("token",getToken);
                                                         if (getToken.equals("OK")) {
                                                             SharedPreferences.Editor editor = sp.edit();
                                                             editor.putString("USER_NAME", userNameValue);
                                                             editor.putString("PASSWORD", passwordValue);
                                                             editor.putBoolean("isLog", true);
                                                             editor.putInt("PAIR_KEY", -1);
                                                             editor.commit();
                                                             Intent goToNetworkExecute = new Intent(LoginActivity.this, networkExecute.class);
                                                             startActivity(goToNetworkExecute);
                                                             finish();
                                                         } else {
                                                             Message message =handler.obtainMessage();
                                                             message.what =SHOW_RESPONSE;
                                                             message.obj=getToken.toString();
                                                             handler.sendMessage(message);
                                                         }
                                                     } catch (IOException e) {
                                                         e.printStackTrace();
                                                     } finally {
                                                         if (token != null) {

                                                         }

                                                     }

                                                 }
                                             }).start();
                                         }
                                         else {
                                                 Toast.makeText(LoginActivity.this, "不能为空", Toast.LENGTH_LONG).show();
                                             }


                                        }

                                }

        );

    }
    private String parseTokenWithJson (String jsonData) {
        Gson gsonToken = new Gson();
        TokenParse tokenParse = gsonToken.fromJson(jsonData, TokenParse.class);
        String err_code = tokenParse.getError().getId();
        String token = null;
        if (err_code.equals("0")) {
            token = "OK";
        } else {
            token=tokenParse.getError().getMsg();
        }
        return token;

    }



}



