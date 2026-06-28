package com.cleanshield.app.ui.firepact

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cleanshield.app.ui.theme.CleanShieldColors

@Composable
fun FirePactScreen(
    onNavigateBack: () -> Unit
) {
    var pactAmount by remember { mutableStateOf("") }
    var isPactActive by remember { mutableStateOf(false) }
    var selectedCharity by remember { mutableStateOf("Cruz Roja") }
    var showConfirmDialog by remember { mutableStateOf(false) }

    val charities = listOf(
        "Cruz Roja",
        "UNICEF",
        "Médicos Sin Fronteras",
        "Cáritas",
        "Banco de Alimentos"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CleanShieldColors.DarkBackground)
    ) {
        SmallTopAppBar(
            title = { Text("Pacto de Fuego", fontWeight = FontWeight.Bold) },
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
            // Header
            Text("🔥", fontSize = 64.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "PACTO DE FUEGO",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = CleanShieldColors.AccentOrange
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Pon dinero real en juego.\nSi recaes, se dona a caridad.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = Color.White.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (!isPactActive) {
                // Setup pact
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = CleanShieldColors.CardBackground
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            "Configura tu pacto",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Amount
                        OutlinedTextField(
                            value = pactAmount,
                            onValueChange = { pactAmount = it },
                            label = { Text("Cantidad a arriesgar (€)") },
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = { Text("€", color = CleanShieldColors.AccentOrange, fontSize = 20.sp) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CleanShieldColors.AccentOrange,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedLabelColor = CleanShieldColors.AccentOrange,
                                unfocusedLabelColor = Color.White.copy(alpha = 0.5f)
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Charity selection
                        Text(
                            "Si recaes, el dinero va a:",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        charities.forEach { charity ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedCharity == charity,
                                    onClick = { selectedCharity = charity },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = CleanShieldColors.AccentOrange
                                    )
                                )
                                Text(
                                    charity,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.White,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Warning
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = CleanShieldColors.AccentRed.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            Icons.Default.Warning,
                            null,
                            tint = CleanShieldColors.AccentRed,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Este pacto es un compromiso moral contigo mismo. La presión de perder dinero real activa tu cerebro de forma diferente que una simple promesa.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { showConfirmDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CleanShieldColors.AccentOrange
                    ),
                    shape = RoundedCornerShape(16.dp),
                    enabled = pactAmount.isNotBlank()
                ) {
                    Icon(Icons.Default.LocalFireDepartment, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "ACTIVAR PACTO DE FUEGO",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            } else {
                // Active pact display
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = CleanShieldColors.AccentOrange.copy(alpha = 0.15f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🔥", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "PACTO ACTIVO",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = CleanShieldColors.AccentOrange
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "€$pactAmount",
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            "en juego para $selectedCharity",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(color = Color.White.copy(alpha = 0.2f))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Cada día limpio es una victoria.\nSigue así, guerrero.",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = CleanShieldColors.GreenPrimary
                        )
                    }
                }
            }
        }
    }

    // Confirmation dialog
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            containerColor = CleanShieldColors.CardBackground,
            title = {
                Text("¿Activar Pacto de Fuego?", color = Color.White)
            },
            text = {
                Text(
                    "Estás comprometiéndote a donar €$pactAmount a $selectedCharity si recaes. ¿Estás seguro?",
                    color = Color.White.copy(alpha = 0.8f)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        isPactActive = true
                        showConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CleanShieldColors.AccentOrange
                    )
                ) {
                    Text("Sí, lo juro")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancelar", color = Color.White.copy(alpha = 0.7f))
                }
            }
        )
    }
}
