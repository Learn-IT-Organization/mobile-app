package com.learnitevekri.ui.feature.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.learnitevekri.R
import com.learnitevekri.data.courses.course.model.CourseData
import com.learnitevekri.databinding.MyCourseListItemBinding

class MyCoursesAdapter(private val courses: List<CourseData>) :
    RecyclerView.Adapter<MyCoursesAdapter.MyCourseViewHolder>() {

    inner class MyCourseViewHolder(private val binding: MyCourseListItemBinding) :
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
                    R.id.action_homeFragment_to_chaptersFragment,
                    bundleOf(com.learnitevekri.data.ApiConstants.COURSE_ID to course.course_id)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCourseViewHolder {
        val binding =
            MyCourseListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyCourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyCourseViewHolder, position: Int) {
        val course = courses[position]
        holder.bind(course)
    }

    override fun getItemCount(): Int {
        return courses.size
    }

}