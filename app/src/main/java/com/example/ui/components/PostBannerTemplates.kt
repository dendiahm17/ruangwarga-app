package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationImportant
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VolunteerActivism
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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

/**
 * Model data Template Banner visual untuk postingan warga.
 */
data class PostBannerTemplate(
    val id: String,
    val categoryId: String, // "Kegiatan", "Pengumuman", "Sosial", "Usulan", "Polling", "Kejadian", "Dokumentasi", "Umum"
    val templateName: String,
    val emoji: String,
    val badgeLabel: String,
    val subtext: String,
    val gradientColors: List<Color>,
    val textColor: Color = Color.White,
    val badgeBgColor: Color = Color.White.copy(alpha = 0.25f),
    val icon: ImageVector = Icons.Default.AutoAwesome
)

/**
 * Katalog template latar/banner yang interaktif dan informatif berdasarkan jenis postingan.
 */
val POST_BANNER_TEMPLATES = listOf(
    // 1. Kategori Kegiatan & Undangan
    PostBannerTemplate(
        id = "tpl_gotong_royong",
        categoryId = "Kegiatan",
        templateName = "Gotong Royong & Kerja Bakti",
        emoji = "🧹🌿",
        badgeLabel = "KERJA BAKTI RW",
        subtext = "Guyub Rukun Lingkungan Asri & Bersih",
        gradientColors = listOf(Color(0xFF10B981), Color(0xFF047857)),
        icon = Icons.Default.VolunteerActivism
    ),
    PostBannerTemplate(
        id = "tpl_senam_sehat",
        categoryId = "Kegiatan",
        templateName = "Senam & Olahraga Bersama",
        emoji = "🏃‍♂️🧘‍♀️",
        badgeLabel = "SEHAT BERSAMA",
        subtext = "Jaga Kebugaran Bersama Warga RT/RW",
        gradientColors = listOf(Color(0xFF06B6D4), Color(0xFF0E7490)),
        icon = Icons.Default.Event
    ),
    PostBannerTemplate(
        id = "tpl_rapat_warga",
        categoryId = "Kegiatan",
        templateName = "Musyawarah & Rapat Rutin",
        emoji = "📋🤝",
        badgeLabel = "MUSYAWARAH WARGA",
        subtext = "Mufakat Bersama Membangun Lingkungan",
        gradientColors = listOf(Color(0xFF3B82F6), Color(0xFF1D4ED8)),
        icon = Icons.Default.Event
    ),

    // 2. Kategori Pengumuman & Himbauan
    PostBannerTemplate(
        id = "tpl_pengumuman_resmi",
        categoryId = "Pengumuman",
        templateName = "Pengumuman Resmi RW/RT",
        emoji = "📢📜",
        badgeLabel = "PENGUMUMAN RESMI",
        subtext = "Wajib Diketahui Seluruh Warga",
        gradientColors = listOf(Color(0xFFEA580C), Color(0xFFC2410C)),
        icon = Icons.Default.Campaign
    ),
    PostBannerTemplate(
        id = "tpl_posyandu",
        categoryId = "Pengumuman",
        templateName = "Posyandu Balita & Lansia",
        emoji = "👶🩺",
        badgeLabel = "POSYANDU MELATI",
        subtext = "Pemeriksaan Kesehatan & Gizi Rutin",
        gradientColors = listOf(Color(0xFFEC4899), Color(0xFFBE185D)),
        icon = Icons.Default.LocalHospital
    ),
    PostBannerTemplate(
        id = "tpl_ketertiban",
        categoryId = "Pengumuman",
        templateName = "Himbauan Ketertiban & Jam Malam",
        emoji = "⚠️🌙",
        badgeLabel = "TATA TERTIB",
        subtext = "Menjaga Ketenangan & Keamanan Bersama",
        gradientColors = listOf(Color(0xFFF59E0B), Color(0xFFD97706)),
        icon = Icons.Default.NotificationImportant
    ),

    // 3. Kategori Bantuan Sosial & Kepedulian
    PostBannerTemplate(
        id = "tpl_bansos",
        categoryId = "Bantuan",
        templateName = "Peduli Sesama & Donasi",
        emoji = "❤️🤲",
        badgeLabel = "RUKUN KASIH",
        subtext = "Uluran Tangan untuk Warga Membutuhkan",
        gradientColors = listOf(Color(0xFFEF4444), Color(0xFFB91C1C)),
        icon = Icons.Default.Handshake
    ),
    PostBannerTemplate(
        id = "tpl_duka_cita",
        categoryId = "Bantuan",
        templateName = "Duka Cita & Takziah",
        emoji = "🕊️🕯️",
        badgeLabel = "BELASUNGKAWA",
        subtext = "Turut Berduka atas Kepulangan Warga",
        gradientColors = listOf(Color(0xFF64748B), Color(0xFF334155)),
        icon = Icons.Default.Favorite
    ),

    // 4. Kategori Usulan & Inovasi Warga
    PostBannerTemplate(
        id = "tpl_aspirasi_inovasi",
        categoryId = "Usulan",
        templateName = "Aspirasi & Ide Inovasi",
        emoji = "💡🚀",
        badgeLabel = "IDE INOVASI",
        subtext = "Gagasan Cerdas Memajukan Lingkungan",
        gradientColors = listOf(Color(0xFFF59E0B), Color(0xFFB45309)),
        icon = Icons.Default.Lightbulb
    ),
    PostBannerTemplate(
        id = "tpl_bank_sampah",
        categoryId = "Usulan",
        templateName = "Bank Sampah & Lingkungan Hijau",
        emoji = "♻️🌱",
        badgeLabel = "RUANG HIJAU",
        subtext = "Kelola Sampah Jadi Berkah & Bernilai",
        gradientColors = listOf(Color(0xFF10B981), Color(0xFF065F46)),
        icon = Icons.Default.Lightbulb
    ),

    // 5. Kategori Polling & Suara Warga
    PostBannerTemplate(
        id = "tpl_polling_warga",
        categoryId = "Polling",
        templateName = "Polling Aspirasi Warga",
        emoji = "🗳️📊",
        badgeLabel = "SUARA WARGA",
        subtext = "Tentukan Keputusan Lewat Suara Anda",
        gradientColors = listOf(Color(0xFF8B5CF6), Color(0xFF6D28D9)),
        icon = Icons.Default.HowToVote
    ),

    // 6. Kategori Kejadian & Laporan Lapangan
    PostBannerTemplate(
        id = "tpl_siskamling_patroli",
        categoryId = "Kejadian",
        templateName = "Laporan Siskamling & Ronda",
        emoji = "🛡️🔦",
        badgeLabel = "SISKAMLING 24/7",
        subtext = "Patroli Keamanan Lingkungan RT/RW",
        gradientColors = listOf(Color(0xFF4F46E5), Color(0xFF3730A3)),
        icon = Icons.Default.Security
    ),
    PostBannerTemplate(
        id = "tpl_waspada_fasum",
        categoryId = "Kejadian",
        templateName = "Peringatan Fasum / Kerusakan",
        emoji = "🚧⚡",
        badgeLabel = "PANTAU FASUM",
        subtext = "Penerangan Jalan & Saluran Air",
        gradientColors = listOf(Color(0xFFDC2626), Color(0xFF991B1B)),
        icon = Icons.Default.ReportProblem
    ),

    // 7. Kategori Dokumentasi
    PostBannerTemplate(
        id = "tpl_dokumentasi_kegiatan",
        categoryId = "Dokumentasi",
        templateName = "Dokumentasi & Galeri RW",
        emoji = "📸✨",
        badgeLabel = "GALERI WARGA",
        subtext = "Momen Kebersamaan Warga RuangWarga",
        gradientColors = listOf(Color(0xFF0284C7), Color(0xFF0369A1)),
        icon = Icons.Default.PhotoCamera
    )
)

