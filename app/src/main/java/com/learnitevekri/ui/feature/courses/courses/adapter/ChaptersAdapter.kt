package com.learnitevekri.ui.feature.courses.courses.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.learnitevekri.R
import com.learnitevekri.data.courses.chapters.model.ChapterWithLessonsData
import com.learnitevekri.data.courses.lessons.model.LessonData
import com.learnitevekri.data.courses.lessons.model.LessonProgressData
import com.learnitevekri.databinding.ChapterListItemBinding

class ChaptersAdapter(
    private val chapters: List<ChapterWithLessonsData>,
    private val lessonProgressList: List<LessonProgressData>,
    private val onChapterItemClickListener: OnItemClickListener,
) :
    RecyclerView.Adapter<ChaptersAdapter.ChaptersViewHolder>() {

    companion object {
        val TAG: String = ChaptersAdapter::class.java.simpleName
    }

    class ChaptersViewHolder(
        val binding: ChapterListItemBinding,
        private var lessonProgressList: List<LessonProgressData>,
        private val listener: OnItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(chapter: ChapterWithLessonsData) {
            binding.nameTextView.text =
                chapter.chapter.chapterSequenceNumber.toString() + ". " + chapter.chapter.chapterName

            binding.progressBar.setProgressPercentage(
                (if (chapter.chapterResultData.totalLessons != 0 && chapter.chapterResultData.lessonsCompleted != 0) {
                    (chapter.chapterResultData.lessonsCompleted.toDouble() / chapter.chapterResultData.totalLessons.toDouble()) * 100
                } else {
                    0.0
                })
            )

            binding.moreButton.setOnClickListener {
                val customDialog = Dialog(binding.root.context)
                customDialog.setContentView(R.layout.dialog_description)

                val descriptionTextView: TextView =
                    customDialog.findViewById(R.id.descriptionTextView)
                descriptionTextView.text = chapter.chapter.chapterDescription

                val closeButton: ImageButton = customDialog.findViewById(R.id.closeButton)
                closeButton.setOnClickListener {
                    customDialog.dismiss()
                }

                customDialog.show()
            }
            
        }
    }

    interface OnItemClickListener {
        fun onQuizClick(lesson: LessonData, lessonProgressData: List<LessonProgressData>)
        fun onTheoryClick(lesson: LessonData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChaptersViewHolder {
        val binding =
            ChapterListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChaptersViewHolder(binding, lessonProgressList, onChapterItemClickListener)
    }

    override fun onBindViewHolder(holder: ChaptersViewHolder, position: Int) {
        val chapter = chapters[position]
        holder.bind(chapter)
        val isPreviousChapterCompleted =
            if (position == 0) true else isChapterCompleted(position - 1)
        holder.binding.courseCardView.alpha = if (isPreviousChapterCompleted) 1.0f else 0.5f
        holder.binding.moreButton.isEnabled = isPreviousChapterCompleted
        val lessonsAdapter = LessonsAdapter(chapter.lessons, lessonProgressList, onChapterItemClickListener, isPreviousChapterCompleted)
        holder.binding.lessonsRecycleView.adapter = lessonsAdapter
    }

    override fun getItemCount(): Int {
        return chapters.size
    }

    private fun isChapterCompleted(chapterPosition: Int): Boolean {
        if (chapterPosition >= 0 && chapterPosition < chapters.size) {
            val chapter = chapters[chapterPosition]
            return chapter.chapterResultData.lessonsCompleted == chapter.chapterResultData.totalLessons
        }
        return false
    }
}