package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.MarkEmailUnread
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NotificationEntity
import com.example.ui.components.AppSearchBar
import com.example.ui.components.FilterChipTab
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
import com.example.ui.viewmodel.MainTab
import com.example.ui.viewmodel.RtrwUiState
import com.example.ui.viewmodel.RtrwViewModel

/**
 * PesanScreen - Single Centralized Kotak Masuk (Filterable)
 * Sesuai Mockup Gambar 2 & 4.
 * Satu-satunya pusat pesan & notifikasi warga tanpa duplikasi lonceng.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PesanScreen(
    uiState: RtrwUiState,
    viewModel: RtrwViewModel,
    modifier: Modifier = Modifier
) {
    var isSearchActive by remember { mutableStateOf(false) }
    val categories = listOf("Semua", "Penting", "Kegiatan", "Pengumuman", "Sosial", "Surat", "Iuran")

    // Filter and Sort Messages
    val filteredMessages = uiState.notifications.filter { item ->
        val matchesCategory = when (uiState.pesanFilter) {
            "Semua" -> true
            "Penting" -> item.tipe.contains("Darurat", ignoreCase = true) || item.tipe.contains("Penting", ignoreCase = true) || item.judul.contains("Rapat", ignoreCase = true)
            "Kegiatan" -> item.tipe.contains("Kegiatan", ignoreCase = true) || item.tipe.contains("Ronda", ignoreCase = true)
            "Pengumuman" -> item.tipe.contains("Pengumuman", ignoreCase = true)
            "Sosial" -> item.tipe.contains("Sosial", ignoreCase = true) || item.tipe.contains("Bantuan", ignoreCase = true)
            "Surat" -> item.tipe.contains("Surat", ignoreCase = true)
            "Iuran" -> item.tipe.contains("Iuran", ignoreCase = true) || item.tipe.contains("Keuangan", ignoreCase = true)
            "Laporan" -> item.tipe.contains("Laporan", ignoreCase = true) || item.tipe.contains("Kejadian", ignoreCase = true)
            else -> true
        }

        val matchesReadStatus = when (uiState.pesanReadStatusFilter) {
            "Belum Dibaca" -> !item.isDibaca
            "Sudah Dibaca" -> item.isDibaca
            else -> true
        }

        val matchesSearch = if (uiState.pesanSearchQuery.isBlank()) true else {
            item.judul.contains(uiState.pesanSearchQuery, ignoreCase = true) ||
            item.pesan.contains(uiState.pesanSearchQuery, ignoreCase = true) ||
            item.tipe.contains(uiState.pesanSearchQuery, ignoreCase = true)
        }

        matchesCategory && matchesReadStatus && matchesSearch
    }.sortedByDescending { it.id }

    // Grouping by Timeline (Hari ini, Kemarin, 2 hari lalu)
    val todayMessages = filteredMessages.filter { it.waktuLalu.contains("menit", ignoreCase = true) || it.waktuLalu.contains("jam", ignoreCase = true) || it.waktuLalu.contains("08.", ignoreCase = true) || it.waktuLalu.contains("07.", ignoreCase = true) || it.waktuLalu.contains("baru", ignoreCase = true) }
    val yesterdayMessages = filteredMessages.filter { it.waktuLalu.contains("kemarin", ignoreCase = true) || it.waktuLalu.contains("1 hari", ignoreCase = true) }
    val olderMessages = filteredMessages.filter { it !in todayMessages && it !in yesterdayMessages }

    Box(modifier = modifier.fillMaxSize().background(BackgroundLight)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("pesan_screen_inbox")
        ) {
            // ============================================================
            // 1. HEADER (KOTAK MASUK + SEARCH + FILTER MODAL BUTTON)
            // ============================================================
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding(),
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "📥", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Kotak Masuk",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            IconButton(
                                onClick = { isSearchActive = !isSearchActive },
                                modifier = Modifier.size(38.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Search,
                                    contentDescription = "Cari Pesan",
                                    tint = TextPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            IconButton(
                                onClick = { viewModel.openPesanFilterSheet() },
                                modifier = Modifier.size(38.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FilterList,
                                    contentDescription = "Filter Kotak Masuk",
                                    tint = PrimaryBlue,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }
            }

            // ============================================================
            // REFERENSI ALARM DARURAT (Bukan Pesan Biasa, Banner Referensi)
            // ============================================================
            val activeEmergencies = uiState.emergencyAlerts.filter { it.status in listOf("Aktif", "Ditangani", "Terkendali") && it.isVerified }
            if (activeEmergencies.isNotEmpty()) {
                item {
                    val topEmergency = activeEmergencies.first()
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .clickable { viewModel.openEmergencyAlarmDetail(topEmergency) },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEE2E2)),
                        border = BorderStroke(1.dp, AccentRed)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Text(text = "🚨", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Ada keadaan darurat aktif di sekitar Anda",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AccentRedDark
                                    )
                                    Text(
                                        text = topEmergency.judul,
                                        fontSize = 10.5.sp,
                                        color = TextSecondary,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = AccentRed
                            ) {
                                Text(
                                    text = "LIHAT ALARM",
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Search Bar if toggled
            if (isSearchActive) {
                item {
                    AppSearchBar(
                        query = uiState.pesanSearchQuery,
                        onQueryChange = { viewModel.setPesanSearchQuery(it) },
                        placeholder = "Cari pengumuman, kegiatan, surat..."
                    )
                }
            }

            // ============================================================
            // 2. HORIZONTAL FILTER CHIPS ROW
            // ============================================================
            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(vertical = 8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        val isSelected = uiState.pesanFilter == category
                        FilterChipTab(
                            label = category,
                            isSelected = isSelected,
                            onClick = { viewModel.setPesanFilter(category) }
                        )
                    }
                }
                HorizontalDivider(color = BorderLight, thickness = 1.dp)
            }

            // ============================================================
            // 3. TIMELINE: HARI INI
            // ============================================================
            if (todayMessages.isNotEmpty() || (yesterdayMessages.isEmpty() && olderMessages.isEmpty() && filteredMessages.isNotEmpty())) {
                item {
                    TimelineSectionHeader(title = "Hari ini")
                }

                val listToShow = if (todayMessages.isNotEmpty()) todayMessages else filteredMessages.take(3)
                items(listToShow) { message ->
                    PesanListItemCard(
                        message = message,
                        onClick = { viewModel.openMessageDetail(message) },
                        onContextActionClick = { handleContextAction(message, viewModel) }
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }

            // ============================================================
            // 4. TIMELINE: KEMARIN
            // ============================================================
            if (yesterdayMessages.isNotEmpty()) {
                item {
                    TimelineSectionHeader(title = "Kemarin")
                }

                items(yesterdayMessages) { message ->
                    PesanListItemCard(
                        message = message,
                        onClick = { viewModel.openMessageDetail(message) },
                        onContextActionClick = { handleContextAction(message, viewModel) }
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }

            // ============================================================
            // 5. TIMELINE: 2 HARI LALU / SEBELUMNYA
            // ============================================================
            if (olderMessages.isNotEmpty()) {
                item {
                    TimelineSectionHeader(title = "2 hari lalu")
                }

                items(olderMessages) { message ->
                    PesanListItemCard(
                        message = message,
                        onClick = { viewModel.openMessageDetail(message) },
                        onContextActionClick = { handleContextAction(message, viewModel) }
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }

            // Empty state
            if (filteredMessages.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.MarkEmailRead,
                                contentDescription = null,
                                tint = AccentGreenDark,
                                modifier = Modifier.size(54.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Tidak ada pesan",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Semua pesan telah dibaca atau sesuai filter Anda.",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(90.dp))
            }
        }

        // ============================================================
        // 6. FILTER MODAL BOTTOM SHEET (SESUAI MOCKUP GAMBAR 4)
        // ============================================================
        if (uiState.showPesanFilterSheet) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.closePesanFilterSheet() },
                containerColor = Color.White,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                PesanFilterBottomSheetContent(
                    currentCategory = uiState.pesanFilter,
                    currentReadStatus = uiState.pesanReadStatusFilter,
                    currentSort = uiState.pesanSortOrder,
                    onApply = { cat, status, sort ->
                        viewModel.setPesanFilter(cat)
                        viewModel.setPesanReadStatusFilter(status)
                        viewModel.setPesanSortOrder(sort)
                        viewModel.closePesanFilterSheet()
                    },
                    onReset = {
                        viewModel.setPesanFilter("Semua")
                        viewModel.setPesanReadStatusFilter("Semua")
                        viewModel.setPesanSortOrder("Terbaru")
                        viewModel.closePesanFilterSheet()
                    }
                )
            }
        }
    }
}

@Composable
fun TimelineSectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = TextSecondary,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
    )
}

/**
 * Message Card item in list with contextual badge & time indicator
 */
