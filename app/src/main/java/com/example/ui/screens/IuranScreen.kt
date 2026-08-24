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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DuesRecordEntity
import com.example.ui.components.AppHeader
import com.example.ui.components.AppSearchBar
import com.example.ui.components.CircularDonutProgress
import com.example.ui.components.StatusBadge
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentGreenDark
import com.example.ui.theme.AccentGreenLight
import com.example.ui.theme.AccentRed
import com.example.ui.theme.BorderLight
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.ui.viewmodel.RtrwUiState
import com.example.ui.viewmodel.RtrwViewModel

@Composable
fun IuranScreen(
    uiState: RtrwUiState,
    viewModel: RtrwViewModel,
    modifier: Modifier = Modifier
) {
    val currentMonthDues = uiState.dues.find { it.periodeBulan == "Mei 2026" }
        ?: DuesRecordEntity(periodeBulan = "Mei 2026", jumlah = 20000, status = "Lunas", tanggalBayar = "10 Mei 2026")

    val isCurrentMonthPaid = currentMonthDues.status.equals("Lunas", ignoreCase = true)

    val filteredDues = if (uiState.iuranSearchQuery.isBlank()) uiState.dues else {
        uiState.dues.filter {
            it.periodeBulan.contains(uiState.iuranSearchQuery, ignoreCase = true) ||
            (it.metodePembayaran ?: "").contains(uiState.iuranSearchQuery, ignoreCase = true) ||
            (it.kodeTransaksi ?: "").contains(uiState.iuranSearchQuery, ignoreCase = true)
        }
    }

    val totalKasMasuk = uiState.cashTransactions.filter { it.tipe == "Pemasukan" }.sumOf { it.jumlah }
    val totalKasKeluar = uiState.cashTransactions.filter { it.tipe == "Pengeluaran" }.sumOf { it.jumlah }
    val saldoKas = totalKasMasuk - totalKasKeluar

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("iuran_screen")
    ) {
        item {
            AppHeader(
                title = "Iuran Kas Warga",
                unreadCount = uiState.unreadNotifications,
                onNotificationClick = { viewModel.openNotificationsSheet() },
                isAdminMode = uiState.isAdminMode,
                onAdminToggle = { viewModel.toggleAdminMode() }
            )
        }

        item {
            AppSearchBar(
                query = uiState.iuranSearchQuery,
                onQueryChange = { viewModel.setIuranSearchQuery(it) },
                placeholder = "Cari bulan atau metode pembayaran..."
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        // Big Green Featured Card (Iuran Bulan Mei 2026)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF1B8E5A),
                                Color(0xFF2E9E6B)
                            )
                        )
                    )
                    .padding(20.dp)
                    .testTag("iuran_featured_card")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Iuran Bulan Ini",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.85f),
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Mei 2026",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Rp 20.000",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        if (isCurrentMonthPaid) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color.White.copy(alpha = 0.2f))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Lunas",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Lunas (10 Mei 2026)",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        } else {
                            Button(
                                onClick = { viewModel.openPayDuesSheet() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.height(40.dp)
                            ) {
                                Text(
                                    text = "Bayar Sekarang",
                                    color = AccentGreenDark,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }

                    // Circular Icon on the Right
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Payments,
                            contentDescription = "Iuran",
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }
        }

        // Section: Ringkasan Pembayaran 2026 Card with Donut Chart
        item {
            Spacer(modifier = Modifier.height(14.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .testTag("ringkasan_pembayaran_card"),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Ringkasan Pembayaran 2026",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left: Donut Chart
                        CircularDonutProgress(
                            percentage = 100f,
                            lunasPercentageText = "100%",
                            size = 110.dp,
                            strokeWidth = 14.dp
                        )

                        // Right: Stats breakdown
                        Column(
                            modifier = Modifier.padding(start = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(AccentGreen)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Sudah Dibayar",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                    Text(
                                        text = "5 Bulan (Rp 100.000)",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(AccentRed)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Tunggakan",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                    Text(
                                        text = "0 Bulan (Rp 0)",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AccentGreen
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section: Riwayat Pembayaran List
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Riwayat Pembayaran",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Button(
                    onClick = { viewModel.openPayDuesSheet() },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text("+ Bayar Iuran", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        items(filteredDues, key = { it.id }) { itemDues ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { viewModel.openDuesDetail(itemDues) }
                    .testTag("dues_item_${itemDues.id}"),
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
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (itemDues.status == "Lunas") AccentGreenLight else Color(0xFFFFECEC)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = itemDues.periodeBulan,
                                tint = if (itemDues.status == "Lunas") AccentGreenDark else AccentRed,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Iuran ${itemDues.periodeBulan}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = itemDues.tanggalBayar ?: "Belum dibayar",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Rp ${String.format("%,d", itemDues.jumlah).replace(',', '.')}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        StatusBadge(status = itemDues.status)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
