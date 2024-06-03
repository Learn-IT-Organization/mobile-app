package com.learnitevekri.ui.feature.courses.quiz.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.learnitevekri.R

class SortingItemAdapter(
    private val items: MutableList<String>
) : RecyclerView.Adapter<SortingItemAdapter.SortingItemViewHolder>() {

    inner class SortingItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.tv_sorting_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SortingItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_sorting_item, parent, false)
        return SortingItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SortingItemViewHolder, position: Int) {
        holder.textView.text = items[position]
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(item: String) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }
}