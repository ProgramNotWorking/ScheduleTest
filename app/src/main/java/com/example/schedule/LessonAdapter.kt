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

        init {
            binding.deleteStudentButton.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
            binding.editStudentInfoButton.setOnClickListener {
                editListener.onEditItemClick(adapterPosition) // TODO: work with adapterPosition
            }
        }

        fun bind(lesson: Lesson) = with(binding) {
            if (lesson.studentName != null) {
                nameTextViewItem.text = lesson.studentName
            }
            if (lesson.lessonTime != null) {
                timeTextViewItem.text = lesson.lessonTime
            }
        }

        override fun onClick(view: View) {
            if (view.id == R.id.deleteStudentButton) {
                listener.onItemClick(adapterPosition)
            }
            if (view.id == R.id.editStudentInfoButton) {
                editListener.onEditItemClick(adapterPosition)
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
        val item = lessonList[position]
        holder.bind(item)
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

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    interface OnEditClickListener {
        fun onEditItemClick(position: Int)
    }
}