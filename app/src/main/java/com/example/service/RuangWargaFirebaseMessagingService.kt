package com.example.service

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import com.example.data.local.AppDatabase
import com.example.data.model.EmergencyAlertEntity
import com.example.utils.EmergencyAudioAlertManager
import com.example.utils.RuangWargaNotificationHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * RuangWargaFirebaseMessagingService
 * Menangani penerimaan sinyal alarm darurat / push notification di latar belakang
 * saat aplikasi ditutup / layar terkunci secara native.
 */
class RuangWargaFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "RuangWargaFCM"
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed FCM Token: $token")
        // Token dapat disimpan di Firestore users profile
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "Message received from: ${remoteMessage.from}")

        val data = remoteMessage.data
        val notificationType = data["type"] ?: "emergency"
        val jenisDarurat = data["jenisDarurat"] ?: (data["title"] ?: remoteMessage.notification?.title ?: "Peringatan Darurat")
        val lokasi = data["lokasi"] ?: (data["location"] ?: "Lingkungan RT 03 / RW 02")
        val pesan = data["pesan"] ?: (data["message"] ?: remoteMessage.notification?.body ?: "Sinyal alarm darurat diaktifkan oleh warga.")
        val catatan = data["catatan"] ?: pesan

        // 1. Tampilkan Notifikasi Sistem di Status Bar (Standar Android Native)
        RuangWargaNotificationHelper.showEmergencyAlertNotification(
            context = applicationContext,
            title = jenisDarurat,
            message = pesan,
            location = lokasi
        )

        // 3. Simpan ke database lokal
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = AppDatabase.getDatabase(applicationContext)
                val alert = EmergencyAlertEntity(
                    jenisDarurat = jenisDarurat,
                    judul = "🚨 $jenisDarurat di $lokasi",
                    lokasi = lokasi,
                    waktu = "Baru saja",
                    tingkatPrioritas = "Kritis",
                    status = "Aktif",
                    catatan = catatan
                )
                db.emergencyAlertDao().insertAlert(alert)
            } catch (e: Exception) {
                Log.e(TAG, "Error inserting emergency alert in background: ${e.message}")
            }
        }
    }
}
