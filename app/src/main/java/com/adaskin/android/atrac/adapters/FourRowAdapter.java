package com.adaskin.android.atrac.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.adaskin.android.atrac.R;

import java.util.List;

public class FourRowAdapter extends BaseAdapter {

    private List<String> mData;
    private LayoutInflater mInflater;

    // Incoming data must be String[24]
    public FourRowAdapter(Context context, List<String> data) {
        mData = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 4;  // Data will be displayed in 4 rows
    }

    @Override
    public Object getItem(int i) { return mData.get(0); }

    @Override
    public long getItemId(int i) { return i; }

    @Override
    public View getView(int row, View view, ViewGroup viewGroup) {
        View rowView = mInflater.inflate(R.layout.regular_row, viewGroup, false);

        int baseIndex = 6 * row;

        TextView timesliceView = (TextView)rowView.findViewById(R.id.timeslice_label);
        timesliceView.setText(mData.get(baseIndex));

        TextView monTimeView = (TextView)rowView.findViewById(R.id.monday_time);
        monTimeView.setText(mData.get(baseIndex+1));

        TextView tueTimeView = (TextView)rowView.findViewById(R.id.tuesday_time);
        tueTimeView.setText(mData.get(baseIndex+2));

        TextView wedTimeView = (TextView)rowView.findViewById(R.id.wednesday_time);
        wedTimeView.setText(mData.get(baseIndex+3));

        TextView thuTimeView = (TextView)rowView.findViewById(R.id.thursday_time);
        thuTimeView.setText(mData.get(baseIndex+4));

        TextView friTimeView = (TextView)rowView.findViewById(R.id.friday_time);
        friTimeView.setText(mData.get(baseIndex+5));

        return rowView;
    }


}
