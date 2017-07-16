package com.adaskin.android.atrac.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adaskin.android.atrac.R;
import com.adaskin.android.atrac.models.WorkWeek;

public class WeekFragment extends Fragment {

    public static WorkWeek mWorkWeek;

    public WeekFragment() {
    }

//    public static WeekFragment newInstance(int sectionNumber) {
//        WeekFragment fragment = new WeekFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//        fragment.setArguments(args);
//        return fragment;
//    }

    public static WeekFragment newInstance(WorkWeek ww) {
        WeekFragment fragment = new WeekFragment();
        WeekFragment.mWorkWeek = ww;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View rootView = inflater.inflate(R.layout.fragment_weekly, container, false);
        TextView textView = (TextView)rootView.findViewById(R.id.week_section_label);
        //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        textView.setText(mWorkWeek.mStartDateString);
        return rootView;
    }
}
