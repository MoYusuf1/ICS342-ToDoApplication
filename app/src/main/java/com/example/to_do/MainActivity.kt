package com.example.to_do

import AppNavigator
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.to_do.datastore.UserPreferencesManager
import com.example.to_do.viewmodal.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val userPreferencesManager = UserPreferencesManager(context = this)
            val factory = ViewModelFactory(userPreferencesManager)
            AppNavigator(userPreferencesManager, factory, apiKey = "6cd34bb8-1740-4dbd-98cf-35e2b77ae787")
        }
    }
}
