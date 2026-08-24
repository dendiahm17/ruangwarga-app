package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.ui.viewmodel.RtrwUiState
import com.example.ui.viewmodel.RtrwViewModel

data class ResidentDisplayItem(
    val name: String,
    val rt: String,
    val role: String,
    val tags: List<Pair<String, Color>>,
    val avatarBg: Color,
    val phone: String
)

@Composable
fun PengurusScreen(
    uiState: RtrwUiState,
    viewModel: RtrwViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val rtFilters = listOf("Semua", "RT 01", "RT 02", "RT 03", "RT 04")
    var selectedRt by remember { mutableStateOf("Semua") }
    var searchQuery by remember { mutableStateOf("") }

    val mockResidentsList = listOf(
        ResidentDisplayItem(
            name = "Budi Santoso",
            rt = "RT 03",
            role = "Kepala Keluarga",
            tags = listOf("Relawan" to Color(0xFFDCFCE7), "Pertukangan" to Color(0xFFFEF3C7)),
            avatarBg = Color(0xFF16A34A),
            phone = "081234567890"
        ),
        ResidentDisplayItem(
            name = "Siti Rahma",
            rt = "RT 03",
            role = "Ibu Rumah Tangga",
            tags = listOf("Kader" to Color(0xFFEDE9FE), "Relawan" to Color(0xFFDCFCE7)),
            avatarBg = Color(0xFFEC4899),
            phone = "081298765432"
        ),
        ResidentDisplayItem(
            name = "Andi Wijaya",
            rt = "RT 03",
            role = "Teknisi",
            tags = listOf("Listrik" to Color(0xFFE0F2FE), "IT" to Color(0xFFE0F2FE)),
            avatarBg = Color(0xFF0284C7),
            phone = "081387654321"
        ),
        ResidentDisplayItem(
            name = "Rina Marlina",
            rt = "RT 03",
            role = "Guru",
            tags = listOf("Pendidikan" to Color(0xFFDCFCE7), "Relawan" to Color(0xFFDCFCE7)),
            avatarBg = Color(0xFF8B5CF6),
            phone = "081566778899"
        ),
        ResidentDisplayItem(
            name = "Joko Prasetyo",
            rt = "RT 03",
            role = "Wiraswasta",
            tags = listOf("Transportasi" to Color(0xFFE0F2FE)),
            avatarBg = Color(0xFF10B981),
            phone = "081711223344"
        ),
        ResidentDisplayItem(
            name = "Hendra Gunawan",
            rt = "RT 01",
            role = "Ketua RW 02",
            tags = listOf("Pengurus RW" to Color(0xFFDCFCE7), "Tokoh Warga" to Color(0xFFFEF3C7)),
            avatarBg = Color(0xFF059669),
            phone = "081298765432"
        ),
        ResidentDisplayItem(
            name = "Agus Prasetyo",
            rt = "RT 02",
            role = "Ketua RT 02",
            tags = listOf("Pengurus RT" to Color(0xFFEDE9FE), "Keamanan" to Color(0xFFFEE2E2)),
            avatarBg = Color(0xFFD97706),
            phone = "081822334455"
        )
    )

    val filteredList = mockResidentsList.filter { item ->
        val matchRt = if (selectedRt == "Semua") true else item.rt == selectedRt
        val matchSearch = if (searchQuery.isBlank()) true else {
            item.name.contains(searchQuery, ignoreCase = true) ||
            item.role.contains(searchQuery, ignoreCase = true) ||
            item.tags.any { it.first.contains(searchQuery, ignoreCase = true) }
        }
        matchRt && matchSearch
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("warga_screen"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Title
        item {
            Text(
                text = "Warga",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Search Bar with Filter Icon
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Cari warga...", fontSize = 13.sp, color = TextTertiary) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = TextTertiary, modifier = Modifier.size(20.dp))
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryGreen,
                        unfocusedBorderColor = BorderLight,
                        focusedContainerColor = Color(0xFFF1F5F9),
                        unfocusedContainerColor = Color(0xFFF1F5F9)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    shape = CircleShape,
                    color = Color(0xFFF1F5F9),
                    modifier = Modifier
                        .size(44.dp)
                        .clickable {
                            Toast.makeText(context, "Filter opsi warga", Toast.LENGTH_SHORT).show()
                        }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(imageVector = Icons.Default.Tune, contentDescription = "Filter", tint = TextSecondary, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }

        // RT Filter Pills (Horizontal)
        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(rtFilters) { rt ->
                    val isSelected = selectedRt == rt
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) PrimaryGreen else Color(0xFFF1F5F9),
                        modifier = Modifier.clickable { selectedRt = rt }
                    ) {
                        Text(
                            text = rt,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else TextPrimary,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Resident Items
        items(filteredList) { resident ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderLight)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(resident.avatarBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Info
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = resident.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${resident.rt} • ${resident.role}",
                            fontSize = 11.5.sp,
                            color = TextSecondary
                        )

                        // Tags
                        if (resident.tags.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                resident.tags.forEach { (tagLabel, tagBg) ->
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = tagBg
                                    ) {
                                        Text(
                                            text = tagLabel,
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = TextPrimary,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Chat Icon Button
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFF8FAFC),
                        border = BorderStroke(1.dp, BorderLight),
                        modifier = Modifier
                            .size(36.dp)
                            .clickable {
                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    data = Uri.parse("https://api.whatsapp.com/send?phone=${resident.phone}&text=Halo%20${resident.name},%20saya%20warga%20RT%2003.")
                                }
                                try {
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Membuka obrolan dengan ${resident.name}", Toast.LENGTH_SHORT).show()
                                }
                            }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.ChatBubbleOutline,
                                contentDescription = "Chat",
                                tint = TextSecondary,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
