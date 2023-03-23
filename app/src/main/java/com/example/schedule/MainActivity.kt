package com.example.schedule

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import androidx.core.view.iterator
import androidx.recyclerview.widget.GridLayoutManager
import com.example.schedule.databinding.ActivityMainBinding
import com.example.schedule.db.DatabaseManager
import java.time.LocalTime

class MainActivity : AppCompatActivity(), LessonAdapter.OnItemClickListener,
    LessonAdapter.OnEditClickListener {
    private lateinit var binding: ActivityMainBinding
    private val adapter = LessonAdapter(this, this)
    private var allDaysLauncher: ActivityResultLauncher<Intent>? = null
    private lateinit var editStudentInfoLauncher: ActivityResultLauncher<Intent>

    private lateinit var studentsList: MutableList<StudentInfo>
    private lateinit var countLessonsList: MutableList<Int>

    private var nameCrutch = "SUS"
    private var timeCrutch = "-_-"

    private var whatDayIndex = 0

    private val dbManager = DatabaseManager(this)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbManager.openDb()
        studentsList = dbManager.readDbData()
        dbManager.close()

        displayLessons()

        countLessonsList = fillLessonsIndexesList()

        binding.apply {
            rcView.layoutManager = GridLayoutManager(this@MainActivity, 1)
            rcView.adapter = adapter

            whatDayTextView.text = getString(R.string.monday)

            navigationView.setNavigationItemSelectedListener {
                menuInit(it)
                drawer.closeDrawer(GravityCompat.START)
                true
            }

            bottomNavigationView.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.addLessonId -> {
                        val intent = Intent(
                            this@MainActivity, EditStudentInfoActivity::class.java
                        )
                        intent.putExtra("whatDay", whatDayIndex)
                        intent.putExtra(IntentConstaces.IS_CHANGED, false)
                        editStudentInfoLauncher.launch(intent)
                    }
                    R.id.open_menu -> {
                        drawer.openDrawer(GravityCompat.START)
                    }
                }

                true
            }

            allDaysLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                    if (result.resultCode == RESULT_OK) {
                        Log.d("Result Log", "Fine")
                    }
                }

            editStudentInfoLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                    if (result.resultCode == RESULT_OK) {
                        intent = result.data

                        if (intent.getBooleanExtra(IntentConstaces.IS_CHANGED_FROM_INFO, false)) {
                            for (item in binding.rcView) {
                                if (
                                    item.findViewById<TextView>(R.id.nameTextViewItem).text.toString() ==
                                    nameCrutch &&
                                    item.findViewById<TextView>(R.id.timeTextViewItem).text.toString() ==
                                    timeCrutch
                                ) {
                                    item.findViewById<TextView>(R.id.nameTextViewItem).text =
                                        intent.getStringExtra(IntentConstaces.NAME_EDIT)
                                    item.findViewById<TextView>(R.id.timeTextViewItem).text =
                                        intent.getStringExtra(IntentConstaces.TIME_EDIT)

                                    break
                                }
                            }

                            val tempStudent = StudentInfo(
                                null,
                                intent.getStringExtra(IntentConstaces.NAME_EDIT),
                                intent.getStringExtra(IntentConstaces.TIME_EDIT),
                                intent.getStringExtra(IntentConstaces.DAY_EDIT)
                            )

                            for (item in studentsList.indices) {
                                if (
                                    studentsList[item].name.equals(nameCrutch) &&
                                    studentsList[item].time.equals(timeCrutch) &&
                                    studentsList[item].day.equals(setDay(whatDayIndex))
                                ) {
                                    studentsList[item] = tempStudent
                                    break
                                }
                            }

                            clearRcView()
                            displayLessons()
                        } else {
                            countLessonsList[whatDayIndex]++

                            val tempStudent = StudentInfo(
                                null,
                                intent.getStringExtra(IntentConstaces.NAME_SET),
                                intent.getStringExtra(IntentConstaces.TIME_SET),
                                intent.getStringExtra(IntentConstaces.DAY_SET)
                            )

                            studentsList.add(tempStudent)

                            val lesson = Lesson(
                                null,
                                intent.getStringExtra(IntentConstaces.NAME_SET),
                                intent.getStringExtra(IntentConstaces.TIME_SET)
                            )

                            adapter.addLesson(lesson)
                        }
                    }
                }
        }
    }

    override fun onStop() {
        super.onStop()
        dbManager.openDb()
        dbManager.insertToDb(studentsList)
    }

    override fun onDestroy() {
        super.onDestroy()
        dbManager.close()
    }

    private fun displayLessons() {
        for (student in studentsList) {
            if (student.day.equals(setDay(whatDayIndex))) {
                val lesson = Lesson(null, student.name, student.time)
                adapter.addLesson(lesson)
            }
        }
    }

    private fun clearRcView() {
        var deletingIndex = -1

        for (student in studentsList) {
            if (student.day.equals(setDay(whatDayIndex))) {
                deletingIndex++
            }
        }

        if (deletingIndex != -1) {
            for (item in deletingIndex downTo 0) {
                adapter.removeLesson(item)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun menuInit(it: MenuItem) {
        when (it.itemId) {
            R.id.mondayId -> {
                clearRcView()
                whatDayIndex = 0
                // studentsList = sortDataList()
                // displayLessons()
                displaySortedByTime()
                binding.whatDayTextView.text = getString(R.string.monday)
            }
            R.id.tuesdayId -> {
                clearRcView()
                whatDayIndex = 1
                displaySortedByTime()
                binding.whatDayTextView.text = getString(R.string.tuesday)
            }
            R.id.wednesdayId -> {
                clearRcView()
                whatDayIndex = 2
                displaySortedByTime()
                binding.whatDayTextView.text = getString(R.string.wednesday)
            }
            R.id.thursdayId -> {
                clearRcView()
                whatDayIndex = 3
                studentsList = sortDataList()
                displaySortedByTime()
                binding.whatDayTextView.text = getString(R.string.thursday)
            }
            R.id.fridayId -> {
                clearRcView()
                whatDayIndex = 4
                displaySortedByTime()
                binding.whatDayTextView.text = getString(R.string.friday)
            }
            R.id.saturdayId -> {
                clearRcView()
                whatDayIndex = 5
                displaySortedByTime()
                binding.whatDayTextView.text = getString(R.string.saturday)
            }
            R.id.allDaysId -> {
                val intent = Intent(
                    this@MainActivity, AllDaysActivity::class.java
                )
                val namesArray = ArrayList<String>()
                val timeArray = ArrayList<String>()
                val daysArray = ArrayList<String>()

                for (student in studentsList) { // puts 3 item max
                    namesArray.add(student.name.toString())
                    timeArray.add(student.time.toString())
                    daysArray.add(student.day.toString())
                }

                intent.putExtra("names", namesArray)
                intent.putExtra("time", timeArray)
                intent.putExtra("days", daysArray)

                allDaysLauncher?.launch(intent)
            }
        }
    }

    private fun setDay(day: Int): String? {
        return when (day) {
            0 -> DaysConstNames.MONDAY
            1 -> DaysConstNames.TUESDAY
            2 -> DaysConstNames.WEDNESDAY
            3 -> DaysConstNames.THURSDAY
            4 -> DaysConstNames.FRIDAY
            5 -> DaysConstNames.SATURDAY
            else -> null
        }
    }

    private fun fillLessonsIndexesList(): MutableList<Int> {
        val dataList = mutableListOf(-1, -1, -1, -1, -1, -1)

        for (student in studentsList) {
            when (student.day) {
                DaysConstNames.MONDAY ->
                    dataList[0]++
                DaysConstNames.TUESDAY ->
                    dataList[1]++
                DaysConstNames.WEDNESDAY ->
                    dataList[2]++
                DaysConstNames.THURSDAY ->
                    dataList[3]++
                DaysConstNames.FRIDAY ->
                    dataList[4]++
                DaysConstNames.SATURDAY ->
                    dataList[5]++
            }
        }

        return dataList
    }

    override fun onEditItemClick(lesson: Lesson) {
        val intent = Intent(this@MainActivity, EditStudentInfoActivity::class.java)
        intent.putExtra(IntentConstaces.NAME_EDIT, lesson.studentName)
        intent.putExtra(IntentConstaces.TIME_EDIT, lesson.lessonTime)
        intent.putExtra("whatDay", whatDayIndex)
        intent.putExtra(IntentConstaces.IS_CHANGED, true)

        nameCrutch = lesson.studentName.toString()
        timeCrutch = lesson.lessonTime.toString()

        editStudentInfoLauncher.launch(intent)
    }

    override fun onItemClick(lesson: Lesson) {
        binding.apply {
            adapter.removeLessonByString(lesson.studentName, lesson.lessonTime)

            for (student in studentsList.indices) {
                if (studentsList[student].name.equals(lesson.studentName)
                    &&
                    studentsList[student].time.equals(lesson.lessonTime)
                ) {
                    studentsList.removeAt(student)
                    break
                }
            }
        }
    }

    private fun displaySortedByTime() {
        val dataList = mutableListOf<StudentInfo>()

        for (item in studentsList) {
            if (item.day.equals(setDay(whatDayIndex))) {
                dataList.add(item)
            }
        }

        val sortedArrayInt = ArrayList<Int>()
        for (item in dataList) {
            item.time?.replace("-", "")?.toInt()?.let { sortedArrayInt.add(it) }
        }

        sortedArrayInt.sort()
        if (sortedArrayInt.isNotEmpty()) {
            for (item in sortedArrayInt) {
                for (student in dataList) {
                    val time: String = if (item % 100 == 0) {
                        (item / 100).toString() + "-00"
                    } else {
                        (item / 100).toString() + "-" + (item % 100).toString()
                    }

                    if (time == student.time) {
                        val lesson = Lesson(null, student.name, student.time)
                        adapter.addLesson(lesson)
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sortDataList(): MutableList<StudentInfo> {
        val dataList = mutableListOf<StudentInfo>()
        var whatDay = 0

        val mondayArray = ArrayList<StudentInfo>()
        val tuesdayArray = ArrayList<StudentInfo>()
        val wednesdayArray = ArrayList<StudentInfo>()
        val thursdayArray = ArrayList<StudentInfo>()
        val fridayArray = ArrayList<StudentInfo>()
        val saturdayArray = ArrayList<StudentInfo>()

        repeat(6) {
            for (student in studentsList) {
                if (student.day.equals(setDay(whatDay))) {
                    when (whatDay) {
                        0 -> mondayArray.add(student)
                        1 -> tuesdayArray.add(student)
                        2 -> wednesdayArray.add(student)
                        3 -> thursdayArray.add(student)
                        4 -> fridayArray.add(student)
                        5 -> saturdayArray.add(student)
                    }
                }
            }
            whatDay++
        }

        addToDataList(dataList, mondayArray)
        addToDataList(dataList, tuesdayArray)
        addToDataList(dataList, wednesdayArray)
        addToDataList(dataList, thursdayArray)
        addToDataList(dataList, fridayArray)
        addToDataList(dataList, saturdayArray)

        return dataList
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sortItemByTime(someDayArray: ArrayList<StudentInfo>): List<String> {
        val timeArray = listOf<String>()

        for (item in someDayArray) {
            timeArray.plus(item.time)
        }

        val sortedTimeArray = timeArray.map {
            LocalTime.parse(it.replace("-", ":"))
        }.sorted().map {
            it.toString().replace(":", "-")
        }

        return sortedTimeArray
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addToDataList(dataList: MutableList<StudentInfo>, someDayArray: ArrayList<StudentInfo>) {
        for (item in sortItemByTime(someDayArray)) {
            for (student in someDayArray) {
                if (student.time.equals(item)) {
                    dataList.add(student)
                }
            }
        }
    }
}