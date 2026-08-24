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
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.data.model.UserParticipationEntity
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentGreenDark
import com.example.ui.theme.AccentGreenLight
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentOrangeLight
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentPurpleLight
import com.example.ui.theme.AccentRed
import com.example.ui.theme.AccentRedLight
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryBlueLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartisipasiSayaBottomSheet(
    participations: List<UserParticipationEntity>,
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
                            .background(AccentGreenLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.VolunteerActivism, contentDescription = null, tint = AccentGreenDark, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("Jejak Partisipasi Saya", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text("Apresiasi kontribusi Anda di lingkungan RW 02", fontSize = 12.sp, color = TextSecondary)
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Summary Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("12 Acara", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                        Text("Kegiatan Diikuti", fontSize = 11.sp, color = TextSecondary)
                    }
                }
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("5 Kasus", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AccentGreenDark)
                        Text("Bantuan Sosial", fontSize = 11.sp, color = TextSecondary)
                    }
                }
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("100%", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AccentPurple)
                        Text("Iuran Rutin", fontSize = 11.sp, color = TextSecondary)
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text("Riwayat Keterlibatan Lingkungan", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(10.dp))
            participations.forEach { item ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(
                                    when (item.kategori) {
                                        "Kegiatan Lingkungan", "kegiatan" -> PrimaryBlueLight
                                        "Bantuan Sosial", "bantuan" -> AccentGreenLight
                                        else -> AccentOrangeLight
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (item.kategori) {
                                    "Kegiatan Lingkungan", "kegiatan" -> Icons.Default.Event
                                    "Bantuan Sosial", "bantuan" -> Icons.Default.Handshake
                                    else -> Icons.Default.CheckCircle
                                },
                                contentDescription = null,
                                tint = when (item.kategori) {
                                    "Kegiatan Lingkungan", "kegiatan" -> PrimaryBlue
                                    "Bantuan Sosial", "bantuan" -> AccentGreenDark
                                    else -> AccentOrange
                                },
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.judulKegiatan, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(item.peran, fontSize = 11.sp, color = TextSecondary)
                        }
                        Text(item.tanggal, fontSize = 11.sp, color = TextTertiary)
                    }
                }
            }
        }
    }
}
