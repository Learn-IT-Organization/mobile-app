package com.example.learnit.ui.feature.courses.courses.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.learnit.R
import com.example.learnit.data.ApiConstants
import com.example.learnit.databinding.CourseListItemBinding
import com.example.learnit.ui.feature.courses.courses.model.CourseModel

class CoursesAdapter(private val courses: List<CourseModel>) :
    RecyclerView.Adapter<CoursesAdapter.CourseViewHolder>() {

    inner class CourseViewHolder(private val binding: CourseListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(course: CourseModel) {
            binding.nameTextView.text = course.courseName
            binding.languageTextView.text = course.programmingLanguage
            binding.root.setOnClickListener{
                Log.d("CoursesAdapter", "Click on: ${course.courseId}")
                itemView.findNavController().navigate(
                    R.id.action_CoursesFragment_to_ChaptersFragemnt,
                    bundleOf(ApiConstants.COURSE_ID to course.courseId)
                )
                true
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