package com.iqbal.submission.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.iqbal.submission.data.api.response.AddNewStoryResponse
import com.iqbal.submission.data.api.response.GetAllStoriesResponse
import com.iqbal.submission.data.api.response.ListStoryItem
import com.iqbal.submission.data.api.response.LoginResponse
import com.iqbal.submission.data.api.response.RegisterResponse
import com.iqbal.submission.data.api.retrofit.ApiService
import com.iqbal.submission.data.local.database.StoryDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class StoryRemoteMediatorTest {

    private var mockApi: ApiService = FakeApiService()
    private var mockDb: StoryDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        StoryDatabase::class.java
    ).allowMainThreadQueries().build()

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runBlocking  {
        val remoteMediator = StoryRemoteMediator(
            mockDb,
            mockApi,
        )
        val pagingState = PagingState<Int, ListStoryItem>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @After
    fun tearDown() {
        mockDb.clearAllTables()
    }
}

class FakeApiService : ApiService {
    override suspend fun register(name: String, email: String, password: String): RegisterResponse {
        TODO("Not yet implemented")
    }

    override suspend fun login(email: String, password: String): LoginResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getAllStories(page: Int, size: Int): GetAllStoriesResponse {
        val items: MutableList<ListStoryItem> = arrayListOf()

        for (i in 0..100) {
            val quote = ListStoryItem(
                i.toString(),
                "photoUrl + $i",
                "createdAt $i",
                "name $i",
                "description $i",
            )
            items.add(quote)
        }
        return GetAllStoriesResponse(items.subList((page - 1) * size, (page - 1) * size + size))
    }

    override suspend fun addNewStory(
        file: MultipartBody.Part,
        description: RequestBody
    ): AddNewStoryResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getStoriesWithLocation(location: Int): GetAllStoriesResponse {
        TODO("Not yet implemented")
    }
}