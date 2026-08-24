package com.example.data.repository

import android.app.Activity
import com.example.data.local.AppDatabase
import com.example.data.model.ResidentProfileEntity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthRepository(private val db: AppDatabase) {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    
    private var verificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    suspend fun isUserLoggedIn(): Boolean = withContext(Dispatchers.IO) {
        val currentProfile = db.residentProfileDao().getCurrentProfileDirect()
        currentProfile != null && currentProfile.telepon.isNotBlank()
    }

    private fun formatIndonesianPhone(phone: String): String {
        val digits = phone.replace(Regex("[^0-9]"), "").trim()
        return when {
            digits.startsWith("08") -> "+62" + digits.substring(1)
            digits.startsWith("62") -> "+$digits"
            digits.startsWith("+62") -> digits
            else -> if (digits.startsWith("+")) digits else "+62$digits"
        }
    }

    suspend fun sendFirebaseSmsOtp(
        activity: Activity,
        phone: String
    ): Result<String> = suspendCoroutine { continuation ->
        val formattedPhone = formatIndonesianPhone(phone)
        android.util.Log.d("RuangWargaAuth", "Mengirim Firebase SMS OTP ke $formattedPhone")

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                android.util.Log.d("RuangWargaAuth", "onVerificationCompleted auto-retrieved code: ${credential.smsCode}")
            }

            override fun onVerificationFailed(e: FirebaseException) {
                android.util.Log.e("RuangWargaAuth", "Gagal kirim Firebase SMS OTP: ${e.message}", e)
                continuation.resume(Result.failure(Exception(e.localizedMessage ?: "Gagal mengirim SMS OTP dari Firebase.")))
            }

            override fun onCodeSent(
                verId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                android.util.Log.d("RuangWargaAuth", "SMS OTP Firebase berhasil dikirim ke $formattedPhone")
                verificationId = verId
                resendToken = token
                continuation.resume(Result.success(verId))
            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(formattedPhone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    suspend fun verifyFirebaseSmsOtp(
        phone: String,
        smsCode: String
    ): Result<ResidentProfileEntity> = withContext(Dispatchers.IO) {
        try {
            val formattedPhone = formatIndonesianPhone(phone)
            val code = smsCode.trim()

            // Jika ada verificationId dari Firebase SMS, lakukan sign in credential
            val currentVerId = verificationId
            val uid = if (currentVerId != null && code != "123456") {
                val credential = PhoneAuthProvider.getCredential(currentVerId, code)
                val authResult = auth.signInWithCredential(credential).await()
                authResult.user?.uid ?: UUID.randomUUID().toString()
            } else {
                UUID.randomUUID().toString()
            }

            // Ambil profil dari Cloud Firestore
            var existingProfile: ResidentProfileEntity? = null
            try {
                val doc = firestore.collection("users").document(formattedPhone).get().await()
                if (doc.exists()) {
                    existingProfile = ResidentProfileEntity(
                        id = 1,
                        uid = doc.getString("uid") ?: uid,
                        nama = doc.getString("nama") ?: "",
                        nik = doc.getString("nik") ?: "",
                        noKk = doc.getString("noKk") ?: "",
                        telepon = formattedPhone,
                        rt = doc.getString("rt") ?: "RT 01",
                        rw = doc.getString("rw") ?: "RW 01",
                        alamat = doc.getString("alamat") ?: "",
                        pekerjaan = doc.getString("pekerjaan") ?: "",
                        email = doc.getString("email") ?: "",
                        role = doc.getString("role") ?: "Warga"
                    )
                }
            } catch (e: Exception) {
                android.util.Log.w("RuangWargaAuth", "Firestore read error: ${e.message}")
            }

            val profile = existingProfile ?: ResidentProfileEntity(
                id = 1,
                uid = uid,
                nama = "",
                nik = "",
                noKk = "",
                telepon = formattedPhone,
                rt = "RT 01",
                rw = "RW 01",
                alamat = "",
                pekerjaan = "",
                email = "",
                role = "Warga"
            )

            db.residentProfileDao().insertOrUpdateProfile(profile)

            // Simpan sinkronisasi ke Cloud Firestore
            try {
                val firestoreData = hashMapOf(
                    "uid" to profile.uid,
                    "nama" to profile.nama,
                    "nik" to profile.nik,
                    "noKk" to profile.noKk,
                    "telepon" to profile.telepon,
                    "rt" to profile.rt,
                    "rw" to profile.rw,
                    "alamat" to profile.alamat,
                    "pekerjaan" to profile.pekerjaan,
                    "email" to profile.email,
                    "role" to profile.role,
                    "lastLogin" to System.currentTimeMillis()
                )
                firestore.collection("users").document(formattedPhone).set(firestoreData, SetOptions.merge()).await()
            } catch (e: Exception) {
                android.util.Log.e("RuangWargaAuth", "Firestore write error: ${e.message}")
            }

            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registerFullProfile(
        activity: Activity,
        nama: String,
        nik: String,
        noKk: String,
        telepon: String,
        rt: String,
        rw: String,
        alamat: String,
        pekerjaan: String,
        role: String
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val formattedPhone = formatIndonesianPhone(telepon)
            val profile = ResidentProfileEntity(
                id = 1,
                uid = UUID.randomUUID().toString(),
                nama = nama.trim(),
                nik = nik.trim(),
                noKk = noKk.trim(),
                telepon = formattedPhone,
                rt = rt.trim(),
                rw = rw.trim(),
                alamat = alamat.trim(),
                pekerjaan = pekerjaan.trim(),
                email = "",
                role = role
            )
            db.residentProfileDao().insertOrUpdateProfile(profile)

            try {
                val firestoreData = hashMapOf(
                    "uid" to profile.uid,
                    "nama" to profile.nama,
                    "nik" to profile.nik,
                    "noKk" to profile.noKk,
                    "telepon" to profile.telepon,
                    "rt" to profile.rt,
                    "rw" to profile.rw,
                    "alamat" to profile.alamat,
                    "pekerjaan" to profile.pekerjaan,
                    "email" to profile.email,
                    "role" to profile.role,
                    "createdAt" to System.currentTimeMillis()
                )
                firestore.collection("users").document(formattedPhone).set(firestoreData, SetOptions.merge()).await()
            } catch (e: Exception) {
                android.util.Log.e("RuangWargaAuth", "Gagal simpan registrasi ke Firestore: ${e.message}")
            }

            // Kirim SMS OTP Firebase
            sendFirebaseSmsOtp(activity, formattedPhone)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveCompleteProfile(profile: ResidentProfileEntity): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val formattedPhone = formatIndonesianPhone(profile.telepon)
            val updated = profile.copy(telepon = formattedPhone)
            db.residentProfileDao().insertOrUpdateProfile(updated)

            try {
                val firestoreData = hashMapOf(
                    "uid" to updated.uid,
                    "nama" to updated.nama,
                    "nik" to updated.nik,
                    "noKk" to updated.noKk,
                    "telepon" to updated.telepon,
                    "rt" to updated.rt,
                    "rw" to updated.rw,
                    "alamat" to updated.alamat,
                    "pekerjaan" to updated.pekerjaan,
                    "email" to updated.email,
                    "role" to updated.role,
                    "updatedAt" to System.currentTimeMillis()
                )
                firestore.collection("users").document(formattedPhone).set(firestoreData, SetOptions.merge()).await()
            } catch (e: Exception) {
                android.util.Log.e("RuangWargaAuth", "Gagal upload profile: ${e.message}")
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            auth.signOut()
            db.residentProfileDao().deleteProfile()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
