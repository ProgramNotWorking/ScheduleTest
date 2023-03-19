package com.example.schedule

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.example.schedule.databinding.ActivityEditStudentInfoBinding

class EditStudentInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditStudentInfoBinding
    private var studentName: String? = null
    private var lessonTime: String? = null
    private lateinit var whatDay: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditStudentInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        binding.apply {
            setDayTextView()
            setOnTouchCloseKeyboard()

            if (intent.getBooleanExtra(IntentConstaces.IS_CHANGED, false)) {
                nameSetTextField.setText(intent.getStringExtra(IntentConstaces.NAME_EDIT))
                timeSetTextField.setText(intent.getStringExtra(IntentConstaces.TIME_EDIT))

                saveButton.setOnClickListener {
                    studentName = findViewById<EditText>(R.id.nameSetTextField).text.toString()
                    lessonTime = findViewById<EditText>(R.id.timeSetTextField).text.toString()

                    val intent = Intent(
                        this@EditStudentInfoActivity, MainActivity::class.java
                    )
                    intent.putExtra(IntentConstaces.NAME_EDIT, studentName)
                    intent.putExtra(IntentConstaces.TIME_EDIT, lessonTime)
                    intent.putExtra(IntentConstaces.DAY_EDIT, whatDay)
                    intent.putExtra(IntentConstaces.IS_CHANGED_FROM_INFO, true)

                    setResult(RESULT_OK, intent)
                    finish()
                }
            } else {
//                nameSetTextField.setText(intent.getStringExtra(IntentConstaces.NAME_CHANGE))
//                timeSetTextField.setText(intent.getStringExtra(IntentConstaces.TIME_CHANGE))

                saveButton.setOnClickListener {
                    studentName = findViewById<EditText>(R.id.nameSetTextField).text.toString()
                    lessonTime = findViewById<EditText>(R.id.timeSetTextField).text.toString()

                    val intent = Intent(
                        this@EditStudentInfoActivity, MainActivity::class.java
                    )
                    intent.putExtra(IntentConstaces.NAME_CHANGE, studentName)
                    intent.putExtra(IntentConstaces.TIME_CHANGE, lessonTime)
                    intent.putExtra(IntentConstaces.DAY, whatDay)
                    intent.putExtra(IntentConstaces.IS_CHANGED_FROM_INFO, false)

                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }
    }

    private fun setDayTextView() {
        binding.apply {
            when (intent.getIntExtra("whatDay", -1)) {
                -1 -> dayTextView.text = getString(R.string.error)
                0 -> {
                    dayTextView.text = getString(R.string.monday)
                    whatDay = DaysConstNames.MONDAY
                }
                1 -> {
                    dayTextView.text = getString(R.string.tuesday)
                    whatDay = DaysConstNames.TUESDAY
                }
                2 -> {
                    dayTextView.text = getString(R.string.wednesday)
                    whatDay = DaysConstNames.WEDNESDAY
                }
                3 -> {
                    dayTextView.text = getString(R.string.thursday)
                    whatDay = DaysConstNames.THURSDAY
                }
                4 -> {
                    dayTextView.text = getString(R.string.friday)
                    whatDay = DaysConstNames.FRIDAY
                }
                5 -> {
                    dayTextView.text = getString(R.string.saturday)
                    whatDay = DaysConstNames.SATURDAY
                }
            }
        }
    }

    private fun setOnTouchCloseKeyboard() {
        binding.apply {
            mainHolder.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(mainHolder.windowToken, 0)
                }

                true
            }
        }
    }
}