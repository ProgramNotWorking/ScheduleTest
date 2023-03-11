package com.example.schedule

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.databinding.LessonItemBinding

class LessonAdapter: RecyclerView.Adapter<LessonAdapter.LessonHolder>() {
    private val lessonList = ArrayList<Lesson>()

    class LessonHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = LessonItemBinding.bind(item)

        fun bind(lesson: Lesson) = with(binding) {
            if (lesson.studentName != null) {
                editNameField.setText(lesson.studentName)
            } else {
                editNameField.text = null
            }
            if (lesson.lessonTime != null) {
                editTimeField.setText(lesson.lessonTime)
            } else {
                editTimeField.text = null
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.lesson_item, parent, false
        )
        return LessonHolder(view)
    }

    override fun onBindViewHolder(holder: LessonHolder, position: Int) {
        holder.bind(lessonList[position])
    }

    override fun getItemCount(): Int {
        return lessonList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addLesson(lesson: Lesson) {
        lessonList.add(lesson)
        notifyDataSetChanged()
    }

    fun removeLesson(position: Int) {
        lessonList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, lessonList.size)


    }
}