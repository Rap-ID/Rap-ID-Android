package com.hackerchai.wiauth;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hackerchai.wiauth.Thread.HttpRequest;

import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * A login screen that offers login via email/password and via Google+ sign in.
 * <p/>
 * ************ IMPORTANT SETUP NOTES: ************
 * In order for Google+ sign in to work with your app, you must first go to:
 * https://developers.google.com/+/mobile/android/getting-started#step_1_enable_the_google_api
 * and follow the steps in "Step 1" to create an OAuth 2.0 client for your package.
 *
 */

public class LoginActivity extends Activity  {
    String userNameValue;
    String passwordValue;
    SharedPreferences sp;
    String IMEI;
    private String token_url;
    private String tokenContent;
    private String getToken;
    String list_url;
    String idContent;
    String getId;
    String update_url;
    String updateContent;


    public static final int SHOW_RESPONSE = 0;
    private Handler handler =new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    if(!response.equals("BAD_TOKEN")) {
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
                    }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("登录");
        sp = this.getSharedPreferences("userAuth", MODE_PRIVATE);
        Button logButton =(Button)findViewById(R.id.log_in_button);
        final EditText username =(EditText)findViewById(R.id.username);
        final EditText password =(EditText)findViewById(R.id.password);
        TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        IMEI =  tm.getDeviceId();
        logButton.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             userNameValue = username.getText().toString();
                                             passwordValue = password.getText().toString();
                                             if (!username.equals("") && !passwordValue.equals("")) {
                                             new Thread(new Runnable() {
                                                 @Override
                                                 public void run() {
                                                     HttpRequest token = new HttpRequest();
                                                     token_url = "http://wiauth.hackerchai.com/api/user/get_token/" + "account=" + userNameValue + "&" + "password=" + passwordValue;
                                                     Log.d("url", token_url);
                                                     try {
                                                         tokenContent = token.get(token_url);
                                                         getToken = parseTokenWithJson(tokenContent);
                                                         if (!getToken.equals("BAD_TOKEN")) {
                                                             list_url = "http://wiauth.hackerchai.com/api/user/list/" + "token=" + getToken + "&" + "account=" + userNameValue;
                                                             Log.d("list_url", list_url);
                                                             HashMap<String, String> headers = new HashMap<String, String>();
                                                             headers.put("Cookie", "PHPSESSID=" + getToken);
                                                             token.setHeaders(headers);
                                                             idContent = token.get(list_url);
                                                             Log.d("idContent", idContent);
                                                             getId = parseIdWithJson(idContent);

                                                             update_url = "http://wiauth.hackerchai.com/api/user/update/" + "token=" + getToken + "&" + "id=" + getId + "&" + "password=" + passwordValue + "&" + "usmid=" + IMEI;
                                                             Log.d("update_url", update_url);
                                                             updateContent = token.get(update_url);
                                                             Log.d("updateContent", updateContent);
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
                                                             getId="BAD_TOKEN";
                                                             Message message =handler.obtainMessage();
                                                             message.what =SHOW_RESPONSE;
                                                             message.obj=getId.toString();
                                                             handler.sendMessage(message);
                                                         }
                                                     } catch (IOException e) {
                                                         e.printStackTrace();
                                                     } finally {
                                                         if (token != null) {
                                                             token.shutdownHttpClient();
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
        String err_code = tokenParse.getErr_code();
        Log.d("err_code", err_code);
        String token = null;
        if (err_code.equals("0")) {
            token = tokenParse.data.getToken();
            Log.d("token",token);
        } else {
            token="BAD_TOKEN";
        }
        return token;

    }
    private String parseIdWithJson (String jsonData) {
        Gson gsonId = new Gson();
        ListParse listParse = gsonId.fromJson(jsonData, ListParse.class);
        String err_code = listParse.getErr_code();
        Log.d("err_code", err_code);
        List<ListParse.Data.Items> idList = null;
        String id =null;
        if (err_code.equals("0")) {
            idList=listParse.getData().getItems();
            for(ListParse.Data.Items items:idList)
            {
                id=items.getId();
            }
            Log.d("id",id);
        } else {
            id="BAD_TOKEN";
        }
        return id;

    }



}



