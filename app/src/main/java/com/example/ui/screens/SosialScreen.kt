package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.filled.WaterDrop
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.IncidentRecordEntity
import com.example.data.model.SocialHelpEntity
import com.example.R
import com.example.ui.components.AppHeader
import com.example.ui.components.FilterChipTab
import com.example.ui.components.StatusBadge
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
fun SosialScreen(
    uiState: RtrwUiState,
    viewModel: RtrwViewModel,
    modifier: Modifier = Modifier
) {
    val subTabs = listOf("Bantuan Sosial", "Kejadian Lingkungan", "Apresiasi Warga")

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = BackgroundLight,
        floatingActionButton = {
            if (uiState.sosialFilter == "Kejadian Lingkungan") {
                FloatingActionButton(
                    onClick = { viewModel.openCreateIncidentSheet() },
                    containerColor = AccentOrange,
                    contentColor = Color.White,
                    modifier = Modifier.testTag("fab_report_incident")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Lapor")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Lapor Kejadian", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Standard AppHeader (Clean, unified & with notification + admin toggle)
            AppHeader(
                title = "Sosial & Lingkungan",
                unreadCount = uiState.unreadNotifications,
                onNotificationClick = { viewModel.openNotificationsSheet() },
                isAdminMode = uiState.isAdminMode,
                onAdminToggle = { viewModel.toggleAdminMode() }
            )

            // Sub Navigation Tabs
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(subTabs) { tab ->
                    FilterChipTab(
                        label = tab,
                        isSelected = uiState.sosialFilter == tab,
                        onClick = { viewModel.setSosialFilter(tab) }
                    )
                }
            }

            HorizontalDivider(color = BorderLight, thickness = 1.dp)

            // Tab Content
            when (uiState.sosialFilter) {
                "Bantuan Sosial" -> BantuanSosialTabContent(uiState, viewModel)
                "Kejadian Lingkungan" -> KejadianLingkunganTabContent(uiState, viewModel)
                "Apresiasi Warga" -> ApresiasiWargaTabContent(uiState, viewModel)
            }
        }
    }

    // Contribute Sheet / Dialog
    if (uiState.showCreateSocialHelpSheet && uiState.selectedSocialHelpForContribute != null) {
        ContributeSocialHelpDialog(
            help = uiState.selectedSocialHelpForContribute!!,
            onDismiss = { viewModel.closeSocialHelpContribute() },
            onConfirm = { note ->
                viewModel.contributeToSocialHelp(uiState.selectedSocialHelpForContribute!!.id, note)
            }
        )
    }

    // Incident Detail Bottom Sheet
    uiState.selectedIncidentForDetail?.let { incident ->
        IncidentDetailBottomSheet(
            incident = incident,
            isAdmin = uiState.isAdminMode,
            onDismiss = { viewModel.closeIncidentDetail() },
            onUpdateStatus = { status, note ->
                viewModel.adminUpdateIncidentStatus(incident.id, status, note)
            }
        )
    }

    // Create Incident Modal Sheet
    if (uiState.showCreateIncidentSheet) {
        CreateIncidentBottomSheet(
            onDismiss = { viewModel.closeCreateIncidentSheet() },
            onSubmit = { judul, lokasi, kategori, deskripsi, fotoType ->
                viewModel.submitIncidentReport(judul, lokasi, kategori, deskripsi, fotoType)
            }
        )
    }
}

@Composable
fun BantuanSosialTabContent(
    uiState: RtrwUiState,
    viewModel: RtrwViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 14.dp)
    ) {
        item {
            // Empathy & Care Banner
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryBlueLight),
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
                            .background(PrimaryBlue.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = PrimaryBlueDark,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Peduli Sesama Tetangga",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlueDark
                        )
                        Text(
                            text = "Bantuan tidak harus berupa materi. Tenaga, kendaraan, waktu, atau keahlian Anda sangat berarti.",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        items(uiState.socialHelp, key = { it.id }) { help ->
            SocialHelpCard(
                help = help,
                onContributeClick = { viewModel.openSocialHelpContribute(help) }
            )
        }
    }
}

