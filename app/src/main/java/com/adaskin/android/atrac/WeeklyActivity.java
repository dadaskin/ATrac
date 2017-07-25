package com.adaskin.android.atrac;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.adaskin.android.atrac.adapters.SectionsPagerAdapter;


public class WeeklyActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipeable_page);

        SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        ViewPager viewPager = (ViewPager) findViewById(R.id.page_container);   // In swipeable_page.xml
        viewPager.setAdapter(pagerAdapter);
   }



}


