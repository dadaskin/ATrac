package com.adaskin.android.atrac.models;

import java.text.SimpleDateFormat;
import android.os.Parcel;
import android.os.Parcelable;

import com.adaskin.android.atrac.utilities.Constants;

import java.util.Date;
import java.util.Locale;


public class DailyEntry implements Parcelable {
    // ------- Fields ----------
    // Raw Fields
    public String mDateString;
    public String mStartString;
    public String mLunchString;
    public String mReturnString;
    public String mStopString;

    // Calculated Fields
    public String mTotalHoursForDay;

    // ---------- Constructors -------------
    public DailyEntry(String dateString,
                      String startString,
                      String lunchString,
                      String returnString,
                      String stopString) {
        mDateString = dateString;
        mStartString = startString;
        mLunchString = lunchString;
        mReturnString = returnString;
        mStopString = stopString;

        mTotalHoursForDay = calculateTotal();
    }

    public DailyEntry(String dateString) {
        this(dateString,
             Constants.TIME_NOT_YET_SET,
             Constants.TIME_NOT_YET_SET,
             Constants.TIME_NOT_YET_SET,
             Constants.TIME_NOT_YET_SET);
    }

    public String calculateTotal() {
        // Convert time strings to real time
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);

        Date startTime = null;
        Date lunchTime = null;
        Date returnTime = null;
        Date stopTime = null;
        try {
            startTime = sdf.parse(mStartString);
            lunchTime = sdf.parse(mLunchString);
            returnTime = sdf.parse(mReturnString);
            stopTime = sdf.parse(mStopString);
        } catch (Exception e) {
            e.getStackTrace();
        }

        // Calculate total
        long morning_ms = 0L;
        long afternoon_ms = 0L;
        if ((startTime != null)&& (lunchTime != null))
        {
            morning_ms = lunchTime.getTime() - startTime.getTime();
            if ((returnTime != null) && (stopTime != null))
                afternoon_ms = stopTime.getTime() - returnTime.getTime();
        }

        long total_ms = morning_ms + afternoon_ms;

        double time_hr = total_ms/(1000.0 *60.0 * 60.0);

        // Convert total to string showing 2 decimal places and return it
        mTotalHoursForDay =  String.format(Locale.US, "%.2f", time_hr);

        return "TBD";
    }

    // --------- Implementation of Parcelable interface ------------
    // Comstructor
    private DailyEntry(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mDateString);
        dest.writeString(mStartString);
        dest.writeString(mLunchString);
        dest.writeString(mReturnString);
        dest.writeString(mStopString);
        dest.writeString(mTotalHoursForDay);
    }

    private void readFromParcel(Parcel in) {
        mDateString = in.readString();
        mStartString = in.readString();
        mLunchString = in.readString();
        mReturnString = in.readString();
        mStopString = in.readString();
        mTotalHoursForDay = in.readString();
    }

    public static final Parcelable.Creator<DailyEntry> CREATOR = new Parcelable.Creator<DailyEntry>() {
        public DailyEntry createFromParcel(Parcel incoming) {
            return new DailyEntry(incoming);
        }

        public DailyEntry[] newArray(int size) {
            return new DailyEntry[size];
        }
    };
}
