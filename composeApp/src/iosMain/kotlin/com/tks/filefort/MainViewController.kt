package com.tks.filefort

import androidx.compose.ui.window.ComposeUIViewController
import cafe.adriel.voyager.navigator.Navigator
import com.tks.filefort.presentation.home.HomeScreen

fun MainViewController() = ComposeUIViewController { Navigator(HomeScreen())  }