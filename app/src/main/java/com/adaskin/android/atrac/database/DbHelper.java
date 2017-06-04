package com.adaskin.android.atrac.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static DbHelper mInstance = null;

    private  DbHelper(Context context) {
        super(context, DbAdapter.DATABASE_NAME, null, DbAdapter.DATABASE_VERSION);
    }


    public static DbHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DbHelper(context.getApplicationContext());
        }

        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        DbTable.onCreate(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldRev, int newRev) {
        DbTable.onUpgrade(database, oldRev, newRev);
    }
}

