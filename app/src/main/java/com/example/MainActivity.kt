package com.example

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.ui.MainApp
import com.example.ui.theme.RTRWWargaTheme
import com.example.ui.viewmodel.RtrwViewModel
import com.example.ui.viewmodel.RtrwViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: RtrwViewModel by viewModels {
        RtrwViewModelFactory(application)
    }

    private val requestNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Izin notifikasi darurat aktif
            com.example.utils.RuangWargaNotificationHelper.createNotificationChannels(applicationContext)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Minta Izin Notifikasi Sistem (Android 13+)
        checkAndRequestMandatoryPermissions()

        // 2. Tangani Intent Notifikasi jika aplikasi dibuka dari klik notifikasi
        handleNotificationIntent(intent)

        setContent {
            RTRWWargaTheme {
                MainApp(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleNotificationIntent(intent)
    }

    private fun handleNotificationIntent(intent: Intent?) {
        if (intent == null) return
        try {
            val shouldOpenEmergency = intent.getBooleanExtra("EXTRA_OPEN_EMERGENCY", false)
            if (shouldOpenEmergency) {
                viewModel.openAlarmScreen()
            }
        } catch (e: Throwable) {
            // Safe fallback
        }
    }

    private fun checkAndRequestMandatoryPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        // Matikan bunyi sirine seketika jika aplikasi ditutup atau keluar ke background
        com.example.utils.EmergencyAudioAlertManager.stopEmergencySiren()
    }
}

