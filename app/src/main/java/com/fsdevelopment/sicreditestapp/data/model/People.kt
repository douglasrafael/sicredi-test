package com.fsdevelopment.sicreditestapp.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

/**
 * Represents an people.
 *
 * @author Copyright (c) 2020, Douglas Rafael
 */
@Keep
@Parcelize
data class People(
    val id: String?,
    val name: String,
    val picture: String,
    val eventId: String?
) : Parcelable
