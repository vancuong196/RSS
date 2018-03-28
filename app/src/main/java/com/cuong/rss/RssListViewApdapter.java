package com.cuong.rss;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cuong on 3/28/2018.
 */

public class RssListViewApdapter extends ArrayAdapter<RSS> {

    public RssListViewApdapter(@NonNull Context context, @NonNull ArrayList<RSS> datas) {
        super(context, R.layout.lv_navigation_item, datas);
    }

    @NonNull

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        RSS rss = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.lv_navigation_item, null);

        }
        TextView tt1 = (TextView) v.findViewById(R.id.tv_name);
        TextView tt2 = (TextView) v.findViewById(R.id.tv_link);


        if (tt1 != null) {
            tt1.setText(rss.getmTitle());
        }

        if (tt2 != null) {
            tt2.setText(rss.getmLink());
        }


        // Return the completed view to render on screen
        return v;
    }
}
