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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.HowToVote
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.AppHeader
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentGreenDark
import com.example.ui.theme.AccentGreenLight
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentPurpleLight
import com.example.ui.theme.AccentRed
import com.example.ui.theme.BorderLight
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryBlueLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.MainTab
import com.example.ui.viewmodel.RtrwUiState
import com.example.ui.viewmodel.RtrwViewModel

@Composable
fun ProfilScreen(
    uiState: RtrwUiState,
    viewModel: RtrwViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("profil_screen")
    ) {
        // Big Blue Resident Card
        item {
            Spacer(modifier = Modifier.height(14.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF1976D2),
                                Color(0xFF2196F3)
                            )
                        )
                    )
                    .padding(20.dp)
                    .testTag("profile_resident_card"),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Circular Avatar
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .border(3.dp, Color.White, CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.budi_avatar_1787473233393),
                            contentDescription = "Avatar",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = uiState.profile.nama.ifBlank { "Warga RuangWarga" },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color.White.copy(alpha = 0.25f)
                    ) {
                        Text(
                            text = "${uiState.profile.role} • ${uiState.profile.rt} / ${uiState.profile.rw}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }

                    if (uiState.profile.nik.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "NIK: ${uiState.profile.nik}",
                            fontSize = 11.5.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Button: "Lihat Profil Keluarga"
                    Button(
                        onClick = { viewModel.openFamilyProfileSheet() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White.copy(alpha = 0.2f),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .testTag("lihat_profil_keluarga_button")
                    ) {
                        Text(
                            text = "Lihat Data Anggota Keluarga",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Profile Menu Items
        item {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ProfileMenuRowItem(
                    title = "Jejak Partisipasi Saya",
                    subtitle = "Riwayat kegiatan & bantuan sosial",
                    icon = Icons.Outlined.VolunteerActivism,
                    onClick = { viewModel.openPartisipasiSayaSheet() }
                )
                ProfileMenuRowItem(
                    title = "RW Pulse & Kinerja Wilayah",
                    subtitle = "Kesehatan sosial & transparansi",
                    icon = Icons.Outlined.TrendingUp,
                    onClick = { viewModel.openRwPulseDashboard() }
                )
                ProfileMenuRowItem(
                    title = "Layanan Surat Pengantar",
                    subtitle = "Pengajuan surat domisili, SKCK, dll",
                    icon = Icons.Outlined.Description,
                    onClick = { viewModel.openSuratScreenSheet() }
                )
                ProfileMenuRowItem(
                    title = "Iuran Kas Warga",
                    subtitle = "Kas RT, sampah, dan keamanan",
                    icon = Icons.Outlined.Payments,
                    onClick = { viewModel.openIuranScreenSheet() }
                )
                ProfileMenuRowItem(
                    title = "Transparansi Buku Kas RT",
                    subtitle = "Laporan keluar masuk dana warga",
                    icon = Icons.Outlined.AccountBalanceWallet,
                    onClick = { viewModel.openBukuKasSheet() }
                )
                ProfileMenuRowItem(
                    title = "Musyawarah & Polling Warga",
                    subtitle = "Ikuti pengambilan keputusan RT/RW",
                    icon = Icons.Outlined.HowToVote,
                    onClick = { viewModel.openPollingSheet() }
                )
                ProfileMenuRowItem(
                    title = "Jadwal Siskamling / Ronda",
                    subtitle = "Jadwal pos ronda malam",
                    icon = Icons.Outlined.Security,
                    onClick = { viewModel.openSiskamlingScheduleSheet() }
                )
                ProfileMenuRowItem(
                    title = "Tombol Darurat (SOS RT)",
                    subtitle = "Sinyal instan ke pos satpam",
                    icon = Icons.Outlined.WarningAmber,
                    onClick = { viewModel.openPanicSosSheet() }
                )
                ProfileMenuRowItem(
                    title = "Data Diri Lengkap",
                    subtitle = "NIK, No HP, Pekerjaan, Alamat",
                    icon = Icons.Outlined.Person,
                    onClick = { viewModel.openPersonalDataSheet() }
                )
                ProfileMenuRowItem(
                    title = "Kontak Darurat & Pengurus",
                    subtitle = "No. Telp Ketua RT, RW, Ambulans",
                    icon = Icons.Outlined.Phone,
                    onClick = { viewModel.openEmergencyContactsSheet() }
                )
                ProfileMenuRowItem(
                    title = "Pengaturan Notifikasi & Akun",
                    subtitle = "Preferensi nada, reminder, dan akun",
                    icon = Icons.Outlined.Settings,
                    onClick = { viewModel.openSettingsSheet() }
                )
                ProfileMenuRowItem(
                    title = "Tentang Aplikasi RuangWarga",
                    subtitle = "Versi 2.0.0 • Platform Digital Warga Modern",
                    icon = Icons.Outlined.Info,
                    onClick = { viewModel.openAboutSheet() }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Tombol Keluar / Logout Akun
                Button(
                    onClick = { viewModel.logout() },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFEE2E2),
                        contentColor = Color(0xFFDC2626)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🚪", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Keluar dari Akun",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun ProfileMenuRowItem(
    title: String,
    subtitle: String? = null,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(PrimaryBlueLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    if (subtitle != null) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = subtitle,
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = Color(0xFFBDC3C7),
                modifier = Modifier.size(14.dp)
            )
        }
    }
}
