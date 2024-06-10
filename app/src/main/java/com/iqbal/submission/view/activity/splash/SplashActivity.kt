package com.iqbal.submission.view.activity.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.iqbal.submission.databinding.ActivitySplashBinding
import com.iqbal.submission.view.activity.ViewModelFactory
import com.iqbal.submission.view.activity.main.MainActivity
import com.iqbal.submission.view.activity.onboarding.OnBoardingActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val viewModel by viewModels<SplashViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val handler = Handler(Looper.getMainLooper())
        val durationSplash:Long = 2000
        handler.postDelayed({
            viewModel.getSession().observe(this) { user ->
                if (user.token.isNotEmpty()) {
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    startActivity(Intent(this@SplashActivity, OnBoardingActivity::class.java))
                    finish()
                }
            }
        }, durationSplash)
    }
}