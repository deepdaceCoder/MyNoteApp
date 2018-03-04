package com.arghadeep.mynoteapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Arghadeep on 26-01-2018.
 */

public class MyNoteAppDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "mynoteapp";
    private static final int DB_VERSION = 3;

    MyNoteAppDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion<1) {
            db.execSQL("CREATE TABLE NOTE (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "TITLE TEXT, "
            + "DESCRIPTION TEXT); ");
        }
        if(oldVersion<2) {
            db.execSQL("ALTER TABLE NOTE ADD COLUMN MODE TEXT");
        }
        if(oldVersion<3) {
            db.execSQL("ALTER TABLE NOTE ADD COLUMN DATE TEXT");
        }
    }
}
