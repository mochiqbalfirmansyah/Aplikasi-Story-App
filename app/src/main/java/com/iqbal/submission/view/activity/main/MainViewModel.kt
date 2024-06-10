package com.iqbal.submission.view.activity.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.iqbal.submission.data.api.response.ListStoryItem
import com.iqbal.submission.data.local.model.UserModel
import com.iqbal.submission.data.repository.StoryRepository
import com.iqbal.submission.data.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepository: UserRepository,
    private val storyRepository: StoryRepository
) : ViewModel()  {

    val story: LiveData<PagingData<ListStoryItem>> = storyRepository.getAllStories().cachedIn(viewModelScope)

//    fun getAllStories() = storyRepository.getAllStories()

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}