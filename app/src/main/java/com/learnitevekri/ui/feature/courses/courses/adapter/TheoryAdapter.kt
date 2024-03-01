package com.learnitevekri.ui.feature.courses.courses.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learnitevekri.R
import com.learnitevekri.data.courses.lessons.model.LessonContentData
import com.learnitevekri.databinding.ContentListItemBinding
import com.learnitevekri.ui.feature.courses.courses.TheoryAdapterListener

class TheoryAdapter(
    private val contents: List<LessonContentData>,
    private val listener: TheoryAdapterListener
) :
    RecyclerView.Adapter<TheoryAdapter.TheoryViewHolder>() {

    inner class TheoryViewHolder(val binding: ContentListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(content: LessonContentData) {
            when (content.contentType) {
                "video" -> binding.contentImageView.setImageResource(R.drawable.video)
                "folder" -> binding.contentImageView.setImageResource(R.drawable.folder)
                else -> binding.contentImageView.setImageResource(R.drawable.link)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TheoryViewHolder {
        val binding =
            ContentListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TheoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TheoryViewHolder, position: Int) {
        val content = contents[position]
        holder.bind(content)

        holder.binding.titleTextView.text = content.contentTitle
        holder.binding.descriptionTextView.text = content.contentDescription

        holder.binding.moreButton.setOnClickListener {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val content = contents[position]
                openUrlInBrowser(content.url, holder.binding.root.context)
            }
        }

        holder.binding.backToQuizButton.setOnClickListener() {
            listener.onBackToQuizClick()
        }
    }

    override fun getItemCount(): Int {
        return contents.size
    }

    private fun openUrlInBrowser(url: String, context: Context) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(browserIntent)
    }
}