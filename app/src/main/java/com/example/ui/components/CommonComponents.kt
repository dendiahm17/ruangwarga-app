package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentGreenLight
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentOrangeLight
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentPurpleLight
import com.example.ui.theme.AccentRed
import com.example.ui.theme.AccentRedLight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryBlueDark
import com.example.ui.theme.PrimaryBlueLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@Composable
fun AppHeader(
    title: String,
    unreadCount: Int = 0,
    onNotificationClick: () -> Unit = {},
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    rightActionIcon: ImageVector? = null,
    onRightActionClick: () -> Unit = {},
    isAdminMode: Boolean = false,
    onAdminToggle: (() -> Unit)? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (showBackButton) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(40.dp)
                            .testTag("header_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali",
                            tint = TextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (onAdminToggle != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isAdminMode) Color(0xFFFFECEC) else Color(0xFFE8F0FE))
                            .border(1.dp, if (isAdminMode) AccentRed.copy(alpha = 0.5f) else PrimaryBlue.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                            .clickable(onClick = onAdminToggle)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("btn_toggle_admin_mode"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isAdminMode) Icons.Default.AdminPanelSettings else Icons.Default.Shield,
                                contentDescription = "Mode",
                                tint = if (isAdminMode) AccentRed else PrimaryBlue,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isAdminMode) "Pengurus RT" else "Warga",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isAdminMode) AccentRed else PrimaryBlue
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                if (rightActionIcon != null) {
                    IconButton(
                        onClick = onRightActionClick,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, BorderLight, CircleShape)
                            .testTag("header_right_action")
                    ) {
                        Icon(
                            imageVector = rightActionIcon,
                            contentDescription = "Action",
                            tint = TextPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                } else {
                    IconButton(
                        onClick = onNotificationClick,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, BorderLight, CircleShape)
                            .testTag("header_notification_button")
                    ) {
                        BadgedBox(
                            badge = {
                                if (unreadCount > 0) {
                                    Box(
                                        modifier = Modifier
                                            .offset(x = 2.dp, y = (-2).dp)
                                            .size(16.dp)
                                            .clip(CircleShape)
                                            .background(AccentRed),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = unreadCount.toString(),
                                            color = Color.White,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifikasi",
                                tint = TextPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        if (isAdminMode) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 2.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFFF3CD))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.AdminPanelSettings, contentDescription = null, tint = Color(0xFF856404), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Mode Pengurus RT Aktif: Anda dapat memverifikasi iuran, menyetujui surat & menanggapi pengaduan.",
                        fontSize = 11.sp,
                        color = Color(0xFF856404),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun AppSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text(placeholder, fontSize = 13.sp, color = TextTertiary) },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Cari", tint = TextSecondary, modifier = Modifier.size(20.dp))
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Hapus", tint = TextSecondary, modifier = Modifier.size(18.dp))
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color(0xFFF8FAFC),
            focusedBorderColor = PrimaryBlue,
            unfocusedBorderColor = BorderLight
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .height(52.dp)
            .testTag("app_search_bar")
    )
}

@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (status.lowercase()) {
        "baru", "transparan" -> Pair(AccentGreenLight, AccentGreen)
        "penting", "aktif" -> Pair(AccentRedLight, AccentRed)
        "selesai", "lunas", "hadir", "ditangani" -> Pair(AccentGreenLight, AccentGreen)
        "diproses", "ragu" -> Pair(AccentOrangeLight, AccentOrange)
        "pengajuan" -> Pair(PrimaryBlueLight, PrimaryBlue)
        "arsip", "jadwal aktif" -> Pair(AccentPurpleLight, AccentPurple)
        "belum lunas", "ditolak", "izin", "tidak hadir", "absen" -> Pair(AccentRedLight, AccentRed)
        "belum konfirmasi" -> Pair(Color(0xFFFFF3CD), Color(0xFF856404))
        else -> Pair(Color(0xFFF1F5F9), TextSecondary)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = status,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}

@Composable
fun FilterChipTab(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) PrimaryBlue else Color(0xFFF1F5F9))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 7.dp)
            .testTag("filter_chip_${label.lowercase().replace(" ", "_")}"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else TextPrimary
        )
    }
}

@Composable
fun MetricStatCard(
    count: String,
    label: String,
    icon: ImageVector,
    iconColor: Color,
    bgColor: Color,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("metric_card_${label.lowercase().replace(" ", "_")}"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconColor,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = count,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun QuickActionCircleButton(
    label: String,
    icon: ImageVector,
    iconColor: Color,
    bgColor: Color,
    isSelected: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(6.dp)
            .testTag("quick_action_${label.lowercase().replace(" ", "_")}")
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(if (isSelected) iconColor else bgColor)
                .border(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) iconColor else BorderLight,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) Color.White else iconColor,
                modifier = Modifier.size(26.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) iconColor else TextPrimary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CircularDonutProgress(
    percentage: Float,
    lunasPercentageText: String,
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
    strokeWidth: Dp = 14.dp
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            // Background track (Belum lunas)
            drawArc(
                color = AccentRedLight,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            // Foreground progress (Lunas)
            drawArc(
                color = AccentGreen,
                startAngle = -90f,
                sweepAngle = 360f * (percentage / 100f),
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = lunasPercentageText,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "Lunas",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = AccentGreen
            )
        }
    }
}

// Intent launching helper functions for Emergency Contacts & Sharing
fun launchPhoneDialer(context: Context, phoneNumber: String) {
    try {
        val cleanPhone = phoneNumber.replace("[^0-9+]".toRegex(), "")
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$cleanPhone"))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Tidak dapat membuka panggilan telepon", Toast.LENGTH_SHORT).show()
    }
}

fun launchWhatsApp(context: Context, phoneNumber: String, message: String = "Halo, saya warga RT 03/RW 02.") {
    try {
        var cleanPhone = phoneNumber.replace("[^0-9]".toRegex(), "")
        if (cleanPhone.startsWith("0")) {
            cleanPhone = "62" + cleanPhone.substring(1)
        }
        val uri = Uri.parse("https://wa.me/$cleanPhone?text=${Uri.encode(message)}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "WhatsApp tidak terpasang atau nomor tidak valid", Toast.LENGTH_SHORT).show()
    }
}

fun shareOfficialDocument(context: Context, title: String, content: String) {
    try {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TEXT, content)
        }
        val chooser = Intent.createChooser(intent, "Bagikan / Unduh Dokumen")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    } catch (e: Exception) {
        Toast.makeText(context, "Tidak dapat membagikan dokumen", Toast.LENGTH_SHORT).show()
    }
}
