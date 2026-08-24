package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.dialogs.AboutBottomSheet
import com.example.ui.dialogs.AddCashTransactionBottomSheet
import com.example.ui.dialogs.AddCommunityEventBottomSheet
import com.example.ui.dialogs.AgendaCalendarBottomSheet
import com.example.ui.dialogs.AnnouncementDetailBottomSheet
import com.example.ui.dialogs.BukuKasBottomSheet
import com.example.ui.dialogs.ComplaintDetailBottomSheet
import com.example.ui.dialogs.CreateComplaintBottomSheet
import com.example.ui.dialogs.CreateLetterBottomSheet
import com.example.ui.dialogs.CreatePollBottomSheet
import com.example.ui.dialogs.DuesDetailBottomSheet
import com.example.ui.dialogs.EmergencyContactsBottomSheet
import com.example.ui.dialogs.FamilyProfileBottomSheet
import com.example.ui.dialogs.HelpBottomSheet
import com.example.ui.dialogs.LetterDetailBottomSheet
import com.example.ui.dialogs.NotificationsBottomSheet
import com.example.ui.dialogs.PanicSosBottomSheet
import com.example.ui.dialogs.PartisipasiSayaBottomSheet
import com.example.ui.dialogs.PayDuesBottomSheet
import com.example.ui.dialogs.PersonalDataBottomSheet
import com.example.ui.dialogs.PollingBottomSheet
import com.example.ui.dialogs.PostDetailBottomSheet
import com.example.ui.dialogs.RwPulseDashboardBottomSheet
import com.example.ui.dialogs.SettingsBottomSheet
import com.example.ui.dialogs.SiskamlingScheduleBottomSheet
import com.example.ui.screens.AktivitasScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.IuranScreen
import com.example.ui.screens.KegiatanScreen
import com.example.ui.screens.NotifikasiScreen
import com.example.ui.screens.PengaduanScreen
import com.example.ui.screens.PengurusScreen
import com.example.ui.screens.PengumumanScreen
import com.example.ui.screens.PesanScreen
import com.example.ui.screens.ProfilScreen
import com.example.ui.screens.SosialScreen
import com.example.ui.screens.SuratScreen
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentRed
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryBlueLight
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.MainTab
import com.example.ui.viewmodel.RtrwViewModel

import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Domain
import androidx.compose.material.icons.outlined.People
import com.example.ui.screens.LayananScreen
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.PrimaryGreenDark
import com.example.ui.theme.PrimaryGreenLight

