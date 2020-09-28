package com.fsdevelopment.sicreditestapp

import android.app.Application
import com.fsdevelopment.sicreditestapp.di.netModule
import com.fsdevelopment.sicreditestapp.di.repositoryModule
import com.fsdevelopment.sicreditestapp.di.viewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@App)

            koin.loadModules(arrayListOf(netModule, repositoryModule, viewModel))
            koin.createRootScope()
        }
    }
}