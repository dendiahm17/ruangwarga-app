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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // ============================================================
        // 1. TOP BRAND HEADER MENEMPEL FULL SECARA HORIZONTAL (NATIVE APP BAR)
        // ============================================================
        val activeAlert = uiState.emergencyAlerts.firstOrNull { it.status == "Aktif" }
        com.example.ui.components.ElevatedTopHeader(
            cloudSyncStatus = uiState.cloudSyncStatus,
            unreadNotifications = uiState.unreadNotifications,
            isSirenActive = uiState.isEmergencySirenActive,
            hasActiveEmergency = activeAlert != null,
            onSyncClick = { viewModel.syncAllDataToCloud() },
            onInboxClick = { viewModel.openNotificationsSheet() },
            onEmergencyClick = {
                // Akses Cepat Langsung ke Layar Alarm SOS Mandiri
                viewModel.openAlarmScreen()
            },
            onSilenceSirenClick = { viewModel.silenceSirenSound() }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("home_screen_redesigned")
        ) {

        // ============================================================
        // BANNER LENGKAPI DATA KEPENDUDUKAN (Jika Warga Baru Belum Isi NIK/Nama)
        // ============================================================
        if (uiState.profile.nama.isBlank() || uiState.profile.nik.isBlank()) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFEFF6FF))
                        .clickable { viewModel.openPersonalDataSheet() }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFDBEAFE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "📋", fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Lengkapi Data Kependudukan Anda",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E40AF)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Isi NIK, No. KK & Alamat untuk mengaktifkan seluruh layanan RT/RW →",
                            fontSize = 11.sp,
                            color = Color(0xFF3B82F6)
                        )
                    }
                }
                HorizontalDivider(thickness = 0.8.dp, color = Color(0xFFE2E8F0))
            }
        }

        // ============================================================
        // 2. KEGIATAN TERDEKAT (Flat Seamless Section)
        // ============================================================
        if (nearestEvent != null) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
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
                        text = nearestEvent.judul,
                        fontSize = 15.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "📅", fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = nearestEvent.tanggal, fontSize = 11.5.sp, color = TextSecondary)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "⏱", fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = nearestEvent.waktu, fontSize = 11.5.sp, color = TextSecondary)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "📍", fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = nearestEvent.lokasi, fontSize = 11.5.sp, color = TextSecondary)
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .background(Color(0xFFF1F5F9))
                )
            }
        }

        // ============================================================
        // 4. UNTUK ANDA SECTION (Surat/Pengumuman Terbaru Flat)
        // ============================================================
        if (uiState.letters.isNotEmpty() || uiState.announcements.isNotEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
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

                    Spacer(modifier = Modifier.height(8.dp))

                    uiState.letters.firstOrNull()?.let { latestLetter ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFF8FAFC))
                                .clickable { viewModel.openSuratScreenSheet() }
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
                                    text = "${latestLetter.jenisSurat} - ${latestLetter.status}",
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = latestLetter.keperluan,
                                    fontSize = 10.5.sp,
                                    color = TextSecondary,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .background(Color(0xFFF1F5F9))
                )
            }
        }

        // ============================================================
        // 5. COMMUNITY POST FEED LIST (Flat Seamless Post Feed)
        // ============================================================
        if (uiState.customFeedPosts.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "📝", fontSize = 32.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Belum Ada Postingan Warga",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Gunakan tombol Buat di bawah untuk membuat kabar warga, usulan, atau kegiatan baru!",
                        fontSize = 11.5.sp,
                        color = TextSecondary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }

        // Render Custom Posts yang dibuat oleh warga/pengurus (Flat Seamless Feed Item)
        items(uiState.customFeedPosts) { customPost ->
            val customTemplate = findBannerTemplateById(customPost.bannerTemplateId)
                ?: getDefaultBannerTemplateForType(customPost.category)
            var postLiked by remember { mutableStateOf(customPost.isLiked) }
            var postLikesCount by remember { mutableIntStateOf(customPost.likesCount) }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.openPostDetail(customPost) }
                    .padding(horizontal = 16.dp, vertical = 14.dp)
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
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "✨", fontSize = 11.sp)
                            }
                            
                            // Nama Peran / Jabatan di Bawah Nama
                            if (customPost.authorRole.isNotBlank()) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(top = 1.dp, bottom = 2.dp)
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (customPost.authorRole.contains("Ketua", ignoreCase = true) || 
                                                    customPost.authorRole.contains("Sekretaris", ignoreCase = true) || 
                                                    customPost.authorRole.contains("Bendahara", ignoreCase = true) || 
                                                    customPost.authorRole.contains("Keamanan", ignoreCase = true)) 
                                                    Color(0xFFFEF3C7) else Color(0xFFF1F5F9)
                                    ) {
                                        Text(
                                            text = customPost.authorRole,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (customPost.authorRole.contains("Ketua", ignoreCase = true) || 
                                                        customPost.authorRole.contains("Sekretaris", ignoreCase = true) || 
                                                        customPost.authorRole.contains("Bendahara", ignoreCase = true) || 
                                                        customPost.authorRole.contains("Keamanan", ignoreCase = true)) 
                                                        Color(0xFFB45309) else Color(0xFF475569),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
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

            // Garis pembatas & space modern antar konten postingan
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(Color(0xFFF1F5F9))
            )
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
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
