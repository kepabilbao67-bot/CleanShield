package com.cleanshield.app.ui.journal

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
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

private val DarkBg = Color(0xFF0F1724)
private val CardBg = Color(0xFF1A2332)
private val AccentBlue = Color(0xFF4FC3F7)
private val AccentGreen = Color(0xFF66BB6A)
private val AccentOrange = Color(0xFFFF9800)
private val AccentRed = Color(0xFFEF5350)

data class Mood(
    val emoji: String,
    val name: String,
    val color: Color
)

data class Trigger(
    val emoji: String,
    val name: String
)

data class JournalEntry(
    val id: Int,
    val mood: Mood,
    val triggers: List<Trigger>,
    val hadUrge: Boolean,
    val resisted: Boolean,
    val note: String,
    val dayOfWeek: String,
    val date: String
)

private val moods = listOf(
    Mood("\uD83D\uDE0A", "Feliz", Color(0xFF66BB6A)),
    Mood("\uD83D\uDE10", "Neutral", Color(0xFFBDBDBD)),
    Mood("\uD83D\uDE22", "Triste", Color(0xFF42A5F5)),
    Mood("\uD83D\uDE24", "Frustrado", Color(0xFFFF7043)),
    Mood("\uD83D\uDE30", "Ansioso", Color(0xFFAB47BC)),
    Mood("\uD83D\uDE34", "Aburrido", Color(0xFF78909C)),
    Mood("\uD83D\uDE14", "Solo", Color(0xFF5C6BC0)),
    Mood("\uD83D\uDE21", "Enojado", Color(0xFFEF5350))
)

private val triggers = listOf(
    Trigger("\uD83D\uDCF1", "Teléfono"),
    Trigger("\uD83D\uDECF\uFE0F", "Cama"),
    Trigger("\uD83C\uDF19", "Noche"),
    Trigger("\uD83D\uDC94", "Rechazo"),
    Trigger("\uD83D\uDCBC", "Estrés laboral"),
    Trigger("\uD83C\uDF7A", "Alcohol"),
    Trigger("\uD83D\uDEB6", "Soledad"),
    Trigger("\uD83D\uDC41\uFE0F", "Vi algo")
)

