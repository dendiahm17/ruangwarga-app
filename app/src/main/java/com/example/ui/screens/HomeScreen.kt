package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Poll
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CommunityEventEntity
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentGreenDark
import com.example.ui.theme.AccentGreenLight
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentOrangeDark
import com.example.ui.theme.AccentOrangeLight
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentRed
import com.example.ui.theme.AccentRedDark
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.PrimaryGreenDark
import com.example.ui.theme.PrimaryGreenLight
import com.example.ui.theme.PrimaryGreenUltraLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.ui.components.PostInteractiveBanner
import com.example.ui.components.findBannerTemplateById
import com.example.ui.components.getDefaultBannerTemplateForType
import com.example.ui.viewmodel.CommunityFeedPost
import com.example.ui.viewmodel.MainTab
import com.example.ui.viewmodel.RtrwUiState
import com.example.ui.viewmodel.RtrwViewModel

@Composable
fun HomeScreen(
    uiState: RtrwUiState,
    viewModel: RtrwViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val nearestEvent = uiState.communityEvents.firstOrNull()
    var isParticipating by remember { mutableStateOf(false) }
    var likesCount by remember { mutableIntStateOf(73) }
    var isLiked by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .testTag("home_screen_redesigned"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // ============================================================
        // 1. TOP BRAND HEADER: RuangWarga | RT 03 / RW 02 | Darurat & Akun
        // ============================================================
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo & Subtitle
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🌿", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "RuangWarga",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = PrimaryGreenDark
                        )
                    }
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(
                        text = "RT 03 / RW 02",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary
                    )
                }

                // Right Header Icons: Kotak Masuk (Mail Icon) & Darurat (Red Siren) - Tanpa Label
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Kotak Masuk (Inbox / Pesan Warga Icon Button)
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF1F5F9))
                            .clickable { viewModel.openNotificationsSheet() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Email,
                            contentDescription = "Kotak Masuk",
                            tint = TextPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        if (uiState.unreadNotifications > 0) {
                            Box(
                                modifier = Modifier
                                    .size(9.dp)
                                    .align(Alignment.TopEnd)
                                    .offset(x = (-4).dp, y = 4.dp)
                                    .clip(CircleShape)
                                    .background(AccentRed)
                                    .border(1.5.dp, Color.White, CircleShape)
                            )
                        }
                    }

                    // Darurat Siren Button
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFEE2E2))
                            .clickable { viewModel.openEmergencyAlarmSheet() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🚨", fontSize = 17.sp)
                    }
                }
            }
        }

        // ============================================================
        // 2. KARTU INFORMASI PESAN DARURAT WARGA (Tampil Ringkas: Judul & Pesan Pemicu Klik ke Detail)
        // ============================================================
        val activeAlert = uiState.emergencyAlerts.firstOrNull { it.status == "Aktif" }
        if (activeAlert != null) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(16.dp), spotColor = Color(0x30DC2626))
                        .clickable { viewModel.openEmergencyAlarmDetail(activeAlert) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.2.dp, Color(0xFFFCA5A5))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Siren Pulse Icon
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFEE2E2)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🚨", fontSize = 16.sp)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Judul & Pesan Singkat Pemicu Klik
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = activeAlert.judul.ifBlank { activeAlert.jenisDarurat },
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF991B1B),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = AccentRed
                                ) {
                                    Text(
                                        text = "SIAGA",
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = "⚠️ Ketuk di sini untuk info situasi & bantu relawan →",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFFB91C1C)
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        // Arrow Icon Indicator
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFF1F2)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Buka Detail",
                                tint = AccentRedDark,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                }
            }
        }

        // ============================================================
        // 3. KEGIATAN TERDEKAT CARD (Kerja Bakti RW 02 with Image & RSVP)
        // ============================================================
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(3.dp, RoundedCornerShape(16.dp), spotColor = Color(0x15000000)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderLight)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    // Badge: Kegiatan Terdekat
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFDCFCE7)
                    ) {
                        Text(
                            text = "Kegiatan Terdekat",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryGreenDark,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Kerja Bakti RW 02",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Event Details (Date, Time, Location)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "📅", fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Minggu, 30 Agustus 2026", fontSize = 11.5.sp, color = TextSecondary)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "⏱", fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "07.00 WIB", fontSize = 11.5.sp, color = TextSecondary)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "📍", fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Balai RW 02", fontSize = 11.5.sp, color = TextSecondary)
                            }
                        }

                        // Illustration Graphic Placeholder (Emerald Scene)
                        Box(
                            modifier = Modifier
                                .size(width = 95.dp, height = 75.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color(0xFF86EFAC), Color(0xFF15803D))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "🧹🌿", fontSize = 22.sp)
                                Text(text = "Gotong Royong", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Bottom Row: Avatar Stack + "73 warga ikut" + Button "Saya Ikut"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Row(horizontalArrangement = Arrangement.spacedBy((-6).dp)) {
                                listOf(PrimaryGreen, AccentOrange, AccentPurple).forEach { color ->
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clip(CircleShape)
                                            .background(color)
                                            .border(1.dp, Color.White, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(11.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "73 warga ikut",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextSecondary
                            )
                        }

                        Button(
                            onClick = {
                                isParticipating = !isParticipating
                                Toast.makeText(context, if (isParticipating) "Anda terdaftar ikut Kerja Bakti ✓" else "Partisipasi dibatalkan", Toast.LENGTH_SHORT).show()
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isParticipating) Color(0xFFDCFCE7) else PrimaryGreenDark,
                                contentColor = if (isParticipating) PrimaryGreenDark else Color.White
                            ),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text(
                                text = if (isParticipating) "✓ Terdaftar" else "Saya Ikut",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // ============================================================
        // 4. UNTUK ANDA SECTION (Rapat RT 03 & Surat Domisili)
        // ============================================================
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Untuk Anda",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Text(
                        text = "Lihat semua",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryGreenDark,
                        modifier = Modifier
                            .clickable { viewModel.selectTab(MainTab.AGENDA) }
                            .padding(vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Card 1: Rapat RT 03 malam ini (Red Dot)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.selectTab(MainTab.AGENDA) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFE0E7FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "📅", fontSize = 14.sp)
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Rapat RT 03 malam ini",
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "19.30 WIB • Balai RT 03",
                                fontSize = 10.5.sp,
                                color = TextSecondary
                            )
                        }

                        // Red Status Dot
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(AccentRed)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Card 2: Surat Anda sudah selesai (Green Dot)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.openSuratScreenSheet() },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFDCFCE7)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "📄", fontSize = 14.sp)
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Surat Anda sudah selesai",
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Surat Domisili",
                                fontSize = 10.5.sp,
                                color = TextSecondary
                            )
                        }

                        // Green Status Dot
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(PrimaryGreen)
                        )
                    }
                }
            }
        }

        // ============================================================
        // 5. COMMUNITY POST FEED LIST (Simulasi Beragam Postingan Warga)
        // ============================================================

        // Render Custom Posts yang baru dibuat oleh warga/pengurus dengan Template Banner
        items(uiState.customFeedPosts) { customPost ->
            val customTemplate = findBannerTemplateById(customPost.bannerTemplateId)
                ?: getDefaultBannerTemplateForType(customPost.category)
            var postLiked by remember { mutableStateOf(customPost.isLiked) }
            var postLikesCount by remember { mutableIntStateOf(customPost.likesCount) }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.openPostDetail(customPost) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderLight)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    // Author Header
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
                                    .background(PrimaryGreenDark),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = customPost.authorName,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "✨", fontSize = 11.sp)
                                }
                                Text(
                                    text = "${customPost.timeAgo} • ${customPost.category}",
                                    fontSize = 10.5.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        IconButton(onClick = {}, modifier = Modifier.size(24.dp)) {
                            Icon(imageVector = Icons.Default.MoreHoriz, contentDescription = null, tint = TextTertiary)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Interaktif Template Banner Postingan
                    PostInteractiveBanner(
                        template = customTemplate,
                        customTitle = customPost.title,
                        height = 105,
                        onClick = { viewModel.openPostDetail(customPost) }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = customPost.content,
                        fontSize = 12.5.sp,
                        color = TextPrimary,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Action Counters
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    postLiked = !postLiked
                                    postLikesCount += if (postLiked) 1 else -1
                                }
                                .padding(end = 18.dp)
                        ) {
                            Icon(
                                imageVector = if (postLiked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Like",
                                tint = if (postLiked) AccentRed else TextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "$postLikesCount", fontSize = 11.5.sp, color = TextSecondary)
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { viewModel.openPostDetail(customPost) }
                                .padding(end = 18.dp)
                        ) {
                            Icon(imageVector = Icons.Outlined.ChatBubbleOutline, contentDescription = "Comment", tint = TextSecondary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "${customPost.commentsCount}", fontSize = 11.5.sp, color = TextSecondary)
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { Toast.makeText(context, "Tautan postingan disalin", Toast.LENGTH_SHORT).show() }
                        ) {
                            Icon(imageVector = Icons.Default.Repeat, contentDescription = "Share", tint = TextSecondary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Bantu Sebarkan", fontSize = 10.5.sp, color = TextSecondary)
                        }
                    }
                }
            }
        }

        // Post 1: Dokumentasi Kerja Bakti oleh Ketua RW 02
        item {
            val tpl1 = findBannerTemplateById("tpl_gotong_royong") ?: getDefaultBannerTemplateForType("Kegiatan")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderLight)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    // Author Header
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
                                    .background(PrimaryGreenDark),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Pak Bambang (Ketua RW 02)",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "👑", fontSize = 11.sp)
                                }
                                Text(
                                    text = "2 jam lalu • Dokumentasi Kegiatan",
                                    fontSize = 10.5.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        IconButton(onClick = {}, modifier = Modifier.size(24.dp)) {
                            Icon(imageVector = Icons.Default.MoreHoriz, contentDescription = null, tint = TextTertiary)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Interaktif Template Banner
                    PostInteractiveBanner(
                        template = tpl1,
                        customTitle = "Laporan Selesai: Kerja Bakti Massal RW 02",
                        height = 100
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Kerja bakti pembersihan saluran air dan pemangkasan dahan pohon pagi ini telah selesai dengan lancar. Terima kasih banyak atas partisipasi 73 warga yang hadir guyub rukun! 🌿💚",
                        fontSize = 12.5.sp,
                        color = TextPrimary,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // 3 Photos Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("🧹 Saluran Air", "🌿 Potong Dahan", "🤝 Foto Bersama").forEach { tag ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(75.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFE2E8F0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "📸", fontSize = 16.sp)
                                    Text(text = tag, fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF334155))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Action Counters
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    isLiked = !isLiked
                                    likesCount += if (isLiked) 1 else -1
                                }
                                .padding(end = 18.dp)
                        ) {
                            Icon(
                                imageVector = if (isLiked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Like",
                                tint = if (isLiked) AccentRed else TextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "$likesCount", fontSize = 11.5.sp, color = TextSecondary)
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { Toast.makeText(context, "Membuka 18 komentar warga", Toast.LENGTH_SHORT).show() }
                                .padding(end = 18.dp)
                        ) {
                            Icon(imageVector = Icons.Outlined.ChatBubbleOutline, contentDescription = "Comment", tint = TextSecondary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "18", fontSize = 11.5.sp, color = TextSecondary)
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { Toast.makeText(context, "Tautan postingan disalin", Toast.LENGTH_SHORT).show() }
                        ) {
                            Icon(imageVector = Icons.Default.Repeat, contentDescription = "Share", tint = TextSecondary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "5", fontSize = 11.5.sp, color = TextSecondary)
                        }
                    }
                }
            }
        }

        // Post 2: Polling Pos Kamling Baru oleh Pak RT 03
        item {
            var selectedPollOpt by remember { mutableStateOf<String?>(null) }
            var pollVotesA by remember { mutableIntStateOf(24) }
            var pollVotesB by remember { mutableIntStateOf(9) }
            val tpl2 = findBannerTemplateById("tpl_polling_warga") ?: getDefaultBannerTemplateForType("Polling")

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderLight)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
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
                                    .background(PrimaryGreen),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🗳️", fontSize = 16.sp)
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(
                                    text = "Pak Joko (Ketua RT 03)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "4 jam lalu • Polling Musyawarah",
                                    fontSize = 10.5.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFEFF6FF)
                        ) {
                            Text(
                                text = "Poling Aktif",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBlue,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Banner Template Polling
                    PostInteractiveBanner(
                        template = tpl2,
                        customTitle = "Voting CCTV Pintar 360° Pos Kamling",
                        height = 100
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Menurut bapak/ibu sekalian, apakah perlu penambahan CCTV pintar 360° di gerbang utama Pos Kamling Blok C?",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Opsi Poling A
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (selectedPollOpt == "A") Color(0xFFDCFCE7) else Color(0xFFF8FAFC),
                        border = BorderStroke(1.dp, if (selectedPollOpt == "A") PrimaryGreenDark else BorderLight),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (selectedPollOpt != "A") {
                                    if (selectedPollOpt == "B") pollVotesB--
                                    selectedPollOpt = "A"
                                    pollVotesA++
                                    Toast.makeText(context, "Pilihan Anda tercatat: Sangat Setuju", Toast.LENGTH_SHORT).show()
                                }
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "A. Sangat Setuju (Tingkatkan Keamanan)",
                                fontSize = 12.sp,
                                fontWeight = if (selectedPollOpt == "A") FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedPollOpt == "A") PrimaryGreenDark else TextPrimary
                            )
                            Text(
                                text = "$pollVotesA Suara",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Opsi Poling B
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (selectedPollOpt == "B") Color(0xFFDCFCE7) else Color(0xFFF8FAFC),
                        border = BorderStroke(1.dp, if (selectedPollOpt == "B") PrimaryGreenDark else BorderLight),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (selectedPollOpt != "B") {
                                    if (selectedPollOpt == "A") pollVotesA--
                                    selectedPollOpt = "B"
                                    pollVotesB++
                                    Toast.makeText(context, "Pilihan Anda tercatat: Cukup Patroli Satpam", Toast.LENGTH_SHORT).show()
                                }
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "B. Cukup Patroli Ronda Fisik",
                                fontSize = 12.sp,
                                fontWeight = if (selectedPollOpt == "B") FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedPollOpt == "B") PrimaryGreenDark else TextPrimary
                            )
                            Text(
                                text = "$pollVotesB Suara",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Total ${pollVotesA + pollVotesB} warga telah berpartisipasi • Ditutup besok malam",
                        fontSize = 10.5.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        // Post 3: Pengumuman Posyandu Balita & Lansia
        item {
            val tpl3 = findBannerTemplateById("tpl_posyandu") ?: getDefaultBannerTemplateForType("Pengumuman")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderLight)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
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
                                    .background(Color(0xFFFCE7F3)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "👶", fontSize = 16.sp)
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(
                                    text = "Ibu Ratna (Kader Posyandu Melati)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Kemarin 16.20 • Pengumuman Kesehatan",
                                    fontSize = 10.5.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        IconButton(onClick = {}, modifier = Modifier.size(24.dp)) {
                            Icon(imageVector = Icons.Default.MoreHoriz, contentDescription = null, tint = TextTertiary)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Banner Template Posyandu
                    PostInteractiveBanner(
                        template = tpl3,
                        customTitle = "Posyandu Balita & Lansia Melati",
                        height = 100
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Jadwal Posyandu Balita & Lansia Rutin bulan ini akan diadakan pada hari Rabu, 2 September 2026 di Balai Warga RT 03 mulai pukul 08.30 WIB. Tersedia imunisasi gratis dan vitamin A.",
                        fontSize = 12.5.sp,
                        color = TextPrimary,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFFDF2F8),
                        border = BorderStroke(1.dp, Color(0xFFFBCFE8)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "🩺", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(text = "Pemeriksaan Tensi & Tumbuh Kembang", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF9D174D))
                                Text(text = "Gratis untuk seluruh warga RT 01 - RT 05", fontSize = 10.5.sp, color = Color(0xFFBE185D))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 18.dp)) {
                            Icon(imageVector = Icons.Default.Favorite, contentDescription = null, tint = AccentRed, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "42", fontSize = 11.5.sp, color = TextSecondary)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Outlined.ChatBubbleOutline, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "7 Komentar", fontSize = 11.5.sp, color = TextSecondary)
                        }
                    }
                }
            }
        }

        // Post 4: Ide Warga Bank Sampah & Daur Ulang
        item {
            var ideLiked by remember { mutableStateOf(false) }
            var ideLikes by remember { mutableIntStateOf(31) }
            val tpl4 = findBannerTemplateById("tpl_bank_sampah") ?: getDefaultBannerTemplateForType("Usulan")

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderLight)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
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
                                    .background(Color(0xFFFEF3C7)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "💡", fontSize = 16.sp)
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(
                                    text = "Rian Pratama (Warga Blok B2)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "2 hari lalu • Usulan Inovasi",
                                    fontSize = 10.5.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFFEF3C7)
                        ) {
                            Text(
                                text = "Ide Lingkungan",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB45309),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Banner Template Bank Sampah
                    PostInteractiveBanner(
                        template = tpl4,
                        customTitle = "Program Bank Sampah Mandiri RW 02",
                        height = 100
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Bagaimana jika kita membuat program 'Bank Sampah Digital RW 02'? Kardus dan botol plastik bekas bisa dikumpulkan setiap akhir pekan lalu hasil penjualannya masuk ke kas RT atau tabungan warga masing-masing.",
                        fontSize = 12.5.sp,
                        color = TextPrimary,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                ideLiked = !ideLiked
                                ideLikes += if (ideLiked) 1 else -1
                            }
                        ) {
                            Icon(
                                imageVector = if (ideLiked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = null,
                                tint = if (ideLiked) AccentRed else TextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "$ideLikes Dukungan Warga", fontSize = 11.5.sp, color = TextSecondary)
                        }

                        Text(
                            text = "Ditinjau Pengurus ✓",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryGreenDark
                        )
                    }
                }
            }
        }

        // Post 5: Laporan Siskamling Ronda Malam
        item {
            val tpl5 = findBannerTemplateById("tpl_siskamling_patroli") ?: getDefaultBannerTemplateForType("Kejadian")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderLight)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
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
                                    .background(Color(0xFFEDE9FE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🛡️", fontSize = 16.sp)
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(
                                    text = "Regu Ronda Malam (Pak Danu & Tim)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Tadi malam 03.15 • Laporan Keamanan",
                                    fontSize = 10.5.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFDCFCE7)
                        ) {
                            Text(
                                text = "Aman Terkendali",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF15803D),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Banner Template Siskamling
                    PostInteractiveBanner(
                        template = tpl5,
                        customTitle = "Patroli Ronda Malam Putaran ke-3 Selesai",
                        height = 100
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Patroli siskamling putaran ke-3 telah dilaksanakan di seluruh perimeter Blok A sampai Blok D. Portal timur telah terkunci rapat dan situasi lingkungan aman tenang. Terima kasih kopi & snack dari warga Blok C!",
                        fontSize = 12.5.sp,
                        color = TextPrimary,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 18.dp)) {
                            Icon(imageVector = Icons.Default.Favorite, contentDescription = null, tint = AccentRed, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "56", fontSize = 11.5.sp, color = TextSecondary)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Outlined.ChatBubbleOutline, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "12 Komentar", fontSize = 11.5.sp, color = TextSecondary)
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun QuickPostShortcut(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, BorderLight),
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryGreenDark,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )
        }
    }
}
