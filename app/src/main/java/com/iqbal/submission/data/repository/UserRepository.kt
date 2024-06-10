package com.iqbal.submission.data.repository

import com.iqbal.submission.data.local.model.UserModel
import com.iqbal.submission.data.local.pref.UserPreference
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val userPreference: UserPreference
) {
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }
}