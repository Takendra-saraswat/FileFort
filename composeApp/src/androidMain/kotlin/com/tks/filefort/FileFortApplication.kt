package com.tks.filefort.android

import android.app.Application
import com.tks.filefort.di.appModule
import com.tks.filefort.di.platformModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class FileFortApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Napier for logging
        Napier.base(DebugAntilog())

        // Initialize Koin
        startKoin {
            androidContext(this@FileFortApplication)
            modules(appModule, platformModule())
        }
    }
}