package com.example.schedule

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MyDao {

    @Insert
    fun insertAll(vararg student: Student)

    @Query("SELECT * FROM student")
    fun getAllStudents(): List<Student>

    @Update
    fun updateAll(myData: List<Student>)

}