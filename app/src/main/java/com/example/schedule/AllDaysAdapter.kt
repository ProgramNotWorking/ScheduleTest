package com.example.schedule

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.databinding.DayLessonItemBinding

class AllDaysAdapter: RecyclerView.Adapter<AllDaysAdapter.DayHolder>() {
    private val lessonsList = ArrayList<Lesson>()

    class DayHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = DayLessonItemBinding.bind(item)

        fun bind(lesson: Lesson) = with(binding) {
            nameTextView.text = lesson.studentName
            timeTextView.text = lesson.lessonTime
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.day_lesson_item, parent, false
        )
        return DayHolder(view)
    }

    override fun onBindViewHolder(holder: DayHolder, position: Int) {
        holder.bind(lessonsList[position])
    }

    override fun getItemCount(): Int {
        return lessonsList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addLessonInfo(lesson: Lesson) {
        lessonsList.add(lesson)
        notifyDataSetChanged()
    }
}