/**
 * Mencari template default sesuai tipe postingan.
 */
fun getDefaultBannerTemplateForType(type: String): PostBannerTemplate {
    val normalized = when (type.lowercase()) {
        "kegiatan", "undangan", "ajakan" -> "Kegiatan"
        "pengumuman", "himbauan" -> "Pengumuman"
        "bantuan", "sosial" -> "Bantuan"
        "usulan", "aspirasi", "ide" -> "Usulan"
        "polling" -> "Polling"
        "kejadian", "lapor", "lapor kejadian" -> "Kejadian"
        "dokumentasi", "galeri" -> "Dokumentasi"
        else -> "Kegiatan"
    }
    return POST_BANNER_TEMPLATES.firstOrNull { it.categoryId == normalized }
        ?: POST_BANNER_TEMPLATES.first()
}

/**
 * Mencari template berdasarkan ID.
 */
fun findBannerTemplateById(templateId: String?): PostBannerTemplate? {
    if (templateId.isNullOrBlank()) return null
    return POST_BANNER_TEMPLATES.firstOrNull { it.id == templateId }
}

/**
 * Komponen Banner Postingan Interaktif & Informatif.
 * Digunakan pada kartu postingan di Beranda dan juga pada halaman Detail Postingan.
 */
@Composable
fun PostInteractiveBanner(
    template: PostBannerTemplate,
    customTitle: String? = null,
    modifier: Modifier = Modifier,
    height: Int = 115,
    showBadges: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Brush.linearGradient(template.gradientColors))
            .then(
                if (onClick != null) Modifier.clickable { onClick() } else Modifier
            )
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        // Dekorasi Latar Belakang Geometris Transparan
        Box(
            modifier = Modifier
                .size(90.dp)
                .align(Alignment.TopEnd)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.08f))
        )
        Box(
            modifier = Modifier
                .size(55.dp)
                .align(Alignment.BottomEnd)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.06f))
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Kolom Teks Informasi Banner
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                if (showBadges) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = template.badgeBgColor
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = template.icon,
                                contentDescription = null,
                                tint = template.textColor,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = template.badgeLabel,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = template.textColor,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }

                Text(
                    text = customTitle?.ifBlank { template.templateName } ?: template.templateName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = template.textColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = template.subtext,
                    fontSize = 10.5.sp,
                    color = template.textColor.copy(alpha = 0.9f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Ilustrasi / Emoji Ikon Besar
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = template.emoji,
                    fontSize = 26.sp
                )
            }
        }
    }
}

