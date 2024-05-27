package com.learnitevekri.ui.feature.courses.courses.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.learnitevekri.data.SharedPreferences.getUserId
import com.learnitevekri.data.user.teacher.model.TeacherRequestData
import com.learnitevekri.databinding.FragmentTeacherRequestBinding
import com.learnitevekri.ui.feature.courses.courses.viewModel.TeacherRequestViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TeacherRequestFragment : Fragment() {
    private val viewModel: TeacherRequestViewModel by viewModels()

    private lateinit var binding: FragmentTeacherRequestBinding

    companion object {
        val TAG: String = FragmentTeacherRequestBinding::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeacherRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnRequestRight.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val fullName = binding.etFullName.text.toString()
            val userId = getUserId()

            val teacherRequestData = TeacherRequestData(
                userId = userId.toInt(),
                email = email,
                fullName = fullName,
            )

            if (email.isEmpty() || fullName.isEmpty()) {
                Snackbar.make(
                    requireView(),
                    "Please fill in all fields",
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            GlobalScope.launch(Dispatchers.Main) {
                val success = viewModel.sendTeacherRequest(teacherRequestData)
                if (success) {
                    binding.etEmail.text.clear()
                    binding.etFullName.text.clear()
                    Snackbar.make(
                        requireView(),
                        "Teacher request sent successfully",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    Snackbar.make(
                        requireView(),
                        "Failed to send teacher request",
                        Snackbar.LENGTH_SHORT
                    ).show()                }
            }
        }
    }
}