package com.tks.filefort.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import cafe.adriel.voyager.navigator.Navigator
import com.tks.filefort.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                Navigator(SplashScreen())
            }
        }

        // Navigate to MainActivity after 3 seconds
        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)// time to delay before navigating to home screen
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }
}