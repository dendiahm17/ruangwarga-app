package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.CommunityEventEntity
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentGreenDark
import com.example.ui.theme.AccentGreenLight
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentOrangeDark
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentPurpleDark
import com.example.ui.theme.AccentPurpleLight
import com.example.ui.theme.AccentRed
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryBlueDark
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.PrimaryGreenDark
import com.example.ui.theme.PrimaryGreenLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.ui.viewmodel.RtrwUiState
import com.example.ui.viewmodel.RtrwViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * AktivitasScreen (Menu Agenda Terintegrasi Kalender Interaktif)
 * Fitur:
 * 1. Kalender Bulanan Interaktif (Navigasi Bulan & Tahun)
 * 2. Indikator Titik (Dot Indicator) pada Tanggal yang Memiliki Agenda
 * 3. Klik Tanggal -> Menampilkan Daftar Kegiatan yang Dijadwalkan pada Tanggal Tersebut
 * 4. Tombol / Dialog Tambah Jadwal Kegiatan Langsung Terhubung ke Database
 */
@Composable
fun AktivitasScreen(
    uiState: RtrwUiState,
    viewModel: RtrwViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    // Inisialisasi Tanggal Hari Ini
    val today = remember { LocalDate.now() }
    var currentYearMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf(today) }
    var showAddEventDialog by remember { mutableStateOf(false) }

    // Format Tanggal Terpilih (Bahasa Indonesia)
    val indonesianLocale = Locale("id", "ID")
    val selectedDateDisplayFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", indonesianLocale)
    val dayMonthFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", indonesianLocale)
    val standardDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val selectedDateStr = selectedDate.format(standardDateFormatter)
    val selectedDayNum = selectedDate.dayOfMonth

    // Filter kegiatan yang cocok dengan tanggal terpilih (pencocokan parsial tanggal)
    val eventsOnSelectedDate = uiState.communityEvents.filter { event ->
        event.tanggal.contains(selectedDate.dayOfMonth.toString()) ||
        event.tanggal.contains(selectedDate.format(DateTimeFormatter.ofPattern("d MMMM", indonesianLocale)), ignoreCase = true) ||
        event.tanggal.contains(selectedDateStr)
    }

    // Set tanggal yang memiliki kegiatan di bulan saat ini untuk dot indicator
    val datesWithEventsInMonth = remember(uiState.communityEvents, currentYearMonth) {
        val set = mutableSetOf<Int>()
        uiState.communityEvents.forEach { ev ->
            // Cek jika mengandung angka tanggal
            val digits = ev.tanggal.filter { it.isDigit() }
            val day = digits.toIntOrNull()
            if (day != null && day in 1..31) {
                set.add(day)
            } else {
                // Ekstrak hari pertama dari tanggal string (misal: "25 Mei 2026")
                val parts = ev.tanggal.split(" ")
                val firstNum = parts.firstOrNull { it.all { ch -> ch.isDigit() } }?.toIntOrNull()
                if (firstNum != null && firstNum in 1..31) {
                    set.add(firstNum)
                }
            }
        }
        set
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        // ============================================================
        // 1. STANDARD NATIVE FULL HORIZONTAL APP HEADER (SELARAS DENGAN MENU LAIN)
        // ============================================================
        com.example.ui.components.AppHeader(
            title = "Agenda Kegiatan",
            rightActionIcon = Icons.Default.Add,
            onRightActionClick = { showAddEventDialog = true }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
        ) {
            // ============================================================
            // 2. WIDGET KALENDER BULANAN (MONTH CALENDAR VIEW)
            // ============================================================
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Month & Year Header Controller
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val monthTitle = currentYearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy", indonesianLocale))
                            Text(
                                text = monthTitle,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimary
                            )

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { currentYearMonth = currentYearMonth.minusMonths(1) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = "Bulan Lalu", tint = TextPrimary)
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                IconButton(
                                    onClick = { currentYearMonth = currentYearMonth.plusMonths(1) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Bulan Depan", tint = TextPrimary)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Day of Week Header (SEN, SEL, RAB, KAM, JUM, SAB, MIN)
                        val daysOfWeek = listOf("Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab")
                        Row(modifier = Modifier.fillMaxWidth()) {
                            daysOfWeek.forEachIndexed { idx, day ->
                                Text(
                                    text = day,
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Center,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (idx == 0) AccentRed else TextTertiary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = Color(0xFFF1F5F9))
                        Spacer(modifier = Modifier.height(8.dp))

                        // Calendar Grid Days
                        val firstDayOfMonth = currentYearMonth.atDay(1)
                        val dayOfWeekFirst = firstDayOfMonth.dayOfWeek.value % 7 // 0 for Sunday
                        val daysInMonth = currentYearMonth.lengthOfMonth()

                        val totalCells = ((dayOfWeekFirst + daysInMonth + 6) / 7) * 7
                        val rows = totalCells / 7

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            for (r in 0 until rows) {
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    for (c in 0 until 7) {
                                        val cellIndex = r * 7 + c
                                        val dayNumber = cellIndex - dayOfWeekFirst + 1

                                        if (dayNumber in 1..daysInMonth) {
                                            val dateObj = currentYearMonth.atDay(dayNumber)
                                            val isSelected = dateObj == selectedDate
                                            val isCurrentDay = dateObj == today
                                            val hasEvent = datesWithEventsInMonth.contains(dayNumber)

                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .height(42.dp)
                                                    .clip(RoundedCornerShape(10.dp))
                                                    .background(
                                                        when {
                                                            isSelected -> PrimaryGreenDark
                                                            isCurrentDay -> Color(0xFFDCFCE7)
                                                            else -> Color.Transparent
                                                        }
                                                    )
                                                    .clickable { selectedDate = dateObj },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                    Text(
                                                        text = "$dayNumber",
                                                        fontSize = 13.sp,
                                                        fontWeight = if (isSelected || isCurrentDay) FontWeight.Bold else FontWeight.Medium,
                                                        color = when {
                                                            isSelected -> Color.White
                                                            c == 0 -> AccentRed
                                                            isCurrentDay -> PrimaryGreenDark
                                                            else -> TextPrimary
                                                        }
                                                    )
                                                    // Dot Indicator jika ada agenda kegiatan
                                                    if (hasEvent) {
                                                        Spacer(modifier = Modifier.height(2.dp))
                                                        Box(
                                                            modifier = Modifier
                                                                .size(4.5.dp)
                                                                .clip(CircleShape)
                                                                .background(if (isSelected) Color.White else AccentOrange)
                                                        )
                                                    }
                                                }
                                            }
                                        } else {
                                            Spacer(modifier = Modifier.weight(1f))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ============================================================
            // 3. DAFTAR KEGIATAN PADA TANGGAL YANG DIKLIK
            // ============================================================
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = selectedDate.format(selectedDateDisplayFormatter),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary
                        )
                        Text(
                            text = if (eventsOnSelectedDate.isNotEmpty()) "${eventsOnSelectedDate.size} Jadwal Kegiatan Ditemukan" else "Tidak ada jadwal kegiatan",
                            fontSize = 11.5.sp,
                            color = if (eventsOnSelectedDate.isNotEmpty()) PrimaryGreenDark else TextSecondary
                        )
                    }

                    if (eventsOnSelectedDate.isNotEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFDCFCE7)
                        ) {
                            Text(
                                text = "Aktif",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF15803D),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (eventsOnSelectedDate.isEmpty()) {
                // Empty State jika tanggal belum ada agenda
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFF1F5F9)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Event,
                                    contentDescription = null,
                                    tint = TextTertiary,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Belum Ada Jadwal Kegiatan",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Tidak ada agenda warga yang dijadwalkan pada tanggal ini. Anda dapat menambahkan jadwal kegiatan baru.",
                                fontSize = 12.sp,
                                color = TextSecondary,
                                textAlign = TextAlign.Center,
                                lineHeight = 16.sp
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            OutlinedButton(
                                onClick = { showAddEventDialog = true },
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, PrimaryGreenDark)
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = PrimaryGreenDark, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Jadwalkan Kegiatan di Tanggal Ini",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryGreenDark
                                )
                            }
                        }
                    }
                }
            } else {
                // List Kegiatan pada tanggal terpilih
                items(eventsOnSelectedDate) { event ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .clickable { viewModel.openEventDetail(event) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = when (event.kategori) {
                                        "Kerja Bakti" -> AccentGreenLight
                                        "Posyandu", "Kesehatan" -> Color(0xFFE0F2FE)
                                        "Rapat RT", "Musyawarah" -> Color(0xFFFEF3C7)
                                        else -> AccentPurpleLight
                                    }
                                ) {
                                    Text(
                                        text = event.kategori,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = when (event.kategori) {
                                            "Kerja Bakti" -> AccentGreenDark
                                            "Posyandu", "Kesehatan" -> PrimaryBlueDark
                                            "Rapat RT", "Musyawarah" -> AccentOrangeDark
                                            else -> AccentPurpleDark
                                        },
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.Schedule, contentDescription = null, tint = TextTertiary, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = event.waktu,
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextSecondary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = event.judul,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = event.deskripsi,
                                fontSize = 12.sp,
                                color = TextSecondary,
                                lineHeight = 16.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = AccentRed, modifier = Modifier.size(15.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = event.lokasi, fontSize = 11.5.sp, color = TextSecondary)
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.Group, contentDescription = null, tint = PrimaryGreenDark, modifier = Modifier.size(15.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "${event.jumlahHadir} Warga Ikut", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = PrimaryGreenDark)
                                }
                            }
                        }
                    }
                }
            }

            // ============================================================
            // 4. SELURUH AGENDA KEGIATAN MENDATANG DI LINGKUNGAN RT/RW
            // ============================================================
            item {
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Semua Agenda Mendatang",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )
                    Text(
                        text = "${uiState.communityEvents.size} Agenda",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryGreenDark
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
            }

            items(uiState.communityEvents) { allEvent ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clickable { viewModel.openEventDetail(allEvent) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFF1F5F9))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Date Box
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFF8FAFC),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            modifier = Modifier.size(width = 50.dp, height = 50.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = allEvent.tanggal.take(6),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextSecondary,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = allEvent.judul,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${allEvent.waktu} • ${allEvent.lokasi}",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = TextTertiary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }

    // ============================================================
    // DIALOG FORM TAMBAH JADWAL KEGIATAN BARU
    // ============================================================
    if (showAddEventDialog) {
        var newTitle by remember { mutableStateOf("") }
        var newCategory by remember { mutableStateOf("Kerja Bakti") }
        var newDate by remember { mutableStateOf(selectedDate.format(dayMonthFormatter)) }
        var newTime by remember { mutableStateOf("07.30 WIB") }
        var newLocation by remember { mutableStateOf("Balai Warga RT 03 / RW 02") }
        var newDescription by remember { mutableStateOf("") }

        val categoryOptions = listOf("Kerja Bakti", "Rapat RT", "Posyandu", "Senam Sehat", "Pengajian", "Musyawarah", "Peringatan Hari Besar")

        Dialog(
            onDismissRequest = { showAddEventDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "📅", fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Tambah Jadwal Kegiatan",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Nama Kegiatan
                        Text(text = "Nama / Judul Kegiatan *", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = newTitle,
                            onValueChange = { newTitle = it },
                            placeholder = { Text("Contoh: Kerja Bakti Bersih Saluran Air", fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryGreenDark,
                                unfocusedBorderColor = BorderLight
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Kategori Kegiatan
                        Text(text = "Kategori Kegiatan *", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            categoryOptions.forEach { cat ->
                                val isSelected = newCategory == cat
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) PrimaryGreenLight else Color(0xFFF1F5F9),
                                    border = BorderStroke(1.dp, if (isSelected) PrimaryGreen else Color.Transparent),
                                    modifier = Modifier.clickable { newCategory = cat }
                                ) {
                                    Text(
                                        text = cat,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) PrimaryGreenDark else TextPrimary,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Tanggal & Waktu
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "Tanggal *", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = newDate,
                                    onValueChange = { newDate = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = PrimaryGreenDark,
                                        unfocusedBorderColor = BorderLight
                                    )
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "Waktu *", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = newTime,
                                    onValueChange = { newTime = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = PrimaryGreenDark,
                                        unfocusedBorderColor = BorderLight
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Lokasi
                        Text(text = "Lokasi Pelaksanaan *", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = newLocation,
                            onValueChange = { newLocation = it },
                            placeholder = { Text("Contoh: Balai RW 02 / Lapangan RT 03", fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryGreenDark,
                                unfocusedBorderColor = BorderLight
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Deskripsi
                        Text(text = "Deskripsi / Catatan Tambahan", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = newDescription,
                            onValueChange = { newDescription = it },
                            placeholder = { Text("Harap membawa peralatan kebersihan masing-masing...", fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            minLines = 2,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryGreenDark,
                                unfocusedBorderColor = BorderLight
                            )
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // Tombol Aksi Simpan & Batal
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedButton(
                                onClick = { showAddEventDialog = false },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, Color(0xFFCBD5E1))
                            ) {
                                Text("Batal", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.5.sp)
                            }

                            Button(
                                onClick = {
                                    if (newTitle.isBlank()) {
                                        Toast.makeText(context, "Judul kegiatan wajib diisi", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }
                                    viewModel.addCommunityEvent(
                                        judul = newTitle.trim(),
                                        kategori = newCategory,
                                        tanggal = newDate.trim(),
                                        waktu = newTime.trim(),
                                        lokasi = newLocation.trim(),
                                        deskripsi = newDescription.trim().ifBlank { "Kegiatan $newCategory bersama warga." },
                                        penanggungJawab = uiState.profile.nama.ifBlank { "Pengurus RT 03 / RW 02" }
                                    )
                                    showAddEventDialog = false
                                    Toast.makeText(context, "Jadwal kegiatan berhasil disimpan ke kalender!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .weight(1.3f)
                                    .height(44.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreenDark)
                            ) {
                                Text("Simpan Jadwal", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.5.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
