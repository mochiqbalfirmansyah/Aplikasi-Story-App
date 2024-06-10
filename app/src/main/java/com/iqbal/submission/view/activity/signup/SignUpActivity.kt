package com.iqbal.submission.view.activity.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.iqbal.submission.R
import com.iqbal.submission.databinding.ActivitySignUpBinding
import com.iqbal.submission.view.activity.ViewModelFactory
import com.iqbal.submission.utils.Result
import com.iqbal.submission.view.activity.login.LoginActivity

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val viewModel by viewModels<SignUpViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        with(binding){
            signupButton.setOnClickListener {
                register()
                val name = edRegisterName.text.toString().trim()
                val email = edRegisterEmail.text.toString().trim()
                val password = edRegisterPassword.text.toString().trim()
                Log.d("Input Name : ", name)
                Log.d("Input Email : ", email)
                Log.d("Input Password : ", password)
                print(edRegisterName.toString())
            }
            loginButton.setOnClickListener {
                navigateToLogin()
            }
        }

    }

    private fun register() {
        val name = binding.edRegisterName.text.toString().trim()
        val email = binding.edRegisterEmail.text.toString().trim()
        val password = binding.edRegisterPassword.text.toString().trim()
        viewModel.register(name, email, password).observe(this){ result ->
            if (result != null) {
                when(result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        val user = result.data
                        if (user.error!!) {
                            showAlert(user.message.toString())
                        } else {
                            showStatusSuccess()
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
    private fun navigateToLogin(){
        val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
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

    private fun showStatusSuccess(){
        AlertDialog.Builder(this@SignUpActivity).apply {
            setTitle(getString(R.string.yeah))
            setMessage(getString(R.string.your_account_successfully_created))
            setPositiveButton(getString(R.string.next)) { _, _ ->
                navigateToLogin()
            }
            create()
            show()
        }
    }
}