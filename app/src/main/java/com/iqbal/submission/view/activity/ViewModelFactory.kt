package com.iqbal.submission.view.activity

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iqbal.submission.data.repository.AuthRepository
import com.iqbal.submission.data.repository.StoryRepository
import com.iqbal.submission.data.repository.UserRepository
import com.iqbal.submission.di.Injection
import com.iqbal.submission.view.activity.addStory.AddStoryViewModel
import com.iqbal.submission.view.activity.login.LoginViewModel
import com.iqbal.submission.view.activity.main.MainViewModel
import com.iqbal.submission.view.activity.signup.SignUpViewModel
import com.iqbal.submission.view.activity.splash.SplashViewModel

class ViewModelFactory(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val storyRepository: StoryRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
           return MainViewModel(userRepository, storyRepository) as T
       } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
           return LoginViewModel(userRepository, authRepository) as T
       } else if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
           return SignUpViewModel(authRepository) as T
       } else if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
           return AddStoryViewModel(storyRepository) as T
       } else if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
           return SplashViewModel(userRepository) as T
       }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        fun getInstance(context: Context): ViewModelFactory{
                return ViewModelFactory(
                    Injection.provideUserRepository(context),
                    Injection.provideAuthRepository(),
                    Injection.provideStoryRepository(context)
                )
        }
    }
}