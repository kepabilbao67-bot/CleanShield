package com.cleanshield.app.ui.meditation

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
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

private val DarkBg = Color(0xFF0F1724)
private val CardBg = Color(0xFF1A2332)
private val AccentPurple = Color(0xFFBB86FC)
private val AccentTeal = Color(0xFF03DAC5)
private val AccentGold = Color(0xFFFFD54F)

data class MeditationStep(
    val instruction: String,
    val durationSeconds: Int
)

data class Meditation(
    val id: String,
    val emoji: String,
    val name: String,
    val description: String,
    val category: String, // "quick", "deep", "ambient"
    val totalMinutes: Int,
    val steps: List<MeditationStep>
)

private val meditations = listOf(
    Meditation(
        id = "grounding",
        emoji = "\uD83C\uDF0D",
        name = "Grounding 5-4-3-2-1",
        description = "Técnica de emergencia para anclarte al presente",
        category = "quick",
        totalMinutes = 5,
        steps = listOf(
            MeditationStep("Cierra los ojos. Respira profundo 3 veces.", 20),
            MeditationStep("Nombra 5 cosas que puedes VER a tu alrededor.", 40),
            MeditationStep("Nombra 4 cosas que puedes TOCAR ahora mismo.", 35),
            MeditationStep("Nombra 3 cosas que puedes ESCUCHAR.", 30),
            MeditationStep("Nombra 2 cosas que puedes OLER.", 25),
            MeditationStep("Nombra 1 cosa que puedes SABOREAR.", 20),
            MeditationStep("Abre los ojos lentamente. Estás aquí. Estás presente.", 30)
        )
    ),
    Meditation(
        id = "box_breathing",
        emoji = "\uD83D\uDFE6",
        name = "Respiración Cuadrada",
        description = "Técnica 4-4-4-4 para calmar el sistema nervioso",
        category = "quick",
        totalMinutes = 4,
        steps = listOf(
            MeditationStep("Inhala durante 4 segundos.", 4),
            MeditationStep("Sostén el aire 4 segundos.", 4),
            MeditationStep("Exhala durante 4 segundos.", 4),
            MeditationStep("Mantén vacío 4 segundos.", 4),
            MeditationStep("Inhala durante 4 segundos.", 4),
            MeditationStep("Sostén el aire 4 segundos.", 4),
            MeditationStep("Exhala durante 4 segundos.", 4),
            MeditationStep("Mantén vacío 4 segundos.", 4),
            MeditationStep("Inhala durante 4 segundos.", 4),
            MeditationStep("Sostén el aire 4 segundos.", 4),
            MeditationStep("Exhala durante 4 segundos.", 4),
            MeditationStep("Mantén vacío 4 segundos.", 4),
            MeditationStep("Bien hecho. Tu sistema nervioso se ha calmado.", 10)
        )
    ),
    Meditation(
        id = "body_scan",
        emoji = "\uD83E\uDDD8",
        name = "Escaneo Corporal",
        description = "Recorre tu cuerpo liberando tensión",
        category = "deep",
        totalMinutes = 12,
        steps = listOf(
            MeditationStep("Acuéstate o siéntate cómodamente. Cierra los ojos.", 30),
            MeditationStep("Lleva tu atención a los pies. Siente cada dedo.", 60),
            MeditationStep("Sube a las piernas. Relaja los músculos.", 60),
            MeditationStep("Lleva la atención al abdomen. Respira profundo.", 60),
            MeditationStep("Siente el pecho subir y bajar con cada respiración.", 60),
            MeditationStep("Relaja los hombros. Déjalos caer.", 60),
            MeditationStep("Recorre los brazos hasta las puntas de los dedos.", 60),
            MeditationStep("Relaja el cuello y la mandíbula.", 60),
            MeditationStep("Siente la frente lisa, sin tensión.", 60),
            MeditationStep("Siente todo tu cuerpo como una unidad relajada.", 60),
            MeditationStep("Vuelve lentamente. Mueve los dedos. Abre los ojos.", 30)
        )
    ),
    Meditation(
        id = "self_compassion",
        emoji = "\u2764\uFE0F",
        name = "Autocompasión",
        description = "Cultiva amabilidad hacia ti mismo",
        category = "deep",
        totalMinutes = 10,
        steps = listOf(
            MeditationStep("Respira profundamente. Estás a salvo aquí.", 30),
            MeditationStep("Repite: 'Soy humano. Es normal tener dificultades.'", 45),
            MeditationStep("Repite: 'Me trato con la misma amabilidad que trataría a un amigo.'", 45),
            MeditationStep("Repite: 'Mi valor no depende de mi perfección.'", 45),
            MeditationStep("Repite: 'Cada día que intento, estoy creciendo.'", 45),
            MeditationStep("Repite: 'Me perdono por los errores del pasado.'", 45),
            MeditationStep("Repite: 'Merezco una vida libre y plena.'", 45),
            MeditationStep("Lleva una mano al corazón. Siente el calor.", 40),
            MeditationStep("Sonríe suavemente. Eres suficiente.", 30)
        )
    ),
    Meditation(
        id = "future_visualization",
        emoji = "\uD83C\uDF1F",
        name = "Visualización del Futuro",
        description = "Conecta con tu mejor versión futura",
        category = "deep",
        totalMinutes = 15,
        steps = listOf(
            MeditationStep("Cierra los ojos. Respira profundo 5 veces.", 30),
            MeditationStep("Imagínate dentro de 1 año, libre de esta adicción.", 60),
            MeditationStep("¿Cómo es tu mañana? Visualiza cada detalle.", 60),
            MeditationStep("¿Cómo te sientes al despertar sin vergüenza?", 60),
            MeditationStep("¿Cómo son tus relaciones? Imagina las sonrisas.", 60),
            MeditationStep("¿Qué has logrado con toda esa energía recuperada?", 60),
            MeditationStep("Siente el orgullo de haberlo conseguido.", 60),
            MeditationStep("Esa persona eres TÚ. Ya estás en camino.", 45),
            MeditationStep("Abre los ojos. Cada día te acerca a esa visión.", 30)
        )
    ),
    Meditation(
        id = "rain",
        emoji = "\uD83C\uDF27\uFE0F",
        name = "Lluvia",
        description = "Sonido ambiente relajante de lluvia",
        category = "ambient",
        totalMinutes = 20,
        steps = listOf(
            MeditationStep("Escucha la lluvia caer suavemente...", 120),
            MeditationStep("Deja que el sonido limpie tus pensamientos...", 120),
            MeditationStep("Cada gota se lleva una preocupación...", 120),
            MeditationStep("Estás protegido. Estás en calma...", 120),
            MeditationStep("La lluvia continúa, constante y tranquila...", 120),
            MeditationStep("Respira con el ritmo de la lluvia...", 120),
            MeditationStep("Siente la paz que trae el agua...", 120),
            MeditationStep("La tormenta pasa. La calma permanece.", 60)
        )
    ),
    Meditation(
        id = "ocean",
        emoji = "\uD83C\uDF0A",
        name = "Océano",
        description = "Olas del mar para relajación profunda",
        category = "ambient",
        totalMinutes = 20,
        steps = listOf(
            MeditationStep("Escucha las olas llegar a la orilla...", 120),
            MeditationStep("Inhala cuando la ola sube...", 120),
            MeditationStep("Exhala cuando la ola se retira...", 120),
            MeditationStep("El mar es infinito, como tu potencial...", 120),
            MeditationStep("Cada ola trae renovación...", 120),
            MeditationStep("Estás flotando, sin peso, sin preocupaciones...", 120),
            MeditationStep("El ritmo del océano es tu ritmo...", 120),
            MeditationStep("Las olas seguirán. Tú también.", 60)
        )
    ),
    Meditation(
        id = "forest",
        emoji = "\uD83C\uDF32",
        name = "Bosque",
        description = "Sonidos de la naturaleza para conectar",
        category = "ambient",
        totalMinutes = 15,
        steps = listOf(
            MeditationStep("Estás en un bosque verde y tranquilo...", 120),
            MeditationStep("Escucha los pájaros cantar entre los árboles...", 120),
            MeditationStep("El viento mueve suavemente las hojas...", 120),
            MeditationStep("Caminas por un sendero de tierra suave...", 120),
            MeditationStep("La luz del sol se filtra entre las ramas...", 120),
            MeditationStep("La naturaleza te recuerda que eres parte de algo grande.", 60)
        )
    ),
    Meditation(
        id = "fireplace",
        emoji = "\uD83D\uDD25",
        name = "Chimenea",
        description = "Calidez y confort para noches difíciles",
        category = "ambient",
        totalMinutes = 15,
        steps = listOf(
            MeditationStep("El fuego crepita suavemente frente a ti...", 120),
            MeditationStep("Sientes el calor en tu rostro...", 120),
            MeditationStep("Las llamas danzan, hipnóticas y tranquilas...", 120),
            MeditationStep("Estás seguro. Estás cálido. Estás en paz...", 120),
            MeditationStep("El fuego consume lo que ya no necesitas...", 120),
            MeditationStep("Solo queda calidez y tranquilidad.", 60)
        )
    )
)

