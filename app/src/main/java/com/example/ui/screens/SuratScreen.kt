package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import com.example.data.model.LetterRequestEntity
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
import com.example.ui.theme.BorderLight
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryBlueLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.RtrwUiState
import com.example.ui.viewmodel.RtrwViewModel

@Composable
fun SuratScreen(
    uiState: RtrwUiState,
    viewModel: RtrwViewModel,
    modifier: Modifier = Modifier
) {
    val letterTypes = listOf(
        "Surat Pengantar",
        "Surat Keterangan Domisili",
        "Surat Keterangan Usaha",
        "Surat Keterangan Tidak Mampu",
        "Surat Kelahiran",
        "Surat Kematian",
        "Surat Pindah",
        "Surat Pengantar SKCK",
        "Surat Lainnya"
    )

    val filteredLetters = uiState.letters.filter { letter ->
        val matchesCategory = when (uiState.suratFilter) {
            "Semua" -> true
            else -> letter.status.equals(uiState.suratFilter, ignoreCase = true)
        }
        val matchesSearch = if (uiState.suratSearchQuery.isBlank()) true else {
            letter.jenisSurat.contains(uiState.suratSearchQuery, ignoreCase = true) ||
            letter.nomorSurat.contains(uiState.suratSearchQuery, ignoreCase = true) ||
            letter.keperluan.contains(uiState.suratSearchQuery, ignoreCase = true)
        }
        matchesCategory && matchesSearch
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("surat_screen")
    ) {
        // App Header
        item {
            AppHeader(
                title = "Layanan Surat",
                unreadCount = uiState.unreadNotifications,
                onNotificationClick = { viewModel.openNotificationsSheet() },
                isAdminMode = uiState.isAdminMode,
                onAdminToggle = { viewModel.toggleAdminMode() }
            )
        }

        // Search Bar
        item {
            AppSearchBar(
                query = uiState.suratSearchQuery,
                onQueryChange = { viewModel.setSuratSearchQuery(it) },
                placeholder = "Cari jenis surat atau nomor surat..."
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        // Quick Category Status Buttons
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                QuickActionCircleButton(
                    label = "Pengajuan",
                    icon = Icons.Default.Description,
                    iconColor = PrimaryBlue,
                    bgColor = PrimaryBlueLight,
                    isSelected = uiState.suratFilter == "Pengajuan",
                    onClick = {
                        viewModel.setSuratFilter(
                            if (uiState.suratFilter == "Pengajuan") "Semua" else "Pengajuan"
                        )
                    }
                )
                QuickActionCircleButton(
                    label = "Diproses",
                    icon = Icons.Default.HourglassTop,
                    iconColor = AccentOrange,
                    bgColor = AccentOrangeLight,
                    isSelected = uiState.suratFilter == "Diproses",
                    onClick = {
                        viewModel.setSuratFilter(
                            if (uiState.suratFilter == "Diproses") "Semua" else "Diproses"
                        )
                    }
                )
                QuickActionCircleButton(
                    label = "Selesai",
                    icon = Icons.Default.CheckCircle,
                    iconColor = AccentGreen,
                    bgColor = AccentGreenLight,
                    isSelected = uiState.suratFilter == "Selesai",
                    onClick = {
                        viewModel.setSuratFilter(
                            if (uiState.suratFilter == "Selesai") "Semua" else "Selesai"
                        )
                    }
                )
                QuickActionCircleButton(
                    label = "Arsip",
                    icon = Icons.Default.Archive,
                    iconColor = AccentPurple,
                    bgColor = AccentPurpleLight,
                    isSelected = uiState.suratFilter == "Arsip",
                    onClick = {
                        viewModel.setSuratFilter(
                            if (uiState.suratFilter == "Arsip") "Semua" else "Arsip"
                        )
                    }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        // If a status filter or search is active, show filtered results
        if (uiState.suratFilter != "Semua" || uiState.suratSearchQuery.isNotBlank()) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Hasil Filter: ${filteredLetters.size} Surat",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Reset Filter",
                        fontSize = 12.sp,
                        color = PrimaryBlue,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable {
                            viewModel.setSuratFilter("Semua")
                            viewModel.setSuratSearchQuery("")
                        }
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
            }

            if (filteredLetters.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Tidak ditemukan permohonan surat yang cocok.",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    }
                }
            } else {
                items(filteredLetters, key = { it.id }) { letter ->
                    SuratItemCard(
                        letter = letter,
                        isAdminMode = uiState.isAdminMode,
                        onClick = { viewModel.openLetterDetail(letter) },
                        onAdminApprove = { viewModel.adminApproveLetter(letter.id) },
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Section: Buat Pengajuan Surat Baru
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = "Buat Pengajuan Surat",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))

                letterTypes.forEach { type ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { viewModel.openCreateLetterSheet(type) }
                            .testTag("letter_type_${type.lowercase().replace(" ", "_")}"),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(PrimaryBlueLight),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Description,
                                        contentDescription = type,
                                        tint = PrimaryBlue,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = type,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                            }
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = "Pilih",
                                tint = TextSecondary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }

        // Section: Riwayat Pengajuan Surat (When filter is Semua)
        if (uiState.suratFilter == "Semua" && uiState.suratSearchQuery.isBlank()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = "Riwayat Pengajuan Surat",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            items(uiState.letters, key = { "history_${it.id}" }) { letter ->
                SuratItemCard(
                    letter = letter,
                    isAdminMode = uiState.isAdminMode,
                    onClick = { viewModel.openLetterDetail(letter) },
                    onAdminApprove = { viewModel.adminApproveLetter(letter.id) },
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun SuratItemCard(
    letter: LetterRequestEntity,
    isAdminMode: Boolean = false,
    onClick: () -> Unit,
    onAdminApprove: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .testTag("surat_card_${letter.id}"),
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
                Text(
                    text = letter.jenisSurat,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                StatusBadge(status = letter.status)
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "No: ${letter.nomorSurat}",
                fontSize = 11.sp,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = letter.keperluan,
                fontSize = 12.sp,
                color = TextPrimary,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Diajukan: ${letter.tanggalPengajuan}",
                fontSize = 10.sp,
                color = TextSecondary
            )

            if (isAdminMode && letter.status == "Pengajuan") {
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = BorderLight)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Aksi Pengurus:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                    Button(
                        onClick = { onAdminApprove?.invoke() },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text("Setujui Langsung", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}
