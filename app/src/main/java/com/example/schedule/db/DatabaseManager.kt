package com.example.schedule.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.schedule.DatabaseHelper
import com.example.schedule.StudentInfo

class DatabaseManager(context: Context) {
    private val dbHelper = StudentsDatabaseHelper(context)
    private var db: SQLiteDatabase? = null

    fun openDb() {
        db = dbHelper.writableDatabase
    }

    private fun addStudent(student: StudentInfo) {
        val values = ContentValues().apply {
            put(DatabaseNames.COLUMN_NAME, student.name)
            put(DatabaseNames.COLUMN_TIME, student.time)
            put(DatabaseNames.COLUMN_DAY, student.day)
        }
        db?.insert(DatabaseNames.TABLE_NAME, null, values)
    }

    fun insertToDb(studentsList: MutableList<StudentInfo>?) {
        db?.beginTransaction()
        try {
            db?.delete(DatabaseNames.TABLE_NAME, null, null)
            if (studentsList != null) {
                for (student in studentsList) {
                    addStudent(student)
                }
            }
            db?.setTransactionSuccessful()
        } finally {
            db?.endTransaction()
        }
    }

    @SuppressLint("Range")
    fun readDbData(): MutableList<StudentInfo> {
        val dataList = ArrayList<StudentInfo>()

        val cursor = db?.query(
            DatabaseNames.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )

        with(cursor) {
            while (this?.moveToNext()!!) {
                val dataName = cursor?.getString(cursor.getColumnIndex(DatabaseNames.COLUMN_NAME))
                val dataTime = cursor?.getString(cursor.getColumnIndex(DatabaseNames.COLUMN_TIME))
                val dataDay = cursor?.getString(cursor.getColumnIndex(DatabaseNames.COLUMN_DAY))

                val dataStudent = StudentInfo(null, dataName, dataTime, dataDay)
                dataList.add(dataStudent)
            }
        }

        cursor?.close()
        return dataList
    }

    fun clear() {
        db?.delete(DatabaseNames.TABLE_NAME, null, null)
    }

    fun close() {
        dbHelper.close()
    }
}