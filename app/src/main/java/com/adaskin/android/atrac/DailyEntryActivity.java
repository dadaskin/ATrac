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
    private long mEntryId;
    private DailyEntry mDailyEntry;

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


        ////////////////////////// TBD //////////////////
        // 1.  Implement calculate method
        // 2.  Make sure that outputToCsv() will append.
        // 3.  Style button
        // 4.  Test somehow (inject a time service object ?)
        //////////////////////////////////////////////////



        // Get Current Date and display it.
        TextView dateView = (TextView)findViewById(R.id.dateView);
        String _dateAsString = getCurrentDateAsString();
        dateView.setText(_dateAsString);

        // Read DB to determine current state, set button text appropriately
        mEntryId = findCurrentEntryId(_dateAsString);
        mDailyEntry = null;
        if (mEntryId > 0) {
            mDailyEntry = findCurrentEntryObject(mEntryId);
        } else {
            mDailyEntry = new DailyEntry(_dateAsString);
            DbAdapter dbAdapter = new DbAdapter(this);
            dbAdapter.open();
            mEntryId = dbAdapter.createDailyEntryRecord(mDailyEntry);
            dbAdapter.close();
        }

        mActionButton = (Button)findViewById(R.id.actionButton);

        setButtonToCurrentState(mDailyEntry);

        // Display current days entries if any.
        displayCurrentEntries(mDailyEntry);

        // Set up button listener
        mActionButton.setOnClickListener(mActionListener);
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

        if(mButtonState == ButtonState.START) {
            mDailyEntry.mStartString = nowString;
            mButtonState = ButtonState.LUNCH;
        } else if (mButtonState == ButtonState.LUNCH) {
            mDailyEntry.mLunchString = nowString;
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
        dbAdapter.changeDailyEntry(mEntryId, mDailyEntry);
        dbAdapter.outputToCsv();
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
