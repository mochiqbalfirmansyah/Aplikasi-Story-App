package com.iqbal.submission.view.activity.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iqbal.submission.data.local.model.UserModel
import com.iqbal.submission.data.repository.AuthRepository
import com.iqbal.submission.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel()  {
    fun login(email:String, password:String) = authRepository.login(email, password)

    fun saveSession(user:UserModel) {
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }
}