package com.fsdevelopment.sicreditestapp.data.model

import androidx.annotation.Keep

/**
 * Represents check in at a given event.
 *
 * @author Copyright (c) 2020, Douglas Rafael
 */
@Keep
data class CheckIn(
    val name: String,
    val email: String,
    val eventId: String
)