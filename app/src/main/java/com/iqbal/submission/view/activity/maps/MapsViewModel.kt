package com.iqbal.submission.view.activity.maps

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.iqbal.submission.data.api.response.GetAllStoriesResponse
import com.iqbal.submission.data.api.response.ListStoryItem
import com.iqbal.submission.data.repository.StoryRepository
import com.iqbal.submission.di.Injection
import com.iqbal.submission.utils.Result
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MapsViewModel (private val storyRepository: StoryRepository) : ViewModel() {
    fun getMaps() =  storyRepository.getStoriesWithLocation()
}