package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.ui.MainApp
import com.example.ui.theme.RTRWWargaTheme
import com.example.ui.viewmodel.RtrwViewModel
import com.example.ui.viewmodel.RtrwViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: RtrwViewModel by viewModels {
        RtrwViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RTRWWargaTheme {
                MainApp(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

