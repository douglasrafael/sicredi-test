package com.fsdevelopment.sicreditestapp.data.repository.net.api

import com.fsdevelopment.sicreditestapp.data.repository.net.helpers.NetworkHelper
import kotlinx.coroutines.CoroutineDispatcher

class EventApiImpl(
    private val service: EventService,
    private val networkHelper: NetworkHelper,
    private val dispatcher: CoroutineDispatcher
) : EventApi {
}