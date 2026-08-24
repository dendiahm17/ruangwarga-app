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
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentRed
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.BorderLight
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.PrimaryGreenDark
import com.example.ui.theme.PrimaryGreenLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.RtrwUiState
import com.example.ui.viewmodel.RtrwViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    uiState: RtrwUiState,
    viewModel: RtrwViewModel,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Form fields untuk registrasi
    var nama by remember { mutableStateOf("") }
    var nik by remember { mutableStateOf("") }
    var noKk by remember { mutableStateOf("") }
    var telepon by remember { mutableStateOf("") }
    var rt by remember { mutableStateOf("RT 01") }
    var rw by remember { mutableStateOf("RW 01") }
    var alamat by remember { mutableStateOf("") }
    var pekerjaan by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("Warga") }
    var roleExpanded by remember { mutableStateOf(false) }

    val isRegister = uiState.authMode == "REGISTER"
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Logo & Header Brand
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(PrimaryGreen, PrimaryGreenDark)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🌿", fontSize = 34.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "RuangWarga",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = PrimaryGreenDark
            )

            Text(
                text = if (isRegister) "Pendaftaran Akun Warga & Pengurus" else "Sistem Pelayanan & Komunikasi Warga",
                fontSize = 12.5.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Tab Switcher (Masuk / Daftar)
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFE2E8F0),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(4.dp)) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (!isRegister) Color.White else Color.Transparent,
                        shadowElevation = if (!isRegister) 2.dp else 0.dp,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.setAuthMode("LOGIN") }
                    ) {
                        Text(
                            text = "Masuk",
                            fontSize = 13.sp,
                            fontWeight = if (!isRegister) FontWeight.Bold else FontWeight.Medium,
                            color = if (!isRegister) PrimaryGreenDark else TextSecondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isRegister) Color.White else Color.Transparent,
                        shadowElevation = if (isRegister) 2.dp else 0.dp,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.setAuthMode("REGISTER") }
                    ) {
                        Text(
                            text = "Daftar Akun",
                            fontSize = 13.sp,
                            fontWeight = if (isRegister) FontWeight.Bold else FontWeight.Medium,
                            color = if (isRegister) PrimaryGreenDark else TextSecondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Error Message Box
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

            // Input Form Fields
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderLight)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (isRegister) {
                        // Nama Lengkap
                        OutlinedTextField(
                            value = nama,
                            onValueChange = { nama = it },
                            label = { Text("Nama Lengkap") },
                            placeholder = { Text("Contoh: Budi Santoso") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = PrimaryGreenDark) },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryGreenDark,
                                unfocusedBorderColor = BorderLight
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        // NIK & No KK
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = nik,
                                onValueChange = { if (it.length <= 16) nik = it },
                                label = { Text("NIK (16 digit)") },
                                placeholder = { Text("3275...") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryGreenDark,
                                    unfocusedBorderColor = BorderLight
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = noKk,
                                onValueChange = { if (it.length <= 16) noKk = it },
                                label = { Text("No. KK") },
                                placeholder = { Text("3275...") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryGreenDark,
                                    unfocusedBorderColor = BorderLight
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // No Telepon / WA
                        OutlinedTextField(
                            value = telepon,
                            onValueChange = { telepon = it },
                            label = { Text("Nomor WhatsApp / HP") },
                            placeholder = { Text("0812-xxxx-xxxx") },
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

                        // RT / RW Selection
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = rt,
                                onValueChange = { rt = it },
                                label = { Text("RT") },
                                placeholder = { Text("RT 01") },
                                singleLine = true,
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
                                placeholder = { Text("RW 01") },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryGreenDark,
                                    unfocusedBorderColor = BorderLight
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // Alamat Lengkap
                        OutlinedTextField(
                            value = alamat,
                            onValueChange = { alamat = it },
                            label = { Text("Alamat Rumah (Blok/Nomor)") },
                            placeholder = { Text("Jl. Melati Blok C No. 12") },
                            leadingIcon = { Icon(Icons.Default.Home, contentDescription = null, tint = PrimaryGreenDark) },
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
                            label = { Text("Pekerjaan") },
                            placeholder = { Text("Wiraswasta / Karyawan / PNS") },
                            leadingIcon = { Icon(Icons.Default.Work, contentDescription = null, tint = PrimaryGreenDark) },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryGreenDark,
                                unfocusedBorderColor = BorderLight
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Pilihan Peran (Warga / Pengurus)
                        ExposedDropdownMenuBox(
                            expanded = roleExpanded,
                            onExpandedChange = { roleExpanded = !roleExpanded },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = role,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Peran / Jabatan") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = roleExpanded) },
                                leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null, tint = PrimaryGreenDark) },
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryGreenDark,
                                    unfocusedBorderColor = BorderLight
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
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
                    }

                    // Email Field
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Alamat Email") },
                        placeholder = { Text("nama@email.com") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = PrimaryGreenDark) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryGreenDark,
                            unfocusedBorderColor = BorderLight
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Password Field
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Kata Sandi") },
                        placeholder = { Text("Minimal 6 karakter") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = PrimaryGreenDark) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = null,
                                    tint = TextSecondary
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryGreenDark,
                            unfocusedBorderColor = BorderLight
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Action Button (Masuk / Daftar)
                    Button(
                        onClick = {
                            if (isRegister) {
                                viewModel.register(
                                    email = email,
                                    pass = password,
                                    nama = nama,
                                    nik = nik,
                                    noKk = noKk,
                                    telepon = telepon,
                                    rt = rt,
                                    rw = rw,
                                    alamat = alamat,
                                    pekerjaan = pekerjaan,
                                    role = role
                                )
                            } else {
                                viewModel.login(email = email, pass = password)
                            }
                        },
                        enabled = !uiState.isAuthLoading,
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
                                text = if (isRegister) "Daftar Akun Sekarang" else "Masuk ke Aplikasi",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Footer / Switch Helper
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (isRegister) "Sudah punya akun?" else "Belum memiliki akun warga?",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isRegister) "Masuk di sini" else "Daftar Akun Baru",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreenDark,
                    modifier = Modifier.clickable {
                        viewModel.setAuthMode(if (isRegister) "LOGIN" else "REGISTER")
                    }
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
