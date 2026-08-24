package com.example.ui.dialogs

import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentGreenDark
import com.example.ui.theme.AccentGreenLight
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentOrangeDark
import com.example.ui.theme.AccentOrangeLight
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentPurpleDark
import com.example.ui.theme.AccentPurpleLight
import com.example.ui.theme.AccentRed
import com.example.ui.theme.AccentRedDark
import com.example.ui.theme.AccentRedLight
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryBlueDark
import com.example.ui.theme.PrimaryBlueLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
data class AktivitasFilterCategoryItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val iconTint: Color,
    val iconBg: Color
)
val AKTIVITAS_FILTER_CATEGORIES = listOf(
    AktivitasFilterCategoryItem("Semua", "Semua", "Lihat semua aktivitas", Icons.Default.Check, AccentGreenDark, AccentGreenLight),
    AktivitasFilterCategoryItem("Kegiatan", "Kegiatan", "Rencana atau undangan kegiatan", Icons.Default.Event, AccentGreenDark, AccentGreenLight),
    AktivitasFilterCategoryItem("Pengumuman", "Pengumuman", "Informasi resmi dari pengurus", Icons.Default.Campaign, PrimaryBlueDark, PrimaryBlueLight),
    AktivitasFilterCategoryItem("Kejadian", "Kejadian", "Masalah atau kejadian di lingkungan", Icons.Default.ReportProblem, AccentRedDark, AccentRedLight),
    AktivitasFilterCategoryItem("Bantuan", "Bantuan", "Permintaan atau kesempatan membantu", Icons.Default.VolunteerActivism, AccentRedDark, Color(0xFFFCE7F3)),
    AktivitasFilterCategoryItem("Usulan", "Usulan", "Gagasan atau usulan untuk lingkungan", Icons.Default.Lightbulb, Color(0xFFD97706), Color(0xFFFEF3C7)),
    AktivitasFilterCategoryItem("Polling", "Polling", "Jajak pendapat warga", Icons.Default.HowToVote, AccentPurpleDark, AccentPurpleLight),
    AktivitasFilterCategoryItem("Dokumentasi", "Dokumentasi", "Foto atau video kegiatan", Icons.Default.PhotoLibrary, PrimaryBlue, Color(0xFFE0F2FE)),
    AktivitasFilterCategoryItem("Apresiasi", "Apresiasi", "Penghargaan untuk kontribusi warga", Icons.Default.Star, Color(0xFFD97706), Color(0xFFFEF3C7))
)
val TIME_FILTER_OPTIONS = listOf(
    "Semua Waktu",
    "Hari Ini",
    "Minggu Ini",
    "Bulan Ini",
    "Tahun Ini"
)
/**
 * AktivitasFilterBottomSheet - Layar 2 (Mockup 2: Filter Aktivitas)
 * Berisi grid 8 kategori aktivitas dengan deskripsi + radio selector filter waktu + tombol aksi Terapkan & Reset.
 */
@Composable
fun AktivitasFilterBottomSheet(
    currentCategory: String,
    currentTime: String,
    onApply: (category: String, time: String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf(currentCategory) }
    var selectedTime by remember { mutableStateOf(currentTime) }
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
            Text(
                text = "Filter Aktivitas",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondary)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // 1. Kategori Section
        Text(
            text = "Kategori",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(10.dp))
        // 3x3 Grid Cards
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            val chunkedCategories = AKTIVITAS_FILTER_CATEGORIES.chunked(3)
            for (row in chunkedCategories) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (cat in row) {
                        val isSelected = selectedCategory.equals(cat.id, ignoreCase = true)
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(88.dp)
                                .clickable { selectedCategory = cat.id },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) Color(0xFFF0FDF4) else Color.White
                            ),
                            border = BorderStroke(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) AccentGreen else BorderLight
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(cat.iconBg),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = cat.icon,
                                            contentDescription = null,
                                            tint = cat.iconTint,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                    if (isSelected) {
                                        Box(
                                            modifier = Modifier
                                                .size(16.dp)
                                                .clip(CircleShape)
                                                .background(AccentGreen),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(10.dp)
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = cat.title,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) AccentGreenDark else TextPrimary,
                                    maxLines = 1
                                )
                                Text(
                                    text = cat.subtitle,
                                    fontSize = 8.5.sp,
                                    color = TextTertiary,
                                    maxLines = 2,
                                    lineHeight = 11.sp
                                )
                            }
                        }
                    }
                    // Fill remainder of row if incomplete
                    if (row.size < 3) {
                        for (i in 0 until (3 - row.size)) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(18.dp))
        // 2. Waktu Section
        Text(
            text = "Waktu",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(TIME_FILTER_OPTIONS) { timeOpt ->
                val isSelected = selectedTime == timeOpt
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSelected) Color(0xFFF0FDF4) else Color.White,
                    border = BorderStroke(1.dp, if (isSelected) AccentGreen else BorderLight),
                    modifier = Modifier.clickable { selectedTime = timeOpt }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .clip(CircleShape)
                                    .background(AccentGreen),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(9.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .clip(CircleShape)
                                    .border(1.dp, BorderLight, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                        Text(
                            text = timeOpt,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) AccentGreenDark else TextPrimary
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        // 3. Tombol Terapkan Filter
        Button(
            onClick = {
                onApply(selectedCategory, selectedTime)
                onDismiss()
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
        ) {
            Text("Terapkan Filter", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
        Spacer(modifier = Modifier.height(6.dp))
        // 4. Tombol Reset
        TextButton(
            onClick = {
                selectedCategory = "Semua"
                selectedTime = "Semua Waktu"
                onApply("Semua", "Semua Waktu")
                onDismiss()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reset", fontSize = 13.sp, color = TextSecondary, fontWeight = FontWeight.Medium)
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}
