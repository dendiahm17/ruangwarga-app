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
        val existingProfile = db.residentProfileDao().getProfile().firstOrNull()
        if (existingProfile == null) {
            db.residentProfileDao().insertOrUpdateProfile(
                ResidentProfileEntity(
                    id = 1,
                    nama = "Budi Santoso",
                    rt = "RT 03",
                    rw = "RW 02",
                    noKk = "3275123456789001",
                    nik = "3275081203890002",
                    alamat = "Jl. Melati Blok C No. 12",
                    statusKeluarga = "Kepala Keluarga",
                    telepon = "0812-3456-7890",
                    email = "budi.santoso@email.com",
                    pekerjaan = "Karyawan Swasta",
                    jenisKelamin = "Laki-laki",
                    agama = "Islam",
                    statusPernikahan = "Kawin"
                )
            )

            db.familyMemberDao().insertMembers(
                listOf(
                    FamilyMemberEntity(
                        nama = "Budi Santoso",
                        nik = "3275081203890002",
                        hubungan = "Kepala Keluarga",
                        tempatTanggalLahir = "Jakarta, 12 Maret 1989",
                        jenisKelamin = "Laki-laki",
                        pekerjaan = "Karyawan Swasta",
                        agama = "Islam"
                    ),
                    FamilyMemberEntity(
                        nama = "Siti Rahmawati",
                        nik = "3275085408920001",
                        hubungan = "Istri",
                        tempatTanggalLahir = "Bandung, 14 Agustus 1992",
                        jenisKelamin = "Perempuan",
                        pekerjaan = "Guru",
                        agama = "Islam"
                    ),
                    FamilyMemberEntity(
                        nama = "Rizky Santoso",
                        nik = "3275082105180004",
                        hubungan = "Anak",
                        tempatTanggalLahir = "Jakarta, 21 Mei 2018",
                        jenisKelamin = "Laki-laki",
                        pekerjaan = "Pelajar",
                        agama = "Islam"
                    )
                )
            )

            db.letterRequestDao().insertLetters(
                listOf(
                    LetterRequestEntity(
                        nomorSurat = "042/SKD/RT03/V/2026",
                        jenisSurat = "Surat Keterangan Domisili",
                        keperluan = "Persyaratan pembukaan rekening bank baru",
                        keteranganTambahan = "Tinggal di Blok C No. 12 sejak tahun 2021",
                        status = "Selesai",
                        tanggalPengajuan = "14 Mei 2026",
                        tanggalSelesai = "15 Mei 2026",
                        catatanRt = "Surat telah disetujui & ditandatangani Ketua RT 03 & RW 02."
                    ),
                    LetterRequestEntity(
                        nomorSurat = "039/SKCK/RT03/V/2026",
                        jenisSurat = "Surat Pengantar SKCK",
                        keperluan = "Melamar pekerjaan BUMN",
                        keteranganTambahan = "Foto copy KTP dan KK sudah dilampirkan",
                        status = "Diproses",
                        tanggalPengajuan = "18 Mei 2026",
                        tanggalSelesai = null,
                        catatanRt = "Sedang verifikasi berkas oleh pengurus RT."
                    ),
                    LetterRequestEntity(
                        nomorSurat = "031/SP/RT03/IV/2026",
                        jenisSurat = "Surat Pengantar",
                        keperluan = "Pengurusan paspor di Kantor Imigrasi",
                        keteranganTambahan = "Rencana perjalanan dinas",
                        status = "Arsip",
                        tanggalPengajuan = "28 Apr 2026",
                        tanggalSelesai = "29 Apr 2026",
                        catatanRt = "Dokumen fisik telah diambil pemohon."
                    )
                )
            )

            db.duesRecordDao().insertDuesList(
                listOf(
                    DuesRecordEntity(
                        periodeBulan = "Mei 2026",
                        jumlah = 20000,
                        status = "Lunas",
                        tanggalBayar = "10 Mei 2026",
                        metodePembayaran = "QRIS",
                        kodeTransaksi = "RTRW-202605-0042",
                        buktiBayar = "QRIS_20260510_0042.png"
                    ),
                    DuesRecordEntity(
                        periodeBulan = "April 2026",
                        jumlah = 20000,
                        status = "Lunas",
                        tanggalBayar = "20 Apr 2026",
                        metodePembayaran = "Transfer Bank",
                        kodeTransaksi = "RTRW-202604-0038",
                        buktiBayar = "TRF_BCA_20260420.png"
                    ),
                    DuesRecordEntity(
                        periodeBulan = "Maret 2026",
                        jumlah = 20000,
                        status = "Lunas",
                        tanggalBayar = "20 Mar 2026",
                        metodePembayaran = "QRIS",
                        kodeTransaksi = "RTRW-202603-0019"
                    ),
                    DuesRecordEntity(
                        periodeBulan = "Februari 2026",
                        jumlah = 20000,
                        status = "Lunas",
                        tanggalBayar = "20 Feb 2026",
                        metodePembayaran = "Tunai ke Bendahara",
                        kodeTransaksi = "RTRW-202602-0015"
                    ),
                    DuesRecordEntity(
                        periodeBulan = "Januari 2026",
                        jumlah = 20000,
                        status = "Lunas",
                        tanggalBayar = "15 Jan 2026",
                        metodePembayaran = "Transfer Bank",
                        kodeTransaksi = "RTRW-202601-0008"
                    )
                )
            )

            db.complaintRecordDao().insertComplaints(
                listOf(
                    ComplaintRecordEntity(
                        judul = "Lampu Jalan Mati",
                        lokasi = "Blok C No. 12",
                        kategori = "Fasilitas Umum",
                        deskripsi = "Lampu penerangan jalan di tiang depan rumah Blok C No. 12 sudah padam sejak 2 malam lalu, jalanan menjadi gelap.",
                        status = "Diproses",
                        tanggal = "17 Mei 2026",
                        waktu = "10:30 WIB",
                        tanggapanRt = "Sudah dikoordinasikan dengan petugas PLN & PJU setempat. Jadwal perbaikan besok.",
                        fotoBukti = "Foto Lampu Padam"
                    ),
                    ComplaintRecordEntity(
                        judul = "Sampah Menumpuk",
                        lokasi = "Depan Pos RT",
                        kategori = "Kebersihan",
                        deskripsi = "Sampah daun dan ranting setelah pemangkasan pohon belum diangkut truk kebersihan.",
                        status = "Diproses",
                        tanggal = "15 Mei 2026",
                        waktu = "09:15 WIB",
                        tanggapanRt = "Truk kebersihan dijadwalkan menjemput hari ini pukul 14:00.",
                        fotoBukti = "Foto Tumpukan Sampah"
                    ),
                    ComplaintRecordEntity(
                        judul = "Saluran Air Tersumbat",
                        lokasi = "Blok B No. 7",
                        kategori = "Infrastruktur",
                        deskripsi = "Got samping tersumbat endapan pasir dan lumpur, air meluap saat hujan deras.",
                        status = "Selesai",
                        tanggal = "12 Mei 2026",
                        waktu = "08:20 WIB",
                        tanggapanRt = "Telah dibersihkan oleh tim kerja bakti dan saluran kembali lancar.",
                        fotoBukti = "Foto Saluran Tersumbat"
                    )
                )
            )

            db.announcementRecordDao().insertAnnouncements(
                listOf(
                    AnnouncementRecordEntity(
                        judul = "Kerja Bakti Lingkungan",
                        ringkasan = "Kerja bakti pembersihan saluran air dan pemangkasan dahan pohon.",
                        konten = "Akan dilaksanakan pada:\nMinggu, 25 Mei 2026\n07:00 WIB\nTempat: Balai Warga RT 03\n\nDiharapkan seluruh warga membawa perlengkapan kebersihan masing-masing. Disediakan konsumsi dan hidangan ringan.",
                        kategori = "RT 03",
                        isPenting = true,
                        isBaru = true,
                        waktuKegiatan = "Minggu, 25 Mei 2026 07:00 WIB",
                        tempatKegiatan = "Balai Warga RT 03",
                        tanggalPosting = "20 Mei 2026",
                        lingkup = "RT 03"
                    ),
                    AnnouncementRecordEntity(
                        judul = "Pembayaran Iuran Kas",
                        ringkasan = "Pengingat pembayaran iuran bulan Mei 2026.",
                        konten = "Pengingat pembayaran iuran bulan Mei 2026. Besar iuran Rp 20.000 per KK untuk kebersihan dan keamanan lingkungan. Terima kasih atas partisipasinya.",
                        kategori = "RW 02",
                        isPenting = false,
                        isBaru = false,
                        waktuKegiatan = "Batas: 25 Mei 2026",
                        tempatKegiatan = "Online / Bendahara RT",
                        tanggalPosting = "19 Mei 2026",
                        lingkup = "RW 02"
                    ),
                    AnnouncementRecordEntity(
                        judul = "Pengajian Rutin Bulanan",
                        ringkasan = "Kajian bulanan warga dan doa bersama.",
                        konten = "Jumat, 23 Mei 2026\n19:30 WIB (Ba'da Isya)\nTempat: Mushola Al-Ikhlas\n\nPenceramah: Ustadz Ahmad Fauzi. Terbuka untuk seluruh warga RT 01 - RT 05.",
                        kategori = "RT 03",
                        isPenting = false,
                        isBaru = false,
                        waktuKegiatan = "Jumat, 23 Mei 2026 19:30 WIB",
                        tempatKegiatan = "Mushola Al-Ikhlas",
                        tanggalPosting = "18 Mei 2026",
                        lingkup = "RT 03"
                    ),
                    AnnouncementRecordEntity(
                        judul = "Posyandu Balita & Lansia",
                        ringkasan = "Pemeriksaan kesehatan rutin bulanan balita dan lansia.",
                        konten = "Selasa, 27 Mei 2026\n08:30 - 11:30 WIB\nTempat: Posyandu Melati RW 02\n\nLayanan: Imunisasi, penimbangan berat badan, pembagian vitamin, dan cek tensi/gula darah gratis.",
                        kategori = "RW 02",
                        isPenting = false,
                        isBaru = false,
                        waktuKegiatan = "Selasa, 27 Mei 2026 08:30 WIB",
                        tempatKegiatan = "Posyandu Melati RW 02",
                        tanggalPosting = "17 Mei 2026",
                        lingkup = "RW 02"
                    )
                )
            )

            db.notificationDao().insertNotifications(
                listOf(
                    NotificationEntity(
                        judul = "Pohon tumbang di Blok C",
                        pesan = "Pohon tumbang di persimpangan Blok C. Tim penanganan sedang menuju lokasi, harap hindari area tersebut sementara waktu.",
                        tanggal = "21 Mei 2026 • 16:40",
                        waktuLalu = "10 menit lalu",
                        isDibaca = false,
                        tipe = "kejadian",
                        kategori = "Urgent",
                        actionLabel = "Lihat Lokasi",
                        fotoType = "pohon"
                    ),
                    NotificationEntity(
                        judul = "Rapat Warga RW 02",
                        pesan = "Undangan rapat koordinasi pengurus & warga RW 02 - Sabtu, 25 Mei 2026 pukul 20.00 WIB di Balai RW.",
                        tanggal = "21 Mei 2026 • 16:20",
                        waktuLalu = "30 menit lalu",
                        isDibaca = false,
                        tipe = "pengumuman",
                        kategori = "Important",
                        actionLabel = "Lihat Undangan",
                        fotoType = null
                    ),
                    NotificationEntity(
                        judul = "Besok Kerja Bakti Lingkungan",
                        pesan = "Persiapan kerja bakti akbar minggu pagi pukul 07.00 WIB. Jangan lupa bawa perlengkapan kebersihan masing-masing.",
                        tanggal = "21 Mei 2026 • 15:50",
                        waktuLalu = "1 jam lalu",
                        isDibaca = false,
                        tipe = "kegiatan",
                        kategori = "Activity",
                        actionLabel = "Saya Ikut",
                        fotoType = null
                    ),
                    NotificationEntity(
                        judul = "Dokumentasi Kerja Bakti Tersedia",
                        pesan = "Foto dan rangkuman kegiatan gotong royong minggu lalu sudah diunggah ke portal warga.",
                        tanggal = "21 Mei 2026 • 14:50",
                        waktuLalu = "2 jam lalu",
                        isDibaca = true,
                        tipe = "pengumuman",
                        kategori = "Informational",
                        actionLabel = "Lihat Foto",
                        fotoType = null
                    ),
                    NotificationEntity(
                        judul = "Surat Keterangan Domisili Selesai",
                        pesan = "Surat Keterangan Domisili No. 042/SKD/RT03/V/2026 telah ditandatangani dan siap diunduh/dibagikan.",
                        tanggal = "15 Mei 2026 • 14:00",
                        waktuLalu = "6 hari lalu",
                        isDibaca = true,
                        tipe = "surat",
                        kategori = "Important",
                        actionLabel = "Buka Surat"
                    ),
                    NotificationEntity(
                        judul = "Iuran Kas RT Mei 2026 Lunas",
                        pesan = "Pembayaran iuran kas lingkungan bulan Mei 2026 sebesar Rp 20.000 telah terverifikasi bendahara.",
                        tanggal = "10 Mei 2026 • 09:30",
                        waktuLalu = "11 hari lalu",
                        isDibaca = true,
                        tipe = "iuran",
                        kategori = "Informational",
                        actionLabel = "Lihat Buku Kas"
                    )
                )
            )

            db.rondaScheduleDao().insertSchedules(
                listOf(
                    RondaScheduleEntity(
                        hari = "Senin",
                        tanggal = "25 Mei 2026",
                        waktu = "22:00 - 04:00 WIB",
                        posko = "Pos Kamling RT 03 (Depan Balai Warga)",
                        petugasList = "Budi Santoso (Koordinator), Pak Agus (Blok A), Pak Bambang (Blok B), Pak Rudi (Blok C)",
                        statusKehadiranSaya = "Hadir",
                        catatan = "Patroli keliling setiap 2 jam, fokus portal masuk & jalan utama"
                    ),
                    RondaScheduleEntity(
                        hari = "Selasa",
                        tanggal = "26 Mei 2026",
                        waktu = "22:00 - 04:00 WIB",
                        posko = "Pos Kamling RT 03",
                        petugasList = "Pak Hendro (Koordinator), Pak Yudi (Blok C), Pak Danang (Blok D), Pak Eko (Blok E)",
                        statusKehadiranSaya = "Bukan Jadwal Saya",
                        catatan = "Kunci portal ditutup pukul 23:00 WIB"
                    ),
                    RondaScheduleEntity(
                        hari = "Rabu",
                        tanggal = "27 Mei 2026",
                        waktu = "22:00 - 04:00 WIB",
                        posko = "Pos Kamling RT 03",
                        petugasList = "Pak Wawan (Koordinator), Pak Arif (Blok B), Pak Farhan (Blok C), Pak Toni (Blok A)",
                        statusKehadiranSaya = "Bukan Jadwal Saya",
                        catatan = "Koordinasi dengan satpam gerbang utama RW 02"
                    ),
                    RondaScheduleEntity(
                        hari = "Kamis",
                        tanggal = "28 Mei 2026",
                        waktu = "22:00 - 04:00 WIB",
                        posko = "Pos Kamling RT 03",
                        petugasList = "Pak Slamet (Koordinator), Pak Rian (Blok D), Pak Lukman (Blok E), Pak Bayu (Blok C)",
                        statusKehadiranSaya = "Bukan Jadwal Saya",
                        catatan = "Pemeriksaan meteran air & lampu penerangan umum"
                    ),
                    RondaScheduleEntity(
                        hari = "Jumat",
                        tanggal = "29 Mei 2026",
                        waktu = "22:00 - 04:00 WIB",
                        posko = "Pos Kamling RT 03",
                        petugasList = "Pak Dedi (Koordinator), Pak Gani (Blok A), Pak Haris (Blok B), Pak Iwan (Blok C)",
                        statusKehadiranSaya = "Bukan Jadwal Saya",
                        catatan = "Patroli pasca pengajian warga"
                    ),
                    RondaScheduleEntity(
                        hari = "Sabtu",
                        tanggal = "30 Mei 2026",
                        waktu = "22:00 - 04:00 WIB",
                        posko = "Pos Kamling RT 03",
                        petugasList = "Budi Santoso, Pak Joko (Ketua RT), Pak Yusuf (Blok D), Pak Dimas (Blok B)",
                        statusKehadiranSaya = "Belum Konfirmasi",
                        catatan = "Malam minggu, pengawasan ekstra untuk tamu dari luar komplek"
                    ),
                    RondaScheduleEntity(
                        hari = "Minggu",
                        tanggal = "31 Mei 2026",
                        waktu = "22:00 - 04:00 WIB",
                        posko = "Pos Kamling RT 03",
                        petugasList = "Pak Kevin (Koordinator), Pak Surya (Blok C), Pak Zaky (Blok A), Pak Fajar (Blok E)",
                        statusKehadiranSaya = "Bukan Jadwal Saya",
                        catatan = "Persiapan menjelang kerja bakti lingkungan"
                    )
                )
            )

            db.cashTransactionDao().insertTransactions(
                listOf(
                    CashTransactionEntity(
                        tipe = "Pemasukan",
                        judul = "Iuran Bulanan Warga (45 KK) Mei 2026",
                        kategori = "Iuran Warga",
                        jumlah = 900000,
                        tanggal = "10 Mei 2026",
                        keterangan = "Iuran rutin keamanan dan kebersihan RT 03",
                        dicatatOleh = "Bendahara RT 03"
                    ),
                    CashTransactionEntity(
                        tipe = "Pemasukan",
                        judul = "Donasi Swadaya Lampu Jalan",
                        kategori = "Iuran Warga",
                        jumlah = 500000,
                        tanggal = "05 Mei 2026",
                        keterangan = "Sumbangan sukarela warga Blok B & C",
                        dicatatOleh = "Bendahara RT 03"
                    ),
                    CashTransactionEntity(
                        tipe = "Pengeluaran",
                        judul = "Honor & Insentif 2 Petugas Satpam RT",
                        kategori = "Gaji Satpam",
                        jumlah = 700000,
                        tanggal = "15 Mei 2026",
                        keterangan = "Honor jaga malam periode Mei 2026",
                        dicatatOleh = "Bendahara RT 03"
                    ),
                    CashTransactionEntity(
                        tipe = "Pengeluaran",
                        judul = "Retribusi Angkutan Sampah Lingkungan",
                        kategori = "Kebersihan & Sampah",
                        jumlah = 300000,
                        tanggal = "12 Mei 2026",
                        keterangan = "Pembayaran ke Dinas Kebersihan & Truk Sampah",
                        dicatatOleh = "Bendahara RT 03"
                    ),
                    CashTransactionEntity(
                        tipe = "Pengeluaran",
                        judul = "Pembelian 4 Bohlam LED & Kabel PJU",
                        kategori = "Perbaikan Fasum",
                        jumlah = 180000,
                        tanggal = "08 Mei 2026",
                        keterangan = "Penggantian lampu penerangan jalan Blok C",
                        dicatatOleh = "Seksi Pembangunan"
                    ),
                    CashTransactionEntity(
                        tipe = "Pengeluaran",
                        judul = "Konsumsi Kerja Bakti Lingkungan",
                        kategori = "Konsumsi Acara",
                        jumlah = 150000,
                        tanggal = "01 Mei 2026",
                        keterangan = "Snack, kopi, dan air mineral kerja bakti",
                        dicatatOleh = "Seksi Sosial"
                    )
                )
            )

            db.communityEventDao().insertEvents(
                listOf(
                    CommunityEventEntity(
                        judul = "Kerja Bakti Lingkungan",
                        kategori = "Kerja Bakti",
                        tanggal = "Minggu, 26 Mei 2026",
                        waktu = "07.00 WIB - Selesai",
                        lokasi = "Balai RW -> Blok A - C",
                        deskripsi = "Mari bersama menjaga kebersihan lingkungan kita agar nyaman dan sehat untuk semua.",
                        rsvpStatus = "Hadir",
                        partisipasiStatus = "Saya Ikut",
                        jumlahHadir = 73,
                        targetPeserta = 100,
                        penanggungJawab = "Pak RT 03 & Seksi Kebersihan",
                        kebutuhanRelawan = 10,
                        terpenuhiRelawan = 5,
                        kebutuhanSapu = 10,
                        terpenuhiSapu = 6,
                        kebutuhanEmber = 5,
                        terpenuhiEmber = 3,
                        kebutuhanPickup = 2,
                        terpenuhiPickup = 1
                    ),
                    CommunityEventEntity(
                        judul = "Posyandu Balita",
                        kategori = "Posyandu",
                        tanggal = "Selasa, 28 Mei 2026",
                        waktu = "08.00 WIB - Selesai",
                        lokasi = "Balai Posyandu RW 02",
                        deskripsi = "Penimbangan balita, pemberian vitamin A, imunisasi dasar lengkap, dan konsultasi nutrisi anak.",
                        rsvpStatus = "Belum Konfirmasi",
                        partisipasiStatus = "Belum Konfirmasi",
                        jumlahHadir = 32,
                        targetPeserta = 60,
                        penanggungJawab = "Ibu Siti (Kader Posyandu RW 02)",
                        kebutuhanRelawan = 6,
                        terpenuhiRelawan = 4,
                        kebutuhanSapu = 0,
                        terpenuhiSapu = 0,
                        kebutuhanEmber = 0,
                        terpenuhiEmber = 0,
                        kebutuhanPickup = 0,
                        terpenuhiPickup = 0
                    ),
                    CommunityEventEntity(
                        judul = "Pengajian Rutin",
                        kategori = "Keagamaan",
                        tanggal = "Jumat, 31 Mei 2026",
                        waktu = "19.30 WIB - Selesai",
                        lokasi = "Mushola Al-Ikhlas",
                        deskripsi = "Kajian tafsir bulanan bersama warga muslim dan silaturahmi antar rukun tetangga.",
                        rsvpStatus = "Belum Konfirmasi",
                        partisipasiStatus = "Belum Konfirmasi",
                        jumlahHadir = 55,
                        targetPeserta = 80,
                        penanggungJawab = "Pengurus Mushola & RT 03",
                        kebutuhanRelawan = 4,
                        terpenuhiRelawan = 3,
                        kebutuhanSapu = 0,
                        terpenuhiSapu = 0,
                        kebutuhanEmber = 0,
                        terpenuhiEmber = 0,
                        kebutuhanPickup = 0,
                        terpenuhiPickup = 0
                    ),
                    CommunityEventEntity(
                        judul = "Senam Sehat Warga & Lansia",
                        kategori = "Senam Sehat",
                        tanggal = "Minggu, 2 Juni 2026",
                        waktu = "06.30 - 08.00 WIB",
                        lokasi = "Lapangan RW 02",
                        deskripsi = "Senam aerobik bersama instruktur profesional dan cek tensi gratis bagi lansia.",
                        rsvpStatus = "Belum Konfirmasi",
                        partisipasiStatus = "Belum Konfirmasi",
                        jumlahHadir = 40,
                        targetPeserta = 70,
                        penanggungJawab = "Seksi Pemuda & Olahraga",
                        kebutuhanRelawan = 4,
                        terpenuhiRelawan = 2,
                        kebutuhanSapu = 0,
                        terpenuhiSapu = 0,
                        kebutuhanEmber = 0,
                        terpenuhiEmber = 0,
                        kebutuhanPickup = 0,
                        terpenuhiPickup = 0
                    )
                )
            )

            db.socialHelpDao().insertSocialHelpList(
                listOf(
                    SocialHelpEntity(
                        judul = "Warga Membutuhkan Bantuan Transportasi",
                        lokasi = "RT 03",
                        tanggal = "21 Mei 2026",
                        kategori = "Transportasi",
                        kebutuhanTags = "Kendaraan, Pendamping",
                        deskripsi = "Warga lansia di Blok C membutuhkan tumpangan kendaraan roda 4 dan 1 pendamping untuk kontrol kesehatan rutin ke RSUD Pasar Rebo besok pagi.",
                        waktuPosting = "2 jam lalu",
                        status = "Membutuhkan",
                        jumlahRelawan = 2,
                        isMyContributed = false
                    ),
                    SocialHelpEntity(
                        judul = "Bantuan untuk Keluarga Kurang Mampu",
                        lokasi = "RT 01",
                        tanggal = "20 Mei 2026",
                        kategori = "Sembako",
                        kebutuhanTags = "Sembako, Beras",
                        deskripsi = "Penggalangan bantuan paket sembako beras, minyak goreng, dan telur untuk keluarga prasejahtera dan lansia tunggal.",
                        waktuPosting = "1 hari lalu",
                        status = "Membutuhkan",
                        jumlahRelawan = 4,
                        isMyContributed = false
                    ),
                    SocialHelpEntity(
                        judul = "Pendampingan Lansia Berobat ke Puskesmas",
                        lokasi = "RT 02",
                        tanggal = "19 Mei 2026",
                        kategori = "Relawan",
                        kebutuhanTags = "Relawan, Transportasi",
                        deskripsi = "Dibutuhkan relawan pendamping untuk mengantar warga lansia mengambil obat rutin di Puskesmas Kelurahan.",
                        waktuPosting = "2 hari lalu",
                        status = "Terbantu",
                        jumlahRelawan = 3,
                        isMyContributed = true
                    )
                )
            )

            db.incidentRecordDao().insertIncidents(
                listOf(
                    IncidentRecordEntity(
                        judul = "Lampu Jalan Mati",
                        lokasi = "Blok C No.12",
                        kategori = "Fasilitas Umum",
                        status = "Dalam Perbaikan",
                        waktuLapor = "21 Mei 16:32",
                        waktuVerifikasi = "21 Mei 16:45",
                        waktuPerbaikan = "21 Mei 17:20",
                        waktuSelesai = null,
                        deskripsi = "Lampu penerangan jalan utama Blok C padam sejak kemarin malam sehingga jalanan agak gelap.",
                        catatanPengurus = "Teknisi PLN RT sedang menangani penggantian fitting dan bohlam LED 40W.",
                        fotoType = "lampu"
                    ),
                    IncidentRecordEntity(
                        judul = "Sampah Menumpuk",
                        lokasi = "Depan Pos RT 03",
                        kategori = "Kebersihan",
                        status = "Diproses",
                        waktuLapor = "21 Mei 14:15",
                        waktuVerifikasi = "21 Mei 14:30",
                        waktuPerbaikan = "21 Mei 15:00",
                        waktuSelesai = null,
                        deskripsi = "Sampah ranting pohon dan kantong menumpuk di dekat pos ronda setelah kerja bakti kecil.",
                        catatanPengurus = "Truk dinas kebersihan sudah dijadwalkan mengangkut sore ini pukul 17:00.",
                        fotoType = "sampah"
                    ),
                    IncidentRecordEntity(
                        judul = "Saluran Air Tersumbat",
                        lokasi = "Blok B No. 7",
                        kategori = "Infrastruktur",
                        status = "Selesai",
                        waktuLapor = "18 Mei 09:00",
                        waktuVerifikasi = "18 Mei 09:15",
                        waktuPerbaikan = "18 Mei 10:00",
                        waktuSelesai = "18 Mei 11:30",
                        deskripsi = "Saluran air tertutup endapan daun dan lumpur kering.",
                        catatanPengurus = "Telah dibersihkan dan saluran air kembali lancar.",
                        fotoType = "saluran"
                    )
                )
            )

            db.userParticipationDao().insertParticipations(
                listOf(
                    UserParticipationEntity(
                        judulKegiatan = "Kerja Bakti Lingkungan",
                        tanggal = "26 Mei 2026",
                        kategori = "Gotong Royong",
                        status = "Diikuti",
                        peran = "Peserta Aktif"
                    ),
                    UserParticipationEntity(
                        judulKegiatan = "Donor Darah RW 02",
                        tanggal = "10 Mei 2026",
                        kategori = "Sosial & Kesehatan",
                        status = "Selesai",
                        peran = "Pendonor"
                    ),
                    UserParticipationEntity(
                        judulKegiatan = "Pengajian Bulanan Warga",
                        tanggal = "23 April 2026",
                        kategori = "Keagamaan",
                        status = "Selesai",
                        peran = "Peserta"
                    ),
                    UserParticipationEntity(
                        judulKegiatan = "Penyaluran Bantuan Sembako RT 03",
                        tanggal = "15 April 2026",
                        kategori = "Sosial",
                        status = "Selesai",
                        peran = "Relawan Penyalur"
                    )
                )
            )

            db.pollingDao().insertPolls(
                listOf(
                    PollingEntity(
                        judul = "Pemasangan Portal Otomatis & RFID Card Gerbang Utama",
                        deskripsi = "Rencana pengadaan portal otomatis dengan tap RFID card untuk warga dan satpam di malam hari mulai pukul 23:00 demi keamanan lingkungan.",
                        kategori = "Keamanan & Fasilitas",
                        batasWaktu = "28 Mei 2026",
                        opsiA = "Setuju (Iuran Khusus Rp 15rb/bln)",
                        opsiB = "Tetap Gunakan Portal Manual Satpam",
                        opsiC = "Tidak Setuju / Belum Butuh",
                        suaraA = 32,
                        suaraB = 14,
                        suaraC = 4,
                        myVote = "A",
                        status = "Aktif"
                    ),
                    PollingEntity(
                        judul = "Pemilihan Hari & Waktu Rutin Senam Sehat",
                        deskripsi = "Penentuan jadwal rutin senam aerobik warga RT 03 agar diikuti oleh lebih banyak warga.",
                        kategori = "Kegiatan Warga",
                        batasWaktu = "26 Mei 2026",
                        opsiA = "Setiap Minggu Pagi (06:30 WIB)",
                        opsiB = "Setiap Sabtu Sore (16:30 WIB)",
                        opsiC = "Cukup Dua Minggu Sekali",
                        suaraA = 27,
                        suaraB = 11,
                        suaraC = 6,
                        myVote = null,
                        status = "Aktif"
                    )
                )
            )

            // Seed Officers (Tier 1: Ketua RW, Tier 2: Jajaran RW, Tier 3: Pengurus RT & Seksi)
            db.officerDao().insertOfficers(
                listOf(
                    // Tier 1: Ketua RW
                    OfficerMemberEntity(
                        nama = "H. Hendra Gunawan, S.E.",
                        jabatan = "Ketua RW 02",
                        tier = 1,
                        levelBadge = "Ketua RW",
                        alamat = "Jl. Melati Raya Blok A No. 01",
                        telepon = "0812-9876-5432",
                        isOnline = true,
                        statusText = "Online (Aktif)",
                        periodeBakti = "2024 - 2029",
                        deskripsiTugas = "Memimpin penyelenggaraan ketertiban, pelayanan administrasi warga, dan koordinasi lintas RT di wilayah RW 02."
                    ),
                    // Tier 2: Jajaran Inti Pengurus RW
                    OfficerMemberEntity(
                        nama = "Drs. Bambang Sudiro",
                        jabatan = "Wakil Ketua RW 02",
                        tier = 2,
                        levelBadge = "Pengurus Inti RW",
                        alamat = "Jl. Mawar Blok B No. 15",
                        telepon = "0813-8765-4321",
                        isOnline = true,
                        statusText = "Online",
                        periodeBakti = "2024 - 2029",
                        deskripsiTugas = "Membantu Ketua RW dalam operasional harian dan pengawasan program pembangunan."
                    ),
                    OfficerMemberEntity(
                        nama = "Rina Indriani, S.Kom.",
                        jabatan = "Sekretaris RW 02",
                        tier = 2,
                        levelBadge = "Pengurus Inti RW",
                        alamat = "Jl. Anggrek Blok D No. 08",
                        telepon = "0812-3344-5566",
                        isOnline = true,
                        statusText = "Online",
                        periodeBakti = "2024 - 2029",
                        deskripsiTugas = "Pengelolaan persuratan, arsip kependudukan digital, dan administrasi umum RW."
                    ),
                    OfficerMemberEntity(
                        nama = "Iwan Setiawan, S.E., Ak.",
                        jabatan = "Bendahara RW 02",
                        tier = 2,
                        levelBadge = "Pengurus Inti RW",
                        alamat = "Jl. Melati Blok C No. 03",
                        telepon = "0815-6677-8899",
                        isOnline = false,
                        statusText = "Aktif 15m lalu",
                        periodeBakti = "2024 - 2029",
                        deskripsiTugas = "Pengelolaan kas keuangan RW, transparansi laporan iuran, dan pembukuan anggaran."
                    ),
                    // Tier 3: Pengurus RT & Seksi Bidang
                    OfficerMemberEntity(
                        nama = "Dedi Mulyadi",
                        jabatan = "Ketua RT 01",
                        tier = 3,
                        levelBadge = "Ketua RT",
                        alamat = "Jl. Mawar No. 04",
                        telepon = "0817-1122-3344",
                        isOnline = false,
                        statusText = "Aktif 1 jam lalu",
                        periodeBakti = "2023 - 2028",
                        deskripsiTugas = "Koordinator pelayanan warga lingkungan RT 01."
                    ),
                    OfficerMemberEntity(
                        nama = "Agus Prasetyo",
                        jabatan = "Ketua RT 02",
                        tier = 3,
                        levelBadge = "Ketua RT",
                        alamat = "Jl. Dahlia No. 10",
                        telepon = "0818-2233-4455",
                        isOnline = true,
                        statusText = "Online",
                        periodeBakti = "2023 - 2028",
                        deskripsiTugas = "Koordinator pelayanan warga lingkungan RT 02."
                    ),
                    OfficerMemberEntity(
                        nama = "Ir. Triyono Santoso",
                        jabatan = "Ketua RT 03",
                        tier = 3,
                        levelBadge = "Ketua RT",
                        alamat = "Jl. Melati Blok C No. 01",
                        telepon = "0812-7788-9900",
                        isOnline = true,
                        statusText = "Online",
                        periodeBakti = "2023 - 2028",
                        deskripsiTugas = "Koordinator pelayanan warga lingkungan RT 03."
                    ),
                    OfficerMemberEntity(
                        nama = "Supriyanto",
                        jabatan = "Ketua RT 04",
                        tier = 3,
                        levelBadge = "Ketua RT",
                        alamat = "Jl. Flamboyan No. 12",
                        telepon = "0819-3344-5566",
                        isOnline = false,
                        statusText = "Offline",
                        periodeBakti = "2023 - 2028",
                        deskripsiTugas = "Koordinator pelayanan warga lingkungan RT 04."
                    ),
                    OfficerMemberEntity(
                        nama = "Kapten (Purn) Slamet Riyadi",
                        jabatan = "Seksi Keamanan & Ketertiban",
                        tier = 3,
                        levelBadge = "Seksi Bidang",
                        alamat = "Jl. Pos Ronda Utama",
                        telepon = "0811-9988-7766",
                        isOnline = true,
                        statusText = "Online (Siaga)",
                        periodeBakti = "2024 - 2029",
                        deskripsiTugas = "Koordinator pos satpam, jadwal ronda malam, dan tanggap darurat lingkungan."
                    ),
                    OfficerMemberEntity(
                        nama = "dr. Ratna Juwita",
                        jabatan = "Seksi Kesehatan & Posyandu",
                        tier = 3,
                        levelBadge = "Seksi Bidang",
                        alamat = "Jl. Anggrek No. 05",
                        telepon = "0813-1199-2288",
                        isOnline = true,
                        statusText = "Online",
                        periodeBakti = "2024 - 2029",
                        deskripsiTugas = "Penyelenggaraan Posyandu balita & lansia serta penanganan kesehatan warga."
                    ),
                    OfficerMemberEntity(
                        nama = "Ahmad Zaelani",
                        jabatan = "Seksi Pembangunan & Fasum",
                        tier = 3,
                        levelBadge = "Seksi Bidang",
                        alamat = "Jl. Melati Blok B No. 09",
                        telepon = "0812-4455-6677",
                        isOnline = false,
                        statusText = "Aktif 30m lalu",
                        periodeBakti = "2024 - 2029",
                        deskripsiTugas = "Pemeliharaan jalan, lampu penerangan jalan, drainase got, dan fasilitas umum."
                    )
                )
            )

            // Seed Residents Directory with Live Online Status
            db.residentDirectoryDao().insertResidents(
                listOf(
                    ResidentDirectoryEntity(
                        nama = "Budi Santoso (Anda)",
                        alamat = "Jl. Melati Blok C No. 12",
                        rt = "RT 03",
                        rw = "RW 02",
                        peranKeluarga = "Kepala Keluarga",
                        telepon = "0812-3456-7890",
                        isOnline = true,
                        statusText = "Online Sekarang",
                        pekerjaan = "Karyawan Swasta",
                        statusIuranBulanIni = true
                    ),
                    ResidentDirectoryEntity(
                        nama = "Ahmad Hidayat",
                        alamat = "Jl. Melati Blok C No. 05",
                        rt = "RT 03",
                        rw = "RW 02",
                        peranKeluarga = "Kepala Keluarga",
                        telepon = "0812-1111-2222",
                        isOnline = true,
                        statusText = "Online Sekarang",
                        pekerjaan = "Wiraswasta",
                        statusIuranBulanIni = true
                    ),
                    ResidentDirectoryEntity(
                        nama = "Dewi Lestari",
                        alamat = "Jl. Melati Blok C No. 08",
                        rt = "RT 03",
                        rw = "RW 02",
                        peranKeluarga = "Ibu Rumah Tangga",
                        telepon = "0813-2222-3333",
                        isOnline = true,
                        statusText = "Online Sekarang",
                        pekerjaan = "Pengusaha Kuliner",
                        statusIuranBulanIni = true
                    ),
                    ResidentDirectoryEntity(
                        nama = "Farhan Pratama",
                        alamat = "Jl. Melati Blok C No. 14",
                        rt = "RT 03",
                        rw = "RW 02",
                        peranKeluarga = "Anggota Keluarga",
                        telepon = "0815-3333-4444",
                        isOnline = true,
                        statusText = "Online Sekarang",
                        pekerjaan = "Mahasiswa / Karang Taruna",
                        statusIuranBulanIni = true
                    ),
                    ResidentDirectoryEntity(
                        nama = "Guntur Wijaya",
                        alamat = "Jl. Mawar Blok B No. 02",
                        rt = "RT 01",
                        rw = "RW 02",
                        peranKeluarga = "Kepala Keluarga",
                        telepon = "0817-4444-5555",
                        isOnline = true,
                        statusText = "Online Sekarang",
                        pekerjaan = "PNS",
                        statusIuranBulanIni = true
                    ),
                    ResidentDirectoryEntity(
                        nama = "Hartono Kusuma",
                        alamat = "Jl. Dahlia Blok A No. 11",
                        rt = "RT 02",
                        rw = "RW 02",
                        peranKeluarga = "Tokoh Warga",
                        telepon = "0818-5555-6666",
                        isOnline = false,
                        statusText = "Aktif 10 menit lalu",
                        pekerjaan = "Pensiunan BUMN",
                        statusIuranBulanIni = true
                    ),
                    ResidentDirectoryEntity(
                        nama = "Indah Permatasari",
                        alamat = "Jl. Anggrek Blok D No. 04",
                        rt = "RT 04",
                        rw = "RW 02",
                        peranKeluarga = "Kepala Keluarga",
                        telepon = "0819-6666-7777",
                        isOnline = false,
                        statusText = "Aktif 25 menit lalu",
                        pekerjaan = "Guru Sekolah",
                        statusIuranBulanIni = true
                    ),
                    ResidentDirectoryEntity(
                        nama = "Joko Susilo",
                        alamat = "Jl. Melati Blok C No. 09",
                        rt = "RT 03",
                        rw = "RW 02",
                        peranKeluarga = "Kepala Keluarga",
                        telepon = "0812-7777-8888",
                        isOnline = false,
                        statusText = "Aktif 1 jam lalu",
                        pekerjaan = "Arsitek",
                        statusIuranBulanIni = false
                    ),
                    ResidentDirectoryEntity(
                        nama = "Kurniawan Putra",
                        alamat = "Jl. Flamboyan Blok E No. 07",
                        rt = "RT 05",
                        rw = "RW 02",
                        peranKeluarga = "Kepala Keluarga",
                        telepon = "0813-8888-9999",
                        isOnline = false,
                        statusText = "Offline (Kemarin)",
                        pekerjaan = "Akuntan",
                        statusIuranBulanIni = true
                    ),
                    ResidentDirectoryEntity(
                        nama = "Lukman Hakim",
                        alamat = "Jl. Mawar Blok B No. 10",
                        rt = "RT 01",
                        rw = "RW 02",
                        peranKeluarga = "Kepala Keluarga",
                        telepon = "0815-9999-0000",
                        isOnline = false,
                        statusText = "Offline",
                        pekerjaan = "Dokter Umum",
                        statusIuranBulanIni = true
                    )
                )
            )

            // Seed Emergency Alert (Laporan Darurat Lingkungan)
            db.emergencyAlertDao().insertAlert(
                EmergencyAlertEntity(
                    jenisDarurat = "Genangan Air",
                    judul = "GENANGAN AIR DI BLOK C",
                    pelapor = "Ketua RW 02",
                    lokasi = "Jl. Melati Blok C",
                    kontak = "0812-3456-7890",
                    waktu = "21.34 WIB",
                    tingkatPrioritas = "Peringatan",
                    status = "Aktif",
                    targetWilayah = "Seluruh RW",
                    dikeluarkanOleh = "RW 02",
                    instruksi = "• Hindari jalur Blok C sementara waktu\n• Alihkan kendaraan ke jalur Blok A\n• Petugas sedang membuka pintu air utama",
                    timelineUpdates = "21.34: Alarm dibuat|21.48: Petugas menuju lokasi|22.05: Genangan mulai surut",
                    isVerified = true,
                    catatan = "Hujan deras menyebabkan genangan setinggi 20-30 cm di area Blok C."
                )
            )

            // Seed Aset RW 02
            db.assetRwDao().insertAssets(
                listOf(
                    AssetRwEntity(
                        kodeAset = "AST-RW-001",
                        namaAset = "Tenda Terop Pesta Warga (4 x 6 meter)",
                        kategori = "Peralatan Acara",
                        jumlah = 2,
                        satuan = "Set",
                        kondisi = "Baik",
                        lokasiPenyimpanan = "Gudang Balai RW 02",
                        penanggungJawab = "Seksi Perlengkapan (Pak Heru)",
                        statusKetersediaan = "Tersedia",
                        nilaiPerolehan = 4500000,
                        tahunPengadaan = "2024",
                        catatan = "Lengkap dengan rangka besi dan terpal anti air",
                        bisaDipinjam = true
                    ),
                    AssetRwEntity(
                        kodeAset = "AST-RW-002",
                        namaAset = "Kursi Lipat Plastik Futura",
                        kategori = "Peralatan Acara",
                        jumlah = 100,
                        satuan = "Buah",
                        kondisi = "Baik",
                        lokasiPenyimpanan = "Balai Pertemuan RW 02",
                        penanggungJawab = "Seksi Perlengkapan (Pak Heru)",
                        statusKetersediaan = "Tersedia",
                        nilaiPerolehan = 15000000,
                        tahunPengadaan = "2024",
                        catatan = "Tersedia dalam 2 rak penyimpanan susun",
                        bisaDipinjam = true
                    ),
                    AssetRwEntity(
                        kodeAset = "AST-RW-003",
                        namaAset = "Sound System Portable Wireless + 2 Mic",
                        kategori = "Elektronik & Sound",
                        jumlah = 1,
                        satuan = "Set",
                        kondisi = "Baik",
                        lokasiPenyimpanan = "Kantor Sekretariat RW 02",
                        penanggungJawab = "Sekretaris RW (Ibu Rina)",
                        statusKetersediaan = "Tersedia",
                        nilaiPerolehan = 3200000,
                        tahunPengadaan = "2025",
                        catatan = "Dapat menggunakan baterai aki recharge untuk acara outdoor",
                        bisaDipinjam = true
                    ),
                    AssetRwEntity(
                        kodeAset = "AST-RW-004",
                        namaAset = "Mesin Fogging Nyamuk DBD",
                        kategori = "Kebersihan & Taman",
                        jumlah = 2,
                        satuan = "Unit",
                        kondisi = "Baik",
                        lokasiPenyimpanan = "Gudang Pos Satpam Utama",
                        penanggungJawab = "Seksi Kesehatan & Lingkungan",
                        statusKetersediaan = "Tersedia",
                        nilaiPerolehan = 2800000,
                        tahunPengadaan = "2023",
                        catatan = "Digunakan saat program fogging serentak",
                        bisaDipinjam = true
                    ),
                    AssetRwEntity(
                        kodeAset = "AST-RW-005",
                        namaAset = "Mesin Potong Rumput Gendong",
                        kategori = "Kebersihan & Taman",
                        jumlah = 3,
                        satuan = "Unit",
                        kondisi = "Baik",
                        lokasiPenyimpanan = "Gudang Lapangan RW 02",
                        penanggungJawab = "Petugas Kebersihan RW",
                        statusKetersediaan = "Tersedia",
                        nilaiPerolehan = 3900000,
                        tahunPengadaan = "2024",
                        catatan = "Bahan bakar bensin campur 2 tak",
                        bisaDipinjam = true
                    ),
                    AssetRwEntity(
                        kodeAset = "AST-RW-006",
                        namaAset = "Handy Talky (HT) Frekuensi Ronda RW",
                        kategori = "Keamanan / Ronda",
                        jumlah = 6,
                        satuan = "Unit",
                        kondisi = "Baik",
                        lokasiPenyimpanan = "Pos Kamling Utama RT 03 / RW 02",
                        penanggungJawab = "Seksi Keamanan (Pak Slamet)",
                        statusKetersediaan = "Dipinjam",
                        nilaiPerolehan = 1800000,
                        tahunPengadaan = "2024",
                        catatan = "Standby aktif untuk koordinasi pos keamanan",
                        bisaDipinjam = false
                    ),
                    AssetRwEntity(
                        kodeAset = "AST-RW-007",
                        namaAset = "Tandu Darurat & Kotak P3K Lengkap",
                        kategori = "Fasilitas Umum",
                        jumlah = 2,
                        satuan = "Set",
                        kondisi = "Baik",
                        lokasiPenyimpanan = "Posyandu RW 02",
                        penanggungJawab = "dr. Ratna Juwita",
                        statusKetersediaan = "Tersedia",
                        nilaiPerolehan = 1200000,
                        tahunPengadaan = "2024",
                        catatan = "Siaga medis untuk keadaan darurat warga",
                        bisaDipinjam = true
                    ),
                    AssetRwEntity(
                        kodeAset = "AST-RW-008",
                        namaAset = "Genset Listrik Darurat 3500 Watt",
                        kategori = "Elektronik & Sound",
                        jumlah = 1,
                        satuan = "Unit",
                        kondisi = "Perlu Perbaikan",
                        lokasiPenyimpanan = "Gudang Balai RW 02",
                        penanggungJawab = "Seksi Pembangunan & Fasum",
                        statusKetersediaan = "Dalam Pemeliharaan",
                        nilaiPerolehan = 5500000,
                        tahunPengadaan = "2022",
                        catatan = "Sedang servis berkala karburator dan ganti oli",
                        bisaDipinjam = false
                    )
                )
            )
        }
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

