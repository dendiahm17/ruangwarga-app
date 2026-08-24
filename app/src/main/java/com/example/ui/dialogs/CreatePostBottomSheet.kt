package com.example.ui.dialogs

import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.NotificationImportant
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.ui.viewmodel.CommunityFeedPost
import com.example.ui.components.PostBannerTemplate
import com.example.ui.components.PostBannerTemplateSelector
import com.example.ui.components.PostInteractiveBanner
import com.example.ui.components.getDefaultBannerTemplateForType

data class PostCategoryOption(
    val id: String, // "Undangan", "Ajakan", "Himbauan", "Polling", "Aspirasi", "Sosial", "Kejadian"
    val label: String,
    val description: String,
    val icon: ImageVector,
    val tintColor: Color,
    val bgColor: Color,
    val ctaLabel: String,
    val ctaIcon: ImageVector
)
val POST_CATEGORY_OPTIONS = listOf(
    PostCategoryOption(
        id = "Undangan",
        label = "Undangan",
        description = "Rapat, tasyakuran, musyawarah warga",
        icon = Icons.Default.Event,
        tintColor = PrimaryBlueDark,
        bgColor = PrimaryBlueLight,
        ctaLabel = "KONFIRMASI HADIR",
        ctaIcon = Icons.Default.Event
    ),
    PostCategoryOption(
        id = "Ajakan",
        label = "Ajakan",
        description = "Kerja bakti, senam sehat, gowes bersama",
        icon = Icons.Default.VolunteerActivism,
        tintColor = AccentGreenDark,
        bgColor = AccentGreenLight,
        ctaLabel = "SAYA IKUT BERGABUNG",
        ctaIcon = Icons.Default.VolunteerActivism
    ),
    PostCategoryOption(
        id = "Himbauan",
        label = "Himbauan",
        description = "Ketertiban, jam malam, buang sampah",
        icon = Icons.Default.NotificationImportant,
        tintColor = AccentOrangeDark,
        bgColor = AccentOrangeLight,
        ctaLabel = "SAYA SUDAH PAHAM & DUKUNG",
        ctaIcon = Icons.Default.NotificationImportant
    ),
    PostCategoryOption(
        id = "Polling",
        label = "Polling",
        description = "Voting suara keputusan & rencana bersama",
        icon = Icons.Default.HowToVote,
        tintColor = AccentPurpleDark,
        bgColor = AccentPurpleLight,
        ctaLabel = "IKUTI VOTING SUARA",
        ctaIcon = Icons.Default.HowToVote
    ),
    PostCategoryOption(
        id = "Aspirasi",
        label = "Aspirasi",
        description = "Usulan perbaikan, ide inovasi warga",
        icon = Icons.Default.Lightbulb,
        tintColor = Color(0xFFD97706),
        bgColor = Color(0xFFFEF3C7),
        ctaLabel = "DUKUNG IDE / BERI MASUKAN",
        ctaIcon = Icons.Default.Lightbulb
    ),
    PostCategoryOption(
        id = "Sosial",
        label = "Bantuan Sosial",
        description = "Warga sakit, duka cita, butuh relawan",
        icon = Icons.Default.Handshake,
        tintColor = AccentRedDark,
        bgColor = AccentRedLight,
        ctaLabel = "SAYA BISA MEMBANTU",
        ctaIcon = Icons.Default.Handshake
    ),
    PostCategoryOption(
        id = "Kejadian",
        label = "Kejadian",
        description = "Lampu jalan mati, jalan rusak, darurat",
        icon = Icons.Default.ReportProblem,
        tintColor = Color(0xFFB91C1C),
        bgColor = Color(0xFFFEE2E2),
        ctaLabel = "PANTAU & BANTU ATASI",
        ctaIcon = Icons.Default.ReportProblem
    )
)
/**
 * CreatePostBottomSheet - Dialog pembuat postingan cerdas
 * Memungkinkan pengurus/warga memilih kategori khusus (Undangan, Ajakan, Himbauan, Polling, Aspirasi, dll.)
 * serta memilih Template Latar Banner Interaktif yang informatif.
 */
