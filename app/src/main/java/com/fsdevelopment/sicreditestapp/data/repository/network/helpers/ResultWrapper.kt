package com.fsdevelopment.sicreditestapp.data.repository.network.helpers

import com.fsdevelopment.sicreditestapp.data.model.common.ErrorResponse

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Error(val code: Int? = null, val error: ErrorResponse? = null) :
        ResultWrapper<Nothing>()
}