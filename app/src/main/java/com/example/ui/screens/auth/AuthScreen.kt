package com.example.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentGreenDark
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.PrimaryGreenDark
import com.example.ui.theme.PrimaryGreenLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.RtrwUiState
import com.example.ui.viewmodel.RtrwViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    uiState: RtrwUiState,
    viewModel: RtrwViewModel,
    modifier: Modifier = Modifier
) {
    var phoneNumber by remember { mutableStateOf("") }
    var otpCode by remember { mutableStateOf("") }

    // State untuk Formulir Pendaftaran Warga Lengkap
    var nama by remember { mutableStateOf("") }
    var nik by remember { mutableStateOf("") }
    var noKk by remember { mutableStateOf("") }
    var rt by remember { mutableStateOf("RT 01") }
    var rw by remember { mutableStateOf("RW 01") }
    var alamat by remember { mutableStateOf("") }
    var pekerjaan by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("Warga") }
    var roleExpanded by remember { mutableStateOf(false) }

    val isOtpStep = uiState.authMode == "OTP_VERIFY"
    val isRegister = uiState.authMode == "REGISTER"

    val context = androidx.compose.ui.platform.LocalContext.current
    val activity = context as? android.app.Activity

    var resendCountdown by remember { mutableIntStateOf(60) }
    var isCountdownActive by remember { mutableStateOf(false) }

    LaunchedEffect(isOtpStep) {
        if (isOtpStep) {
            resendCountdown = 60
            isCountdownActive = true
            while (resendCountdown > 0) {
                delay(1000)
                resendCountdown--
            }
            isCountdownActive = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Mewah dengan Gradien Hijau Botani RuangWarga
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .clip(RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(PrimaryGreenDark, PrimaryGreen, AccentGreenDark)
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.22f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🌿", fontSize = 34.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "RuangWarga",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        letterSpacing = 0.5.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Satu Pintu Layanan Digital RT & RW",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = PrimaryGreenLight
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.Black.copy(alpha = 0.18f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "Autentikasi Aman No. HP & OTP",
                                fontSize = 10.sp,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Card Konten
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Segmented Switcher Tab (Hanya tampil jika tidak sedang di halaman OTP)
                if (!isOtpStep) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFFE2E8F0),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(4.dp)
                        ) {
                            // Tab Masuk Cepat
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (!isRegister) Color.White else Color.Transparent)
                                    .clickable { viewModel.setAuthMode("LOGIN") }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "📱 Masuk No. HP",
                                    fontSize = 12.5.sp,
                                    fontWeight = if (!isRegister) FontWeight.Bold else FontWeight.Medium,
                                    color = if (!isRegister) PrimaryGreenDark else TextSecondary
                                )
                            }

                            // Tab Daftar Baru
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isRegister) Color.White else Color.Transparent)
                                    .clickable { viewModel.setAuthMode("REGISTER") }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "✍️ Daftar Warga",
                                    fontSize = 12.5.sp,
                                    fontWeight = if (isRegister) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isRegister) PrimaryGreenDark else TextSecondary
                                )
                            }
                        }
                    }
                }

                // Error Message Alert
                AnimatedVisibility(visible = uiState.authErrorMessage != null) {
                    uiState.authErrorMessage?.let { errMsg ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 14.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEE2E2)),
                            border = BorderStroke(1.dp, Color(0xFFFCA5A5))
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "⚠️", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = errMsg,
                                    fontSize = 12.sp,
                                    color = Color(0xFF991B1B),
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }

                // Card Formulir
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, BorderLight),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        if (isOtpStep) {
                            // ==========================================
                            // 🔢 TAHAP VERIFIKASI KODE OTP 6 DIGIT
                            // ==========================================
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                IconButton(
                                    onClick = { viewModel.setAuthMode("LOGIN") },
                                    modifier = Modifier.size(30.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Kembali",
                                        tint = PrimaryGreenDark
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = "Verifikasi Kode OTP",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "Kode dikirim ke: $phoneNumber",
                                        fontSize = 11.5.sp,
                                        color = TextSecondary
                                    )
                                }
                            }

                            // Banner Informasi Demo Code
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFF0FDF4),
                                border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "💡", fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Masukkan 6 digit kode OTP yang tertera atau gunakan kode instan '123456' untuk kemudahan uji coba.",
                                        fontSize = 11.5.sp,
                                        color = Color(0xFF166534),
                                        lineHeight = 16.sp
                                    )
                                }
                            }

                            OutlinedTextField(
                                value = otpCode,
                                onValueChange = { if (it.length <= 6) otpCode = it },
                                placeholder = { Text("0 0 0 0 0 0", textAlign = TextAlign.Center) },
                                leadingIcon = {
                                    Icon(Icons.Default.Key, contentDescription = null, tint = PrimaryGreenDark)
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryGreenDark,
                                    unfocusedBorderColor = BorderLight
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            // Countdown & Tombol Kirim Ulang
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isCountdownActive) "Kirim ulang dlm ${resendCountdown}s" else "Belum menerima kode?",
                                    fontSize = 11.5.sp,
                                    color = TextSecondary
                                )
                                Text(
                                    text = "Kirim Ulang OTP",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (!isCountdownActive) PrimaryGreenDark else Color.Gray,
                                    modifier = Modifier.clickable(enabled = !isCountdownActive) {
                                        if (activity != null) {
                                            viewModel.requestOtp(activity, phoneNumber)
                                        }
                                    }
                                )
                            }

                            Button(
                                onClick = { viewModel.verifyOtp(phoneNumber, otpCode) },
                                enabled = !uiState.isAuthLoading && otpCode.length == 6,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PrimaryGreenDark,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                            ) {
                                if (uiState.isAuthLoading) {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        modifier = Modifier.size(20.dp),
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Text(
                                        text = "Verifikasi & Masuk ke Aplikasi",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        } else if (!isRegister) {
                            // ==========================================
                            // 📱 TAHAP 1: MASUK INSTAN DENGAN NO HP
                            // ==========================================
                            Text(
                                text = "Masuk Cepat dengan No. HP",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )

                            Text(
                                text = "Cukup masukkan nomor WhatsApp / HP Anda. Kami akan mengirimkan 6 digit kode OTP verifikasi tanpa perlu repot menghafal sandi.",
                                fontSize = 12.sp,
                                color = TextSecondary,
                                lineHeight = 17.sp
                            )

                            OutlinedTextField(
                                value = phoneNumber,
                                onValueChange = { phoneNumber = it },
                                label = { Text("Nomor WhatsApp / HP") },
                                placeholder = { Text("Contoh: 081234567890") },
                                leadingIcon = {
                                    Icon(Icons.Default.Phone, contentDescription = null, tint = PrimaryGreenDark)
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryGreenDark,
                                    unfocusedBorderColor = BorderLight
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Button(
                                onClick = { 
                                    if (activity != null) {
                                        viewModel.requestOtp(activity, phoneNumber) 
                                    }
                                },
                                enabled = !uiState.isAuthLoading && phoneNumber.length >= 9,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PrimaryGreenDark,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                            ) {
                                if (uiState.isAuthLoading) {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        modifier = Modifier.size(20.dp),
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Text(
                                        text = "Kirim Kode OTP →",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        } else {
                            // ==========================================
                            // ✍️ TAHAP 2: DAFTAR WARGA LENGKAP
                            // ==========================================
                            Text(
                                text = "Pendaftaran Warga Baru",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )

                            // Nama Lengkap Sesuai KTP
                            OutlinedTextField(
                                value = nama,
                                onValueChange = { nama = it },
                                label = { Text("Nama Lengkap (Sesuai KTP)") },
                                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = PrimaryGreenDark) },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryGreenDark,
                                    unfocusedBorderColor = BorderLight
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            // NIK (16 Digit)
                            OutlinedTextField(
                                value = nik,
                                onValueChange = { if (it.length <= 16 && it.all { c -> c.isDigit() }) nik = it },
                                label = { Text("NIK (16 Digit)") },
                                placeholder = { Text("3201xxxxxxxxxxxx") },
                                leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null, tint = PrimaryGreenDark) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryGreenDark,
                                    unfocusedBorderColor = BorderLight
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            // No KK (16 Digit)
                            OutlinedTextField(
                                value = noKk,
                                onValueChange = { if (it.length <= 16 && it.all { c -> c.isDigit() }) noKk = it },
                                label = { Text("No. Kartu Keluarga (KK)") },
                                placeholder = { Text("3201xxxxxxxxxxxx") },
                                leadingIcon = { Icon(Icons.Default.Home, contentDescription = null, tint = PrimaryGreenDark) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryGreenDark,
                                    unfocusedBorderColor = BorderLight
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            // Nomor HP / WhatsApp
                            OutlinedTextField(
                                value = phoneNumber,
                                onValueChange = { phoneNumber = it },
                                label = { Text("Nomor HP / WhatsApp Aktif") },
                                placeholder = { Text("081234567890") },
                                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = PrimaryGreenDark) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryGreenDark,
                                    unfocusedBorderColor = BorderLight
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            // RT & RW
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedTextField(
                                    value = rt,
                                    onValueChange = { rt = it },
                                    label = { Text("RT") },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = PrimaryGreenDark,
                                        unfocusedBorderColor = BorderLight
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = rw,
                                    onValueChange = { rw = it },
                                    label = { Text("RW") },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = PrimaryGreenDark,
                                        unfocusedBorderColor = BorderLight
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            // Alamat Rumah
                            OutlinedTextField(
                                value = alamat,
                                onValueChange = { alamat = it },
                                label = { Text("Alamat Rumah / Blok") },
                                leadingIcon = { Icon(Icons.Default.Apartment, contentDescription = null, tint = PrimaryGreenDark) },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryGreenDark,
                                    unfocusedBorderColor = BorderLight
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            // Pekerjaan
                            OutlinedTextField(
                                value = pekerjaan,
                                onValueChange = { pekerjaan = it },
                                label = { Text("Pekerjaan / Profesi") },
                                leadingIcon = { Icon(Icons.Default.Work, contentDescription = null, tint = PrimaryGreenDark) },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryGreenDark,
                                    unfocusedBorderColor = BorderLight
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            // Pilihan Peran/Jabatan
                            ExposedDropdownMenuBox(
                                expanded = roleExpanded,
                                onExpandedChange = { roleExpanded = !roleExpanded },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = role,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Peran / Jabatan di RT/RW") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = roleExpanded) },
                                    leadingIcon = { Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = PrimaryGreenDark) },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = PrimaryGreenDark,
                                        unfocusedBorderColor = BorderLight
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                                )
                                ExposedDropdownMenu(
                                    expanded = roleExpanded,
                                    onDismissRequest = { roleExpanded = false }
                                ) {
                                    listOf("Warga", "Ketua RT", "Ketua RW", "Sekretaris RT/RW", "Bendahara RT/RW", "Seksi Keamanan").forEach { r ->
                                        DropdownMenuItem(
                                            text = { Text(r) },
                                            onClick = {
                                                role = r
                                                roleExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            // Tombol Submit Pendaftaran
                            Button(
                                onClick = {
                                    if (activity != null) {
                                        viewModel.registerFullProfile(
                                            activity = activity,
                                            nama = nama,
                                            nik = nik,
                                            noKk = noKk,
                                            telepon = phoneNumber,
                                            rt = rt,
                                            rw = rw,
                                            alamat = alamat,
                                            pekerjaan = pekerjaan,
                                            role = role
                                        )
                                    }
                                },
                                enabled = !uiState.isAuthLoading && nama.isNotBlank() && phoneNumber.length >= 9 && nik.length == 16,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PrimaryGreenDark,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                            ) {
                                if (uiState.isAuthLoading) {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        modifier = Modifier.size(20.dp),
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Text(
                                        text = "Daftar & Verifikasi No. HP →",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Footer Helper Link
                if (!isOtpStep) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (isRegister) "Sudah pernah mendaftar?" else "Belum terdaftar sebagai warga?",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = if (isRegister) "Masuk dengan No. HP" else "Daftar Warga Baru",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryGreenDark,
                            modifier = Modifier.clickable {
                                viewModel.setAuthMode(if (isRegister) "LOGIN" else "REGISTER")
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
