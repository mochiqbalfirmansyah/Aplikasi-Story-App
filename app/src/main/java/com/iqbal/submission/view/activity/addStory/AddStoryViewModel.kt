package com.iqbal.submission.view.activity.addStory

import androidx.lifecycle.ViewModel
import com.iqbal.submission.data.repository.StoryRepository
import com.iqbal.submission.data.repository.UserRepository
import java.io.File

class AddStoryViewModel(
    private val storyRepository: StoryRepository
) : ViewModel()  {
    fun addNewStory(file: File, description: String) = storyRepository.addNewStory(file, description)
}