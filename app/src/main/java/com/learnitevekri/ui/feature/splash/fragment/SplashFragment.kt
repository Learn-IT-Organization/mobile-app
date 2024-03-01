package com.learnitevekri.ui.feature.splash.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.learnitevekri.R
import com.learnitevekri.databinding.FragmentSplashBinding
import com.learnitevekri.ui.activities.MainActivity
import com.learnitevekri.ui.feature.splash.viewModel.SplashViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

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

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("FCM", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            val token = task.result
            viewModel.sendFCMToken(token)

        })

        Handler().postDelayed({
            if (isAdded && !isDetached) {
                if (viewModel.verifyTokenValid()) {
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    findNavController().navigate(R.id.action_SplashFragment_to_LoginFragment)
                }
            }
        }, 3000)

        return binding.root
    }

}