/**
 * Komponen Pemilih Template Banner Interaktif untuk digunakan di Form Pembuatan Postingan.
 */
@Composable
fun PostBannerTemplateSelector(
    postType: String,
    selectedTemplate: PostBannerTemplate?,
    onSelectTemplate: (PostBannerTemplate) -> Unit,
    modifier: Modifier = Modifier
) {
    val normalizedType = when (postType.lowercase()) {
        "kegiatan", "undangan", "ajakan" -> "Kegiatan"
        "pengumuman", "himbauan" -> "Pengumuman"
        "bantuan", "sosial" -> "Bantuan"
        "usulan", "aspirasi", "ide" -> "Usulan"
        "polling" -> "Polling"
        "kejadian", "lapor", "lapor kejadian" -> "Kejadian"
        "dokumentasi", "galeri" -> "Dokumentasi"
        else -> "Kegiatan"
    }

    // Tampilkan template yang sesuai kategori terlebih dahulu
    val categoryTemplates = POST_BANNER_TEMPLATES.filter { it.categoryId.equals(normalizedType, ignoreCase = true) }
    val displayTemplates = if (categoryTemplates.isNotEmpty()) categoryTemplates else POST_BANNER_TEMPLATES

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Pilih Template Banner / Latar:",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFFDCFCE7)
                ) {
                    Text(
                        text = "Interaktif",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentGreenDark,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Horizontal Carousel Kartu Template Banner
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(displayTemplates, key = { it.id }) { tpl ->
                val isSelected = selectedTemplate?.id == tpl.id
                Box(
                    modifier = Modifier
                        .width(135.dp)
                        .height(80.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(
                            width = if (isSelected) 2.5.dp else 1.dp,
                            color = if (isSelected) AccentGreenDark else Color.Transparent,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .background(Brush.linearGradient(tpl.gradientColors))
                        .clickable { onSelectTemplate(tpl) }
                        .padding(8.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = tpl.emoji,
                                    fontSize = 16.sp
                                )
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(CircleShape)
                                            .background(Color.White),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = AccentGreenDark,
                                            modifier = Modifier.size(12.dp)
                                        )
                                    }
                                }
                            }

                            Column {
                                Text(
                                    text = tpl.templateName,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = tpl.badgeLabel,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White.copy(alpha = 0.85f),
                                    maxLines = 1
                                )
                            }
                        }
                }
            }
        }
    }
}
