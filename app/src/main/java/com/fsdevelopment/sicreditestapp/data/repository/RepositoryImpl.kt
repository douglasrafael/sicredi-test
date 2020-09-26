package com.fsdevelopment.sicreditestapp.data.repository

import com.fsdevelopment.sicreditestapp.data.repository.net.api.EventApi

class RepositoryImpl(
    private val eventApi: EventApi
) : Repository {
}