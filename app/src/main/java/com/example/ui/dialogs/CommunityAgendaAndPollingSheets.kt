package com.example.ui.dialogs

import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CommunityEventEntity
import com.example.data.model.PollingEntity
import com.example.ui.components.FilterChipTab
import com.example.ui.components.StatusBadge
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentGreenDark
import com.example.ui.theme.AccentGreenLight
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentOrangeLight
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentPurpleLight
import com.example.ui.theme.AccentRed
import com.example.ui.theme.AccentRedLight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryBlueLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
// ==========================================
// FEATURE 3A: KALENDER AGENDA KEGIATAN RT
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaCalendarBottomSheet(
    events: List<CommunityEventEntity>,
    isAdminMode: Boolean,
    onDismiss: () -> Unit,
    onRsvpChange: (eventId: Int, status: String) -> Unit,
    onOpenAddEvent: () -> Unit
) {
    var filterCategory by remember { mutableStateOf("Semua") }
    val categories = listOf("Semua", "Kerja Bakti", "Musyawarah", "Kesehatan", "Sosial / Pesta")
    val filteredEvents = if (filterCategory == "Semua") events else {
        events.filter { it.kategori.equals(filterCategory, ignoreCase = true) }
    }
    Dialog(properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false), onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier.fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
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
                            .background(AccentPurpleLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Agenda RT",
                            tint = AccentPurple,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Kalender Kegiatan Warga",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Agenda & Musyawarah RT 03 / RW 08",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup")
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            // Add Event Action (For Pengurus RT or all)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Daftar Kegiatan Mendatang",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Button(
                    onClick = onOpenAddEvent,
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed), shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .height(34.dp)
                        .testTag("btn_tambah_agenda")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Tambah Acara", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            // Filter Category Tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.take(3).forEach { cat ->
                    FilterChipTab(
                        label = cat,
                        isSelected = filterCategory == cat,
                        onClick = { filterCategory = cat }
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.drop(3).forEach { cat ->
                    FilterChipTab(
                        label = cat,
                        isSelected = filterCategory == cat,
                        onClick = { filterCategory = cat }
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            // List of Events
            if (filteredEvents.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Belum ada agenda kegiatan di kategori ini.", color = TextSecondary, fontSize = 13.sp)
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    filteredEvents.forEach { event ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .testTag("event_card_${event.id}"),
                            colors = CardDefaults.cardColors(containerColor = Color.White), border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = event.judul,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = event.kategori,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = PrimaryBlue
                                        )
                                    }
                                    StatusBadge(status = event.rsvpStatus)
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                // Date, Time & Location Chips
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.Event, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = "${event.tanggal} • ${event.waktu}", fontSize = 12.sp, color = TextSecondary)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = AccentRed, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = event.lokasi, fontSize = 12.sp, color = TextSecondary)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = event.deskripsi,
                                    fontSize = 12.sp,
                                    color = TextPrimary,
                                    lineHeight = 16.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.Group, contentDescription = null, tint = AccentGreenDark, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "${event.jumlahHadir} Warga Siap Hadir",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = AccentGreenDark
                                        )
                                    }
                                    Text(
                                        text = "PIC: ${event.penanggungJawab}",
                                        fontSize = 11.sp,
                                        color = TextTertiary
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                HorizontalDivider(color = BorderLight)
                                Spacer(modifier = Modifier.height(10.dp))
                                // RSVP Toggle Buttons
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Konfirmasi Anda:",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        val isHadir = event.rsvpStatus == "Hadir"
                                        val isRagu = event.rsvpStatus == "Ragu"
                                        val isTidak = event.rsvpStatus == "Tidak Hadir"
                                        Button(
                                            onClick = { onRsvpChange(event.id, "Hadir") },
                                            colors = ButtonDefaults.buttonColors(containerColor = AccentRed), shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.height(32.dp)
                                        ) {
                                            Text(
                                                text = if (isHadir) "✓ Hadir" else "Hadir",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isHadir) Color.White else TextPrimary
                                            )
                                        }
                                        Button(
                                            onClick = { onRsvpChange(event.id, "Ragu") },
                                            colors = ButtonDefaults.buttonColors(containerColor = AccentRed), shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.height(32.dp)
                                        ) {
                                            Text(
                                                text = "Ragu",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isRagu) Color.White else TextPrimary
                                            )
                                        }
                                        Button(
                                            onClick = { onRsvpChange(event.id, "Tidak Hadir") },
                                            colors = ButtonDefaults.buttonColors(containerColor = AccentRed), shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.height(32.dp)
                                        ) {
                                            Text(
                                                text = "Absen",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isTidak) Color.White else TextPrimary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
// Add Event Sheet
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCommunityEventBottomSheet(
    onDismiss: () -> Unit,
    onSubmit: (judul: String, kategori: String, tanggal: String, waktu: String, lokasi: String, deskripsi: String, penanggungJawab: String) -> Unit
) {
    var judul by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("Kerja Bakti") }
    var tanggal by remember { mutableStateOf("Minggu, 31 Mei 2026") }
    var waktu by remember { mutableStateOf("07:00 - 10:00 WIB") }
    var lokasi by remember { mutableStateOf("Sepanjang Jalan Blok C & Lapangan RT") }
    var deskripsi by remember { mutableStateOf("") }
    var penanggungJawab by remember { mutableStateOf("Pak Joko (Ketua RT 03)") }
    var expandedKategori by remember { mutableStateOf(false) }
    val categories = listOf("Kerja Bakti", "Musyawarah", "Kesehatan", "Sosial / Pesta", "Olahraga Bersama")
    Dialog(properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false), onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier.fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tambah Agenda Kegiatan RT",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup")
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            OutlinedTextField(
                value = judul,
                onValueChange = { judul = it },
                label = { Text("Nama Kegiatan *") },
                placeholder = { Text("Contoh: Kerja Bakti Massal Bersih Saluran") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_judul_agenda"),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))
            ExposedDropdownMenuBox(
                expanded = expandedKategori,
                onExpandedChange = { expandedKategori = !expandedKategori }
            ) {
                OutlinedTextField(
                    value = kategori,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Kategori Acara") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedKategori) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = expandedKategori,
                    onDismissRequest = { expandedKategori = false }
                ) {
                    categories.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                kategori = item
                                expandedKategori = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = tanggal,
                    onValueChange = { tanggal = it },
                    label = { Text("Tanggal") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = waktu,
                    onValueChange = { waktu = it },
                    label = { Text("Waktu") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = lokasi,
                onValueChange = { lokasi = it },
                label = { Text("Lokasi Acara *") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = deskripsi,
                onValueChange = { deskripsi = it },
                label = { Text("Deskripsi & Himbauan Acara") },
                placeholder = { Text("Membawa sapu lidi, cangkul, dan sarung tangan.") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                minLines = 2
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = penanggungJawab,
                onValueChange = { penanggungJawab = it },
                label = { Text("Penanggung Jawab / PIC") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    if (judul.isNotBlank() && lokasi.isNotBlank()) {
                        onSubmit(judul, kategori, tanggal, waktu, lokasi, deskripsi, penanggungJawab)
                    }
                },
                enabled = judul.isNotBlank() && lokasi.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_simpan_agenda"),
                colors = ButtonDefaults.buttonColors(containerColor = AccentRed), shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Simpan & Umumkan Agenda", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}
// ==========================================
// FEATURE 3B: POLLING & MUSYAWARAH DIGITAL RT
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PollingBottomSheet(
    polls: List<PollingEntity>,
    isAdminMode: Boolean,
    onDismiss: () -> Unit,
    onVote: (pollId: Int, option: String) -> Unit,
    onOpenCreatePoll: () -> Unit,
    onClosePoll: (pollId: Int) -> Unit
) {
    Dialog(properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false), onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier.fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
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
                            .background(PrimaryBlueLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.HowToVote,
                            contentDescription = "Polling RT",
                            tint = PrimaryBlue,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Musyawarah & Polling Warga",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Voting keputusan bersama RT 03 secara transparan",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup")
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            // Create Poll Action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Daftar Jajak Pendapat (${polls.size})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Button(
                    onClick = onOpenCreatePoll,
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed), shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .height(34.dp)
                        .testTag("btn_buat_polling")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Buat Polling", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            if (polls.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Belum ada polling musyawarah yang aktif.", color = TextSecondary, fontSize = 13.sp)
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    polls.forEach { poll ->
                        val totalVotes = (poll.suaraA + poll.suaraB + (poll.suaraC ?: 0)).coerceAtLeast(1)
                        val pctA = (poll.suaraA.toFloat() / totalVotes.toFloat())
                        val pctB = (poll.suaraB.toFloat() / totalVotes.toFloat())
                        val pctC = ((poll.suaraC ?: 0).toFloat() / totalVotes.toFloat())
                        val isClosed = poll.status == "Selesai"
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .testTag("poll_card_${poll.id}"),
                            colors = CardDefaults.cardColors(containerColor = Color.White), border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = poll.judul,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "Batas Waktu: ${poll.batasWaktu} • ${poll.kategori}",
                                            fontSize = 11.sp,
                                            color = TextSecondary
                                        )
                                    }
                                    StatusBadge(status = poll.status)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = poll.deskripsi,
                                    fontSize = 12.sp,
                                    color = TextPrimary,
                                    lineHeight = 16.sp
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                // Option A
                                PollOptionRow(
                                    optionLetter = "A",
                                    optionText = poll.opsiA,
                                    votes = poll.suaraA,
                                    fraction = pctA,
                                    isSelected = poll.myVote == "A",
                                    isClosed = isClosed,
                                    onClick = { if (!isClosed) onVote(poll.id, "A") }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                // Option B
                                PollOptionRow(
                                    optionLetter = "B",
                                    optionText = poll.opsiB,
                                    votes = poll.suaraB,
                                    fraction = pctB,
                                    isSelected = poll.myVote == "B",
                                    isClosed = isClosed,
                                    onClick = { if (!isClosed) onVote(poll.id, "B") }
                                )
                                // Option C (if present)
                                if (!poll.opsiC.isNullOrBlank()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    PollOptionRow(
                                        optionLetter = "C",
                                        optionText = poll.opsiC,
                                        votes = poll.suaraC ?: 0,
                                        fraction = pctC,
                                        isSelected = poll.myVote == "C",
                                        isClosed = isClosed,
                                        onClick = { if (!isClosed) onVote(poll.id, "C") }
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                        if (isAdminMode && !isClosed) {
                                            OutlinedButton(
                                                onClick = { onClosePoll(poll.id) },
                                                shape = RoundedCornerShape(8.dp),
                                                modifier = Modifier.height(30.dp)
                                            ) {
                                                Text(text = "Tutup Polling", fontSize = 11.sp, color = AccentRed)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
@Composable
private fun PollOptionRow(
    optionLetter: String,
    optionText: String,
    votes: Int,
    fraction: Float,
    isSelected: Boolean,
    isClosed: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(enabled = !isClosed) { onClick() }
            .testTag("poll_option_$optionLetter"),
        colors = CardDefaults.cardColors(containerColor = Color.White), border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) PrimaryBlue else BorderLight
        )
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) PrimaryBlue else Color(0xFFE2E8F0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = optionLetter,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else TextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = optionText,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) PrimaryBlue else TextPrimary
                    )
                }
                Text(
                    text = "${(fraction * 100).toInt()}% ($votes)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) PrimaryBlue else TextSecondary
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { fraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (isSelected) PrimaryBlue else Color(0xFF94A3B8),
                trackColor = Color(0xFFE2E8F0)
            )
        }
    }
}
// Create Poll Sheet
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePollBottomSheet(
    onDismiss: () -> Unit,
    onSubmit: (judul: String, deskripsi: String, kategori: String, batasWaktu: String, opsiA: String, opsiB: String, opsiC: String?) -> Unit
) {
    var judul by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("Kebersihan") }
    var batasWaktu by remember { mutableStateOf("31 Mei 2026") }
    var opsiA by remember { mutableStateOf("") }
    var opsiB by remember { mutableStateOf("") }
    var opsiC by remember { mutableStateOf("") }
    Dialog(properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false), onDismissRequest = onDismiss) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 32.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Buat Polling / Jajak Pendapat",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup")
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
                OutlinedTextField(
                    value = judul,
                    onValueChange = { judul = it },
                    label = { Text("Pertanyaan / Topik Musyawarah *") },
                    placeholder = { Text("Contoh: Jadwal Pengangkutan Sampah Tambahan") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_judul_poll"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = deskripsi,
                    onValueChange = { deskripsi = it },
                    label = { Text("Penjelasan Musyawarah") },
                    placeholder = { Text("Jelaskan latar belakang perlunya keputusan warga...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 2
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = kategori,
                        onValueChange = { kategori = it },
                        label = { Text("Kategori") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = batasWaktu,
                        onValueChange = { batasWaktu = it },
                        label = { Text("Batas Akhir") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))
                Text(text = "Pilihan Opsi Voting:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = opsiA,
                    onValueChange = { opsiA = it },
                    label = { Text("Opsi A *") },
                    placeholder = { Text("Contoh: Setuju (3x Seminggu)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = opsiB,
                    onValueChange = { opsiB = it },
                    label = { Text("Opsi B *") },
                    placeholder = { Text("Contoh: Cukup 2x Seminggu") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = opsiC,
                    onValueChange = { opsiC = it },
                    label = { Text("Opsi C (Opsional)") },
                    placeholder = { Text("Contoh: Jadwal Fleksibel Akhir Pekan") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        if (judul.isNotBlank() && opsiA.isNotBlank() && opsiB.isNotBlank()) {
                            onSubmit(judul, deskripsi, kategori, batasWaktu, opsiA, opsiB, opsiC.ifBlank { null })
                        }
                    },
                    enabled = judul.isNotBlank() && opsiA.isNotBlank() && opsiB.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_simpan_polling"),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Buka Polling untuk Warga", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
    }
}
