package com.example.learnit.ui.feature.courses.courses.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.learnit.R
import com.example.learnit.data.ApiConstants
import com.example.learnit.data.courses.course.model.CourseData
import com.example.learnit.databinding.CourseListItemBinding

class CoursesAdapter(private val courses: List<CourseData>) :
    RecyclerView.Adapter<CoursesAdapter.CourseViewHolder>() {

    inner class CourseViewHolder(private val binding: CourseListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(course: CourseData) {
            binding.nameTextView.text = course.course_name
            if (adapterPosition % 2 == 0) {
                binding.courseImageView.setImageResource(R.drawable.girl_with_code_snippet)
            } else {
                binding.courseImageView.setImageResource(R.drawable.boy_developer)
            }
            binding.root.setOnClickListener {
                itemView.findNavController().navigate(
                    R.id.action_CoursesFragment_to_ChaptersFragemnt,
                    bundleOf(ApiConstants.COURSE_ID to course.course_id)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding =
            CourseListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courses[position]
        holder.bind(course)
    }

    override fun getItemCount(): Int {
        return courses.size
    }
}