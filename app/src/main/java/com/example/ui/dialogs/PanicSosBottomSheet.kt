package com.example.ui.dialogs

import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.fillMaxSize
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EmergencyAlertEntity
import com.example.ui.components.StatusBadge
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentRed
import com.example.ui.theme.AccentRedLight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
data class EmergencyTypeOption(
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val description: String
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanicSosBottomSheet(
    defaultLocation: String,
    recentAlerts: List<EmergencyAlertEntity>,
    isAdminMode: Boolean,
    onDismiss: () -> Unit,
    onTriggerAlert: (jenis: String, lokasi: String, catatan: String) -> Unit,
    onResolveAlert: (id: Int) -> Unit
) {
    val context = LocalContext.current
    var selectedType by remember { mutableStateOf("Keamanan / Maling") }
    var locationInput by remember { mutableStateOf(defaultLocation) }
    var noteInput by remember { mutableStateOf("") }
    var isConfirmingAlert by remember { mutableStateOf(false) }
    val emergencyOptions = listOf(
        EmergencyTypeOption("Keamanan / Maling", Icons.Default.Security, Color(0xFFDC2626), "Penyusup, pencurian, atau gangguan keamanan"),
        EmergencyTypeOption("Kebakaran", Icons.Default.LocalFireDepartment, Color(0xFFEA580C), "Kebakaran rumah, konsleting listrik atau ledakan gas"),
        EmergencyTypeOption("Medis / Kesehatan", Icons.Default.MedicalServices, Color(0xFF0284C7), "Warga serangan jantung, pingsan, kecelakaan parah"),
        EmergencyTypeOption("Bencana / Banjir", Icons.Default.Warning, Color(0xFFD97706), "Pohon tumbang, banjir meluap, atau longsor")
    )
    Dialog(properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false), onDismissRequest = onDismiss) {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(
            modifier = Modifier.fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
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
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(AccentRedLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.WarningAmber,
                            contentDescription = "SOS",
                            tint = AccentRed,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Tombol Darurat (SOS RT)",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentRed
                        )
                        Text(
                            text = "Kirim sinyal bahaya instan ke Pos Kamling & Warga",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Warning Notice
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFEF2F2))
                    .border(1.dp, Color(0xFFFCA5A5), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = "⚠️ Gunakan hanya untuk keadaan darurat nyata! Sinyal akan membunyikan alarm di Pos Satpam RT 03 dan mengirim notifikasi prioritas ke pengurus.",
                    fontSize = 12.sp,
                    color = Color(0xFF991B1B),
                    lineHeight = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Pilih Kategori Darurat
            Text(
                text = "Pilih Jenis Keadaan Darurat:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                emergencyOptions.forEach { option ->
                    val isSelected = selectedType == option.title
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { selectedType = option.title }
                            .testTag("emergency_opt_${option.title}"),
                        colors = CardDefaults.cardColors(containerColor = Color.White), border = androidx.compose.foundation.BorderStroke(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) option.color else BorderLight
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(option.color.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = option.icon,
                                    contentDescription = option.title,
                                    tint = option.color,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = option.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) option.color else TextPrimary
                                )
                                Text(
                                    text = option.description,
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            // Lokasi Darurat
            OutlinedTextField(
                value = locationInput,
                onValueChange = { locationInput = it },
                label = { Text("Lokasi Kejadian") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = AccentRed)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("sos_location_input"),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(10.dp))
            // Catatan Tambahan (Opsional)
            OutlinedTextField(
                value = noteInput,
                onValueChange = { noteInput = it },
                label = { Text("Keterangan Singkat (Opsional)") },
                placeholder = { Text("Contoh: Maling lari ke arah gang belakang") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("sos_note_input"),
                shape = RoundedCornerShape(12.dp),
                minLines = 2
            )
            Spacer(modifier = Modifier.height(18.dp))
            // Big Trigger Button
            if (!isConfirmingAlert) {
                Button(
                    onClick = { isConfirmingAlert = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("trigger_sos_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed), shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(imageVector = Icons.Default.WarningAmber, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "AKTIFKAN SINYAL DARURAT",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White), border = androidx.compose.foundation.BorderStroke(2.dp, AccentRed)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Konfirmasi Pengiriman Sinyal Bahaya?",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentRed
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Sinyal '$selectedType' di '$locationInput' akan segera dibunyikan.",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = { isConfirmingAlert = false },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AccentRed), shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(text = "Batal", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Button(
                                onClick = {
                                    onTriggerAlert(selectedType, locationInput, noteInput)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("confirm_sos_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = AccentRed), shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(text = "YA, KIRIM SEKARANG!", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = BorderLight)
            Spacer(modifier = Modifier.height(16.dp))
            // Direct Call Shortcuts
            Text(
                text = "Panggilan Cepat Darurat:",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                EmergencyCallChip(
                    label = "Satpam",
                    number = "0812-9988-7711",
                    color = Color(0xFF0F172A),
                    modifier = Modifier.weight(1f)
                ) {
                    Toast.makeText(context, "Menghubungi Pos Satpam RT 03...", Toast.LENGTH_SHORT).show()
                }
                EmergencyCallChip(
                    label = "Ketua RT",
                    number = "0812-3456-7890",
                    color = Color(0xFF1E40AF),
                    modifier = Modifier.weight(1f)
                ) {
                    Toast.makeText(context, "Menghubungi Ketua RT (Pak Joko)...", Toast.LENGTH_SHORT).show()
                }
                EmergencyCallChip(
                    label = "Damkar 113",
                    number = "113",
                    color = Color(0xFFB91C1C),
                    modifier = Modifier.weight(1f)
                ) {
                    Toast.makeText(context, "Menghubungi Call Center Damkar 113...", Toast.LENGTH_SHORT).show()
                }
            }
            // Riwayat Alert Aktif
            if (recentAlerts.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Riwayat Peringatan Darurat Terbaru:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                recentAlerts.take(4).forEach { alert ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White), border = androidx.compose.foundation.BorderStroke(
                            width = 1.dp,
                            color = if (alert.status == "Aktif") Color(0xFFFECDD3) else BorderLight
                        )
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
                                if (alert.catatan.isNotBlank()) {
                                    Text(
                                        text = "Catatan: ${alert.catatan}",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                            }
                            if (alert.status == "Aktif") {
                                Button(
                                    onClick = { onResolveAlert(alert.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.height(34.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Selesai", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
        }
    }
}
@Composable
private fun EmergencyCallChip(
    label: String,
    number: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = color),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(text = number, fontSize = 9.sp, color = Color.White.copy(alpha = 0.8f))
        }
    }
}
