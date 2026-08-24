package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.WaterDamage
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ComplaintRecordEntity
import com.example.ui.components.AppHeader
import com.example.ui.components.AppSearchBar
import com.example.ui.components.QuickActionCircleButton
import com.example.ui.components.StatusBadge
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentGreenLight
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentOrangeLight
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentPurpleLight
import com.example.ui.theme.AccentRed
import com.example.ui.theme.AccentRedLight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryBlueLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.RtrwUiState
import com.example.ui.viewmodel.RtrwViewModel

@Composable
fun PengaduanScreen(
    uiState: RtrwUiState,
    viewModel: RtrwViewModel,
    modifier: Modifier = Modifier
) {
    val filteredComplaints = uiState.complaints.filter { complaint ->
        val matchesCategory = when (uiState.pengaduanFilter) {
            "Semua", "Riwayat" -> true
            "Diproses" -> complaint.status.equals("Diproses", ignoreCase = true)
            "Selesai" -> complaint.status.equals("Selesai", ignoreCase = true)
            else -> true
        }
        val matchesSearch = if (uiState.pengaduanSearchQuery.isBlank()) true else {
            complaint.judul.contains(uiState.pengaduanSearchQuery, ignoreCase = true) ||
            complaint.lokasi.contains(uiState.pengaduanSearchQuery, ignoreCase = true) ||
            complaint.kategori.contains(uiState.pengaduanSearchQuery, ignoreCase = true) ||
            complaint.deskripsi.contains(uiState.pengaduanSearchQuery, ignoreCase = true)
        }
        matchesCategory && matchesSearch
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp)
                .testTag("pengaduan_screen")
        ) {
            item {
                AppHeader(
                    title = "Layanan Pengaduan",
                    unreadCount = uiState.unreadNotifications,
                    onNotificationClick = { viewModel.openNotificationsSheet() },
                    isAdminMode = uiState.isAdminMode,
                    onAdminToggle = { viewModel.toggleAdminMode() }
                )
            }

            item {
                AppSearchBar(
                    query = uiState.pengaduanSearchQuery,
                    onQueryChange = { viewModel.setPengaduanSearchQuery(it) },
                    placeholder = "Cari pengaduan atau lokasi..."
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            // 4 Top Quick Action Buttons
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    QuickActionCircleButton(
                        label = "Buat Baru",
                        icon = Icons.Default.Add,
                        iconColor = AccentGreen,
                        bgColor = AccentGreenLight,
                        isSelected = false,
                        onClick = { viewModel.openCreateComplaintSheet() }
                    )
                    QuickActionCircleButton(
                        label = "Diproses",
                        icon = Icons.Default.Engineering,
                        iconColor = AccentOrange,
                        bgColor = AccentOrangeLight,
                        isSelected = uiState.pengaduanFilter == "Diproses",
                        onClick = {
                            viewModel.setPengaduanFilter(
                                if (uiState.pengaduanFilter == "Diproses") "Semua" else "Diproses"
                            )
                        }
                    )
                    QuickActionCircleButton(
                        label = "Selesai",
                        icon = Icons.Default.CheckCircle,
                        iconColor = PrimaryBlue,
                        bgColor = PrimaryBlueLight,
                        isSelected = uiState.pengaduanFilter == "Selesai",
                        onClick = {
                            viewModel.setPengaduanFilter(
                                if (uiState.pengaduanFilter == "Selesai") "Semua" else "Selesai"
                            )
                        }
                    )
                    QuickActionCircleButton(
                        label = "Riwayat",
                        icon = Icons.Default.History,
                        iconColor = AccentPurple,
                        bgColor = AccentPurpleLight,
                        isSelected = uiState.pengaduanFilter == "Riwayat",
                        onClick = {
                            viewModel.setPengaduanFilter(
                                if (uiState.pengaduanFilter == "Riwayat") "Semua" else "Riwayat"
                            )
                        }
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Section Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (uiState.pengaduanFilter == "Semua") "Daftar Pengaduan Warga" else "Pengaduan (${uiState.pengaduanFilter})",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    if (uiState.pengaduanFilter != "Semua" || uiState.pengaduanSearchQuery.isNotBlank()) {
                        Text(
                            text = "Reset",
                            fontSize = 12.sp,
                            color = PrimaryBlue,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable {
                                viewModel.setPengaduanFilter("Semua")
                                viewModel.setPengaduanSearchQuery("")
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            if (filteredComplaints.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Tidak ada laporan pengaduan yang sesuai.",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    }
                }
            } else {
                items(filteredComplaints, key = { it.id }) { complaint ->
                    ComplaintItemCard(
                        complaint = complaint,
                        isAdminMode = uiState.isAdminMode,
                        onClick = { viewModel.openComplaintDetail(complaint) },
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Floating Action Button to Create Complaint
        FloatingActionButton(
            onClick = { viewModel.openCreateComplaintSheet() },
            containerColor = PrimaryBlue,
            contentColor = Color.White,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 20.dp)
                .testTag("fab_create_complaint")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Buat Pengaduan")
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Lapor", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun ComplaintItemCard(
    complaint: ComplaintRecordEntity,
    isAdminMode: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .testTag("complaint_card_${complaint.id}"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                when (complaint.kategori) {
                                    "Fasilitas Umum" -> PrimaryBlueLight
                                    "Kebersihan" -> AccentGreenLight
                                    "Infrastruktur" -> AccentOrangeLight
                                    else -> AccentPurpleLight
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (complaint.kategori) {
                                "Fasilitas Umum" -> Icons.Default.Lightbulb
                                "Kebersihan" -> Icons.Default.CleaningServices
                                "Infrastruktur" -> Icons.Default.WaterDamage
                                else -> Icons.Default.Build
                            },
                            contentDescription = complaint.kategori,
                            tint = when (complaint.kategori) {
                                "Fasilitas Umum" -> PrimaryBlue
                                "Kebersihan" -> AccentGreen
                                "Infrastruktur" -> AccentOrange
                                else -> AccentPurple
                            },
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = complaint.judul,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = complaint.lokasi,
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
                StatusBadge(status = complaint.status)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = complaint.deskripsi,
                fontSize = 12.sp,
                color = TextSecondary,
                maxLines = 2,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${complaint.tanggal} • ${complaint.waktu}",
                    fontSize = 10.sp,
                    color = TextSecondary
                )

                if (complaint.fotoBukti != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(PrimaryBlueLight)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Icon(imageVector = Icons.Default.AddPhotoAlternate, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Foto Bukti", fontSize = 9.sp, color = PrimaryBlue, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
