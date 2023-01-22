package com.example.branch.base

import android.app.Application
import com.example.branch.network.appModule
import com.example.branch.network.networkModule
import com.example.branch.network.prefModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {

            //Setup Logger
            androidLogger(Level.DEBUG)

            //inject Android context
            androidContext(this@App)
            modules(listOf(prefModule, networkModule, appModule))
        }
    }
}