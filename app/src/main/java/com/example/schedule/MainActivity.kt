package com.example.schedule

import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.core.view.iterator
import androidx.recyclerview.widget.GridLayoutManager
import com.example.schedule.databinding.ActivityMainBinding
import com.example.schedule.db.DatabaseManager

class MainActivity : AppCompatActivity(),
    LessonAdapter.OnItemClickListener { // check OnClickDelete method
    private lateinit var binding: ActivityMainBinding
    private val adapter = LessonAdapter(this)
    private var allDaysLauncher: ActivityResultLauncher<Intent>? = null
    private var isKeyboardVisible = false

    private lateinit var studentsList: MutableList<StudentInfo>

    private var lessonsCount = 0
    private var whatDayIndex = 0

    private val dbManager = DatabaseManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbManager.openDb()
        studentsList = dbManager.readDbData()

        lessonsCount = displayLessonsAndCountIt(whatDayIndex)

        binding.apply {
            rcView.layoutManager = GridLayoutManager(this@MainActivity, 1)
            rcView.adapter = adapter

            whatDayTextView.text = getString(R.string.monday)

            window.decorView.viewTreeObserver.addOnGlobalLayoutListener {
                val rect = Rect()

                window.decorView.getWindowVisibleDisplayFrame(rect)
                val screenHeight = window.decorView.height
                val keypadHeight = screenHeight - rect.bottom

                if (keypadHeight > screenHeight * 0.15)
                    bottomNavigationView.visibility = View.GONE
                else
                    bottomNavigationView.visibility = View.VISIBLE

            }

            navigationView.setNavigationItemSelectedListener {
                menu_init(it) // <------ check inner of this
                drawer.closeDrawer(GravityCompat.START)
                true
            }

            bottomNavigationView.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.saveId -> {
                        saveData()
                        Toast.makeText(
                            this@MainActivity, getString(R.string.saved), Toast.LENGTH_SHORT
                        ).show()
                    }
                    R.id.addLessonId -> {
                        for (item in rcView) {
                            item.findViewById<EditText>(R.id.editNameField).clearFocus()
                            item.findViewById<EditText>(R.id.editTimeField).clearFocus()
                        }

                        correctAdding()
                        lessonsCount++
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

    private fun correctAdding() {
        val tempNamesArray = ArrayList<String>()
        val tempTimeArray = ArrayList<String>()

        for (item in binding.rcView) {
            if (
                item.findViewById<EditText>(R.id.editNameField).text != null &&
                item.findViewById<EditText>(R.id.editTimeField).text != null
            ) {
                tempNamesArray.add(item.findViewById<EditText>(R.id.editNameField).text.toString())
                tempTimeArray.add(item.findViewById<EditText>(R.id.editTimeField).text.toString())
            }
        }

        val lesson = Lesson(lessonsCount, null, null)
        adapter.addLesson(lesson)

        binding.apply {
            for (item in 0 until tempNamesArray.size) {
                rcView[item].findViewById<EditText>(R.id.editNameField)
                    .setText(tempNamesArray[item])
                rcView[item].findViewById<EditText>(R.id.editTimeField).setText(tempTimeArray[item])
            }
        }
    }

    override fun onItemClick(position: Int) {
        val dayString = setDay(whatDayIndex)
        var isDeleteStudent = false
        val tempStudent = StudentInfo(null, null, null, null)

        for (student in studentsList) {
            if (
                student.name.equals(binding.rcView[position].findViewById<EditText>(R.id.editNameField).text.toString())
                &&
                student.time.equals(binding.rcView[position].findViewById<EditText>(R.id.editTimeField).text.toString())
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
                    item.findViewById<EditText>(R.id.editNameField)
                        .setText(studentsList[student].name)
                    item.findViewById<EditText>(R.id.editTimeField)
                        .setText(studentsList[student].time)

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

    private fun saveData() {
        var isHaveThatStudent: Boolean
        val dayString = setDay(whatDayIndex)

        if (studentsList.isNotEmpty()) {
            for (item in studentsList.size - 1 downTo 0) {
                if (studentsList[item].day.equals(dayString)) {
                    studentsList.removeAt(item)
                }
            }
        }

        for (item in binding.rcView) {
            val tempStudent = StudentInfo(null, null, null, null)

            tempStudent.name = item.findViewById<EditText>(R.id.editNameField).text?.toString()
            tempStudent.time = item.findViewById<EditText>(R.id.editTimeField).text?.toString()
            tempStudent.day = dayString

            studentsList.add(tempStudent)
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

    private fun menu_init(it: MenuItem) {
        when (it.itemId) {
            R.id.mondayId -> {
                whatDayIndex = 0
                binding.whatDayTextView.text = getString(R.string.monday)
                clearRcView(lessonsCount)
                lessonsCount = displayLessonsAndCountIt(whatDayIndex)
            }
            R.id.tuesdayId -> {
                whatDayIndex = 1
                binding.whatDayTextView.text = getString(R.string.tuesday)
                clearRcView(lessonsCount)
                lessonsCount = displayLessonsAndCountIt(whatDayIndex)
            }
            R.id.wednesdayId -> {
                whatDayIndex = 2
                binding.whatDayTextView.text = getString(R.string.wednesday)
                clearRcView(lessonsCount)
                lessonsCount = displayLessonsAndCountIt(whatDayIndex)
            }
            R.id.thursdayId -> {
                whatDayIndex = 3
                binding.whatDayTextView.text = getString(R.string.thursday)
                clearRcView(lessonsCount)
                lessonsCount = displayLessonsAndCountIt(whatDayIndex)
            }
            R.id.fridayId -> {
                whatDayIndex = 4
                binding.whatDayTextView.text = getString(R.string.friday)
                clearRcView(lessonsCount)
                lessonsCount = displayLessonsAndCountIt(whatDayIndex)
            }
            R.id.saturdayId -> {
                whatDayIndex = 5
                binding.whatDayTextView.text = getString(R.string.saturday)
                clearRcView(lessonsCount)
                lessonsCount = displayLessonsAndCountIt(whatDayIndex)
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
}