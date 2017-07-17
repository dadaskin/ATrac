package com.adaskin.android.atrac.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.adaskin.android.atrac.R;
import com.adaskin.android.atrac.WeeklyActivity;
import com.adaskin.android.atrac.models.DailyEntry;
import com.adaskin.android.atrac.models.WorkWeek;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;


public class WeekFragment extends Fragment {

    private static String WEEK_INDEX_KEY = "weekIndexKey";
    private static WeeklyActivity.SectionsPagerAdapter mPager;

    public WeekFragment() {
    }

    public static WeekFragment newInstance(WeeklyActivity.SectionsPagerAdapter pager, int weekIndex) {
        WeekFragment fragment = new WeekFragment();
        mPager = pager;

        Bundle args = new Bundle();
        args.putInt(WEEK_INDEX_KEY, weekIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View rootView = inflater.inflate(R.layout.fragment_weekly, container, false);

        int weekIndex = getArguments().getInt(WEEK_INDEX_KEY);
        WorkWeek thisWeek = mPager.mWeekCollection.get(weekIndex);
        ArrayList<String> thisWeekAsArrayList = convertToArrayList(thisWeek);

        TextView textView = (TextView)rootView.findViewById(R.id.week_section_label);
        textView.setText(thisWeek.mStartDateString);

        GridView gridView = (GridView)rootView.findViewById(R.id.weekly_grid);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, thisWeekAsArrayList);
        gridView.setAdapter(adapter);

        return rootView;
    }

    private static ArrayList<String> convertToArrayList(WorkWeek ww) {
        ArrayList<String> result = new ArrayList<>();

        addDailyEntryToResult(result, ww.mMonday);
        addDailyEntryToResult(result, ww.mTuesday);
        addDailyEntryToResult(result, ww.mWednesday);
        addDailyEntryToResult(result, ww.mThursday);
        addDailyEntryToResult(result, ww.mFriday);

        return result;
    }

    private static void addDailyEntryToResult(ArrayList<String> list, DailyEntry de) {
        if (de == null) {
            handleNullDailyEntry(list);
        } else {
            list.add(makeDisplayDateString(de.mDateString));
            list.add(de.mStartString);
            list.add(de.mLunchString);
            list.add(de.mReturnString);
            list.add(de.mStopString);
            // TBD: list.add(de.mTotalHoursForDay);
        }
    }

    private static void handleNullDailyEntry(ArrayList<String> list) {
        list.add("foo\nbar 32");
        list.add("--:--");
        list.add("--:--");
        list.add("--:--");
        list.add("--:--");
        // TBD: list.add("0.00");
    }

    private static String makeDisplayDateString(String dateString) {
        String result = "";
        SimpleDateFormat longFmt = new SimpleDateFormat("EEEE", Locale.US);
        SimpleDateFormat shortFmt = new SimpleDateFormat("EEE", Locale.US);

        String[] fields = dateString.split(", ");

        try {
            Calendar dayOfWeek = new GregorianCalendar();
            dayOfWeek.setTime(longFmt.parse(fields[0]));
            String shortDayString = shortFmt.format(dayOfWeek.getTime());
            result += shortDayString;
        } catch (Exception e) {
            result += "xxx";
        }

        result += "\n" + fields[1];

        return result;
    }
}
