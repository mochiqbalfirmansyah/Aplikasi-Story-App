package com.iqbal.submission.view.activity.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.iqbal.submission.R
import com.iqbal.submission.data.local.model.UserModel
import com.iqbal.submission.databinding.ActivityLoginBinding
import com.iqbal.submission.utils.Result
import com.iqbal.submission.view.activity.ViewModelFactory
import com.iqbal.submission.view.activity.main.MainActivity
import com.iqbal.submission.view.activity.signup.SignUpActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.signUpButton.setOnClickListener {
            navigateToSignUp()
        }
        binding.loginButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString().trim()
            val password = binding.edLoginPassword.text.toString().trim()

            viewModel.login(email, password).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            val data = result.data.loginResult
                            if (data != null) {
                                val token = data.token
                                val name = data.name

                                viewModel.saveSession(UserModel(email,token))
                                showStatusSuccess(name)
                            }
                        }
                        is Result.Error -> {
                            showLoading(false)
                            showAlert(result.error)
                        }
                    }
                }
            }
        }
    }

    private fun navigateToMain(){
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun navigateToSignUp(){
        val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun showLoading(isLoading:Boolean) {
        if (isLoading) binding.progressBar.visibility =
            View.VISIBLE else binding.progressBar.visibility = View.GONE
    }

    private fun showAlert(string: String) {
        Snackbar.make(binding.root, string, Snackbar.LENGTH_LONG).show()
    }

   private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val message =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                message,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                login
            )
            startDelay = 100
        }.start()
    }

    private fun showStatusSuccess(name:String){
        AlertDialog.Builder(this@LoginActivity).apply {
            setTitle(getString(R.string.login_berhasil))
            setMessage(getString(R.string.selamat_datang, name))
            setPositiveButton(getString(R.string.next)) { _, _ ->
                navigateToMain()
            }
            create()
            show()
        }
    }
}