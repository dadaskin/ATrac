package com.adaskin.android.atrac.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.adaskin.android.atrac.R;

import java.util.List;

public class BoldRowAdapter extends BaseAdapter {

    private List<String> mData;
    private LayoutInflater mInflater;

    // Incoming data must be String[6]
    public BoldRowAdapter(Context context, List<String> data) {
        mData = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 1;  // Display a single row
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = mInflater.inflate(R.layout.bold_row, viewGroup, false);

        TextView placeHolderView = (TextView)rowView.findViewById(R.id.placeholder_field);
        placeHolderView.setText(mData.get(0));

        TextView monView = (TextView)rowView.findViewById(R.id.monday_label);
        monView.setText(mData.get(1));

        TextView tueView = (TextView)rowView.findViewById(R.id.tuesday_label);
        tueView.setText(mData.get(2));

        TextView wedView = (TextView)rowView.findViewById(R.id.wednesday_label);
        wedView.setText(mData.get(3));

        TextView thuView = (TextView)rowView.findViewById(R.id.thursday_label);
        thuView.setText(mData.get(4));

        TextView friView = (TextView)rowView.findViewById(R.id.friday_label);
        friView.setText(mData.get(5));

        return rowView;
    }


}
