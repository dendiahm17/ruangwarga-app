package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "resident_profile")
data class ResidentProfileEntity(
    @PrimaryKey val id: Int = 1,
    val uid: String = "",
    val nama: String = "",
    val rt: String = "RT 01",
    val rw: String = "RW 01",
    val noKk: String = "",
    val nik: String = "",
    val alamat: String = "",
    val statusKeluarga: String = "Kepala Keluarga",
    val telepon: String = "",
    val email: String = "",
    val pekerjaan: String = "",
    val jenisKelamin: String = "Laki-laki",
    val agama: String = "Islam",
    val statusPernikahan: String = "Kawin",
    val role: String = "Warga" // "Warga", "Ketua RT", "Ketua RW", "Sekretaris", "Bendahara"
)

@Entity(tableName = "family_members")
data class FamilyMemberEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nama: String,
    val nik: String,
    val hubungan: String, // Kepala Keluarga, Istri, Anak
    val tempatTanggalLahir: String,
    val jenisKelamin: String,
    val pekerjaan: String,
    val agama: String = "Islam"
)

@Entity(tableName = "letter_requests")
data class LetterRequestEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nomorSurat: String,
    val jenisSurat: String,
    val keperluan: String,
    val keteranganTambahan: String = "",
    val status: String, // "Pengajuan", "Diproses", "Selesai", "Arsip", "Ditolak"
    val tanggalPengajuan: String,
    val tanggalSelesai: String? = null,
    val catatanRt: String? = null
)

@Entity(tableName = "dues_records")
data class DuesRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val periodeBulan: String, // "Mei 2026", "April 2026", etc.
    val jumlah: Long = 20000,
    val status: String, // "Lunas", "Belum Lunas"
    val tanggalBayar: String? = null,
    val metodePembayaran: String? = null,
    val kodeTransaksi: String? = null,
    val buktiBayar: String? = null
)

@Entity(tableName = "complaint_records")
data class ComplaintRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val judul: String,
    val lokasi: String,
    val kategori: String, // "Fasilitas Umum", "Kebersihan", "Keamanan", "Infrastruktur", "Lainnya"
    val deskripsi: String,
    val status: String, // "Pengajuan", "Diproses", "Selesai"
    val tanggal: String,
    val waktu: String,
    val tanggapanRt: String? = null,
    val fotoBukti: String? = null
)

@Entity(tableName = "announcement_records")
data class AnnouncementRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val judul: String,
    val ringkasan: String,
    val konten: String,
    val kategori: String, // "Semua", "RT 03", "RW 02", "Penting"
    val isPenting: Boolean = false,
    val isBaru: Boolean = false,
    val waktuKegiatan: String? = null,
    val tempatKegiatan: String? = null,
    val tanggalPosting: String,
    val lingkup: String = "RT 03"
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val judul: String,
    val pesan: String,
    val tanggal: String,
    val waktuLalu: String = "Baru saja",
    val isDibaca: Boolean = false,
    val tipe: String = "umum", // "surat", "iuran", "pengaduan", "pengumuman", "siskamling", "kegiatan", "kejadian"
    val kategori: String = "Important", // "Urgent", "Important", "Activity", "Informational"
    val actionLabel: String? = null,
    val fotoType: String? = null // "pohon", "lampu", "kegiatan", null
)

@Entity(tableName = "ronda_schedules")
data class RondaScheduleEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val hari: String, // "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"
    val tanggal: String, // "25 Mei 2026"
    val waktu: String = "22:00 - 04:00 WIB",
    val posko: String = "Pos Kamling RT 03",
    val petugasList: String, // "Budi Santoso (Koordinator), Pak Agus, Pak Bambang, Pak Rudi"
    val statusKehadiranSaya: String = "Belum Konfirmasi", // "Belum Konfirmasi", "Hadir", "Izin", "Digantikan"
    val catatan: String = "Harap membawa senter dan jas hujan bila mendung"
)

