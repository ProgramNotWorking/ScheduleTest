package com.example.schedule

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
    private val studentsList = arrayListOf<StudentInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var whatDayIndex = 0
        var howMuchLessons = 0

        binding.apply {
            rcView.layoutManager = GridLayoutManager(this@MainActivity, 1)
            rcView.adapter = adapter

            navigationView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.mondayId -> {
                        for (item in studentsList) {
                            if (item.day == DaysConstNames.MONDAY) {
                                val lesson = Lesson(null, item.name, item.time)
                                adapter.addLesson(lesson)
                            }
                        }

                        // TODO: Think about that

                        whatDayIndex = 0
                        clearRcView(howMuchLessons)
                        howMuchLessons = 0
                    }
                    R.id.tuesdayId -> {
                        whatDayIndex = 1
                        clearRcView(howMuchLessons)
                        howMuchLessons = 0
                    }
                    R.id.wednesdayId -> {
                        whatDayIndex = 2
                        clearRcView(howMuchLessons)
                        howMuchLessons = 0
                    }
                    R.id.thursdayId -> {
                        whatDayIndex = 3
                        clearRcView(howMuchLessons)
                        howMuchLessons = 0
                    }
                    R.id.fridayId -> {
                        whatDayIndex = 4
                        clearRcView(howMuchLessons)
                        howMuchLessons = 0
                    }
                    R.id.saturdayId -> {
                        whatDayIndex = 5
                        clearRcView(howMuchLessons)
                        howMuchLessons = 0
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
                        saveData(whatDayIndex)
                        Toast.makeText(
                            this@MainActivity, "Saved", Toast.LENGTH_SHORT
                        ).show()
                    }
                    R.id.addLessonId -> {
                        val lesson = Lesson(howMuchLessons, null, null)
                        adapter.addLesson(lesson)
                        howMuchLessons++
                    }
                }

                true
            }
        }
    }

    private fun saveData(whatDay: Int) {
        var isHaveThatStudent: Boolean

        for (item in binding.rcView) {
            val tempStudent = StudentInfo(null, null, null)
            isHaveThatStudent = false

            tempStudent.name = item.findViewById<EditText>(R.id.editNameField).text.toString()
            tempStudent.time = item.findViewById<EditText>(R.id.editTimeField).text.toString()

            when (whatDay) {
                0 -> tempStudent.day = DaysConstNames.MONDAY
                1 -> tempStudent.day = DaysConstNames.TUESDAY
                2 -> tempStudent.day = DaysConstNames.WEDNESDAY
                3 -> tempStudent.day = DaysConstNames.THURSDAY
                4 -> tempStudent.day = DaysConstNames.FRIDAY
                5 -> tempStudent.day = DaysConstNames.SATURDAY
            }

            for (student in studentsList) {
                if (
                    student.name == tempStudent.name &&
                    student.time == tempStudent.time &&
                    student.day == tempStudent.day
                ) {
                    isHaveThatStudent = true
                }
            }

            if (!isHaveThatStudent) {
                studentsList.add(tempStudent)
            }
        }
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