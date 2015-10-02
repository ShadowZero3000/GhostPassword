package com.github.ghostpassword.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.ghostpassword.ghostpasswordbackend.PasswordService;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class CheckAndInit extends AppCompatActivity {

    private TextView password;
    private TextView confirm;
    private TextView errorField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_and_init);
        if(PasswordService.isInited()){
            Intent intent = new Intent(this, UnlockActivity.class);
            startActivity(intent);
        }
        password = (TextView)findViewById(R.id.password);
        confirm = (TextView)findViewById(R.id.confirm);
        errorField =(TextView)findViewById(R.id.textView5);
    }

    public void onSetup(View view) throws IOException, NoSuchAlgorithmException {
        System.out.println("Trying to init new password db...");
        if(password.getText().toString().equals(confirm.getText().toString())){
            System.out.println("Initing DB");
            PasswordService.init(password.getText().toString());
            Intent intent = new Intent(this, UnlockActivity.class);
            startActivity(intent);

        } else {
            System.out.println("Passwords don't match." + password.getText().toString());
            errorField.setText("Passwords don't match. Try again.");
            password.setText("");
            confirm.setText("");
        }

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
