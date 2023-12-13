package com.example.learnit.ui.feature.chapters.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.learnit.databinding.ChapterListItemBinding
import com.example.learnit.ui.feature.chapters.model.ChapterModel

class ChaptersAdapter(private val chapters: List<ChapterModel>) :
    RecyclerView.Adapter<ChaptersAdapter.ChaptersViewHolder>() {

    inner class ChaptersViewHolder(private val binding: ChapterListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            Log.d("ChaptersAdapter", "ViewHolder created")
        }

        fun bind(chapter: ChapterModel) {
            Log.d("ChapterAdapter", "Binding data: $chapter")
            binding.nameTextView.text = chapter.chapterName
            binding.descriptionTextView.text = chapter.chapterDescription
        }
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
        val itemCount = chapters.size
        Log.d("ChaptersAdapter", "Item count: $itemCount")
        return itemCount
    }

}