package com.example.ui.dialogs

import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.fillMaxSize
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentGreenDark
import com.example.ui.theme.AccentGreenLight
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentOrangeDark
import com.example.ui.theme.AccentOrangeLight
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentPurpleDark
import com.example.ui.theme.AccentPurpleLight
import com.example.ui.theme.AccentRed
import com.example.ui.theme.AccentRedDark
import com.example.ui.theme.AccentRedLight
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryBlueDark
import com.example.ui.theme.PrimaryBlueLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.ui.components.PostInteractiveBanner
import com.example.ui.components.findBannerTemplateById
import com.example.ui.components.getDefaultBannerTemplateForType
import com.example.ui.viewmodel.CommunityFeedPost
import com.example.ui.viewmodel.CommunityPostComment
import com.example.ui.viewmodel.RtrwViewModel
/**
 * PostDetailBottomSheet - Mockup Layar 4 (Kegiatan), Layar 5 (Komentar), Layar 7 (Polling), & Layar 8 (Kejadian)
 * Menampilkan detail lengkap postingan sesuai tipe:
 * 1. Kegiatan: Tag (KEGIATAN), badge Akan Datang, progress bar 73%, tombol Saya Ikut + Saya Bisa Membantu
 * 2. Polling: Tag (POLLING), badge Berakhir 2 hari lagi, voting progress bar dengan persentase & jumlah pemilih
 * 3. Kejadian: Tag (KEJADIAN), badge Dalam Penanganan, status stepper (Dilaporkan -> Diverifikasi -> Ditangani -> Selesai)
 * 4. Komentar: List komentar bertingkat dengan jumlah apresiasi ❤️, tombol Balas, dan input box
 */
