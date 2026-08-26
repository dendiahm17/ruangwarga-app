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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.FamilyRestroom
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentOrangeDark
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentPurpleLight
import com.example.ui.theme.AccentRed
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryBlueLight
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.PrimaryGreenDark
import com.example.ui.theme.PrimaryGreenLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.ui.viewmodel.RtrwUiState
import com.example.ui.viewmodel.RtrwViewModel

@Composable
fun ProfilScreen(
    uiState: RtrwUiState,
    viewModel: RtrwViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        // Native Full Horizontal App Header
        AppHeader(
            title = "Profil & Pengaturan",
            rightActionIcon = Icons.Default.Settings,
            onRightActionClick = { viewModel.openSettingsSheet() },
            isAdminMode = uiState.isAdminMode,
            onAdminToggle = { viewModel.toggleAdminMode() }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("profil_screen")
        ) {
            // Big Blue Resident Profile Header Card
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF1E3A8A),
                                    Color(0xFF2563EB),
                                    Color(0xFF3B82F6)
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
                                .size(74.dp)
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
                            fontSize = 18.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.White.copy(alpha = 0.22f)
                        ) {
                            Text(
                                text = "${uiState.profile.role} • ${uiState.profile.rt} / ${uiState.profile.rw}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                            )
                        }

                        if (uiState.profile.nik.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "NIK: ${uiState.profile.nik}",
                                fontSize = 11.5.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Quick action: Kelola Anggota Keluarga
                        Button(
                            onClick = { viewModel.openFamilyProfileSheet() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White.copy(alpha = 0.2f),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(42.dp)
                                .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                .testTag("lihat_profil_keluarga_button")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Outlined.FamilyRestroom,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(17.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Data Anggota Keluarga",
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            // ============================================================
            // KELOMPOK 1: KEPENDUDUKAN & DATA SAYA
            // ============================================================
            item {
                ProfileSectionGroup(
                    title = "Kependudukan & Identitas",
                    items = listOf(
                        ProfileMenuItemData(
                            title = "Data Diri Lengkap",
                            subtitle = "NIK, No. KK, Pekerjaan & Alamat",
                            icon = Icons.Outlined.Person,
                            iconTint = Color(0xFF2563EB),
                            iconBg = Color(0xFFEFF6FF),
                            onClick = { viewModel.openPersonalDataSheet() }
                        ),
                        ProfileMenuItemData(
                            title = "Kartu Keluarga & Anggota",
                            subtitle = "Susunan anggota keluarga terdaftar",
                            icon = Icons.Outlined.FamilyRestroom,
                            iconTint = Color(0xFF0284C7),
                            iconBg = Color(0xFFE0F2FE),
                            onClick = { viewModel.openFamilyProfileSheet() }
                        )
                    )
                )
            }

            // ============================================================
            // KELOMPOK 2: KEAKTIFAN WARGA & STATISTIK
            // ============================================================
            item {
                ProfileSectionGroup(
                    title = "Keaktifan Warga",
                    items = listOf(
                        ProfileMenuItemData(
                            title = "Jejak Partisipasi Saya",
                            subtitle = "Riwayat gotong royong & kontribusi sosial",
                            icon = Icons.Outlined.VolunteerActivism,
                            iconTint = Color(0xFF16A34A),
                            iconBg = Color(0xFFDCFCE7),
                            onClick = { viewModel.openPartisipasiSayaSheet() }
                        ),
                        ProfileMenuItemData(
                            title = "RW Pulse & Kinerja Wilayah",
                            subtitle = "Statistik kesehatan sosial dan capaian RW",
                            icon = Icons.Outlined.TrendingUp,
                            iconTint = Color(0xFF7C3AED),
                            iconBg = Color(0xFFF3E8FF),
                            onClick = { viewModel.openRwPulseDashboard() }
                        )
                    )
                )
            }

            // ============================================================
            // KELOMPOK 3: PENGATURAN & INFORMASI PENDUKUNG
            // ============================================================
            item {
                ProfileSectionGroup(
                    title = "Pengaturan & Bantuan",
                    items = listOf(
                        ProfileMenuItemData(
                            title = "Kontak Pengurus & Darurat",
                            subtitle = "Direktori no. telepon pengurus RT/RW",
                            icon = Icons.Outlined.Phone,
                            iconTint = Color(0xFFEA580C),
                            iconBg = Color(0xFFFFEDD5),
                            onClick = { viewModel.openEmergencyContactsSheet() }
                        ),
                        ProfileMenuItemData(
                            title = "Pengaturan Akun & Notifikasi",
                            subtitle = "Preferensi nada, reminder, dan keamanan akun",
                            icon = Icons.Outlined.Settings,
                            iconTint = Color(0xFF475569),
                            iconBg = Color(0xFFF1F5F9),
                            onClick = { viewModel.openSettingsSheet() }
                        ),
                        ProfileMenuItemData(
                            title = "Pusat Bantuan & Panduan",
                            subtitle = "Tata cara dan panduan aplikasi warga",
                            icon = Icons.Outlined.HelpOutline,
                            iconTint = Color(0xFF0D9488),
                            iconBg = Color(0xFFCCFBF1),
                            onClick = { viewModel.openHelpSheet() }
                        ),
                        ProfileMenuItemData(
                            title = "Tentang Aplikasi RuangWarga",
                            subtitle = "Versi 2.0.0 • Platform Digital RT/RW Terpadu",
                            icon = Icons.Outlined.Info,
                            iconTint = Color(0xFF64748B),
                            iconBg = Color(0xFFF8FAFC),
                            onClick = { viewModel.openAboutSheet() }
                        )
                    )
                )
            }

            // ============================================================
            // TOMBOL KELUAR / LOGOUT
            // ============================================================
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Button(
                        onClick = { viewModel.logout() },
                        shape = RoundedCornerShape(14.dp),
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
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFDC2626)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

data class ProfileMenuItemData(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val iconTint: Color,
    val iconBg: Color,
    val onClick: () -> Unit
)

@Composable
fun ProfileSectionGroup(
    title: String,
    items: List<ProfileMenuItemData>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Text(
            text = title,
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF64748B),
            modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                items.forEachIndexed { index, item ->
                    ProfileCardRowItem(
                        title = item.title,
                        subtitle = item.subtitle,
                        icon = item.icon,
                        iconTint = item.iconTint,
                        iconBg = item.iconBg,
                        onClick = item.onClick
                    )
                    if (index < items.size - 1) {
                        HorizontalDivider(
                            thickness = 0.8.dp,
                            color = Color(0xFFF1F5F9),
                            modifier = Modifier.padding(start = 58.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileCardRowItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
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
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = title,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = Color(0xFFCBD5E1),
            modifier = Modifier.size(13.dp)
        )
    }
}

