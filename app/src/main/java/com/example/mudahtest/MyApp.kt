package com.example.mudahtest

import android.app.Application
import com.example.mudahtest.di.allModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Suppress("Unused")
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            // declare used Android context
            androidContext(this@MyApp)
            // declare modules )
            modules(allModules)
        }
    }
}