@Composable
fun MeditationScreen(
    onBack: () -> Unit = {}
) {
    var selectedMeditation by remember { mutableStateOf<Meditation?>(null) }
    var isCompleted by remember { mutableStateOf(false) }

    when {
        isCompleted -> MeditationCompletionScreen(
            meditation = selectedMeditation!!,
            onFinish = {
                isCompleted = false
                selectedMeditation = null
            }
        )
        selectedMeditation != null -> ActiveMeditationScreen(
            meditation = selectedMeditation!!,
            onComplete = { isCompleted = true },
            onCancel = { selectedMeditation = null }
        )
        else -> MeditationListScreen(
            onSelect = { selectedMeditation = it },
            onBack = onBack
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MeditationListScreen(
    onSelect: (Meditation) -> Unit,
    onBack: () -> Unit
) {
    val quickMeditations = meditations.filter { it.category == "quick" }
    val deepMeditations = meditations.filter { it.category == "deep" }
    val ambientMeditations = meditations.filter { it.category == "ambient" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
    ) {
        TopAppBar(
            title = {
                Text(
                    "Meditaciones",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, "Volver", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = "\u26A1 Rápidas (3-5 min)",
                    color = AccentTeal,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            items(quickMeditations) { meditation ->
                MeditationCard(meditation = meditation, onClick = { onSelect(meditation) })
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "\uD83E\uDDD8 Profundas (10-20 min)",
                    color = AccentPurple,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            items(deepMeditations) { meditation ->
                MeditationCard(meditation = meditation, onClick = { onSelect(meditation) })
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "\uD83C\uDFB6 Sonidos Ambiente",
                    color = AccentGold,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            items(ambientMeditations) { meditation ->
                MeditationCard(meditation = meditation, onClick = { onSelect(meditation) })
            }
        }
    }
}

@Composable
private fun MeditationCard(
    meditation: Meditation,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = CardBg),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = meditation.emoji, fontSize = 36.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = meditation.name,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = meditation.description,
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 13.sp
                )
                Text(
                    text = "${meditation.totalMinutes} min",
                    color = AccentTeal,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Icon(
                Icons.Default.PlayArrow,
                contentDescription = "Iniciar",
                tint = AccentTeal,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
private fun ActiveMeditationScreen(
    meditation: Meditation,
    onComplete: () -> Unit,
    onCancel: () -> Unit
) {
    var currentStepIndex by remember { mutableIntStateOf(0) }
    var stepTimeRemaining by remember { mutableIntStateOf(meditation.steps[0].durationSeconds) }

    val totalSteps = meditation.steps.size
    val currentStep = meditation.steps[currentStepIndex]

    // Breathing animation
    val infiniteTransition = rememberInfiniteTransition(label = "breathe")
    val breatheScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breatheScale"
    )

    LaunchedEffect(currentStepIndex) {
        stepTimeRemaining = meditation.steps[currentStepIndex].durationSeconds
        while (stepTimeRemaining > 0) {
            delay(1000)
            stepTimeRemaining--
        }
        if (currentStepIndex < totalSteps - 1) {
            currentStepIndex++
        } else {
            onComplete()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Progress
        Text(
            text = "${meditation.emoji} ${meditation.name}",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Paso ${currentStepIndex + 1} de $totalSteps",
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Animated breathing circle
        Box(
            modifier = Modifier
                .size(160.dp)
                .scale(breatheScale)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            AccentPurple.copy(alpha = 0.5f),
                            AccentTeal.copy(alpha = 0.2f),
                            Color.Transparent
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$stepTimeRemaining",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Current instruction
        Text(
            text = currentStep.instruction,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            lineHeight = 28.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Step progress bar
        LinearProgressIndicator(
            progress = { (currentStepIndex + 1).toFloat() / totalSteps },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = AccentPurple,
            trackColor = Color.White.copy(alpha = 0.15f)
        )

        Spacer(modifier = Modifier.height(48.dp))

        TextButton(onClick = onCancel) {
            Text(
                text = "Cancelar",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun MeditationCompletionScreen(
    meditation: Meditation,
    onFinish: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowPulse"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "\u2728",
            fontSize = 72.sp,
            modifier = Modifier.scale(glowScale)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "\u00A1Meditación completada!",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "\"${meditation.name}\" - ${meditation.totalMinutes} min",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 16.sp
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

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Tu mente está más clara y en control.",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onFinish,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentPurple),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "FINALIZAR",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
