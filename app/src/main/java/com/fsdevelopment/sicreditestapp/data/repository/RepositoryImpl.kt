package com.fsdevelopment.sicreditestapp.data.repository

import com.fsdevelopment.sicreditestapp.data.model.CheckIn
import com.fsdevelopment.sicreditestapp.data.model.Event
import com.fsdevelopment.sicreditestapp.data.repository.local.pref.PreferencesHelper
import com.fsdevelopment.sicreditestapp.data.repository.network.EventApi
import com.fsdevelopment.sicreditestapp.data.repository.network.helpers.ResultWrapper

/**
 * Class responsible for calling the methods of each data source.
 * Implements the Repository interface.
 *
 * @author Copyright (c) 2020, Douglas Rafael
 */
class RepositoryImpl(
    private val pref: PreferencesHelper,
    private val eventApi: EventApi
) : Repository {
    companion object {
        private const val KEY_EVENTS_FAVORITES = "key_favorites"
        private const val KEY_EVENTS_CHECK_IN = "key_check_in"
    }

    override suspend fun listEvents(search: String?): ResultWrapper<List<Event>> {
        val favorites = getFavorites()
        return eventApi.listEvents(search).apply {
            if (this is ResultWrapper.Success) {
                for (fav in favorites) {
                    findEventId(this.value, fav).also { eventIndex ->
                        if (eventIndex > -1) this.value[eventIndex].isFavorite = true
                    }
                }
            }
        }
    }

    override suspend fun checkIn(checkIn: CheckIn): ResultWrapper<Unit> {
        return eventApi.checkIn(checkIn).apply {
            if (this is ResultWrapper.Success) {
                val checkInList = getListOfStr(KEY_EVENTS_CHECK_IN).apply {
                    if (!this.contains(checkIn.eventId)) add(checkIn.eventId)
                }
                saveListOfStr(KEY_EVENTS_CHECK_IN, checkInList)
            }
        }
    }

    /**
     * Returns the position if it finds an element with the same id. otherwise it returns -1
     */
    private fun findEventId(events: List<Event>, id: String): Int {
        for ((index, elem) in events.withIndex()) {
            if (elem.id == id) return index
        }
        return -1
    }

    override suspend fun saveListOfStr(key: String, values: List<String>) {
        pref.saveListOfStr(key, values)
    }

    override suspend fun getListOfStr(key: String): MutableList<String> {
        return pref.getListOfStr(key)
    }

    override suspend fun getFavorites(): List<String> {
        return getListOfStr(KEY_EVENTS_FAVORITES)
    }

    override suspend fun saveFavorite(eventId: String) {
        val favorites = getListOfStr(KEY_EVENTS_FAVORITES).apply { add(eventId) }
        saveListOfStr(KEY_EVENTS_FAVORITES, favorites)
    }

    override suspend fun removeFavorite(eventId: String) {
        val favorites = getListOfStr(KEY_EVENTS_FAVORITES).apply { remove(eventId) }
        saveListOfStr(KEY_EVENTS_FAVORITES, favorites)
    }

    override suspend fun listCheckInEvents(): ResultWrapper<List<Event>> {
        val checkInList = getListOfStr(KEY_EVENTS_CHECK_IN)
        val result = arrayListOf<Event>()
        return eventApi.listEvents().apply {
            if (this is ResultWrapper.Success) {
                for (check in checkInList) {
                    findEventId(this.value, check).also { eventIndex ->
                        if (eventIndex > -1) result.add(this.value[eventIndex])
                    }
                }
                return ResultWrapper.Success(result)
            }
        }
    }
}