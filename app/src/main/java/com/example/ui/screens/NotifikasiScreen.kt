package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.data.model.NotificationEntity
import com.example.R
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
import com.example.ui.viewmodel.MainTab
import com.example.ui.viewmodel.RtrwUiState
import com.example.ui.viewmodel.RtrwViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotifikasiScreen(
    uiState: RtrwUiState,
    viewModel: RtrwViewModel,
    modifier: Modifier = Modifier
) {
    val filterCategories = listOf("Semua", "Urgent", "Important", "Activity", "Informational")

    val filteredNotifications = uiState.notifications.filter { item ->
        if (uiState.notifikasiFilter == "Semua") true
        else item.kategori.equals(uiState.notifikasiFilter, ignoreCase = true)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = BackgroundLight
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Top Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Notifikasi & Pengingat",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Pemberitahuan kontekstual wilayah RW 02",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }

                    IconButton(
                        onClick = { viewModel.openNotificationSettingsSheet() },
                        modifier = Modifier.testTag("btn_notification_settings")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Pengaturan Notifikasi",
                            tint = TextSecondary
                        )
                    }
                }
            }

            HorizontalDivider(color = BorderLight, thickness = 1.dp)

            // Category Filter Tabs
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filterCategories) { category ->
                    FilterChipTab(
                        label = when (category) {
                            "Urgent" -> "🔴 Urgent"
                            "Important" -> "🟠 Important"
                            "Activity" -> "🔵 Activity"
                            "Informational" -> "⚪ Info"
                            else -> "Semua"
                        },
                        isSelected = uiState.notifikasiFilter == category,
                        onClick = { viewModel.setNotifikasiFilter(category) }
                    )
                }
            }

            HorizontalDivider(color = BorderLight, thickness = 1.dp)

            // Notifications List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 14.dp)
            ) {
                items(filteredNotifications, key = { it.id }) { notif ->
                    NotificationContextCard(
                        notif = notif,
                        onClick = {
                            viewModel.markNotificationRead(notif.id)
                            handleNotificationAction(notif, viewModel)
                        },
                        onActionClick = {
                            viewModel.markNotificationRead(notif.id)
                            handleNotificationAction(notif, viewModel)
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

    // Notification Settings Sheet
    if (uiState.showNotificationSettingsSheet) {
        NotificationSettingsBottomSheet(
            onDismiss = { viewModel.closeNotificationSettingsSheet() }
        )
    }
}

private fun handleNotificationAction(notif: NotificationEntity, viewModel: RtrwViewModel) {
    when {
        notif.kategori == "Urgent" || notif.tipe == "darurat" -> {
            viewModel.selectTab(MainTab.SOSIAL)
            viewModel.setSosialFilter("Kejadian Lingkungan")
        }
        notif.kategori == "Important" && notif.pesan.contains("Rapat", ignoreCase = true) -> {
            viewModel.selectTab(MainTab.AKTIVITAS)
        }
        notif.kategori == "Activity" -> {
            viewModel.selectTab(MainTab.AKTIVITAS)
        }
        notif.tipe == "surat" -> {
            viewModel.openSuratScreenSheet()
        }
        notif.tipe == "iuran" -> {
            viewModel.openIuranScreenSheet()
        }
        else -> {
            // Generic info
        }
    }
}

@Composable
fun NotificationContextCard(
    notif: NotificationEntity,
    onClick: () -> Unit,
    onActionClick: () -> Unit
) {
    val isUrgent = notif.kategori.equals("Urgent", ignoreCase = true) || notif.tipe == "darurat"
    val isImportant = notif.kategori.equals("Important", ignoreCase = true)
    val isActivity = notif.kategori.equals("Activity", ignoreCase = true) || notif.tipe == "kegiatan"

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUrgent) AccentRedLight.copy(alpha = 0.6f) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isUrgent) 3.dp else 1.5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("notification_card_${notif.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header Row: Icon, Category Badge, Timestamp
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isUrgent -> AccentRedLight
                                    isImportant -> AccentOrangeLight
                                    isActivity -> AccentGreenLight
                                    else -> PrimaryBlueLight
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when {
                                isUrgent -> Icons.Default.Warning
                                isImportant -> Icons.Default.Campaign
                                isActivity -> Icons.Default.Event
                                notif.tipe == "surat" -> Icons.Default.Description
                                notif.tipe == "iuran" -> Icons.Default.Payment
                                else -> Icons.Default.Info
                            },
                            contentDescription = null,
                            tint = when {
                                isUrgent -> AccentRed
                                isImportant -> AccentOrangeDark
                                isActivity -> AccentGreenDark
                                else -> PrimaryBlueDark
                            },
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Priority Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                when {
                                    isUrgent -> AccentRed
                                    isImportant -> AccentOrange
                                    isActivity -> AccentGreen
                                    else -> BorderLight
                                }
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = notif.kategori.ifEmpty { "Info" }.uppercase(),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isUrgent || isImportant || isActivity) Color.White else TextSecondary
                        )
                    }
                }

                Text(
                    text = notif.waktuLalu.ifEmpty { notif.tanggal },
                    fontSize = 11.sp,
                    color = TextTertiary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title
            Text(
                text = notif.judul,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = if (isUrgent) AccentRed else TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Message
            Text(
                text = notif.pesan,
                fontSize = 13.sp,
                color = TextSecondary,
                lineHeight = 17.sp
            )

            // Optional Thumbnail for Tree incident
            if (isUrgent && notif.pesan.contains("Pohon", ignoreCase = true)) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.tree_incident_thumb_1787479622536),
                        contentDescription = "Pohon Tumbang",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Action Button Row
            if (notif.actionLabel != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = onActionClick,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (isUrgent) AccentRed else PrimaryBlue
                        ),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text(notif.actionLabel, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(12.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsBottomSheet(
    onDismiss: () -> Unit
) {
    var urgentEnabled by remember { mutableStateOf(true) }
    var importantEnabled by remember { mutableStateOf(true) }
    var activityEnabled by remember { mutableStateOf(true) }
    var infoEnabled by remember { mutableStateOf(true) }
    var quietHoursEnabled by remember { mutableStateOf(false) }

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
                Text("Pengaturan Notifikasi", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Text(
                text = "Sesuaikan preferensi pengingat agar Anda tetap terhubung tanpa terganggu.",
                fontSize = 12.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Urgent setting (locked on)
            NotificationToggleRow(
                title = "🔴 Notifikasi Urgent (Darurat)",
                desc = "Pohon tumbang, kebakaran, banjir, keamanan wilayah (Wajib aktif untuk keselamatan).",
                checked = urgentEnabled,
                onCheckedChange = { /* Always true */ },
                isLocked = true
            )

            HorizontalDivider(color = BorderLight, thickness = 0.8.dp)

            NotificationToggleRow(
                title = "🟠 Notifikasi Penting (Important)",
                desc = "Rapat pleno warga, musyawarah RT/RW, perubahan aturan wilayah.",
                checked = importantEnabled,
                onCheckedChange = { importantEnabled = it }
            )

            HorizontalDivider(color = BorderLight, thickness = 0.8.dp)

            NotificationToggleRow(
                title = "🔵 Notifikasi Kegiatan (Activity)",
                desc = "Pengingat kerja bakti, jadwal posyandu balita, senam sehat.",
                checked = activityEnabled,
                onCheckedChange = { activityEnabled = it }
            )

            HorizontalDivider(color = BorderLight, thickness = 0.8.dp)

            NotificationToggleRow(
                title = "⚪ Informasi & Dokumentasi",
                desc = "Foto kegiatan selesai, ringkasan kas bulanan, kabar warga.",
                checked = infoEnabled,
                onCheckedChange = { infoEnabled = it }
            )

            HorizontalDivider(color = BorderLight, thickness = 0.8.dp)

            NotificationToggleRow(
                title = "🌙 Mode Istirahat Malam (Quiet Hours)",
                desc = "Senyapkan notifikasi non-darurat mulai pukul 22:00 - 06:00 WIB.",
                checked = quietHoursEnabled,
                onCheckedChange = { quietHoursEnabled = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Simpan Pengaturan", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun NotificationToggleRow(
    title: String,
    desc: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    isLocked: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(2.dp))
            Text(desc, fontSize = 12.sp, color = TextSecondary, lineHeight = 16.sp)
        }

        Spacer(modifier = Modifier.width(12.dp))

        Switch(
            checked = checked,
            onCheckedChange = if (isLocked) null else onCheckedChange,
            enabled = !isLocked,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = PrimaryBlue
            )
        )
    }
}
