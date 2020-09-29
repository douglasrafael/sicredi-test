package com.fsdevelopment.sicreditestapp.repository

import com.fsdevelopment.sicreditestapp.data.repository.Repository
import com.fsdevelopment.sicreditestapp.data.repository.network.helpers.NetworkHelper
import com.fsdevelopment.sicreditestapp.data.repository.network.helpers.ResultWrapper
import com.fsdevelopment.sicreditestapp.mocks.checkIn1
import com.fsdevelopment.sicreditestapp.mocks.event1
import com.fsdevelopment.sicreditestapp.mocks.event2
import com.fsdevelopment.sicreditestapp.mocks.event3
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException


@ExperimentalCoroutinesApi
class RepositoryTest {
    companion object {
        const val KEY_STR = "key"
    }

    private lateinit var repo: Repository
    private lateinit var networkHelper: NetworkHelper

    private val testDispatcher = TestCoroutineDispatcher()
    private val resultEventListSuccess = ResultWrapper.Success(arrayListOf(event1, event2, event3))
    private val resultEventCheckIn = ResultWrapper.Success(arrayListOf(event2))
    private val resultEvenCheckInSuccess = ResultWrapper.Success(Unit)
    private val eventsIds = arrayListOf("2", "3")

    @Before
    fun setUp() {
        repo = mock()
        networkHelper = NetworkHelper(mock())

        val mockException: HttpException = mock()
        whenever(mockException.code()).thenReturn(500)

        runBlocking {
            whenever(repo.listEvents())
                .thenReturn(resultEventListSuccess)
            whenever(repo.checkIn(checkIn1)).thenReturn(resultEvenCheckInSuccess)
            whenever(repo.listCheckInEvents()).thenReturn(resultEventCheckIn)
            whenever(repo.getFavorites()).thenReturn(eventsIds)
            whenever(repo.saveFavorite("1")).thenReturn(Unit)
            whenever(repo.removeFavorite("1")).thenReturn(Unit)
            whenever(repo.saveListOfStr(KEY_STR, eventsIds)).thenReturn(Unit)
            whenever(repo.getListOfStr(KEY_STR)).thenReturn(eventsIds)
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `When event listing is successful`() =
        runBlocking {
            when (val result = repo.listEvents()) {
                is ResultWrapper.Success -> assertEquals(resultEventListSuccess, result)
                is ResultWrapper.Error -> fail()
            }
        }

    @Test
    fun `When check-in is successful`() =
        runBlocking {
            when (val result = repo.checkIn(checkIn1)) {
                is ResultWrapper.Success -> assertEquals(Unit, result.value)
                is ResultWrapper.Error -> fail()
            }
        }

    @Test
    fun `When list check-in events is successful`() = runBlocking {
        assertEquals(repo.listCheckInEvents(), resultEventCheckIn)
    }

    @Test
    fun `When list favorites is successful`() = runBlocking {
        assertEquals(repo.getFavorites(), eventsIds)
    }

    @Test
    fun `When save a favorite event`() = runBlocking {
        assertEquals(repo.saveFavorite("1"), Unit)
    }

    @Test
    fun `When remove a favorite event`() = runBlocking {
        assertEquals(repo.removeFavorite("1"), Unit)
    }

    @Test
    fun `When saving string list with event ids`() = runBlocking {
        assertEquals(repo.saveListOfStr(KEY_STR, eventsIds), Unit)
    }

    @Test
    fun `When recovers string list with event ids`() = runBlocking {
        assertEquals(repo.getListOfStr(KEY_STR), eventsIds)
    }
}