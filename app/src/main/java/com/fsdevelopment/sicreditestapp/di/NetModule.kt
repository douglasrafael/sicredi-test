package com.fsdevelopment.sicreditestapp.di

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import coil.ImageLoader
import com.fsdevelopment.sicreditestapp.BuildConfig
import com.fsdevelopment.sicreditestapp.data.repository.net.api.EventApi
import com.fsdevelopment.sicreditestapp.data.repository.net.api.EventApiImpl
import com.fsdevelopment.sicreditestapp.data.repository.net.api.EventService
import com.fsdevelopment.sicreditestapp.data.repository.net.helpers.NetworkHelper
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
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

const val EVENT_API_URL = "http://5f5a8f24d44d640016169133.mockapi.io/api/events"

val netModule = module {
    factory { provideGson() }
    factory { provideCache(androidApplication()) }

    single { provideRetrofit(get(), get()) }

    single { provideEventService(get()) }
    single { provideEventApi(get(), get()) }

    when (BuildConfig.DEBUG) {
        true -> single { provideUnsafeHttpClient(get()) }
        false -> single { provideHttpClient(get()) }
    }

    single { provideImageLoader(get(), get()) }
}

fun provideCache(application: Application): Cache {
    val cacheSize = 10 * 1024 * 1024
    return Cache(application.cacheDir, cacheSize.toLong())
}

fun provideGson(): Gson {
    return GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()
}

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
 * Supports connections with self-signed certificates
 */
fun provideUnsafeHttpClient(cache: Cache): OkHttpClient {
    val client = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES)
        .writeTimeout(1, TimeUnit.MINUTES)
        .cache(cache)

    try {
        // Create a trust manager that does not validate certificate chains
        val trustAllCerts =
            arrayOf<TrustManager>(
                object : X509TrustManager {
                    @SuppressLint("TrustAllX509TrustManager")
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                        // Not implemented!
                    }

                    @SuppressLint("TrustAllX509TrustManager")
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                        // Not implemented!
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            )

        // Install the all-trusting trust manager
        val sslContext = SSLContext.getInstance("TLSv1.2")
        sslContext.init(null, trustAllCerts, SecureRandom())

        // Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory = sslContext.socketFactory
        client.sslSocketFactory(sslSocketFactory, (trustAllCerts[0] as X509TrustManager))
            .hostnameVerifier { hostname: String, session: SSLSession ->
                hostname.equals(session.peerHost, ignoreCase = true)
            }

        /**
         * Add interceptors
         */
        if (BuildConfig.DEBUG) {
            client.addInterceptor(provideLogger())
        }
    } catch (e: Exception) {
        Timber.e(e)
    }
    return client.build()
}

fun provideLogger(): Interceptor {
    val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
        override fun log(message: String) = Timber.tag("OkHttp").d(message)
    })
    logging.level = HttpLoggingInterceptor.Level.BODY
    return logging
}

fun provideRetrofit(factory: Gson, client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(EVENT_API_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(factory))
        .build()
}

fun provideEventService(retrofit: Retrofit): EventService {
    return retrofit.create(EventService::class.java)
}

fun provideEventApi(service: EventService, networkHelper: NetworkHelper): EventApi {
    return EventApiImpl(service, networkHelper, Dispatchers.IO)
}

fun provideImageLoader(context: Context, httpClient: OkHttpClient): ImageLoader {
    return ImageLoader.Builder(context)
//        .availableMemoryPercentage(0.25) // Use 25% of the application's available memory.
        .crossfade(false)
        .okHttpClient { httpClient }
        .build()
}