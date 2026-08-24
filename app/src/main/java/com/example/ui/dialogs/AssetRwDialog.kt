package com.example.ui.dialogs

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EventSeat
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.AssetRwEntity
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentGreenDark
import com.example.ui.theme.AccentGreenLight
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentOrangeDark
import com.example.ui.theme.AccentOrangeLight
import com.example.ui.theme.AccentRed
import com.example.ui.theme.AccentRedDark
import com.example.ui.theme.AccentRedLight
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryBlueLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AssetRwDialog(
    assets: List<AssetRwEntity>,
    isAdminMode: Boolean,
    onDismiss: () -> Unit,
    onBorrowAsset: (assetId: Int, namaAset: String) -> Unit,
    onReturnAsset: (assetId: Int, namaAset: String) -> Unit,
    onAddAsset: (
        kode: String,
        nama: String,
        kategori: String,
        jumlah: Int,
        satuan: String,
        kondisi: String,
        lokasi: String,
        pj: String,
        tahun: String,
        nilai: Long,
        catatan: String
    ) -> Unit,
    onDeleteAsset: (assetId: Int) -> Unit
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf("Semua") }
    var searchQuery by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }

    val categories = listOf("Semua", "Peralatan Acara", "Elektronik & Sound", "Kebersihan & Taman", "Keamanan / Ronda", "Fasilitas Umum")

    val filteredAssets = assets.filter { item ->
        val matchCat = if (selectedCategory == "Semua") true else item.kategori.contains(selectedCategory, ignoreCase = true)
        val matchSearch = searchQuery.isBlank() ||
                item.namaAset.contains(searchQuery, ignoreCase = true) ||
                item.kodeAset.contains(searchQuery, ignoreCase = true) ||
                item.lokasiPenyimpanan.contains(searchQuery, ignoreCase = true)
        matchCat && matchSearch
    }

    val totalUnit = assets.sumOf { it.jumlah }
    val totalTersedia = assets.filter { it.statusKetersediaan == "Tersedia" }.sumOf { it.jumlah }
    val totalDipinjam = assets.filter { it.statusKetersediaan == "Dipinjam" }.sumOf { it.jumlah }

    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismiss
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // 1. Top Header
                item {
                    Spacer(modifier = Modifier.height(14.dp))
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
                                    .background(Color(0xFFD1FAE5)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Inventory2,
                                    contentDescription = "Aset RW",
                                    tint = AccentGreenDark,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Daftar Aset RW 02",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Inventaris barang & fasilitas pinjaman warga",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup")
                        }
                    }
                }

                // 2. Summary Dashboard Banner
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF047857), Color(0xFF059669), Color(0xFF10B981))
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
                                    text = "REKAPITULASI INVENTARIS",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White.copy(alpha = 0.85f),
                                    letterSpacing = 0.5.sp
                                )
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color.White.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "${assets.size} Jenis Aset",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(text = "Total Item", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                                    Text(text = "$totalUnit Unit", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                                }
                                Column {
                                    Text(text = "Siap Dipinjam", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                                    Text(text = "$totalTersedia Unit", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFBBF7D0))
                                }
                                Column {
                                    Text(text = "Sedang Digunakan", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                                    Text(text = "$totalDipinjam Unit", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFFEF08A))
                                }
                            }
                        }
                    }
                }

                // 3. Search & Category Filter
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        placeholder = { Text("Cari aset (tenda, kursi, genset, sound)...", fontSize = 13.sp) },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(18.dp))
                        },
                        singleLine = true
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        categories.take(3).forEach { cat ->
                            val isSel = selectedCategory == cat
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSel) AccentGreenLight else Color(0xFFF1F5F9))
                                    .border(1.dp, if (isSel) AccentGreenDark else Color.Transparent, RoundedCornerShape(10.dp))
                                    .clickable { selectedCategory = cat }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cat,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSel) AccentGreenDark else TextPrimary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        categories.drop(3).forEach { cat ->
                            val isSel = selectedCategory == cat
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSel) AccentGreenLight else Color(0xFFF1F5F9))
                                    .border(1.dp, if (isSel) AccentGreenDark else Color.Transparent, RoundedCornerShape(10.dp))
                                    .clickable { selectedCategory = cat }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cat,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSel) AccentGreenDark else TextPrimary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }

                // 4. Action Button for Admins
                if (isAdminMode) {
                    item {
                        Button(
                            onClick = { showAddDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AccentGreenDark)
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Tambah Aset Baru (Pengurus)", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // 5. Asset List Cards
                items(filteredAssets, key = { it.id }) { item ->
                    AssetCardItem(
                        asset = item,
                        isAdminMode = isAdminMode,
                        onBorrow = { onBorrowAsset(item.id, item.namaAset) },
                        onReturn = { onReturnAsset(item.id, item.namaAset) },
                        onDelete = { onDeleteAsset(item.id) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }

    // Modal Tambah Aset Baru
    if (showAddDialog) {
        AddAssetDialog(
            onDismiss = { showAddDialog = false },
            onSubmit = { kode, nama, kat, jml, sat, kond, lok, pj, thn, nil, cat ->
                onAddAsset(kode, nama, kat, jml, sat, kond, lok, pj, thn, nil, cat)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun AssetCardItem(
    asset: AssetRwEntity,
    isAdminMode: Boolean,
    onBorrow: () -> Unit,
    onReturn: () -> Unit,
    onDelete: () -> Unit
) {
    val isAvailable = asset.statusKetersediaan == "Tersedia"
    val isBorrowed = asset.statusKetersediaan == "Dipinjam"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BorderLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Row 1: Kode & Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFF1F5F9)
                    ) {
                        Text(
                            text = asset.kodeAset,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF475569),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = asset.kategori,
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when (asset.statusKetersediaan) {
                        "Tersedia" -> AccentGreenLight
                        "Dipinjam" -> AccentOrangeLight
                        else -> Color(0xFFFEE2E2)
                    }
                ) {
                    Text(
                        text = "● ${asset.statusKetersediaan}",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (asset.statusKetersediaan) {
                            "Tersedia" -> AccentGreenDark
                            "Dipinjam" -> AccentOrangeDark
                            else -> AccentRedDark
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Row 2: Nama Aset & Jumlah
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = asset.namaAset,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = PrimaryBlueLight
                ) {
                    Text(
                        text = "${asset.jumlah} ${asset.satuan}",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Row 3: Detail Info (Lokasi, Kondisi, PJ)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF8FAFC))
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Lokasi: ${asset.lokasiPenyimpanan}", fontSize = 11.5.sp, color = TextPrimary)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "PJ: ${asset.penanggungJawab}", fontSize = 11.5.sp, color = TextPrimary)
                }
                if (asset.catatan.isNotBlank()) {
                    Text(text = "ℹ️ ${asset.catatan}", fontSize = 11.sp, color = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Row 4: Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isAvailable && asset.bisaDipinjam) {
                    Button(
                        onClick = onBorrow,
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AccentGreenDark)
                    ) {
                        Text("📋 Ajukan Pinjam Aset", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                } else if (isBorrowed) {
                    Button(
                        onClick = onReturn,
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AccentOrange)
                    ) {
                        Text("🔄 Kembalikan Aset", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                } else {
                    OutlinedButton(
                        onClick = {},
                        enabled = false,
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Sedang Pemeliharaan", fontSize = 11.5.sp)
                    }
                }

                if (isAdminMode) {
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Hapus", tint = AccentRed)
                    }
                }
            }
        }
    }
}

@Composable
fun AddAssetDialog(
    onDismiss: () -> Unit,
    onSubmit: (
        kode: String,
        nama: String,
        kategori: String,
        jumlah: Int,
        satuan: String,
        kondisi: String,
        lokasi: String,
        pj: String,
        tahun: String,
        nilai: Long,
        catatan: String
    ) -> Unit
) {
    var nama by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("Peralatan Acara") }
    var jumlahText by remember { mutableStateOf("1") }
    var satuan by remember { mutableStateOf("Unit") }
    var lokasi by remember { mutableStateOf("Gudang Balai RW 02") }
    var pj by remember { mutableStateOf("Seksi Perlengkapan (Pak Heru)") }
    var catatan by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(text = "Tambah Aset RW Baru", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)

                OutlinedTextField(
                    value = nama,
                    onValueChange = { nama = it },
                    label = { Text("Nama Aset / Fasilitas") },
                    placeholder = { Text("Contoh: Tenda Lipat 3x3m") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = jumlahText,
                        onValueChange = { jumlahText = it },
                        label = { Text("Jumlah") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = satuan,
                        onValueChange = { satuan = it },
                        label = { Text("Satuan") },
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = lokasi,
                    onValueChange = { lokasi = it },
                    label = { Text("Lokasi Penyimpanan") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = pj,
                    onValueChange = { pj = it },
                    label = { Text("Penanggung Jawab") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = catatan,
                    onValueChange = { catatan = it },
                    label = { Text("Catatan / Keterangan") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Batal")
                    }
                    Button(
                        onClick = {
                            if (nama.isNotBlank()) {
                                val jml = jumlahText.toIntOrNull() ?: 1
                                val kode = "AST-RW-${System.currentTimeMillis().toString().takeLast(3)}"
                                onSubmit(kode, nama, kategori, jml, satuan, "Baik", lokasi, pj, "2026", 0L, catatan)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentGreenDark),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Simpan", color = Color.White)
                    }
                }
            }
        }
    }
}
