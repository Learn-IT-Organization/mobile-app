package com.example.learnit.ui.feature.courses.courses.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.learnit.data.courses.chapters.model.ChapterData
import com.example.learnit.data.courses.chapters.model.ChapterWithLessonsData
import com.example.learnit.data.courses.lessons.model.LessonData
import com.example.learnit.data.courses.lessons.model.LessonProgressData
import com.example.learnit.databinding.ChapterListItemBinding

class ChaptersAdapter(
    private val chapters: List<ChapterWithLessonsData>,
    private val lessonProgressList: List<LessonProgressData>,
    private val onChapterItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<ChaptersAdapter.ChaptersViewHolder>() {

    companion object {
        val TAG: String = ChaptersAdapter::class.java.simpleName
    }

    class ChaptersViewHolder(
        private val binding: ChapterListItemBinding,
        private var lessonProgressList: List<LessonProgressData>,
        private val listener: OnItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(chapter: ChapterWithLessonsData) {
            binding.nameTextView.text =
                chapter.chapter.chapterSequenceNumber.toString() + "." + chapter.chapter.chapterName

            Log.d(TAG, "Chapter: $lessonProgressList")

            binding.lessonsRecycleView.adapter =
                LessonsAdapter(chapter.lessons, lessonProgressList, listener)
        }
    }

    interface OnItemClickListener {
        fun onChapterItemClick(chapter: ChapterData)
        fun onPlayStateClick(lesson: LessonData, lessonProgressData: List<LessonProgressData>)
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
        holder.itemView.setOnClickListener {
            onChapterItemClickListener.onChapterItemClick(chapter.chapter)
        }
    }

    override fun getItemCount(): Int {
        return chapters.size
    }

}