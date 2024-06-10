package com.iqbal.submission.view.activity.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.iqbal.submission.data.local.model.UserModel
import com.iqbal.submission.data.repository.UserRepository

class SplashViewModel(
    private val userRepository: UserRepository
) : ViewModel()  {
    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }
}