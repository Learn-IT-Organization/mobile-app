package com.example.learnit.ui.feature.splash.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.learnit.R
import com.example.learnit.databinding.FragmentSplashBinding
import com.example.learnit.ui.activities.MainActivity
import com.example.learnit.ui.feature.splash.viewModel.SplashViewModel

class SplashFragment : Fragment() {

    private val viewModel: SplashViewModel by viewModels()
    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)

        val topAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.top_animation)
        val bottomAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.bottom_animation)
        val scaleUpAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_up_animation)

        val splashImage = binding.splashImage
        val splashTitle = binding.splashTitle

        splashImage.startAnimation(topAnim)
        splashImage.startAnimation(scaleUpAnimation)
        splashTitle.startAnimation(bottomAnim)

        Handler().postDelayed({
            if (viewModel.verifyTokenValid()) {
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
            } else {
                findNavController().navigate(R.id.action_SplashFragment_to_LoginFragment)
            }
        }, 3000)

        return binding.root
    }

}