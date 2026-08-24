package com.example.ui.dialogs

import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.fillMaxSize
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentGreenDark
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentRed
import com.example.ui.theme.AccentRedDark
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.ui.viewmodel.RtrwViewModel
val EMERGENCY_TYPES = listOf(
    "Kebakaran",
    "Banjir / Genangan Air",
    "Pohon Tumbang",
    "Jalan Terputus / Ambles",
    "Gangguan Keamanan",
    "Medis Darurat",
    "Orang Hilang",
    "Lainnya"
)
val EMERGENCY_LOCATIONS = listOf(
    "Blok A",
    "Blok B",
    "Blok C",
    "Blok D",
    "Blok E",
    "Balai RW 02",
    "Pos Kamling Utama",
    "Gerbang Depan"
)
/**
 * ReportEmergencyBottomSheet - Form Warga Laporkan Keadaan Darurat
 * Alur: Warga mengisi jenis kejadian, lokasi, deskripsi -> Laporan masuk ke pengurus untuk diverifikasi sebelum menjadi Alarm Resmi
 */
@Composable
fun ReportEmergencyBottomSheet(
    viewModel: RtrwViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var selectedType by remember { mutableStateOf(EMERGENCY_TYPES[0]) }
    var selectedLocation by remember { mutableStateOf(EMERGENCY_LOCATIONS[2]) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var hasPhoto by remember { mutableStateOf(false) }
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
                Text(text = "🚨", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Laporkan Keadaan Darurat",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentRedDark
                    )
                    Text(
                        text = "Laporan akan segera diverifikasi oleh Pengurus RW",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }
            IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondary)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Warning Banner
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFFFEE2E2),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = AccentRed, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Gunakan form ini HANYA untuk kejadian darurat yang membutuhkan respon segera.",
                    fontSize = 11.sp,
                    color = AccentRedDark,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 15.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(14.dp))
        // 1. Jenis Kejadian
        Text("Jenis Kejadian Darurat", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(modifier = Modifier.height(6.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(EMERGENCY_TYPES) { type ->
                val isSelected = selectedType == type
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) AccentRed else Color.White,
                    border = BorderStroke(1.dp, if (isSelected) AccentRed else BorderLight),
                    modifier = Modifier.clickable {
                        selectedType = type
                        if (title.isBlank()) title = type.uppercase()
                    }
                ) {
                    Text(
                        text = type,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else TextPrimary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        // 2. Lokasi
        Text("Pilih Lokasi Kejadian", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(modifier = Modifier.height(6.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(EMERGENCY_LOCATIONS) { loc ->
                val isSelected = selectedLocation == loc
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) Color(0xFF1E293B) else Color.White,
                    border = BorderStroke(1.dp, if (isSelected) Color(0xFF1E293B) else BorderLight),
                    modifier = Modifier.clickable { selectedLocation = loc }
                ) {
                    Text(
                        text = loc,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else TextPrimary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        // 3. Judul
        Text("Judul Laporan", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            placeholder = { Text("Contoh: GENANGAN AIR DI BLOK C", fontSize = 12.sp, color = TextTertiary) },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentRed,
                unfocusedBorderColor = BorderLight
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        // 4. Deskripsi
        Text("Deskripsi / Penjelasan Situasi", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            placeholder = { Text("Jelaskan kondisi darurat saat ini secara singkat dan jelas...", fontSize = 12.sp, color = TextTertiary) },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().height(90.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentRed,
                unfocusedBorderColor = BorderLight
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        // 5. Tambah Foto Bukti
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clickable {
                    hasPhoto = !hasPhoto
                    Toast.makeText(context, if (hasPhoto) "Foto bukti ditambahkan" else "Foto dihapus", Toast.LENGTH_SHORT).show()
                },
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = if (hasPhoto) Color(0xFFDCFCE7) else BackgroundLight),
            border = BorderStroke(1.dp, if (hasPhoto) AccentGreen else BorderLight)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.AddAPhoto, contentDescription = null, tint = if (hasPhoto) AccentGreenDark else TextSecondary, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (hasPhoto) "Foto Bukti Terlampir ✓ (Tap ganti)" else "Tambahkan Foto Kejadian (Opsional)",
                    fontSize = 11.5.sp,
                    color = if (hasPhoto) AccentGreenDark else TextSecondary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        // Submit Button
        Button(
            onClick = {
                val finalTitle = title.ifBlank { selectedType.uppercase() }
                viewModel.reportEmergencyAlert(
                    jenisDarurat = selectedType,
                    judul = finalTitle,
                    lokasi = selectedLocation,
                    catatan = description.ifBlank { "Laporan darurat $selectedType di $selectedLocation." }
                )
                onDismiss()
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
            modifier = Modifier.fillMaxWidth().height(46.dp)
        ) {
            Text("KIRIM LAPORAN DARURAT", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}
