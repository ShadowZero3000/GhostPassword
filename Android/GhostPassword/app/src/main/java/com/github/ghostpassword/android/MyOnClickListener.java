package com.github.ghostpassword.android;

import android.content.Intent;
import android.view.View;

/**
 * Created by upunych on 10/2/15.
 */
public class MyOnClickListener implements View.OnClickListener {
    private final int position;

    public MyOnClickListener(int position)
    {
        this.position = position;
    }

    public void onClick(View v)
    {

        // Preform a function based on the position
        //someFunction(this.position)
    }
}
