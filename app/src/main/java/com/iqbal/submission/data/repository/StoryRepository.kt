package com.iqbal.submission.data.repository

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.iqbal.submission.data.api.retrofit.ApiService
import com.iqbal.submission.utils.Result
import com.google.gson.Gson
import com.iqbal.submission.data.api.response.AddNewStoryResponse
import com.iqbal.submission.data.api.response.GetAllStoriesResponse
import com.iqbal.submission.data.api.response.ListStoryItem
import com.iqbal.submission.data.api.response.StoryPagingSource
import com.iqbal.submission.data.local.database.StoryDatabase
import com.iqbal.submission.data.paging.StoryRemoteMediator
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepository(
    private val apiService: ApiService,
    private val storyDatabase: StoryDatabase
){

    fun getAllStories(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    fun addNewStory(imageFile: File, description: String): LiveData<Result<AddNewStoryResponse>> = liveData {
        emit(Result.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.addNewStory(multipartBody, requestBody)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, AddNewStoryResponse::class.java)
            emit(Result.Error(errorResponse.message))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getStoriesWithLocation(): LiveData<Result<GetAllStoriesResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStoriesWithLocation()
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d(ContentValues.TAG, "onFailure: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }
}
