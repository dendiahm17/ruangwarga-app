package com.example.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.media.ToneGenerator
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * EmergencyAudioAlertManager
 * Modul pemutar suara sirene darurat dan getaran serentak berkekuatan tinggi.
 * Menggunakan perpaduan ToneGenerator frekuensi sirene (800Hz - 1500Hz) & Ringtone Alarm
 * beserta pola getar darurat terus-menerus.
 */
object EmergencyAudioAlertManager {

    private var toneGenerator: ToneGenerator? = null
    private var emergencyRingtone: Ringtone? = null
    private var vibrator: Vibrator? = null
    private var sirenJob: Job? = null
    private val alertScope = CoroutineScope(Dispatchers.Default)

    @Volatile
    var isSirenPlaying: Boolean = false
        private set

    /**
     * Memulai pemutaran sirine darurat serentak dengan getaran HP.
     */
    fun startEmergencySiren(context: Context, maxDurationMs: Long = 60000L) {
        if (isSirenPlaying) return
        isSirenPlaying = true

        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
        try {
            // Pastikan volume Alarm tidak nol
            val maxVol = audioManager?.getStreamMaxVolume(AudioManager.STREAM_ALARM) ?: 10
            audioManager?.setStreamVolume(AudioManager.STREAM_ALARM, maxVol, 0)
        } catch (e: Exception) {
            // ignore
        }

        try {
            // Inisialisasi ToneGenerator pada stream Alarm
            toneGenerator = ToneGenerator(AudioManager.STREAM_ALARM, 100)
        } catch (e: Exception) {
            try {
                toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
            } catch (ex: Exception) {
                toneGenerator = null
            }
        }

        // Inisialisasi Ringtone Alarm sebagai layer suara keras
        try {
            var alarmUri: Uri? = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            if (alarmUri == null) {
                alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            }
            if (alarmUri == null) {
                alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            }
            if (alarmUri != null) {
                emergencyRingtone = RingtoneManager.getRingtone(context.applicationContext, alarmUri)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    emergencyRingtone?.audioAttributes = AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                }
                emergencyRingtone?.play()
            }
        } catch (e: Exception) {
            // ignore
        }

        // Inisialisasi Vibrator HP
        try {
            vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }

            if (vibrator?.hasVibrator() == true) {
                val vibrationPattern = longArrayOf(0, 500, 200, 500, 200, 800)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator?.vibrate(
                        VibrationEffect.createWaveform(vibrationPattern, 0) // 0 = berulang terus
                    )
                } else {
                    @Suppress("DEPRECATION")
                    vibrator?.vibrate(vibrationPattern, 0)
                }
            }
        } catch (e: Exception) {
            // ignore
        }

        // Loop coroutine modulasi sirene (Hi-Lo Siren Tone)
        sirenJob?.cancel()
        sirenJob = alertScope.launch {
            val startTime = System.currentTimeMillis()
            try {
                while (isActive && isSirenPlaying) {
                    // Cek batas durasi maksimum
                    if (System.currentTimeMillis() - startTime > maxDurationMs) {
                        break
                    }

                    // Pola Nada Tinggi (1400Hz - 1600Hz)
                    toneGenerator?.startTone(ToneGenerator.TONE_CDMA_EMERGENCY_RINGBACK, 500)
                    delay(520)

                    if (!isActive || !isSirenPlaying) break

                    // Pola Nada Rendah (800Hz - 950Hz)
                    toneGenerator?.startTone(ToneGenerator.TONE_CDMA_ALERT_NETWORK_LITE, 450)
                    delay(480)

                    if (!isActive || !isSirenPlaying) break

                    // Pola Peringatan Cepat (Fast Beeps)
                    toneGenerator?.startTone(ToneGenerator.TONE_SUP_ERROR, 400)
                    delay(420)
                }
            } finally {
                stopEmergencySiren()
            }
        }
    }

    /**
     * Menghentikan bunyi sirene dan getaran seketika.
     */
    fun stopEmergencySiren() {
        isSirenPlaying = false
        sirenJob?.cancel()
        sirenJob = null

        try {
            toneGenerator?.stopTone()
            toneGenerator?.release()
            toneGenerator = null
        } catch (e: Exception) {
            // ignore
        }

        try {
            if (emergencyRingtone?.isPlaying == true) {
                emergencyRingtone?.stop()
            }
            emergencyRingtone = null
        } catch (e: Exception) {
            // ignore
        }

        try {
            vibrator?.cancel()
            vibrator = null
        } catch (e: Exception) {
            // ignore
        }
    }
}