@Entity(tableName = "emergency_alerts")
data class EmergencyAlertEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val jenisDarurat: String, // "Maling / Keamanan", "Kebakaran", "Medis Darurat", "Bencana Alam", "Genangan Air"
    val judul: String = "Keadaan Darurat",
    val pelapor: String = "Budi Santoso (RT 03 / No. 12)",
    val lokasi: String = "Jl. Melati Blok C No. 12",
    val kontak: String = "0812-3456-7890",
    val waktu: String,
    val tingkatPrioritas: String = "Kritis", // "Kritis", "Peringatan", "Informasi Penting"
    val status: String = "Aktif", // "Aktif", "Ditangani", "Terkendali", "Selesai"
    val targetWilayah: String = "Seluruh RW", // "Seluruh RW", "RT 03", "Blok C"
    val dikeluarkanOleh: String = "Pengurus RW 02",
    val instruksi: String = "• Hindari area sekitar\n• Jangan mendekati lokasi\n• Ikuti arahan petugas lapangan",
    val timelineUpdates: String = "", // "21.34: Alarm dibuat|21.48: Petugas menuju lokasi"
    val isVerified: Boolean = true,
    val catatan: String = ""
)

@Entity(tableName = "cash_transactions")
data class CashTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tipe: String, // "Pemasukan", "Pengeluaran"
    val judul: String,
    val kategori: String, // "Iuran Warga", "Gaji Satpam", "Kebersihan & Sampah", "Perbaikan Fasum", "Santunan Warga", "Konsumsi Acara", "Lainnya"
    val jumlah: Long,
    val tanggal: String,
    val keterangan: String = "",
    val dicatatOleh: String = "Bendahara RT 03"
)

@Entity(tableName = "community_events")
data class CommunityEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val judul: String,
    val kategori: String, // "Kerja Bakti", "Posyandu", "Rapat RT", "Senam Sehat", "Peringatan 17-an"
    val tanggal: String,
    val waktu: String,
    val lokasi: String,
    val deskripsi: String,
    val rsvpStatus: String = "Belum Konfirmasi", // "Belum Konfirmasi", "Hadir", "Tidak Hadir", "Saya Ikut"
    val jumlahHadir: Int = 18,
    val targetPeserta: Int = 100,
    val penanggungJawab: String = "Seksi Lingkungan & Pemuda",
    val kebutuhanRelawan: Int = 10,
    val terpenuhiRelawan: Int = 5,
    val kebutuhanSapu: Int = 10,
    val terpenuhiSapu: Int = 6,
    val kebutuhanEmber: Int = 5,
    val terpenuhiEmber: Int = 3,
    val kebutuhanPickup: Int = 2,
    val terpenuhiPickup: Int = 1,
    val partisipasiStatus: String = "Belum Konfirmasi" // "Saya Ikut", "Saya Bisa Membantu", "Saya Belum Tahu", "Saya Tidak Bisa"
)

@Entity(tableName = "social_help_records")
data class SocialHelpEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val judul: String,
    val lokasi: String, // "RT 03", "RT 01", "RT 02"
    val tanggal: String,
    val kategori: String, // "Transportasi", "Sembako", "Kesehatan", "Relawan"
    val kebutuhanTags: String, // "Kendaraan, Pendamping", "Sembako"
    val deskripsi: String,
    val waktuPosting: String, // "2 jam lalu", "1 hari lalu"
    val status: String = "Membutuhkan", // "Membutuhkan", "Terbantu", "Selesai"
    val jumlahRelawan: Int = 2,
    val isMyContributed: Boolean = false
)

@Entity(tableName = "incident_records")
data class IncidentRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val judul: String, // "Lampu Jalan Mati", "Sampah Menumpuk", "Saluran Air Tersumbat"
    val lokasi: String, // "Blok C No.12", "Depan Pos RT 03", "Blok B No. 7"
    val kategori: String, // "Fasilitas Umum", "Kebersihan", "Infrastruktur", "Keamanan"
    val status: String, // "Dalam Perbaikan", "Diproses", "Selesai", "Dilaporkan"
    val waktuLapor: String, // "21 Mei 16:32"
    val waktuVerifikasi: String? = "21 Mei 16:45",
    val waktuPerbaikan: String? = "21 Mei 17:20",
    val waktuSelesai: String? = null,
    val deskripsi: String = "Lampu jalan penerangan utama padam sejak kemarin malam.",
    val catatanPengurus: String = "Teknisi RT sedang melakukan perbaikan dan penggantian lampu LED.",
    val fotoType: String = "lampu" // "lampu", "sampah", "saluran", "pohon"
)

