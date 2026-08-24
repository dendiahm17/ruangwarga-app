package com.example.ui.screens

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.CommunityEventEntity
import com.example.ui.components.AppHeader
import com.example.ui.components.FilterChipTab
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentGreenDark
import com.example.ui.theme.AccentGreenLight
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentOrangeDark
import com.example.ui.theme.AccentOrangeLight
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentPurpleLight
import com.example.ui.theme.AccentRed
import com.example.ui.theme.AccentRedLight
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryBlueDark
import com.example.ui.theme.PrimaryBlueLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.ui.viewmodel.RtrwUiState
import com.example.ui.viewmodel.RtrwViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KegiatanScreen(
    uiState: RtrwUiState,
    viewModel: RtrwViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val filterTimeTabs = listOf("Semua", "Akan Datang", "Berlangsung", "Selesai")
    val filterCategoryChips = listOf("Semua Kategori", "Kerja Bakti", "Posyandu", "Keagamaan", "Senam Sehat")

    var showContributeDialogForEvent by remember { mutableStateOf<CommunityEventEntity?>(null) }

    // Logic filtering
    val filteredEvents = uiState.communityEvents.filter { event ->
        // Status filter
        val matchesTime = when (uiState.kegiatanFilter) {
            "Semua" -> true
            "Akan Datang" -> event.tanggal.contains("Mei") || event.tanggal.contains("Juni")
            "Berlangsung" -> event.tanggal.contains("26 Mei")
            "Selesai" -> event.tanggal.contains("April") || event.tanggal.contains("10 Mei")
            else -> true
        }

        // Category filter
        val matchesCategory = when (uiState.kegiatanCategoryFilter) {
            "Semua Kategori" -> true
            else -> event.kategori.contains(uiState.kegiatanCategoryFilter, ignoreCase = true)
        }

        // Search query
        val matchesSearch = uiState.kegiatanSearchQuery.isEmpty() ||
                event.judul.contains(uiState.kegiatanSearchQuery, ignoreCase = true) ||
                event.lokasi.contains(uiState.kegiatanSearchQuery, ignoreCase = true) ||
                event.kategori.contains(uiState.kegiatanSearchQuery, ignoreCase = true) ||
                event.deskripsi.contains(uiState.kegiatanSearchQuery, ignoreCase = true)

        matchesTime && matchesCategory && matchesSearch
    }

    // Featured event (closest upcoming)
    val featuredEvent = uiState.communityEvents.firstOrNull { it.tanggal.contains("26 Mei") }
        ?: uiState.communityEvents.firstOrNull()

    val totalRegisteredParticipants = uiState.communityEvents.sumOf { it.jumlahHadir }
    val myJoinedEventsCount = uiState.communityEvents.count { it.partisipasiStatus == "Saya Ikut" || it.partisipasiStatus == "Saya Bisa Membantu" }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = BackgroundLight,
        floatingActionButton = {
            if (uiState.isAdminMode) {
                FloatingActionButton(
                    onClick = { viewModel.openAddCommunityEventSheet() },
                    containerColor = AccentGreen,
                    contentColor = Color.White,
                    modifier = Modifier.testTag("fab_add_kegiatan")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah Kegiatan")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Standard AppHeader (Clean, unified & perfectly responsive)
            AppHeader(
                title = "Kegiatan Warga",
                unreadCount = uiState.unreadNotifications,
                onNotificationClick = { viewModel.openNotificationsSheet() },
                isAdminMode = uiState.isAdminMode,
                onAdminToggle = { viewModel.toggleAdminMode() }
            )

            // Search Bar & Filter Controls
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 8.dp)
            ) {
                // Search Field
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                    OutlinedTextField(
                        value = uiState.kegiatanSearchQuery,
                        onValueChange = { viewModel.setKegiatanSearchQuery(it) },
                        placeholder = { Text("Cari agenda, kerja bakti, pengajian, senam...", fontSize = 13.sp, color = TextTertiary) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = TextSecondary, modifier = Modifier.size(18.dp))
                        },
                        trailingIcon = {
                            if (uiState.kegiatanSearchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.setKegiatanSearchQuery("") }) {
                                    Icon(imageVector = Icons.Default.Close, contentDescription = "Clear", tint = TextSecondary, modifier = Modifier.size(18.dp))
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AccentGreen,
                            unfocusedBorderColor = BorderLight,
                            focusedContainerColor = BackgroundLight,
                            unfocusedContainerColor = BackgroundLight
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("kegiatan_search_input")
                    )
                }

                // Time Filter Tabs (Semua, Akan Datang, Berlangsung, Selesai)
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filterTimeTabs) { tab ->
                        FilterChipTab(
                            label = tab,
                            isSelected = uiState.kegiatanFilter == tab,
                            onClick = { viewModel.setKegiatanFilter(tab) }
                        )
                    }
                }

                // Category Filter Chips
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(filterCategoryChips) { category ->
                        val isSelected = uiState.kegiatanCategoryFilter == category
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) PrimaryBlue else BackgroundLight)
                                .border(1.dp, if (isSelected) PrimaryBlueDark else BorderLight, RoundedCornerShape(16.dp))
                                .clickable { viewModel.setKegiatanCategoryFilter(category) }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = category,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else TextSecondary
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = BorderLight, thickness = 1.dp)

            // Content List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 14.dp)
            ) {
                // Spotlight / Featured Event (Shown if search is empty and tab is Semua / Akan Datang)
                if (uiState.kegiatanSearchQuery.isEmpty() && (uiState.kegiatanFilter == "Semua" || uiState.kegiatanFilter == "Akan Datang") && featuredEvent != null) {
                    item {
                        FeaturedSpotlightCard(
                            event = featuredEvent,
                            onCardClick = { viewModel.openEventDetail(featuredEvent) },
                            onRsvpClick = {
                                val newStatus = if (featuredEvent.partisipasiStatus == "Saya Ikut") "Belum Konfirmasi" else "Saya Ikut"
                                viewModel.participateInEvent(featuredEvent.id, newStatus)
                            },
                            onContributeClick = {
                                showContributeDialogForEvent = featuredEvent
                            },
                            onShareClick = {
                                shareEventText(context, featuredEvent)
                            }
                        )
                    }
                }

                // Header List Count
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Daftar Kegiatan (${filteredEvents.size})",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Text(
                            text = "$totalRegisteredParticipants Warga Berpartisipasi",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = PrimaryBlue
                        )
                    }
                }

                // Events List
                if (filteredEvents.isEmpty()) {
                    item {
                        EmptyStateKegiatan(
                            hasSearch = uiState.kegiatanSearchQuery.isNotEmpty(),
                            onReset = {
                                viewModel.setKegiatanSearchQuery("")
                                viewModel.setKegiatanFilter("Semua")
                                viewModel.setKegiatanCategoryFilter("Semua Kategori")
                            }
                        )
                    }
                } else {
                    items(filteredEvents, key = { it.id }) { event ->
                        ModernKegiatanCard(
                            event = event,
                            onCardClick = { viewModel.openEventDetail(event) },
                            onRsvpClick = {
                                val newStatus = if (event.partisipasiStatus == "Saya Ikut") "Belum Konfirmasi" else "Saya Ikut"
                                viewModel.participateInEvent(event.id, newStatus)
                            },
                            onContributeClick = {
                                showContributeDialogForEvent = event
                            },
                            onShareClick = {
                                shareEventText(context, event)
                            }
                        )
                    }
                }

                // Bottom Community Motivation Card
                item {
                    GotongRoyongMotivationBanner()
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }

    // Detail Kegiatan Sheet
    uiState.selectedEventForDetail?.let { event ->
        DetailKegiatanBottomSheet(
            event = event,
            onDismiss = { viewModel.closeEventDetail() },
            onRsvpToggle = {
                val newStatus = if (event.partisipasiStatus == "Saya Ikut") "Belum Konfirmasi" else "Saya Ikut"
                viewModel.participateInEvent(event.id, newStatus)
            },
            onContributeClick = {
                showContributeDialogForEvent = event
            },
            onShareClick = {
                shareEventText(context, event)
            }
        )
    }

    // Contribute Equipment / Volunteer Dialog
    showContributeDialogForEvent?.let { event ->
        ContributeLogisticsDialog(
            event = event,
            onDismiss = { showContributeDialogForEvent = null },
            onSubmitContribution = { itemType, amount, note ->
                viewModel.contributeEventLogistic(event.id, itemType, amount, note)
                showContributeDialogForEvent = null
            }
        )
    }
}

