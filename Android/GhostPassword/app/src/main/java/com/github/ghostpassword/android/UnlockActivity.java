package com.github.ghostpassword.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.ghostpassword.ghostpasswordbackend.PasswordService;
import com.github.ghostpassword.ghostpasswordbackend.PasswordServiceHolder;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class UnlockActivity extends AppCompatActivity {

    private TextView password;
    private TextView errorField;

    public void onUnlockAttempt(View view) throws NoSuchAlgorithmException, IOException{
        System.out.println("Trying to unlock...");
        if(PasswordService.checkPassword(password.getText().toString())){
            System.out.println("Unlocking...");
            PasswordService pwService = new PasswordService(password.getText().toString());
            PasswordServiceHolder.setPasswordService(pwService);
            Intent intent = new Intent(this, SavedPasswordMainActivity.class);
            startActivity(intent);

        } else {
            System.out.println("Password is incorrect");
            errorField.setText("The password is incorrect. Try again.");
            password.setText("");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unlock_screen);
        password = (TextView)findViewById(R.id.masterpass);
        errorField =(TextView)findViewById(R.id.errorLabel);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_saved_pass_screen, menu);
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
