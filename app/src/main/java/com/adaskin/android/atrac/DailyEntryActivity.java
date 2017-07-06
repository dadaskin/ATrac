package com.adaskin.android.atrac;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.adaskin.android.atrac.database.DbAdapter;
import com.adaskin.android.atrac.models.DailyEntry;
import com.adaskin.android.atrac.utilities.ButtonState;
import com.adaskin.android.atrac.utilities.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DailyEntryActivity extends AppCompatActivity {

    private Button mActionButton;
    private Button mChangeViewButton;
    private ButtonState mButtonState;
    private String mDateString;
    private DailyEntry mDailyEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_entry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get Current Date and display it.
        TextView dateView = (TextView)findViewById(R.id.dateView);
        mDateString = getCurrentDateAsString();
        dateView.setText(mDateString);

        //testCalculations(_dateAsString);
        //testDataAccumulation();

        mActionButton = (Button)findViewById(R.id.actionButton);
        mChangeViewButton = (Button)findViewById(R.id.changeView);

        // Read DB to determine current state, set button text appropriately
        setCurrentStateAndDisplay(mDateString);

        // Set up button listener
        mActionButton.setOnClickListener(mActionListener);
        mChangeViewButton.setOnClickListener(mChangeViewListener);
    }

//    private void testDataAccumulation() {
//        DailyEntry de0 = new DailyEntry("Saturday, Feb 09, 1963", "7:45", "12:01", "12:55", "17:30");
//        de0.calculateTotal();
//        DailyEntry de1 = new DailyEntry("Sunday, Feb 10, 1963", "7:52", "11:51", "13:02", "17:15");
//        de1.calculateTotal();
//        DailyEntry de2 = new DailyEntry("Monday, Feb 11, 1963", "7:36", "12:07", "13:25", "17:00");
//        de2.calculateTotal();
//
//        DbAdapter dbAdapter = new DbAdapter(this);
//        dbAdapter.open();
//        dbAdapter.createDailyEntryRecord(de0);
//        dbAdapter.createDailyEntryRecord(de1);
//        dbAdapter.createDailyEntryRecord(de2);
//        dbAdapter.close();
//    }
//
//    private void testCalculations(String dateString) {
//        // Test Calculations:
//        // 1.  Nominal: 8-12, 13-17 -> 8.00
//        DailyEntry de1 = new DailyEntry(dateString, "8:00", "12:00", "13:00", "17:00");
//        de1.calculateTotal();
//
//        // 2.  Morning only:  7:23 - 11:52 -> 4.48
//        DailyEntry de2 = new DailyEntry(dateString, "7:23", "11:52", "--:--", "--:--");
//        de2.calculateTotal();
//
//        // 3.  Over 10 hours:  7:16 - 12:10, 13:05 - 20:15 -> 12.07
//        DailyEntry de3 = new DailyEntry(dateString, "7:16", "12:10", "13:05", "20:15");
//        de3.calculateTotal();
//
//        // 4. Start only:  7:49, --:--, --:--, --:--  ->  0.00
//        DailyEntry de4 = new DailyEntry(dateString, "7:49", "--:--", "--:--", "--:--");
//        de4.calculateTotal();
//
//        // 5. Start, Lunch, Return only: 7:32, 12:05, 13:10, --:--  -> 4.55
//        DailyEntry de5 = new DailyEntry(dateString, "7:32", "12:05", "13:10", "--:--");
//        de5.calculateTotal();
//    }

    private void setCurrentStateAndDisplay(String dateString) {
        mDailyEntry = getTodaysEntryInfo(dateString);
        setButtonToCurrentState(mDailyEntry);
        displayCurrentEntries(mDailyEntry);
    }

    private DailyEntry getTodaysEntryInfo(String dateString){
        long id = findCurrentEntryId(dateString);
        DailyEntry de;
        if (id > 0) {
            de = findCurrentEntryObject(id);
        } else {
            de = new DailyEntry(dateString);
            DbAdapter dbAdapter = new DbAdapter(this);
            dbAdapter.open();
            dbAdapter.createDailyEntryRecord(de);
            dbAdapter.close();
        }

        return de;
    }

    private void displayCurrentEntries(DailyEntry de) {
        ((TextView)findViewById(R.id.startHours)).setText(de.mStartString);
        ((TextView)findViewById(R.id.lunchHours)).setText(de.mLunchString);
        ((TextView)findViewById(R.id.returnHours)).setText(de.mReturnString);
        ((TextView)findViewById(R.id.stopHours)).setText(de.mStopString);
        ((TextView)findViewById(R.id.totalHours)).setText(de.mTotalHoursForDay);
    }

    private DailyEntry findCurrentEntryObject(long entryId) {
        DbAdapter dbAdapter = new DbAdapter(this);
        dbAdapter.open();
        DailyEntry de = null;
        try {
            de = dbAdapter.fetchDailyEntryObjectFromId(entryId);
        } catch(Exception e) {
            Toast.makeText(this, "findCurrentEntryObject(): " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        dbAdapter.close();
        return de;
    }

    private long findCurrentEntryId(String dateString) {
        DbAdapter dbAdapter = new DbAdapter(this);
        dbAdapter.open();
        long entryId = 0;
        try {
            entryId= dbAdapter.fetchDailyEntryIdFromDate(dateString);
        } catch(Exception e) {
            Toast.makeText(this, "findCurrentEntry(): " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        dbAdapter.close();
        return entryId;
    }

    private final View.OnClickListener mActionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doAction();
        }
    };

    private final View.OnClickListener mChangeViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeToWeeklyView();
        }
    };

    private void changeToWeeklyView(){
        Intent intent = new Intent(this, WeeklyActivity.class);
        startActivity(intent);
    }

    
    private String getCurrentDateAsString() {
        String todayAsString = "foo";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.US);
            Calendar today = Calendar.getInstance();
            todayAsString = sdf.format(today.getTime());
        } catch(Exception e) {
            e.printStackTrace();
            String msg = "Date: " + e.getMessage();
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        }
        return todayAsString;
    }

    private String getCurrentTimeAsString() {
        String nowAsString = "bar";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
            Calendar now = Calendar.getInstance();
            nowAsString = sdf.format(now.getTime());
        } catch(Exception e){
            e.printStackTrace();
            String msg = "Time: " + e.getMessage();
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        }
        return nowAsString;
    }

    private void doAction() {
        String nowString = getCurrentTimeAsString();

        if(mButtonState == ButtonState.START) {
            mDailyEntry.mStartString = nowString;
            mButtonState = ButtonState.LUNCH;
        } else if (mButtonState == ButtonState.LUNCH) {
            mDailyEntry.mLunchString = nowString;
            mDailyEntry.calculateTotal();
            mButtonState = ButtonState.RETURN;
        }else if (mButtonState == ButtonState.RETURN) {
            mDailyEntry.mReturnString = nowString;
            mButtonState = ButtonState.STOP;
        }else if (mButtonState == ButtonState.STOP) {
            mDailyEntry.mStopString = nowString;
            mButtonState = ButtonState.ENTRY_COMPLETE;
            mDailyEntry.calculateTotal();
        }
        mActionButton.setText(convertStateToString(mButtonState));
        displayCurrentEntries(mDailyEntry);
        setButtonToCurrentState(mDailyEntry);

        DbAdapter dbAdapter = new DbAdapter(this);
        dbAdapter.open();
        dbAdapter.exportDB();
        long id = dbAdapter.fetchDailyEntryIdFromDate(mDateString);
        dbAdapter.changeDailyEntry(id, mDailyEntry);
        dbAdapter.close();
    }

    private ButtonState findCurrentButtonState(DailyEntry de) {
        ButtonState bs = ButtonState.ENTRY_COMPLETE;
        if (de.mStartString.equals(Constants.TIME_NOT_YET_SET))
            bs = ButtonState.START;
        else if (de.mLunchString.equals(Constants.TIME_NOT_YET_SET))
            bs = ButtonState.LUNCH;
        else if (de.mReturnString.equals(Constants.TIME_NOT_YET_SET))
            bs = ButtonState.RETURN;
        else if (de.mStopString.equals(Constants.TIME_NOT_YET_SET))
            bs = ButtonState.STOP;

        return bs;
    }

    private String convertStateToString(ButtonState state) {
        if (state == ButtonState.START) return Constants.STATE_START;
        if (state == ButtonState.LUNCH) return Constants.STATE_LUNCH;
        if (state == ButtonState.RETURN) return Constants.STATE_RETURN;
        if (state == ButtonState.STOP) return Constants.STATE_STOP;
        return Constants.STATE_COMPLETE;
    }

    private void setButtonToCurrentState(DailyEntry de) {
        mButtonState = findCurrentButtonState(de);

        // Set Button text or hide it
        if (mButtonState == ButtonState.ENTRY_COMPLETE)
            mActionButton.setVisibility(View.INVISIBLE);
        else {
            mActionButton.setVisibility(View.VISIBLE);
            mActionButton.setText(convertStateToString(mButtonState));
        }
    }

    public void MenuCommand(MenuItem item) {
        int id = item.getItemId();
        DbAdapter dbAdapter = new DbAdapter(this);
        dbAdapter.open();
        switch (id) {
            case R.id.undo:
                dbAdapter.importDB();
                break;
            case R.id.read_csv:
                dbAdapter.readFromCSV();
                break;
        }
        setCurrentStateAndDisplay(mDateString);
        dbAdapter.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_daily_entry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.version_info) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
