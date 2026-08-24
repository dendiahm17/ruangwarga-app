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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.data.model.AnnouncementRecordEntity
import com.example.ui.components.AppHeader
import com.example.ui.components.AppSearchBar
import com.example.ui.components.StatusBadge
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentOrangeLight
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
fun PengumumanScreen(
    uiState: RtrwUiState,
    viewModel: RtrwViewModel,
    modifier: Modifier = Modifier
) {
    val filterCategories = listOf("Semua", "RT 03", "RW 02", "Penting")

    val filteredAnnouncements = uiState.announcements.filter { announcement ->
        val matchesCategory = when (uiState.pengumumanFilter) {
            "Semua" -> true
            "Penting" -> announcement.isPenting
            "RT 03" -> announcement.kategori == "RT 03" || announcement.lingkup == "RT 03"
            "RW 02" -> announcement.kategori == "RW 02" || announcement.lingkup == "RW 02"
            else -> true
        }
        val matchesSearch = if (uiState.pengumumanSearchQuery.isBlank()) true else {
            announcement.judul.contains(uiState.pengumumanSearchQuery, ignoreCase = true) ||
            announcement.ringkasan.contains(uiState.pengumumanSearchQuery, ignoreCase = true) ||
            announcement.konten.contains(uiState.pengumumanSearchQuery, ignoreCase = true)
        }
        matchesCategory && matchesSearch
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("pengumuman_screen")
    ) {
        item {
            AppHeader(
                title = "Pengumuman Warga",
                unreadCount = uiState.unreadNotifications,
                showBackButton = uiState.showAllAnnouncementsScreen,
                onBackClick = { viewModel.setShowAllAnnouncements(false) },
                onNotificationClick = { viewModel.openNotificationsSheet() }
            )
        }

        item {
            AppSearchBar(
                query = uiState.pengumumanSearchQuery,
                onQueryChange = { viewModel.setPengumumanSearchQuery(it) },
                placeholder = "Cari judul pengumuman kegiatan..."
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        // Filter Chips Row
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filterCategories) { category ->
                    val isSelected = uiState.pengumumanFilter == category
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) PrimaryBlue else Color(0xFFF1F5F9))
                            .clickable { viewModel.setPengumumanFilter(category) }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .testTag("filter_chip_${category.lowercase().replace(" ", "_")}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = category,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else TextSecondary
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        if (filteredAnnouncements.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Tidak ada pengumuman yang sesuai.",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            items(filteredAnnouncements, key = { it.id }) { announcement ->
                AnnouncementItemCard(
                    announcement = announcement,
                    onClick = { viewModel.openAnnouncementDetail(announcement) },
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun AnnouncementItemCard(
    announcement: AnnouncementRecordEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("announcement_card_${announcement.id}"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (announcement.isPenting) AccentRedLight else PrimaryBlueLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Campaign,
                            contentDescription = "Pengumuman",
                            tint = if (announcement.isPenting) AccentRed else PrimaryBlue,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = announcement.judul,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = announcement.tanggalPosting,
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                    }
                }

                if (announcement.isPenting) {
                    StatusBadge(status = "Penting")
                } else if (announcement.isBaru) {
                    StatusBadge(status = "Baru")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = announcement.ringkasan,
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 16.sp
            )

            if (announcement.waktuKegiatan != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF8FAFC), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${announcement.waktuKegiatan} • ${announcement.tempatKegiatan ?: ""}",
                        fontSize = 11.sp,
                        color = TextPrimary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
