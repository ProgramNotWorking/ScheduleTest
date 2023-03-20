package com.example.schedule

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.core.view.iterator
import androidx.recyclerview.widget.GridLayoutManager
import com.example.schedule.databinding.ActivityMainBinding
import com.example.schedule.db.DatabaseManager

class MainActivity : AppCompatActivity(), LessonAdapter.OnItemClickListener,
    LessonAdapter.OnEditClickListener {
    private lateinit var binding: ActivityMainBinding
    private val adapter = LessonAdapter(this, this)
    private var allDaysLauncher: ActivityResultLauncher<Intent>? = null
    private lateinit var editStudentInfoLauncher: ActivityResultLauncher<Intent>

    private lateinit var studentsList: MutableList<StudentInfo>
    private lateinit var countLessonsList: MutableList<Int>

    private var lessonsCount = 0
    private var whatDayIndex = 0
    private var editItemPosition = 0

    private val dbManager = DatabaseManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbManager.openDb()
        studentsList = dbManager.readDbData()

        countLessonsList = fillLessonsIndexesList()

        val testArray = ArrayList<String?>()
        for (student in studentsList) {
            testArray.add(student.day)
        }



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

                        lessonsCount++
                    }
                    R.id.open_menu -> {
                        drawer.openDrawer(GravityCompat.START)

                        Log.d("SUKA", testArray.toString())
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
                            binding.rcView[editItemPosition].findViewById<TextView>(R.id.nameTextViewItem).text =
                                intent.getStringExtra(IntentConstaces.NAME_EDIT)
                            binding.rcView[editItemPosition].findViewById<TextView>(R.id.timeTextViewItem).text =
                                intent.getStringExtra(IntentConstaces.TIME_EDIT)

                            val tempStudent = StudentInfo(
                                null,
                                intent.getStringExtra(IntentConstaces.NAME_CHANGE),
                                intent.getStringExtra(IntentConstaces.TIME_CHANGE),
                                intent.getStringExtra(IntentConstaces.DAY)
                            )

                            for (item in 0 until studentsList.size) {
                                if (
                                    studentsList[item].name.equals(tempStudent.name) &&
                                    studentsList[item].time.equals(tempStudent.time) &&
                                    studentsList[item].day.equals(tempStudent.day)
                                ) {
                                    studentsList[item] = tempStudent
                                    break
                                }
                            }
                        } else {
                            countLessonsList[whatDayIndex]++

                            val tempStudent = StudentInfo(
                                null,
                                intent.getStringExtra(IntentConstaces.NAME_CHANGE),
                                intent.getStringExtra(IntentConstaces.TIME_CHANGE),
                                intent.getStringExtra(IntentConstaces.DAY)
                            )

                            studentsList.add(tempStudent)

                            val lesson = Lesson(
                                null,
                                intent.getStringExtra(IntentConstaces.NAME_CHANGE),
                                intent.getStringExtra(IntentConstaces.TIME_CHANGE)
                            )

                            adapter.addLesson(lesson)
                            lessonsCount++
                        }
                    }
                }
        }
    }

    override fun onStop() {
        super.onStop()
        dbManager.insertToDb(studentsList)
    }

    override fun onDestroy() {
        super.onDestroy()
        dbManager.close()
    }

    override fun onEditItemClick(position: Int) {
        val intent = Intent(this@MainActivity, EditStudentInfoActivity::class.java)
        intent.putExtra(
            IntentConstaces.NAME_EDIT,
            binding.rcView[position].findViewById<TextView>(R.id.nameTextViewItem).text.toString()
        )
        intent.putExtra(
            IntentConstaces.TIME_EDIT,
            binding.rcView[position].findViewById<TextView>(R.id.timeTextViewItem).text.toString()
        )
        intent.putExtra("whatDay", whatDayIndex)
        intent.putExtra(IntentConstaces.IS_CHANGED, true)

        editItemPosition = position
        editStudentInfoLauncher.launch(intent)
    }

    override fun onItemClick(position: Int) {
        val dayString = setDay(whatDayIndex)
        var isDeleteStudent = false
        val tempStudent = StudentInfo(null, null, null, null)

        countLessonsList[whatDayIndex]--

        for (student in studentsList) {
            if (
                student.name.equals(
                    binding.rcView[position].findViewById<TextView>(R.id.nameTextViewItem).text.toString()
                )
                &&
                student.time.equals(
                    binding.rcView[position].findViewById<TextView>(R.id.timeTextViewItem).text.toString()
                )
                &&
                student.day.equals(dayString)
            ) {
                isDeleteStudent = true
                tempStudent.name = student.name
                tempStudent.time = student.time
                tempStudent.day = student.day

                break
            }
        }

        if (isDeleteStudent) {
            for (item in 0 until studentsList.size) {
                if (
                    studentsList[item].name.equals(tempStudent.name) &&
                    studentsList[item].time.equals(tempStudent.time) &&
                    studentsList[item].day.equals(tempStudent.day)
                ) {
                    studentsList.removeAt(item)
                    break
                }
            }
        }

        adapter.removeLesson(position)
        lessonsCount -= 1

        var isFindNeededStudent = false
        var neededStudentIndex = 0
        for (item in binding.rcView) {
            for (student in neededStudentIndex until studentsList.size) {
                if (studentsList[student].day.equals(dayString)) {
                    item.findViewById<TextView>(R.id.nameTextViewItem).text =
                        studentsList[student].name
                    item.findViewById<TextView>(R.id.timeTextViewItem).text =
                        studentsList[student].time

                    isFindNeededStudent = true
                }
                if (isFindNeededStudent) {
                    neededStudentIndex++
                    break
                } else
                    neededStudentIndex++
            }

            isFindNeededStudent = false
        }
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

//
//        var timesCount = 0
//
//        if (countLessonsList[whatDayIndex] != -1) {
//            for (student in studentsList) {
//                if (student.day.equals(setDay(whatDayIndex))) {
//                    timesCount++
//                }
//            }
//        }
//
//        for (item in timesCount - 1 until 0) {
//            adapter.removeLesson(item)
//        }
    }

    private fun menuInit(it: MenuItem) {
        when (it.itemId) {
            R.id.mondayId -> {
                clearRcView()
                whatDayIndex = 0
                displayLessons()
                binding.whatDayTextView.text = getString(R.string.monday)
            }
            R.id.tuesdayId -> {
                clearRcView()
                whatDayIndex = 1
                displayLessons()
                binding.whatDayTextView.text = getString(R.string.tuesday)
            }
            R.id.wednesdayId -> {
                clearRcView()
                whatDayIndex = 2
                displayLessons()
                binding.whatDayTextView.text = getString(R.string.wednesday)
            }
            R.id.thursdayId -> {
                clearRcView()
                whatDayIndex = 3
                displayLessons()
                binding.whatDayTextView.text = getString(R.string.thursday)
            }
            R.id.fridayId -> {
                clearRcView()
                whatDayIndex = 4
                displayLessons()
                binding.whatDayTextView.text = getString(R.string.friday)
            }
            R.id.saturdayId -> {
                clearRcView()
                whatDayIndex = 5
                displayLessons()
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
}