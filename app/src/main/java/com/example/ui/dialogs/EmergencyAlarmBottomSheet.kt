package com.example.ui.dialogs

import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.fillMaxSize
import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentOrangeDark
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
 * EmergencyAlarmBottomSheet - Layar 6 (Mockup 6: Darurat)
 * Menampilkan:
 * 1. Header Merah Solid dengan tombol Back dan Icon History Clock
 * 2. Card Peringatan Bencana / Darurat (Peringatan Banjir di Blok C)
 * 3. Metadata terstruktur (Lokasi, Waktu Kejadian, Dikeluarkan oleh, Status)
 * 4. Tombol Merah Solid "Lihat Detail & Update"
 * 5. Section "Update Terbaru" dengan Timeline Step
 */
@Composable
fun EmergencyAlarmBottomSheet(
    alerts: List<EmergencyAlertEntity>,
    viewModel: RtrwViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        // ============================================================
        // 1. TOP HEADER MERAH SOLID
        // ============================================================
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(AccentRed)
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Darurat",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                IconButton(
                    onClick = {
                        Toast.makeText(context, "Riwayat alarm darurat", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Riwayat",
                        tint = Color.White
                    )
                }
            }
        }
        // ============================================================
        // 2. CONTENT LAZY COLUMN
        // ============================================================
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Peringatan Card (Banjir di Blok C)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1F2)),
                    border = BorderStroke(1.dp, Color(0xFFFECDD3))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Warning Tag & Title
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFFEE2E2)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🚨", fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "PERINGATAN",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentRedDark,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "BANJIR DI BLOK C",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        // Description
                        Text(
                            text = "Hujan deras menyebabkan genangan air di area Blok C. Hindari jalur tersebut sementara waktu.",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        // Structured Metadata
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            // Lokasi
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "📍", fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(text = "Lokasi", fontSize = 10.sp, color = TextTertiary)
                                    Text(text = "Blok C", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                                }
                            }
                            // Waktu Kejadian
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "⏱", fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(text = "Waktu Kejadian", fontSize = 10.sp, color = TextTertiary)
                                    Text(text = "Hari ini, 20.15 WIB", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                                }
                            }
                            // Dikeluarkan oleh
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "📢", fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(text = "Dikeluarkan oleh", fontSize = 10.sp, color = TextTertiary)
                                    Text(text = "RW 02", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                                }
                            }
                            // Status
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(AccentRed)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(text = "Status", fontSize = 10.sp, color = TextTertiary)
                                    Text(text = "Sedang Ditangani", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AccentRedDark)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        // Button "Lihat Detail & Update"
                        Button(
                            onClick = {
                                Toast.makeText(context, "Status terkini: Tim penanganan berada di Blok C", Toast.LENGTH_SHORT).show()
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                        ) {
                            Text(
                                text = "Lihat Detail & Update",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
            // Section "Update Terbaru"
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Update Terbaru",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, BorderLight)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Step 1
                            Row(verticalAlignment = Alignment.Top) {
                                Box(
                                    modifier = Modifier
                                        .padding(top = 4.dp)
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(PrimaryBlue)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(text = "20.45 WIB", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Petugas sudah menuju lokasi untuk penanganan.",
                                        fontSize = 11.5.sp,
                                        color = TextSecondary,
                                        lineHeight = 15.sp
                                    )
                                }
                            }
                            HorizontalDivider(color = Color(0xFFF1F5F9))
                            // Step 2
                            Row(verticalAlignment = Alignment.Top) {
                                Box(
                                    modifier = Modifier
                                        .padding(top = 4.dp)
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(AccentGreen)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(text = "20.15 WIB", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Peringatan darurat banjir diterbitkan oleh Ketua RW 02.",
                                        fontSize = 11.5.sp,
                                        color = TextSecondary,
                                        lineHeight = 15.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}
