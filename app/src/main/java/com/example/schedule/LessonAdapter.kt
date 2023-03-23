package com.example.schedule

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.databinding.LessonItemBinding

class LessonAdapter(
    private val listener: OnItemClickListener,
    private val editListener: OnEditClickListener
    ): RecyclerView.Adapter<LessonAdapter.LessonHolder>() {
    private val lessonList = ArrayList<Lesson>()

    inner class LessonHolder(item: View): RecyclerView.ViewHolder(item), View.OnClickListener {
        private val binding = LessonItemBinding.bind(item)

        private var nameText = binding.nameTextViewItem.text.toString()
        private var timeText = binding.timeTextViewItem.text.toString()

        fun bind(
            lesson: Lesson, listenerDelete: OnItemClickListener, listenerEdit: OnEditClickListener
        ) = with(binding) {
            nameTextViewItem.text = lesson.studentName
            timeTextViewItem.text = lesson.lessonTime

            deleteStudentButton.setOnClickListener {
                listenerDelete.onItemClick(lesson)
            }

            editStudentInfoButton.setOnClickListener {
                listenerEdit.onEditItemClick(lesson)
            }
        }

        override fun onClick(p0: View?) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.lesson_item, parent, false
        )
        return LessonHolder(view)
    }

    override fun onBindViewHolder(holder: LessonHolder, position: Int) {
        val item = lessonList[position]
        holder.bind(item, listener, editListener)
    }

    override fun getItemCount(): Int = lessonList.size

    @SuppressLint("NotifyDataSetChanged")
    fun addLesson(lesson: Lesson) {
        lessonList.add(lesson)
        notifyDataSetChanged()
    }

    fun removeLesson(position: Int) {
        lessonList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun removeLessonByString(name: String?, time: String?) {
        for (item in lessonList.indices) {
            if (lessonList[item].studentName == name && lessonList[item].lessonTime == time) {
                lessonList.removeAt(item)
                notifyItemRemoved(item)

                break
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(lesson: Lesson)
    }

    interface OnEditClickListener {
        fun onEditItemClick(lesson: Lesson)
    }
}