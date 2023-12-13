package com.example.learnit.ui.feature.courses.courses.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.learnit.databinding.CourseListItemBinding
import com.example.learnit.ui.feature.courses.courses.model.CourseModel

class CoursesAdapter(private val courses: List<CourseModel>) :
    RecyclerView.Adapter<CoursesAdapter.CourseViewHolder>() {

    inner class CourseViewHolder(private val binding: CourseListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            Log.d("CoursesAdapter", "ViewHolder created")
        }

        fun bind(course: CourseModel) {
            Log.d("CoursesAdapter", "Binding data: $course")
            binding.nameTextView.text = course.courseName
            binding.languageTextView.text = course.programmingLanguage
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
        val itemCount = courses.size
        Log.d("CoursesAdapter", "Item count: $itemCount")
        return itemCount
    }

}