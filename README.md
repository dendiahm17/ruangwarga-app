# RuangWarga App (`ruangwarga-app`)

Aplikasi Mobile Layanan Digital Warga RT/RW modern berbasis Android (Jetpack Compose) yang dirancang untuk memudahkan komunikasi, transparansi keuangan, dan pelayanan warga secara terpadu.

---

## ✨ Fitur Utama

- 📢 **Template Banner & Latar Interaktif Postingan**: Pembuatan postingan dengan tema visual yang dinamis (Kerja Bakti, Senam, Musyawarah, Pengumuman, Posyandu, Donasi, dll).
- 🚨 **Peringatan Dini & Status Darurat Siaga Warga**: Sistem notifikasi darurat (Banjir, Kebakaran, Medis) dengan tombol aksi relawan langsung.
- 💰 **Laporan Keuangan & Buku Kas Transparan**: Rekapitulasi kas masuk/keluar RW, saldo riil, serta visualisasi grafik pemasukan dan pengeluaran.
- 📦 **Manajemen & Peminjaman Aset RW**: Inventarisasi barang/fasilitas warga beserta status ketersediaan dan form peminjaman.
- 📂 **Katalog Layanan Digital Warga**: Pengajuan surat pengantar, pembayaran iuran kas/sampah, pengaduan lingkungan, dan direktori data warga.
- 🗳️ **Polling & Musyawarah Warga**: Fitur voting interaktif untuk pengambilan keputusan bersama.

---

## 🛠️ Menjalankan Aplikasi

### Prasyarat
- [Android Studio](https://developer.android.com/studio) (Koala / Ladybug atau versi terbaru)
- Android SDK (API 34/36)
- JDK 17 atau JDK 21

### Langkah Instalasi
1. Buka folder proyek ini di **Android Studio**.
2. Tunggu proses Gradle Sync selesai.
3. Hubungkan perangkat fisik Android melalui USB Debugging atau jalankan Android Virtual Device (AVD).
4. Klik tombol **Run** atau jalankan perintah:
   ```bash
   ./gradlew installDebug
   ```

---

## 📱 Arsitektur & Teknologi

- **UI Framework**: Jetpack Compose & Material 3
- **Database Lokal**: Android Room Database (SQLite)
- **Asinkron & Reaktif**: Kotlin Coroutines & StateFlow
- **Image Loading**: Coil Compose
- **Design & Layout**: Pure Solid Design with Modern Glassmorphism Accent