@Composable
fun PesanListItemCard(
    message: NotificationEntity,
    onClick: () -> Unit,
    onContextActionClick: () -> Unit
) {
    val meta = getNotificationStyle(message.tipe)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(1.dp, RoundedCornerShape(14.dp), spotColor = Color(0x0F000000))
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (message.isDibaca) Color.White else Color(0xFFF0FDF4) // Soft green tint for unread
        ),
        border = BorderStroke(1.dp, if (message.isDibaca) BorderLight else AccentGreenLight)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Type Icon
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(meta.bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = meta.icon,
                    contentDescription = null,
                    tint = meta.tintColor,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Main Message Text
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${message.tipe} - ${message.judul}",
                        fontSize = 13.sp,
                        fontWeight = if (message.isDibaca) FontWeight.SemiBold else FontWeight.Bold,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = message.waktuLalu,
                            fontSize = 10.sp,
                            color = TextTertiary
                        )
                        if (!message.isDibaca) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(AccentGreen)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = message.pesan,
                    fontSize = 11.sp,
                    color = TextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 15.sp
                )
            }
        }
    }
}

/**
 * Filter Bottom Sheet Content (Mockup Gambar 4)
 */
@Composable
fun PesanFilterBottomSheetContent(
    currentCategory: String,
    currentReadStatus: String,
    currentSort: String,
    onApply: (category: String, readStatus: String, sort: String) -> Unit,
    onReset: () -> Unit
) {
    var selectedCat by remember { mutableStateOf(currentCategory) }
    var selectedStatus by remember { mutableStateOf(currentReadStatus) }
    var selectedSort by remember { mutableStateOf(currentSort) }

    val categories = listOf(
        Pair("Semua", Icons.Default.Info),
        Pair("Penting", Icons.Default.Warning),
        Pair("Kegiatan", Icons.Default.Event),
        Pair("Pengumuman", Icons.Default.Campaign),
        Pair("Sosial", Icons.Default.VolunteerActivism),
        Pair("Komunitas", Icons.Default.Info),
        Pair("Laporan / Kejadian", Icons.Default.ReportProblem),
        Pair("Surat", Icons.Default.Description),
        Pair("Iuran / Keuangan", Icons.Default.Payments)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Filter Kotak Masuk",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Text(
                text = "Reset",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = AccentGreenDark,
                modifier = Modifier
                    .clickable { onReset() }
                    .padding(6.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Category Section
        Text("Kategori", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(modifier = Modifier.height(6.dp))

        LazyColumn(modifier = Modifier.height(200.dp)) {
            items(categories) { (cat, icon) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedCat = cat }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedCat == cat,
                        onClick = { selectedCat = cat },
                        colors = RadioButtonDefaults.colors(selectedColor = AccentGreen)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (selectedCat == cat) AccentGreenDark else TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = cat,
                        fontSize = 12.sp,
                        fontWeight = if (selectedCat == cat) FontWeight.Bold else FontWeight.Normal,
                        color = TextPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider(color = BorderLight)
        Spacer(modifier = Modifier.height(10.dp))

        // Status Section
        Text("Status", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(modifier = Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            listOf("Semua", "Belum dibaca", "Sudah dibaca").forEach { status ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { selectedStatus = status }
                ) {
                    RadioButton(
                        selected = selectedStatus == status,
                        onClick = { selectedStatus = status },
                        colors = RadioButtonDefaults.colors(selectedColor = AccentGreen)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(status, fontSize = 11.sp, color = TextPrimary)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider(color = BorderLight)
        Spacer(modifier = Modifier.height(10.dp))

        // Urutan Sorting
        Text("Urutkan", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(modifier = Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            listOf("Terbaru", "Terlama", "Paling penting").forEach { sort ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { selectedSort = sort }
                ) {
                    RadioButton(
                        selected = selectedSort == sort,
                        onClick = { selectedSort = sort },
                        colors = RadioButtonDefaults.colors(selectedColor = AccentGreen)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(sort, fontSize = 11.sp, color = TextPrimary)
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Apply Button
        Button(
            onClick = { onApply(selectedCat, selectedStatus, selectedSort) },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
        ) {
            Text("Terapkan Filter", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(14.dp))
    }
}

// Style helper
data class NotificationStyle(val icon: ImageVector, val tintColor: Color, val bgColor: Color)

private fun getNotificationStyle(tipe: String): NotificationStyle {
    return when {
        tipe.contains("Kegiatan", ignoreCase = true) || tipe.contains("Ronda", ignoreCase = true) ->
            NotificationStyle(Icons.Default.Event, AccentGreenDark, AccentGreenLight)
        tipe.contains("Pengumuman", ignoreCase = true) ->
            NotificationStyle(Icons.Default.Campaign, AccentOrangeDark, AccentOrangeLight)
        tipe.contains("Sosial", ignoreCase = true) || tipe.contains("Bantuan", ignoreCase = true) ->
            NotificationStyle(Icons.Default.VolunteerActivism, AccentRedDark, AccentRedLight)
        tipe.contains("Surat", ignoreCase = true) ->
            NotificationStyle(Icons.Default.Description, PrimaryBlueDark, PrimaryBlueLight)
        tipe.contains("Iuran", ignoreCase = true) ->
            NotificationStyle(Icons.Default.Payments, AccentOrangeDark, AccentOrangeLight)
        tipe.contains("Laporan", ignoreCase = true) || tipe.contains("Kejadian", ignoreCase = true) ->
            NotificationStyle(Icons.Default.Warning, AccentPurpleDark, AccentPurpleLight)
        else ->
            NotificationStyle(Icons.Default.Info, PrimaryBlue, PrimaryBlueLight)
    }
}

private fun handleContextAction(message: NotificationEntity, viewModel: RtrwViewModel) {
    when {
        message.tipe.contains("Kegiatan", ignoreCase = true) -> viewModel.selectTab(MainTab.AKTIVITAS)
        message.tipe.contains("Sosial", ignoreCase = true) -> viewModel.selectTab(MainTab.SOSIAL)
        message.tipe.contains("Surat", ignoreCase = true) -> viewModel.openSuratScreenSheet()
        message.tipe.contains("Iuran", ignoreCase = true) -> viewModel.openIuranScreenSheet()
        message.tipe.contains("Pengumuman", ignoreCase = true) -> viewModel.openAllAnnouncementsScreen()
        else -> viewModel.openMessageDetail(message)
    }
}