data class NavigationTabItem(
    val tab: MainTab,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val isCenterButton: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(
    viewModel: RtrwViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    val tabs = listOf(
        NavigationTabItem(
            tab = MainTab.BERANDA,
            label = "Beranda",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        NavigationTabItem(
            tab = MainTab.AGENDA,
            label = "Agenda",
            selectedIcon = Icons.Filled.CalendarMonth,
            unselectedIcon = Icons.Outlined.CalendarMonth
        ),
        NavigationTabItem(
            tab = MainTab.BUAT,
            label = "Buat",
            selectedIcon = Icons.Filled.Add,
            unselectedIcon = Icons.Filled.Add,
            isCenterButton = true
        ),
        NavigationTabItem(
            tab = MainTab.LAYANAN,
            label = "Layanan",
            selectedIcon = Icons.Filled.Domain,
            unselectedIcon = Icons.Outlined.Domain
        ),
        NavigationTabItem(
            tab = MainTab.PROFIL,
            label = "Saya",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person
        )
    )

    if (!uiState.isLoggedIn) {
        com.example.ui.screens.auth.AuthScreen(
            uiState = uiState,
            viewModel = viewModel,
            modifier = modifier
        )
        return
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        
        bottomBar = {
            // Modern Bottom Navigation Bar matching RuangWarga Mockup
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                    .shadow(12.dp, RoundedCornerShape(24.dp), spotColor = Color(0x25000000)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    tabs.forEach { item ->
                        val isSelected = uiState.selectedTab == item.tab ||
                                (item.tab == MainTab.AGENDA && uiState.selectedTab == MainTab.AKTIVITAS) ||
                                (item.tab == MainTab.PROFIL && (uiState.selectedTab == MainTab.WARGA || uiState.selectedTab == MainTab.SOSIAL))
                        val interactionSource = remember { MutableInteractionSource() }

                        if (item.isCenterButton) {
                            // Center Floating-Style Action Button for "Buat"
                            Column(
                                modifier = Modifier
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        viewModel.openCreatePostSheet()
                                    }
                                    .padding(horizontal = 6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(PrimaryGreenDark),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Add,
                                        contentDescription = "Buat",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Buat",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = TextSecondary
                                )
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        viewModel.selectTab(item.tab)
                                    }
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                    .testTag("nav_tab_${item.label.lowercase()}"),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSelected) PrimaryGreenLight else Color.Transparent)
                                        .padding(horizontal = 8.dp, vertical = 3.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = item.label,
                                        tint = if (isSelected) PrimaryGreenDark else TextSecondary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = item.label,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) PrimaryGreenDark else TextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = if (uiState.showAllAnnouncementsScreen) "pengumuman_full" else uiState.selectedTab.name,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screen_transition"
            ) { target ->
                when (target) {
                    "pengumuman_full" -> PengumumanScreen(uiState = uiState, viewModel = viewModel)
                    MainTab.BERANDA.name -> HomeScreen(uiState = uiState, viewModel = viewModel)
                    MainTab.AGENDA.name, MainTab.AKTIVITAS.name -> AktivitasScreen(uiState = uiState, viewModel = viewModel)
                    MainTab.LAYANAN.name -> LayananScreen(uiState = uiState, viewModel = viewModel)
                    MainTab.WARGA.name -> PengurusScreen(uiState = uiState, viewModel = viewModel)
                    MainTab.SOSIAL.name -> SosialScreen(uiState = uiState, viewModel = viewModel)
                    MainTab.KOTAK_MASUK.name, MainTab.PESAN.name -> PesanScreen(uiState = uiState, viewModel = viewModel)
                    MainTab.PROFIL.name -> ProfilScreen(uiState = uiState, viewModel = viewModel)
                    else -> HomeScreen(uiState = uiState, viewModel = viewModel)
                }
            }
        }
    }

    // Modal Bottom Sheets & Extra Views

    // Surat Sheet Modal
    if (uiState.showSuratScreenSheet) {
        Dialog(
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = { viewModel.closeSuratScreenSheet() }
        ) {
            Surface(modifier = Modifier.fillMaxSize(), color = BackgroundLight) {
                SuratScreen(
                    uiState = uiState,
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    // Iuran Sheet Modal
    if (uiState.showIuranScreenSheet) {
        Dialog(
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = { viewModel.closeIuranScreenSheet() }
        ) {
            Surface(modifier = Modifier.fillMaxSize(), color = BackgroundLight) {
                IuranScreen(
                    uiState = uiState,
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    // Create Letter Sheet
    if (uiState.showCreateLetterSheet) {
        CreateLetterBottomSheet(
            preselectedType = uiState.preselectedLetterType,
            onDismiss = { viewModel.closeCreateLetterSheet() },
            onSubmit = { jenis, keperluan, catatan ->
                viewModel.submitLetter(jenis, keperluan, catatan)
            }
        )
    }

    uiState.selectedLetterForDetail?.let { letter ->
        LetterDetailBottomSheet(
            letter = letter,
            profile = uiState.profile,
            isAdminMode = uiState.isAdminMode,
            onDismiss = { viewModel.closeLetterDetail() },
            onAdminApprove = { viewModel.adminApproveLetter(letter.id) },
            onAdminProcess = { viewModel.adminProcessLetter(letter.id) },
            onAdminReject = { viewModel.adminRejectLetter(letter.id) }
        )
    }

    if (uiState.showCreateComplaintSheet) {
        CreateComplaintBottomSheet(
            onDismiss = { viewModel.closeCreateComplaintSheet() },
            onSubmit = { judul, lokasi, kategori, deskripsi, fotoBukti ->
                viewModel.submitComplaint(judul, lokasi, kategori, deskripsi, fotoBukti)
            }
        )
    }

    uiState.selectedComplaintForDetail?.let { complaint ->
        ComplaintDetailBottomSheet(
            complaint = complaint,
            isAdminMode = uiState.isAdminMode,
            onDismiss = { viewModel.closeComplaintDetail() },
            onAdminUpdate = { status, tanggapan ->
                viewModel.adminUpdateComplaintStatus(complaint.id, status, tanggapan)
            }
        )
    }

    if (uiState.showPayDuesSheet) {
        PayDuesBottomSheet(
            onDismiss = { viewModel.closePayDuesSheet() },
            onPay = { bulan, metode, buktiBayar ->
                viewModel.payDues(bulan, metode, buktiBayar)
            }
        )
    }

    uiState.showDuesDetailSheet?.let { dues ->
        DuesDetailBottomSheet(
            dues = dues,
            profile = uiState.profile,
            isAdminMode = uiState.isAdminMode,
            onDismiss = { viewModel.closeDuesDetail() },
            onAdminToggleStatus = { makeLunas -> viewModel.adminToggleDuesStatus(dues.id, makeLunas) }
        )
    }

    if (uiState.showFamilyProfileSheet) {
        FamilyProfileBottomSheet(
            profile = uiState.profile,
            familyMembers = uiState.familyMembers,
            onDismiss = { viewModel.closeFamilyProfileSheet() },
            onAddMember = { member ->
                viewModel.addFamilyMember(member)
            },
            onDeleteMember = { member ->
                viewModel.deleteFamilyMember(member)
            }
        )
    }

    uiState.selectedAnnouncementForDetail?.let { announcement ->
        AnnouncementDetailBottomSheet(
            announcement = announcement,
            onDismiss = { viewModel.closeAnnouncementDetail() }
        )
    }

    if (uiState.showNotificationsSheet) {
        NotificationsBottomSheet(
            notifications = uiState.notifications,
            onDismiss = { viewModel.closeNotificationsSheet() },
            onClearAll = { viewModel.clearNotifications() }
        )
    }

    if (uiState.showEmergencyContactsSheet) {
        EmergencyContactsBottomSheet(
            onDismiss = { viewModel.closeEmergencyContactsSheet() }
        )
    }

    if (uiState.showSiskamlingScheduleSheet) {
        SiskamlingScheduleBottomSheet(
            schedules = uiState.rondaSchedules,
            onDismiss = { viewModel.closeSiskamlingScheduleSheet() },
            onConfirmAttendance = { id, status ->
                viewModel.updateRondaAttendance(id, status)
            }
        )
    }

    if (uiState.showPersonalDataSheet) {
        PersonalDataBottomSheet(
            profile = uiState.profile,
            onDismiss = { viewModel.closePersonalDataSheet() },
            onSave = { updated ->
                viewModel.updateProfile(updated)
            }
        )
    }

    if (uiState.showSettingsSheet) {
        SettingsBottomSheet(onDismiss = { viewModel.closeSettingsSheet() })
    }

    if (uiState.showHelpSheet) {
        HelpBottomSheet(onDismiss = { viewModel.closeHelpSheet() })
    }

    if (uiState.showAboutSheet) {
        AboutBottomSheet(onDismiss = { viewModel.closeAboutSheet() })
    }

    // SOS / Panic Button Sheet
    if (uiState.showPanicSosSheet) {
        PanicSosBottomSheet(
            defaultLocation = "${uiState.profile.alamat}, ${uiState.profile.rt}/${uiState.profile.rw}",
            recentAlerts = uiState.emergencyAlerts,
            isAdminMode = uiState.isAdminMode,
            onDismiss = { viewModel.closePanicSosSheet() },
            onTriggerAlert = { jenis, lokasi, catatan ->
                viewModel.triggerEmergencyAlert(jenis, lokasi, catatan)
            },
            onResolveAlert = { id ->
                viewModel.resolveEmergencyAlert(id)
            }
        )
    }

    // Transparansi Buku Kas RT Sheets
    if (uiState.showBukuKasSheet) {
        BukuKasBottomSheet(
            transactions = uiState.cashTransactions,
            isAdminMode = uiState.isAdminMode,
            onDismiss = { viewModel.closeBukuKasSheet() },
            onOpenAddTransaction = { viewModel.openAddCashTransactionSheet() },
            onDeleteTransaction = { id -> viewModel.deleteCashTransaction(id) }
        )
    }

    // Aset & Inventaris RW Dialog
    if (uiState.showAssetRwSheet) {
        com.example.ui.dialogs.AssetRwDialog(
            assets = uiState.assets,
            isAdminMode = uiState.isAdminMode,
            onDismiss = { viewModel.closeAssetRwSheet() },
            onBorrowAsset = { id, nama -> viewModel.borrowAsset(id, nama) },
            onReturnAsset = { id, nama -> viewModel.returnAsset(id, nama) },
            onAddAsset = { kode, nama, kat, jml, sat, kond, lok, pj, thn, nil, cat ->
                viewModel.addAsset(kode, nama, kat, jml, sat, kond, lok, pj, thn, nil, cat)
            },
            onDeleteAsset = { id -> viewModel.deleteAsset(id) }
        )
    }

    if (uiState.showAddCashTransactionSheet) {
        AddCashTransactionBottomSheet(
            onDismiss = { viewModel.closeAddCashTransactionSheet() },
            onSubmit = { tipe, judul, kategori, jumlah, tanggal, keterangan ->
                viewModel.addCashTransaction(tipe, judul, kategori, jumlah, tanggal, keterangan)
            }
        )
    }

    // Kalender Kegiatan & Agenda Warga Sheets
    if (uiState.showAgendaCalendarSheet) {
        AgendaCalendarBottomSheet(
            events = uiState.communityEvents,
            isAdminMode = uiState.isAdminMode,
            onDismiss = { viewModel.closeAgendaCalendarSheet() },
            onRsvpChange = { eventId, status ->
                viewModel.updateEventRsvp(eventId, status)
            },
            onOpenAddEvent = { viewModel.openAddCommunityEventSheet() }
        )
    }

    if (uiState.showAddCommunityEventSheet) {
        AddCommunityEventBottomSheet(
            onDismiss = { viewModel.closeAddCommunityEventSheet() },
            onSubmit = { judul, kategori, tanggal, waktu, lokasi, deskripsi, penanggungJawab ->
                viewModel.addCommunityEvent(judul, kategori, tanggal, waktu, lokasi, deskripsi, penanggungJawab)
            }
        )
    }

    // Musyawarah & Polling RT Sheets
    if (uiState.showPollingSheet) {
        PollingBottomSheet(
            polls = uiState.polls,
            isAdminMode = uiState.isAdminMode,
            onDismiss = { viewModel.closePollingSheet() },
            onVote = { pollId, option ->
                viewModel.votePoll(pollId, option)
            },
            onOpenCreatePoll = { viewModel.openCreatePollSheet() },
            onClosePoll = { pollId ->
                viewModel.closePoll(pollId)
            }
        )
    }

    if (uiState.showCreatePollSheet) {
        CreatePollBottomSheet(
            onDismiss = { viewModel.closeCreatePollSheet() },
            onSubmit = { judul, deskripsi, kategori, batasWaktu, opsiA, opsiB, opsiC ->
                viewModel.createPoll(judul, deskripsi, kategori, batasWaktu, opsiA, opsiB, opsiC)
            }
        )
    }

    // RW Pulse Dashboard Sheet
    if (uiState.showRwPulseDashboard) {
        RwPulseDashboardBottomSheet(
            onDismiss = { viewModel.closeRwPulseDashboard() }
        )
    }

    // Partisipasi Saya Sheet
    if (uiState.showPartisipasiSayaSheet) {
        PartisipasiSayaBottomSheet(
            participations = uiState.userParticipations,
            onDismiss = { viewModel.closePartisipasiSayaSheet() }
        )
    }

    // Community Post Detail Sheet (Mockup Layar 4, 5, 7, 8)
    uiState.selectedPostForDetail?.let { post ->
        Dialog(
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = { viewModel.closePostDetail() }
        ) {
            Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
                PostDetailBottomSheet(
                    post = post,
                    viewModel = viewModel,
                    isAdminMode = uiState.isAdminMode,
                    onDismiss = { viewModel.closePostDetail() }
                )
            }
        }
    }

    // Layar 2: Filter Aktivitas Sheet
    if (uiState.showAktivitasFilterSheet) {
        Dialog(
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = { viewModel.closeAktivitasFilterSheet() }
        ) {
            Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
                com.example.ui.dialogs.AktivitasFilterBottomSheet(
                    currentCategory = uiState.aktivitasCategoryFilter,
                    currentTime = uiState.aktivitasTimeFilter,
                    onApply = { category, time ->
                        viewModel.setAktivitasCategoryFilter(category)
                        viewModel.setAktivitasTimeFilter(time)
                    },
                    onDismiss = { viewModel.closeAktivitasFilterSheet() }
                )
            }
        }
    }

    // Layar 3: Buat Aktivitas - Pilih Tipe Sheet
    if (uiState.showCreatePostSheet) {
        Dialog(
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = { viewModel.closeCreatePostSheet() }
        ) {
            Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
                com.example.ui.dialogs.AktivitasCreateSelectTypeBottomSheet(
                    onSelectType = { selectedType ->
                        viewModel.selectAktivitasTypeAndOpenForm(selectedType)
                    },
                    onDismiss = { viewModel.closeCreatePostSheet() }
                )
            }
        }
    }

    // Layar 6: Form Buat Aktivitas / Kegiatan Sheet
    if (uiState.showCreateAktivitasFormSheet) {
        Dialog(
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = { viewModel.closeCreateAktivitasFormSheet() }
        ) {
            Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
                com.example.ui.dialogs.AktivitasFormBottomSheet(
                    type = uiState.selectedAktivitasTypeForCreate,
                    onDismiss = { viewModel.closeCreateAktivitasFormSheet() },
                    onSubmit = { post ->
                        val currentName = uiState.profile.nama.ifBlank { "Warga Lingkungan" }
                        val currentRole = uiState.profile.role.ifBlank { "Warga" }
                        val currentRtRw = "${uiState.profile.rt} / ${uiState.profile.rw}"
                        val enrichedPost = post.copy(
                            authorName = currentName,
                            authorRole = "$currentRole ${uiState.profile.rt}",
                            authorRtRw = currentRtRw
                        )
                        viewModel.addCustomFeedPost(enrichedPost)
                    }
                )
            }
        }
    }

    // 🚨 Emergency Alarm Bottom Sheet
    if (uiState.showEmergencyAlarmSheet) {
        Dialog(
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = { viewModel.closeEmergencyAlarmSheet() }
        ) {
            Surface(modifier = Modifier.fillMaxSize(), color = BackgroundLight) {
                com.example.ui.dialogs.EmergencyAlarmBottomSheet(
                    alerts = uiState.emergencyAlerts,
                    viewModel = viewModel,
                    onDismiss = { viewModel.closeEmergencyAlarmSheet() }
                )
            }
        }
    }

    // 🚨 Emergency Alarm Detail Sheet
    if (uiState.showEmergencyAlarmDetailSheet) {
        uiState.selectedEmergencyAlertForDetail?.let { alert ->
            Dialog(
                properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false),
                onDismissRequest = { viewModel.closeEmergencyAlarmDetail() }
            ) {
                Surface(modifier = Modifier.fillMaxSize(), color = BackgroundLight) {
                    com.example.ui.dialogs.EmergencyAlarmDetailBottomSheet(
                        alert = alert,
                        viewModel = viewModel,
                        isAdminMode = uiState.isAdminMode,
                        onDismiss = { viewModel.closeEmergencyAlarmDetail() }
                    )
                }
            }
        }
    }

    // 🚨 Form Laporkan Keadaan Darurat Sheet (Warga)
    if (uiState.showReportEmergencySheet) {
        Dialog(
            properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = { viewModel.closeReportEmergencySheet() }
        ) {
            Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
                com.example.ui.dialogs.ReportEmergencyBottomSheet(
                    viewModel = viewModel,
                    onDismiss = { viewModel.closeReportEmergencySheet() }
                )
            }
        }
    }
}
