package com.example.data.remote

import android.util.Log
import com.example.data.model.AnnouncementRecordEntity
import com.example.data.model.CashTransactionEntity
import com.example.data.model.ComplaintRecordEntity
import com.example.data.model.EmergencyAlertEntity
import com.example.data.model.LetterRequestEntity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class FirestoreSyncService(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    companion object {
        private const val TAG = "FirestoreSyncService"
        private const val COL_LETTERS = "letters"
        private const val COL_COMPLAINTS = "complaints"
        private const val COL_CASH_TRANSACTIONS = "cash_transactions"
        private const val COL_EMERGENCY_ALERTS = "emergency_alerts"
        private const val COL_ANNOUNCEMENTS = "announcements"
        private const val COL_USERS = "users"
        private const val COL_COMMUNITY_POSTS = "community_posts"
    }

    // 0. Sinkronisasi Data Profil Warga
    suspend fun syncProfile(profile: com.example.data.model.ResidentProfileEntity): Boolean {
        return try {
            val docId = profile.telepon.ifBlank { profile.uid.ifBlank { "user_1" } }
            val data = hashMapOf(
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
            firestore.collection(COL_USERS).document(docId).set(data, SetOptions.merge()).await()
            Log.d(TAG, "Profile synced successfully: $docId")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing profile: ${e.message}", e)
            false
        }
    }

    // 0.1 Sinkronisasi Postingan Aktivitas / Feed Warga ke Cloud Firestore
    suspend fun syncCommunityPost(post: com.example.ui.viewmodel.CommunityFeedPost): Boolean {
        return try {
            val docId = post.id.ifBlank { "post_${System.currentTimeMillis()}" }
            val data = hashMapOf(
                "id" to docId,
                "authorName" to post.authorName,
                "authorRole" to post.authorRole,
                "authorRtRw" to post.authorRtRw,
                "timeAgo" to post.timeAgo,
                "category" to post.category,
                "title" to post.title,
                "content" to post.content,
                "eventDate" to post.eventDate,
                "eventTime" to post.eventTime,
                "eventLocation" to post.eventLocation,
                "participantsCount" to post.participantsCount,
                "isParticipating" to post.isParticipating,
                "canContribute" to post.canContribute,
                "likesCount" to post.likesCount,
                "isLiked" to post.isLiked,
                "commentsCount" to post.commentsCount,
                "bannerTemplateId" to post.bannerTemplateId,
                "createdAt" to System.currentTimeMillis()
            )
            firestore.collection(COL_COMMUNITY_POSTS).document(docId).set(data, SetOptions.merge()).await()
            Log.d(TAG, "Community post synced successfully to Firestore: $docId")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing community post to Firestore: ${e.message}")
            false
        }
    }

    // Listener Realtime Postingan Komunitas dari Cloud Firestore
    fun listenToCommunityPosts(onPostsUpdated: (List<com.example.ui.viewmodel.CommunityFeedPost>) -> Unit) {
        firestore.collection(COL_COMMUNITY_POSTS)
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Listen error for community posts: ${error.message}")
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val posts = snapshot.documents.mapNotNull { doc ->
                        try {
                            com.example.ui.viewmodel.CommunityFeedPost(
                                id = doc.getString("id") ?: doc.id,
                                authorName = doc.getString("authorName") ?: "Warga",
                                authorRole = doc.getString("authorRole") ?: "Warga RT 03",
                                authorRtRw = doc.getString("authorRtRw") ?: "RT 03 / RW 02",
                                timeAgo = doc.getString("timeAgo") ?: "Baru saja",
                                category = doc.getString("category") ?: "Kegiatan",
                                title = doc.getString("title") ?: "",
                                content = doc.getString("content") ?: "",
                                eventDate = doc.getString("eventDate"),
                                eventTime = doc.getString("eventTime"),
                                eventLocation = doc.getString("eventLocation"),
                                participantsCount = (doc.getLong("participantsCount") ?: 0L).toInt(),
                                isParticipating = doc.getBoolean("isParticipating") ?: false,
                                canContribute = doc.getBoolean("canContribute") ?: false,
                                likesCount = (doc.getLong("likesCount") ?: 0L).toInt(),
                                isLiked = doc.getBoolean("isLiked") ?: false,
                                commentsCount = (doc.getLong("commentsCount") ?: 0L).toInt(),
                                bannerTemplateId = doc.getString("bannerTemplateId")
                            )
                        } catch (e: Exception) {
                            null
                        }
                    }
                    onPostsUpdated(posts)
                }
            }
    }

    // 1. Sinkronisasi Surat Pengantar
    suspend fun syncLetter(letter: LetterRequestEntity): Boolean {
        return try {
            val docId = if (letter.id != 0) "letter_${letter.id}" else "letter_${System.currentTimeMillis()}"
            val data = hashMapOf(
                "id" to letter.id,
                "nomorSurat" to letter.nomorSurat,
                "jenisSurat" to letter.jenisSurat,
                "keperluan" to letter.keperluan,
                "keteranganTambahan" to letter.keteranganTambahan,
                "status" to letter.status,
                "tanggalPengajuan" to letter.tanggalPengajuan,
                "tanggalSelesai" to letter.tanggalSelesai,
                "catatanRt" to letter.catatanRt,
                "updatedAt" to System.currentTimeMillis()
            )
            firestore.collection(COL_LETTERS).document(docId).set(data, SetOptions.merge()).await()
            Log.d(TAG, "Letter synced: $docId")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing letter: ${e.message}")
            false
        }
    }

    // 2. Sinkronisasi Pengaduan Warga
    suspend fun syncComplaint(complaint: ComplaintRecordEntity): Boolean {
        return try {
            val docId = if (complaint.id != 0) "complaint_${complaint.id}" else "complaint_${System.currentTimeMillis()}"
            val data = hashMapOf(
                "id" to complaint.id,
                "judul" to complaint.judul,
                "lokasi" to complaint.lokasi,
                "kategori" to complaint.kategori,
                "deskripsi" to complaint.deskripsi,
                "status" to complaint.status,
                "tanggal" to complaint.tanggal,
                "waktu" to complaint.waktu,
                "tanggapanRt" to complaint.tanggapanRt,
                "fotoBukti" to complaint.fotoBukti,
                "updatedAt" to System.currentTimeMillis()
            )
            firestore.collection(COL_COMPLAINTS).document(docId).set(data, SetOptions.merge()).await()
            Log.d(TAG, "Complaint synced: $docId")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing complaint: ${e.message}")
            false
        }
    }

    // 3. Sinkronisasi Transaksi Kas RW
    suspend fun syncCashTransaction(transaction: CashTransactionEntity): Boolean {
        return try {
            val docId = if (transaction.id != 0) "cash_${transaction.id}" else "cash_${System.currentTimeMillis()}"
            val data = hashMapOf(
                "id" to transaction.id,
                "tipe" to transaction.tipe,
                "judul" to transaction.judul,
                "kategori" to transaction.kategori,
                "jumlah" to transaction.jumlah,
                "tanggal" to transaction.tanggal,
                "keterangan" to transaction.keterangan,
                "dicatatOleh" to transaction.dicatatOleh,
                "updatedAt" to System.currentTimeMillis()
            )
            firestore.collection(COL_CASH_TRANSACTIONS).document(docId).set(data, SetOptions.merge()).await()
            Log.d(TAG, "Cash transaction synced: $docId")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing cash transaction: ${e.message}")
            false
        }
    }

    // 4. Sinkronisasi Peringatan Darurat Siaga Warga
    suspend fun syncEmergencyAlert(alert: EmergencyAlertEntity): Boolean {
        return try {
            val docId = if (alert.id != 0) "alert_${alert.id}" else "alert_${System.currentTimeMillis()}"
            val data = hashMapOf(
                "id" to alert.id,
                "jenisDarurat" to alert.jenisDarurat,
                "judul" to alert.judul,
                "lokasi" to alert.lokasi,
                "kontak" to alert.kontak,
                "tingkatPrioritas" to alert.tingkatPrioritas,
                "status" to alert.status,
                "waktu" to alert.waktu,
                "pelapor" to alert.pelapor,
                "instruksi" to alert.instruksi,
                "catatan" to alert.catatan,
                "isVerified" to alert.isVerified,
                "updatedAt" to System.currentTimeMillis()
            )
            firestore.collection(COL_EMERGENCY_ALERTS).document(docId).set(data, SetOptions.merge()).await()
            Log.d(TAG, "Emergency alert synced: $docId")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing emergency alert: ${e.message}")
            false
        }
    }

    // 5. Sinkronisasi Pengumuman
    suspend fun syncAnnouncement(announcement: AnnouncementRecordEntity): Boolean {
        return try {
            val docId = if (announcement.id != 0) "announcement_${announcement.id}" else "announcement_${System.currentTimeMillis()}"
            val data = hashMapOf(
                "id" to announcement.id,
                "judul" to announcement.judul,
                "ringkasan" to announcement.ringkasan,
                "konten" to announcement.konten,
                "kategori" to announcement.kategori,
                "isPenting" to announcement.isPenting,
                "isBaru" to announcement.isBaru,
                "tanggalPosting" to announcement.tanggalPosting,
                "lingkup" to announcement.lingkup,
                "updatedAt" to System.currentTimeMillis()
            )
            firestore.collection(COL_ANNOUNCEMENTS).document(docId).set(data, SetOptions.merge()).await()
            Log.d(TAG, "Announcement synced: $docId")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing announcement: ${e.message}")
            false
        }
    }
}
