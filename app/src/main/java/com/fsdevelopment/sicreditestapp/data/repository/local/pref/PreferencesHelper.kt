package com.fsdevelopment.sicreditestapp.data.repository.local.pref

/**
 * Interface for Preferences Helper.
 *
 * @author Copyright (c) 2020, Douglas Rafael
 */
interface PreferencesHelper {
    suspend fun saveListOfStr(key: String, values: List<String>)
    suspend fun getListOfStr(key: String): MutableList<String>
}