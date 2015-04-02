package com.rwssistent.LARA.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rwssistent.LARA.R;

/**
 * Created by Samme on 3/08/2015.
 */
public class MyAdapter extends BaseAdapter {
    String[] menuItems;
    int[] images = {R.drawable.ic_home, R.drawable.ic_settings, R.drawable.ic_exit};
    private Activity context;

    /**
     * Called by BaseActivity
     * @param context contains the Activity class
     */
    public MyAdapter(Activity context) {
        this.context = context;
        menuItems = context.getResources().getStringArray(R.array.menuItems);
    }

    @Override
    public int getCount() {
        return menuItems.length;
    }

    @Override
    public Object getItem(int position) {

        return menuItems[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Inserts the menuItems & images arrays into the listbox
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.custom_row, parent, false);
        } else {
            row = convertView;
        }
        TextView titleTextView = (TextView) row.findViewById(R.id.textViewRow);
        ImageView titleImageView = (ImageView) row
                .findViewById(R.id.imageViewRow);
        titleTextView.setText(menuItems[position]);
        titleTextView.setTextColor(Color.WHITE);
        titleImageView.setImageResource(images[position]);
        return row;
    }
}
