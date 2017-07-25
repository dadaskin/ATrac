package com.adaskin.android.atrac.adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.adaskin.android.atrac.R;
import com.adaskin.android.atrac.models.DailyEntry;
import com.adaskin.android.atrac.models.WorkWeek;
import com.adaskin.android.atrac.utilities.Constants;

import java.text.SimpleDateFormat;
import java.util.Locale;


public class DailyEntryAdapter extends BaseAdapter {

    private WorkWeek mWorkWeek;
    private LayoutInflater mInflater;


    public DailyEntryAdapter(Context context, WorkWeek ww) {
        mWorkWeek = ww;
        mInflater = LayoutInflater.from(context);
        adjustForEmptyDays();
    }


    @Override
    public int getCount() {
        return 5;   // Display a row for each work day in the week
    }

    @Override
    public Object getItem(int i) {
        switch (i) {
            case 0: return mWorkWeek.mMonday;
            case 1: return mWorkWeek.mTuesday;
            case 2: return mWorkWeek.mWednesday;
            case 3: return mWorkWeek.mThursday;
            case 4: return mWorkWeek.mFriday;
            default: return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {

        View rowView = mInflater.inflate(R.layout.weekly_row_content, parent, false);

        DailyEntry de = (DailyEntry)getItem(i);

        TextView dateLabelView = (TextView)rowView.findViewById(R.id.date_label);
        dateLabelView.setText(convertToShortDateString(de.mDateString));

        TextView startView = (TextView)rowView.findViewById(R.id.start_time);
        startView.setText(de.mStartString);

        TextView lunchView = (TextView)rowView.findViewById(R.id.lunch_time);
        lunchView.setText(de.mLunchString);

        TextView returnView = (TextView)rowView.findViewById(R.id.return_time);
        returnView.setText(de.mReturnString);

        TextView stopView = (TextView)rowView.findViewById(R.id.stop_time);
        stopView.setText(de.mStopString);

        return rowView;
    }

    private String convertToShortDateString(String dateString) {

        // Do the right thing here

        return "Mon\nFeb 28";
    }


    private void adjustForEmptyDays() {
        long sundayDate = mWorkWeek.mLongWeekStartDate;

        DailyEntry nullEntry = new DailyEntry("tbd", "--:--", "--:--", "--:--", "--:--");
        long dayInMilliseconds = 24L*60L*60L*1000L;
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.dateFormat, Locale.US);

        long date = sundayDate + dayInMilliseconds;
        if (mWorkWeek.mMonday == null) {
            nullEntry.mDateString = sdf.format(date);
            mWorkWeek.mMonday = nullEntry;
        }

        date += dayInMilliseconds;
        if (mWorkWeek.mTuesday == null) {
            nullEntry.mDateString = sdf.format(date);
            mWorkWeek.mTuesday = nullEntry;
        }

        date += dayInMilliseconds;
        if (mWorkWeek.mWednesday == null) {
            nullEntry.mDateString = sdf.format(date);
            mWorkWeek.mWednesday = nullEntry;
        }

        date += dayInMilliseconds;
        if (mWorkWeek.mThursday == null) {
            nullEntry.mDateString = sdf.format(date);
            mWorkWeek.mThursday = nullEntry;
        }

        date += dayInMilliseconds;
        if (mWorkWeek.mFriday == null) {
            nullEntry.mDateString = sdf.format(date);
            mWorkWeek.mFriday = nullEntry;
        }
    }

}
