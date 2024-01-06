package com.example.learnit.ui.feature.courses.lessons.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.learnit.databinding.LessonListItemBinding
import com.example.learnit.ui.feature.courses.lessons.model.LessonModel

class LessonsAdapter(
    private val lessons: List<LessonModel>,
    private val onLessonItemClickListener: OnLessonItemClickListener
) : RecyclerView.Adapter<LessonsAdapter.LessonViewHolder>() {

    interface OnLessonItemClickListener {
        fun onLessonItemClick(lesson: LessonModel)
    }

    inner class LessonViewHolder(private val binding: LessonListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(lesson: LessonModel) {
            binding.nameTextView.text = lesson.lessonName
            binding.descriptionTextView.text = lesson.lessonDescription
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val binding =
            LessonListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LessonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        val lesson = lessons[position]
        holder.bind(lesson)
        holder.itemView.setOnClickListener {
            onLessonItemClickListener.onLessonItemClick(lesson)
        }
    }

    override fun getItemCount(): Int {
        return lessons.size
    }

}