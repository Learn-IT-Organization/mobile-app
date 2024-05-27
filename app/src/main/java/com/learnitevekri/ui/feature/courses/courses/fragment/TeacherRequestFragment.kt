package com.learnitevekri.ui.feature.courses.courses.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.learnitevekri.databinding.FragmentTeacherRequestBinding

class TeacherRequestFragment : Fragment() {
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

    }
}