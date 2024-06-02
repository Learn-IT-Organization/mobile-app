package com.learnitevekri.ui.feature.courses.quiz.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.learnitevekri.databinding.ItemMatchingPairBinding

class MatchingPairsAdapter(
    private val pairsList: MutableList<Pair<String, String>>
) : RecyclerView.Adapter<MatchingPairsAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemMatchingPairBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pair: Pair<String, String>) {
            binding.etItemLeft.setText(pair.first)
            binding.etItemRight.setText(pair.second)

            binding.etItemLeft.addTextChangedListener {
                pairsList[adapterPosition] = Pair(it.toString(), binding.etItemRight.text.toString())
            }

            binding.etItemRight.addTextChangedListener {
                pairsList[adapterPosition] = Pair(binding.etItemLeft.text.toString(), it.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMatchingPairBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pairsList[position])
    }

    override fun getItemCount(): Int = pairsList.size
}