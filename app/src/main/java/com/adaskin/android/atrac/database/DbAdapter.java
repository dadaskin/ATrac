package com.adaskin.android.atrac.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.adaskin.android.atrac.models.DailyEntry;
import com.adaskin.android.atrac.utilities.Constants;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DbAdapter {

    // Database constants
    static final String  DATABASE_NAME = "ATrac_hours";
    static final int DATABASE_VERSION = 1;
    //public static final String DB_VIEW_NAME = "db_view";

    static final String DAILY_ENTRY_TABLE = "daily_entry_table";
    static final String H_ROW_ID = "hours_row_id";
    static final String H_DATE = "hours_date";
    static final String H_START = "hours_start";
    static final String H_LUNCH = "hours_lunch";
    static final String H_RETURN = "hours_return";
    static final String H_STOP = "hours_stop";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DbHelper mDbHelper;

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
        cv.put(H_DATE, entry.mDateString);
        cv.put(H_START, entry.mStartString);
        cv.put(H_LUNCH, entry.mLunchString);
        cv.put(H_RETURN, entry.mReturnString);
        cv.put(H_STOP, entry.mStopString);

        return cv;
    }

    public void createDailyEntryRecord(DailyEntry entry) {
        ContentValues cv = createDailyEntrysCV(entry);
        mDb.insert(DAILY_ENTRY_TABLE, "", cv);
    }

//    public int getEntryCount() {
//        Cursor cursor = mDb.rawQuery("Select * from " + DAILY_ENTRY_TABLE, null);
//        int n = cursor.getCount();
//        cursor.close();
//        return n;
//    }

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

    public DailyEntry fetchDailyEntryObjectFromId(long id) {
        Cursor cursor = fetchDailyEntryRecordFromId(id);
        return makeDailyEntryFromCursor(cursor);
    }

    private DailyEntry makeDailyEntryFromCursor(Cursor cursor) {
        String dateString = cursor.getString(cursor.getColumnIndex(H_DATE));
        String startString = cursor.getString(cursor.getColumnIndex(H_START));
        String lunchString = cursor.getString(cursor.getColumnIndex(H_LUNCH));
        String returnString = cursor.getString(cursor.getColumnIndex(H_RETURN));
        String stopString = cursor.getString(cursor.getColumnIndex(H_STOP));

        DailyEntry dailyEntry = new DailyEntry(dateString,startString,lunchString, returnString, stopString);
        dailyEntry.calculateTotal();

        return dailyEntry;
    }

    public List<DailyEntry> fetchAllDailyEntryObjects() {
        Cursor cursor = fetchAllDailyEntryRecords();
        ArrayList<DailyEntry> deList = new ArrayList<DailyEntry>();
        if (cursor.isAfterLast()) {
            cursor.close();
            return deList;
        }
        try {
            cursor.moveToFirst();
            deList.add(makeDailyEntryFromCursor(cursor));
            while (cursor.moveToNext()) {
                deList.add(makeDailyEntryFromCursor(cursor));
            }
        } finally {
            cursor.close();
        }

        return deList;
    }

    private Cursor fetchAllDailyEntryRecords() {
        String sql = "select " + H_ROW_ID + " as _id, * from " + DAILY_ENTRY_TABLE;
        Cursor cursor = mDb.rawQuery(sql, new String[] {} );
        cursor.moveToFirst();
        return cursor;
    }

    public void changeDailyEntry(long id, DailyEntry newDailyEntry) {
        newDailyEntry.calculateTotal();
        ContentValues cv = createDailyEntrysCV(newDailyEntry);
        mDb.update(DAILY_ENTRY_TABLE,
                   cv,
                   H_ROW_ID + "=?",
                   new String[] {String.valueOf(id)});
    }

//    public void importDB()
//    {
//        try  {
//            String srcFileName = Environment.getExternalStorageDirectory() + "/ATrac_backup.db";
//            File srcFile = new File(srcFileName);
//            File dstFile = mContext.getDatabasePath(DbAdapter.DATABASE_NAME);
//
//            FileChannel src = new FileInputStream(srcFile).getChannel();
//            FileChannel dst = new FileOutputStream(dstFile).getChannel();
//
//            dst.transferFrom(src, 0, src.size());
//            src.close();
//            dst.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public void exportDB()
//    {
//        try {
//            String dstFileName = Environment.getExternalStorageDirectory() + "/ATrac_backup.db";
//            File dstFile = new File(dstFileName);
//            File srcFile = mContext.getDatabasePath(DbAdapter.DATABASE_NAME);
//
//            FileChannel src = new FileInputStream(srcFile).getChannel();
//            FileChannel dst = new FileOutputStream(dstFile).getChannel();
//
//            dst.transferFrom(src, 0, src.size());
//
//            src.close();
//            dst.close();
//
//            outputToCsv();
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//    }

    // Change to private once tested
    public void outputToCsv() {

        String csvFileName = Environment.getExternalStorageDirectory() + Constants.BACKUP_FILENAME;
        File csvFile = new File(csvFileName);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(csvFile));
            Cursor cursor = fetchAllDailyEntryRecords();
            while (!cursor.isAfterLast()) {
                DailyEntry de = makeDailyEntryFromCursor(cursor);

                String line = de.mDateString.replace(", ", "=") + "," +
                        de.mStartString + "," +
                        de.mLunchString + "," +
                        de.mReturnString + "," +
                        de.mStopString + "\n";
                writer.write(line);
                cursor.moveToNext();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {writer.flush(); writer.close();} catch (Exception e) {}
        }
    }

    public void readFromCSV() {
        String csvFileName = Environment.getExternalStorageDirectory() + Constants.BACKUP_FILENAME;
        File csvFile = new File(csvFileName);

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(csvFile));
            String line;
            mDb.execSQL("DELETE from " + DbAdapter.DAILY_ENTRY_TABLE);
            while ((line = reader.readLine()) !=null ) {
                String[] fieldArray = line.split(",");
                if (fieldArray.length != 5)
                    return;
                String dateString = fieldArray[0].replace("=", ", ");
                DailyEntry de = new DailyEntry(dateString, fieldArray[1], fieldArray[2], fieldArray[3], fieldArray[4]);

                createDailyEntryRecord(de);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
