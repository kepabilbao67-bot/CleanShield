package com.cleanshield.app.ui.timesafe

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cleanshield.app.ui.theme.CleanShieldColors

data class TimeSafeLetter(
    val id: Long,
    val content: String,
    val unlockAtDays: Int,
    val createdAt: String,
    val isUnlocked: Boolean,
    val daysRemaining: Int
)

@Composable
fun TimeSafeScreen(
    onNavigateBack: () -> Unit
) {
    var showWriteDialog by remember { mutableStateOf(false) }
    var letterContent by remember { mutableStateOf("") }
    var selectedDays by remember { mutableIntStateOf(30) }

    // Sample letters - in real app these come from Room DB
    val letters = remember {
        mutableStateListOf(
            TimeSafeLetter(1, "Querido yo del futuro: si estás leyendo esto, lo lograste...", 30, "2024-01-01", false, 23),
            TimeSafeLetter(2, "Recuerda cómo te sentías el día que decidiste cambiar...", 60, "2024-01-01", false, 53),
            TimeSafeLetter(3, "Para cuando leas esto habrás demostrado que eres más fuerte...", 90, "2024-01-01", false, 83)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CleanShieldColors.DarkBackground)
    ) {
        SmallTopAppBar(
            title = { Text("Caja Fuerte del Tiempo", fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, "Volver")
                }
            },
            actions = {
                IconButton(onClick = { showWriteDialog = true }) {
                    Icon(Icons.Default.Add, "Nueva carta", tint = CleanShieldColors.GreenPrimary)
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
                .padding(16.dp)
        ) {
            // Header illustration
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = CleanShieldColors.GreenPrimary.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🔐", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Escribe cartas a tu yo del futuro",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Se desbloquean a los 30, 60 y 90 días limpios",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Tus cartas",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(12.dp))

            letters.forEach { letter ->
                LetterCard(letter)
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (letters.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("📭", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Aún no has escrito cartas",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Write new letter button
            Button(
                onClick = { showWriteDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CleanShieldColors.GreenPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Escribir nueva carta", modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }

    // Write dialog
    if (showWriteDialog) {
        AlertDialog(
            onDismissRequest = { showWriteDialog = false },
            containerColor = CleanShieldColors.CardBackground,
            title = {
                Text("Carta para tu yo del futuro", color = Color.White)
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = letterContent,
                        onValueChange = { letterContent = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        label = { Text("Escribe tu carta...") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CleanShieldColors.GreenPrimary,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Desbloquear a los:", color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf(30, 60, 90).forEach { days ->
                            FilterChip(
                                selected = selectedDays == days,
                                onClick = { selectedDays = days },
                                label = { Text("$days días") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = CleanShieldColors.GreenPrimary
                                )
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (letterContent.isNotBlank()) {
                            letters.add(
                                TimeSafeLetter(
                                    id = letters.size + 1L,
                                    content = letterContent,
                                    unlockAtDays = selectedDays,
                                    createdAt = "Hoy",
                                    isUnlocked = false,
                                    daysRemaining = selectedDays
                                )
                            )
                            letterContent = ""
                            showWriteDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CleanShieldColors.GreenPrimary
                    )
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showWriteDialog = false }) {
                    Text("Cancelar", color = Color.White.copy(alpha = 0.7f))
                }
            }
        )
    }
}

@Composable
fun LetterCard(letter: TimeSafeLetter) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = CleanShieldColors.CardBackground
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Lock icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (letter.isUnlocked) CleanShieldColors.GreenPrimary.copy(alpha = 0.2f)
                        else CleanShieldColors.AccentOrange.copy(alpha = 0.2f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    if (letter.isUnlocked) Icons.Default.LockOpen else Icons.Default.Lock,
                    contentDescription = null,
                    tint = if (letter.isUnlocked) CleanShieldColors.GreenPrimary else CleanShieldColors.AccentOrange
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Carta para los ${letter.unlockAtDays} días",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                if (letter.isUnlocked) {
                    Text(
                        letter.content.take(50) + "...",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                } else {
                    Text(
                        "Faltan ${letter.daysRemaining} días para abrir",
                        style = MaterialTheme.typography.bodySmall,
                        color = CleanShieldColors.AccentOrange
                    )
                }
            }
        }
    }
}
