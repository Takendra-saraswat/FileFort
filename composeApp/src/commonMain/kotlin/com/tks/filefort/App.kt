package com.tks.filefort

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.tks.filefort.presentation.SplashScreen
import com.tks.filefort.presentation.theme.FileFortTheme

@Composable
fun App() {
    FileFortTheme {
        Navigator(SplashScreen())
    }
}