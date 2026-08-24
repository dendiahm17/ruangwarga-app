package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.model.ResidentProfileEntity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

class AuthRepository(private val db: AppDatabase) {

    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    
    private var activePhoneNumber: String? = null
    private var generatedOtp: String? = null

    suspend fun isUserLoggedIn(): Boolean = withContext(Dispatchers.IO) {
        val currentProfile = db.residentProfileDao().getCurrentProfileDirect()
        currentProfile != null && currentProfile.telepon.isNotBlank()
    }

    suspend fun requestOtp(phone: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val cleanPhone = phone.replace(Regex("[^0-9+]"), "").trim()
            if (cleanPhone.length < 9) {
                return@withContext Result.failure(Exception("Nomor HP minimal 9 digit angka."))
            }

            // Generate 6 Digit OTP
            val otp = (100000..999999).random().toString()
            generatedOtp = otp
            activePhoneNumber = cleanPhone

            // Simpan log request OTP ke Cloud Firestore
            val otpLog = hashMapOf(
                "phone" to cleanPhone,
                "otp" to otp,
                "createdAt" to System.currentTimeMillis()
            )
            try {
                firestore.collection("otp_requests").document(cleanPhone).set(otpLog).await()
            } catch (e: Exception) {
                android.util.Log.w("RuangWargaAuth", "Offline fallback saat kirim OTP: ${e.message}")
            }

            Result.success(otp)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun verifyOtp(phone: String, inputOtp: String): Result<ResidentProfileEntity> = withContext(Dispatchers.IO) {
        try {
            val cleanPhone = phone.replace(Regex("[^0-9+]"), "").trim()
            val cleanInputOtp = inputOtp.trim()

            // Validasi Kode OTP (Bisa kode yang di-generate atau kode instan demo '123456')
            if (generatedOtp != null && cleanInputOtp != generatedOtp && cleanInputOtp != "123456") {
                return@withContext Result.failure(Exception("Kode OTP salah. Silakan periksa kembali."))
            }

            // Cari apakah data profil nomor HP ini sudah pernah ada di Cloud Firestore
            var existingProfile: ResidentProfileEntity? = null
            try {
                val doc = firestore.collection("users").document(cleanPhone).get().await()
                if (doc.exists()) {
                    existingProfile = ResidentProfileEntity(
                        id = 1,
                        uid = doc.getString("uid") ?: UUID.randomUUID().toString(),
                        nama = doc.getString("nama") ?: "",
                        nik = doc.getString("nik") ?: "",
                        noKk = doc.getString("noKk") ?: "",
                        telepon = cleanPhone,
                        rt = doc.getString("rt") ?: "RT 01",
                        rw = doc.getString("rw") ?: "RW 01",
                        alamat = doc.getString("alamat") ?: "",
                        pekerjaan = doc.getString("pekerjaan") ?: "",
                        email = doc.getString("email") ?: "",
                        role = doc.getString("role") ?: "Warga"
                    )
                }
            } catch (e: Exception) {
                android.util.Log.w("RuangWargaAuth", "Offline Firestore read: ${e.message}")
            }

            // Jika belum ada, buat profil warga baru
            val profile = existingProfile ?: ResidentProfileEntity(
                id = 1,
                uid = UUID.randomUUID().toString(),
                nama = "",
                nik = "",
                noKk = "",
                telepon = cleanPhone,
                rt = "RT 01",
                rw = "RW 01",
                alamat = "",
                pekerjaan = "",
                email = "",
                role = "Warga"
            )

            // Simpan ke Room Database lokal
            db.residentProfileDao().insertOrUpdateProfile(profile)

            // Sinkronkan ke Cloud Firestore
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
                firestore.collection("users").document(cleanPhone).set(firestoreData, SetOptions.merge()).await()
            } catch (e: Exception) {
                android.util.Log.e("RuangWargaAuth", "Gagal sync Firestore saat verifyOtp: ${e.message}")
            }

            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registerFullProfile(
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
            val cleanPhone = telepon.replace(Regex("[^0-9+]"), "").trim()
            if (cleanPhone.length < 9) {
                return@withContext Result.failure(Exception("Nomor HP minimal 9 digit angka."))
            }

            // Simpan profil ke Room
            val profile = ResidentProfileEntity(
                id = 1,
                uid = UUID.randomUUID().toString(),
                nama = nama.trim(),
                nik = nik.trim(),
                noKk = noKk.trim(),
                telepon = cleanPhone,
                rt = rt.trim(),
                rw = rw.trim(),
                alamat = alamat.trim(),
                pekerjaan = pekerjaan.trim(),
                email = "",
                role = role
            )
            db.residentProfileDao().insertOrUpdateProfile(profile)

            // Simpan ke Firestore
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
                firestore.collection("users").document(cleanPhone).set(firestoreData, SetOptions.merge()).await()
            } catch (e: Exception) {
                android.util.Log.e("RuangWargaAuth", "Gagal simpan Firestore pendaftaran: ${e.message}")
            }

            // Buat OTP otomatis untuk verifikasi nomor
            requestOtp(cleanPhone)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveCompleteProfile(profile: ResidentProfileEntity): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val cleanPhone = profile.telepon.replace(Regex("[^0-9+]"), "").trim()
            db.residentProfileDao().insertOrUpdateProfile(profile.copy(telepon = cleanPhone))
            
            try {
                val docId = if (cleanPhone.isNotBlank()) cleanPhone else profile.uid
                val firestoreData = hashMapOf(
                    "uid" to profile.uid,
                    "nama" to profile.nama,
                    "nik" to profile.nik,
                    "noKk" to profile.noKk,
                    "telepon" to cleanPhone,
                    "rt" to profile.rt,
                    "rw" to profile.rw,
                    "alamat" to profile.alamat,
                    "pekerjaan" to profile.pekerjaan,
                    "email" to profile.email,
                    "role" to profile.role,
                    "updatedAt" to System.currentTimeMillis()
                )
                firestore.collection("users").document(docId).set(firestoreData, SetOptions.merge()).await()
            } catch (e: Exception) {
                android.util.Log.e("RuangWargaAuth", "Gagal upload profile ke Firestore: ${e.message}")
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            generatedOtp = null
            activePhoneNumber = null
            db.residentProfileDao().deleteProfile()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
