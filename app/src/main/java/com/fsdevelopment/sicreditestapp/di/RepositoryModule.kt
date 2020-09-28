package com.fsdevelopment.sicreditestapp.di

import com.fsdevelopment.sicreditestapp.data.repository.Repository
import com.fsdevelopment.sicreditestapp.data.repository.RepositoryImpl
import com.fsdevelopment.sicreditestapp.data.repository.network.EventApi
import org.koin.dsl.module

val repositoryModule = module {
    single { provideRepository(get()) }
}

fun provideRepository(eventApi: EventApi): Repository {
    return RepositoryImpl(eventApi)
}