@Composable
fun SocialHelpCard(
    help: SocialHelpEntity,
    onContributeClick: () -> Unit
) {
    val isDone = help.status == "Terbantu" || help.isMyContributed

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("social_help_card_${help.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Top Row: Location & Timestamp
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = AccentRed, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${help.lokasi} • ${help.tanggal}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary
                    )
                }

                Text(
                    text = help.waktuPosting,
                    fontSize = 11.sp,
                    color = TextTertiary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text = help.judul,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Tags
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                help.kebutuhanTags.split(",").forEach { tag ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(BackgroundLight)
                            .border(0.8.dp, BorderLight, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = tag.trim(),
                            fontSize = 11.sp,
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Description
            Text(
                text = help.deskripsi,
                fontSize = 13.sp,
                color = TextSecondary,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Action Button
            if (isDone) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = AccentGreenDark, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("${help.jumlahRelawan} warga membantu", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AccentGreenDark)
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(AccentGreenLight)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Terbantu", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AccentGreenDark)
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${help.jumlahRelawan} relawan siap",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )

                    Button(
                        onClick = onContributeClick,
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentGreen,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.height(38.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Handshake, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("SAYA BISA MEMBANTU", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun KejadianLingkunganTabContent(
    uiState: RtrwUiState,
    viewModel: RtrwViewModel
) {
    val filterStatusOptions = listOf("Semua", "Dilaporkan", "Dalam Perbaikan", "Diproses", "Selesai")

    val filteredIncidents = uiState.incidents.filter { incident ->
        if (uiState.kejadianStatusFilter == "Semua") true
        else incident.status.equals(uiState.kejadianStatusFilter, ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 14.dp)
    ) {
        // Filter row
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filterStatusOptions) { status ->
                    FilterChipTab(
                        label = status,
                        isSelected = uiState.kejadianStatusFilter == status,
                        onClick = { viewModel.setKejadianStatusFilter(status) }
                    )
                }
            }
        }

        items(filteredIncidents, key = { it.id }) { incident ->
            IncidentTimelineCard(
                incident = incident,
                onClick = { viewModel.openIncidentDetail(incident) }
            )
        }
    }
}

@Composable
fun IncidentTimelineCard(
    incident: IncidentRecordEntity,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("incident_card_${incident.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with Icon, Title, and Status
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
                            .background(
                                when (incident.kategori) {
                                    "Fasilitas Umum" -> AccentOrangeLight
                                    "Kebersihan" -> AccentGreenLight
                                    else -> PrimaryBlueLight
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (incident.kategori) {
                                "Fasilitas Umum" -> Icons.Default.Lightbulb
                                "Kebersihan" -> Icons.Default.ReportProblem
                                else -> Icons.Default.WaterDrop
                            },
                            contentDescription = null,
                            tint = when (incident.kategori) {
                                "Fasilitas Umum" -> AccentOrangeDark
                                "Kebersihan" -> AccentGreenDark
                                else -> PrimaryBlueDark
                            },
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = incident.judul,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "${incident.lokasi} • ${incident.kategori}",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }

                StatusBadge(status = incident.status)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Incident Process Step Timeline
            IncidentProgressSteps(incident = incident)

            Spacer(modifier = Modifier.height(12.dp))

            // Catatan Pengurus / Info
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(BackgroundLight)
                    .padding(10.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(imageVector = Icons.Default.Engineering, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = incident.catatanPengurus,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun IncidentProgressSteps(incident: IncidentRecordEntity) {
    val steps = listOf("Lapor", "Diverifikasi", "Perbaikan", "Selesai")
    val currentStep = when (incident.status) {
        "Dilaporkan" -> 1
        "Diproses" -> 2
        "Dalam Perbaikan" -> 3
        "Selesai" -> 4
        else -> 1
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, stepTitle ->
            val stepNumber = index + 1
            val isDone = stepNumber <= currentStep
            val isCurrent = stepNumber == currentStep

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(
                            if (isDone) {
                                if (stepNumber == 4) AccentGreen else PrimaryBlue
                            } else BorderLight
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isDone) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                    } else {
                        Text(text = "$stepNumber", fontSize = 11.sp, color = TextTertiary, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stepTitle,
                    fontSize = 10.sp,
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                    color = if (isDone) TextPrimary else TextTertiary
                )
            }
        }
    }
}

@Composable
fun ApresiasiWargaTabContent(
    uiState: RtrwUiState,
    viewModel: RtrwViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 16.dp)
    ) {
        // Hero Gratitude Banner
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = AccentGreenLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(AccentGreen),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolunteerActivism,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Terima Kasih Warga RW 02",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentGreenDark
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "\"Saya ikut karena saya bagian dari lingkungan ini.\"",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Lingkungan yang aman, bersih, dan nyaman terwujud berkat gotong royong dan kepedulian seluruh warga.",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        }

        // Monthly Impact Summary
        item {
            Text("Capaian Kebersamaan Bulan Ini (Mei 2026)", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ImpactMetricCard(
                    title = "Warga Ikut Acara",
                    value = "73 Warga",
                    desc = "Kerja bakti & posyandu",
                    icon = Icons.Default.People,
                    iconColor = PrimaryBlue,
                    bgColor = PrimaryBlueLight,
                    modifier = Modifier.weight(1f)
                )
                ImpactMetricCard(
                    title = "Relawan Aktif",
                    value = "24 Relawan",
                    desc = "Membantu tetangga",
                    icon = Icons.Default.Handshake,
                    iconColor = AccentGreen,
                    bgColor = AccentGreenLight,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ImpactMetricCard(
                    title = "Bantuan Tersalur",
                    value = "12 Kasus",
                    desc = "Sembako & transportasi",
                    icon = Icons.Default.Favorite,
                    iconColor = AccentRed,
                    bgColor = AccentRedLight,
                    modifier = Modifier.weight(1f)
                )
                ImpactMetricCard(
                    title = "Kejadian Selesai",
                    value = "18 Titik",
                    desc = "Fasilitas & kebersihan",
                    icon = Icons.Default.Verified,
                    iconColor = AccentPurple,
                    bgColor = AccentPurpleLight,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Wall of Community Photos / Highlights
        item {
            Text("Dokumentasi Gotong Royong Terkini", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.community_cleanup_hero_1787479598332),
                            contentDescription = "Dokumentasi Kerja Bakti",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Kerja Bakti Bersama Blok A - C Berjalan Lancar!",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Saluran air sepanjang 400 meter berhasil dibersihkan bersama 73 warga dan dinas kebersihan.",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ImpactMetricCard(
    title: String,
    value: String,
    desc: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TextSecondary)
            Text(text = desc, fontSize = 10.sp, color = TextTertiary)
        }
    }
}

@Composable
fun ContributeSocialHelpDialog(
    help: SocialHelpEntity,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var selectedOption by remember { mutableStateOf("Tenaga / Relawan") }
    var noteText by remember { mutableStateOf("") }
    val options = listOf("Tenaga / Relawan", "Kendaraan / Antar", "Barang / Sembako", "Keahlian", "Donasi")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Bantu Sesama Warga", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Bantuan untuk: ${help.judul}",
                    fontSize = 13.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Pilih Bentuk Bantuan Anda:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(6.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(options) { opt ->
                        FilterChipTab(
                            label = opt,
                            isSelected = selectedOption == opt,
                            onClick = { selectedOption = opt }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    label = { Text("Catatan / Ketersediaan Waktu") },
                    placeholder = { Text("Contoh: Siap besok pagi jam 08:00 dengan mobil Avanza") },
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentGreen,
                        unfocusedBorderColor = BorderLight
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm("$selectedOption: $noteText") },
                colors = ButtonDefaults.buttonColors(containerColor = AccentGreen)
            ) {
                Text("Kirim Bantuan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncidentDetailBottomSheet(
    incident: IncidentRecordEntity,
    isAdmin: Boolean,
    onDismiss: () -> Unit,
    onUpdateStatus: (String, String) -> Unit
) {
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
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
                Text("Detail Kejadian Lingkungan", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                IconButton(onClick = { /* share */ }) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "Share", tint = PrimaryBlue)
                }
            }

            // Photo Thumbnail
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(BackgroundLight)
            ) {
                Image(
                    painter = painterResource(
                        id = if (incident.fotoType == "lampu") R.drawable.streetlight_incident_1787479643676
                        else R.drawable.tree_incident_thumb_1787479622536
                    ),
                    contentDescription = incident.judul,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(incident.judul, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary, modifier = Modifier.weight(1f))
                    StatusBadge(status = incident.status)
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = AccentRed, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${incident.lokasi} • Kategori: ${incident.kategori}", fontSize = 13.sp, color = TextSecondary)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Timeline Process
                Text("Proses Penanganan", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                IncidentProgressSteps(incident = incident)

                Spacer(modifier = Modifier.height(16.dp))

                // Deskripsi Warga
                Text("Deskripsi Laporan", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(4.dp))
                Text(incident.deskripsi, fontSize = 13.sp, color = TextSecondary, lineHeight = 18.sp)

                Spacer(modifier = Modifier.height(16.dp))

                // Catatan Pengurus
                Text("Tindak Lanjut Pengurus RT/RW", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(4.dp))
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = BackgroundLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = incident.catatanPengurus,
                        fontSize = 13.sp,
                        color = TextSecondary,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }

                // Admin Controls if in admin mode
                if (isAdmin) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text("Aksi Pengurus (Mode Admin)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PrimaryBlueDark)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onUpdateStatus("Dalam Perbaikan", "Sedang dikerjakan teknisi RT.") },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentOrange),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Perbaikan", fontSize = 12.sp)
                        }

                        Button(
                            onClick = { onUpdateStatus("Selesai", "Perbaikan telah selesai dan berfungsi normal.") },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Selesai", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateIncidentBottomSheet(
    onDismiss: () -> Unit,
    onSubmit: (String, String, String, String, String) -> Unit
) {
    var judul by remember { mutableStateOf("") }
    var lokasi by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("Fasilitas Umum") }
    var deskripsi by remember { mutableStateOf("") }
    val kategoriOptions = listOf("Fasilitas Umum", "Kebersihan", "Keamanan", "Infrastruktur")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Lapor Kejadian Lingkungan", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Kategori Kejadian", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(kategoriOptions) { cat ->
                    FilterChipTab(
                        label = cat,
                        isSelected = kategori == cat,
                        onClick = { kategori = cat }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = judul,
                onValueChange = { judul = it },
                label = { Text("Judul Kejadian") },
                placeholder = { Text("Contoh: Lampu jalan mati / Pohon tumbang") },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentOrange,
                    unfocusedBorderColor = BorderLight
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = lokasi,
                onValueChange = { lokasi = it },
                label = { Text("Lokasi Kejadian") },
                placeholder = { Text("Contoh: Depan Blok C No. 12") },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentOrange,
                    unfocusedBorderColor = BorderLight
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = deskripsi,
                onValueChange = { deskripsi = it },
                label = { Text("Deskripsi / Keterangan Tambahan") },
                placeholder = { Text("Jelaskan detail kejadian agar pengurus dapat menindaklanjuti dengan cepat.") },
                minLines = 3,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentOrange,
                    unfocusedBorderColor = BorderLight
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (judul.isNotBlank() && lokasi.isNotBlank()) {
                        onSubmit(judul, lokasi, kategori, deskripsi, "lampu")
                    }
                },
                enabled = judul.isNotBlank() && lokasi.isNotBlank(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentOrange),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Kirim Laporan Kejadian", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
    }
}
