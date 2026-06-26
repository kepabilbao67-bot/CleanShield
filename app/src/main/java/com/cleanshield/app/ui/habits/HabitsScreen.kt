package com.cleanshield.app.ui.habits

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val DarkBg = Color(0xFF0F1724)
private val CardBg = Color(0xFF1A2332)
private val AccentGreen = Color(0xFF66BB6A)
private val AccentBlue = Color(0xFF4FC3F7)
private val AccentGold = Color(0xFFFFD54F)

data class Habit(
    val id: Int,
    val emoji: String,
    val name: String,
    val description: String,
    val color: Color,
    val weekLog: List<Boolean> // 7 days: Mon-Sun
)

private val defaultHabits = listOf(
    Habit(
        id = 1,
        emoji = "\uD83C\uDFCB\uFE0F",
        name = "Ejercicio",
        description = "Mínimo 30 minutos de actividad física",
        color = Color(0xFFEF5350),
        weekLog = listOf(true, true, false, true, false, true, false)
    ),
    Habit(
        id = 2,
        emoji = "\uD83E\uDDD8",
        name = "Meditación",
        description = "Al menos 5 minutos de meditación consciente",
        color = Color(0xFFAB47BC),
        weekLog = listOf(true, false, true, true, true, false, false)
    ),
    Habit(
        id = 3,
        emoji = "\uD83D\uDCDA",
        name = "Lectura",
        description = "Leer al menos 20 páginas",
        color = Color(0xFF42A5F5),
        weekLog = listOf(false, true, true, false, true, true, false)
    ),
    Habit(
        id = 4,
        emoji = "\uD83D\uDE34",
        name = "Dormir bien",
        description = "Acostarse antes de las 23:00",
        color = Color(0xFF5C6BC0),
        weekLog = listOf(true, true, true, false, true, false, false)
    ),
    Habit(
        id = 5,
        emoji = "\uD83E\uDD1D",
        name = "Conexión social",
        description = "Hablar con alguien significativo",
        color = Color(0xFF66BB6A),
        weekLog = listOf(true, false, false, true, false, true, true)
    ),
    Habit(
        id = 6,
        emoji = "\uD83D\uDCA7",
        name = "Agua",
        description = "Beber al menos 2 litros de agua",
        color = Color(0xFF4FC3F7),
        weekLog = listOf(true, true, true, true, true, true, false)
    ),
    Habit(
        id = 7,
        emoji = "\u270D\uFE0F",
        name = "Escritura",
        description = "Escribir en diario o reflexiones",
        color = Color(0xFFFF9800),
        weekLog = listOf(false, true, false, true, false, false, false)
    )
)

@Composable
fun HabitsScreen(
    onBack: () -> Unit = {}
) {
    var habits by remember { mutableStateOf(defaultHabits) }

    val todayIndex = 3 // Thursday as example (0-indexed Mon-Sun)
    val todayCompleted = habits.count { it.weekLog[todayIndex] }
    val todayTotal = habits.size
    val todayProgress = todayCompleted.toFloat() / todayTotal.toFloat()

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
                text = "Hábitos Saludables",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Daily progress card
            item {
                DailyProgressCard(
                    completed = todayCompleted,
                    total = todayTotal,
                    progress = todayProgress
                )
            }

            // Insight card
            item {
                InsightCard()
            }

            // Habits list
            item {
                Text(
                    text = "Mis hábitos",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(habits) { habit ->
                HabitItem(
                    habit = habit,
                    todayIndex = todayIndex,
                    onToggleToday = { habitId ->
                        habits = habits.map { h ->
                            if (h.id == habitId) {
                                val newLog = h.weekLog.toMutableList()
                                newLog[todayIndex] = !newLog[todayIndex]
                                h.copy(weekLog = newLog)
                            } else h
                        }
                    }
                )
            }

            // Science card
            item {
                Spacer(modifier = Modifier.height(8.dp))
                ScienceCard()
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
private fun DailyProgressCard(
    completed: Int,
    total: Int,
    progress: Float
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            AccentGreen.copy(alpha = 0.2f),
                            CardBg,
                            AccentBlue.copy(alpha = 0.1f)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "\uD83D\uDCC8 Progreso de hoy",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Circular progress representation
                Text(
                    text = "$completed/$total",
                    color = AccentGreen,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "hábitos completados",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    color = AccentGreen,
                    trackColor = Color.White.copy(alpha = 0.15f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${(progress * 100).toInt()}% completado",
                    color = AccentGreen,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun InsightCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardBg),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "\uD83D\uDCA1", fontSize = 28.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Insight",
                    color = AccentGold,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Los días que haces ejercicio, tienes un 73% menos de tentaciones.",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
private fun HabitItem(
    habit: Habit,
    todayIndex: Int,
    onToggleToday: (Int) -> Unit
) {
    val isCompletedToday = habit.weekLog[todayIndex]
    val weekDays = listOf("L", "M", "X", "J", "V", "S", "D")

    Card(
        colors = CardDefaults.cardColors(containerColor = CardBg),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circular checkbox
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        if (isCompletedToday) habit.color.copy(alpha = 0.2f)
                        else Color.White.copy(alpha = 0.05f)
                    )
                    .clickable { onToggleToday(habit.id) },
                contentAlignment = Alignment.Center
            ) {
                if (isCompletedToday) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "Completado",
                        tint = habit.color,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(text = habit.emoji, fontSize = 22.sp)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Name and description
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${habit.emoji} ${habit.name}",
                    color = if (isCompletedToday) Color.White else Color.White.copy(alpha = 0.8f),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = habit.description,
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 12.sp
                )

                // Mini week view (7 dots)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    habit.weekLog.forEachIndexed { index, completed ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = weekDays[index],
                                color = Color.White.copy(alpha = 0.4f),
                                fontSize = 9.sp
                            )
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            completed -> habit.color
                                            index == todayIndex -> habit.color.copy(alpha = 0.3f)
                                            else -> Color.White.copy(alpha = 0.1f)
                                        }
                                    )
                            )
                        }
                    }
                }
            }

            // Weekly count
            val weekCount = habit.weekLog.count { it }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$weekCount/7",
                    color = habit.color,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "semana",
                    color = Color.White.copy(alpha = 0.4f),
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
private fun ScienceCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardBg),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "\uD83E\uDDE0", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Neurociencia del reemplazo",
                    color = AccentBlue,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Tu cerebro no puede simplemente 'eliminar' un hábito. Los circuitos neuronales de " +
                        "la adicción persisten. La clave es crear NUEVOS circuitos más fuertes que compitan " +
                        "por los mismos recursos neurales.",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Cada hábito saludable que repites fortalece vías de dopamina alternativas. " +
                        "Después de 66 días (promedio), el nuevo comportamiento se vuelve automático. " +
                        "Los ganglios basales lo registran como 'lo normal'.",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = AccentGreen.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "\uD83D\uDCA1 Consejo: Vincula cada hábito a un momento fijo del día. " +
                            "La consistencia temporal acelera la automatización cerebral.",
                    color = AccentGreen,
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}
