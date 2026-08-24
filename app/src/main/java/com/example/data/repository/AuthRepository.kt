package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.model.ResidentProfileEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthRepository(private val db: AppDatabase) {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    suspend fun register(
        email: String,
        password: String,
        nama: String,
        nik: String,
        noKk: String,
        telepon: String,
        rt: String,
        rw: String,
        alamat: String,
        pekerjaan: String,
        role: String
    ): Result<ResidentProfileEntity> = withContext(Dispatchers.IO) {
        try {
            val authResult = auth.createUserWithEmailAndPassword(email.trim(), password).await()
            val user = authResult.user ?: throw Exception("Pendaftaran gagal: User tidak terbentuk")
            val uid = user.uid

            val profile = ResidentProfileEntity(
                id = 1,
                uid = uid,
                nama = nama.trim(),
                nik = nik.trim(),
                noKk = noKk.trim(),
                telepon = telepon.trim(),
                rt = rt.trim(),
                rw = rw.trim(),
                alamat = alamat.trim(),
                pekerjaan = pekerjaan.trim(),
                email = email.trim(),
                role = role
            )

            // Simpan ke Room Lokal
            db.residentProfileDao().insertOrUpdateProfile(profile)

            // Simpan ke Firebase Firestore
            val firestoreData = hashMapOf(
                "uid" to uid,
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
            firestore.collection("users").document(uid).set(firestoreData).await()

            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<ResidentProfileEntity> = withContext(Dispatchers.IO) {
        try {
            val authResult = auth.signInWithEmailAndPassword(email.trim(), password).await()
            val user = authResult.user ?: throw Exception("Login gagal: User tidak ditemukan")
            val uid = user.uid

            // Ambil dari Firestore
            val doc = firestore.collection("users").document(uid).get().await()
            val profile = if (doc.exists()) {
                ResidentProfileEntity(
                    id = 1,
                    uid = uid,
                    nama = doc.getString("nama") ?: user.displayName ?: "Warga",
                    nik = doc.getString("nik") ?: "",
                    noKk = doc.getString("noKk") ?: "",
                    telepon = doc.getString("telepon") ?: "",
                    rt = doc.getString("rt") ?: "RT 01",
                    rw = doc.getString("rw") ?: "RW 01",
                    alamat = doc.getString("alamat") ?: "",
                    pekerjaan = doc.getString("pekerjaan") ?: "",
                    email = doc.getString("email") ?: email,
                    role = doc.getString("role") ?: "Warga"
                )
            } else {
                ResidentProfileEntity(
                    id = 1,
                    uid = uid,
                    nama = user.displayName ?: "Warga",
                    email = email
                )
            }

            // Simpan ke Room DB
            db.residentProfileDao().insertOrUpdateProfile(profile)
            Result.success(profile)
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

    suspend fun reloadProfileFromCloud(): Result<ResidentProfileEntity?> = withContext(Dispatchers.IO) {
        try {
            val user = auth.currentUser ?: return@withContext Result.success(null)
            val doc = firestore.collection("users").document(user.uid).get().await()
            if (doc.exists()) {
                val profile = ResidentProfileEntity(
                    id = 1,
                    uid = user.uid,
                    nama = doc.getString("nama") ?: "",
                    nik = doc.getString("nik") ?: "",
                    noKk = doc.getString("noKk") ?: "",
                    telepon = doc.getString("telepon") ?: "",
                    rt = doc.getString("rt") ?: "RT 01",
                    rw = doc.getString("rw") ?: "RW 01",
                    alamat = doc.getString("alamat") ?: "",
                    pekerjaan = doc.getString("pekerjaan") ?: "",
                    email = doc.getString("email") ?: user.email ?: "",
                    role = doc.getString("role") ?: "Warga"
                )
                db.residentProfileDao().insertOrUpdateProfile(profile)
                Result.success(profile)
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
