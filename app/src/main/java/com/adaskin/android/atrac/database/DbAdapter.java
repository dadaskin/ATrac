package com.adaskin.android.atrac.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Dave on 5/25/2017.
 */

public class DbAdapter {

    // Database constants
    public static final String  DATABASE_NAME = "ATrac_hours";
    public static final int DATABASE_VERSION = 1;
    public static final String DB_VIEW_NAME = "db_view";

    public static final String DAILY_ENTRY_TABLE = "daily_entry_table";
    public static final String H_ROW_ID = "hours_row_id";
    public static final String H_DATE = "hours_date";
    public static final String H_START = "hours_start";
    public static final String H_LUNCH = "hours_lunch";
    public static final String H_RETURN = "hours_return";
    public static final String H_STOP = "hours_stop";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private mDbHelper mDbHelper;

    public DbAdapter(Context context) {
        mContext = context;
    }

    public void open() throws SQLException {
        mDbHelper = DbHelper.getInstance(mContext);
        mDb = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    private ContentValues createDailyEntrysCV(DailyEntry entry) {
        ContentValues cv = new ContentValues();
        cv.put(H_DATE, entry.mDate);
        cv.put(H_START, entry.mStart);
        cv.put(H_LUNCH, entry.mLunch);
        cv.put(H_RETURN, entry.mReturn);
        cv.put(H_STOP, entry.mStop);

        return cv;
    }

    public void createDailyEntryRecord(DailyEntry entry) {
        ContentValues cv = createDailyEntrysCV(entry);
        long newRow = mDb.insert(DAILY_ENTRY_TABLE, "", cv);
    }

    public int getEntryCount() {
        Cursor cursor = mDb.rawQuery("Select * from " + DAILY_ENTRY_TABLE, null);
        int n = cursor.getCount();
        cursor.close();
        return n;
    }

    public long fetchDailyEntryIdFromDate(String dateString) {
        String[] params = new String[] { dateString };
        String sql = "select " + H_ROW_ID + " from " + DAILY_ENTRY_TABLE + " where " + H_DATE + "=?";
        Cursor cursor = mDb.rawQuery(sql, params);
        cursor.moveToFirst();
        if (cursor.isAfterLast()) { return -1; }
        int idx = cursor.getColumnIndex(H_ROW_ID);
        if (cursor.isNull(idx)) { return -1; }
        long id = cursor.getLong(idx);
        cursor.close();
        return id;
    }

    private Cursor fetchDailyEntryRecordFromId(long id) {
        String[] params = new String[] { String.valueOf(id) };
        String sql = "select * from " + DAILY_ENTRY_TABLE + " where " + H_ROW_ID + "=?";
        Cursor cursor = mDb.rawQuery(sql, params);
        cursor.moveToFirst();
        return cursor;
    }

    public DailyEntry fetchQuoteObjectFromId(long id) {
        // Get elements of the Quote object
        Cursor cursor = fetchDailyEntryRecordFromId(id);
        return makeQuoteFromCursor(cursor);
    }

    public DailyEntry makeDailyEntryFromCursor(Cursor cursor) {
        String dateString = cursor.getString(cursor.getColumnIndex(H_DATE));
        String
    }
}
