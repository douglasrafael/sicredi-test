package com.fsdevelopment.sicreditestapp.data.repository.network

import com.fsdevelopment.sicreditestapp.data.model.CheckIn
import com.fsdevelopment.sicreditestapp.data.model.Event
import com.fsdevelopment.sicreditestapp.data.repository.network.helpers.NetworkHelper
import com.fsdevelopment.sicreditestapp.data.repository.network.helpers.ResultWrapper
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Class responsible for making HTTP requests to the Event Rest API.
 * Implements the EventApi interface.
 *
 * @author Copyright (c) 2020, Douglas Rafael
 */
class EventApiImpl(
    private val service: EventService,
    private val networkHelper: NetworkHelper,
    private val dispatcher: CoroutineDispatcher
) : EventApi {
    override suspend fun listEvents(search: String?): ResultWrapper<List<Event>> {
        return networkHelper.safeApiCall(dispatcher) {
            service.listEvents(search)
        }
    }

    override suspend fun checkIn(checkIn: CheckIn): ResultWrapper<Unit> {
        return networkHelper.safeApiCall(dispatcher) {
            service.checkIn(checkIn)
        }
    }
}