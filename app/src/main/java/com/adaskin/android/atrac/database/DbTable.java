package com.adaskin.android.atrac.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Dave on 5/25/2017.
 */

class DbTable {
    private static final String DAILY_ENTRY_TABLE_CONTENTS =
            " (" +
            DbAdapter.H_ROW_ID  + " integer primary key autoincrement, " +
            DbAdapter.H_DATE    + " text not null, " +
            DbAdapter.H_START   + " text, " +
            DbAdapter.H_LUNCH   + " text, " +
            DbAdapter.H_RETURN  + " text, " +
            DbAdapter.H_STOP    + " text); ";

    private static final String DAILY_ENTRY_TABLE_CREATE = "create table " +
            DbAdapter.DAILY_ENTRY_TABLE +
            DAILY_ENTRY_TABLE_CONTENTS;


    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DAILY_ENTRY_TABLE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldRev, int newRev) { }
}
