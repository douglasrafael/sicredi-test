package com.fsdevelopment.sicreditestapp.data.repository.network

import com.fsdevelopment.sicreditestapp.data.model.CheckIn
import com.fsdevelopment.sicreditestapp.data.model.Event
import com.fsdevelopment.sicreditestapp.data.repository.network.helpers.ResultWrapper

/**
 * Rest API Event Interface.
 *
 * @author Copyright (c) 2020, Douglas Rafael
 */
interface EventApi {
    suspend fun listEvents(search: String? = null): ResultWrapper<List<Event>>

    suspend fun checkIn(checkIn: CheckIn): ResultWrapper<Unit>
}