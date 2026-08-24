package com.example.ui.dialogs

import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Poll
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentGreenDark
import com.example.ui.theme.AccentGreenLight
import com.example.ui.theme.AccentOrangeDark
import com.example.ui.theme.AccentPurpleDark
import com.example.ui.theme.AccentPurpleLight
import com.example.ui.theme.AccentRedDark
import com.example.ui.theme.AccentRedLight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryBlueDark
import com.example.ui.theme.PrimaryBlueLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
data class CreateAktivitasTypeItem(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val iconTint: Color,
    val iconBg: Color
)
val CREATE_AKTIVITAS_TYPE_LIST = listOf(
    CreateAktivitasTypeItem(
        id = "Kegiatan",
        title = "Kegiatan",
        description = "Buat kegiatan atau event",
        icon = Icons.Default.Groups,
        iconTint = Color(0xFF16A34A),
        iconBg = Color(0xFFDCFCE7)
    ),
    CreateAktivitasTypeItem(
        id = "Pengumuman",
        title = "Pengumuman",
        description = "Sampaikan informasi penting",
        icon = Icons.Default.Campaign,
        iconTint = Color(0xFFDC2626),
        iconBg = Color(0xFFFEE2E2)
    ),
    CreateAktivitasTypeItem(
        id = "Bantuan",
        title = "Bantuan",
        description = "Minta atau tawarkan bantuan",
        icon = Icons.Default.Favorite,
        iconTint = Color(0xFFEF4444),
        iconBg = Color(0xFFFEE2E2)
    ),
    CreateAktivitasTypeItem(
        id = "Usulan",
        title = "Usulan",
        description = "Berikan ide untuk lingkungan",
        icon = Icons.Default.Lightbulb,
        iconTint = Color(0xFFD97706),
        iconBg = Color(0xFFFEF3C7)
    ),
    CreateAktivitasTypeItem(
        id = "Polling",
        title = "Polling",
        description = "Buat jajak pendapat",
        icon = Icons.Default.Poll,
        iconTint = Color(0xFF2563EB),
        iconBg = Color(0xFFEFF6FF)
    ),
    CreateAktivitasTypeItem(
        id = "Dokumentasi",
        title = "Dokumentasi",
        description = "Bagikan dokumentasi kegiatan",
        icon = Icons.Default.PhotoCamera,
        iconTint = Color(0xFF0284C7),
        iconBg = Color(0xFFE0F2FE)
    ),
    CreateAktivitasTypeItem(
        id = "Kejadian",
        title = "Lapor Kejadian",
        description = "Laporkan kejadian di lingkungan",
        icon = Icons.Default.Warning,
        iconTint = Color(0xFFDC2626),
        iconBg = Color(0xFFFEE2E2)
    )
)
/**
 * AktivitasCreateSelectTypeBottomSheet - Layar 3 (Mockup 3: Buat Sesuatu)
 * Menampilkan list kartu aksi vertikal dengan judul dan subdeskripsi.
 */
@Composable
fun AktivitasCreateSelectTypeBottomSheet(
    onSelectType: (type: String) -> Unit,
    onDismiss: () -> Unit
) {
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
            Text(
                text = "Buat Sesuatu",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondary)
            }
        }
        Spacer(modifier = Modifier.height(14.dp))
        // Vertical List of 7 Action Items
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            CREATE_AKTIVITAS_TYPE_LIST.forEach { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(68.dp)
                        .clickable { onSelectType(item.id) },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, BorderLight)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(item.iconBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                tint = item.iconTint,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = item.description,
                                fontSize = 11.5.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(28.dp))
    }
}
