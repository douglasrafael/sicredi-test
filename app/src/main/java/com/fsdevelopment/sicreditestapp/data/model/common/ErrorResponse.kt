package com.fsdevelopment.sicreditestapp.data.model.common

import androidx.annotation.Keep

/**
 * Represents the Error for HTTP requests.
 *
 * @author Copyright (c) 2020, Douglas Rafael
 */
@Keep
class ErrorResponse(
    val code: Int,
    val message: String?,
    val description: String?
)
