package com.example.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.MainActivity

object RuangWargaNotificationHelper {

    const val CHANNEL_EMERGENCY = "channel_rw_emergency"
    const val CHANNEL_COMMUNITY = "channel_rw_community"
    const val CHANNEL_SERVICE = "channel_rw_services"

    /**
     * Inisialisasi Channel Notifikasi untuk Android 8.0 (Oreo) ke atas.
     */
    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // 1. Channel Peringatan Darurat Siaga (High Importance, Sound & Vibration)
            val emergencyChannel = NotificationChannel(
                CHANNEL_EMERGENCY,
                "Peringatan Darurat Siaga Warga",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifikasi darurat kebencanaan, kebakaran, dan alarm siaga RT/RW"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500, 200, 500)
            }

            // 2. Channel Pengumuman & Agenda Warga
            val communityChannel = NotificationChannel(
                CHANNEL_COMMUNITY,
                "Pengumuman & Agenda RW",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Informasi kerja bakti, pengumuman warga, dan kegiatan RT/RW"
            }

            // 3. Channel Layanan Surat & Iuran
            val serviceChannel = NotificationChannel(
                CHANNEL_SERVICE,
                "Layanan Surat & Iuran Kas",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Update status pengajuan surat pengantar dan konfirmasi pembayaran iuran"
            }

            notificationManager.createNotificationChannel(emergencyChannel)
            notificationManager.createNotificationChannel(communityChannel)
            notificationManager.createNotificationChannel(serviceChannel)
        }
    }

    /**
     * Menampilkan notifikasi darurat siaga warga.
     */
    fun showEmergencyAlertNotification(
        context: Context,
        title: String,
        message: String,
        location: String
    ) {
        createNotificationChannels(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            1001,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder = NotificationCompat.Builder(context, CHANNEL_EMERGENCY)
            .setSmallIcon(android.R.drawable.stat_sys_warning)
            .setContentTitle("🚨 SIAGA DARURAT: $title")
            .setContentText("Lokasi: $location • $message")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("🚨 Peringatan Siaga Warga\nLokasi: $location\n\n$message\n\nMohon warga sekitar tetap waspada dan saling berkoordinasi.")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setSound(defaultSoundUri)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        try {
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify((System.currentTimeMillis() % 10000).toInt(), builder.build())
        } catch (e: SecurityException) {
            // Permission not granted on Android 13+
        }
    }

    /**
     * Menampilkan notifikasi update status surat atau layanan.
     */
    fun showServiceStatusNotification(
        context: Context,
        title: String,
        message: String
    ) {
        createNotificationChannels(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            1002,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_SERVICE)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        try {
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify((System.currentTimeMillis() % 10000).toInt(), builder.build())
        } catch (e: SecurityException) {
            // Permission not granted
        }
    }
}
