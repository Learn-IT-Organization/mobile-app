package com.learnitevekri.ui.feature.courses.courses.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.learnitevekri.R
import com.learnitevekri.data.courses.course.model.CourseData
import com.learnitevekri.databinding.CourseListItemBinding

class CoursesAdapter(
    private val courses: List<CourseData>,
    private val userId: String,
    private val onEditClicked: (CourseData) -> Unit
) :
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
                    R.id.action_CoursesFragment_to_ChaptersFragment,
                    bundleOf(com.learnitevekri.data.ApiConstants.COURSE_ID to course.course_id)
                )
            }

            if (course.course_user_id.toString() == userId) {
                binding.btnEdit.visibility = View.VISIBLE
                binding.btnEdit.setOnClickListener {
                    onEditClicked(course)
                }
            } else {
                binding.btnEdit.visibility = View.GONE
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