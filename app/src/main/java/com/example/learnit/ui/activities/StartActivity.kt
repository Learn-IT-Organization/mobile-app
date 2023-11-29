package com.example.learnit.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.learnit.R
import com.example.learnit.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val handlerThread = HandlerThread("SplashHandlerThread", -10)
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val SPLASH_TIME_OUT = 4000L;
        handler.postDelayed({
            val intent = Intent(this@StartActivity, MainActivity::class.java)
            startActivity(intent)
            finish() },
            SPLASH_TIME_OUT)

        val topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        val bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)
        val scaleUpAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_up_animation)

        val splashImage = binding.splashImage
        val splashTitle = binding.splashTitle

        splashImage.startAnimation(topAnim)
        splashImage.startAnimation(scaleUpAnimation)
        splashTitle.startAnimation(bottomAnim)


    }
}