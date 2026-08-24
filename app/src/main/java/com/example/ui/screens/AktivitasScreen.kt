package com.example.ui.screens

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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentGreenDark
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.PrimaryGreenDark
import com.example.ui.theme.PrimaryGreenLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.ui.viewmodel.RtrwUiState
import com.example.ui.viewmodel.RtrwViewModel

data class AgendaSectionGroup(
    val sectionHeader: String,
    val items: List<AgendaItemData>
)

data class AgendaItemData(
    val id: String,
    val time: String,
    val timeBg: Color,
    val title: String,
    val location: String,
    val participantCount: Int,
    val isMyAgenda: Boolean = false
)

@Composable
fun AktivitasScreen(
    uiState: RtrwUiState,
    viewModel: RtrwViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf("Agenda Saya") }

    val agendaData = listOf(
        AgendaSectionGroup(
            sectionHeader = "Hari Ini",
            items = listOf(
                AgendaItemData(
                    id = "agenda_1",
                    time = "19.30\nWIB",
                    timeBg = Color(0xFFDCFCE7),
                    title = "Rapat RT 03",
                    location = "Balai RT 03",
                    participantCount = 8,
                    isMyAgenda = true
                )
            )
        ),
        AgendaSectionGroup(
            sectionHeader = "Besok",
            items = listOf(
                AgendaItemData(
                    id = "agenda_2",
                    time = "07.00\nWIB",
                    timeBg = Color(0xFFFEF3C7),
                    title = "Kerja Bakti RW 02",
                    location = "Balai RW 02",
                    participantCount = 73,
                    isMyAgenda = true
                )
            )
        ),
        AgendaSectionGroup(
            sectionHeader = "Sabtu, 31 Agustus",
            items = listOf(
                AgendaItemData(
                    id = "agenda_3",
                    time = "06.30\nWIB",
                    timeBg = Color(0xFFE0E7FF),
                    title = "Senam Warga",
                    location = "Lapangan RW 02",
                    participantCount = 25,
                    isMyAgenda = false
                )
            )
        ),
        AgendaSectionGroup(
            sectionHeader = "Minggu, 1 September",
            items = listOf(
                AgendaItemData(
                    id = "agenda_4",
                    time = "16.00\nWIB",
                    timeBg = Color(0xFFE0F2FE),
                    title = "Pengajian Rutin",
                    location = "Mushola Al-Ikhlas",
                    participantCount = 32,
                    isMyAgenda = true
                )
            )
        )
    )

    val displayedGroups = if (selectedTab == "Agenda Saya") {
        agendaData.mapNotNull { group ->
            val myItems = group.items.filter { it.isMyAgenda }
            if (myItems.isNotEmpty()) group.copy(items = myItems) else null
        }
    } else {
        agendaData
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("agenda_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header (Agenda + Calendar Icon)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Agenda",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                IconButton(
                    onClick = { viewModel.openAgendaCalendarSheet() },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Kalender",
                        tint = TextPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        // Segmented Tabs: [ Agenda Saya ] | [ Semua Agenda ]
        item {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Tab Agenda Saya
                val isTab1 = selectedTab == "Agenda Saya"
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedTab = "Agenda Saya" },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Agenda Saya",
                        fontSize = 13.sp,
                        fontWeight = if (isTab1) FontWeight.Bold else FontWeight.Medium,
                        color = if (isTab1) PrimaryGreenDark else TextSecondary,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.5.dp)
                            .background(if (isTab1) PrimaryGreen else Color.Transparent)
                    )
                }

                // Tab Semua Agenda
                val isTab2 = selectedTab == "Semua Agenda"
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedTab = "Semua Agenda" },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Semua Agenda",
                        fontSize = 13.sp,
                        fontWeight = if (isTab2) FontWeight.Bold else FontWeight.Medium,
                        color = if (isTab2) PrimaryGreenDark else TextSecondary,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.5.dp)
                            .background(if (isTab2) PrimaryGreen else Color.Transparent)
                    )
                }
            }
        }

        // Timeline Groups
        displayedGroups.forEach { group ->
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = group.sectionHeader,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    group.items.forEach { agenda ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    Toast.makeText(context, "${agenda.title} di ${agenda.location}", Toast.LENGTH_SHORT).show()
                                },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, BorderLight)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Time Pill Box
                                Box(
                                    modifier = Modifier
                                        .size(width = 54.dp, height = 54.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(agenda.timeBg),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = agenda.time,
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary,
                                        lineHeight = 14.sp
                                    )
                                }

                                Spacer(modifier = Modifier.width(14.dp))

                                // Event Details
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = agenda.title,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = agenda.location,
                                        fontSize = 11.5.sp,
                                        color = TextSecondary
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))

                                    // Avatar Stack + Counter
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Row(horizontalArrangement = Arrangement.spacedBy((-6).dp)) {
                                            listOf(PrimaryGreen, AccentOrange, AccentPurple).forEach { color ->
                                                Box(
                                                    modifier = Modifier
                                                        .size(18.dp)
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
                                            text = "+${agenda.participantCount}",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = TextSecondary
                                        )
                                    }
                                }
                            }
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
