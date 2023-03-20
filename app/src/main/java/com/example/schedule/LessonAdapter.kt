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

//        init {
//            binding.deleteStudentButton.setOnClickListener {
//                listener.onItemClick(adapterPosition)
//            }
//            binding.editStudentInfoButton.setOnClickListener {
//                editListener.onEditItemClick(adapterPosition) // TODO: work with adapterPosition
//            }
//        }

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

//        override fun onClick(view: View) {
//            if (view.id == R.id.deleteStudentButton) {
//                listener.onItemClick(adapterPosition)
//            }
//            if (view.id == R.id.editStudentInfoButton) {
//                editListener.onEditItemClick(adapterPosition)
//            }
//        }

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
        // notifyItemRangeChanged(position, lessonList.size)
    }

    fun removeLessonByData(name: String?, time: String?) {
        var index = -1
        for (item in 0 until lessonList.size) {
            if (lessonList[item].studentName.equals(name) && lessonList[item].lessonTime.equals(time)) {
                index = item
                break
            }
        }

        if (index != -1) {
            lessonList.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(lesson: Lesson)
    }

    interface OnEditClickListener {
        fun onEditItemClick(lesson: Lesson)
    }
}