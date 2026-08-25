package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentRed
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.ui.viewmodel.RtrwUiState
import com.example.ui.viewmodel.RtrwViewModel

private data class EmergencyCategoryItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val color: Color
)

/**
 * AlarmScreen - Halaman Form Pengiriman Sinyal Alarm Darurat Bertahap
 * Murni untuk tombol dan pemilihan kategori bahaya, tanpa riwayat dan tanpa kontak.
 * 
 * Tahapan pengisian yang aman & terpandu:
 * 1. Pilih Kategori Bahaya
 * 2. Masukkan / Konfirmasi Lokasi Kejadian (Enable setelah kategori dipilih)
 * 3. Catatan / Keterangan Tambahan Opsional (Enable setelah lokasi terisi)
 * 4. Tombol Konfirmasi Kirim Sinyal Darurat (Enable setelah Langkah 1 & 2 lengkap)
 */
@Composable
fun AlarmScreen(
    uiState: RtrwUiState,
    viewModel: RtrwViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    // State form bertahap
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var locationInput by remember {
        mutableStateOf(
            if (uiState.profile.alamat.isNotBlank()) "${uiState.profile.alamat}, ${uiState.profile.rt}/${uiState.profile.rw}"
            else "RT 03 / RW 02"
        )
    }
    var noteInput by remember { mutableStateOf("") }
    var showConfirmDialog by remember { mutableStateOf(false) }

    // Aturan Enable Bertahap
    val isStep1Complete = !selectedCategory.isNullOrBlank()
    val isStep2Enabled = isStep1Complete
    val isStep2Complete = isStep2Enabled && locationInput.isNotBlank()
    val isStep3Enabled = isStep2Complete
    val isSubmitEnabled = isStep1Complete && isStep2Complete

    val categoryList = listOf(
        EmergencyCategoryItem(
            id = "Keamanan / Maling",
            title = "Keamanan / Maling",
            subtitle = "Pencurian, penyusup mencurigakan, atau kerusuhan warga",
            icon = Icons.Default.Security,
            color = Color(0xFFDC2626)
        ),
        EmergencyCategoryItem(
            id = "Kebakaran",
            title = "Kebakaran",
            subtitle = "Kebakaran rumah, korsleting listrik, atau kebocoran gas",
            icon = Icons.Default.LocalFireDepartment,
            color = Color(0xFFEA580C)
        ),
        EmergencyCategoryItem(
            id = "Medis / Kesehatan",
            title = "Medis / Kesehatan",
            subtitle = "Warga sakit kritis, serangan jantung, butuh ambulans darurat",
            icon = Icons.Default.MedicalServices,
            color = Color(0xFF0284C7)
        ),
        EmergencyCategoryItem(
            id = "Bencana / Banjir",
            title = "Bencana / Banjir",
            subtitle = "Pohon tumbang, banjir parah, gempa, atau tanah longsor",
            icon = Icons.Default.Warning,
            color = Color(0xFFD97706)
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        // ============================================================
        // 1. TOP HEADER APP BAR
        // ============================================================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF991B1B), Color(0xFFDC2626))
                    )
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack, modifier = Modifier.size(36.dp)) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🚨", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Kirim Sinyal Alarm Darurat",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Text(
                            text = "Peringatan Siaga Cepat ke Seluruh Warga RT/RW",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }

                // Tombol Tes Sirine Uji Coba
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.clickable {
                        if (uiState.isEmergencySirenActive) {
                            viewModel.silenceSirenSound()
                            Toast.makeText(context, "Sirine dihentikan", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.testEmergencySiren(durationMs = 5000L)
                            Toast.makeText(context, "Menguji coba nada sirine (5 detik)...", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (uiState.isEmergencySirenActive) "🔇 Hentikan" else "🔊 Tes Suara",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // ============================================================
        // 2. KONTEN FORMULIR BERTAHAP
        // ============================================================
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // BANNER INSTRUKSI
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                border = BorderStroke(1.dp, Color(0xFFBFDBFE)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Lengkapi tahapan di bawah ini secara berurutan untuk menyiagakan warga dan membunyikan sirine darurat.",
                        fontSize = 11.5.sp,
                        color = Color(0xFF1E3A8A),
                        lineHeight = 16.sp
                    )
                }
            }

            // ============================================================
            // LANGKAH 1: PILIH KATEGORI BAHAYA (WAJIB)
            // ============================================================
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, if (isStep1Complete) Color(0xFF86EFAC) else BorderLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(if (isStep1Complete) Color(0xFF16A34A) else Color(0xFFDC2626)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isStep1Complete) {
                                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                } else {
                                    Text(text = "1", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Langkah 1: Pilih Kategori Bahaya *",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        if (isStep1Complete) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFFDCFCE7)
                            ) {
                                Text(
                                    text = "Terpilih ✓",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF15803D),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        categoryList.forEach { cat ->
                            val isSelected = selectedCategory == cat.id
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) cat.color.copy(alpha = 0.08f) else Color(0xFFF8FAFC),
                                border = BorderStroke(
                                    width = if (isSelected) 1.8.dp else 1.dp,
                                    color = if (isSelected) cat.color else Color(0xFFE2E8F0)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedCategory = cat.id }
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(cat.color.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = cat.icon,
                                            contentDescription = cat.title,
                                            tint = cat.color,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = cat.title,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) cat.color else TextPrimary
                                        )
                                        Text(
                                            text = cat.subtitle,
                                            fontSize = 11.sp,
                                            color = TextSecondary,
                                            lineHeight = 15.sp
                                        )
                                    }
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = cat.color,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ============================================================
            // LANGKAH 2: LOKASI KEJADIAN (ENABLE SETELAH LANGKAH 1)
            // ============================================================
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isStep2Enabled) Color.White else Color(0xFFF8FAFC)
                ),
                border = BorderStroke(
                    1.dp,
                    if (isStep2Complete) Color(0xFF86EFAC) else if (isStep2Enabled) Color(0xFFCBD5E1) else Color(0xFFE2E8F0)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isStep2Complete) Color(0xFF16A34A)
                                        else if (isStep2Enabled) Color(0xFFDC2626)
                                        else Color(0xFF94A3B8)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isStep2Complete) {
                                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                } else {
                                    Text(text = "2", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Langkah 2: Lokasi Kejadian *",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isStep2Enabled) TextPrimary else TextTertiary
                            )
                        }

                        if (!isStep2Enabled) {
                            Text(
                                text = "Terkunci 🔒",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextTertiary
                            )
                        } else if (isStep2Complete) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFFDCFCE7)
                            ) {
                                Text(
                                    text = "Siap ✓",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF15803D),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (isStep2Enabled) "Pastikan titik lokasi akurat (nama jalan, nomor rumah, atau patokan terdekat):" 
                               else "Pilih kategori bahaya pada Langkah 1 terlebih dahulu untuk membuka kolom ini.",
                        fontSize = 11.5.sp,
                        color = if (isStep2Enabled) TextSecondary else TextTertiary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = locationInput,
                        onValueChange = { locationInput = it },
                        enabled = isStep2Enabled,
                        placeholder = { Text("Contoh: Jl. Mawar No. 12, Depan Pos Ronda RT 03", fontSize = 12.sp) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = if (isStep2Enabled) AccentRed else TextTertiary,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AccentRed,
                            unfocusedBorderColor = BorderLight,
                            disabledBorderColor = Color(0xFFE2E8F0),
                            disabledTextColor = TextTertiary
                        )
                    )
                }
            }

            // ============================================================
            // LANGKAH 3: KETERANGAN TAMBAHAN (OPSIONAL)
            // ============================================================
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isStep3Enabled) Color.White else Color(0xFFF8FAFC)
                ),
                border = BorderStroke(
                    1.dp,
                    if (isStep3Enabled) Color(0xFFCBD5E1) else Color(0xFFE2E8F0)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(if (isStep3Enabled) Color(0xFF0284C7) else Color(0xFF94A3B8)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "3", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Langkah 3: Keterangan Tambahan",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isStep3Enabled) TextPrimary else TextTertiary
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFF1F5F9)
                        ) {
                            Text(
                                text = "Opsional",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextSecondary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (isStep3Enabled) "Tuliskan rincian penting seperti ciri pelaku, kondisi kebakaran, jumlah korban, dll:" 
                               else "Lengkapi Langkah 1 & Langkah 2 terlebih dahulu untuk mengisi keterangan.",
                        fontSize = 11.5.sp,
                        color = if (isStep3Enabled) TextSecondary else TextTertiary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = noteInput,
                        onValueChange = { noteInput = it },
                        enabled = isStep3Enabled,
                        placeholder = { Text("Contoh: Terduga pelaku 2 orang membawa motor matic.", fontSize = 11.5.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        minLines = 2,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AccentRed,
                            unfocusedBorderColor = BorderLight,
                            disabledBorderColor = Color(0xFFE2E8F0),
                            disabledTextColor = TextTertiary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ============================================================
            // LANGKAH 4: TOMBOL KONFIRMASI KIRIM SINYAL DARURAT
            // ============================================================
            Button(
                onClick = { showConfirmDialog = true },
                enabled = isSubmitEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFDC2626),
                    disabledContainerColor = Color(0xFFCBD5E1)
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = if (isSubmitEnabled) 3.dp else 0.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = null,
                        tint = if (isSubmitEnabled) Color.White else Color(0xFF64748B),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isSubmitEnabled) "KIRIM SINYAL ALARM DARURAT SEKARANG" else "LENGKAPI TAHAPAN UNTUK MENGIRIM",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isSubmitEnabled) Color.White else Color(0xFF64748B)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // ============================================================
    // DIALOG KONFIRMASI PENGIRIMAN AKHIR
    // ============================================================
    if (showConfirmDialog) {
        androidx.compose.ui.window.Dialog(
            onDismissRequest = { showConfirmDialog = false },
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFEE2E2)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🚨", fontSize = 26.sp)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Kirim Sinyal Bahaya?",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF991B1B)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Sinyal alarm darurat '$selectedCategory' di lokasi '$locationInput' akan segera dikirimkan ke handphone seluruh warga dan Pos Kamling.",
                            fontSize = 12.5.sp,
                            color = TextSecondary,
                            textAlign = TextAlign.Center,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = { showConfirmDialog = false },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, Color(0xFFCBD5E1))
                            ) {
                                Text("Batal", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.5.sp)
                            }

                            Button(
                                onClick = {
                                    showConfirmDialog = false
                                    selectedCategory?.let { cat ->
                                        viewModel.triggerEmergencyAlert(cat, locationInput, noteInput)
                                        Toast.makeText(context, "Sinyal alarm darurat berhasil disiarkan!", Toast.LENGTH_LONG).show()
                                        onBack()
                                    }
                                },
                                modifier = Modifier
                                    .weight(1.4f)
                                    .height(44.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                            ) {
                                Text("YA, KIRIMKAN!", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 12.5.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
