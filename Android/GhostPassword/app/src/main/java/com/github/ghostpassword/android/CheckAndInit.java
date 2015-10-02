package com.github.ghostpassword.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.ghostpassword.ghostpasswordbackend.PasswordService;

public class CheckAndInit extends AppCompatActivity {

    TextView password;
    TextView confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_and_init);
        if(PasswordService.isInited()){
            Intent intent = new Intent(this, DisplaySavedPassScreenActivity.class);
            startActivity(intent);
        }
        password = (TextView)findViewById(R.id.password);
        confirm = (TextView)findViewById(R.id.confirm);
    }

    public void onSetup(View view){
        System.out.println("Trying to init new password db...");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_check_and_init, menu);
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
