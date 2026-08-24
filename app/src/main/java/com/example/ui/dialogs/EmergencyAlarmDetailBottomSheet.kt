package com.example.ui.dialogs

import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.fillMaxSize
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
    val priorityColor = when (alert.tingkatPrioritas) {
        "Kritis" -> AccentRed
        "Peringatan" -> AccentOrange
        else -> Color(0xFFEAB308)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = TextPrimary)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when (alert.tingkatPrioritas) {
                        "Kritis" -> AccentRedLight
                        "Peringatan" -> AccentOrangeLight
                        else -> Color(0xFFFEF9C3)
                    }
                ) {
                    Text(
                        text = "🚨 ${alert.tingkatPrioritas.uppercase()}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (alert.tingkatPrioritas == "Kritis") AccentRedDark else AccentOrangeDark,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = when (alert.status) {
                    "Aktif" -> Color(0xFFFEE2E2)
                    "Ditangani" -> Color(0xFFFEF3C7)
                    "Terkendali" -> Color(0xFFDCFCE7)
                    else -> BackgroundLight
                },
                border = BorderStroke(1.dp, priorityColor)
            ) {
                Text(
                    text = alert.status,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = when (alert.status) {
                        "Aktif" -> AccentRedDark
                        "Ditangani" -> AccentOrangeDark
                        "Terkendali" -> AccentGreenDark
                        else -> TextPrimary
                    },
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(14.dp))
        // Judul Kejadian
        Text(
            text = alert.judul,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = alert.catatan.ifBlank { "Terdapat kejadian darurat di lingkungan yang membutuhkan perhatian warga sekitar." },
            fontSize = 13.sp,
            color = TextSecondary,
            lineHeight = 18.sp
        )
        Spacer(modifier = Modifier.height(14.dp))
        // Info Metadata Box
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = BackgroundLight,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = AccentRed, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Lokasi: ${alert.lokasi}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Schedule, contentDescription = null, tint = TextTertiary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Waktu Kejadian: ${alert.waktu}", fontSize = 12.sp, color = TextSecondary)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Dikeluarkan Oleh: ${alert.dikeluarkanOleh} (Target: ${alert.targetWilayah})", fontSize = 12.sp, color = TextSecondary)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // INSTRUKSI WARGA BOX
        Text(text = "INSTRUKSI WARGA:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(modifier = Modifier.height(6.dp))
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
            border = BorderStroke(1.dp, Color(0xFFFDE68A)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                val instructions = alert.instruksi.lines().ifEmpty { listOf("• Hindari area sekitar", "• Ikuti arahan petugas di lokasi") }
                instructions.forEach { line ->
                    Text(
                        text = line,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF92400E),
                        lineHeight = 17.sp,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(18.dp))
        // TIMELINE PERKEMBANGAN (LIFECYCLE ALARM)
        Text(text = "Timeline Penanganan:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(modifier = Modifier.height(10.dp))
        val timelineList = if (alert.timelineUpdates.isNotBlank()) {
            alert.timelineUpdates.split("|")
        } else {
            listOf(
                "${alert.waktu}: Alarm dibuat",
                "Petugas menuju lokasi",
                "Situasi dalam penanganan pengurus"
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            timelineList.forEachIndexed { index, step ->
                Row(verticalAlignment = Alignment.Top) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(if (index == 0) AccentGreen else PrimaryBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(11.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = step, fontSize = 12.sp, color = TextPrimary, fontWeight = if (index == timelineList.lastIndex) FontWeight.Bold else FontWeight.Normal)
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        // KONTROL KHUSUS PENGURUS (JIKA DALAM MODE PENGURUS)
        if (isAdminMode) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "Kontrol Alarm (Mode Pengurus):", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF92400E))
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { viewModel.updateEmergencyStatus(alert.id, "Ditangani") },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AccentOrange),
                            modifier = Modifier.weight(1f).height(34.dp)
                        ) {
                            Text("Ditangani", fontSize = 11.sp)
                        }
                        Button(
                            onClick = { viewModel.updateEmergencyStatus(alert.id, "Terkendali") },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
                            modifier = Modifier.weight(1f).height(34.dp)
                        ) {
                            Text("Terkendali", fontSize = 11.sp)
                        }
                        Button(
                            onClick = {
                                viewModel.updateEmergencyStatus(alert.id, "Selesai")
                                onDismiss()
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF475569)),
                            modifier = Modifier.weight(1f).height(34.dp)
                        ) {
                            Text("Selesai", fontSize = 11.sp)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        // TOMBOL AKSI UTAMA WARGA (BANTU JADI RELAWAN & HUBUNGI POSKO)
        val uiState by viewModel.uiState.collectAsState()
        val isVolunteered = uiState.volunteeredEmergencyAlertIds.contains(alert.id)

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.toggleEmergencyVolunteer(alert.id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isVolunteered) Color(0xFF166534) else AccentGreenDark
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = if (isVolunteered) "✅" else "🤝", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isVolunteered) "Anda Terdaftar Sebagai Relawan" else "Bantu Jadi Relawan di Lokasi",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:081234567890"))
                        context.startActivity(callIntent)
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                ) {
                    Icon(imageVector = Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Posko Satpam", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                OutlinedButton(
                    onClick = {
                        Toast.makeText(context, "Informasi alarm darurat disalin", Toast.LENGTH_SHORT).show()
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Bagikan Info", fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
