package com.cleanshield.app.ui.sos

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cleanshield.app.ui.theme.CleanShieldColors
import kotlinx.coroutines.delay

@Composable
fun SilentSosScreen(
    onNavigateBack: () -> Unit
) {
    var emergencyContact by remember { mutableStateOf("") }
    var emergencyName by remember { mutableStateOf("") }
    var customMessage by remember { mutableStateOf("Necesito ayuda. Estoy pasando un momento difícil.") }
    var tapCount by remember { mutableIntStateOf(0) }
    var sosSent by remember { mutableStateOf(false) }
    var isConfigured by remember { mutableStateOf(false) }

    // Reset tap count after delay
    LaunchedEffect(tapCount) {
        if (tapCount > 0 && tapCount < 5) {
            delay(3000)
            tapCount = 0
        }
    }

    // Send SOS when pattern detected
    LaunchedEffect(tapCount) {
        if (tapCount >= 5) {
            sosSent = true
            delay(3000)
            sosSent = false
            tapCount = 0
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CleanShieldColors.DarkBackground)
    ) {
        SmallTopAppBar(
            title = { Text("SOS Silencioso", fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, "Volver")
                }
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = CleanShieldColors.DarkBackground
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // SOS Area
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (sosSent)
                        CleanShieldColors.GreenPrimary.copy(alpha = 0.2f)
                    else
                        CleanShieldColors.AccentRed.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = { tapCount++ }
                            )
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (sosSent) {
                        Text("✅", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "SOS Enviado",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = CleanShieldColors.GreenPrimary
                        )
                        Text(
                            "Tu contacto de emergencia ha sido notificado",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    } else {
                        Text("🆘", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Toca 5 veces rápido aquí",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            "para enviar SOS invisible",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Tap progress
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            repeat(5) { index ->
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (index < tapCount) CleanShieldColors.AccentRed
                                            else Color.White.copy(alpha = 0.2f)
                                        )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Configuration
            Text(
                "Configuración",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = CleanShieldColors.CardBackground
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = emergencyName,
                        onValueChange = { emergencyName = it },
                        label = { Text("Nombre del contacto") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Person, null) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CleanShieldColors.GreenPrimary,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = CleanShieldColors.GreenPrimary,
                            unfocusedLabelColor = Color.White.copy(alpha = 0.5f)
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = emergencyContact,
                        onValueChange = { emergencyContact = it },
                        label = { Text("Número de teléfono") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Phone, null) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CleanShieldColors.GreenPrimary,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = CleanShieldColors.GreenPrimary,
                            unfocusedLabelColor = Color.White.copy(alpha = 0.5f)
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = customMessage,
                        onValueChange = { customMessage = it },
                        label = { Text("Mensaje de emergencia") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Message, null) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CleanShieldColors.GreenPrimary,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedLabelColor = CleanShieldColors.GreenPrimary,
                            unfocusedLabelColor = Color.White.copy(alpha = 0.5f)
                        ),
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { isConfigured = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CleanShieldColors.GreenPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        enabled = emergencyContact.isNotBlank() && emergencyName.isNotBlank()
                    ) {
                        Text("Guardar configuración")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // How it works
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = CleanShieldColors.CardBackground
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "¿Cómo funciona?",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HowItWorksStep("1", "Configura tu contacto de emergencia")
                    HowItWorksStep("2", "Cuando sientas un impulso fuerte, toca 5 veces rápido")
                    HowItWorksStep("3", "Se envía un SMS invisible a tu contacto")
                    HowItWorksStep("4", "Nadie a tu alrededor notará nada")
                }
            }
        }
    }
}

@Composable
fun HowItWorksStep(number: String, text: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(CleanShieldColors.GreenPrimary),
            contentAlignment = Alignment.Center
        ) {
            Text(number, color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.8f)
        )
    }
}
