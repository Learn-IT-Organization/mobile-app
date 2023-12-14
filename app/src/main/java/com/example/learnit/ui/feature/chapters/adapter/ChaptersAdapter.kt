package com.example.learnit.ui.feature.chapters.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.learnit.databinding.ChapterListItemBinding
import com.example.learnit.ui.feature.chapters.model.ChapterModel

class ChaptersAdapter(
    private val chapters: List<ChapterModel>,
    private val clickListener: OnChapterItemClickListener
) :
    RecyclerView.Adapter<ChaptersAdapter.ChaptersViewHolder>() {

    inner class ChaptersViewHolder(private val binding: ChapterListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chapter: ChapterModel) {
            binding.nameTextView.text = chapter.chapterName
            binding.descriptionTextView.text = chapter.chapterDescription
            binding.root.setOnClickListener {
                clickListener.onChapterItemClick(chapter)
            }
        }
    }

    interface OnChapterItemClickListener {
        fun onChapterItemClick(chapter: ChapterModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChaptersViewHolder {
        val binding =
            ChapterListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChaptersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChaptersViewHolder, position: Int) {
        val chapter = chapters[position]
        holder.bind(chapter)
    }

    override fun getItemCount(): Int {
        return chapters.size
    }

}