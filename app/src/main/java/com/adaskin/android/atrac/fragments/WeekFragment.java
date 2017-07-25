package com.adaskin.android.atrac.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.adaskin.android.atrac.R;
import com.adaskin.android.atrac.adapters.DailyEntryAdapter;
import com.adaskin.android.atrac.models.WorkWeek;

import java.util.List;

public class WeekFragment extends Fragment {

    public static String WEEK_INDEX_KEY = "weekIndexKey";
    private static Context mContext;

    private List<WorkWeek> mWWList;

    public WeekFragment() {}

    public void setWorkWeekCollection(List<WorkWeek> wwCollection) {
        mWWList = wwCollection;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        View rootView = inflater.inflate(R.layout.weekly_fragment, container, false);
        ListView listView = (ListView)rootView.findViewById(R.id.weekly_list);

        int weekIndex = getArguments().getInt(WEEK_INDEX_KEY);
        WorkWeek thisWeek = mWWList.get(weekIndex);

        DailyEntryAdapter dea = new DailyEntryAdapter(mContext, thisWeek);
        listView.setAdapter(dea);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
