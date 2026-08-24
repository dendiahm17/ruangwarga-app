package com.example.ui.dialogs

import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.StatusBadge
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentGreenDark
import com.example.ui.theme.AccentGreenLight
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentOrangeDark
import com.example.ui.theme.AccentOrangeLight
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentPurpleLight
import com.example.ui.theme.AccentRed
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryBlueDark
import com.example.ui.theme.PrimaryBlueLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RwPulseDashboardBottomSheet(
    onDismiss: () -> Unit
) {
    Dialog(properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false), onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .padding(bottom = 32.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(PrimaryBlueLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.TrendingUp, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("RW Pulse & Partisipasi", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text("Kesehatan sosial & keaktifan warga RW 02", fontSize = 12.sp, color = TextSecondary)
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Score Index Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Indeks Kesehatan Sosial Wilayah", fontSize = 13.sp, color = Color(0xFF94A3B8))
                        StatusBadge(status = "Sangat Sehat")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("92.4", fontSize = 34.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        Text(" / 100", fontSize = 16.sp, color = Color(0xFF64748B), modifier = Modifier.padding(bottom = 4.dp))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "RW 02 menunjukkan partisipasi gotong royong tinggi, respon cepat terhadap fasilitas umum, dan transparansi kas terbuka.",
                        fontSize = 12.sp,
                        color = Color(0xFFCBD5E1),
                        lineHeight = 16.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Pilar Partisipasi & Kinerja (Bulan Ini)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(10.dp))
            // Progress Metrics
            PulseProgressRow(
                title = "Partisipasi Acara Warga",
                percent = 0.84f,
                ratioText = "73 / 87 KK Ikut Serta",
                color = AccentGreen
            )
            Spacer(modifier = Modifier.height(12.dp))
            PulseProgressRow(
                title = "Kesiapan Relawan & Saling Bantu",
                percent = 0.92f,
                ratioText = "24 Relawan Terdaftar",
                color = PrimaryBlue
            )
            Spacer(modifier = Modifier.height(12.dp))
            PulseProgressRow(
                title = "Kejadian Lingkungan Terselesaikan",
                percent = 0.88f,
                ratioText = "18 / 20 Titik Selesai",
                color = AccentOrange
            )
            Spacer(modifier = Modifier.height(12.dp))
            PulseProgressRow(
                title = "Kepatuhan Iuran Kas Terbuka",
                percent = 0.95f,
                ratioText = "95% KK Lunas Tepat Waktu",
                color = AccentPurple
            )
            Spacer(modifier = Modifier.height(20.dp))
            // Community Care Highlights
            Text("Sorotan Komunitas Warga", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Favorite, contentDescription = null, tint = AccentRed, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("3 Lansia Terbantu Transportasi Medis", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Diantar oleh relawan Pak Rudi (Blok B) dan Pak Joko (Blok A).", fontSize = 11.sp, color = TextSecondary)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = BorderLight)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Handshake, contentDescription = null, tint = AccentGreenDark, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Musyawarah Paving Blok C Mencapai Kuorum (89%)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Pengerjaan dijadwalkan mulai Sabtu depan dengan dana kas RT.", fontSize = 11.sp, color = TextSecondary)
                }
            }
        }
    }
}
@Composable
fun PulseProgressRow(
    title: String,
    percent: Float,
    ratioText: String,
    color: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Text(ratioText, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = color)
        }
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { percent },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = BorderLight
        )
    }
}
