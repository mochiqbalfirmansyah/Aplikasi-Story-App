package com.iqbal.submission.di

import android.content.Context
import com.iqbal.submission.data.api.retrofit.ApiConfig
import com.iqbal.submission.data.local.database.StoryDatabase
import com.iqbal.submission.data.local.pref.UserPreference
import com.iqbal.submission.data.local.pref.dataStore
import com.iqbal.submission.data.repository.AuthRepository
import com.iqbal.submission.data.repository.StoryRepository
import com.iqbal.submission.data.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {

    fun provideAuthRepository(): AuthRepository {
        val apiService = ApiConfig.getApiService()
        return AuthRepository(apiService)
    }

    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository(pref)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return StoryRepository(apiService, database)
    }
}