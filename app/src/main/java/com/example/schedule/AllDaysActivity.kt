package com.example.schedule

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.schedule.databinding.ActivityAllDaysBinding

class AllDaysActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAllDaysBinding

    private lateinit var namesArray: ArrayList<String>
    private lateinit var timeArray: ArrayList<String>
    private lateinit var daysArray: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllDaysBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        namesArray = intent.getStringArrayListExtra("names") as ArrayList<String>
        timeArray = intent.getStringArrayListExtra("time") as ArrayList<String>
        daysArray = intent.getStringArrayListExtra("days") as ArrayList<String>

        clearText()

        binding.apply {
            for (item in 0 until daysArray.size) {
                when (daysArray[item]) {
                    DaysConstNames.MONDAY -> {
                        mondayScrollTextItem.append(namesArray[item] + "\n")
                        mondayScrollTextItem.append(timeArray[item] + "\n")
                        mondayScrollTextItem.append("\n")
                    }
                    DaysConstNames.TUESDAY -> {
                        tuesdayScrollTextItem.append(namesArray[item] + "\n")
                        tuesdayScrollTextItem.append(timeArray[item] + "\n")
                        tuesdayScrollTextItem.append("\n")
                    }
                    DaysConstNames.WEDNESDAY -> {
                        wednesdayScrollTextItem.append(namesArray[item] + "\n")
                        wednesdayScrollTextItem.append(timeArray[item] + "\n")
                        wednesdayScrollTextItem.append("\n")
                    }
                    DaysConstNames.THURSDAY -> {
                        thursdayScrollTextItem.append(namesArray[item] + "\n")
                        thursdayScrollTextItem.append(timeArray[item] + "\n")
                        thursdayScrollTextItem.append("\n")
                    }
                    DaysConstNames.FRIDAY -> {
                        fridayScrollTextItem.append(namesArray[item] + "\n")
                        fridayScrollTextItem.append(timeArray[item] + "\n")
                        fridayScrollTextItem.append("\n")
                    }
                    DaysConstNames.SATURDAY -> {
                        saturdayScrollTextItem.append(namesArray[item] + "\n")
                        saturdayScrollTextItem.append(timeArray[item] + "\n")
                        saturdayScrollTextItem.append("\n")
                    }
                }
            }

            returnButton.setOnClickListener {
                setResult(
                    RESULT_OK, Intent(this@AllDaysActivity, MainActivity::class.java)
                )
                finish()
            }
        }
    }

    private fun clearText() {
        for (item in daysArray) {
            when(item) {
                DaysConstNames.MONDAY -> binding.mondayScrollTextItem.text = null
                DaysConstNames.TUESDAY -> binding.tuesdayScrollTextItem.text = null
                DaysConstNames.WEDNESDAY -> binding.wednesdayScrollTextItem.text = null
                DaysConstNames.THURSDAY -> binding.thursdayScrollTextItem.text = null
                DaysConstNames.FRIDAY -> binding.fridayScrollTextItem.text = null
                DaysConstNames.SATURDAY -> binding.saturdayScrollTextItem.text = null
            }
        }
    }
}