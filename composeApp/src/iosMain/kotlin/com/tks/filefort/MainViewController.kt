package com.tks.filefort

import androidx.compose.ui.window.ComposeUIViewController
import com.tks.filefort.App
import com.tks.filefort.di.appModule
import com.tks.filefort.di.platformModule
import org.koin.core.context.startKoin // Ensure this import is correct
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

fun MainViewController() = ComposeUIViewController {
    // Initialize Napier for logging
    // It's good practice to initialize logging before Koin,
    // in case Koin initialization itself has issues you want to log.
    Napier.base(DebugAntilog())

    // Initialize Koin.
    // startKoin is idempotent, so it's safe to call it here.
    // It will only initialize Koin on the first call.
    startKoin {
        // Log Koin initialization
        Napier.d("Initializing Koin")
        modules(appModule, platformModule())
    }

    App()
}
