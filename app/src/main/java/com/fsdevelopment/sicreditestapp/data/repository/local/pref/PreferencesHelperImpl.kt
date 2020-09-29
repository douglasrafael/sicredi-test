package com.fsdevelopment.sicreditestapp.data.repository.local.pref

import android.content.SharedPreferences

/**
 * Class to perform operations on the device's shared preference.
 * The data is saved encrypted.
 *
 * @author Copyright (c) 2020, Douglas Rafael
 */
class PreferencesHelperImpl(
    private val mPrefs: SharedPreferences
) : PreferencesHelper {
    companion object {
        const val SEPARATOR = ","
    }

    override suspend fun saveListOfStr(key: String, values: List<String>) {
        mPrefs.edit()
            .putString(key, values.joinToString(SEPARATOR))
            .apply()
    }

    override suspend fun getListOfStr(key: String): MutableList<String> {
        val result = arrayListOf<String>()
        mPrefs.getString(key, "")?.also {
            if (it.isEmpty()) return result
            result.addAll(it.split(SEPARATOR))
        }
        return result
    }
}