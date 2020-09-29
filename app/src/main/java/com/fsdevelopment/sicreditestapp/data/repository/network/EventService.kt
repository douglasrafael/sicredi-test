package com.fsdevelopment.sicreditestapp.data.repository.network

import com.fsdevelopment.sicreditestapp.data.model.CheckIn
import com.fsdevelopment.sicreditestapp.data.model.Event
import retrofit2.http.*

/**
 * Retrofit interface responsible for making HTTP requests to the Event Rest API.
 *
 * @author Copyright (c) 2020, Douglas Rafael
 */
interface EventService {

    @GET("/api/events")
    suspend fun listEvents(@Query("title") search: String?): List<Event>

    @POST("/api/checkin")
    suspend fun checkIn(@Body checkIn: CheckIn)
}