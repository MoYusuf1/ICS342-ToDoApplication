package com.example.to_do

import AppNavigator
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.to_do.datastore.UserPreferencesManager
import com.example.to_do.network.apiKey
import com.example.to_do.ui.theme.ToDoTheme
import com.example.to_do.viewmodal.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoTheme {
                val userPreferencesManager = UserPreferencesManager(context = this)
                val factory = ViewModelFactory(userPreferencesManager)
                AppNavigator(userPreferencesManager, factory, apiKey = apiKey)
            }
        }
    }
}
