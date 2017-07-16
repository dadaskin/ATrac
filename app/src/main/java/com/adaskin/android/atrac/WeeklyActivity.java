package com.adaskin.android.atrac;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.Toast;

import com.adaskin.android.atrac.database.DbAdapter;
import com.adaskin.android.atrac.fragments.WeekFragment;
import com.adaskin.android.atrac.models.DailyEntry;
import com.adaskin.android.atrac.models.WorkWeek;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.util.List;


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


   public class SectionsPagerAdapter extends FragmentPagerAdapter {

       private DbAdapter mDbAdapter;
       private List<DailyEntry> mAllEntries;
       private List<WorkWeek> mWeekCollection;

       public SectionsPagerAdapter(FragmentManager fm) {
           super(fm);
           mDbAdapter = new DbAdapter(getApplicationContext());
           mDbAdapter.open();
           mAllEntries = mDbAdapter.fetchAllDailyEntryObjects();
           mWeekCollection = new ArrayList<WorkWeek>();
           mDbAdapter.close();

           putDailyEntriesIntoWeeks();


       }

       @Override
       public Fragment getItem(int position) {
           return WeekFragment.newInstance(mWeekCollection.get(position));
       }

       @Override
       public int getCount() {
           return  mWeekCollection.size();
       }

       @Override
       public CharSequence getPageTitle(int position) {
           switch (position) {
               case 0:
                   return "SECTION 1";
               case 1:
                   return "SECTION 2";
               case 2:
                   return "SECTION 3";
           }
           return null;
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
}


