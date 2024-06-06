package com.learnitevekri.ui.feature.courses.courses.adapter

import android.R
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.learnitevekri.data.courses.lessons.model.AddNewLessonData
import com.learnitevekri.data.courses.lessons.model.EditLessonData
import com.learnitevekri.databinding.AddNewLessonItemBinding
import com.learnitevekri.ui.feature.courses.courses.LessonItemClickListener
import com.learnitevekri.ui.feature.courses.courses.LessonTypeChangeListener

class AddNewLessonAdapter(
    private var lessons: MutableList<AddNewLessonData>,
    private val itemClickListener: LessonItemClickListener,
) : RecyclerView.Adapter<AddNewLessonAdapter.LessonViewHolder>() {

    private val TAG: String = AddNewLessonAdapter::class.java.simpleName
    private val lessonTypes = arrayOf("theory", "exercise")

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        val lesson = lessons[position]
        holder.bind(lesson)
    }

    override fun getItemCount(): Int {
        return lessons.size
    }

    inner class LessonViewHolder(private val binding: AddNewLessonItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(lesson: AddNewLessonData) {
            binding.etLessonName.setText(lesson.lessonName)
            binding.etLessonDescription.setText(lesson.lessonDescription)
            binding.etLessonTags.setText(lesson.lessonTags)

            binding.etLessonName.addTextChangedListener { text ->
                lessons[adapterPosition].lessonName = text.toString()
            }
            binding.etLessonDescription.addTextChangedListener { text ->
                lessons[adapterPosition].lessonDescription = text.toString()
            }
            binding.etLessonTags.addTextChangedListener { text ->
                lessons[adapterPosition].lessonTags = text.toString()
            }

            val adapter =
                ArrayAdapter(itemView.context, R.layout.simple_spinner_dropdown_item, lessonTypes)
            binding.spinnerLessonType.adapter = adapter
            val selectedTypeIndex = lessonTypes.indexOf(lesson.lessonType)
            binding.spinnerLessonType.setSelection(selectedTypeIndex)
            binding.spinnerLessonType.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long,
                    ) {
                        lessons[adapterPosition].lessonType = lessonTypes[position]
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }

            val position = adapterPosition + 1
            binding.tvLessonNumber.text = "Lesson $position:"

            binding.etLessonDescription.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    s?.let {
                        val currentLength = it.length
                        binding.tvCharCount.text = "$currentLength/255"
                        if (currentLength > 255) {
                            val trimmedText = it.substring(0, 255)
                            binding.etLessonDescription.setText(trimmedText)
                            binding.etLessonDescription.setSelection(trimmedText.length)
                        }
                    }
                }
            })

            binding.ivEditLesson.setOnClickListener {
                Log.d(
                    TAG,
                    "Edit Clicked on $adapterPosition: ${lessons[adapterPosition]}"
                )
                binding.ivSaveEditLesson.visibility = View.VISIBLE
                itemClickListener.onEditClick(lessons[adapterPosition])
                enableEditing()
            }

            binding.ivSaveEditLesson.setOnClickListener {
                val editedLesson = EditLessonData(
                    lessons[adapterPosition].lessonName,
                    lessons[adapterPosition].lessonDescription,
                    lessons[adapterPosition].lessonType,
                    lessons[adapterPosition].lessonTags
                )
                val lessonId = lessons[adapterPosition].lessonId!!
                itemClickListener.onSaveClick(lessonId, editedLesson)
                disableEditing()
                binding.ivSaveEditLesson.visibility = View.GONE
            }

        }

        fun disableEditing() {
            binding.etLessonName.isEnabled = false
            binding.etLessonDescription.isEnabled = false
            binding.etLessonTags.isEnabled = false
            binding.spinnerLessonType.isEnabled = false
        }

        fun enableEditing() {
            binding.etLessonName.isEnabled = true
            binding.etLessonDescription.isEnabled = true
            binding.etLessonTags.isEnabled = true
            binding.spinnerLessonType.isEnabled = true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val binding =
            AddNewLessonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LessonViewHolder(binding)
    }

    fun updateLessonId(position: Int, lessonId: Int) {
        Log.d(TAG, "Updating lesson ID: $lessonId at position: $position")
        lessons[position].lessonId = lessonId
        notifyItemChanged(position)
        Log.d(TAG, lessons.toString())
    }
}