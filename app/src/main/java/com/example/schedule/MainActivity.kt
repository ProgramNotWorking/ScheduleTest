package com.example.schedule

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.core.view.iterator
import androidx.recyclerview.widget.GridLayoutManager
import com.example.schedule.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter = LessonAdapter()

    private lateinit var studentsList: MutableList<StudentInfo>

    private var lessonsCount = 0
    private var whatDayIndex = 0

    private val dbHelper = DatabaseHelper(this) // TODO: Check ChatGPT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        studentsList = dbHelper.getAllStudents()

        binding.apply {
            rcView.layoutManager = GridLayoutManager(this@MainActivity, 1)
            rcView.adapter = adapter

            navigationView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.mondayId -> {
                        whatDayIndex = 0
                        clearRcView(lessonsCount)
                        lessonsCount = displayLessonsAndCountIt(whatDayIndex)
                    }
                    R.id.tuesdayId -> {
                        whatDayIndex = 1
                        clearRcView(lessonsCount)
                        lessonsCount = displayLessonsAndCountIt(whatDayIndex)
                    }
                    R.id.wednesdayId -> {
                        whatDayIndex = 2
                        clearRcView(lessonsCount)
                        lessonsCount = displayLessonsAndCountIt(whatDayIndex)
                    }
                    R.id.thursdayId -> {
                        whatDayIndex = 3
                        clearRcView(lessonsCount)
                        lessonsCount = displayLessonsAndCountIt(whatDayIndex)
                    }
                    R.id.fridayId -> {
                        whatDayIndex = 4
                        clearRcView(lessonsCount)
                        lessonsCount = displayLessonsAndCountIt(whatDayIndex)
                    }
                    R.id.saturdayId -> {
                        whatDayIndex = 5
                        clearRcView(lessonsCount)
                        lessonsCount = displayLessonsAndCountIt(whatDayIndex)
                    }
                    R.id.allDaysId -> {
                        Toast.makeText(
                            this@MainActivity, "Not done yet", Toast.LENGTH_SHORT
                        ).show()

                        // TODO: Work on it(oh, rly?)
                    }
                }
                drawer.closeDrawer(GravityCompat.START)

                true
            }

            bottomNavigationView.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.saveId -> {
                        saveData()
                        Toast.makeText(
                            this@MainActivity, "Saved", Toast.LENGTH_SHORT
                        ).show()
                    }
                    R.id.addLessonId -> {
                        val lesson = Lesson(lessonsCount, null, null)
                        adapter.addLesson(lesson)
                        lessonsCount++
                    }
                    R.id.open_menu -> {
                        drawer.openDrawer(GravityCompat.START)
                    }
                }

                true
            }
        }
    }

    override fun onDestroy() {
        dbHelper.repopulateDatabase(studentsList)
        dbHelper.close()
        super.onDestroy()
    }

    private fun temp() {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            for (item in studentsList) {
                put("name", item.name)
                put("time", item.time)
                put("day", item.day)
            }
        }
        db.insert("my_table", null, values)

        val projection = arrayOf("id", "name", "time", "day")
        val cursor = db.query(
            "my_table",
            projection,
            null,
            null,
            null,
            null,
            null
        )

        cursor.close()
        db.close()
    }

    private fun saveData() {
        var isHaveThatStudent: Boolean
        var dayString: String = ""

        when (whatDayIndex) {
            0 -> dayString = DaysConstNames.MONDAY
            1 -> dayString = DaysConstNames.TUESDAY
            2 -> dayString = DaysConstNames.WEDNESDAY
            3 -> dayString = DaysConstNames.THURSDAY
            4 -> dayString = DaysConstNames.FRIDAY
            5 -> dayString = DaysConstNames.SATURDAY
        }

        if (studentsList.isNotEmpty()) {
            for (item in studentsList.size - 1 downTo 0) {
                if (studentsList[item].day.equals(dayString)) {
                    studentsList.removeAt(item)
                }
            }
        }

        for (item in binding.rcView) {
            val tempStudent = StudentInfo(null, null, null, null)
            isHaveThatStudent = false

            tempStudent.name = item.findViewById<EditText>(R.id.editNameField).text?.toString()
            tempStudent.time = item.findViewById<EditText>(R.id.editTimeField).text?.toString()

            when (whatDayIndex) {
                0 -> tempStudent.day = DaysConstNames.MONDAY
                1 -> tempStudent.day = DaysConstNames.TUESDAY
                2 -> tempStudent.day = DaysConstNames.WEDNESDAY
                3 -> tempStudent.day = DaysConstNames.THURSDAY
                4 -> tempStudent.day = DaysConstNames.FRIDAY
                5 -> tempStudent.day = DaysConstNames.SATURDAY
            }

            for (student in studentsList) {
                if (
                    student.name.equals(tempStudent.name) &&
                    student.time.equals(tempStudent.time) &&
                    student.day.equals(tempStudent.day)
                ) {
                    isHaveThatStudent = true
                }
            }

            if (!isHaveThatStudent) {
                studentsList.add(tempStudent)
            }
        }
    }

    private fun displayLessonsAndCountIt(dayIndex: Int): Int {
        var lessonsIndex = 0

        when (dayIndex) {
            0 -> {
                for (item in studentsList) {
                    if (item.day == DaysConstNames.MONDAY) {
                        val lesson = Lesson(null, item.name, item.time)
                        adapter.addLesson(lesson)
                        lessonsIndex++
                    }
                }
            }
            1 -> {
                for (item in studentsList) {
                    if (item.day == DaysConstNames.TUESDAY) {
                        val lesson = Lesson(null, item.name, item.time)
                        adapter.addLesson(lesson)
                        lessonsIndex++

                    }
                }
            }
            2 -> {
                for (item in studentsList) {
                    if (item.day == DaysConstNames.WEDNESDAY) {
                        val lesson = Lesson(null, item.name, item.time)
                        adapter.addLesson(lesson)
                        lessonsIndex++
                    }
                }
            }
            3 -> {
                for (item in studentsList) {
                    if (item.day == DaysConstNames.THURSDAY) {
                        val lesson = Lesson(null, item.name, item.time)
                        adapter.addLesson(lesson)
                        lessonsIndex++
                    }
                }
            }
            4 -> {
                for (item in studentsList) {
                    if (item.day == DaysConstNames.FRIDAY) {
                        val lesson = Lesson(null, item.name, item.time)
                        adapter.addLesson(lesson)
                        lessonsIndex++
                    }
                }
            }
            5 -> {
                for (item in studentsList) {
                    if (item.day == DaysConstNames.SATURDAY) {
                        val lesson = Lesson(null, item.name, item.time)
                        adapter.addLesson(lesson)
                        lessonsIndex++
                    }
                }
            }
        }

        return lessonsIndex
    }

    private fun clearRcView(countLessons: Int) {
        var tempIndexOfPosition = countLessons - 1

        binding.apply {
            repeat(countLessons) {
                adapter.removeLesson(tempIndexOfPosition)
                tempIndexOfPosition -= 1
            }
        }
    }
}