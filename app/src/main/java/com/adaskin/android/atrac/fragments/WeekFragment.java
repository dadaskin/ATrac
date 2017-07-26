package com.adaskin.android.atrac.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.adaskin.android.atrac.R;
import com.adaskin.android.atrac.adapters.DailyEntryAdapter;
import com.adaskin.android.atrac.models.WorkWeek;
import com.adaskin.android.atrac.utilities.Constants;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;

public class WeekFragment extends Fragment {

    public static String WEEK_INDEX_KEY = "weekIndexKey";
    private Context mContext;
    private List<WorkWeek> mWWList;

    public WeekFragment() {}

    public void setWorkWeekCollection(List<WorkWeek> wwCollection) {
        mWWList = wwCollection;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        View rootView = inflater.inflate(R.layout.weekly_fragment, container, false);

        int weekIndex = getArguments().getInt(WEEK_INDEX_KEY);
        WorkWeek thisWeek = mWWList.get(weekIndex);

        TextView weeklyTitleView = (TextView)rootView.findViewById(R.id.week_section_label);
        String titleText = makeTitleString(thisWeek.mStartDateString);
        weeklyTitleView.setText(titleText);

        ListView listView = (ListView)rootView.findViewById(R.id.weekly_list);
        DailyEntryAdapter dea = new DailyEntryAdapter(mContext, thisWeek);
        listView.setAdapter(dea);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
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
}
