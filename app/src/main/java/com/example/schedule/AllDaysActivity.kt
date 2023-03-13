package com.example.schedule

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.core.view.iterator
import androidx.core.view.size
import androidx.recyclerview.widget.GridLayoutManager
import com.example.schedule.databinding.ActivityAllDaysBinding

class AllDaysActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAllDaysBinding
    private val mondayAdapter = AllDaysAdapter()
    private val tuesdayAdapter = AllDaysAdapter()
    private val wednesdayAdapter = AllDaysAdapter()
    private val thursdayAdapter = AllDaysAdapter()
    private val fridayAdapter = AllDaysAdapter()
    private val saturdayAdapter = AllDaysAdapter()

    private lateinit var namesArray: ArrayList<String>
    private lateinit var timeArray: ArrayList<String>
    private lateinit var daysArray: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllDaysBinding.inflate(layoutInflater)
        setContentView(binding.root)

        namesArray = intent.getStringArrayListExtra("names") as ArrayList<String>
        timeArray = intent.getStringArrayListExtra("time") as ArrayList<String>
        daysArray = intent.getStringArrayListExtra("days") as ArrayList<String>

        _init_() // work with only first

        Log.d("Test:", binding.saturdayDaysRcView.size.toString())

        binding.apply {
            returnButton.setOnClickListener {
                setResult(
                    RESULT_OK, Intent(this@AllDaysActivity, MainActivity::class.java)
                )
                finish()
            }
        }
    }

    private fun _init_() {
        binding.apply {
            mondayDaysRcView.layoutManager = GridLayoutManager(this@AllDaysActivity, 1)
            tuesdayDaysRcView.layoutManager = GridLayoutManager(this@AllDaysActivity, 1)
            wednesdayDaysRcView.layoutManager = GridLayoutManager(this@AllDaysActivity, 1)
            thursdayDaysRcView.layoutManager = GridLayoutManager(this@AllDaysActivity, 1)
            fridayDaysRcView.layoutManager = GridLayoutManager(this@AllDaysActivity, 1)
            saturdayDaysRcView.layoutManager = GridLayoutManager(this@AllDaysActivity, 1)

            mondayDaysRcView.adapter = mondayAdapter
            tuesdayDaysRcView.adapter = tuesdayAdapter
            wednesdayDaysRcView.adapter = wednesdayAdapter
            thursdayDaysRcView.adapter = thursdayAdapter
            fridayDaysRcView.adapter = fridayAdapter
            saturdayDaysRcView.adapter = saturdayAdapter

            for (item in 0 until daysArray.size) {
                when (daysArray[item]) {
                    DaysConstNames.MONDAY -> {
                        val lesson = Lesson(null, namesArray[item], timeArray[item])
                        mondayAdapter.addLessonInfo(lesson)
                    }
                    DaysConstNames.TUESDAY -> {
                        val lesson = Lesson(null, namesArray[item], timeArray[item])
                        tuesdayAdapter.addLessonInfo(lesson)
                    }
                    DaysConstNames.WEDNESDAY -> {
                        val lesson = Lesson(null, namesArray[item], timeArray[item])
                        wednesdayAdapter.addLessonInfo(lesson)
                    }
                    DaysConstNames.THURSDAY -> {
                        val lesson = Lesson(null, namesArray[item], timeArray[item])
                        thursdayAdapter.addLessonInfo(lesson)
                    }
                    DaysConstNames.FRIDAY -> {
                        val lesson = Lesson(null, namesArray[item], timeArray[item])
                        fridayAdapter.addLessonInfo(lesson)
                    }
                    DaysConstNames.SATURDAY -> {
                        val lesson = Lesson(null, namesArray[item], timeArray[item])
                        saturdayAdapter.addLessonInfo(lesson)
                    }
                }
            }
        }
    }
}