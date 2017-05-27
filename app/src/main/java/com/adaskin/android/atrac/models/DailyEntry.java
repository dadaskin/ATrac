package com.adaskin.android.atrac.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dave on 5/26/2017.
 */

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

    private String calculateTotal() {
        // Convert time strings to real time
        // Calculate total
        // Convert total to string and return it

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
