package com.learnitevekri.ui.feature.courses.courses.adapter

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
import com.learnitevekri.data.courses.lessons.model.EditLessonContentData
import com.learnitevekri.data.courses.lessons.model.LessonContentData
import com.learnitevekri.databinding.AddContentListItemBinding
import com.learnitevekri.ui.feature.courses.courses.LessonContentClickListener

class AddLessonContentAdapter(
    private var contents: MutableList<LessonContentData>,
    private val itemClickListener: LessonContentClickListener
) :
    RecyclerView.Adapter<AddLessonContentAdapter.AddLessonContentViewHolder>() {
    private val TAG = AddLessonContentAdapter::class.java.simpleName
    override fun onBindViewHolder(holder: AddLessonContentViewHolder, position: Int) {
        val content = contents[position]
        holder.bind(content)
    }

    override fun getItemCount(): Int {
        return contents.size
    }

    private val contentTypes = arrayOf("folder", "link", "video")

    inner class AddLessonContentViewHolder(private val binding: AddContentListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(content: LessonContentData) {

            binding.etContentTitle.setText(content.contentTitle)
            binding.etContentDescription.setText(content.contentDescription)
            binding.etContentUrl.setText(content.url)

            binding.etContentTitle.addTextChangedListener { text ->
                contents[adapterPosition].contentTitle = text.toString()
            }
            binding.etContentDescription.addTextChangedListener { text ->
                contents[adapterPosition].contentDescription = text.toString()
            }
            binding.etContentUrl.addTextChangedListener { text ->
                contents[adapterPosition].url = text.toString()
            }

            val adapter =
                ArrayAdapter(
                    itemView.context,
                    android.R.layout.simple_spinner_dropdown_item,
                    contentTypes
                )
            binding.spinnerContentType.adapter = adapter
            val selectedTypeIndex = contentTypes.indexOf(content.contentType)
            binding.spinnerContentType.setSelection(selectedTypeIndex)
            binding.spinnerContentType.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        contents[adapterPosition].contentType = contentTypes[position]
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }

            val position = adapterPosition + 1
            binding.tvContentNumber.text = "Content $position:"

            binding.etContentDescription.addTextChangedListener(object : TextWatcher {
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
                            binding.etContentDescription.setText(trimmedText)
                            binding.etContentDescription.setSelection(trimmedText.length)
                        }
                    }
                }
            })

            binding.ivEditContent.setOnClickListener {
                Log.d(
                    TAG,
                    "Edit Clicked on $adapterPosition: ${contents[adapterPosition]}"
                )
                binding.ivSaveEditContent.visibility = View.VISIBLE
                itemClickListener.onAddContentClick(contents[adapterPosition])
                enableEditing()
            }

            binding.ivSaveEditContent.setOnClickListener {
                val editedContent = EditLessonContentData(
                    contents[adapterPosition].contentType,
                    contents[adapterPosition].url,
                    contents[adapterPosition].contentTitle,
                    contents[adapterPosition].contentDescription,
                )
                val contentID = contents[adapterPosition].contentId
                Log.d(
                    TAG,
                    "Save Clicked on $adapterPosition: $editedContent"
                )
                itemClickListener.onSaveClick(contentID, editedContent)
                binding.ivSaveEditContent.visibility = View.GONE
            }

        }

        fun disableEditing() {
            binding.etContentUrl.isEnabled = false
            binding.etContentDescription.isEnabled = false
            binding.etContentTitle.isEnabled = false
            binding.spinnerContentType.isEnabled = false
        }

        fun enableEditing() {
            binding.etContentUrl.isEnabled = true
            binding.etContentDescription.isEnabled = true
            binding.etContentTitle.isEnabled = true
            binding.spinnerContentType.isEnabled = true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddLessonContentViewHolder {
        val binding =
            AddContentListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddLessonContentViewHolder(binding)
    }

    fun updateContentId(position: Int, contentId: Int) {
        Log.d(TAG, "Updating content id at position $position to $contentId")
        contents[position].contentId = contentId
        notifyItemChanged(position)
    }
}