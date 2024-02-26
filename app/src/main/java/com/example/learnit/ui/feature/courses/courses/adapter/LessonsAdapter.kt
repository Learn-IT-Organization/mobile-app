package com.example.learnit.ui.feature.courses.courses.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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

        @SuppressLint("SetTextI18n")
        fun bind(lesson: LessonData, lessonProgressList: List<LessonProgressData>) {
            binding.lessonTitleTextView.text = lesson.lessonName

            val progress = lessonProgressList.find { it.lessonId == lesson.lessonId }

            if (progress?.isCompleted == true) {
                binding.buttonQuiz.text = "See results"
            } else {
                binding.buttonQuiz.text = "Quiz"
            }

            if (lesson.lessonType == "theory")
                binding.buttonQuiz.visibility = View.GONE

            binding.buttonQuiz.setOnClickListener {
                onLessonItemClickListener.onQuizClick(lesson, lessonProgressList)
            }

            binding.lessonTitleTextView.setOnClickListener {
                onLessonItemClickListener.onQuizClick(lesson, lessonProgressList)
            }

            binding.buttonTheory.setOnClickListener {
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