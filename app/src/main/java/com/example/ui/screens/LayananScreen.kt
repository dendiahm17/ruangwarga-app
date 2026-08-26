package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.shadow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.FolderShared
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.HomeRepairService
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentGreenDark
import com.example.ui.theme.AccentGreenLight
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentOrangeDark
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentPurpleDark
import com.example.ui.theme.AccentRed
import com.example.ui.theme.AccentRedDark
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.PrimaryGreenDark
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.ui.viewmodel.MainTab
import com.example.ui.viewmodel.RtrwUiState
import com.example.ui.viewmodel.RtrwViewModel

data class ServiceMenuCardItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val iconTint: Color,
    val iconBg: Color
)

data class ServiceCategoryGroup(
    val categoryTitle: String,
    val categorySubtitle: String,
    val items: List<ServiceMenuCardItem>
)

@Composable
fun LayananScreen(
    uiState: RtrwUiState,
    viewModel: RtrwViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val serviceCategories = listOf(
        ServiceCategoryGroup(
            categoryTitle = "Administrasi & Keuangan",
            categorySubtitle = "Pelayanan persuratan & transparansi kas warga",
            items = listOf(
                ServiceMenuCardItem(
                    id = "surat",
                    title = "Surat",
                    subtitle = "Pengajuan & riwayat",
                    icon = Icons.Default.Description,
                    iconTint = Color(0xFF2563EB),
                    iconBg = Color(0xFFEFF6FF)
                ),
                ServiceMenuCardItem(
                    id = "iuran",
                    title = "Iuran",
                    subtitle = "Tagihan & pembayaran",
                    icon = Icons.Default.AccountBalanceWallet,
                    iconTint = Color(0xFFD97706),
                    iconBg = Color(0xFFFEF3C7)
                ),
                ServiceMenuCardItem(
                    id = "buku_kas",
                    title = "Buku Kas RW",
                    subtitle = "Laporan & transparansi",
                    icon = Icons.Default.AccountBalance,
                    iconTint = Color(0xFF047857),
                    iconBg = Color(0xFFD1FAE5)
                )
            )
        ),
        ServiceCategoryGroup(
            categoryTitle = "Fasilitas & Lingkungan",
            categorySubtitle = "Inventaris perlengkapan dan pengaduan masalah",
            items = listOf(
                ServiceMenuCardItem(
                    id = "aset_rw",
                    title = "Aset RW",
                    subtitle = "Inventaris & peminjaman",
                    icon = Icons.Default.Inventory2,
                    iconTint = Color(0xFF059669),
                    iconBg = Color(0xFFD1FAE5)
                ),
                ServiceMenuCardItem(
                    id = "laporan",
                    title = "Laporan",
                    subtitle = "Sampaikan keluhan",
                    icon = Icons.Default.Warning,
                    iconTint = Color(0xFFDC2626),
                    iconBg = Color(0xFFFEE2E2)
                ),
                ServiceMenuCardItem(
                    id = "alarm_darurat",
                    title = "Alarm Darurat (SOS)",
                    subtitle = "Pusat siaga & sirene",
                    icon = Icons.Default.Warning,
                    iconTint = Color(0xFFB91C1C),
                    iconBg = Color(0xFFFFE4E6)
                )
            )
        ),
        ServiceCategoryGroup(
            categoryTitle = "Komunitas Warga",
            categorySubtitle = "Direktori pengurus dan warga lingkungan RT/RW",
            items = listOf(
                ServiceMenuCardItem(
                    id = "warga",
                    title = "Warga & Pengurus",
                    subtitle = "Direktori & kontak",
                    icon = Icons.Default.People,
                    iconTint = Color(0xFF0D9488),
                    iconBg = Color(0xFFCCFBF1)
                )
            )
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        // Native Full Horizontal App Header
        com.example.ui.components.AppHeader(
            title = "Layanan Terpadu",
            rightActionIcon = Icons.Default.HelpOutline,
            onRightActionClick = { viewModel.openHelpSheet() }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .testTag("layanan_screen"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

        // Categorized Service Sections
        serviceCategories.forEach { category ->
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Category Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(4.dp, 16.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(PrimaryGreen)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = category.categoryTitle,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = category.categorySubtitle,
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    // Category Cards in 2-Column Grid
                    val chunked = category.items.chunked(2)
                    for (row in chunked) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            for (item in row) {
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(105.dp)
                                        .clickable {
                                            when (item.id) {
                                                "surat" -> viewModel.openSuratScreenSheet()
                                                "iuran" -> viewModel.openIuranScreenSheet()
                                                "laporan" -> viewModel.openPengaduanScreenSheet()
                                                "alarm_darurat" -> viewModel.openAlarmScreen()
                                                "warga" -> viewModel.selectTab(MainTab.WARGA)
                                                "buku_kas" -> viewModel.openBukuKasSheet()
                                                "aset_rw" -> viewModel.openAssetRwSheet()
                                                else -> {}
                                            }
                                        },
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    border = BorderStroke(1.dp, BorderLight)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(12.dp),
                                        verticalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(34.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(item.iconBg),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = item.icon,
                                                contentDescription = null,
                                                tint = item.iconTint,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }

                                        Column {
                                            Text(
                                                text = item.title,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = item.iconTint
                                            )
                                            Spacer(modifier = Modifier.height(1.dp))
                                            Text(
                                                text = item.subtitle,
                                                fontSize = 10.sp,
                                                color = TextSecondary,
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }
                            }
                            if (row.size < 2) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }

        // Bottom Help Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.openEmergencyContactsSheet() },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                border = BorderStroke(1.dp, Color(0xFFBBF7D0))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFDCFCE7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Headphones,
                            contentDescription = null,
                            tint = PrimaryGreenDark,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Butuh bantuan?",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryGreenDark
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Hubungi pengurus atau lapor melalui layanan yang tersedia.",
                            fontSize = 11.sp,
                            color = TextSecondary,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
}
