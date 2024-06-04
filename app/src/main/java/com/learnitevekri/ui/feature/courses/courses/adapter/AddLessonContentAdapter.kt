package com.learnitevekri.ui.feature.courses.courses.adapter

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.learnitevekri.R
import com.learnitevekri.data.courses.lessons.model.EditLessonContentData
import com.learnitevekri.data.courses.lessons.model.LessonContentData
import com.learnitevekri.databinding.AddContentListItemBinding
import com.learnitevekri.ui.feature.courses.courses.LessonContentClickListener
import com.learnitevekri.ui.feature.courses.courses.viewModel.AddContentViewModel

class AddLessonContentAdapter(
    private var contents: MutableList<LessonContentData>,
    private val itemClickListener: LessonContentClickListener,
    private val viewModel: AddContentViewModel
) : RecyclerView.Adapter<AddLessonContentAdapter.AddLessonContentViewHolder>() {

    private val TAG = AddLessonContentAdapter::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddLessonContentViewHolder {
        val binding =
            AddContentListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddLessonContentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddLessonContentViewHolder, position: Int) {
        holder.bind(contents[position])
    }

    override fun getItemCount(): Int {
        return contents.size
    }

    inner class AddLessonContentViewHolder(private val binding: AddContentListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(content: LessonContentData) {
            val buttonAndEditTextVisibility =
                viewModel.contentButtonVisibilityAndEditTextMap.getOrDefault(adapterPosition, true)

            if (buttonAndEditTextVisibility) {
                showEditMode()
            } else {
                showViewMode()
            }

            binding.btnAddContent.setOnClickListener {
                if (itemClickListener.onAddQuestionClick(adapterPosition)) {
                    disableEditing()
                    binding.btnAddContent.visibility = View.GONE
                    binding.ivEditContent.visibility = View.VISIBLE
                    viewModel.contentButtonVisibilityAndEditTextMap[adapterPosition] = false
                    saveContent()
                }
            }

            binding.etContentTitle.setText(content.contentTitle)
            binding.etContentUrl.setText(content.url)
            binding.etContentDescription.setText(content.contentDescription)
            setupContentTypeSpinner(content)
            setupCharCountListener()

            binding.etContentTitle.addTextChangedListener { text ->
                contents[adapterPosition].contentTitle = text.toString()
            }
            binding.etContentUrl.addTextChangedListener { text ->
                contents[adapterPosition].url = text.toString()
            }
            binding.etContentDescription.addTextChangedListener { text ->
                contents[adapterPosition].contentDescription = text.toString()
            }

            binding.ivEditContent.setOnClickListener {
                binding.ivSaveEditContent.visibility = View.VISIBLE
                itemClickListener.onEditClick(contents[adapterPosition])
                enableEditing()
            }

            binding.ivSaveEditContent.setOnClickListener {
                val editedContent = EditLessonContentData(
                    contents[adapterPosition].contentType,
                    contents[adapterPosition].url,
                    contents[adapterPosition].contentTitle,
                    contents[adapterPosition].contentDescription
                )
                val contentId = contents[adapterPosition].contentId
                itemClickListener.onSaveClick(contentId, editedContent)
                disableEditing()
                binding.ivSaveEditContent.visibility = View.GONE
            }
        }

        private fun setupContentTypeSpinner(content: LessonContentData) {
            val contentTypes =
                binding.root.context.resources.getStringArray(R.array.content_type_array)
            val contentTypeIndex = contentTypes.indexOf(content.contentType)
            if (contentTypeIndex >= 0) {
                binding.spinnerContentType.setSelection(contentTypeIndex)
            }

            binding.spinnerContentType.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        contents[adapterPosition].contentType = contentTypes[position]
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {}
                }
        }

        private fun setupCharCountListener() {
            binding.etContentDescription.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                @SuppressLint("SetTextI18n")
                override fun afterTextChanged(s: Editable?) {
                    s?.let {
                        val currentLength = it.length
                        binding.tvCharCount.text = "$currentLength/255"
                        if (currentLength > 255) {
                            val trimmedText = it.substring(0, 255)
                            binding.etContentDescription.setText(trimmedText)
                            binding.etContentDescription.setSelection(trimmedText.length)
                        }
                    }
                }
            })
        }

        private fun saveContent() {
            val editedContent = EditLessonContentData(
                contents[adapterPosition].contentType,
                contents[adapterPosition].url,
                contents[adapterPosition].contentTitle,
                contents[adapterPosition].contentDescription
            )
            val contentId = contents[adapterPosition].contentId
            itemClickListener.onSaveClick(contentId, editedContent)
        }

        fun showEditMode() {
            binding.btnAddContent.visibility = View.VISIBLE
            enableEditing()
            binding.ivEditContent.visibility = View.GONE
        }

        fun showViewMode() {
            binding.btnAddContent.visibility = View.GONE
            disableEditing()
            binding.ivEditContent.visibility = View.VISIBLE
        }

        fun enableEditing() {
            binding.etContentTitle.isEnabled = true
            binding.etContentUrl.isEnabled = true
            binding.etContentDescription.isEnabled = true
        }

        fun disableEditing() {
            binding.etContentTitle.isEnabled = false
            binding.etContentUrl.isEnabled = false
            binding.etContentDescription.isEnabled = false
        }
    }

    fun updateContentId(position: Int, contentId: Int) {
        contents[position].contentId = contentId
        notifyItemChanged(position)
    }
}