@Entity(tableName = "user_participations")
data class UserParticipationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val judulKegiatan: String,
    val tanggal: String,
    val kategori: String,
    val status: String = "Diikuti", // "Diikuti", "Relawan"
    val peran: String = "Peserta Aktif"
)

@Entity(tableName = "polling_records")
data class PollingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val judul: String,
    val deskripsi: String,
    val kategori: String = "Fasilitas & Keamanan",
    val batasWaktu: String,
    val opsiA: String,
    val opsiB: String,
    val opsiC: String? = null,
    val suaraA: Int = 0,
    val suaraB: Int = 0,
    val suaraC: Int = 0,
    val myVote: String? = null, // "A", "B", "C", or null
    val status: String = "Aktif" // "Aktif", "Selesai"
)

@Entity(tableName = "officer_members")
data class OfficerMemberEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nama: String,
    val jabatan: String, // "Ketua RW 02", "Wakil Ketua RW", "Sekretaris RW", "Bendahara RW", "Ketua RT 01", "Seksi Keamanan & Ronda", etc.
    val tier: Int = 3, // 1: Ketua RW, 2: Jajaran Pengurus RW (Wakil/Sekretaris/Bendahara), 3: Pengurus RT & Seksi Lainnya
    val levelBadge: String = "Pengurus RW", // "Ketua RW", "Pengurus Inti RW", "Ketua RT", "Seksi Bidang"
    val alamat: String = "",
    val telepon: String = "0812-3456-7890",
    val isOnline: Boolean = true,
    val statusText: String = "Online", // "Online", "Aktif 5m lalu", "Offline"
    val periodeBakti: String = "2024 - 2029",
    val fotoAvatarId: Int = 0, // 0 for icon fallback
    val deskripsiTugas: String = ""
)

@Entity(tableName = "resident_directory")
data class ResidentDirectoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nama: String,
    val alamat: String, // e.g. "Blok C No. 12"
    val rt: String = "RT 03",
    val rw: String = "RW 02",
    val peranKeluarga: String = "Kepala Keluarga", // "Kepala Keluarga", "Anggota", "Tokoh Warga"
    val telepon: String = "0812-3456-7890",
    val isOnline: Boolean = false,
    val statusText: String = "Offline", // "Online", "Aktif 10m lalu", "Aktif 1 jam lalu", "Offline"
    val pekerjaan: String = "Wiraswasta",
    val statusIuranBulanIni: Boolean = true
)

@Entity(tableName = "asset_rw")
data class AssetRwEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val kodeAset: String, // e.g. "AST-RW-001"
    val namaAset: String, // e.g. "Tenda Terop Pesta (4x6m)"
    val kategori: String, // "Fasilitas Umum", "Peralatan Acara", "Keamanan / Ronda", "Kebersihan & Taman", "Elektronik & Sound"
    val jumlah: Int = 1,
    val satuan: String = "Unit", // "Unit", "Set", "Buah", "Pasang"
    val kondisi: String = "Baik", // "Baik", "Perlu Perbaikan", "Rusak"
    val lokasiPenyimpanan: String = "Gudang Balai RW 02",
    val penanggungJawab: String = "Seksi Perlengkapan (Pak Heru)",
    val statusKetersediaan: String = "Tersedia", // "Tersedia", "Dipinjam", "Dalam Pemeliharaan"
    val nilaiPerolehan: Long = 0L,
    val tahunPengadaan: String = "2024",
    val catatan: String = "",
    val bisaDipinjam: Boolean = true
)



