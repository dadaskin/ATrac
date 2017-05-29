package com.adaskin.android.atrac;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.adaskin.android.atrac.database.DbAdapter;
import com.adaskin.android.atrac.models.DailyEntry;
import com.adaskin.android.atrac.utilities.ButtonState;
import com.adaskin.android.atrac.utilities.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DailyEntryActivity extends AppCompatActivity {

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


        // Get Current Date and display it.
        TextView dateView = (TextView)findViewById(R.id.dateView);
        String dateAsString = getCurrentDateAsString();
        dateView.setText(dateAsString);

        // Read DB to determine current state, set button text appropriately
        setButtonToCurrentState(dateAsString);

        // Set up button listener
        // Display current days entries if any.

    }

    private ButtonState findCurrentButtonState(String dateString) {
        DbAdapter dbAdapter = new DbAdapter(this);
        ButtonState state = ButtonState.WHAT;
        try {
            long todaysEntryId = dbAdapter.fetchDailyEntryIdFromDate(dateString);
            if (todaysEntryId != -1) {
                DailyEntry todaysEntry = dbAdapter.fetchDailyEntryObjectFromId(todaysEntryId);
                if (todaysEntry.mStartString.equals(Constants.TIME_NOT_YET_SET))
                    state = ButtonState.START;
                else if (todaysEntry.mLunchString.equals(Constants.TIME_NOT_YET_SET))
                    state = ButtonState.LUNCH;
                else if (todaysEntry.mReturnString.equals(Constants.TIME_NOT_YET_SET))
                    state = ButtonState.RETURN;
                else if (todaysEntry.mStopString.equals(Constants.TIME_NOT_YET_SET))
                    state = ButtonState.STOP;
                else state = ButtonState.ENTRY_COMPLETE;
            }
            dbAdapter.close();
        } catch(Exception e){
            state = ButtonState.WHAT;
        }
        return state;
    }

    private String convertStateToString(ButtonState state) {
        if (state == ButtonState.WHAT) return Constants.STATE_WHAT;
        if (state == ButtonState.START) return Constants.STATE_START;
        if (state == ButtonState.LUNCH) return Constants.STATE_LUNCH;
        if (state == ButtonState.RETURN) return Constants.STATE_RETURN;
        if (state == ButtonState.STOP) return Constants.STATE_STOP;
        return Constants.STATE_COMPLETE;
    }

    private void setButtonToCurrentState(String dateString) {
        ButtonState state = findCurrentButtonState(dateString);

        // Set Button text or hide it
        Button actionButton = (Button)findViewById(R.id.actionButton);
        if (state == ButtonState.ENTRY_COMPLETE)
            actionButton.setVisibility(View.GONE);
        else
            actionButton.setText(convertStateToString(state));
    }

    private String getCurrentDateAsString() {
        String todayAsString = "foo";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.US);
           Calendar today = Calendar.getInstance();
            todayAsString = sdf.format(today.getTime());
        } catch(Exception e) {
            e.printStackTrace();
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
