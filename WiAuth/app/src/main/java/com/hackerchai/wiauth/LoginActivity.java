package com.hackerchai.wiauth;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A login screen that offers login via email/password and via Google+ sign in.
 * <p/>
 * ************ IMPORTANT SETUP NOTES: ************
 * In order for Google+ sign in to work with your app, you must first go to:
 * https://developers.google.com/+/mobile/android/getting-started#step_1_enable_the_google_api
 * and follow the steps in "Step 1" to create an OAuth 2.0 client for your package.
 */
public class LoginActivity extends Activity  {
    String userNameValue;
    String passwordValue;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp = this.getSharedPreferences("userAuth", MODE_PRIVATE);
        Button logButton =(Button)findViewById(R.id.log_in_button);
        final EditText username =(EditText)findViewById(R.id.username);
        final EditText password =(EditText)findViewById(R.id.password);
        logButton.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             userNameValue= username.getText().toString();
                                             passwordValue= password.getText().toString();
                                             if(!username.equals("")&&!passwordValue.equals(""))
                                             {
                                                 SharedPreferences.Editor editor = sp.edit();
                                                 editor.putString("USER_NAME", userNameValue);
                                                 editor.putString("PASSWORD",passwordValue);
                                                 editor.putBoolean("isLog", true);
                                                 editor.commit();
                                             }
                                             else
                                             {
                                                 Toast.makeText(LoginActivity.this,"请检查格式",Toast.LENGTH_LONG).show();
                                             }

                                             }

                                     }

        );

    }



}



