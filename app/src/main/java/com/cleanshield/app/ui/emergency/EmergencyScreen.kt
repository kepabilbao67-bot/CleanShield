package com.cleanshield.app.ui.emergency

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private val DarkBlueGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFF0D1B2A), Color(0xFF1B2838), Color(0xFF0D1B2A))
)

private val AccentBlue = Color(0xFF4FC3F7)
private val AccentGreen = Color(0xFF66BB6A)
private val AccentGold = Color(0xFFFFD54F)

@Composable
fun EmergencyScreen(
    streakDays: Int = 0,
    onExit: () -> Unit = {},
    onCallTutor: () -> Unit = {}
) {
    var currentPhase by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlueGradient)
    ) {
        when (currentPhase) {
            0 -> IntroPhase(
                onStart = { currentPhase = 1 },
                onCallTutor = onCallTutor
            )
            1 -> BreathingPhase(onComplete = { currentPhase = 2 })
            2 -> ImpactPhrasesPhase(
                streakDays = streakDays,
                onComplete = { currentPhase = 3 }
            )
            3 -> ActivitiesPhase(onComplete = { currentPhase = 4 })
            4 -> VictoryPhase(onExit = onExit)
        }

        // Close button
        IconButton(
            onClick = onExit,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Cerrar",
                tint = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun IntroPhase(
    onStart: () -> Unit,
    onCallTutor: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "\uD83D\uDEE1\uFE0F",
            fontSize = 64.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Estás aquí porque eres FUERTE",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "La urgencia dura máximo 15 minutos.\nVamos a superarla juntos.",
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            lineHeight = 26.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Respira. Ya diste el primer paso al abrir esto.",
            color = AccentBlue,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onStart,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentBlue),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "COMENZAR EJERCICIO",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onCallTutor,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentGreen)
        ) {
            Icon(Icons.Default.Call, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Llamar a mi tutor",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun BreathingPhase(onComplete: () -> Unit) {
    var currentCycle by remember { mutableIntStateOf(1) }
    var currentStep by remember { mutableStateOf("inhala") }
    var secondsElapsed by remember { mutableIntStateOf(0) }
    var stepSeconds by remember { mutableIntStateOf(0) }

    val totalCycles = 4

    val scale by animateFloatAsState(
        targetValue = when (currentStep) {
            "inhala" -> 1.5f
            "sostén" -> 1.5f
            "exhala" -> 0.8f
            else -> 1f
        },
        animationSpec = tween(
            durationMillis = when (currentStep) {
                "inhala" -> 4000
                "sostén" -> 7000
                "exhala" -> 8000
                else -> 1000
            },
            easing = LinearEasing
        ),
        label = "breathScale"
    )

    LaunchedEffect(Unit) {
        for (cycle in 1..totalCycles) {
            currentCycle = cycle

            // Inhale 4 seconds
            currentStep = "inhala"
            stepSeconds = 4
            for (i in 4 downTo 1) {
                stepSeconds = i
                delay(1000)
                secondsElapsed++
            }

            // Hold 7 seconds
            currentStep = "sostén"
            stepSeconds = 7
            for (i in 7 downTo 1) {
                stepSeconds = i
                delay(1000)
                secondsElapsed++
            }

            // Exhale 8 seconds
            currentStep = "exhala"
            stepSeconds = 8
            for (i in 8 downTo 1) {
                stepSeconds = i
                delay(1000)
                secondsElapsed++
            }
        }
        onComplete()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Respiración 4-7-8",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Ciclo $currentCycle de $totalCycles",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Animated breathing circle
        Box(
            modifier = Modifier
                .size(180.dp)
                .scale(scale)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            AccentBlue.copy(alpha = 0.6f),
                            AccentBlue.copy(alpha = 0.2f),
                            Color.Transparent
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$stepSeconds",
                color = Color.White,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = when (currentStep) {
                "inhala" -> "INHALA por la nariz"
                "sostén" -> "SOSTÉN el aire"
                "exhala" -> "EXHALA por la boca"
                else -> ""
            },
            color = AccentBlue,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Tiempo total: ${secondsElapsed}s",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 14.sp
        )
    }
}

@Composable
private fun ImpactPhrasesPhase(
    streakDays: Int,
    onComplete: () -> Unit
) {
    val phrases = listOf(
        "Cada día que resistes, tu cerebro se reconecta.",
        "Llevas $streakDays días. No tires eso a la basura.",
        "Tu yo del futuro te agradecerá este momento.",
        "La dopamina fácil destruye la capacidad de sentir felicidad real.",
        "Eres más fuerte que un impulso de 15 minutos.",
        "Esto pasará. Siempre pasa. Y te sentirás orgulloso."
    )

    var currentIndex by remember { mutableIntStateOf(0) }
    var visible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        for (i in phrases.indices) {
            currentIndex = i
            visible = true
            delay(4000)
            visible = false
            delay(500)
        }
        onComplete()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "\uD83D\uDCAA",
            fontSize = 48.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(500)),
            exit = fadeOut(animationSpec = tween(500))
        ) {
            Text(
                text = phrases[currentIndex],
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 34.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        LinearProgressIndicator(
            progress = { (currentIndex + 1).toFloat() / phrases.size },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = AccentBlue,
            trackColor = Color.White.copy(alpha = 0.2f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${currentIndex + 1} / ${phrases.size}",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 14.sp
        )
    }
}

@Composable
private fun ActivitiesPhase(onComplete: () -> Unit) {
    data class Activity(val emoji: String, val name: String, val description: String)

    val activities = listOf(
        Activity("\uD83D\uDEB6", "Caminar", "Sal 10 minutos. El movimiento libera endorfinas."),
        Activity("\uD83D\uDEBF", "Ducha fría", "30 segundos de agua fría resetean tu sistema nervioso."),
        Activity("\uD83D\uDCAA", "Flexiones", "Haz 20. Quema esa energía acumulada."),
        Activity("\uD83C\uDFB5", "Música", "Pon tu canción favorita a todo volumen."),
        Activity("\uD83D\uDCDE", "Llamar a alguien", "Habla con alguien. No tienes que estar solo."),
        Activity("\uD83C\uDF73", "Cocinar", "Prepara algo rico. Ocupa tus manos."),
        Activity("\uD83D\uDCDA", "Leer", "Abre un libro. Cambia el foco mental."),
        Activity("\uD83E\uDDF9", "Limpiar", "Ordena tu espacio. Ordena tu mente."),
        Activity("\u270D\uFE0F", "Escribir", "Escribe lo que sientes. Sácalo de tu cabeza."),
        Activity("\uD83E\uDDCA", "Sostener hielo", "Aprieta un cubo de hielo. Redirecciona la sensación.")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "Elige una actividad alternativa",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Haz cualquiera de estas durante 10 minutos",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        activities.forEach { activity ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.08f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = activity.emoji, fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = activity.name,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = activity.description,
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onComplete,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "YA LO HICE \u2714\uFE0F",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun VictoryPhase(onExit: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "victory")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "\uD83C\uDFC6",
            fontSize = 80.sp,
            modifier = Modifier.scale(pulseScale)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "\u00A1LO LOGRASTE!",
            color = AccentGold,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Superaste la tentación.\nEres más fuerte de lo que crees.",
            color = Color.White,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            lineHeight = 26.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = AccentGold.copy(alpha = 0.15f)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "\u2B50", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "+10 XP ganados",
                    color = AccentGold,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onExit,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "VOLVER AL INICIO",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
