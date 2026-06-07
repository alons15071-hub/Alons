package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.ui.SmartLaunchViewModel
import com.example.ui.SmartLaunchViewModelFactory
import com.example.ui.screens.SmartLaunchAppContainer
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    // ViewModel constructed with the application's unique repository instance
    private val viewModel: SmartLaunchViewModel by viewModels {
        val app = application as SmartLaunchApp
        SmartLaunchViewModelFactory(app.repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                ) {
                    SmartLaunchAppContainer(viewModel = viewModel)
                }
            }
        }
    }
}
