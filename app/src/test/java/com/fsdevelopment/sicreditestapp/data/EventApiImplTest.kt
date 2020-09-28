package com.fsdevelopment.sicreditestapp.data

import com.fsdevelopment.sicreditestapp.data.model.common.ErrorResponse
import com.fsdevelopment.sicreditestapp.data.repository.network.EventApi
import com.fsdevelopment.sicreditestapp.data.repository.network.EventApiImpl
import com.fsdevelopment.sicreditestapp.data.repository.network.EventService
import com.fsdevelopment.sicreditestapp.data.repository.network.helpers.NetworkHelper
import com.fsdevelopment.sicreditestapp.data.repository.network.helpers.ResultWrapper
import com.fsdevelopment.sicreditestapp.mocks.event1
import com.fsdevelopment.sicreditestapp.mocks.event2
import com.fsdevelopment.sicreditestapp.mocks.event3
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException

@ExperimentalCoroutinesApi
class EventApiImplTest {
    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var eventService: EventService
    private lateinit var eventApi: EventApi
    private lateinit var networkHelper: NetworkHelper
    private lateinit var resultEventListError: ResultWrapper.Error
    private val resultEventListSuccess = ResultWrapper.Success(arrayListOf(event1, event2, event3))

    @Before
    fun setUp() {
        eventService = mock()
        networkHelper = NetworkHelper(mock())
        eventApi = EventApiImpl(eventService, networkHelper, testDispatcher)

        val mockException: HttpException = mock()
        whenever(mockException.code()).thenReturn(500)
        whenever(mockException.message()).thenReturn("Internal Server Error")

        resultEventListError = ResultWrapper.Error(
            500, ErrorResponse(500, "Internal Server Error", "")
        )

        runBlocking {
            whenever(eventApi.listEvents(-1, -15)).thenThrow(mockException)
            whenever(eventApi.listEvents(1, 15))
                .thenReturn(resultEventListSuccess)
        }
    }

    @After
    fun tearDown() {
//        Dispatchers.resetMain()
//        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `test getWeather when valid location is requested, then weather is returned`() =
        runBlocking {
            when (val result = eventApi.listEvents(1, 15)) {
                is ResultWrapper.Success -> assertEquals(resultEventListSuccess, result.value)
                is ResultWrapper.Error -> fail()
            }
        }

    @Test
    fun `test error when valid location is requested, then weather is returned`() =
        runBlocking {
//            assertEquals(resultEventListError, eventApi.listEvents(-1, -15))
            when (val result = eventApi.listEvents(-1, -15)) {
                is ResultWrapper.Error -> assertEquals(
                    ResultWrapper.Error(
                        500, ErrorResponse(500, "Internal Server Error", "")
                    ), result
                )
                is ResultWrapper.Success -> fail()
            }
        }

//    @Test
//    fun `When the service's listEvent() must be successfully invoked`(): Unit = runBlockingTest {
//        coEvery { eventApi.listEvents(1, 10) } returns mockResultEventListSuccess
//
//
//        coVerify { eventApi.listEvents(1, 10) }
//    }
}