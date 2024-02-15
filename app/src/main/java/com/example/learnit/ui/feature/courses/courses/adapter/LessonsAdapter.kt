package com.example.learnit.ui.feature.courses.courses.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.learnit.R
import com.example.learnit.data.courses.lessons.model.LessonData
import com.example.learnit.databinding.LessonListItemBinding

class LessonsAdapter(
    private val onLessonItemClickListener: ChaptersAdapter.OnItemClickListener
) : RecyclerView.Adapter<LessonsAdapter.LessonsViewHolder>() {

    private var lessons: List<LessonData> = listOf()

    fun submitList(newList: List<LessonData>) {
        lessons = newList
    }

    inner class LessonsViewHolder(private val binding: LessonListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(lesson: LessonData) {
            binding.lessonTitleTextView.text = lesson.lessonName

            val completionImageResource =
                if (lesson.isCompleted) R.drawable.ic_completed else R.drawable.ic_play
            
            binding.stateImageView.setImageResource(completionImageResource)

            binding.stateImageView.setOnClickListener {
                onLessonItemClickListener.onPlayStateClick(lesson)
            }

            binding.lessonTitleTextView.setOnClickListener {
                onLessonItemClickListener.onPlayStateClick(lesson)
            }

            binding.theoryImageView.setOnClickListener {
                onLessonItemClickListener.onTheoryClick(lesson)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonsViewHolder {
        val binding = LessonListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LessonsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LessonsViewHolder, position: Int) {
        val lesson = lessons[position]
        holder.bind(lesson)
    }

    override fun getItemCount(): Int {
        return lessons.size
    }
}