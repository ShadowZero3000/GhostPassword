package com.github.ghostpassword.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.ghostpassword.ghostpasswordbackend.PasswordServiceHolder;
import com.github.ghostpassword.ghostpasswordbackend.domain.Password;

import java.util.List;

public class SavedPasswordMainActivity extends AppCompatActivity {

    private ListView list = null;

    public void onCreateNew(View view) {
        System.out.println("Starting add new password menu.");
        Intent intent = new Intent(this, AddNewPasswordActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_password_main);
        list = (ListView) findViewById(R.id.listView);
        try {
            List<Password> passwords = PasswordServiceHolder.getPasswordService().getAllPasswordsOrderByAlphabetical();
            String[] array = new String[passwords.size()];
            for (int i = 0; i < passwords.size(); i++) {
                array[i] = passwords.get(i).getFriendlyName();
            }
            ArrayAdapter listAdaptor = new ArrayAdapter(getBaseContext(), R.layout.activity_saved_password_main, array);
            list.setAdapter(listAdaptor);
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_saved_password_main, menu);
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