@Composable
fun JournalScreen(
    onBack: () -> Unit = {}
) {
    var showNewEntryDialog by remember { mutableStateOf(false) }
    var entries by remember {
        mutableStateOf(
            listOf(
                JournalEntry(1, moods[0], listOf(), false, false, "Buen día, me sentí productivo.", "Lun", "2024-01-15"),
                JournalEntry(2, moods[4], listOf(triggers[2], triggers[5]), true, true, "Noche difícil pero resistí.", "Mar", "2024-01-16"),
                JournalEntry(3, moods[1], listOf(), false, false, "", "Mié", "2024-01-17")
            )
        )
    }

    if (showNewEntryDialog) {
        NewEntryDialog(
            onDismiss = { showNewEntryDialog = false },
            onSave = { entry ->
                entries = listOf(entry) + entries
                showNewEntryDialog = false
            },
            nextId = entries.size + 1
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, "Volver", tint = Color.White)
            }
            Text(
                text = "Diario Emocional",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            FloatingActionButton(
                onClick = { showNewEntryDialog = true },
                containerColor = AccentBlue,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(Icons.Default.Add, "Nueva entrada", tint = Color.White)
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Weekly mood tracker
            item {
                WeeklyMoodTracker(entries = entries)
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Entradas recientes",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Recent entries
            items(entries) { entry ->
                JournalEntryCard(entry = entry)
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun WeeklyMoodTracker(entries: List<JournalEntry>) {
    val daysOfWeek = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")

    Card(
        colors = CardDefaults.cardColors(containerColor = CardBg),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "\uD83D\uDCC5 Esta semana",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                daysOfWeek.forEachIndexed { index, day ->
                    val matchingEntry = entries.find { it.dayOfWeek == day }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = day,
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(
                                    if (matchingEntry != null) matchingEntry.mood.color.copy(alpha = 0.2f)
                                    else Color.White.copy(alpha = 0.05f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = matchingEntry?.mood?.emoji ?: "·",
                                fontSize = if (matchingEntry != null) 18.sp else 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun JournalEntryCard(entry: JournalEntry) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardBg),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = entry.mood.emoji, fontSize = 28.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = entry.mood.name,
                        color = entry.mood.color,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = entry.date,
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 12.sp
                    )
                }
                if (entry.hadUrge) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (entry.resisted) AccentGreen.copy(alpha = 0.2f)
                            else AccentRed.copy(alpha = 0.2f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (entry.resisted) "\u2705 Resistió" else "\u26A0\uFE0F Urgencia",
                            color = if (entry.resisted) AccentGreen else AccentRed,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            if (entry.triggers.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    entry.triggers.forEach { trigger ->
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White.copy(alpha = 0.08f)
                            ),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "${trigger.emoji} ${trigger.name}",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }

            if (entry.note.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = entry.note,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
private fun NewEntryDialog(
    onDismiss: () -> Unit,
    onSave: (JournalEntry) -> Unit,
    nextId: Int
) {
    var selectedMood by remember { mutableStateOf<Mood?>(null) }
    var selectedTriggers by remember { mutableStateOf<Set<Trigger>>(emptySet()) }
    var hadUrge by remember { mutableStateOf(false) }
    var resisted by remember { mutableStateOf(false) }
    var noteText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CardBg,
        title = {
            Text(
                text = "\u00BFCómo te sientes?",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Mood selection
                item {
                    Text(
                        text = "Estado de ánimo",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        for (row in moods.chunked(4)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                row.forEach { mood ->
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { selectedMood = mood }
                                            .background(
                                                if (selectedMood == mood) mood.color.copy(alpha = 0.2f)
                                                else Color.Transparent
                                            )
                                            .padding(8.dp)
                                    ) {
                                        Text(text = mood.emoji, fontSize = 24.sp)
                                        Text(
                                            text = mood.name,
                                            color = Color.White.copy(alpha = 0.7f),
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Trigger selection
                item {
                    Text(
                        text = "Disparadores (opcional)",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        for (row in triggers.chunked(4)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                row.forEach { trigger ->
                                    val isSelected = trigger in selectedTriggers
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable {
                                                selectedTriggers = if (isSelected) {
                                                    selectedTriggers - trigger
                                                } else {
                                                    selectedTriggers + trigger
                                                }
                                            }
                                            .background(
                                                if (isSelected) AccentOrange.copy(alpha = 0.2f)
                                                else Color.Transparent
                                            )
                                            .border(
                                                width = if (isSelected) 1.dp else 0.dp,
                                                color = if (isSelected) AccentOrange else Color.Transparent,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .padding(8.dp)
                                    ) {
                                        Text(text = trigger.emoji, fontSize = 20.sp)
                                        Text(
                                            text = trigger.name,
                                            color = Color.White.copy(alpha = 0.7f),
                                            fontSize = 9.sp,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Urge checkboxes
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = hadUrge,
                            onCheckedChange = { hadUrge = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = AccentOrange,
                                uncheckedColor = Color.White.copy(alpha = 0.5f)
                            )
                        )
                        Text(
                            text = "Tuve urgencia",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                    if (hadUrge) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = resisted,
                                onCheckedChange = { resisted = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = AccentGreen,
                                    uncheckedColor = Color.White.copy(alpha = 0.5f)
                                )
                            )
                            Text(
                                text = "Resistí la urgencia",
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                // Free text
                item {
                    OutlinedTextField(
                        value = noteText,
                        onValueChange = { noteText = it },
                        label = { Text("Notas (opcional)", color = Color.White.copy(alpha = 0.5f)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = AccentBlue,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                            cursorColor = AccentBlue
                        ),
                        maxLines = 4,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedMood?.let { mood ->
                        onSave(
                            JournalEntry(
                                id = nextId,
                                mood = mood,
                                triggers = selectedTriggers.toList(),
                                hadUrge = hadUrge,
                                resisted = resisted,
                                note = noteText,
                                dayOfWeek = "Hoy",
                                date = "2024-01-18"
                            )
                        )
                    }
                },
                enabled = selectedMood != null,
                colors = ButtonDefaults.buttonColors(containerColor = AccentBlue)
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color.White.copy(alpha = 0.7f))
            }
        }
    )
}
