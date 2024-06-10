package com.iqbal.submission.view.activity.signup

import androidx.lifecycle.ViewModel
import com.iqbal.submission.data.repository.AuthRepository

class SignUpViewModel(
    private val authRepository: AuthRepository,
) : ViewModel()  {
    fun register(name:String,email:String,password:String) = authRepository.register(name, email, password)
}