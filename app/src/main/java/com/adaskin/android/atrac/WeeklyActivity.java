package com.adaskin.android.atrac;


import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adaskin.android.atrac.fragments.WeekFragment;


public class WeeklyActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weekly_activity);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);   // In weekly_activity.xml
        mViewPager.setAdapter(mSectionsPagerAdapter);
   }


//   public static class PlaceholderFragment extends Fragment {
//
//       private static final String ARG_SECTION_NUMBER = "section_number";
//
//       public PlaceholderFragment() {
//       }
//
//       public static PlaceholderFragment newInstance(int sectionNumber) {
//           PlaceholderFragment fragment = new PlaceholderFragment();
//           Bundle args = new Bundle();
//           args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//           fragment.setArguments(args);
//           return fragment;
//       }
//
//       @Override
//       public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
//           View rootView = inflater.inflate(R.layout.fragment_placeholder, container, false);
//           TextView textView = (TextView)rootView.findViewById(R.id.section_label);
//           textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
//           return rootView;
//       }
//   }

   public class SectionsPagerAdapter extends FragmentPagerAdapter {
       public SectionsPagerAdapter(FragmentManager fm) {
           super(fm);
       }

       @Override
       public Fragment getItem(int position) {
        //   return PlaceholderFragment.newInstance(position + 1);
           return WeekFragment.newInstance(position + 1);
       }

       @Override
       public int getCount() {
           return 3;
       }

       @Override
       public CharSequence getPageTitle(int position) {
           switch (position) {
               case 0: return "SECTION 1";
               case 1: return "SECTION 2";
               case 2: return "SECTION 3";
           }
           return null;
       }
   }
}