@Composable
fun PostDetailBottomSheet(
    post: CommunityFeedPost,
    viewModel: RtrwViewModel,
    isAdminMode: Boolean,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var isParticipating by remember { mutableStateOf(post.isParticipating) }
    var likesCount by remember { mutableStateOf(post.likesCount) }
    var isLiked by remember { mutableStateOf(post.isLiked) }
    var commentText by remember { mutableStateOf("") }
    var isSaved by remember { mutableStateOf(false) }
    var isFollowingIncident by remember { mutableStateOf(false) }
    // Polling State (Mockup 7)
    var selectedPollOption by remember { mutableStateOf<Int?>(null) }
    var hasVoted by remember { mutableStateOf(false) }
    val comments = remember {
        mutableStateListOf<CommunityPostComment>().apply {
            if (post.comments.isNotEmpty()) {
                addAll(post.comments)
            } else {
                addAll(
                    listOf(
                        CommunityPostComment("c1", "Budi Santoso", "Warga RT 03", "Saya bisa membawa mesin potong rumput.", "1 jam lalu", isContribution = true, contributionItem = "Mesin Potong Rumput"),
                        CommunityPostComment("c2", "Siti Rahma", "Warga RT 01", "Saya bisa membantu konsumsi.", "50 menit lalu", isContribution = true, contributionItem = "Konsumsi"),
                        CommunityPostComment("c3", "Andi Wijaya", "Warga RT 02", "Siap ikut, mohon infonya untuk titik kumpul.", "30 menit lalu")
                    )
                )
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f, fill = false)
                .fillMaxWidth()
        ) {
            // ============================================================
            // 1. TOP BAR (TAG KATEGORI + BADGE STATUS + ACTIONS)
            // ============================================================
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = TextPrimary)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        // Category Pill Tag
                        val categoryUpper = post.category.uppercase()
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = when (post.category) {
                                "Kegiatan", "Undangan", "Ajakan" -> AccentGreenLight
                                "Polling" -> AccentPurpleLight
                                "Kejadian" -> AccentRedLight
                                "Pengumuman", "Himbauan" -> PrimaryBlueLight
                                else -> AccentGreenLight
                            }
                        ) {
                            Text(
                                text = categoryUpper,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (post.category) {
                                    "Kegiatan", "Undangan", "Ajakan" -> AccentGreenDark
                                    "Polling" -> AccentPurpleDark
                                    "Kejadian" -> AccentRedDark
                                    "Pengumuman", "Himbauan" -> PrimaryBlueDark
                                    else -> AccentGreenDark
                                },
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Status Badge
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = when (post.category) {
                                "Kegiatan", "Undangan", "Ajakan" -> Color(0xFFDCFCE7)
                                "Polling" -> Color(0xFFE0E7FF)
                                "Kejadian" -> Color(0xFFFEE2E2)
                                else -> BackgroundLight
                            },
                            border = BorderStroke(
                                1.dp,
                                when (post.category) {
                                    "Kegiatan", "Undangan", "Ajakan" -> AccentGreen
                                    "Polling" -> PrimaryBlue
                                    "Kejadian" -> AccentRed
                                    else -> BorderLight
                                }
                            )
                        ) {
                            Text(
                                text = when (post.category) {
                                    "Kegiatan", "Undangan", "Ajakan" -> "Akan Datang"
                                    "Polling" -> "Berakhir 2 hari lagi"
                                    "Kejadian" -> "Dalam Penanganan"
                                    else -> "Aktif"
                                },
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (post.category) {
                                    "Kegiatan", "Undangan", "Ajakan" -> AccentGreenDark
                                    "Polling" -> PrimaryBlueDark
                                    "Kejadian" -> AccentRedDark
                                    else -> TextPrimary
                                },
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        IconButton(
                            onClick = {
                                isSaved = !isSaved
                                Toast.makeText(context, if (isSaved) "Tersimpan ke favorit ✓" else "Dihapus dari simpanan", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = if (isSaved) Icons.Filled.BookmarkBorder else Icons.Outlined.BookmarkBorder,
                                contentDescription = "Simpan",
                                tint = if (isSaved) AccentGreenDark else TextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        IconButton(onClick = { /* Share or Options */ }, modifier = Modifier.size(32.dp)) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Lainnya", tint = TextSecondary, modifier = Modifier.size(18.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // ============================================================
            // 2. BANNER TEMPLATE, JUDUL & AUTHOR
            // ============================================================
            item {
                val bannerTemplate = findBannerTemplateById(post.bannerTemplateId) 
                    ?: getDefaultBannerTemplateForType(post.category)

                PostInteractiveBanner(
                    template = bannerTemplate,
                    customTitle = post.title,
                    height = 120
                )
                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = post.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE2E8F0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(14.dp))
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Oleh ${post.authorName} (${post.authorRtRw}) • ${post.timeAgo}",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = post.content,
                    fontSize = 13.sp,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(14.dp))
            }
            // ============================================================
            // 3. SPESIFIK TIPE: LAYAR 4 (KEGIATAN)
            // ============================================================
            if (post.category in listOf("Kegiatan", "Undangan", "Ajakan")) {
                item {
                    // Date / Time / Location Box
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = BackgroundLight,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null, tint = AccentGreenDark, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = post.eventDate ?: "Minggu, 30 Agustus 2026", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Schedule, contentDescription = null, tint = TextTertiary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = post.eventTime ?: "07.00 WIB", fontSize = 12.sp, color = TextSecondary)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = TextTertiary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = post.eventLocation ?: "Balai RW 02 — Blok A – C", fontSize = 12.sp, color = TextSecondary)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    // Progress Target Warga
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "73 warga sudah ikut",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Target 100 warga (73%)",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { 0.73f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = AccentGreen,
                        trackColor = Color(0xFFE2E8F0)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // Aksi Kegiatan: Saya Ikut + Saya Bisa Membantu
                    Button(
                        onClick = {
                            isParticipating = !isParticipating
                            Toast.makeText(context, if (isParticipating) "✓ Anda terdaftar hadir!" else "Pendaftaran dibatalkan", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isParticipating) AccentGreenLight else AccentGreen,
                            contentColor = if (isParticipating) AccentGreenDark else Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                    ) {
                        if (isParticipating) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Anda Sudah Terdaftar Ikut", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        } else {
                            Text("Saya Ikut", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = {
                            Toast.makeText(context, "Silakan tulis bantuan yang dapat Anda bawa pada kolom komentar", Toast.LENGTH_LONG).show()
                        },
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, AccentGreen),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentGreenDark),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                    ) {
                        Text("Saya Bisa Membantu", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            // ============================================================
            // 4. SPESIFIK TIPE: LAYAR 7 (POLLING)
            // ============================================================
            if (post.category == "Polling") {
                item {
                    val pollOptions = listOf(
                        Triple("Sabtu pagi", 45, 32),
                        Triple("Sabtu sore", 26, 18),
                        Triple("Minggu pagi", 20, 14),
                        Triple("Minggu sore", 9, 6)
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        pollOptions.forEachIndexed { index, (optTitle, percentage, voters) ->
                            val isChosen = selectedPollOption == index
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedPollOption = index
                                        hasVoted = true
                                        Toast.makeText(context, "Pilihan Anda: $optTitle", Toast.LENGTH_SHORT).show()
                                    },
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                border = BorderStroke(1.dp, if (isChosen) AccentGreen else BorderLight)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = optTitle, fontSize = 12.sp, fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Medium, color = TextPrimary)
                                        Text(text = "$percentage% ($voters)", fontSize = 11.sp, color = TextSecondary, fontWeight = FontWeight.SemiBold)
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    LinearProgressIndicator(
                                        progress = { percentage / 100f },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(6.dp)
                                            .clip(RoundedCornerShape(3.dp)),
                                        color = if (index == 0 || isChosen) AccentGreen else Color(0xFFCBD5E1),
                                        trackColor = Color(0xFFF1F5F9)
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "70 warga sudah memilih", fontSize = 11.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            if (selectedPollOption == null) {
                                Toast.makeText(context, "Pilih salah satu opsi polling terlebih dahulu", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Suara Anda berhasil disimpan! ✓", Toast.LENGTH_SHORT).show()
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                    ) {
                        Text(if (hasVoted) "Pilihan Tersimpan ✓" else "Pilih", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            // ============================================================
            // 5. SPESIFIK TIPE: LAYAR 8 (KEJADIAN)
            // ============================================================
            if (post.category == "Kejadian") {
                item {
                    // Location Info
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = TextTertiary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Blok C — Depan No. 12", fontSize = 12.sp, color = TextSecondary)
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    // Status Penanganan Stepper
                    Text(text = "Status Penanganan", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Spacer(modifier = Modifier.height(10.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        // 1. Dilaporkan (Selesai - Hijau)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(AccentGreen),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(text = "Dilaporkan", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Text(text = "1 jam lalu", fontSize = 10.sp, color = TextTertiary)
                            }
                        }
                        // 2. Diverifikasi (Aktif - Biru)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color.White))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(text = "Diverifikasi", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
                                Text(text = "Dalam proses", fontSize = 10.sp, color = TextTertiary)
                            }
                        }
                        // 3. Ditangani (Pending - Abu)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE2E8F0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF94A3B8)))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(text = "Ditangani", fontSize = 11.sp, fontWeight = FontWeight.Normal, color = TextSecondary)
                                Text(text = "Dalam proses", fontSize = 10.sp, color = TextTertiary)
                            }
                        }
                        // 4. Selesai (Pending - Abu)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE2E8F0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF94A3B8)))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(text = "Selesai", fontSize = 11.sp, fontWeight = FontWeight.Normal, color = TextSecondary)
                                Text(text = "Belum selesai", fontSize = 10.sp, color = TextTertiary)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                    Button(
                        onClick = {
                            isFollowingIncident = !isFollowingIncident
                            Toast.makeText(context, if (isFollowingIncident) "Anda sekarang mengikuti pembaruan status laporan ini ✓" else "Berhenti mengikuti", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isFollowingIncident) AccentGreenLight else AccentGreen,
                            contentColor = if (isFollowingIncident) AccentGreenDark else Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                    ) {
                        Text(if (isFollowingIncident) "✓ Mengikuti Perkembangan" else "Ikuti Perkembangan", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            // ============================================================
            // 6. INTERAKSI FOOTER BAR (❤️ Apresiasi, 💬 Komentar, ↗ Bagikan, 🔖 Simpan)
            // ============================================================
            item {
                HorizontalDivider(color = BorderLight, thickness = 0.8.dp)
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Like / Apresiasi
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable {
                                isLiked = !isLiked
                                likesCount += if (isLiked) 1 else -1
                            }
                            .padding(4.dp)
                    ) {
                        Icon(
                            imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint = if (isLiked) AccentRed else TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "$likesCount Apresiasi", fontSize = 11.sp, color = if (isLiked) AccentRed else TextSecondary)
                    }
                    // Komentar Counter
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(4.dp)) {
                        Text(text = "${comments.size} Komentar", fontSize = 11.sp, color = TextSecondary)
                    }
                    // Bagikan
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable {
                                Toast.makeText(context, "Tautan berhasil disalin!", Toast.LENGTH_SHORT).show()
                            }
                            .padding(4.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Bagikan", fontSize = 11.sp, color = TextSecondary)
                    }
                    // Simpan
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable {
                                isSaved = !isSaved
                                Toast.makeText(context, if (isSaved) "Tersimpan ✓" else "Dihapus", Toast.LENGTH_SHORT).show()
                            }
                            .padding(4.dp)
                    ) {
                        Icon(
                            imageVector = if (isSaved) Icons.Filled.BookmarkBorder else Icons.Outlined.BookmarkBorder,
                            contentDescription = null,
                            tint = if (isSaved) AccentGreenDark else TextSecondary,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Simpan", fontSize = 11.sp, color = if (isSaved) AccentGreenDark else TextSecondary)
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }
            // ============================================================
            // 7. LAYAR 5: DAFTAR KOMENTAR BERTINGKAT
            // ============================================================
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Komentar (${comments.size})",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Terbaru ⌄",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
            items(comments) { c ->
                var commentLikes by remember { mutableStateOf(if (c.id == "c1") 6 else if (c.id == "c2") 4 else 2) }
                var isCommentLiked by remember { mutableStateOf(false) }
                Column(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                    Row(verticalAlignment = Alignment.Top) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE2E8F0)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = c.authorName.take(1), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = c.authorName, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = c.timestamp, fontSize = 10.sp, color = TextTertiary)
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(text = c.content, fontSize = 11.5.sp, color = TextSecondary, lineHeight = 15.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "▷ Balas",
                                    fontSize = 10.sp,
                                    color = PrimaryBlue,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.clickable {
                                        commentText = "@${c.authorName} "
                                    }
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable {
                                        isCommentLiked = !isCommentLiked
                                        commentLikes += if (isCommentLiked) 1 else -1
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (isCommentLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = null,
                                        tint = if (isCommentLiked) AccentRed else TextTertiary,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(text = "$commentLikes", fontSize = 10.sp, color = if (isCommentLiked) AccentRed else TextTertiary)
                                }
                            }
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
        // ============================================================
        // 8. INPUT BOX KOMENTAR (BAWAH)
        // ============================================================
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = BackgroundLight,
            border = BorderStroke(1.dp, BorderLight),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    placeholder = { Text("Tulis komentar...", fontSize = 12.sp, color = TextTertiary) },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )
                IconButton(
                    onClick = {
                        if (commentText.isNotBlank()) {
                            comments.add(
                                CommunityPostComment(
                                    id = "c_${System.currentTimeMillis()}",
                                    authorName = "Budi Santoso",
                                    authorRole = "Warga RT 03",
                                    content = commentText.trim(),
                                    timestamp = "Baru saja"
                                )
                            )
                            commentText = ""
                            Toast.makeText(context, "Komentar terkirim", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Kirim", tint = AccentGreenDark)
                }
            }
        }
    }
}
