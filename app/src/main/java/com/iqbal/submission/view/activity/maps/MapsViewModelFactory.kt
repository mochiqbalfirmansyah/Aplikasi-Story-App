package com.iqbal.submission.view.activity.maps

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iqbal.submission.data.repository.StoryRepository
import com.iqbal.submission.di.Injection

class MapsViewModelFactory private constructor(private val storyRepository: StoryRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(storyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        private var instance: MapsViewModelFactory? = null

        fun getInstance(context: Context): MapsViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: MapsViewModelFactory(Injection.provideStoryRepository(context))
            }.also { instance = it }
    }
}