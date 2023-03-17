package com.example.schedule.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class StudentsDatabaseHelper(context: Context): SQLiteOpenHelper(
    context, DatabaseNames.TABLE_NAME, null, DatabaseNames.DATABASE_VERSION
) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(DatabaseNames.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL(DatabaseNames.SQL_DELETE_ENTRIES)
        onCreate(db)
    }
}