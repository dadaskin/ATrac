package com.adaskin.android.atrac.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.adaskin.android.atrac.database.DbAdapter;
import com.adaskin.android.atrac.fragments.WeekFragment;
import com.adaskin.android.atrac.models.DailyEntry;
import com.adaskin.android.atrac.models.WorkWeek;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private DbAdapter mDbAdapter;
    private List<DailyEntry> mAllEntries;
    public List<WorkWeek> mWeekCollection;

    public SectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);

        mDbAdapter = new DbAdapter(context);
        mDbAdapter.open();
        mAllEntries = mDbAdapter.fetchAllDailyEntryObjects();
        mWeekCollection = new ArrayList<>();
        mDbAdapter.close();

        putDailyEntriesIntoWeeks();
    }

    @Override
    public Fragment getItem(int weekIndex) {
        Bundle args = new Bundle();
        int max = mWeekCollection.size();
        //int index = max - weekIndex - 1;
        int index = weekIndex;

        WeekFragment fragment = new WeekFragment();
        fragment.setWorkWeek(mWeekCollection.get(index));
        return fragment;

    }

    @Override
    public int getCount() {
        return  mWeekCollection.size();
    }

    private void putDailyEntriesIntoWeeks() {
        Calendar startDate = new GregorianCalendar(2017, 3, 23); // Months are 0-based
        WorkWeek firstWw = new WorkWeek(startDate);
        mWeekCollection.add(firstWw);

        for (DailyEntry de : mAllEntries) {

            // Ignore any entries from Saturday or Sunday
            String dayName = WorkWeek.findDayName(de.mDateString);
            if (dayName.equals("Saturday") || dayName.equals("Sunday"))
                continue;

            // Ignore any entries from before our start date
            long stsrtDateLong = startDate.getTimeInMillis();
            long thisDateLong = WorkWeek.convertDateStringToLong(de.mDateString);
            if (thisDateLong < stsrtDateLong)
                continue;

            boolean matchFound = false;
            for (WorkWeek ww : mWeekCollection) {
                if (ww.isPartOfThisWeek(de)) {
                    matchFound = true;
                    break;
                }
            }

            while (!matchFound) {
                startDate.add(Calendar.DATE, 7);
                WorkWeek thisWw = new WorkWeek(startDate);
                mWeekCollection.add(thisWw);

                boolean result = thisWw.isPartOfThisWeek(de);
                if (result)
                    break;
            }

        }
    }

}