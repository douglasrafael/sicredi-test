package com.fsdevelopment.sicreditestapp.data.repository

import com.fsdevelopment.sicreditestapp.data.model.CheckIn
import com.fsdevelopment.sicreditestapp.data.model.Event
import com.fsdevelopment.sicreditestapp.data.repository.network.EventApi
import com.fsdevelopment.sicreditestapp.data.repository.network.helpers.ResultWrapper

/**
 * Class responsible for calling the methods of each data source.
 * Implements the Repository interface.
 *
 * @author Copyright (c) 2020, Douglas Rafael
 */
class RepositoryImpl(
    private val eventApi: EventApi
) : Repository {
    override suspend fun listEvents(search: String?): ResultWrapper<List<Event>> {
        return eventApi.listEvents(search)
    }

    override suspend fun checkIn(checkIn: CheckIn): ResultWrapper<Unit> {
        return eventApi.checkIn(checkIn)
    }
}