package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.model.AnnouncementRecordEntity
import com.example.data.model.AssetRwEntity
import com.example.data.model.CashTransactionEntity
import com.example.data.model.CommunityEventEntity
import com.example.data.model.ComplaintRecordEntity
import com.example.data.model.DuesRecordEntity
import com.example.data.model.EmergencyAlertEntity
import com.example.data.model.FamilyMemberEntity
import com.example.data.model.IncidentRecordEntity
import com.example.data.model.LetterRequestEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.OfficerMemberEntity
import com.example.data.model.PollingEntity
import com.example.data.model.ResidentDirectoryEntity
import com.example.data.model.ResidentProfileEntity
import com.example.data.model.RondaScheduleEntity
import com.example.data.model.SocialHelpEntity
import com.example.data.model.UserParticipationEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class RtrwRepository(private val db: AppDatabase) {

    val profile: Flow<ResidentProfileEntity?> = db.residentProfileDao().getProfile()
    val familyMembers: Flow<List<FamilyMemberEntity>> = db.familyMemberDao().getFamilyMembers()
    val letters: Flow<List<LetterRequestEntity>> = db.letterRequestDao().getAllLetters()
    val dues: Flow<List<DuesRecordEntity>> = db.duesRecordDao().getAllDues()
    val complaints: Flow<List<ComplaintRecordEntity>> = db.complaintRecordDao().getAllComplaints()
    val announcements: Flow<List<AnnouncementRecordEntity>> = db.announcementRecordDao().getAllAnnouncements()
    val notifications: Flow<List<NotificationEntity>> = db.notificationDao().getAllNotifications()
    val unreadNotificationsCount: Flow<Int> = db.notificationDao().getUnreadCount()
    val rondaSchedules: Flow<List<RondaScheduleEntity>> = db.rondaScheduleDao().getAllSchedules()
    val emergencyAlerts: Flow<List<EmergencyAlertEntity>> = db.emergencyAlertDao().getAllAlerts()
    val cashTransactions: Flow<List<CashTransactionEntity>> = db.cashTransactionDao().getAllTransactions()
    val communityEvents: Flow<List<CommunityEventEntity>> = db.communityEventDao().getAllEvents()
    val polls: Flow<List<PollingEntity>> = db.pollingDao().getAllPolls()
    val socialHelp: Flow<List<SocialHelpEntity>> = db.socialHelpDao().getAllSocialHelp()
    val incidents: Flow<List<IncidentRecordEntity>> = db.incidentRecordDao().getAllIncidents()
    val participations: Flow<List<UserParticipationEntity>> = db.userParticipationDao().getAllParticipations()
    val officers: Flow<List<OfficerMemberEntity>> = db.officerDao().getAllOfficers()
    val residents: Flow<List<ResidentDirectoryEntity>> = db.residentDirectoryDao().getAllResidents()
    val assets: Flow<List<AssetRwEntity>> = db.assetRwDao().getAllAssets()

    suspend fun seedInitialDataIfEmpty() {
        // Data dummy dibersihkan. Aplikasi siap digunakan untuk data real.
    }

    suspend fun clearAllData() {
        db.clearAllTables()
    }

    suspend fun borrowAsset(id: Int) {
        db.assetRwDao().updateAssetAvailability(id, "Dipinjam")
    }

    suspend fun returnAsset(id: Int) {
        db.assetRwDao().updateAssetAvailability(id, "Tersedia")
    }

    suspend fun addAsset(
        kode: String,
        nama: String,
        kategori: String,
        jumlah: Int,
        satuan: String,
        kondisi: String,
        lokasi: String,
        pj: String,
        tahun: String,
        nilai: Long,
        catatan: String
    ) {
        val asset = AssetRwEntity(
            kodeAset = kode,
            namaAset = nama,
            kategori = kategori,
            jumlah = jumlah,
            satuan = satuan,
            kondisi = kondisi,
            lokasiPenyimpanan = lokasi,
            penanggungJawab = pj,
            statusKetersediaan = "Tersedia",
            tahunPengadaan = tahun,
            nilaiPerolehan = nilai,
            catatan = catatan,
            bisaDipinjam = true
        )
        db.assetRwDao().insertAsset(asset)
    }

    suspend fun deleteAsset(id: Int) {
        db.assetRwDao().deleteAsset(id)
    }

    suspend fun submitLetter(jenisSurat: String, keperluan: String, keteranganTambahan: String): Long {
        val count = db.letterRequestDao().getAllLetters().firstOrNull()?.size ?: 0
        val entity = LetterRequestEntity(
            nomorSurat = String.format("%03d/REQ/RT03/V/2026", count + 43),
            jenisSurat = jenisSurat,
            keperluan = keperluan,
            keteranganTambahan = keteranganTambahan,
            status = "Pengajuan",
            tanggalPengajuan = "23 Mei 2026",
            catatanRt = "Pengajuan baru diterima sistem, menunggu verifikasi pengurus RT."
        )
        val id = db.letterRequestDao().insertLetter(entity)
        db.notificationDao().insertNotification(
            NotificationEntity(
                judul = "Pengajuan $jenisSurat Terkirim",
                pesan = "Permohonan surat sedang menunggu verifikasi oleh Ketua RT.",
                tanggal = "23 Mei 2026 • Baru saja",
                isDibaca = false,
                tipe = "surat"
            )
        )
        return id
    }

    suspend fun submitComplaint(judul: String, lokasi: String, kategori: String, deskripsi: String, fotoBukti: String? = null): Long {
        val entity = ComplaintRecordEntity(
            judul = judul,
            lokasi = lokasi,
            kategori = kategori,
            deskripsi = deskripsi,
            status = "Pengajuan",
            tanggal = "23 Mei 2026",
            waktu = "11:20 WIB",
            tanggapanRt = "Laporan diterima. Akan segera ditinjau oleh pengurus RT.",
            fotoBukti = fotoBukti
        )
        val id = db.complaintRecordDao().insertComplaint(entity)
        db.notificationDao().insertNotification(
            NotificationEntity(
                judul = "Pengaduan Baru Terkirim",
                pesan = "Laporan '$judul' berhasil dikirim ke pengurus RT.",
                tanggal = "23 Mei 2026 • Baru saja",
                isDibaca = false,
                tipe = "pengaduan"
            )
        )
        return id
    }

    suspend fun payDues(periodeBulan: String, metode: String, buktiBayar: String? = null) {
        val existing = db.duesRecordDao().getAllDues().firstOrNull()?.find { it.periodeBulan == periodeBulan }
        val kodeTrx = "RTRW-202605-${(1000..9999).random()}"
        if (existing != null) {
            db.duesRecordDao().updateDues(
                existing.copy(
                    status = "Lunas",
                    tanggalBayar = "23 Mei 2026",
                    metodePembayaran = metode,
                    kodeTransaksi = kodeTrx,
                    buktiBayar = buktiBayar ?: "$metode-Bukti-$kodeTrx.png"
                )
            )
        } else {
            db.duesRecordDao().insertDues(
                DuesRecordEntity(
                    periodeBulan = periodeBulan,
                    jumlah = 20000,
                    status = "Lunas",
                    tanggalBayar = "23 Mei 2026",
                    metodePembayaran = metode,
                    kodeTransaksi = kodeTrx,
                    buktiBayar = buktiBayar ?: "$metode-Bukti-$kodeTrx.png"
                )
            )
        }
        db.notificationDao().insertNotification(
            NotificationEntity(
                judul = "Iuran $periodeBulan Berhasil Dibayar",
                pesan = "Pembayaran melalui $metode telah berhasil dicatat.",
                tanggal = "23 Mei 2026 • Baru saja",
                isDibaca = false,
                tipe = "iuran"
            )
        )
    }

    suspend fun updateProfile(profile: ResidentProfileEntity) {
        db.residentProfileDao().insertOrUpdateProfile(profile)
    }

    suspend fun addFamilyMember(member: FamilyMemberEntity) {
        db.familyMemberDao().insertMember(member)
    }

    suspend fun deleteFamilyMember(memberId: Int) {
        db.familyMemberDao().deleteMember(memberId)
    }

    suspend fun clearAllNotifications() {
        db.notificationDao().deleteAllNotifications()
    }

    suspend fun markAllNotificationsAsRead() {
        db.notificationDao().markAllAsRead()
    }

    suspend fun markNotificationRead(notificationId: Int) {
        db.notificationDao().markAsRead(notificationId)
    }

    suspend fun updateRondaAttendance(scheduleId: Int, status: String) {
        db.rondaScheduleDao().updateAttendance(scheduleId, status)
        db.notificationDao().insertNotification(
            NotificationEntity(
                judul = "Konfirmasi Ronda Diperbarui",
                pesan = "Status kehadiran ronda Anda telah diubah menjadi '$status'.",
                tanggal = "23 Mei 2026 • Baru saja",
                isDibaca = false,
                tipe = "siskamling"
            )
        )
    }

    suspend fun adminUpdateLetterStatus(letterId: Int, newStatus: String, catatanRt: String) {
        val tglSelesai = if (newStatus == "Selesai") "23 Mei 2026" else null
        db.letterRequestDao().updateLetterStatus(letterId, newStatus, catatanRt, tglSelesai)
        db.notificationDao().insertNotification(
            NotificationEntity(
                judul = "Status Surat Diperbarui (Pengurus RT)",
                pesan = "Surat diubah statusnya menjadi '$newStatus': $catatanRt",
                tanggal = "23 Mei 2026 • Baru saja",
                isDibaca = false,
                tipe = "surat"
            )
        )
    }

    suspend fun adminUpdateComplaintStatus(complaintId: Int, newStatus: String, tanggapanRt: String) {
        db.complaintRecordDao().updateComplaintStatus(complaintId, newStatus, tanggapanRt)
        db.notificationDao().insertNotification(
            NotificationEntity(
                judul = "Tanggapan Pengaduan Baru",
                pesan = "Pengurus RT memperbarui pengaduan menjadi '$newStatus': $tanggapanRt",
                tanggal = "23 Mei 2026 • Baru saja",
                isDibaca = false,
                tipe = "pengaduan"
            )
        )
    }

    suspend fun adminUpdateDuesStatus(duesId: Int, newStatus: String) {
        val tgl = if (newStatus == "Lunas") "23 Mei 2026" else null
        db.duesRecordDao().updateDuesStatus(duesId, newStatus, tgl)
    }

    suspend fun triggerPanicEmergency(jenisDarurat: String, lokasi: String, catatan: String): Long {
        val alert = EmergencyAlertEntity(
            jenisDarurat = jenisDarurat,
            lokasi = lokasi,
            waktu = "23 Mei 2026 • Baru saja",
            catatan = catatan
        )
        val id = db.emergencyAlertDao().insertAlert(alert)
        db.notificationDao().insertNotification(
            NotificationEntity(
                judul = "🚨 SINYAL DARURAT: $jenisDarurat!",
                pesan = "Peringatan darurat $jenisDarurat di $lokasi. Tim Pos Kamling dan Satpam menuju lokasi!",
                tanggal = "23 Mei 2026 • Baru saja",
                isDibaca = false,
                tipe = "siskamling"
            )
        )
        return id
    }

    suspend fun resolveEmergencyAlert(id: Int) {
        db.emergencyAlertDao().updateStatus(id, "Ditangani")
    }

    suspend fun addCashTransaction(
        tipe: String,
        judul: String,
        kategori: String,
        jumlah: Long,
        tanggal: String,
        keterangan: String
    ): Long {
        val id = db.cashTransactionDao().insertTransaction(
            CashTransactionEntity(
                tipe = tipe,
                judul = judul,
                kategori = kategori,
                jumlah = jumlah,
                tanggal = tanggal,
                keterangan = keterangan,
                dicatatOleh = "Bendahara RT 03"
            )
        )
        db.notificationDao().insertNotification(
            NotificationEntity(
                judul = "Buku Kas RT Diperbarui: $tipe",
                pesan = "$judul senilai Rp ${String.format("%,d", jumlah).replace(',', '.')} berhasil dicatat di buku kas.",
                tanggal = "23 Mei 2026 • Baru saja",
                isDibaca = false,
                tipe = "iuran"
            )
        )
        return id
    }

    suspend fun deleteCashTransaction(id: Int) {
        db.cashTransactionDao().deleteTransaction(id)
    }

    suspend fun updateEventRsvp(eventId: Int, newStatus: String) {
        val event = db.communityEventDao().getAllEvents().firstOrNull()?.find { it.id == eventId }
        if (event != null) {
            val oldStatus = event.rsvpStatus
            var currentCount = event.jumlahHadir
            if (newStatus == "Hadir" && oldStatus != "Hadir") {
                currentCount += 1
            } else if (newStatus != "Hadir" && oldStatus == "Hadir") {
                currentCount = (currentCount - 1).coerceAtLeast(0)
            }
            db.communityEventDao().updateRsvp(eventId, newStatus, currentCount)
            db.notificationDao().insertNotification(
                NotificationEntity(
                    judul = "Konfirmasi Kehadiran Acara",
                    pesan = "RSVP untuk '${event.judul}' dicatat sebagai '$newStatus'.",
                    tanggal = "23 Mei 2026 • Baru saja",
                    isDibaca = false,
                    tipe = "pengumuman"
                )
            )
        }
    }

    suspend fun addCommunityEvent(
        judul: String,
        kategori: String,
        tanggal: String,
        waktu: String,
        lokasi: String,
        deskripsi: String,
        penanggungJawab: String
    ): Long {
        val id = db.communityEventDao().insertEvent(
            CommunityEventEntity(
                judul = judul,
                kategori = kategori,
                tanggal = tanggal,
                waktu = waktu,
                lokasi = lokasi,
                deskripsi = deskripsi,
                penanggungJawab = penanggungJawab
            )
        )
        db.notificationDao().insertNotification(
            NotificationEntity(
                judul = "Agenda Kegiatan Baru: $judul",
                pesan = "Kegiatan $kategori pada $tanggal di $lokasi telah ditambahkan.",
                tanggal = "23 Mei 2026 • Baru saja",
                isDibaca = false,
                tipe = "pengumuman"
            )
        )
        return id
    }

    suspend fun votePoll(pollId: Int, option: String) {
        val poll = db.pollingDao().getAllPolls().firstOrNull()?.find { it.id == pollId }
        if (poll != null) {
            var a = poll.suaraA
            var b = poll.suaraB
            var c = poll.suaraC

            // Remove previous vote if any
            when (poll.myVote) {
                "A" -> a = (a - 1).coerceAtLeast(0)
                "B" -> b = (b - 1).coerceAtLeast(0)
                "C" -> c = (c - 1).coerceAtLeast(0)
            }

            // Apply new vote
            when (option) {
                "A" -> a += 1
                "B" -> b += 1
                "C" -> c += 1
            }

            db.pollingDao().recordVote(pollId, a, b, c, option)
            db.notificationDao().insertNotification(
                NotificationEntity(
                    judul = "Suara Musyawarah Tercatat",
                    pesan = "Pilihan suara Anda untuk musyawarah '${poll.judul}' berhasil disimpan.",
                    tanggal = "23 Mei 2026 • Baru saja",
                    isDibaca = false,
                    tipe = "pengumuman"
                )
            )
        }
    }

    suspend fun createPoll(
        judul: String,
        deskripsi: String,
        kategori: String,
        batasWaktu: String,
        opsiA: String,
        opsiB: String,
        opsiC: String? = null
    ): Long {
        val id = db.pollingDao().insertPoll(
            PollingEntity(
                judul = judul,
                deskripsi = deskripsi,
                kategori = kategori,
                batasWaktu = batasWaktu,
                opsiA = opsiA,
                opsiB = opsiB,
                opsiC = opsiC,
                suaraA = 0,
                suaraB = 0,
                suaraC = 0,
                status = "Aktif"
            )
        )
        db.notificationDao().insertNotification(
            NotificationEntity(
                judul = "Musyawarah & Polling Baru: $judul",
                pesan = "Partisipasi suara Anda ditunggu untuk musyawarah RT hingga $batasWaktu.",
                tanggal = "23 Mei 2026 • Baru saja",
                isDibaca = false,
                tipe = "pengumuman"
            )
        )
        return id
    }

    suspend fun closePoll(pollId: Int) {
        db.pollingDao().updatePollStatus(pollId, "Selesai")
    }

    suspend fun participateInEvent(eventId: Int, status: String) {
        val event = db.communityEventDao().getAllEvents().firstOrNull()?.find { it.id == eventId }
        if (event != null) {
            val newCount = if (status == "Saya Ikut" && event.partisipasiStatus != "Saya Ikut") {
                event.jumlahHadir + 1
            } else if (status != "Saya Ikut" && event.partisipasiStatus == "Saya Ikut") {
                (event.jumlahHadir - 1).coerceAtLeast(0)
            } else {
                event.jumlahHadir
            }
            db.communityEventDao().updatePartisipasi(eventId, status, newCount)
            db.communityEventDao().updateRsvp(eventId, if (status == "Saya Ikut") "Hadir" else status, newCount)

            if (status == "Saya Ikut") {
                db.userParticipationDao().insertParticipation(
                    UserParticipationEntity(
                        judulKegiatan = event.judul,
                        tanggal = event.tanggal,
                        kategori = event.kategori,
                        status = "Diikuti",
                        peran = "Peserta Aktif"
                    )
                )
            }
        }
    }

    suspend fun contributeEventLogistic(eventId: Int, itemType: String, amount: Int, contributionNote: String) {
        val event = db.communityEventDao().getAllEvents().firstOrNull()?.find { it.id == eventId }
        if (event != null) {
            val newRelawan = if (itemType == "Relawan") (event.terpenuhiRelawan + amount).coerceAtMost(event.kebutuhanRelawan.coerceAtLeast(event.terpenuhiRelawan + amount)) else event.terpenuhiRelawan
            val newSapu = if (itemType == "Sapu") (event.terpenuhiSapu + amount).coerceAtMost(event.kebutuhanSapu.coerceAtLeast(event.terpenuhiSapu + amount)) else event.terpenuhiSapu
            val newEmber = if (itemType == "Ember") (event.terpenuhiEmber + amount).coerceAtMost(event.kebutuhanEmber.coerceAtLeast(event.terpenuhiEmber + amount)) else event.terpenuhiEmber
            val newPickup = if (itemType == "Pickup") (event.terpenuhiPickup + amount).coerceAtMost(event.kebutuhanPickup.coerceAtLeast(event.terpenuhiPickup + amount)) else event.terpenuhiPickup

            db.communityEventDao().updateLogistics(eventId, newRelawan, newSapu, newEmber, newPickup, "Saya Bisa Membantu")
            
            val newCount = if (event.partisipasiStatus != "Saya Ikut" && event.partisipasiStatus != "Saya Bisa Membantu") event.jumlahHadir + 1 else event.jumlahHadir
            db.communityEventDao().updatePartisipasi(eventId, "Saya Bisa Membantu", newCount)

            db.userParticipationDao().insertParticipation(
                UserParticipationEntity(
                    judulKegiatan = event.judul,
                    tanggal = event.tanggal,
                    kategori = event.kategori,
                    status = "Kontributor",
                    peran = "Bantuan $itemType ($contributionNote)"
                )
            )

            db.notificationDao().insertNotification(
                NotificationEntity(
                    judul = "Kontribusi Bantuan Tercatat",
                    pesan = "Terima kasih atas kesediaan membantu $itemType untuk kegiatan '${event.judul}'.",
                    tanggal = "${event.tanggal} • Baru saja",
                    waktuLalu = "Baru saja",
                    isDibaca = false,
                    tipe = "kegiatan",
                    kategori = "Activity",
                    actionLabel = "Lihat Agenda"
                )
            )
        }
    }

    suspend fun contributeToSocialHelp(helpId: Int, note: String) {
        val item = db.socialHelpDao().getAllSocialHelp().firstOrNull()?.find { it.id == helpId }
        if (item != null) {
            val newCount = item.jumlahRelawan + 1
            db.socialHelpDao().updateContribution(helpId, "Terbantu", newCount, true)
            db.notificationDao().insertNotification(
                NotificationEntity(
                    judul = "Terima Kasih Atas Kepedulian Anda",
                    pesan = "Bantuan Anda untuk '${item.judul}' telah tercatat dan dikoordinasikan oleh pengurus RT.",
                    tanggal = "21 Mei 2026 • Baru saja",
                    waktuLalu = "Baru saja",
                    isDibaca = false,
                    tipe = "kegiatan",
                    kategori = "Activity"
                )
            )
            db.userParticipationDao().insertParticipation(
                UserParticipationEntity(
                    judulKegiatan = item.judul,
                    tanggal = item.tanggal,
                    kategori = "Bantuan Sosial",
                    status = "Relawan",
                    peran = "Relawan Peduli Warga"
                )
            )
        }
    }

    suspend fun submitIncidentReport(
        judul: String,
        lokasi: String,
        kategori: String,
        deskripsi: String,
        fotoType: String = "lampu"
    ): Long {
        val id = db.incidentRecordDao().insertIncident(
            IncidentRecordEntity(
                judul = judul,
                lokasi = lokasi,
                kategori = kategori,
                status = "Dilaporkan",
                waktuLapor = "21 Mei 16:32",
                waktuVerifikasi = null,
                waktuPerbaikan = null,
                waktuSelesai = null,
                deskripsi = deskripsi,
                catatanPengurus = "Laporan baru masuk ke antrean pengurus RT/RW.",
                fotoType = fotoType
            )
        )
        db.notificationDao().insertNotification(
            NotificationEntity(
                judul = "Laporan Kejadian Terkirim",
                pesan = "Laporan '$judul' di $lokasi telah diterima sistem dan segera diverifikasi pengurus.",
                tanggal = "21 Mei 2026 • Baru saja",
                waktuLalu = "Baru saja",
                isDibaca = false,
                tipe = "kejadian",
                kategori = "Important",
                actionLabel = "Lihat Kejadian"
            )
        )
        return id
    }

    suspend fun updateIncidentStatus(incidentId: Int, status: String, catatan: String) {
        val incident = db.incidentRecordDao().getAllIncidents().firstOrNull()?.find { it.id == incidentId }
        if (incident != null) {
            val perbaikanTime = if (status == "Dalam Perbaikan" || status == "Diproses" || status == "Selesai") "21 Mei 17:20" else incident.waktuPerbaikan
            val selesaiTime = if (status == "Selesai") "21 Mei 18:00" else null
            db.incidentRecordDao().updateIncidentStatus(
                incidentId,
                status,
                perbaikanTime,
                selesaiTime,
                catatan
            )
        }
    }

    suspend fun insertEmergencyAlert(alert: EmergencyAlertEntity): Long {
        return db.emergencyAlertDao().insertAlert(alert)
    }

    suspend fun updateEmergencyStatus(id: Int, status: String) {
        db.emergencyAlertDao().updateStatus(id, status)
    }
}

