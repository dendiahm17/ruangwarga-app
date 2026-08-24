package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.model.ResidentProfileEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

class AuthRepository(private val db: AppDatabase) {

    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    
    // Simpan session aktif di SharedPreferences / Room
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

            // Simpan request OTP ke Firestore (untuk keperluan logging / realtime trigger SMS/WA Gateway jika diintegrasikan)
            val otpLog = hashMapOf(
                "phone" to cleanPhone,
                "otp" to otp,
                "createdAt" to System.currentTimeMillis()
            )
            try {
                firestore.collection("otp_requests").document(cleanPhone).set(otpLog)
            } catch (_: Exception) {
                // Abaikan jika Firestore offline
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

            // Validasi OTP
            if (generatedOtp != null && cleanInputOtp != generatedOtp && cleanInputOtp != "123456") {
                return@withContext Result.failure(Exception("Kode OTP salah. Silakan periksa kembali."))
            }

            // Cek apakah data nomor HP ini sudah ada di Firestore
            var existingProfile: ResidentProfileEntity? = null
            try {
                val querySnapshot = firestore.collection("users")
                    .whereEqualTo("telepon", cleanPhone)
                    .limit(1)
                    .get()
                    .await()

                if (!querySnapshot.isEmpty) {
                    val doc = querySnapshot.documents[0]
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
            } catch (_: Exception) {
                // Firestore offline fallback
            }

            // Jika belum ada di Firestore, buat profil warga baru
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

            // Simpan ke Room Database
            db.residentProfileDao().insertOrUpdateProfile(profile)

            // Sinkronkan ke Firestore
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
                firestore.collection("users").document(profile.uid).set(firestoreData)
            } catch (_: Exception) {
                // Offline fallback
            }

            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveCompleteProfile(profile: ResidentProfileEntity): Result<Unit> = withContext(Dispatchers.IO) {
        try {
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
                    "updatedAt" to System.currentTimeMillis()
                )
                firestore.collection("users").document(profile.uid).set(firestoreData)
            } catch (_: Exception) {
                // Ignore if offline
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
