package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EmergencyAlertEntity
import com.example.ui.components.StatusBadge
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentGreenDark
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentOrangeDark
import com.example.ui.theme.AccentRed
import com.example.ui.theme.AccentRedDark
import com.example.ui.theme.AccentRedLight
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryGreenDark
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.ui.viewmodel.RtrwUiState
import com.example.ui.viewmodel.RtrwViewModel

private data class QuickEmergencyCategory(
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val description: String
)

/**
 * AlarmScreen - Halaman Khusus Pusat Alarm & Tanggap Darurat RuangWarga
 * Fitur:
 * 1. Header Khusus Kesiapsiagaan & Status Sinyal Darurat
 * 2. Big SOS Pulsing Alarm Button untuk Pemicuan Seketika
 * 3. Pemilihan Cepat 4 Kategori Darurat Utama
 * 4. Status Live Peringatan Darurat Lingkungan yang Sedang Berlangsung
 * 5. Panggilan Cepat 1-Sentuhan (Satpam, Ketua RT, Damkar, Ambulans, Polisi)
 * 6. Riwayat Log Alarm & Status Penanganan
 */
@Composable
fun AlarmScreen(
    uiState: RtrwUiState,
    viewModel: RtrwViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf("Keamanan / Maling") }
    var locationInput by remember {
        mutableStateOf(
            if (uiState.profile.alamat.isNotBlank()) "${uiState.profile.alamat}, ${uiState.profile.rt}/${uiState.profile.rw}"
            else "Blok C, RT 03 / RW 02"
        )
    }
    var noteInput by remember { mutableStateOf("") }
    var isConfirmingAlert by remember { mutableStateOf(false) }

    val categories = listOf(
        QuickEmergencyCategory(
            title = "Keamanan / Maling",
            icon = Icons.Default.Security,
            color = Color(0xFFDC2626),
            description = "Pencurian, penyusup mencurigakan, atau kerusuhan"
        ),
        QuickEmergencyCategory(
            title = "Kebakaran",
            icon = Icons.Default.LocalFireDepartment,
            color = Color(0xFFEA580C),
            description = "Kebakaran rumah, korsleting listrik, atau kebocoran gas"
        ),
        QuickEmergencyCategory(
            title = "Medis / Kesehatan",
            icon = Icons.Default.MedicalServices,
            color = Color(0xFF0284C7),
            description = "Serangan jantung, pingsan, warga butuh ambulans darurat"
        ),
        QuickEmergencyCategory(
            title = "Bencana / Banjir",
            icon = Icons.Default.Warning,
            color = Color(0xFFD97706),
            description = "Pohon tumbang, genangan air parah, atau tanah longsor"
        )
    )

    // Animasi Pulse untuk Tombol SOS
    val infiniteTransition = rememberInfiniteTransition(label = "sos_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sos_scale"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        // ============================================================
        // 1. TOP HEADER MERAH GRADIENT DENGAN TOMBOL KEMBALI
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
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🚨", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Pusat Alarm & Darurat",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Text(
                            text = "Siaga Wilayah RT 03 / RW 02",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }

                // Tombol Buka Form Lapor Tambahan
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.clickable { viewModel.openReportEmergencySheet() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "✍️ Buat Laporan",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // ============================================================
        // 2. KONTEN UTAMA HALAMAN ALARM
        // ============================================================
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(6.dp))
            }

            // --- SECTION 1: BIG SOS PULSE BUTTON & FAST TRIGGER ---
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.2.dp, Color(0xFFFECDD3)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "TOMBOL DARURAT CEPAT",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AccentRedDark,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Tekan tombol di bawah untuk membunyikan alarm di Pos Satpam & menyiagakan warga",
                            fontSize = 11.5.sp,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // Pulsing Circular SOS Button
                        Box(
                            modifier = Modifier
                                .size(130.dp)
                                .scale(if (!isConfirmingAlert) pulseScale else 1f)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(Color(0xFFEF4444), Color(0xFFB91C1C))
                                    )
                                )
                                .border(4.dp, Color(0xFFFECDD3), CircleShape)
                                .clickable {
                                    isConfirmingAlert = true
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.NotificationsActive,
                                    contentDescription = "SOS",
                                    tint = Color.White,
                                    modifier = Modifier.size(38.dp)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "SOS",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    letterSpacing = 1.5.sp
                                )
                                Text(
                                    text = "BUNYIKAN",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Tombol Uji Coba / Tes Bunyi Alarm
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            if (uiState.isEmergencySirenActive) {
                                Button(
                                    onClick = { viewModel.silenceSirenSound() },
                                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.height(38.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = "🔇 Hentikan Bunyi Alarm", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                }
                            } else {
                                OutlinedButton(
                                    onClick = { viewModel.testEmergencySiren(durationMs = 6000L) },
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.2.dp, AccentRed),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentRedDark),
                                    modifier = Modifier.height(38.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = "🔊 Tes Bunyi Alarm (Uji Coba)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Konfirmasi Pengiriman Alarm
                        AnimatedVisibility(visible = isConfirmingAlert) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1F2)),
                                border = BorderStroke(1.5.dp, AccentRed)
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "🚨 Konfirmasi Bunyikan Alarm Darurat?",
                                        fontSize = 13.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AccentRedDark
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Sinyal '$selectedCategory' di lokasi '$locationInput' akan segera dikirim ke Pos Kamling dan seluruh handphone warga.",
                                        fontSize = 11.5.sp,
                                        color = TextSecondary,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Button(
                                            onClick = { isConfirmingAlert = false },
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(40.dp),
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE2E8F0))
                                        ) {
                                            Text(text = "Batal", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        }
                                        Button(
                                            onClick = {
                                                viewModel.triggerEmergencyAlert(selectedCategory, locationInput, noteInput)
                                                isConfirmingAlert = false
                                            },
                                            modifier = Modifier
                                                .weight(1.3f)
                                                .height(40.dp),
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = AccentRed)
                                        ) {
                                            Text(text = "YA, AKTIFKAN!", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                        }

                        // Kategori Kejadian
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Pilih Kategori Bahaya:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            categories.forEach { category ->
                                val isSelected = selectedCategory == category.title
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) category.color.copy(alpha = 0.08f) else Color(0xFFF8FAFC),
                                    border = BorderStroke(
                                        width = if (isSelected) 1.5.dp else 1.dp,
                                        color = if (isSelected) category.color else BorderLight
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedCategory = category.title }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(34.dp)
                                                .clip(CircleShape)
                                                .background(category.color.copy(alpha = 0.15f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = category.icon,
                                                contentDescription = category.title,
                                                tint = category.color,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = category.title,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) category.color else TextPrimary
                                            )
                                            Text(
                                                text = category.description,
                                                fontSize = 10.5.sp,
                                                color = TextSecondary
                                            )
                                        }
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = category.color,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Lokasi & Catatan Tambahan
                        OutlinedTextField(
                            value = locationInput,
                            onValueChange = { locationInput = it },
                            label = { Text("Lokasi Kejadian", fontSize = 12.sp) },
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = AccentRed, modifier = Modifier.size(18.dp))
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AccentRed,
                                unfocusedBorderColor = BorderLight
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = noteInput,
                            onValueChange = { noteInput = it },
                            label = { Text("Keterangan Tambahan (Opsional)", fontSize = 12.sp) },
                            placeholder = { Text("Contoh: Maling membawa motor merah", fontSize = 11.5.sp, color = TextTertiary) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            minLines = 2,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AccentRed,
                                unfocusedBorderColor = BorderLight
                            )
                        )
                    }
                }
            }

            // --- SECTION 2: PERINGATAN DARURAT AKTIF (LIVE STATUS) ---
            val activeAlerts = uiState.emergencyAlerts.filter { it.status == "Aktif" || it.status == "Ditangani" }
            if (activeAlerts.isNotEmpty()) {
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(AccentRed)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Peringatan Darurat Aktif",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                            Text(
                                text = "${activeAlerts.size} Kejadian",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentRed
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        activeAlerts.forEach { alert ->
                            val isVolunteered = uiState.volunteeredEmergencyAlertIds.contains(alert.id)
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable { viewModel.openEmergencyAlarmDetail(alert) },
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1F2)),
                                border = BorderStroke(1.2.dp, Color(0xFFFECDD3))
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(text = "🚨", fontSize = 16.sp)
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = alert.judul.ifBlank { alert.jenisDarurat },
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF991B1B)
                                            )
                                        }
                                        StatusBadge(status = alert.status)
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = alert.catatan.ifBlank { "Kejadian darurat sedang dalam penanganan petugas RT/RW." },
                                        fontSize = 12.sp,
                                        color = TextSecondary,
                                        lineHeight = 16.sp
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "📍 ${alert.lokasi} • ⏱ ${alert.waktu}",
                                            fontSize = 11.sp,
                                            color = TextTertiary
                                        )

                                        Button(
                                            onClick = { viewModel.toggleEmergencyVolunteer(alert.id) },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (isVolunteered) Color(0xFF166534) else AccentGreenDark
                                            ),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.height(32.dp)
                                        ) {
                                            Text(
                                                text = if (isVolunteered) "✓ Relawan" else "+ Jadi Relawan",
                                                fontSize = 10.5.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // --- SECTION 3: PANGGILAN CEPAT 1-SENTUHAN ---
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Panggilan Cepat Darurat",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Hubungi pos penjagaan atau layanan bantuan seketika",
                        fontSize = 11.5.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        EmergencyCallCard(
                            label = "Pos Satpam",
                            subtext = "RT 03",
                            number = "0812-9988-7711",
                            icon = Icons.Default.Security,
                            bgColor = Color(0xFF0F172A),
                            modifier = Modifier.weight(1f)
                        ) {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:081299887711"))
                            context.startActivity(intent)
                        }

                        EmergencyCallCard(
                            label = "Ketua RT",
                            subtext = "Pak Joko",
                            number = "0812-3456-7890",
                            icon = Icons.Default.Phone,
                            bgColor = Color(0xFF1E40AF),
                            modifier = Modifier.weight(1f)
                        ) {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:081234567890"))
                            context.startActivity(intent)
                        }

                        EmergencyCallCard(
                            label = "Damkar 113",
                            subtext = "Kebakaran",
                            number = "113",
                            icon = Icons.Default.LocalFireDepartment,
                            bgColor = Color(0xFFB91C1C),
                            modifier = Modifier.weight(1f)
                        ) {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:113"))
                            context.startActivity(intent)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        EmergencyCallCard(
                            label = "Ambulans 118",
                            subtext = "Medis Darurat",
                            number = "118",
                            icon = Icons.Default.LocalHospital,
                            bgColor = Color(0xFF047857),
                            modifier = Modifier.weight(1f)
                        ) {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:118"))
                            context.startActivity(intent)
                        }

                        EmergencyCallCard(
                            label = "Polisi 110",
                            subtext = "Polsek Terdekat",
                            number = "110",
                            icon = Icons.Default.LocalPolice,
                            bgColor = Color(0xFF4338CA),
                            modifier = Modifier.weight(1f)
                        ) {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:110"))
                            context.startActivity(intent)
                        }
                    }
                }
            }

            // --- SECTION 4: RIWAYAT LOG ALARM & PENANGANAN ---
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Riwayat Alarm & Penanganan",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (uiState.emergencyAlerts.isEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, BorderLight)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "🛡️", fontSize = 28.sp)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Lingkungan Aman Terkendali",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Belum ada catatan alarm bahaya yang dilaporkan.",
                                    fontSize = 11.5.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    } else {
                        uiState.emergencyAlerts.take(6).forEach { alert ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .clickable { viewModel.openEmergencyAlarmDetail(alert) },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                border = BorderStroke(1.dp, BorderLight)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = alert.jenisDarurat,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (alert.status == "Aktif") AccentRed else TextPrimary
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            StatusBadge(status = alert.status)
                                        }
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "📍 ${alert.lokasi} • ${alert.waktu}",
                                            fontSize = 11.sp,
                                            color = TextSecondary
                                        )
                                    }

                                    if (alert.status == "Aktif" && uiState.isAdminMode) {
                                        Button(
                                            onClick = { viewModel.resolveEmergencyAlert(alert.id) },
                                            colors = ButtonDefaults.buttonColors(containerColor = AccentGreenDark),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.height(32.dp)
                                        ) {
                                            Text("Selesai", fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun EmergencyCallCard(
    label: String,
    subtext: String,
    number: String,
    icon: ImageVector,
    bgColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = bgColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Text(
                text = subtext,
                fontSize = 9.5.sp,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}
