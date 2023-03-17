package com.example.schedule.db

import android.provider.BaseColumns

object DatabaseNames: BaseColumns {
    const val TABLE_NAME = "students_table"
    const val COLUMN_NAME = "name"
    const val COLUMN_TIME = "time"
    const val COLUMN_DAY = "day"

    const val DATABASE_VERSION = 1
    const val DATABASE = "Students.db"

    const val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
            "$COLUMN_NAME TEXT, $COLUMN_TIME TEXT, $COLUMN_DAY TEXT)"
    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"
}