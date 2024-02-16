package com.example.learnit.ui.feature.courses.courses.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.learnit.R
import com.example.learnit.data.courses.lessons.model.LessonData
import com.example.learnit.data.courses.lessons.model.LessonProgressData
import com.example.learnit.databinding.LessonListItemBinding

class LessonsAdapter(
    private val lessonList: List<LessonData>,
    private var lessonProgressList: List<LessonProgressData>,
    private val onLessonItemClickListener: ChaptersAdapter.OnItemClickListener
) : RecyclerView.Adapter<LessonsAdapter.LessonsViewHolder>() {

    companion object {
        val TAG: String = LessonsAdapter::class.java.simpleName
    }

    inner class LessonsViewHolder(private val binding: LessonListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(lesson: LessonData, lessonProgressList: List<LessonProgressData>) {
            binding.lessonTitleTextView.text = lesson.lessonName

            val progress = lessonProgressList.find { it.lessonId == lesson.lessonId }

            Log.d(TAG, "Lesson progress: $progress")

            val completionImageResource =
                if (progress?.isCompleted == true) R.drawable.ic_completed else R.drawable.ic_play

            binding.stateImageView.setImageResource(completionImageResource)

            binding.stateImageView.setOnClickListener {
                onLessonItemClickListener.onPlayStateClick(lesson, lessonProgressList)
            }

            binding.lessonTitleTextView.setOnClickListener {
                onLessonItemClickListener.onPlayStateClick(lesson, lessonProgressList)
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
        val lesson = lessonList[position]
        holder.bind(lesson, lessonProgressList)
    }

    override fun getItemCount(): Int {
        return lessonList.size
    }
}