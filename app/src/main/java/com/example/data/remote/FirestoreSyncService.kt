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
                "deskripsi" to alert.deskripsi,
                "status" to alert.status,
                "waktu" to alert.waktu,
                "tanggal" to alert.tanggal,
                "pelapor" to alert.pelapor,
                "jumlahRelawan" to alert.jumlahRelawan,
                "relawanList" to alert.relawanList,
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
