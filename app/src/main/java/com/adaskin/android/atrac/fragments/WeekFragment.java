package com.adaskin.android.atrac.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.adaskin.android.atrac.R;
import com.adaskin.android.atrac.adapters.BoldRowAdapter;
import com.adaskin.android.atrac.models.DailyEntry;
import com.adaskin.android.atrac.models.WorkWeek;
import com.adaskin.android.atrac.utilities.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;

public class WeekFragment extends Fragment {

    //public static String WEEK_INDEX_KEY = "weekIndexKey";
    private Context mContext;
    private WorkWeek mWW;

    public WeekFragment(){}

    public void setWorkWeek(WorkWeek ww) {
        mWW = ww;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        View rootView = inflater.inflate(R.layout.weekly_fragment1, container, false);

//        int weekIndex = getArguments().getInt(WEEK_INDEX_KEY);
//        WorkWeek thisWeek = mWWList.get(weekIndex);
        adjustForEmptyDays(mWW);

        TextView weeklyTitleView = (TextView)rootView.findViewById(R.id.week_section_label1);
        String titleText = makeTitleString(mWW.mStartDateString);
        weeklyTitleView.setText(titleText);

        List<String> dateList = accumulateDateStrings(mWW);
        List<String> timeList = accumulateTimeStrings(mWW);
        List<String> dailyTotalList = accumulateDailyTotalStrings(mWW);

        String[] dateArray = new String[dateList.size()];
        dateArray = dateList.toArray(dateArray);

        String[] timeArray = new String[timeList.size()];
        timeArray = timeList.toArray(timeArray);

        String[] dailyTotalArray = new String[dailyTotalList.size()];
        dailyTotalArray = dailyTotalList.toArray(dailyTotalArray);

        ListView dayLabelView = (ListView)rootView.findViewById(R.id.date_line);
        BoldRowAdapter dateLineAdapter = new BoldRowAdapter(mContext, dateArray);
        dayLabelView.setAdapter(dateLineAdapter);
//
//        ListView timeListView = (ListView)rootView.findViewById(R.id.time_list);
//        ArrayAdapter<String> timeListAdapter = new ArrayAdapter<>(mContext, R.layout.regular_row, timeArray);
//        timeListView.setAdapter(timeListAdapter);
//
        ListView dailyTotalView = (ListView)rootView.findViewById(R.id.daily_total_line);
        BoldRowAdapter dailyTotalAdapter = new BoldRowAdapter(mContext, dailyTotalArray);
        dailyTotalView.setAdapter(dailyTotalAdapter);


        TextView weeklyTotalView = (TextView)rootView.findViewById(R.id.weekly_total);
        weeklyTotalView.setText(displayWeeklyTotal(mWW));

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
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

    private List<String>  accumulateDateStrings(WorkWeek ww) {
        List<String> resultList = new ArrayList<>();
        resultList.add("~~");  // First row, first column is blank.
        resultList.add(makeDisplayDateString(ww.mMonday.mDateString));
        resultList.add(makeDisplayDateString(ww.mTuesday.mDateString));
        resultList.add(makeDisplayDateString(ww.mWednesday.mDateString));
        resultList.add(makeDisplayDateString(ww.mThursday.mDateString));
        resultList.add(makeDisplayDateString(ww.mFriday.mDateString));

        return resultList;
    }

    private List<String> accumulateTimeStrings(WorkWeek ww) {
        List<String> resultList = new ArrayList<>();

        resultList.add("Start:");
        resultList.add(ww.mMonday.mStartString);
        resultList.add(ww.mTuesday.mStartString);
        resultList.add(ww.mWednesday.mStartString);
        resultList.add(ww.mThursday.mStartString);
        resultList.add(ww.mFriday.mStartString);

        resultList.add("Lunch:");
        resultList.add(ww.mMonday.mLunchString);
        resultList.add(ww.mTuesday.mLunchString);
        resultList.add(ww.mWednesday.mLunchString);
        resultList.add(ww.mThursday.mLunchString);
        resultList.add(ww.mFriday.mLunchString);

        resultList.add("Return:");
        resultList.add(ww.mMonday.mReturnString);
        resultList.add(ww.mTuesday.mReturnString);
        resultList.add(ww.mWednesday.mReturnString);
        resultList.add(ww.mThursday.mReturnString);
        resultList.add(ww.mFriday.mReturnString);

        resultList.add("Stop:");
        resultList.add(ww.mMonday.mStopString);
        resultList.add(ww.mTuesday.mStopString);
        resultList.add(ww.mWednesday.mStopString);
        resultList.add(ww.mThursday.mStopString);
        resultList.add(ww.mFriday.mStopString);

        return resultList;
    }

    private List<String> accumulateDailyTotalStrings(WorkWeek ww) {
        List<String> resultList = new ArrayList<>();

        resultList.add("##");  // Last row, first column is blank
        resultList.add(ww.mMonday.mTotalHoursForDay);
        resultList.add(ww.mTuesday.mTotalHoursForDay);
        resultList.add(ww.mWednesday.mTotalHoursForDay);
        resultList.add(ww.mThursday.mTotalHoursForDay);
        resultList.add(ww.mFriday.mTotalHoursForDay);

        return resultList;
    }

    private String displayWeeklyTotal(WorkWeek ww) {
        ww.calculateTotal();
        return "Total for week: " + ww.mTotalHoursForWeek;
    }

    private String makeTitleString(String dateString) {

        String result = "Week of ";

        SimpleDateFormat sdf = new SimpleDateFormat(Constants.dateFormat, Locale.US);
        long longSunday = 0;
        try {
            Calendar calSunday = new GregorianCalendar();
            calSunday.setTime(sdf.parse(dateString));
            longSunday = calSunday.getTimeInMillis();

        } catch (Exception e) {
            result += "xxx";
        }

        long longMonday = longSunday + Constants.dayInMilliseconds;
        long longFriday = longSunday + 5 * Constants.dayInMilliseconds;
        Calendar calMonday = new GregorianCalendar();
        calMonday.setTimeInMillis(longMonday);
        Calendar calFriday = new GregorianCalendar();
        calFriday.setTimeInMillis(longFriday);

        String stringMonday = sdf.format(calMonday.getTime());
        String[] fieldsMonday = stringMonday.split(", ");

        String stringFriday = sdf.format(calFriday.getTime());
        String[] fieldsFriday = stringFriday.split(", ");

        result += fieldsMonday[1] + " - " + fieldsFriday[1];
        return result;
    }

    private void adjustForEmptyDays(WorkWeek ww) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.dateFormat, Locale.US);

        long lMon = ww.mLongWeekStartDate + Constants.dayInMilliseconds;
        long lTue = lMon + Constants.dayInMilliseconds;
        long lWed = lTue + Constants.dayInMilliseconds;
        long lThu = lWed + Constants.dayInMilliseconds;
        long lFri = lThu + Constants.dayInMilliseconds;

        if (ww.mMonday == null) {
            ww.mMonday = new DailyEntry(sdf.format(new Date(lMon)), "--:--", "--:--", "--:--", "--:--");
        }

        if (ww.mTuesday == null) {
            ww.mTuesday = new DailyEntry(sdf.format(new Date(lTue)), "--:--", "--:--", "--:--", "--:--");
        }

        if (ww.mWednesday == null) {
            ww.mWednesday = new DailyEntry(sdf.format(new Date(lWed)), "--:--", "--:--", "--:--", "--:--");
        }

        if (ww.mThursday == null) {
            ww.mThursday = new DailyEntry(sdf.format(new Date(lThu)), "--:--", "--:--", "--:--", "--:--");
        }

        if (ww.mFriday == null) {
            ww.mFriday = new DailyEntry(sdf.format(new Date(lFri)), "--:--", "--:--", "--:--", "--:--");
        }
    }
}
