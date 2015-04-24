package com.hackerchai.wiauth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    SharedPreferences userAuth;
    String bool;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userAuth =getSharedPreferences("userAuth",MODE_PRIVATE);
        Log.d(bool,"boolean");
        if(userAuth.getString("USER_NAME",null)==null)
        {
            Intent loginActivity =new Intent(MainActivity.this,LoginActivity.class);
            startActivity(loginActivity);
            finish();
        }
        else
        {
            Intent networkExcute = new Intent(MainActivity.this,networkExecute.class);
            startActivity(networkExcute);
            finish();

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
