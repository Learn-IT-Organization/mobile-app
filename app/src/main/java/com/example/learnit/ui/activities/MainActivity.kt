package com.example.learnit.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.learnit.R
import com.example.learnit.data.MyFirebaseMessagingService
import com.example.learnit.databinding.ActivityMainBinding
import com.example.learnit.ui.feature.courses.quiz.fragment.QuizFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    fun hideBottomNavigationView() {
        binding.bottomNavigationView.visibility = View.GONE
    }

    fun showBottomNavigationView() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = this.findNavController(R.id.nav_host_fragment)
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setupWithNavController(navController)

        MyFirebaseMessagingService.getInstance().notificationLiveData.observe(this) {
            if (it) {
                bottomNavigationView.menu.findItem(R.id.notificationsFragment)
                    .setIcon(R.drawable.nav_notifications_active)
                Log.d("MainActivity", "Notification received")
            }
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }

                R.id.coursesFragment -> {
                    navController.navigate(R.id.coursesFragment)
                    true
                }

                R.id.settingsFragment -> {
                    navController.navigate(R.id.settingsFragment)
                    true
                }

                R.id.notificationsFragment -> {
                    navController.navigate(R.id.notificationsFragment)
                    true
                }

                else -> false
            }
        }
    }
}

