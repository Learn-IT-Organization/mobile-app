package com.learnitevekri.ui.feature.courses.courses.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.learnitevekri.R
import com.learnitevekri.data.courses.lessons.model.LessonContentData
import com.learnitevekri.databinding.FragmentAddContentBinding
import com.learnitevekri.ui.feature.courses.courses.viewModel.AddContentViewModel
import kotlinx.coroutines.launch

class AddContentFragment : Fragment() {

    companion object {
        val TAG: String = AddContentFragment::class.java.simpleName
    }

    private lateinit var binding: FragmentAddContentBinding
    private val viewModel: AddContentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val getContent =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    data?.data?.let { uri ->
                        binding.etContentUrl.setText(uri.toString())
                    }
                }
            }

        binding.btnAddContent.setOnClickListener {
            val lessonContent = LessonContentData(
                contentId = 0,
                contentType = binding.spinnerContentType.selectedItem.toString(),
                url = binding.etContentUrl.text.toString(),
                contentLessonId = 1,
                contentTitle = binding.etContentTitle.text.toString(),
                contentDescription = binding.etContentDescription.text.toString()
            )

            viewModel.createLessonContent(lessonContent)
        }

        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.imgGoogleDrive.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            getContent.launch(intent)
        }

        binding.imgBrowser.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://")
            getContent.launch(intent)
        }

        binding.imgYouTube.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEARCH)
            intent.setPackage("com.google.android.youtube")
            intent.putExtra("query", "")
            getContent.launch(intent)
        }

        binding.etContentDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val length = s?.length ?: 0
                binding.tvDescriptionCounter.text = "$length/255"
                if (length > 255) {
                    binding.tvDescriptionCounter.setTextColor(resources.getColor(R.color.red, null))
                    Toast.makeText(
                        requireContext(),
                        "Description exceeds 255 characters!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    binding.tvDescriptionCounter.setTextColor(
                        resources.getColor(
                            R.color.md_theme_onSurface_highContrast,
                            null
                        )
                    )
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    is AddContentViewModel.AddContentScreenState.Loading -> {
                        Log.d(TAG, "Loading..")
                    }

                    is AddContentViewModel.AddContentScreenState.Success -> {
                        Toast.makeText(
                            requireContext(),
                            "Content added successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        resetForm()
                        Log.d(TAG, "Content added successfully: ${state.response}")
                    }

                    is AddContentViewModel.AddContentScreenState.Failure -> {
                        Log.e(TAG, "Error adding content: ${state.throwable.message}")
                    }
                }
            }
        }
    }

    private fun resetForm() {
        binding.spinnerContentType.setSelection(0)
        binding.etContentUrl.text.clear()
        binding.etContentTitle.text.clear()
        binding.etContentDescription.text.clear()
    }
}