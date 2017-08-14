package com.adaskin.android.atrac.models;

import android.util.Log;

import com.adaskin.android.atrac.utilities.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class TimeDateStringConversions {

    public static long convertDateStringToLong(String dateString) {
        long dateInMilliseconds;
        try {
            Date date = new SimpleDateFormat(Constants.dateFormat, Locale.US).parse(dateString);
            dateInMilliseconds = date.getTime();
        } catch (ParseException ex) {
            Calendar cal = new GregorianCalendar(1969,6,20);  // Months are 0-based
            dateInMilliseconds = cal.getTimeInMillis();
        }
        return dateInMilliseconds;
    }

    // It looks like timeInMs value is being mutiplied by 10. ???
    private static long convertTimeStringToLong(String timeString) {
        long timeInMilliseconds;
        try {
            Date date = new SimpleDateFormat(Constants.timeFormat, Locale.US).parse(timeString);
            timeInMilliseconds = date.getTime();
        } catch (ParseException ex) {
            Calendar cal = new GregorianCalendar(1969, 6, 20);
            timeInMilliseconds = cal.getTimeInMillis();
        }
        return timeInMilliseconds;
    }

    public static String findDayName(String dateString) {
        int idx = dateString.indexOf(",");
        return dateString.substring(0, idx);
    }

    public static long restoreTicks(String dateString, String timeString) {
        long dateLong = convertDateStringToLong(dateString);
        long timeLong = convertTimeStringToLong(timeString);
        String msg = String.format(Locale.US, "Date: %d  Time: %d", dateLong, timeLong);
        Log.d("LC", msg);
        return dateLong + timeLong;
    }

    public static String getCurrentDateAsString() {
        String todayAsString = "foo";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(Constants.dateFormat, Locale.US);
            Calendar today = Calendar.getInstance();
            todayAsString = sdf.format(today.getTime());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return todayAsString;
    }
}
