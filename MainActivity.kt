package com.example.pethabittracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.pethabittracker.ui.MainScreen

class MainActivity: ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      MainScreen()  // Compose UI
    }
  }
}
