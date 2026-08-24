package com.example.ui.dialogs

import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CashTransactionEntity
import com.example.ui.components.FilterChipTab
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentGreenDark
import com.example.ui.theme.AccentGreenLight
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentRed
import com.example.ui.theme.AccentRedLight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryBlueLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BukuKasBottomSheet(
    transactions: List<CashTransactionEntity>,
    isAdminMode: Boolean,
    onDismiss: () -> Unit,
    onOpenAddTransaction: () -> Unit,
    onDeleteTransaction: (id: Int) -> Unit
) {
    var filterTab by remember { mutableStateOf("Semua") }
    var searchQuery by remember { mutableStateOf("") }
    val totalPemasukan = transactions.filter { it.tipe == "Pemasukan" }.sumOf { it.jumlah }
    val totalPengeluaran = transactions.filter { it.tipe == "Pengeluaran" }.sumOf { it.jumlah }
    val saldoKas = totalPemasukan - totalPengeluaran
    val filteredList = transactions.filter { item ->
        val matchTab = when (filterTab) {
            "Pemasukan" -> item.tipe == "Pemasukan"
            "Pengeluaran" -> item.tipe == "Pengeluaran"
            else -> true
        }
        val matchSearch = searchQuery.isBlank() ||
                item.judul.contains(searchQuery, ignoreCase = true) ||
                item.kategori.contains(searchQuery, ignoreCase = true) ||
                item.keterangan.contains(searchQuery, ignoreCase = true)
        matchTab && matchSearch
    }
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismiss
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(horizontal = 20.dp, vertical = 14.dp)
                    .padding(bottom = 32.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(AccentGreenLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountBalanceWallet,
                                contentDescription = "Buku Kas",
                                tint = AccentGreenDark,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Buku Kas Keuangan RW 02",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Laporan keterbukaan & transparansi arus kas",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Featured Card: Saldo Kas RT
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF0F766E), Color(0xFF0D9488), Color(0xFF14B8A6))
                            )
                        )
                        .padding(18.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total Saldo Kas RT 03",
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.9f),
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Periode Mei 2026",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Rp ${String.format("%,d", saldoKas).replace(',', '.')}",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(modifier = Modifier.fillMaxWidth()) {
                            // Total Masuk
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.TrendingUp,
                                        contentDescription = null,
                                        tint = Color(0xFF86EFAC),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(text = "Total Masuk", fontSize = 10.sp, color = Color.White.copy(alpha = 0.8f))
                                    Text(
                                        text = "Rp ${String.format("%,d", totalPemasukan).replace(',', '.')}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                            // Total Keluar
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.TrendingDown,
                                        contentDescription = null,
                                        tint = Color(0xFFFCA5A5),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(text = "Total Keluar", fontSize = 10.sp, color = Color.White.copy(alpha = 0.8f))
                                    Text(
                                        text = "Rp ${String.format("%,d", totalPengeluaran).replace(',', '.')}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Action: Catat Transaksi
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Riwayat Arus Kas (${filteredList.size})",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Button(
                        onClick = onOpenAddTransaction,
                        colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .height(36.dp)
                            .testTag("btn_tambah_transaksi_kas")
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Catat Transaksi", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))

                // Search Bar & Filter Chips
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Cari rincian transaksi kas...") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = TextTertiary)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Semua", "Pemasukan", "Pengeluaran").forEach { tab ->
                        FilterChipTab(
                            label = tab,
                            isSelected = filterTab == tab,
                            onClick = { filterTab = tab }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                // List of Transactions
                if (filteredList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Tidak ada transaksi kas yang sesuai.",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        filteredList.forEach { item ->
                            val isIncome = item.tipe == "Pemasukan"
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp)),
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
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(CircleShape)
                                                .background(if (isIncome) AccentGreenLight else AccentRedLight),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = if (isIncome) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                                                contentDescription = item.tipe,
                                                tint = if (isIncome) AccentGreenDark else AccentRed,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = item.judul,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextPrimary
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = "${item.tanggal} • ${item.kategori}",
                                                fontSize = 11.sp,
                                                color = TextSecondary
                                            )
                                            if (item.keterangan.isNotBlank()) {
                                                Text(
                                                    text = item.keterangan,
                                                    fontSize = 11.sp,
                                                    color = TextTertiary
                                                )
                                            }
                                        }
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "${if (isIncome) "+ " else "- "}Rp ${String.format("%,d", item.jumlah).replace(',', '.')}",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isIncome) AccentGreenDark else AccentRed
                                        )
                                        if (isAdminMode) {
                                            IconButton(
                                                onClick = { onDeleteTransaction(item.id) },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = "Hapus",
                                                    tint = Color.Gray,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCashTransactionBottomSheet(
    onDismiss: () -> Unit,
    onSubmit: (tipe: String, judul: String, kategori: String, jumlah: Long, tanggal: String, keterangan: String) -> Unit
) {
    var tipe by remember { mutableStateOf("Pengeluaran") }
    var judul by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("Kebersihan & Sampah") }
    var jumlahText by remember { mutableStateOf("") }
    var tanggal by remember { mutableStateOf("23 Mei 2026") }
    var keterangan by remember { mutableStateOf("") }
    var expandedKategori by remember { mutableStateOf(false) }
    val incomeCategories = listOf("Iuran Bulanan Warga", "Donasi Warga", "Sewa Lapangan / Fasum", "Kas Masuk Lainnya")
    val expenseCategories = listOf("Kebersihan & Sampah", "Keamanan / Honor Satpam", "Perbaikan Fasum & Lampu", "Kegiatan Warga / Acara", "Operasional Sekretariat RT")
    val activeCategories = if (tipe == "Pemasukan") incomeCategories else expenseCategories

    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismiss
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(horizontal = 20.dp, vertical = 14.dp)
                    .padding(bottom = 32.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Catat Transaksi Kas RT",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup")
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))

                // Tipe Selector (Pemasukan vs Pengeluaran)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                tipe = "Pemasukan"
                                kategori = incomeCategories.first()
                            },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (tipe == "Pemasukan") 2.dp else 1.dp,
                            color = if (tipe == "Pemasukan") AccentGreen else BorderLight
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(imageVector = Icons.Default.TrendingUp, contentDescription = null, tint = AccentGreenDark, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Pemasukan (+)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AccentGreenDark)
                        }
                    }
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                tipe = "Pengeluaran"
                                kategori = expenseCategories.first()
                            },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (tipe == "Pengeluaran") 2.dp else 1.dp,
                            color = if (tipe == "Pengeluaran") AccentRed else BorderLight
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(imageVector = Icons.Default.TrendingDown, contentDescription = null, tint = AccentRed, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Pengeluaran (-)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AccentRed)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))

                // Judul Transaksi
                OutlinedTextField(
                    value = judul,
                    onValueChange = { judul = it },
                    label = { Text("Nama / Judul Transaksi *") },
                    placeholder = { Text(if (tipe == "Pemasukan") "Contoh: Iuran Kas Warga Blok C" else "Contoh: Pembelian Lampu Taman RT") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_judul_kas"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Kategori Dropdown
                ExposedDropdownMenuBox(
                    expanded = expandedKategori,
                    onExpandedChange = { expandedKategori = !expandedKategori }
                ) {
                    OutlinedTextField(
                        value = kategori,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Kategori Transaksi") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedKategori) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = expandedKategori,
                        onDismissRequest = { expandedKategori = false }
                    ) {
                        activeCategories.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item) },
                                onClick = {
                                    kategori = item
                                    expandedKategori = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                // Jumlah Nominal
                OutlinedTextField(
                    value = jumlahText,
                    onValueChange = { if (it.all { char -> char.isDigit() }) jumlahText = it },
                    label = { Text("Jumlah Nominal (Rp) *") },
                    placeholder = { Text("Contoh: 150000") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_nominal_kas"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Tanggal
                OutlinedTextField(
                    value = tanggal,
                    onValueChange = { tanggal = it },
                    label = { Text("Tanggal Transaksi") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Keterangan
                OutlinedTextField(
                    value = keterangan,
                    onValueChange = { keterangan = it },
                    label = { Text("Catatan / Keterangan Tambahan") },
                    placeholder = { Text("Contoh: Pembelian 5 unit lampu LED di Toko Terang") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 2
                )
                Spacer(modifier = Modifier.height(20.dp))

                // Submit Button
                Button(
                    onClick = {
                        val nominal = jumlahText.toLongOrNull() ?: 0L
                        if (judul.isNotBlank() && nominal > 0) {
                            onSubmit(tipe, judul, kategori, nominal, tanggal, keterangan)
                        }
                    },
                    enabled = judul.isNotBlank() && (jumlahText.toLongOrNull() ?: 0L) > 0,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_simpan_transaksi_kas"),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Simpan Catatan Kas",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}



