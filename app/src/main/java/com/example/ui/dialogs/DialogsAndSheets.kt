package com.example.ui.dialogs

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.HourglassTop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.AnnouncementRecordEntity
import com.example.data.model.ComplaintRecordEntity
import com.example.data.model.DuesRecordEntity
import com.example.data.model.FamilyMemberEntity
import com.example.data.model.LetterRequestEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.ResidentProfileEntity
import com.example.data.model.RondaScheduleEntity
import com.example.ui.components.StatusBadge
import com.example.ui.components.launchPhoneDialer
import com.example.ui.components.launchWhatsApp
import com.example.ui.components.shareOfficialDocument
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentGreenDark
import com.example.ui.theme.AccentGreenLight
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentOrangeLight
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentPurpleLight
import com.example.ui.theme.AccentRed
import com.example.ui.theme.AccentRedLight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryBlueDark
import com.example.ui.theme.PrimaryBlueLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateLetterBottomSheet(
    preselectedType: String = "",
    onDismiss: () -> Unit,
    onSubmit: (jenisSurat: String, keperluan: String, keteranganTambahan: String) -> Unit
) {
    val letterOptions = listOf(
        "Surat Pengantar",
        "Surat Keterangan Domisili",
        "Surat Keterangan Usaha",
        "Surat Keterangan Tidak Mampu",
        "Surat Kelahiran",
        "Surat Kematian",
        "Surat Pindah",
        "Surat Pengantar SKCK",
        "Surat Lainnya"
    )

    var expandedDropdown by remember { mutableStateOf(false) }
    var selectedType by remember {
        mutableStateOf(if (preselectedType.isNotBlank() && letterOptions.contains(preselectedType)) preselectedType else letterOptions[0])
    }
    var keperluan by remember { mutableStateOf("") }
    var keterangan by remember { mutableStateOf("") }

    val isKeperluanValid = keperluan.trim().length >= 5
    val hasAttemptedSubmit by remember { mutableStateOf(false) }

    Dialog(properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false), onDismissRequest = onDismiss) {
        
        Column(
            modifier = Modifier.fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ajukan Permohonan Surat",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Dropdown Jenis Surat
            Text(
                text = "Jenis Surat *",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))

            ExposedDropdownMenuBox(
                expanded = expandedDropdown,
                onExpandedChange = { expandedDropdown = !expandedDropdown }
            ) {
                OutlinedTextField(
                    value = selectedType,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .testTag("dropdown_jenis_surat"),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = expandedDropdown,
                    onDismissRequest = { expandedDropdown = false }
                ) {
                    letterOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedType = option
                                expandedDropdown = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Keperluan Permohonan *",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = keperluan,
                onValueChange = { keperluan = it },
                placeholder = { Text("Contoh: Persyaratan melamar pekerjaan / buka rekening") },
                isError = keperluan.isNotBlank() && !isKeperluanValid,
                supportingText = {
                    if (keperluan.isNotBlank() && !isKeperluanValid) {
                        Text("Minimal 5 karakter", color = AccentRed, fontSize = 11.sp)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_keperluan_surat"),
                shape = RoundedCornerShape(12.dp),
                minLines = 2
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Keterangan Tambahan / Catatan (Opsional)",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = keterangan,
                onValueChange = { keterangan = it },
                placeholder = { Text("Tambahkan informasi pendukung jika diperlukan") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_keterangan_surat"),
                shape = RoundedCornerShape(12.dp),
                minLines = 2
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Notice Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Pengajuan akan diverifikasi oleh pengurus RT 03 dalam kurun waktu 1x24 jam.",
                        fontSize = 12.sp,
                        color = PrimaryBlueDark,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (isKeperluanValid) {
                        onSubmit(selectedType, keperluan.trim(), keterangan.trim())
                    }
                },
                enabled = isKeperluanValid,
                colors = ButtonDefaults.buttonColors(containerColor = AccentRed), shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("btn_submit_surat")
            ) {
                Text(text = "Kirim Permohonan Surat", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LetterDetailBottomSheet(
    letter: LetterRequestEntity,
    profile: ResidentProfileEntity,
    isAdminMode: Boolean = false,
    onDismiss: () -> Unit,
    onAdminApprove: (() -> Unit)? = null,
    onAdminProcess: (() -> Unit)? = null,
    onAdminReject: (() -> Unit)? = null
) {
    val context = LocalContext.current

    val officialLetterText = """
        PEMERINTAH KOTA ADMINISTRASI
        RUKUN TETANGGA 03 / RUKUN WARGA 02
        Kelurahan Sukamaju, Kecamatan Bersama
        ========================================
        Nomor: ${letter.nomorSurat}
        Perihal: ${letter.jenisSurat}
        
        Yang bertanda tangan di bawah ini Ketua RT 03 / RW 02 menerangkan bahwa:
        Nama: ${profile.nama}
        NIK: ${profile.nik}
        No. KK: ${profile.noKk}
        Alamat: ${profile.alamat} ${profile.rt} / ${profile.rw}
        Pekerjaan: ${profile.pekerjaan}
        
        Menerangkan dengan sebenarnya bahwa yang bersangkutan mengajukan permohonan untuk keperluan:
        "${letter.keperluan}"
        ${if (letter.keteranganTambahan.isNotBlank()) "Catatan: " + letter.keteranganTambahan else ""}
        
        Status: ${letter.status.uppercase()}
        Tanggal Pengajuan: ${letter.tanggalPengajuan}
        ${if (letter.tanggalSelesai != null) "Tanggal Disetujui: " + letter.tanggalSelesai else ""}
        Catatan RT: ${letter.catatanRt ?: "-"}
        
        Dokumen ini diterbitkan secara digital & sah melalui Sistem RuangWarga.
    """.trimIndent()

    Dialog(properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false), onDismissRequest = onDismiss) {
        
        Column(
            modifier = Modifier.fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pratinjau Surat Digital",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Official Letter Frame
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderLight, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // Header Kop Surat
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "RUKUN TETANGGA 03 / RUKUN WARGA 02",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlueDark,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "KELURAHAN SUKAMAJU, KECAMATAN BERSAMA",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(thickness = 2.dp, color = PrimaryBlue)
                        Spacer(modifier = Modifier.height(2.dp))
                        HorizontalDivider(thickness = 0.5.dp, color = BorderLight)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = letter.jenisSurat,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "No: ${letter.nomorSurat}",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                        StatusBadge(status = letter.status)
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = BorderLight)
                    Spacer(modifier = Modifier.height(14.dp))

                    Text(text = "Nama Pemohon: ${profile.nama}", fontSize = 12.sp, color = TextPrimary)
                    Text(text = "NIK: ${profile.nik}", fontSize = 12.sp, color = TextPrimary)
                    Text(text = "Alamat: ${profile.alamat} ${profile.rt} / ${profile.rw}", fontSize = 12.sp, color = TextPrimary)

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Keperluan:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        text = letter.keperluan,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF1F5F9), RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    )

                    if (letter.keteranganTambahan.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Keterangan: ${letter.keteranganTambahan}", fontSize = 11.sp, color = TextSecondary)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Catatan RT / Digital Stamp
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (letter.status == "Selesai") AccentGreenLight else Color(0xFFFFFBEB),
                                RoundedCornerShape(10.dp)
                            )
                            .padding(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (letter.status == "Selesai") Icons.Default.CheckCircle else Icons.Default.HourglassTop,
                                contentDescription = null,
                                tint = if (letter.status == "Selesai") AccentGreenDark else AccentOrange,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = if (letter.status == "Selesai") "Tervalidasi & Ditandatangani Ketua RT" else "Status Pengurusan: ${letter.status}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (letter.status == "Selesai") AccentGreenDark else AccentOrange
                                )
                                letter.catatanRt?.let {
                                    Text(text = it, fontSize = 11.sp, color = TextSecondary)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action Buttons: Share & Download PDF Document
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        val pdfFile = com.example.utils.DocumentPdfGenerator.generateLetterPdf(context, letter, profile)
                        if (pdfFile != null) {
                            com.example.utils.DocumentPdfGenerator.openOrSharePdf(
                                context,
                                pdfFile,
                                "Surat Pengantar ${letter.jenisSurat} - ${profile.nama}"
                            )
                        } else {
                            shareOfficialDocument(context, "Surat Resmi - ${letter.jenisSurat}", officialLetterText)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Bagikan PDF", fontSize = 13.sp)
                }

                OutlinedButton(
                    onClick = {
                        val pdfFile = com.example.utils.DocumentPdfGenerator.generateLetterPdf(context, letter, profile)
                        if (pdfFile != null) {
                            com.example.utils.DocumentPdfGenerator.openOrSharePdf(
                                context,
                                pdfFile,
                                "Cetak / Buka PDF Surat"
                            )
                        } else {
                            Toast.makeText(context, "Gagal membuat dokumen PDF", Toast.LENGTH_SHORT).show()
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Unduh / Cetak", fontSize = 13.sp)
                }
            }

            // Admin Controls if in Admin Mode
            if (isAdminMode) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.AdminPanelSettings, contentDescription = null, tint = Color(0xFF856404), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Kontrol Pengurus RT", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF856404))
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { onAdminApprove?.invoke() },
                                colors = ButtonDefaults.buttonColors(containerColor = AccentRed), shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Setujui", fontSize = 12.sp)
                            }
                            Button(
                                onClick = { onAdminProcess?.invoke() },
                                colors = ButtonDefaults.buttonColors(containerColor = AccentRed), shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Proses", fontSize = 12.sp)
                            }
                            Button(
                                onClick = { onAdminReject?.invoke() },
                                colors = ButtonDefaults.buttonColors(containerColor = AccentRed), shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Tolak", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateComplaintBottomSheet(
    onDismiss: () -> Unit,
    onSubmit: (judul: String, lokasi: String, kategori: String, deskripsi: String, fotoBukti: String?) -> Unit
) {
    val categories = listOf("Fasilitas Umum", "Kebersihan", "Keamanan", "Infrastruktur", "Lainnya")
    val photoPresets = listOf("Foto Bukti Lapangan", "Foto Lampu Padam", "Foto Tumpukan Sampah", "Foto Jalan Berlubang", "Foto Saluran Air")

    var judul by remember { mutableStateOf("") }
    var lokasi by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf(categories[0]) }
    var deskripsi by remember { mutableStateOf("") }
    var selectedPhoto by remember { mutableStateOf<String?>(null) }
    var showPhotoSelector by remember { mutableStateOf(false) }

    val isJudulValid = judul.trim().length >= 4
    val isLokasiValid = lokasi.trim().length >= 3
    val isDeskripsiValid = deskripsi.trim().length >= 8
    val isValid = isJudulValid && isLokasiValid && isDeskripsiValid

    Dialog(properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false), onDismissRequest = onDismiss) {
        
        Column(
            modifier = Modifier.fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Buat Pengaduan Warga",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(text = "Judul Laporan *", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = judul,
                onValueChange = { judul = it },
                placeholder = { Text("Contoh: Lampu Jalan Padam di Blok C") },
                isError = judul.isNotBlank() && !isJudulValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_judul_pengaduan"),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = "Lokasi Kejadian *", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = lokasi,
                onValueChange = { lokasi = it },
                placeholder = { Text("Contoh: Depan rumah Blok C No. 12") },
                isError = lokasi.isNotBlank() && !isLokasiValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_lokasi_pengaduan"),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = "Kategori Masalah", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                categories.take(3).forEach { cat ->
                    val isSel = kategori == cat
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSel) PrimaryBlueLight else Color(0xFFF1F5F9))
                            .border(1.dp, if (isSel) PrimaryBlue else Color.Transparent, RoundedCornerShape(10.dp))
                            .clickable { kategori = cat }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = cat,
                            fontSize = 11.sp,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSel) PrimaryBlue else TextPrimary
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                categories.drop(3).forEach { cat ->
                    val isSel = kategori == cat
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSel) PrimaryBlueLight else Color(0xFFF1F5F9))
                            .border(1.dp, if (isSel) PrimaryBlue else Color.Transparent, RoundedCornerShape(10.dp))
                            .clickable { kategori = cat }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = cat,
                            fontSize = 11.sp,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSel) PrimaryBlue else TextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = "Deskripsi / Rincian Masalah *", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = deskripsi,
                onValueChange = { deskripsi = it },
                placeholder = { Text("Jelaskan rincian masalah, kondisi, dan dampaknya bagi warga") },
                isError = deskripsi.isNotBlank() && !isDeskripsiValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_deskripsi_pengaduan"),
                shape = RoundedCornerShape(12.dp),
                minLines = 3
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Lampiran Foto Nyata dengan Photo Picker Android
            Text(text = "Lampiran Foto Bukti (Opsional)", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Spacer(modifier = Modifier.height(6.dp))

            val photoPickerLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
                contract = androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia()
            ) { uri: android.net.Uri? ->
                if (uri != null) {
                    selectedPhoto = uri.toString()
                }
            }

            if (selectedPhoto != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = AccentGreen, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (selectedPhoto!!.startsWith("content://")) "Foto Bukti Terpilih (${selectedPhoto!!.takeLast(14)})" else selectedPhoto!!,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextPrimary,
                                maxLines = 1
                            )
                        }
                        IconButton(onClick = { selectedPhoto = null }, modifier = Modifier.size(24.dp)) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Hapus", tint = TextSecondary, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            photoPickerLauncher.launch(
                                androidx.activity.result.PickVisualMediaRequest(androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.AddPhotoAlternate, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Buka Galeri HP", color = PrimaryBlue, fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = { showPhotoSelector = !showPhotoSelector },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Contoh", color = TextSecondary, fontSize = 12.sp)
                    }
                }
            }

            if (showPhotoSelector) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White), border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "Pilih Contoh Foto Bukti:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                        Spacer(modifier = Modifier.height(6.dp))
                        photoPresets.forEach { preset ->
                            Text(
                                text = "📷 $preset",
                                fontSize = 12.sp,
                                color = PrimaryBlue,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedPhoto = preset
                                        showPhotoSelector = false
                                    }
                                    .padding(vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (isValid) {
                        onSubmit(judul.trim(), lokasi.trim(), kategori, deskripsi.trim(), selectedPhoto)
                    }
                },
                enabled = isValid,
                colors = ButtonDefaults.buttonColors(containerColor = AccentRed), shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("btn_submit_pengaduan")
            ) {
                Text(text = "Kirim Pengaduan", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComplaintDetailBottomSheet(
    complaint: ComplaintRecordEntity,
    isAdminMode: Boolean = false,
    onDismiss: () -> Unit,
    onAdminUpdate: ((newStatus: String, tanggapan: String) -> Unit)? = null
) {
    var showAdminDialog by remember { mutableStateOf(false) }
    var adminStatus by remember { mutableStateOf(complaint.status) }
    var adminTanggapan by remember { mutableStateOf(complaint.tanggapanRt ?: "") }

    Dialog(properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false), onDismissRequest = onDismiss) {
        
        Column(
            modifier = Modifier.fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Detail Pengaduan",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = complaint.judul,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                StatusBadge(status = complaint.status)
            }

            Text(
                text = "${complaint.tanggal} • ${complaint.waktu}",
                fontSize = 11.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = BorderLight)
            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = AccentRed, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Lokasi: ${complaint.lokasi}", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Kategori: ${complaint.kategori}", fontSize = 12.sp, color = TextSecondary)

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Deskripsi Masalah:", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = complaint.deskripsi,
                fontSize = 13.sp,
                color = TextSecondary,
                lineHeight = 18.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8FAFC), RoundedCornerShape(10.dp))
                    .padding(12.dp)
            )

            if (complaint.fotoBukti != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Foto Bukti Terlampir:", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                Spacer(modifier = Modifier.height(4.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.AddPhotoAlternate, contentDescription = null, tint = PrimaryBlue)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = complaint.fotoBukti ?: "", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = PrimaryBlueDark)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tanggapan RT
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (complaint.status == "Selesai") Icons.Default.CheckCircle else Icons.Default.Engineering,
                            contentDescription = null,
                            tint = if (complaint.status == "Selesai") AccentGreenDark else AccentOrange,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Tanggapan Pengurus RT",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (complaint.status == "Selesai") AccentGreenDark else AccentOrange
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = complaint.tanggapanRt ?: "Pengurus RT sedang memverifikasi laporan ini.",
                        fontSize = 12.sp,
                        color = TextPrimary,
                        lineHeight = 16.sp
                    )
                }
            }

            // Admin action button
            if (isAdminMode) {
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { showAdminDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed), shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Icon(imageVector = Icons.Default.AdminPanelSettings, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Perbarui Status & Tanggapan RT")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    if (showAdminDialog) {
        AlertDialog(
            onDismissRequest = { showAdminDialog = false },
            title = { Text("Tanggapi Pengaduan (Pengurus RT)") },
            text = {
                Column {
                    Text("Pilih Status:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Diproses", "Selesai").forEach { st ->
                            Button(
                                onClick = { adminStatus = st },
                                colors = ButtonDefaults.buttonColors(containerColor = AccentRed), shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(st, color = if (adminStatus == st) Color.White else TextPrimary, fontSize = 12.sp)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Tanggapan / Catatan RT:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = adminTanggapan,
                        onValueChange = { adminTanggapan = it },
                        placeholder = { Text("Contoh: Petugas telah memperbaiki lampu jalan.") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        minLines = 2
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onAdminUpdate?.invoke(adminStatus, adminTanggapan)
                        showAdminDialog = false
                    }
                ) {
                    Text("Simpan Pembaruan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAdminDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayDuesBottomSheet(
    onDismiss: () -> Unit,
    onPay: (periodeBulan: String, metode: String, buktiBayar: String?) -> Unit
) {
    val months = listOf("Mei 2026", "Juni 2026", "Juli 2026", "Agustus 2026")
    val methods = listOf("QRIS", "Transfer Bank", "Tunai ke Bendahara")

    var selectedMonth by remember { mutableStateOf(months[0]) }
    var selectedMethod by remember { mutableStateOf(methods[0]) }
    var buktiBayarSelected by remember { mutableStateOf<String?>(null) }

    Dialog(properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false), onDismissRequest = onDismiss) {
        
        Column(
            modifier = Modifier.fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pembayaran Iuran Kas",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Total Tagihan Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Total Nominal Iuran", fontSize = 12.sp, color = PrimaryBlueDark)
                    Text(text = "Rp 20.000", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                    Text(text = "Untuk alokasi kebersihan lingkungan & pos keamanan", fontSize = 11.sp, color = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Pilih Periode Bulan", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                months.take(2).forEach { m ->
                    val isSel = selectedMonth == m
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSel) PrimaryBlueLight else Color(0xFFF1F5F9))
                            .border(1.dp, if (isSel) PrimaryBlue else Color.Transparent, RoundedCornerShape(10.dp))
                            .clickable { selectedMonth = m }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = m,
                            fontSize = 12.sp,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSel) PrimaryBlue else TextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(text = "Pilih Metode Pembayaran", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Spacer(modifier = Modifier.height(6.dp))

            methods.forEach { met ->
                val isSel = selectedMethod == met
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { selectedMethod = met },
                    colors = CardDefaults.cardColors(containerColor = Color.White), border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSel) PrimaryBlue else BorderLight
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSel,
                            onClick = { selectedMethod = met },
                            colors = RadioButtonDefaults.colors(selectedColor = PrimaryBlue)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = met, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(
                                text = when (met) {
                                    "QRIS" -> "Scan otomatis via GoPay, OVO, Dana, BCA, Mandiri"
                                    "Transfer Bank" -> "BCA 8720-1234-56 (Kas RT 03 Joko)"
                                    else -> "Serahkan langsung ke Bendahara RT saat bertugas"
                                },
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            if (selectedMethod == "QRIS") {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White), border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(imageVector = Icons.Default.QrCode2, contentDescription = "QRIS", tint = TextPrimary, modifier = Modifier.size(90.dp))
                        Text(text = "NMID: ID10203040506070 (KAS RT 03)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(text = "Screenshot QR di atas untuk membayar via aplikasi m-banking Anda", fontSize = 10.sp, color = TextSecondary, textAlign = TextAlign.Center)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Bukti Pembayaran dengan Photo Picker
            Text(text = "Bukti Bayar / Screenshot Transfer", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Spacer(modifier = Modifier.height(6.dp))

            val duesPhotoPickerLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
                contract = androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia()
            ) { uri: android.net.Uri? ->
                if (uri != null) {
                    buktiBayarSelected = uri.toString()
                }
            }

            if (buktiBayarSelected != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AccentGreenLight, RoundedCornerShape(10.dp))
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (buktiBayarSelected!!.startsWith("content://")) "✓ Struk Foto Terlampir (${buktiBayarSelected!!.takeLast(12)})" else "✓ $buktiBayarSelected",
                        fontSize = 12.sp,
                        color = AccentGreenDark,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    IconButton(onClick = { buktiBayarSelected = null }, modifier = Modifier.size(20.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            duesPhotoPickerLauncher.launch(
                                androidx.activity.result.PickVisualMediaRequest(androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.AddPhotoAlternate, contentDescription = null, tint = PrimaryBlue)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Pilih Struk dari Galeri", color = PrimaryBlue, fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = {
                            buktiBayarSelected = "Bukti_Trf_${selectedMonth.replace(" ", "")}.png"
                        },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Auto Bukti", color = TextSecondary, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    onPay(selectedMonth, selectedMethod, buktiBayarSelected)
                },
                colors = ButtonDefaults.buttonColors(containerColor = AccentRed), shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("btn_confirm_pay_dues")
            ) {
                Text(text = "Konfirmasi Pembayaran (Rp 20.000)", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DuesDetailBottomSheet(
    dues: DuesRecordEntity,
    profile: ResidentProfileEntity,
    isAdminMode: Boolean = false,
    onDismiss: () -> Unit,
    onAdminToggleStatus: ((makeLunas: Boolean) -> Unit)? = null
) {
    val context = LocalContext.current

    val receiptText = """
        KAS RUKUN TETANGGA 03 / RUKUN WARGA 02
        BUKTI KUITANSI IURAN WARGA
        ========================================
        Kode Transaksi: ${dues.kodeTransaksi ?: "RTRW-202605-0042"}
        Periode: ${dues.periodeBulan}
        Tanggal Bayar: ${dues.tanggalBayar ?: "10 Mei 2026"}
        Metode: ${dues.metodePembayaran ?: "QRIS"}
        
        Data Warga:
        Nama: ${profile.nama}
        No. KK: ${profile.noKk}
        Alamat: ${profile.alamat} ${profile.rt} / ${profile.rw}
        
        Total Iuran: Rp ${String.format("%,d", dues.jumlah).replace(',', '.')}
        Status: ${dues.status.uppercase()}
        
        Kuitansi ini diterbitkan secara sah oleh Sistem RuangWarga.
    """.trimIndent()

    Dialog(properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false), onDismissRequest = onDismiss) {
        
        Column(
            modifier = Modifier.fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Bukti Kuitansi Iuran",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderLight, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "KAS WARGA RT 03 / RW 02",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        )
                        StatusBadge(status = dues.status)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Iuran Bulan ${dues.periodeBulan}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Kode Transaksi: ${dues.kodeTransaksi ?: "RTRW-202605-0042"}",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = BorderLight)
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "Nama Pembayar: ${profile.nama}", fontSize = 12.sp, color = TextPrimary)
                    Text(text = "Alamat: ${profile.alamat}", fontSize = 12.sp, color = TextPrimary)
                    Text(text = "Metode: ${dues.metodePembayaran ?: "QRIS"}", fontSize = 12.sp, color = TextPrimary)
                    Text(text = "Tanggal Bayar: ${dues.tanggalBayar ?: "10 Mei 2026"}", fontSize = 12.sp, color = TextPrimary)

                    if (dues.buktiBayar != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Bukti Transfer: ${dues.buktiBayar}", fontSize = 11.sp, color = PrimaryBlueDark)
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = BorderLight)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Jumlah Dibayar", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(
                            text = "Rp ${String.format("%,d", dues.jumlah).replace(',', '.')}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentGreenDark
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        shareOfficialDocument(context, "Kuitansi Iuran ${dues.periodeBulan}", receiptText)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed), shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Bagikan Kuitansi", fontSize = 13.sp)
                }

                OutlinedButton(
                    onClick = {
                        Toast.makeText(context, "Kuitansi berhasil diunduh ke galeri!", Toast.LENGTH_SHORT).show()
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Unduh Gambar", fontSize = 13.sp)
                }
            }

            if (isAdminMode) {
                Spacer(modifier = Modifier.height(14.dp))
                val isCurrentlyLunas = dues.status.equals("Lunas", ignoreCase = true)
                Button(
                    onClick = {
                        onAdminToggleStatus?.invoke(!isCurrentlyLunas)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed), shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = if (isCurrentlyLunas) "Tandai Belum Lunas (Pengurus RT)" else "Verifikasi & Tandai Lunas (Pengurus RT)",
                        color = if (isCurrentlyLunas) TextPrimary else Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamilyProfileBottomSheet(
    profile: ResidentProfileEntity,
    familyMembers: List<FamilyMemberEntity>,
    onDismiss: () -> Unit,
    onAddMember: (FamilyMemberEntity) -> Unit,
    onDeleteMember: ((FamilyMemberEntity) -> Unit)? = null
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var namaBaru by remember { mutableStateOf("") }
    var nikBaru by remember { mutableStateOf("") }
    var hubunganBaru by remember { mutableStateOf("Anak") }
    var ttlBaru by remember { mutableStateOf("") }
    var pekerjaanBaru by remember { mutableStateOf("") }

    val isNikValid = nikBaru.length == 16 && nikBaru.all { it.isDigit() }
    val isNamaValid = namaBaru.trim().isNotBlank()

    Dialog(properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false), onDismissRequest = onDismiss) {
        
        Column(
            modifier = Modifier.fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Data Kartu Keluarga (KK)",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // KK Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Nomor KK: ${profile.noKk}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                    Text(text = "Kepala Keluarga: ${profile.nama}", fontSize = 13.sp, color = TextPrimary)
                    Text(text = "Alamat: ${profile.alamat} ${profile.rt} / ${profile.rw}", fontSize = 12.sp, color = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Daftar Anggota Keluarga (${familyMembers.size})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                TextButton(onClick = { showAddDialog = true }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Tambah Anggota", fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            familyMembers.forEach { member ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White), border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = member.nama, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(text = "NIK: ${member.nik}", fontSize = 12.sp, color = TextSecondary)
                            Text(text = "${member.tempatTanggalLahir} • ${member.pekerjaan}", fontSize = 11.sp, color = TextTertiary)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            StatusBadge(status = member.hubungan)
                            if (onDeleteMember != null && member.hubungan != "Kepala Keluarga") {
                                Spacer(modifier = Modifier.width(4.dp))
                                IconButton(
                                    onClick = { onDeleteMember(member) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Hapus",
                                        tint = AccentRed,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Tambah Anggota Keluarga") },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text("Nama Lengkap *", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = namaBaru,
                        onValueChange = { namaBaru = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("NIK (16 Digit) *", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = nikBaru,
                        onValueChange = { if (it.length <= 16) nikBaru = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = nikBaru.isNotBlank() && !isNikValid,
                        supportingText = {
                            if (nikBaru.isNotBlank() && !isNikValid) {
                                Text("Harus 16 digit angka (${nikBaru.length}/16)", color = AccentRed, fontSize = 10.sp)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Hubungan Keluarga", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("Istri", "Anak", "Orang Tua", "Lainnya").forEach { rel ->
                            val isSel = hubunganBaru == rel
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) PrimaryBlueLight else Color(0xFFF1F5F9))
                                    .clickable { hubunganBaru = rel }
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Text(rel, fontSize = 11.sp, color = if (isSel) PrimaryBlue else TextPrimary)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Tempat, Tanggal Lahir", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = ttlBaru,
                        onValueChange = { ttlBaru = it },
                        placeholder = { Text("Jakarta, 01 Jan 2000") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Pekerjaan", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = pekerjaanBaru,
                        onValueChange = { pekerjaanBaru = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (isNamaValid && isNikValid) {
                            onAddMember(
                                FamilyMemberEntity(
                                    nama = namaBaru.trim(),
                                    nik = nikBaru.trim(),
                                    hubungan = hubunganBaru,
                                    tempatTanggalLahir = if (ttlBaru.isBlank()) "Jakarta, 01 Jan 2000" else ttlBaru,
                                    jenisKelamin = if (hubunganBaru == "Istri") "Perempuan" else "Laki-laki",
                                    pekerjaan = if (pekerjaanBaru.isBlank()) "Pelajar" else pekerjaanBaru
                                )
                            )
                            showAddDialog = false
                        }
                    },
                    enabled = isNamaValid && isNikValid
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyContactsBottomSheet(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val contacts = listOf(
        Pair("Ketua RT 03 (Pak Joko)", "081298765432"),
        Pair("Ketua RW 02 (Pak Hendra)", "081387654321"),
        Pair("Pos Satpam & Keamanan RT", "081122334455"),
        Pair("Bhabinkamtibmas", "081233445566"),
        Pair("Babinsa Kelurahan", "081344556677"),
        Pair("Damkar (Pemadam)", "113"),
        Pair("Puskesmas / Ambulans", "119")
    )

    Dialog(properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false), onDismissRequest = onDismiss) {
        
        Column(
            modifier = Modifier.fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Kontak Darurat & Pengurus",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            contacts.forEach { (name, phone) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White), border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(text = phone, fontSize = 13.sp, color = PrimaryBlue, fontWeight = FontWeight.Medium)
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Direct Dial Button
                            IconButton(
                                onClick = {
                                    launchPhoneDialer(context, phone)
                                },
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(AccentGreenLight)
                            ) {
                                Icon(imageVector = Icons.Default.Phone, contentDescription = "Panggil", tint = AccentGreenDark, modifier = Modifier.size(18.dp))
                            }

                            // Direct WhatsApp Button if it's mobile number
                            if (phone.length >= 10) {
                                IconButton(
                                    onClick = {
                                        launchWhatsApp(context, phone, "Halo $name, saya Budi Santoso warga RT 03 ingin menghubungi.")
                                    },
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(PrimaryBlueLight)
                                ) {
                                    Icon(imageVector = Icons.AutoMirrored.Filled.Chat, contentDescription = "WhatsApp", tint = PrimaryBlue, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SiskamlingScheduleBottomSheet(
    schedules: List<RondaScheduleEntity>,
    onDismiss: () -> Unit,
    onConfirmAttendance: (id: Int, status: String) -> Unit = { _, _ -> }
) {
    Dialog(properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false), onDismissRequest = onDismiss) {
        
        Column(
            modifier = Modifier.fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Jadwal Ronda & Siskamling",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = Color(0xFF38BDF8), modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "Pos Kamling RT 03", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(text = "Jam Operasional: 22:00 - 04:00 WIB", fontSize = 12.sp, color = Color(0xFF94A3B8))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Jadwal Giliran Ronda Pekan Ini", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))

            schedules.forEach { sch ->
                val isMySchedule = sch.petugasList.contains("Budi Santoso")
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White), border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isMySchedule) PrimaryBlue else BorderLight
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "${sch.hari}, ${sch.tanggal}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                if (isMySchedule) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(PrimaryBlue)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text("Jadwal Saya", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                            StatusBadge(status = sch.statusKehadiranSaya)
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "Petugas: ${sch.petugasList}", fontSize = 12.sp, color = TextSecondary)
                        Text(text = "Catatan: ${sch.catatan}", fontSize = 11.sp, color = TextTertiary)

                        if (isMySchedule) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { onConfirmAttendance(sch.id, "Hadir") },
                                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed), shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Konfirmasi Hadir", fontSize = 11.sp)
                                }
                                OutlinedButton(
                                    onClick = { onConfirmAttendance(sch.id, "Izin") },
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Izin / Berhalangan", fontSize = 11.sp, color = AccentRed)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDataBottomSheet(
    profile: ResidentProfileEntity,
    onDismiss: () -> Unit,
    onSave: (ResidentProfileEntity) -> Unit
) {
    var nama by remember { mutableStateOf(profile.nama) }
    var nik by remember { mutableStateOf(profile.nik) }
    var noKk by remember { mutableStateOf(profile.noKk) }
    var alamat by remember { mutableStateOf(profile.alamat) }
    var telepon by remember { mutableStateOf(profile.telepon) }
    var email by remember { mutableStateOf(profile.email) }
    var pekerjaan by remember { mutableStateOf(profile.pekerjaan) }

    val isNikValid = nik.length == 16 && nik.all { it.isDigit() }
    val isNoKkValid = noKk.length == 16 && noKk.all { it.isDigit() }
    val isTeleponValid = telepon.length >= 10
    val isNamaValid = nama.trim().isNotBlank()
    val isValid = isNikValid && isNoKkValid && isTeleponValid && isNamaValid

    Dialog(properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false), onDismissRequest = onDismiss) {
        
        Column(
            modifier = Modifier.fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ubah Data Diri Warga",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(text = "Nama Lengkap *", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            OutlinedTextField(
                value = nama,
                onValueChange = { nama = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(text = "NIK (16 Digit) *", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            OutlinedTextField(
                value = nik,
                onValueChange = { if (it.length <= 16) nik = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = nik.isNotBlank() && !isNikValid,
                supportingText = {
                    if (nik.isNotBlank() && !isNikValid) {
                        Text("NIK harus berupa 16 digit angka (${nik.length}/16)", color = AccentRed, fontSize = 11.sp)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(text = "No. KK (16 Digit) *", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            OutlinedTextField(
                value = noKk,
                onValueChange = { if (it.length <= 16) noKk = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = noKk.isNotBlank() && !isNoKkValid,
                supportingText = {
                    if (noKk.isNotBlank() && !isNoKkValid) {
                        Text("No. KK harus berupa 16 digit angka (${noKk.length}/16)", color = AccentRed, fontSize = 11.sp)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(text = "Alamat Lengkap *", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            OutlinedTextField(
                value = alamat,
                onValueChange = { alamat = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(text = "Nomor Telepon / WhatsApp *", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            OutlinedTextField(
                value = telepon,
                onValueChange = { telepon = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                isError = telepon.isNotBlank() && !isTeleponValid,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(text = "Email", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(text = "Pekerjaan", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            OutlinedTextField(
                value = pekerjaan,
                onValueChange = { pekerjaan = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (isValid) {
                        onSave(
                            profile.copy(
                                nama = nama.trim(),
                                nik = nik.trim(),
                                noKk = noKk.trim(),
                                alamat = alamat.trim(),
                                telepon = telepon.trim(),
                                email = email.trim(),
                                pekerjaan = pekerjaan.trim()
                            )
                        )
                    }
                },
                enabled = isValid,
                colors = ButtonDefaults.buttonColors(containerColor = AccentRed), shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Simpan Perubahan", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsBottomSheet(
    notifications: List<NotificationEntity>,
    onDismiss: () -> Unit,
    onClearAll: (() -> Unit)? = null
) {
    Dialog(properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false), onDismissRequest = onDismiss) {
        
        Column(
            modifier = Modifier.fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pemberitahuan Warga",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (notifications.isNotEmpty() && onClearAll != null) {
                        TextButton(onClick = { onClearAll() }) {
                            Text("Hapus Semua", fontSize = 12.sp, color = AccentRed)
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (notifications.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Tidak ada notifikasi baru.", color = TextSecondary)
                }
            } else {
                notifications.forEach { notif ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White), border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when (notif.tipe) {
                                            "surat" -> PrimaryBlueLight
                                            "iuran" -> AccentGreenLight
                                            "pengaduan" -> AccentOrangeLight
                                            "siskamling" -> Color(0xFFE2E8F0)
                                            else -> AccentPurpleLight
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = when (notif.tipe) {
                                        "surat" -> Icons.Default.Description
                                        "iuran" -> Icons.Default.Payments
                                        "pengaduan" -> Icons.Default.Engineering
                                        "siskamling" -> Icons.Default.Security
                                        else -> Icons.Default.Notifications
                                    },
                                    contentDescription = null,
                                    tint = when (notif.tipe) {
                                        "surat" -> PrimaryBlue
                                        "iuran" -> AccentGreenDark
                                        "pengaduan" -> AccentOrange
                                        "siskamling" -> Color(0xFF334155)
                                        else -> AccentPurple
                                    },
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = notif.judul,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = notif.pesan,
                                    fontSize = 12.sp,
                                    color = TextSecondary,
                                    lineHeight = 16.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = notif.tanggal,
                                    fontSize = 10.sp,
                                    color = TextTertiary
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnouncementDetailBottomSheet(
    announcement: AnnouncementRecordEntity,
    onDismiss: () -> Unit
) {
    Dialog(properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false), onDismissRequest = onDismiss) {
        
        Column(
            modifier = Modifier.fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Detail Pengumuman",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = announcement.judul,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                StatusBadge(status = announcement.lingkup)
            }

            Text(
                text = "Dipublikasikan: ${announcement.tanggalPosting}",
                fontSize = 11.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = BorderLight)
            Spacer(modifier = Modifier.height(12.dp))

            if (announcement.waktuKegiatan != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Waktu: ${announcement.waktuKegiatan}", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                }
                Spacer(modifier = Modifier.height(6.dp))
            }

            if (announcement.tempatKegiatan != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = AccentRed, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Tempat: ${announcement.tempatKegiatan}", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            Text(
                text = announcement.konten,
                fontSize = 13.sp,
                color = TextPrimary,
                lineHeight = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8FAFC), RoundedCornerShape(12.dp))
                    .padding(14.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsBottomSheet(onDismiss: () -> Unit) {
    Dialog(properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false), onDismissRequest = onDismiss) {
        
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(24.dp)
            ) {
                Text(text = "Pengaturan Aplikasi", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "• Bahasa: Bahasa Indonesia (Default)", fontSize = 13.sp, color = TextPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "• Notifikasi Suara: Aktif", fontSize = 13.sp, color = TextPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "• Mode Offline / Database Lokal: Aktif (Room DB)", fontSize = 13.sp, color = TextPrimary)
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp)) {
                    Text("Tutup")
                }
            }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpBottomSheet(onDismiss: () -> Unit) {
    Dialog(properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false), onDismissRequest = onDismiss) {
        
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(24.dp)
            ) {
                Text(text = "Pusat Bantuan & Panduan", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(14.dp))
                Text(text = "1. Ajukan Surat: Buka tab Surat, pilih jenis surat yang diinginkan, isi keperluan lalu klik Ajukan.", fontSize = 12.sp, color = TextSecondary, lineHeight = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "2. Bayar Iuran: Buka tab Iuran, klik tombol Bayar Sekarang, pilih metode QRIS atau Transfer Bank.", fontSize = 12.sp, color = TextSecondary, lineHeight = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "3. Laporan Pengaduan: Buka tab Pengaduan, klik Buat Baru, tuliskan rincian keluhan fasilitas lingkungan.", fontSize = 12.sp, color = TextSecondary, lineHeight = 16.sp)
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp)) {
                    Text("Mengerti")
                }
            }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutBottomSheet(onDismiss: () -> Unit) {
    Dialog(properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false), onDismissRequest = onDismiss) {
        
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(48.dp))
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "RuangWarga", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(text = "Versi 2.0.0 (Official Release)", fontSize = 12.sp, color = TextSecondary)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Aplikasi manajemen komunitas warga, surat-menyurat digital, kas transparan, agenda lingkungan, dan tanggap darurat RT 03 / RW 02.",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp)) {
                    Text("Tutup")
                }
            }
    }
}
