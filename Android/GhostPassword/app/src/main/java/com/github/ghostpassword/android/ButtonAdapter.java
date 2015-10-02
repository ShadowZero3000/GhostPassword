package com.github.ghostpassword.android;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

/**
 * Created by upunych on 10/2/15.
 */
public class ButtonAdapter extends BaseAdapter {
    private Context mContext;

    public String[] filenames = {
            "File 1",
            "File 2",
            "Roflcopters"
    };

    // Gets the context so it can be used later
    public ButtonAdapter(Context c) {
        mContext = c;
    }

    @Override
    public int getCount() {
        return filenames.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position,
                        View convertView, ViewGroup parent) {
        Button btn;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            btn = new Button(mContext);
            btn.setLayoutParams(new GridView.LayoutParams(100, 55));
            btn.setPadding(8, 8, 8, 8);
        }
        else {
            btn = (Button) convertView;
        }

        btn.setText(filenames[position]);
        // filenames is an array of strings
        btn.setTextColor(Color.WHITE);
       // btn.setBackgroundResource(R.drawable.button);
        btn.setId(position);

        btn.setOnClickListener(new MyOnClickListener(position));

        return btn;
    }
}
