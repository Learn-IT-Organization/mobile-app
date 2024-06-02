package com.learnitevekri.ui.feature.courses.courses.adapter

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.learnitevekri.data.courses.chapters.model.AddNewChapterData
import com.learnitevekri.data.courses.chapters.model.EditChapterData
import com.learnitevekri.databinding.AddNewChapterItemBinding
import com.learnitevekri.ui.feature.courses.courses.ChapterItemClickListener
import com.learnitevekri.ui.feature.courses.courses.viewModel.SharedLessonViewModel

class AddNewChapterAdapter(
    private var chapters: MutableList<AddNewChapterData>,
    private val itemClickListener: ChapterItemClickListener,
    private val sharedViewModel: SharedLessonViewModel
) :
    RecyclerView.Adapter<AddNewChapterAdapter.ChapterViewHolder>() {

    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {
        val chapter = chapters[position]
        holder.bind(chapter)
    }

    override fun getItemCount(): Int {
        return chapters.size
    }

    inner class ChapterViewHolder(val binding: AddNewChapterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(chapter: AddNewChapterData) {

            val buttonAndEditTextVisibility =
                sharedViewModel.lessonButtonVisibilityAndEditTextMap.getOrDefault(
                    adapterPosition,
                    true
                )

            if (buttonAndEditTextVisibility) {
                binding.btnAddLesson.visibility = View.VISIBLE
                binding.etChapterName.isEnabled = true
                binding.etChapterDescription.isEnabled = true
                binding.ivEditChapter.visibility = View.GONE
            } else {
                binding.btnAddLesson.visibility = View.GONE
                binding.etChapterName.isEnabled = false
                binding.etChapterDescription.isEnabled = false
                binding.ivEditChapter.visibility = View.VISIBLE
            }

            binding.btnAddLesson.setOnClickListener {
                if (itemClickListener.onAddLessonClick(adapterPosition)) {
                    disableEditing()
                    binding.btnAddLesson.visibility = View.GONE
                    binding.ivEditChapter.visibility = View.VISIBLE
                }
                sharedViewModel.lessonButtonVisibilityAndEditTextMap[adapterPosition] = false
            }

            binding.etChapterName.setText(chapter.chapterName)
            binding.etChapterDescription.setText(chapter.chapterDescription)

            binding.etChapterName.addTextChangedListener { text ->
                chapters[adapterPosition].chapterName = text.toString()
            }
            binding.etChapterDescription.addTextChangedListener { text ->
                chapters[adapterPosition].chapterDescription = text.toString()
            }

            val position = adapterPosition + 1
            binding.tvChapterNumber.text = "Chapter $position:"

            binding.etChapterDescription.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    s?.let {
                        val currentLength = it.length
                        binding.tvCharCount.text = "$currentLength/255"
                        if (currentLength > 255) {
                            val trimmedText = it.substring(0, 255)
                            binding.etChapterDescription.setText(trimmedText)
                            binding.etChapterDescription.setSelection(trimmedText.length)
                        }
                    }
                }
            })

            binding.ivEditChapter.setOnClickListener {
                Log.d(
                    "AddNewChapterAdapter",
                    "Edit Clicked on $adapterPosition: ${chapters[adapterPosition]}"
                )
                binding.ivSaveEditChapter.visibility = View.VISIBLE
                itemClickListener.onEditClick(chapters[adapterPosition])
                enableEditing()
            }

            binding.ivSaveEditChapter.setOnClickListener {
                val editedChapter = EditChapterData(
                    chapters[adapterPosition].chapterName,
                    chapters[adapterPosition].chapterDescription
                )
                val chapterId = chapters[adapterPosition].chapterId!!
                itemClickListener.onSaveClick(chapterId, editedChapter)
                disableEditing()
                binding.ivSaveEditChapter.visibility = View.GONE
            }

//            binding.btnAddLesson.setOnClickListener {
//                itemClickListener.onAddLessonClick()
//                disableEditing()
//                binding.btnAddLesson.visibility = View.GONE
//            }
        }

        fun disableEditing() {
            binding.etChapterName.isEnabled = false
            binding.etChapterDescription.isEnabled = false
        }

        fun enableEditing() {
            binding.etChapterName.isEnabled = true
            binding.etChapterDescription.isEnabled = true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {
        val binding =
            AddNewChapterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChapterViewHolder(binding)
    }

    fun updateChapterId(position: Int, chapterId: Int) {
        chapters[position].chapterId = chapterId
        notifyItemChanged(position)
    }

}
