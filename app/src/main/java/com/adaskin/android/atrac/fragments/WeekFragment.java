package com.adaskin.android.atrac.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adaskin.android.atrac.R;
import com.adaskin.android.atrac.WeeklyActivity;
import com.adaskin.android.atrac.models.WorkWeek;


public class WeekFragment extends Fragment {

    private static String WEEK_INDEX_KEY = "weekIndexKey";
    private static WeeklyActivity.SectionsPagerAdapter mPager;

    public WeekFragment() {
    }

//    public static WeekFragment newInstance(int sectionNumber) {
//        WeekFragment fragment = new WeekFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//        fragment.setArguments(args);
//        return fragment;
//    }

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
        TextView textView = (TextView)rootView.findViewById(R.id.week_section_label);

        //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

        int weekIndex = getArguments().getInt(WEEK_INDEX_KEY);
        WorkWeek thisWeek = mPager.mWeekCollection.get(weekIndex);

        textView.setText(thisWeek.mStartDateString);
        return rootView;
    }
}
