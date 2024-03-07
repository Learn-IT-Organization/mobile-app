package com.learnitevekri.ui.feature.courses.courses.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learnitevekri.data.courses.lessons.model.LessonData
import com.learnitevekri.data.courses.lessons.model.LessonProgressData
import com.learnitevekri.databinding.LessonListItemBinding

class LessonsAdapter(
    private val lessonList: List<LessonData>,
    private var lessonProgressList: List<LessonProgressData>,
    private val onLessonItemClickListener: ChaptersAdapter.OnItemClickListener,
    private val isPreviousChapterCompleted: Boolean
) : RecyclerView.Adapter<LessonsAdapter.LessonsViewHolder>() {

    companion object {
        val TAG: String = LessonsAdapter::class.java.simpleName
    }

    inner class LessonsViewHolder(private val binding: LessonListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(lesson: LessonData, lessonProgressList: List<LessonProgressData>) {
            binding.lessonTitleTextView.text = lesson.lessonName

            if (!isPreviousChapterCompleted) {
                binding.buttonQuiz.isEnabled = false
                binding.buttonSeeResults.isEnabled = false
            } else {
                val progress = lessonProgressList.find { it.lessonId == lesson.lessonId }

                if (progress?.isCompleted == true) {
                    binding.buttonQuiz.visibility = View.GONE
                    binding.buttonSeeResults.visibility = View.VISIBLE
                } else {
                    binding.buttonQuiz.visibility = View.VISIBLE
                    binding.buttonSeeResults.visibility = View.GONE
                    binding.buttonQuiz.text = "Quiz"
                }

                if (lesson.lessonType == "theory")
                    binding.buttonQuiz.visibility = View.GONE

                binding.buttonQuiz.setOnClickListener {
                    onLessonItemClickListener.onQuizClick(lesson, lessonProgressList)
                }

                binding.buttonSeeResults.setOnClickListener {
                    onLessonItemClickListener.onQuizClick(lesson, lessonProgressList)
                }

                binding.lessonTitleTextView.setOnClickListener {
                    onLessonItemClickListener.onQuizClick(lesson, lessonProgressList)
                }

                binding.buttonReadMore.setOnClickListener {
                    onLessonItemClickListener.onTheoryClick(lesson)
                }
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