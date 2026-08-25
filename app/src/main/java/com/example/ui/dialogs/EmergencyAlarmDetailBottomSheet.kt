package com.example.ui.dialogs

import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.fillMaxSize
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.core.animateFloat
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EmergencyAlertEntity
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentGreenDark
import com.example.ui.theme.AccentGreenLight
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentOrangeDark
import com.example.ui.theme.AccentOrangeLight
import com.example.ui.theme.AccentRed
import com.example.ui.theme.AccentRedDark
import com.example.ui.theme.AccentRedLight
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.ui.viewmodel.RtrwViewModel
/**
 * EmergencyAlarmDetailBottomSheet - Detail komprehensif Alarm Darurat
 * Menampilkan:
 * 1. Header Tingkat Prioritas (🔴 KRITIS / 🟠 PERINGATAN / 🟡 INFORMASI PENTING)
 * 2. Judul, deskripsi, lokasi, waktu, dan otoritas pengeluar
 * 3. INSTRUKSI WARGA
 * 4. Status Lifecycle (Aktif -> Ditangani -> Terkendali -> Selesai)
 * 5. Timeline kronologis pembaruan penanganan
 * 6. Kontrol Pengurus (Update Status & Terbitkan Alarm) jika dalam Mode Pengurus
 */
@Composable
fun EmergencyAlarmDetailBottomSheet(
    alert: EmergencyAlertEntity,
    viewModel: RtrwViewModel,
    isAdminMode: Boolean,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val isVolunteered = uiState.volunteeredEmergencyAlertIds.contains(alert.id)

    // Animasi Pulse Dramatis untuk Efek Hazard Alert
    val infiniteTransition = androidx.compose.animation.core.rememberInfiniteTransition(label = "emergency_pulse")
    val hazardAlpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = androidx.compose.animation.core.infiniteRepeatable(
            animation = androidx.compose.animation.core.tween(durationMillis = 500, easing = androidx.compose.animation.core.FastOutSlowInEasing),
            repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
        ),
        label = "hazard_alpha"
    )

    val hazardScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = androidx.compose.animation.core.infiniteRepeatable(
            animation = androidx.compose.animation.core.tween(durationMillis = 600, easing = androidx.compose.animation.core.FastOutSlowInEasing),
            repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
        ),
        label = "hazard_scale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .verticalScroll(rememberScrollState())
    ) {
        // ============================================================
        // 1. TOP DRAMATIC HAZARD HEADER
        // ============================================================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF991B1B).copy(alpha = hazardAlpha),
                            Color(0xFF7F1D1D),
                            Color(0xFF0F172A)
                        )
                    )
                )
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0x33FFFFFF),
                            modifier = Modifier.size(36.dp)
                        ) {
                            IconButton(onClick = onDismiss) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Tutup",
                                    tint = Color.White
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        // Pulsing High Priority Badge
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0xFFEF4444),
                            border = BorderStroke(1.5.dp, Color.White.copy(alpha = 0.8f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "🚨", fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "PRIORITAS TINGGI • ${alert.tingkatPrioritas.uppercase()}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }

                    // Status Badge
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = when (alert.status) {
                            "Aktif" -> Color(0x33DC2626)
                            "Ditangani" -> Color(0x33F59E0B)
                            "Terkendali" -> Color(0x3310B981)
                            else -> Color(0x22FFFFFF)
                        },
                        border = BorderStroke(
                            1.dp,
                            when (alert.status) {
                                "Aktif" -> Color(0xFFEF4444)
                                "Ditangani" -> Color(0xFFFBBF24)
                                "Terkendali" -> Color(0xFF34D399)
                                else -> Color.Gray
                            }
                        )
                    ) {
                        Text(
                            text = alert.status.uppercase(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = when (alert.status) {
                                "Aktif" -> Color(0xFFFCA5A5)
                                "Ditangani" -> Color(0xFFFDE68A)
                                "Terkendali" -> Color(0xFFA7F3D0)
                                else -> Color.White
                            },
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Hazard Siren Icon & Alert Title Centerpiece
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFDC2626).copy(alpha = 0.3f))
                            .border(1.5.dp, Color(0xFFEF4444), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (alert.jenisDarurat) {
                                "Kebakaran" -> "🔥"
                                "Pencurian", "Maling" -> "🚨"
                                "Medis Darurat", "Ambulans" -> "🚑"
                                "Bencana Alam", "Banjir" -> "🌊"
                                else -> "⚠️"
                            },
                            fontSize = 28.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = alert.judul,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            lineHeight = 26.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Jenis: ${alert.jenisDarurat} • ${alert.waktu}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFCBD5E1)
                        )
                    }
                }

                // Tombol Matikan Suara Sirine Jika Sedang Aktif
                if (uiState.isEmergencySirenActive) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = { viewModel.silenceSirenSound() },
                        modifier = Modifier.fillMaxWidth().height(40.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155))
                    ) {
                        Text(text = "🔇 Matikan Bunyi Sirine di HP Saya", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }

        // ============================================================
        // 2. KONTEN BODY (DARK THEME WITH HIGH VISIBILITY)
        // ============================================================
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            // Catatan / Deskripsi Kejadian
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                border = BorderStroke(1.dp, Color(0xFF334155)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "DESKRIPSI SITUASI DARURAT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF94A3B8),
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = alert.catatan.ifBlank { "Terdapat kejadian darurat di lingkungan yang membutuhkan perhatian warga sekitar segera." },
                        fontSize = 13.5.sp,
                        color = Color(0xFFF1F5F9),
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Lokasi & Otoritas Pelapor
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                border = BorderStroke(1.dp, Color(0xFF334155)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0x33EF4444)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = "Titik Lokasi Kejadian", fontSize = 10.5.sp, color = Color(0xFF94A3B8))
                            Text(text = alert.lokasi, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    HorizontalDivider(color = Color(0xFF334155))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0x333B82F6)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = Color(0xFF60A5FA), modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = "Penerbit Sinyal / Wilayah Siaga", fontSize = 10.5.sp, color = Color(0xFF94A3B8))
                            Text(text = "${alert.dikeluarkanOleh} (${alert.targetWilayah})", fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFFE2E8F0))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ============================================================
            // 3. INSTRUKSI KESELAMATAN WARGA (URGENT HIGHLIGHT BOX)
            // ============================================================
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF451A03)),
                border = BorderStroke(1.5.dp, Color(0xFFD97706)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "⚠️", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "PETUNJUK & INSTRUKSI KESELAMATAN:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFFDE68A),
                            letterSpacing = 0.5.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    val instructions = alert.instruksi.lines().ifEmpty { listOf("• Hindari mendekati titik bahaya", "• Siapkan jalur evakuasi", "• Tunggu instruksi lanjutan dari tim keamanan RT/RW") }
                    instructions.forEach { line ->
                        Text(
                            text = line,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFFEF3C7),
                            lineHeight = 19.sp,
                            modifier = Modifier.padding(vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ============================================================
            // 4. TIMELINE PENANGANAN SITUASI
            // ============================================================
            Text(
                text = "TIMELINE PERKEMBANGAN PENANGANAN:",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF94A3B8),
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            val timelineList = if (alert.timelineUpdates.isNotBlank()) {
                alert.timelineUpdates.split("|")
            } else {
                listOf(
                    "${alert.waktu}: Sinyal bahaya diaktifkan",
                    "Petugas Satpam & Pengurus menuju lokasi",
                    "Penanganan darurat sedang berlangsung"
                )
            }
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                border = BorderStroke(1.dp, Color(0xFF334155)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    timelineList.forEachIndexed { index, step ->
                        Row(verticalAlignment = Alignment.Top) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(if (index == timelineList.lastIndex) Color(0xFF10B981) else Color(0xFF3B82F6)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = step,
                                fontSize = 12.5.sp,
                                color = if (index == timelineList.lastIndex) Color.White else Color(0xFFCBD5E1),
                                fontWeight = if (index == timelineList.lastIndex) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ============================================================
            // 5. KONTROL KHUSUS PENGURUS (JIKA DALAM MODE PENGURUS)
            // ============================================================
            if (isAdminMode) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1B4B)),
                    border = BorderStroke(1.dp, Color(0xFF6366F1)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "KONTROL SITUASI (MODE PENGURUS)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFA5B4FC)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { viewModel.updateEmergencyStatus(alert.id, "Ditangani") },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AccentOrange),
                                modifier = Modifier.weight(1f).height(36.dp)
                            ) {
                                Text("Ditangani", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Button(
                                onClick = { viewModel.updateEmergencyStatus(alert.id, "Terkendali") },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
                                modifier = Modifier.weight(1f).height(36.dp)
                            ) {
                                Text("Terkendali", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Button(
                                onClick = {
                                    viewModel.updateEmergencyStatus(alert.id, "Selesai")
                                    onDismiss()
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64748B)),
                                modifier = Modifier.weight(1f).height(36.dp)
                            ) {
                                Text("Selesai", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // ============================================================
            // 6. TOMBOL RESPON CEPAT UTAMA (RELAWAN & PANGGILAN POSKO)
            // ============================================================
            Button(
                onClick = { viewModel.toggleEmergencyVolunteer(alert.id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isVolunteered) Color(0xFF059669) else Color(0xFF10B981)
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = if (isVolunteered) "✅" else "🤝", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isVolunteered) "Anda Terdaftar Sebagai Relawan Penolong" else "Bantu Jadi Relawan di Lokasi",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:081234567890"))
                        context.startActivity(callIntent)
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                ) {
                    Icon(imageVector = Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Hubungi Posko", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                OutlinedButton(
                    onClick = {
                        Toast.makeText(context, "Informasi alarm darurat disalin ke papan klip", Toast.LENGTH_SHORT).show()
                    },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFF64748B)),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFFCBD5E1))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Bagikan Info", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE2E8F0))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
