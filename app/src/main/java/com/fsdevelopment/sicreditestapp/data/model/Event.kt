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
    val people: List<People>,
    var address: String = "",
    var isFavorite: Boolean = false
) : Parcelable {

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
    
    override fun equals(other: Any?) = (other is Event) && id == other.id
}