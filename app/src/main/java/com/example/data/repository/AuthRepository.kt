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

            // Ambil profil dari Cloud Firestore dengan multi-format lookup
            var existingProfile: ResidentProfileEntity? = null
            try {
                // 1. Coba document ID formattedPhone (+628xxx)
                var doc = firestore.collection("users").document(formattedPhone).get().await()
                
                // 2. Coba document ID raw phone jika belum ketemu
                if (!doc.exists()) {
                    val rawDigits = phone.replace(Regex("[^0-9]"), "").trim()
                    doc = firestore.collection("users").document(rawDigits).get().await()
                }

                // 3. Coba query field telepon jika document ID berbeda
                if (!doc.exists()) {
                    val querySnap = firestore.collection("users")
                        .whereEqualTo("telepon", formattedPhone)
                        .limit(1)
                        .get()
                        .await()
                    if (!querySnap.isEmpty) {
                        doc = querySnap.documents[0]
                    }
                }

                // 4. Coba query field telepon dengan format 08xx
                if (!doc.exists()) {
                    val local08Phone = if (formattedPhone.startsWith("+62")) "0" + formattedPhone.substring(3) else formattedPhone
                    val querySnap2 = firestore.collection("users")
                        .whereEqualTo("telepon", local08Phone)
                        .limit(1)
                        .get()
                        .await()
                    if (!querySnap2.isEmpty) {
                        doc = querySnap2.documents[0]
                    }
                }

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

            // Update lastLogin dan metadata ke Cloud Firestore tanpa menimpa data yang sudah ada
            try {
                val loginMetadata = hashMapOf<String, Any>(
                    "uid" to profile.uid,
                    "telepon" to formattedPhone,
                    "lastLogin" to System.currentTimeMillis()
                )
                if (profile.nama.isNotBlank()) loginMetadata["nama"] = profile.nama
                if (profile.nik.isNotBlank()) loginMetadata["nik"] = profile.nik
                if (profile.noKk.isNotBlank()) loginMetadata["noKk"] = profile.noKk
                if (profile.alamat.isNotBlank()) loginMetadata["alamat"] = profile.alamat
                if (profile.pekerjaan.isNotBlank()) loginMetadata["pekerjaan"] = profile.pekerjaan
                if (profile.email.isNotBlank()) loginMetadata["email"] = profile.email
                if (profile.role.isNotBlank()) loginMetadata["role"] = profile.role
                if (profile.rt.isNotBlank()) loginMetadata["rt"] = profile.rt
                if (profile.rw.isNotBlank()) loginMetadata["rw"] = profile.rw

                firestore.collection("users").document(formattedPhone).set(loginMetadata, SetOptions.merge()).await()
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
            val rawDigits = profile.telepon.replace(Regex("[^0-9]"), "").trim()
            val local08Phone = if (formattedPhone.startsWith("+62")) "0" + formattedPhone.substring(3) else formattedPhone

            val updated = profile.copy(telepon = formattedPhone)
            db.residentProfileDao().insertOrUpdateProfile(updated)

            val firestoreData = hashMapOf(
                "uid" to updated.uid,
                "nama" to updated.nama.trim(),
                "nik" to updated.nik.trim(),
                "noKk" to updated.noKk.trim(),
                "telepon" to formattedPhone,
                "teleponAlt" to local08Phone,
                "rt" to updated.rt.trim(),
                "rw" to updated.rw.trim(),
                "alamat" to updated.alamat.trim(),
                "pekerjaan" to updated.pekerjaan.trim(),
                "email" to updated.email.trim(),
                "role" to updated.role.trim(),
                "updatedAt" to System.currentTimeMillis()
            )

            // Simpan ke key +628xxx (utama) dan 08xxx (alternatif) agar pencarian di Firestore selalu cocok
            try {
                firestore.collection("users").document(formattedPhone).set(firestoreData, SetOptions.merge()).await()
                if (local08Phone != formattedPhone) {
                    firestore.collection("users").document(local08Phone).set(firestoreData, SetOptions.merge()).await()
                }
                android.util.Log.d("RuangWargaAuth", "Data profil lengkap berhasil terunggah ke Cloud Firestore untuk $formattedPhone")
            } catch (e: Exception) {
                android.util.Log.e("RuangWargaAuth", "Gagal upload profile ke Firestore: ${e.message}", e)
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
