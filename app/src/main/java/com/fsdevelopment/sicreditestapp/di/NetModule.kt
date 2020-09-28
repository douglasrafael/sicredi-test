package com.fsdevelopment.sicreditestapp.di

import android.app.Application
import android.content.Context
import coil.ImageLoader
import com.fsdevelopment.sicreditestapp.BuildConfig
import com.fsdevelopment.sicreditestapp.data.repository.network.EventApi
import com.fsdevelopment.sicreditestapp.data.repository.network.EventApiImpl
import com.fsdevelopment.sicreditestapp.data.repository.network.EventService
import com.fsdevelopment.sicreditestapp.data.repository.network.helpers.NetworkHelper
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

val netModule = module {
    factory { provideGson() }
    factory { provideLoggerInterceptor() }
    factory { provideCache(androidApplication()) }
    factory { NetworkHelper(get()) }

    single { provideRetrofit(get(), get()) }
    single { provideEventService(get()) }
    single { provideEventApi(get(), get()) }
    single { provideHttpClient(get()) }
    single { provideImageLoader(get(), get()) }
}

/**
 * Provide Cache instance used in OkHttpClient.
 */
fun provideCache(application: Application): Cache {
    val cacheSize = 10 * 1024 * 1024
    return Cache(application.cacheDir, cacheSize.toLong())
}

/**
 * Provide Gson instance used in Retrofit.
 */
fun provideGson(): Gson {
    return GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()
}

/**
 * Provide http client security.
 */
fun provideHttpClient(cache: Cache): OkHttpClient {
    return OkHttpClient
        .Builder()
        .cache(cache)
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES)
        .writeTimeout(1, TimeUnit.MINUTES)
        .build()
}

/**
 * Provides Interceptor for logs used in OkHttpClient.
 */
fun provideLoggerInterceptor(): Interceptor {
    val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
        override fun log(message: String) = Timber.tag("OkHttp").d(message)
    })
    logging.level = HttpLoggingInterceptor.Level.BODY
    return logging
}

/**
 * Provide Retrofit instance.
 */
fun provideRetrofit(factory: Gson, client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.EVENT_API_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(factory))
        .build()
}

/**
 * Provide EventService instance.
 */
fun provideEventService(retrofit: Retrofit): EventService {
    return retrofit.create(EventService::class.java)
}

/**
 * Provide EventApi instance.
 */
fun provideEventApi(service: EventService, networkHelper: NetworkHelper): EventApi {
    return EventApiImpl(service, networkHelper, Dispatchers.IO)
}

/**
 * Provide ImageLoader(Coil) instance.
 */
fun provideImageLoader(context: Context, httpClient: OkHttpClient): ImageLoader {
    return ImageLoader.Builder(context)
        .crossfade(false)
        .okHttpClient { httpClient }
        .build()
}