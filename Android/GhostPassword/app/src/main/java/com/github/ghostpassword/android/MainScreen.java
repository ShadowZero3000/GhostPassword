package com.github.ghostpassword.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.ghostpassword.ghostpasswordbackend.BlueToothDao;

import java.io.IOException;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        //this is an ugly hack
        System.setProperty("com.github.ghostpassword.filedir", getFilesDir().getAbsolutePath());
    }

    public void sendString(View view) {
        synchronized (this) {
            BlueToothDao dao = new BlueToothDao();
            try {
                dao.write("This is a string!");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                dao.close();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
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

    /**
     * Called when the user clicks the Send button
     */
    public void gotoOnetimeScreen(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DisplayOneTimeActivity.class);
        startActivity(intent);
    }

    /**
     * Called when the user clicks the Send button
     */
    public void gotoSavedPassScreen(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, CheckAndInit.class);
        startActivity(intent);
    }
}
