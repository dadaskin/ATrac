package com.adaskin.android.atrac;

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
    private ButtonState mButtonState;
    private String mDateAsString;
    private long mEntryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_entry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        createSeedData();

        // Get Current Date and display it.
        TextView dateView = (TextView)findViewById(R.id.dateView);
        mDateAsString = getCurrentDateAsString();
        dateView.setText(mDateAsString);

        // Set up button listener
        mActionButton = (Button)findViewById(R.id.actionButton);
        mActionButton.setOnClickListener(mActionListener);
        // Read DB to determine current state, set button text appropriately
        setButtonToCurrentState(mDateAsString);

        // Display current days entries if any.
        displayCurrentEntries();

    }

    private void displayCurrentEntries() {
        DailyEntry de = null;
        if (mEntryId != -1) {
            DbAdapter dbAdapter = new DbAdapter(this);
            dbAdapter.open();
            de = dbAdapter.fetchDailyEntryObjectFromId(mEntryId);
            dbAdapter.close();
        } else {
            de = new DailyEntry(mDateAsString, Constants.TIME_NOT_YET_SET, Constants.TIME_NOT_YET_SET, Constants.TIME_NOT_YET_SET, Constants.TIME_NOT_YET_SET);
        }

        ((TextView)findViewById(R.id.startHours)).setText(de.mStartString);
        ((TextView)findViewById(R.id.lunchHours)).setText(de.mLunchString);
        ((TextView)findViewById(R.id.returnHours)).setText(de.mReturnString);
        ((TextView)findViewById(R.id.stopHours)).setText(de.mStopString);
    }


    private void createSeedData() {
        DailyEntry seed = new DailyEntry("Monday, February 11, 1963", "07:36", "11:30", "13:30", "17:05");
        DbAdapter dbAdapter = new DbAdapter(this);
        dbAdapter.open();
        dbAdapter.createDailyEntryRecord(seed);
        int count = dbAdapter.getEntryCount();
        Toast.makeText(this, "count: " + count, Toast.LENGTH_LONG).show();
        dbAdapter.close();
    }

    private final View.OnClickListener mActionListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doAction();
        }
    };

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

    public void doAction() {
        String nowString = getCurrentTimeAsString();
        DbAdapter dbAdapter = new DbAdapter(this);
        dbAdapter.open();
        if (mButtonState == ButtonState.START) {
            //create new record
            DailyEntry de = new DailyEntry(mDateAsString, nowString);
            mEntryId = dbAdapter.createDailyEntryRecord(de);
            dbAdapter.outputToCsv();   // Remove after testing
            mButtonState = ButtonState.LUNCH;

        }  else {
            // Update existing record
            DailyEntry de = dbAdapter.fetchDailyEntryObjectFromId(mEntryId);
            if (mButtonState == ButtonState.LUNCH) {
                de.mLunchString = nowString;
            }else if (mButtonState == ButtonState.RETURN) {
                de.mReturnString = nowString;
            }else if (mButtonState == ButtonState.STOP) {
                de.mStopString = nowString;
            }
            dbAdapter.changeDailyEntry(mEntryId, de);
        }
        mActionButton.setText(convertStateToString(mButtonState));
        dbAdapter.close();
    }

    private ButtonState findCurrentButtonState(String dateString) {
        DbAdapter dbAdapter = new DbAdapter(this);
        dbAdapter.open();
        mButtonState = ButtonState.START;
        mEntryId = 0;
        try {
            mEntryId = dbAdapter.fetchDailyEntryIdFromDate(dateString);
            if (mEntryId != -1) {
                DailyEntry todaysEntry = dbAdapter.fetchDailyEntryObjectFromId(mEntryId);
                if (todaysEntry.mStartString.equals(Constants.TIME_NOT_YET_SET))
                    mButtonState = ButtonState.START;
                else if (todaysEntry.mLunchString.equals(Constants.TIME_NOT_YET_SET))
                    mButtonState = ButtonState.LUNCH;
                else if (todaysEntry.mReturnString.equals(Constants.TIME_NOT_YET_SET))
                    mButtonState = ButtonState.RETURN;
                else if (todaysEntry.mStopString.equals(Constants.TIME_NOT_YET_SET))
                    mButtonState = ButtonState.STOP;
                else mButtonState = ButtonState.ENTRY_COMPLETE;
            }
            dbAdapter.close();
        } catch(Exception e){
            mButtonState = ButtonState.START;
            Toast.makeText(this, "findCurrentButtonState() " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return mButtonState;
    }

    private String convertStateToString(ButtonState state) {
        if (state == ButtonState.START) return Constants.STATE_START;
        if (state == ButtonState.LUNCH) return Constants.STATE_LUNCH;
        if (state == ButtonState.RETURN) return Constants.STATE_RETURN;
        if (state == ButtonState.STOP) return Constants.STATE_STOP;
        return Constants.STATE_COMPLETE;
    }

    private void setButtonToCurrentState(String dateString) {
        ButtonState state = findCurrentButtonState(dateString);

        // Set Button text or hide it
        if (state == ButtonState.ENTRY_COMPLETE)
            mActionButton.setVisibility(View.INVISIBLE);
        else {
            mActionButton.setVisibility(View.VISIBLE);
            mActionButton.setText(convertStateToString(state));
        }
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
