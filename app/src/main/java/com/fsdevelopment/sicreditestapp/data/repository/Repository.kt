package com.fsdevelopment.sicreditestapp.data.repository

import com.fsdevelopment.sicreditestapp.data.model.Event
import com.fsdevelopment.sicreditestapp.data.repository.local.pref.PreferencesHelper
import com.fsdevelopment.sicreditestapp.data.repository.network.EventApi
import com.fsdevelopment.sicreditestapp.data.repository.network.helpers.ResultWrapper

/**
 * Interface covering all other data sources.
 *
 * @author Copyright (c) 2020, Douglas Rafael
 */
interface Repository : PreferencesHelper, EventApi {
    suspend fun getFavorites(): List<String>
    suspend fun saveFavorite(eventId: String)
    suspend fun removeFavorite(eventId: String)
    suspend fun listCheckInEvents(): ResultWrapper<List<Event>>
}