@Composable
fun CreatePostBottomSheet(
    onDismiss: () -> Unit,
    onSubmit: (CommunityFeedPost) -> Unit
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf(POST_CATEGORY_OPTIONS[0]) }
    var selectedTemplate by remember { mutableStateOf<PostBannerTemplate?>(getDefaultBannerTemplateForType(POST_CATEGORY_OPTIONS[0].label)) }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var eventDate by remember { mutableStateOf("") }
    var eventTime by remember { mutableStateOf("") }
    var eventLocation by remember { mutableStateOf("") }
    var pollOptionA by remember { mutableStateOf("Setuju") }
    var pollOptionB by remember { mutableStateOf("Tidak Setuju") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Buat Postingan Baru",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Pilih kategori & template banner interaktif warga",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }
            IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondary)
            }
        }
        Spacer(modifier = Modifier.height(14.dp))
        // 1. Pilih Kategori Postingan (Horizontal Carousel)
        Text(
            text = "Kategori Postingan:",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(POST_CATEGORY_OPTIONS) { opt ->
                val isSelected = selectedCategory.id == opt.id
                Card(
                    modifier = Modifier
                        .clickable {
                            selectedCategory = opt
                            selectedTemplate = getDefaultBannerTemplateForType(opt.label)
                            if (opt.id == "Undangan" || opt.id == "Ajakan") {
                                if (eventDate.isBlank()) eventDate = "Minggu depan"
                                if (eventTime.isBlank()) eventTime = "08.00 WIB"
                                if (eventLocation.isBlank()) eventLocation = "Balai RW 02"
                            }
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) opt.bgColor else Color.White
                    ),
                    border = BorderStroke(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = if (isSelected) opt.tintColor else BorderLight
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = opt.icon,
                            contentDescription = null,
                            tint = if (isSelected) opt.tintColor else TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = opt.label,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) opt.tintColor else TextPrimary
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        // 2. Banner Live Preview & Template Selector
        selectedTemplate?.let { tpl ->
            Text(
                text = "Preview Latar Banner:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            PostInteractiveBanner(
                template = tpl,
                customTitle = title.ifBlank { tpl.templateName },
                height = 100
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        PostBannerTemplateSelector(
            postType = selectedCategory.label,
            selectedTemplate = selectedTemplate,
            onSelectTemplate = { tpl ->
                selectedTemplate = tpl
                Toast.makeText(context, "Template '${tpl.templateName}' diterapkan!", Toast.LENGTH_SHORT).show()
            }
        )

        Spacer(modifier = Modifier.height(10.dp))
        // Deskripsi Kategori Terpilih + Preview CTA Button
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = selectedCategory.bgColor,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = "Tujuan: ${selectedCategory.description}",
                    fontSize = 11.sp,
                    color = selectedCategory.tintColor,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Tombol Call To Action:",
                        fontSize = 10.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = selectedCategory.tintColor
                    ) {
                        Text(
                            text = selectedCategory.ctaLabel,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(14.dp))
        // 2. Input Judul Postingan
        Text("Judul Postingan", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            placeholder = { Text("Contoh: Rapat Koordinasi Ronda Malam...", fontSize = 12.sp) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = selectedCategory.tintColor,
                unfocusedBorderColor = BorderLight
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        // 3. Input Konten / Penjelasan
        Text("Isi Pesan / Penjelasan", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            placeholder = { Text("Tuliskan detail informasi yang ingin disampaikan ke warga...", fontSize = 12.sp) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = selectedCategory.tintColor,
                unfocusedBorderColor = BorderLight
            )
        )
        // 4. Form Dinamis Sesuai Kategori
        if (selectedCategory.id == "Undangan" || selectedCategory.id == "Ajakan") {
            Spacer(modifier = Modifier.height(12.dp))
            Text("Jadwal & Lokasi Kegiatan:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = eventDate,
                    onValueChange = { eventDate = it },
                    label = { Text("Tanggal (Misal: Minggu, 26 Mei)", fontSize = 10.sp) },
                    modifier = Modifier.weight(1.2f),
                    shape = RoundedCornerShape(10.dp)
                )
                OutlinedTextField(
                    value = eventTime,
                    onValueChange = { eventTime = it },
                    label = { Text("Waktu (08.00 WIB)", fontSize = 10.sp) },
                    modifier = Modifier.weight(0.8f),
                    shape = RoundedCornerShape(10.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = eventLocation,
                onValueChange = { eventLocation = it },
                label = { Text("Lokasi (Misal: Balai RW 02 / Lapangan)", fontSize = 10.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )
        } else if (selectedCategory.id == "Polling") {
            Spacer(modifier = Modifier.height(12.dp))
            Text("Opsi Pilihan Polling:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = pollOptionA,
                onValueChange = { pollOptionA = it },
                label = { Text("Opsi A", fontSize = 10.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = pollOptionB,
                onValueChange = { pollOptionB = it },
                label = { Text("Opsi B", fontSize = 10.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        // 5. Submit Button
        Button(
            onClick = {
                if (title.isBlank()) {
                    Toast.makeText(context, "Mohon isi judul postingan", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                val newPost = CommunityFeedPost(
                    id = "post_${System.currentTimeMillis()}",
                    authorName = "Budi Santoso",
                    authorRole = "Warga RT 03",
                    authorRtRw = "RT 03 / RW 02",
                    timeAgo = "Baru saja",
                    category = selectedCategory.label,
                    title = title.trim(),
                    content = content.ifBlank { "Informasi dari warga untuk lingkungan RT 03 / RW 02." },
                    eventDate = if (eventDate.isNotBlank()) eventDate else null,
                    eventTime = if (eventTime.isNotBlank()) eventTime else null,
                    eventLocation = if (eventLocation.isNotBlank()) eventLocation else null,
                    participantsCount = 1,
                    isParticipating = true,
                    likesCount = 1,
                    isLiked = true,
                    commentsCount = 0,
                    bannerTemplateId = selectedTemplate?.id
                )
                onSubmit(newPost)
                Toast.makeText(context, "Postingan '${selectedCategory.label}' dengan template banner berhasil diterbitkan!", Toast.LENGTH_SHORT).show()
                onDismiss()
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = selectedCategory.tintColor),
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
        ) {
            Icon(imageVector = selectedCategory.icon, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Terbitkan Postingan (${selectedCategory.label})", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
        Spacer(modifier = Modifier.height(14.dp))
    }
}
