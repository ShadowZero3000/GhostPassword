package com.github.ghostpassword.android;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.github.ghostpassword.ghostpasswordbackend.PasswordServiceHolder;

import java.util.ArrayList;

public class SavedPasswordMainActivity extends AppCompatActivity implements View.OnClickListener{

    private ArrayList mButtons = new ArrayList();

    public void onCreateNew(View view){
        System.out.println("Starting add new password menu.");


        Intent intent = new Intent(this, AddNewPasswordActivity.class);
        startActivity(intent);



        /*if()    //passwords present
        {
            defineButtons();
        }
        PasswordServiceHolder.getPasswordService().getAllPasswordsOrderByRecentlyUsed();*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_password_main);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        //gridview.setAdapter(new ButtonAdapter(this));
        gridview.setAdapter(new CustomAdapter(mButtons));

        Button cb = null;

        for (int i =0; i<12; i++) {
            cb = new Button(this);
            cb.setText(Integer.toString(i));
            cb.setOnClickListener(this);
            cb.setId(i);
            mButtons.add(cb);
        }

    }

    public void defineButton(){

    }

    @Override
    public void onClick(View v) {
        Button selection = (Button)v;
        Toast.makeText(getBaseContext(), selection.getText()+ " was pressed!", Toast.LENGTH_SHORT).show();
    }

/*    @Override
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
    }*/
}
