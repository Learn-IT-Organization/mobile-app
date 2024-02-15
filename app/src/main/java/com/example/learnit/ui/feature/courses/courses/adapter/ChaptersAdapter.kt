package com.example.learnit.ui.feature.courses.courses.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.learnit.data.courses.chapters.model.ChapterData
import com.example.learnit.data.courses.chapters.model.ChapterWithLessonsData
import com.example.learnit.data.courses.lessons.model.LessonData
import com.example.learnit.databinding.ChapterListItemBinding

class ChaptersAdapter(
    private val chapters: List<ChapterWithLessonsData>,
    private val onChapterItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<ChaptersAdapter.ChaptersViewHolder>() {
    companion object {
        val TAG: String = ChaptersAdapter::class.java.simpleName
    }

    class ChaptersViewHolder(
        private val binding: ChapterListItemBinding,
        listener: OnItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        private val lessonsAdapter = LessonsAdapter(listener)

        init {
            binding.lessonsRecycleView.adapter = lessonsAdapter
        }

        @SuppressLint("SetTextI18n")
        fun bind(chapter: ChapterWithLessonsData) {
            binding.nameTextView.text =
                chapter.chapter.chapterSequenceNumber.toString() + "." + chapter.chapter.chapterName
            lessonsAdapter.submitList(chapter.lessons)
        }
    }

    interface OnItemClickListener {
        fun onChapterItemClick(chapter: ChapterData)
        fun onPlayStateClick(lesson: LessonData)
        fun onTheoryClick(lesson: LessonData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChaptersViewHolder {
        val binding =
            ChapterListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChaptersViewHolder(binding, onChapterItemClickListener)
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