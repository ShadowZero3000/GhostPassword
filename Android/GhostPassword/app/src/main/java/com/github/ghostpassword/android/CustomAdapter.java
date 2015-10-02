package com.github.ghostpassword.android;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by upunych on 10/2/15.
 */
public class CustomAdapter extends BaseAdapter {

    private ArrayList mButtons = null;

    public CustomAdapter(ArrayList b)
    {
        mButtons = b;
    }

    @Override
    public int getCount() {
        return mButtons.size();
    }

    @Override
    public Object getItem(int position) {
        return mButtons.get(position);
    }

    @Override
    public long getItemId(int position) {
//in our case position and id are synonymous
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Button button;
        if (convertView == null) {
            button = (Button) mButtons.get(position);
        } else {
            button = (Button) convertView;
        }
        return button;
    }
}
