package com.iqbal.submission.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.iqbal.submission.data.api.response.LoginResponse
import com.iqbal.submission.data.api.response.RegisterResponse
import com.iqbal.submission.data.api.retrofit.ApiService
import com.iqbal.submission.utils.Result

class AuthRepository(
    private val apiService: ApiService
) {
    fun login(email: String, password: String) : LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.login(email, password)
            emit(Result.Success(result))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun register(name: String, email: String, password: String) : LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try{
            val result = apiService.register(name, email, password)
            emit(Result.Success(result))
        } catch (e : Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }
}