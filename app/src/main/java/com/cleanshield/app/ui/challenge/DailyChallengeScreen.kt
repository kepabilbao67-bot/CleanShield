package com.cleanshield.app.ui.challenge

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cleanshield.app.ui.theme.CleanShieldColors
import java.time.LocalDate

data class DailyChallenge(
    val title: String,
    val description: String,
    val category: String,
    val difficulty: String,
    val xpReward: Int,
    val icon: String
)

@Composable
fun DailyChallengeScreen(
    onNavigateBack: () -> Unit
) {
    var isChallengeCompleted by remember { mutableStateOf(false) }
    var streakCount by remember { mutableIntStateOf(5) }

    val todayChallenge = getTodayChallenge()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CleanShieldColors.DarkBackground)
    ) {
        SmallTopAppBar(
            title = { Text("Reto del Día", fontWeight = FontWeight.Bold) },
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
            // Streak
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = CleanShieldColors.StreakGold.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("🔥", fontSize = 32.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "$streakCount días seguidos completando retos",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = CleanShieldColors.StreakGold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Today's challenge
            Text(
                todayChallenge.icon,
                fontSize = 64.sp
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "RETO DE HOY",
                style = MaterialTheme.typography.labelLarge,
                color = CleanShieldColors.GreenPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                todayChallenge.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = CleanShieldColors.CardBackground
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        todayChallenge.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AssistChip(
                            onClick = { },
                            label = { Text(todayChallenge.category) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = CleanShieldColors.AccentBlue.copy(alpha = 0.2f),
                                labelColor = CleanShieldColors.AccentBlue
                            )
                        )
                        AssistChip(
                            onClick = { },
                            label = { Text(todayChallenge.difficulty) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = CleanShieldColors.AccentOrange.copy(alpha = 0.2f),
                                labelColor = CleanShieldColors.AccentOrange
                            )
                        )
                        AssistChip(
                            onClick = { },
                            label = { Text("+${todayChallenge.xpReward} XP") },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = CleanShieldColors.StreakGold.copy(alpha = 0.2f),
                                labelColor = CleanShieldColors.StreakGold
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Complete button
            if (!isChallengeCompleted) {
                Button(
                    onClick = {
                        isChallengeCompleted = true
                        streakCount++
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CleanShieldColors.GreenPrimary
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.CheckCircle, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "¡RETO COMPLETADO!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            } else {
                // Completed state
                val infiniteTransition = rememberInfiniteTransition(label = "complete")
                val scale by infiniteTransition.animateFloat(
                    initialValue = 1f,
                    targetValue = 1.1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "scale"
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(scale),
                    colors = CardDefaults.cardColors(
                        containerColor = CleanShieldColors.GreenPrimary.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🎉", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "¡Reto completado!",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = CleanShieldColors.GreenPrimary
                        )
                        Text(
                            "+${todayChallenge.xpReward} XP ganados",
                            style = MaterialTheme.typography.bodyLarge,
                            color = CleanShieldColors.StreakGold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tips
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = CleanShieldColors.CardBackground
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "💡 ¿Por qué este reto?",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Los retos diarios reconectan tu cerebro con fuentes sanas de dopamina. Cada reto completado fortalece tu corteza prefrontal, la parte del cerebro responsable del autocontrol.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

private fun getTodayChallenge(): DailyChallenge {
    val dayOfYear = LocalDate.now().dayOfYear
    val challenges = listOf(
        DailyChallenge(
            "Llamada de 5 minutos",
            "Llama a alguien que te importa. No textos, una llamada REAL de mínimo 5 minutos. Pregúntale cómo está de verdad.",
            "Social",
            "Fácil",
            20,
            "📞"
        ),
        DailyChallenge(
            "Ducha fría 30 segundos",
            "Al final de tu ducha, cambia a agua fría durante 30 segundos. Esto entrena tu voluntad y activa el sistema nervioso simpático.",
            "Físico",
            "Medio",
            30,
            "🚿"
        ),
        DailyChallenge(
            "Escribir 3 gratitudes",
            "Escribe 3 cosas por las que estás agradecido HOY. No repitas las de ayer. Sé específico.",
            "Mental",
            "Fácil",
            15,
            "📝"
        ),
        DailyChallenge(
            "30 minutos sin teléfono",
            "Deja el teléfono en otra habitación durante 30 minutos. Haz algo con tus manos: cocinar, limpiar, dibujar.",
            "Desconexión",
            "Medio",
            25,
            "📵"
        ),
        DailyChallenge(
            "Ejercicio de 15 minutos",
            "Haz 15 minutos de ejercicio intenso: flexiones, sentadillas, saltar. No importa la forma, importa el esfuerzo.",
            "Físico",
            "Medio",
            30,
            "💪"
        ),
        DailyChallenge(
            "Meditación 10 minutos",
            "Siéntate en silencio 10 minutos. Respira. Cuando tu mente divague, vuelve a la respiración sin juzgarte.",
            "Mental",
            "Fácil",
            20,
            "🧘"
        ),
        DailyChallenge(
            "Acto de servicio",
            "Haz algo por alguien sin que te lo pida: ayuda a un vecino, ofrece tu asiento, carga las bolsas de alguien.",
            "Social",
            "Fácil",
            25,
            "🤝"
        ),
        DailyChallenge(
            "Cocinar algo nuevo",
            "Prepara una receta que nunca hayas hecho. No importa si sale mal. El proceso es la recompensa.",
            "Creatividad",
            "Medio",
            20,
            "👨‍🍳"
        ),
        DailyChallenge(
            "Caminata consciente 20 min",
            "Camina 20 minutos sin auriculares. Presta atención a cada paso, cada sonido, cada olor. Estás AQUÍ.",
            "Físico",
            "Fácil",
            20,
            "🚶"
        ),
        DailyChallenge(
            "Carta a ti mismo",
            "Escribe una carta honesta a tu yo adicto. Dile por qué no vas a volver atrás. Sé brutal.",
            "Emocional",
            "Difícil",
            40,
            "✉️"
        ),
        DailyChallenge(
            "Limpieza profunda",
            "Limpia a fondo una zona de tu casa. El orden externo refleja el orden interno. Tu espacio = tu mente.",
            "Físico",
            "Medio",
            25,
            "🧹"
        ),
        DailyChallenge(
            "Aprender algo nuevo",
            "Dedica 30 minutos a aprender algo: un idioma, un instrumento, programar. Inversión en ti mismo.",
            "Mental",
            "Medio",
            30,
            "📚"
        ),
        DailyChallenge(
            "Confesión valiente",
            "Cuéntale a alguien de confianza cómo te sientes HOY de verdad. Sin máscaras. La vulnerabilidad es fuerza.",
            "Emocional",
            "Difícil",
            40,
            "💬"
        ),
        DailyChallenge(
            "Dormir antes de las 23:00",
            "Hoy acuéstate antes de las 11pm. Apaga pantallas a las 10pm. El sueño es tu arma secreta contra los impulsos.",
            "Salud",
            "Medio",
            25,
            "😴"
        )
    )
    return challenges[dayOfYear % challenges.size]
}
