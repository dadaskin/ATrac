package com.adaskin.android.atrac.models;

import android.os.Parcel;
import android.os.Parcelable;

public class WorkWeek implements Parcelable {
    // ------- Fields ----------
    // Raw Fields
    public DailyEntry mMonday;
    public DailyEntry mTuesday;
    public DailyEntry mWednesday;
    public DailyEntry mThursday;
    public DailyEntry mFriday;

    // Calculated Fields
    public String mTotalHoursForWeek;

    // ---------- Constructors -------------
    public WorkWeek() {}

    public void calculateTotal() {
        mTotalHoursForWeek = mMonday.mTotalHoursForDay +
                             mTuesday.mTotalHoursForDay +
                             mWednesday.mTotalHoursForDay +
                             mThursday.mTotalHoursForDay +
                             mFriday.mTotalHoursForDay;
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