// ==========================================
// FEATURED SPOTLIGHT CARD (MODERN HERO)
// ==========================================
@Composable
fun FeaturedSpotlightCard(
    event: CommunityEventEntity,
    onCardClick: () -> Unit,
    onRsvpClick: () -> Unit,
    onContributeClick: () -> Unit,
    onShareClick: () -> Unit
) {
    val isParticipating = event.partisipasiStatus == "Saya Ikut"
    val isContributed = event.partisipasiStatus == "Saya Bisa Membantu"

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() }
            .testTag("featured_event_card")
    ) {
        Column {
            // Header Top Gradient Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(AccentGreenDark, AccentGreen, PrimaryBlue)
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                        Text(
                            text = "AGENDA UTAMA MINGGU INI",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.25f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = event.kategori,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // Card Body
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = event.judul,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(15.dp))
                            Text(text = "${event.tanggal} • ${event.waktu}", fontSize = 12.sp, color = TextSecondary, fontWeight = FontWeight.Medium)
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = AccentRed, modifier = Modifier.size(15.dp))
                            Text(text = event.lokasi, fontSize = 12.sp, color = TextSecondary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    }

                    IconButton(
                        onClick = onShareClick,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(BackgroundLight)
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Bagikan", tint = PrimaryBlue, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Logistics & Volunteers Progress Preview
                if (event.kebutuhanRelawan > 0) {
                    val relawanProgress = (event.terpenuhiRelawan.toFloat() / event.kebutuhanRelawan.toFloat()).coerceIn(0f, 1f)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(BackgroundLight)
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(imageVector = Icons.Default.People, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(14.dp))
                                    Text("Kebutuhan Relawan", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                                }
                                Text(
                                    text = "${event.terpenuhiRelawan}/${event.kebutuhanRelawan} orang",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (event.terpenuhiRelawan >= event.kebutuhanRelawan) AccentGreenDark else AccentOrangeDark
                                )
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                            LinearProgressIndicator(
                                progress = { relawanProgress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(5.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = if (event.terpenuhiRelawan >= event.kebutuhanRelawan) AccentGreen else AccentOrange,
                                trackColor = BorderLight
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Participant Info Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Row(horizontalArrangement = Arrangement.spacedBy((-6).dp)) {
                            listOf(PrimaryBlue, AccentGreen, AccentOrange, AccentPurple).forEach { color ->
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .border(1.5.dp, Color.White, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(13.dp))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${event.jumlahHadir} warga sudah ikut",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    if (isParticipating) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(AccentGreenLight)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = AccentGreenDark, modifier = Modifier.size(13.dp))
                            Text("Terdaftar", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentGreenDark)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Action Buttons Row (Bantu & Saya Ikut) - Separated on its own line for spacious layout
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onContributeClick,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = PrimaryBlue
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Handshake, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Bantu Logistik", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onRsvpClick,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isParticipating) AccentGreenLight else AccentGreen,
                            contentColor = if (isParticipating) AccentGreenDark else Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .testTag("rsvp_btn_${event.id}")
                    ) {
                        if (isParticipating) {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Sudah Ikut", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        } else {
                            Text("SAYA IKUT", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// MODERN KEGIATAN CARD (LIST ITEM)
// ==========================================
@Composable
fun ModernKegiatanCard(
    event: CommunityEventEntity,
    onCardClick: () -> Unit,
    onRsvpClick: () -> Unit,
    onContributeClick: () -> Unit,
    onShareClick: () -> Unit
) {
    val isParticipating = event.partisipasiStatus == "Saya Ikut"
    val isHelping = event.partisipasiStatus == "Saya Bisa Membantu"

    // Dynamic category color & icon
    val (categoryBg, categoryText, categoryIcon) = when {
        event.kategori.contains("Kerja Bakti", ignoreCase = true) -> Triple(AccentGreenLight, AccentGreenDark, Icons.Default.CleaningServices)
        event.kategori.contains("Posyandu", ignoreCase = true) -> Triple(AccentOrangeLight, AccentOrangeDark, Icons.Default.VolunteerActivism)
        event.kategori.contains("Keagamaan", ignoreCase = true) -> Triple(AccentPurpleLight, AccentPurple, Icons.Default.Mosque)
        event.kategori.contains("Senam", ignoreCase = true) -> Triple(PrimaryBlueLight, PrimaryBlueDark, Icons.Default.SelfImprovement)
        else -> Triple(BackgroundLight, TextSecondary, Icons.Default.Event)
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() }
            .testTag("kegiatan_card_${event.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row: Category Badge, Status RSVP & Share
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category Chip
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(categoryBg)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(imageVector = categoryIcon, contentDescription = null, tint = categoryText, modifier = Modifier.size(13.dp))
                    Text(
                        text = event.kategori,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = categoryText
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (isParticipating) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(AccentGreenLight)
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = AccentGreenDark, modifier = Modifier.size(12.dp))
                            Text(text = "Saya Ikut", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AccentGreenDark)
                        }
                    } else if (isHelping) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(PrimaryBlueLight)
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Handshake, contentDescription = null, tint = PrimaryBlueDark, modifier = Modifier.size(12.dp))
                            Text(text = "Membantu", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = PrimaryBlueDark)
                        }
                    }

                    IconButton(
                        onClick = onShareClick,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Share", tint = TextTertiary, modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Event Title
            Text(
                text = event.judul,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Date & Time
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "${event.tanggal} • ${event.waktu}",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Location
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = AccentRed,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = event.lokasi,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Participants Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Avatar Stack + Count
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Row(horizontalArrangement = Arrangement.spacedBy((-5).dp)) {
                        listOf(PrimaryBlue, AccentGreen, AccentOrange).forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(1.dp, Color.White, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "${event.jumlahHadir} warga sudah ikut",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary
                    )
                }

                if (isParticipating) {
                    Text(
                        text = "Terdaftar",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentGreenDark,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(AccentGreenLight)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons Row (Bantu & Saya Ikut) - Separated on the next line to avoid text cutting
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onContributeClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryBlue),
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                ) {
                    Icon(imageVector = Icons.Default.Handshake, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Bantu", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onRsvpClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isParticipating) AccentGreenLight else AccentGreen,
                        contentColor = if (isParticipating) AccentGreenDark else Color.White
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .testTag("rsvp_btn_${event.id}")
                ) {
                    if (isParticipating) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Batal Ikut", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    } else {
                        Text("Saya Ikut", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ==========================================
// DETAIL KEGIATAN BOTTOM SHEET
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailKegiatanBottomSheet(
    event: CommunityEventEntity,
    onDismiss: () -> Unit,
    onRsvpToggle: () -> Unit,
    onContributeClick: () -> Unit,
    onShareClick: () -> Unit
) {
    val isParticipating = event.partisipasiStatus == "Saya Ikut"
    val isHelping = event.partisipasiStatus == "Saya Bisa Membantu"

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
        ) {
            // Top Bar with Close & Share
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup")
                }
                Text(
                    text = "Detail Kegiatan Warga",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                IconButton(onClick = onShareClick) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "Bagikan", tint = PrimaryBlue)
                }
            }

            // Hero Banner Illustration
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(AccentGreenLight)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.community_cleanup_hero_1787479598332),
                    contentDescription = "Kegiatan Bersama",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Title & Status Badge
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = event.judul,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(AccentGreenLight)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = event.kategori,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentGreenDark
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // PIC Info
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(BackgroundLight)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Penanggung Jawab: ${event.penanggungJawab}",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Date, Time, Location Info Cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = BackgroundLight),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("Tanggal", fontSize = 10.sp, color = TextTertiary)
                                Text(event.tanggal, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            }
                        }
                    }

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = BackgroundLight),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.Schedule, contentDescription = null, tint = AccentOrange, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("Waktu", fontSize = 10.sp, color = TextTertiary)
                                Text(event.waktu, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = BackgroundLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = AccentRed, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Titik Kumpul / Lokasi", fontSize = 10.sp, color = TextTertiary)
                            Text(event.lokasi, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Deskripsi
                Text("Deskripsi Kegiatan", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = event.deskripsi,
                    fontSize = 13.sp,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // KEBUTUHAN PERALATAN & LOGISTIK
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Kebutuhan Relawan & Logistik", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(
                        text = "+ Bantu Sekarang",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue,
                        modifier = Modifier.clickable { onContributeClick() }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    NeedItemRow(
                        title = "Relawan Warga",
                        fulfilled = event.terpenuhiRelawan,
                        target = event.kebutuhanRelawan,
                        unit = "orang",
                        icon = Icons.Default.People
                    )
                    if (event.kebutuhanSapu > 0) {
                        NeedItemRow(
                            title = "Sapu Lidi / Penggaruk",
                            fulfilled = event.terpenuhiSapu,
                            target = event.kebutuhanSapu,
                            unit = "unit",
                            icon = Icons.Default.CleaningServices
                        )
                    }
                    if (event.kebutuhanEmber > 0) {
                        NeedItemRow(
                            title = "Ember / Karung Sampah",
                            fulfilled = event.terpenuhiEmber,
                            target = event.kebutuhanEmber,
                            unit = "unit",
                            icon = Icons.Default.Handshake
                        )
                    }
                    if (event.kebutuhanPickup > 0) {
                        NeedItemRow(
                            title = "Kendaraan Pickup / Gerobak",
                            fulfilled = event.terpenuhiPickup,
                            target = event.kebutuhanPickup,
                            unit = "unit",
                            icon = Icons.Default.LocalShipping
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Participant count & avatars
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "${event.jumlahHadir} warga sudah terdaftar",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Target partisipasi: ${event.targetPeserta} warga RT 01 - RT 04",
                            fontSize = 11.sp,
                            color = TextTertiary
                        )
                    }

                    // Participant avatar stack
                    Row(horizontalArrangement = Arrangement.spacedBy((-6).dp)) {
                        listOf(PrimaryBlue, AccentGreen, AccentOrange, AccentPurple).forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(1.5.dp, Color.White, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons
                Button(
                    onClick = onRsvpToggle,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isParticipating) AccentGreenLight else AccentGreen,
                        contentColor = if (isParticipating) AccentGreenDark else Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    if (isParticipating) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("SUDAH TERDAFTAR IKUT (BATALKAN)", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    } else {
                        Text("SAYA IKUT HADIR KEGIATAN INI", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onContributeClick,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryBlue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                ) {
                    Icon(imageVector = Icons.Default.Handshake, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("SAYA BISA MEMBANTU (ALAT / LOGISTIK)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ==========================================
// CONTRIBUTE LOGISTICS & EQUIPMENT DIALOG
// ==========================================
@Composable
fun ContributeLogisticsDialog(
    event: CommunityEventEntity,
    onDismiss: () -> Unit,
    onSubmitContribution: (itemType: String, amount: Int, note: String) -> Unit
) {
    val items = listOf("Relawan", "Sapu", "Ember", "Pickup", "Konsumsi / Snack")
    var selectedItem by remember { mutableStateOf("Relawan") }
    var quantityText by remember { mutableStateOf("1") }
    var noteText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Bantu Kegiatan Lingkungan",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Pilih bentuk bantuan yang ingin Anda sumbangkan untuk kegiatan '${event.judul}':",
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Jenis Bantuan:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(6.dp))

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(items) { item ->
                        val isSelected = selectedItem == item
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) AccentGreen else BackgroundLight)
                                .clickable { selectedItem = item }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = item,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else TextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = quantityText,
                    onValueChange = { quantityText = it },
                    label = { Text("Jumlah (Orang / Unit)") },
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    label = { Text("Catatan Bantuan") },
                    placeholder = { Text("Contoh: Siap membawa 2 sapu lidi dari rumah") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = quantityText.toIntOrNull() ?: 1
                    onSubmitContribution(selectedItem, amount, noteText.ifBlank { "Bantuan $selectedItem" })
                },
                colors = ButtonDefaults.buttonColors(containerColor = AccentGreen)
            ) {
                Text("Konfirmasi Bantuan", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

// ==========================================
// NEED ITEM ROW COMPONENT
// ==========================================
@Composable
fun NeedItemRow(
    title: String,
    fulfilled: Int,
    target: Int,
    unit: String,
    icon: ImageVector
) {
    val progress = if (target > 0) (fulfilled.toFloat() / target.toFloat()).coerceIn(0f, 1f) else 1f

    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = BackgroundLight),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = icon, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(title, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                }
                Text(
                    text = "$fulfilled / $target $unit",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (fulfilled >= target) AccentGreenDark else TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (fulfilled >= target) AccentGreen else PrimaryBlue,
                trackColor = BorderLight
            )
        }
    }
}

// ==========================================
// EMPTY STATE COMPONENT
// ==========================================
@Composable
fun EmptyStateKegiatan(
    hasSearch: Boolean,
    onReset: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(PrimaryBlueLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Event,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (hasSearch) "Kegiatan Tidak Ditemukan" else "Belum Ada Agenda Kegiatan",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (hasSearch) "Coba gunakan kata kunci lain atau ubah filter status waktu." else "Pengurus RT belum menambahkan agenda untuk kategori ini.",
                fontSize = 12.sp,
                color = TextSecondary,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onReset,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Text("Reset Semua Filter", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ==========================================
// GOTONG ROYONG MOTIVATION BANNER
// ==========================================
@Composable
fun GotongRoyongMotivationBanner() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AccentGreenLight),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(AccentGreen.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.VolunteerActivism,
                    contentDescription = null,
                    tint = AccentGreenDark,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Gotong Royong & Partisipasi Warga",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentGreenDark
                )
                Text(
                    text = "Setiap kehadiran dan bantuan peralatan Anda mempererat kebersamaan dan merawat lingkungan kita.",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 15.sp
                )
            }
        }
    }
}

// ==========================================
// SHARE HELPER FUNCTION
// ==========================================
private fun shareEventText(context: android.content.Context, event: CommunityEventEntity) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(
            Intent.EXTRA_TEXT,
            """
            📣 *AGENDA KEGIATAN WARGA RT 03 / RW 02* 📣
            
            📌 *${event.judul}*
            🏷️ Kategori: ${event.kategori}
            📅 Tanggal: ${event.tanggal}
            ⏰ Waktu: ${event.waktu}
            📍 Lokasi: ${event.lokasi}
            
            📝 *Deskripsi:*
            ${event.deskripsi}
            
            👤 PIC / Penanggung Jawab: ${event.penanggungJawab}
            
            Mari bersama-sama hadir dan berpartisipasi untuk lingkungan kita yang lebih baik dan asri! 🤝🌿
            """.trimIndent()
        )
    }
    context.startActivity(Intent.createChooser(shareIntent, "Bagikan Agenda Kegiatan"))
}
