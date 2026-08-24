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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.ui.components.PostBannerTemplate
import com.example.ui.components.PostBannerTemplateSelector
import com.example.ui.components.PostInteractiveBanner
import com.example.ui.components.getDefaultBannerTemplateForType
import com.example.ui.viewmodel.CommunityFeedPost

/**
 * AktivitasFormBottomSheet - Layar 6 (Mockup 6: Buat Kegiatan - Form)
 * Berisi pilihan template banner interaktif berdasarkan jenis postingan, judul, deskripsi, tanggal & waktu, lokasi, dan tombol Publikasikan.
 */
@Composable
fun AktivitasFormBottomSheet(
    type: String, // "Kegiatan", "Pengumuman", "Kejadian", "Bantuan", "Usulan", "Polling", "Dokumentasi", "Apresiasi"
    onDismiss: () -> Unit,
    onSubmit: (CommunityFeedPost) -> Unit
) {
    val context = LocalContext.current
    var selectedTemplate by remember(type) { mutableStateOf<PostBannerTemplate?>(getDefaultBannerTemplateForType(type)) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("30/08/2026") }
    var time by remember { mutableStateOf("07.00 WIB") }
    var location by remember { mutableStateOf("Balai RW 02 — Blok A – C") }
    var hasPhoto by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Kembali", tint = TextPrimary)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Buat $type",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
            TextButton(onClick = {
                Toast.makeText(context, "Disimpan sebagai draft", Toast.LENGTH_SHORT).show()
                onDismiss()
            }) {
                Text(text = "Draft", fontSize = 13.sp, color = AccentGreenDark, fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 1. Interactive Banner Preview (Live Preview Sesuai Judul & Template)
        selectedTemplate?.let { tpl ->
            Text(
                text = "Preview Latar Banner Postingan:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            PostInteractiveBanner(
                template = tpl,
                customTitle = title.ifBlank { tpl.templateName },
                height = 105
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        // 2. Pemilih Template Banner Interaktif
        PostBannerTemplateSelector(
            postType = type,
            selectedTemplate = selectedTemplate,
            onSelectTemplate = { tpl ->
                selectedTemplate = tpl
                Toast.makeText(context, "Template '${tpl.templateName}' diterapkan!", Toast.LENGTH_SHORT).show()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
        // 2. Judul Kegiatan *
        Row {
            Text("Judul $type", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(" *", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Red)
        }
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            placeholder = { Text(if (type == "Kegiatan") "Contoh: Kerja Bakti RW 02" else "Tuliskan judul...", fontSize = 12.sp, color = TextTertiary) },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentGreen,
                unfocusedBorderColor = BorderLight
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        // 3. Deskripsi *
        Row {
            Text("Deskripsi", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(" *", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Red)
        }
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            placeholder = { Text("Tuliskan deskripsi $type...", fontSize = 12.sp, color = TextTertiary) },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(95.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentGreen,
                unfocusedBorderColor = BorderLight
            )
        )
        // 4. Tanggal & Waktu (Jika kegiatan / undangan)
        if (type == "Kegiatan" || type == "Undangan" || type == "Ajakan" || type == "Pengumuman") {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row {
                        Text("Tanggal", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(" *", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Red)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = date,
                        onValueChange = { date = it },
                        shape = RoundedCornerShape(10.dp),
                        trailingIcon = {
                            Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(18.dp))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AccentGreen,
                            unfocusedBorderColor = BorderLight
                        )
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Row {
                        Text("Waktu", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(" *", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Red)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = time,
                        onValueChange = { time = it },
                        shape = RoundedCornerShape(10.dp),
                        trailingIcon = {
                            Icon(imageVector = Icons.Default.Schedule, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(18.dp))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AccentGreen,
                            unfocusedBorderColor = BorderLight
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            // 5. Lokasi *
            Row {
                Text("Lokasi", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(" *", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Red)
            }
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                shape = RoundedCornerShape(10.dp),
                trailingIcon = {
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(18.dp))
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentGreen,
                    unfocusedBorderColor = BorderLight
                )
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        // 6. Tombol Publikasikan (Hijau Solid Full Width)
        Button(
            onClick = {
                if (title.isBlank()) {
                    Toast.makeText(context, "Mohon masukkan judul", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                val newPost = CommunityFeedPost(
                    id = "post_${System.currentTimeMillis()}",
                    authorName = "Budi Santoso",
                    authorRole = "Warga RT 03",
                    authorRtRw = "RT 03 / RW 02",
                    timeAgo = "Baru saja",
                    category = type,
                    title = title.trim(),
                    content = description.ifBlank { "Informasi $type dari warga untuk lingkungan." },
                    eventDate = if (type in listOf("Kegiatan", "Undangan", "Ajakan")) date else null,
                    eventTime = if (type in listOf("Kegiatan", "Undangan", "Ajakan")) time else null,
                    eventLocation = if (type in listOf("Kegiatan", "Undangan", "Ajakan")) location else null,
                    participantsCount = 1,
                    isParticipating = true,
                    likesCount = 1,
                    isLiked = true,
                    commentsCount = 0,
                    bannerTemplateId = selectedTemplate?.id
                )
                onSubmit(newPost)
                Toast.makeText(context, "$type berhasil dipublikasikan!", Toast.LENGTH_SHORT).show()
                onDismiss()
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
        ) {
            Text("Publikasikan", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
        Spacer(modifier = Modifier.height(14.dp))
    }
}
