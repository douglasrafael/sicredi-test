package com.fsdevelopment.sicreditestapp.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

/**
 * Represents an event.
 *
 * @author Copyright (c) 2020, Douglas Rafael
 */
@Keep
@Parcelize
data class Event(
    val id: String?,
    val title: String,
    val description: String,
    val date: Long,
    val price: Double,
    val image: String,
    val latitude: String,
    val longitude: String,
    val people: List<People>?
): Parcelable