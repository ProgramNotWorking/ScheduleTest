package com.example.schedule

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule.databinding.LessonItemBinding

class LessonAdapter(private val listener: OnItemClickListener): RecyclerView.Adapter<LessonAdapter.LessonHolder>() {
    private val lessonList = ArrayList<Lesson>()

    inner class LessonHolder(item: View): RecyclerView.ViewHolder(item), View.OnClickListener {
        private val binding = LessonItemBinding.bind(item)

        val nameText = itemView.findViewById<EditText>(R.id.editNameField)
        val timeText = itemView.findViewById<EditText>(R.id.editTimeField)

        init {
            binding.deleteStudentButton.setOnClickListener(this)
        }

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

        override fun onClick(view: View) {
            if (view.id == R.id.deleteStudentButton) {
                listener.onItemClick(adapterPosition)
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

        holder.nameText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable?) {
                item.studentName = s.toString()
            }
        })
        holder.timeText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable?) {
                item.lessonTime = s.toString()
            }
        })
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
        notifyItemRangeChanged(position, lessonList.size)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}