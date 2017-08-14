package com.adaskin.android.atrac.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.adaskin.android.atrac.utilities.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class WorkWeek implements Parcelable {
    // ------- Fields ----------
    // Raw Fields
    public long mLongWeekStartDate;
    public String mStartDateString;
    public DailyEntry mMonday;
    public DailyEntry mTuesday;
    public DailyEntry mWednesday;
    public DailyEntry mThursday;
    public DailyEntry mFriday;

    // Calculated Fields
    public String mTotalHoursForWeek;

    // ---------- Constructors -------------
    public WorkWeek(Calendar startDate) {
        mLongWeekStartDate = startDate.getTimeInMillis();
        mStartDateString = new SimpleDateFormat(Constants.dateFormat, Locale.US).format(startDate.getTime());
    }

    public boolean isPartOfThisWeek(DailyEntry de)   {
        long weekStartDate = mLongWeekStartDate;
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(mLongWeekStartDate);
        endCalendar.add(Calendar.DATE, 7);
        long weekEndDate = endCalendar.getTimeInMillis();

        long incomingDate = TimeDateStringConversions.convertDateStringToLong(de.mDateString);

        boolean result = false;
        if ((weekStartDate < incomingDate) && (incomingDate < weekEndDate)) {
            saveCorrectDay(de);
            result = true;
        }

        return result;
    }

    private void saveCorrectDay(DailyEntry de) {
        String dayName = TimeDateStringConversions.findDayName(de.mDateString);
        switch(dayName) {
            case "Monday": mMonday = de; break;
            case "Tuesday": mTuesday = de; break;
            case "Wednesday" : mWednesday = de; break;
            case "Thursday" : mThursday = de; break;
            case "Friday" : mFriday = de; break;
            default: break;
        }
    }

    public void calculateTotal() {
        double totalTime = mMonday.mTotalMinutes +
                           mTuesday.mTotalMinutes +
                           mWednesday.mTotalMinutes +
                           mThursday.mTotalMinutes +
                           mFriday.mTotalMinutes;
        mTotalHoursForWeek = String.format(Locale.US,"%.1f", totalTime/60.0);
    }

    // --------- Implementation of Parcelable interface ------------
    // Constructor
    private WorkWeek(Parcel in) {
        mMonday = in.readParcelable(DailyEntry.class.getClassLoader());
        mTuesday = in.readParcelable(DailyEntry.class.getClassLoader());
        mWednesday = in.readParcelable(DailyEntry.class.getClassLoader());
        mThursday = in.readParcelable(DailyEntry.class.getClassLoader());
        mFriday = in.readParcelable(DailyEntry.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mMonday, flags);
        dest.writeParcelable(mTuesday, flags);
        dest.writeParcelable(mWednesday, flags);
        dest.writeParcelable(mThursday, flags);
        dest.writeParcelable(mFriday, flags);
    }

    public static final Parcelable.Creator<WorkWeek> CREATOR = new Parcelable.Creator<WorkWeek>() {
        public WorkWeek createFromParcel(Parcel in) {
            return new WorkWeek(in);
        }

        public WorkWeek[] newArray(int size) {
            return new WorkWeek[size];
        }
    };
}
