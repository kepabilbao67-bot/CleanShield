package com.cleanshield.app.ui.recovery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
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

data class RecoveryPhase(
    val id: String,
    val name: String,
    val emoji: String,
    val color: Color,
    val dayRange: IntRange,
    val description: String,
    val brainScience: String,
    val dailyAdvice: List<String>
)

private val phases = listOf(
    RecoveryPhase(
        id = "recognition",
        name = "RECONOCIMIENTO",
        emoji = "\uD83E\uDDE0",
        color = Color(0xFFEF5350),
        dayRange = 0..7,
        description = "Tu cerebro está pidiendo su dosis. Aguanta.",
        brainScience = "Durante los primeros 7 días, tu corteza prefrontal lucha contra el sistema límbico. " +
                "Los niveles de dopamina están por debajo de lo normal porque tu cerebro se acostumbró a picos artificiales. " +
                "Esto causa irritabilidad, ansiedad y antojos intensos. Es temporal: tu cerebro ya está comenzando a recalibrarse.",
        dailyAdvice = listOf(
            "Hoy es el día más difícil. Cada hora que pasa, eres más fuerte.",
            "Tu cerebro grita por dopamina. Dale ejercicio, no veneno.",
            "La ansiedad que sientes es tu cerebro sanando. Abrázala.",
            "Bebe agua, sal a caminar, llama a alguien. No estés solo.",
            "Ya pasaron los peores días. El pico de abstinencia está cediendo.",
            "Tu sueño mejorará pronto. Tu cerebro está reorganizándose.",
            "Una semana. 7 días de guerra ganada. Eres un guerrero."
        )
    ),
    RecoveryPhase(
        id = "cleanse",
        name = "LIMPIEZA",
        emoji = "\uD83D\uDCA7",
        color = Color(0xFF42A5F5),
        dayRange = 8..30,
        description = "El flatline es normal. Tu cerebro se está recalibrando.",
        brainScience = "Entre los días 8-30, tu cerebro entra en un período llamado 'flatline'. " +
                "Los receptores de dopamina (D2) que estaban saturados comienzan a regenerarse. " +
                "Puedes sentir apatía, falta de motivación y baja libido. Esto es BUENO: significa que tu cerebro " +
                "está eliminando las vías neuronales dañinas y construyendo nuevas conexiones.",
        dailyAdvice = listOf(
            "El flatline no es permanente. Tu cerebro está en modo reparación.",
            "Hoy podrías sentir apatía. Es tu cerebro reconstruyendo receptores.",
            "La falta de motivación es temporal. Haz ejercicio aunque no quieras.",
            "Tus emociones volverán más fuertes y reales que antes.",
            "Cada día sin recaer, miles de sinapsis se fortalecen.",
            "Estás en el valle antes de la montaña. Sigue caminando.",
            "Tu cerebro está creando nuevas rutas de placer natural."
        )
    ),
    RecoveryPhase(
        id = "strength",
        name = "FORTALEZA",
        emoji = "\uD83D\uDCAA",
        color = Color(0xFF66BB6A),
        dayRange = 31..90,
        description = "Construye nuevos hábitos. Las vías antiguas se debilitan.",
        brainScience = "De los días 31-90, la neuroplasticidad trabaja a tu favor. " +
                "Las conexiones neuronales asociadas a la adicción se debilitan por falta de uso (poda sináptica). " +
                "Simultáneamente, los nuevos hábitos crean y fortalecen vías alternativas. " +
                "Tu corteza prefrontal recupera control sobre los impulsos. La materia gris se regenera.",
        dailyAdvice = listOf(
            "Las vías neuronales de la adicción se están marchitando. Sigue así.",
            "Tu cerebro produce más dopamina naturalmente ahora.",
            "Los hábitos que construyes hoy serán automáticos mañana.",
            "Tu capacidad de concentración está mejorando visiblemente.",
            "La energía que recuperas es la que la adicción te robaba.",
            "Cada nuevo hábito crea miles de conexiones neuronales.",
            "90 días cambian la estructura física de tu cerebro."
        )
    ),
    RecoveryPhase(
        id = "freedom",
        name = "LIBERTAD",
        emoji = "\uD83C\uDF1F",
        color = Color(0xFFFFD54F),
        dayRange = 91..365,
        description = "Estás viviendo libre. Mantén la vigilancia.",
        brainScience = "Después de 90 días, tu cerebro ha completado la mayor parte de la neuroadaptación. " +
                "Los receptores D2 están en niveles normales. Tu sistema de recompensa responde a placeres naturales: " +
                "ejercicio, conexión social, logros. Sin embargo, las vías de memoria asociativa permanecen. " +
                "Un disparador fuerte puede activarlas. La vigilancia es tu mejor aliada.",
        dailyAdvice = listOf(
            "Tu cerebro funciona mejor que hace meses. Disfruta la claridad.",
            "Los disparadores pueden aparecer. Recuerda tus herramientas.",
            "Ayudar a otros refuerza tu propio compromiso.",
            "La confianza que sientes es real. La ganaste con esfuerzo.",
            "Celebra cada mes. Cada uno es una victoria.",
            "Tu ejemplo inspira a otros aunque no lo sepas.",
            "La libertad que sientes es tu estado natural recuperado."
        )
    ),
    RecoveryPhase(
        id = "mastery",
        name = "MAESTRÍA",
        emoji = "\uD83D\uDD25",
        color = Color(0xFFFF6D00),
        dayRange = 366..Int.MAX_VALUE,
        description = "Conquistaste lo que la mayoría no puede.",
        brainScience = "Después de un año, tu cerebro ha experimentado una remodelación significativa. " +
                "La densidad de materia gris en la corteza prefrontal ha aumentado. Tu control de impulsos " +
                "es más fuerte que el de la persona promedio porque lo entrenaste bajo presión extrema. " +
                "Las vías antiguas existen como cicatrices, pero ya no tienen poder sobre ti. " +
                "Eres neurológicamente más resiliente.",
        dailyAdvice = listOf(
            "Un año de disciplina te hizo más fuerte que la mayoría.",
            "Tu cerebro es neurológicamente más resiliente ahora.",
            "Comparte tu experiencia. Tu historia puede salvar a alguien.",
            "La maestría no es perfección, es consistencia.",
            "Eres la prueba viviente de que se puede.",
            "Nunca olvides quién eras. Te mantiene humilde y fuerte.",
            "Cada día sigue siendo una elección. Y tú ya sabes elegir."
        )
    )
)

data class HelpResource(
    val name: String,
    val phone: String,
    val description: String,
    val emoji: String
)

private val helpResources = listOf(
    HelpResource(
        name = "Línea de Atención a la Conducta Suicida",
        phone = "024",
        description = "Atención 24h, gratuita y confidencial",
        emoji = "\uD83D\uDCDE"
    ),
    HelpResource(
        name = "Teléfono de la Esperanza",
        phone = "717 003 717",
        description = "Apoyo emocional 24 horas",
        emoji = "\u2764\uFE0F"
    ),
    HelpResource(
        name = "Proyecto Hombre",
        phone = "900 16 15 15",
        description = "Tratamiento de adicciones",
        emoji = "\uD83E\uDD1D"
    ),
    HelpResource(
        name = "ANAR (Menores)",
        phone = "900 20 20 10",
        description = "Ayuda a niños y adolescentes",
        emoji = "\uD83D\uDC66"
    ),
    HelpResource(
        name = "Asociación ACAL",
        phone = "900 22 22 29",
        description = "Adicciones comportamentales",
        emoji = "\uD83E\uDDE0"
    )
)

@Composable
fun RecoveryPathScreen(
    currentDay: Int = 15,
    onBack: () -> Unit = {}
) {
    val currentPhase = phases.first { currentDay in it.dayRange }
    val dayInPhase = currentDay - currentPhase.dayRange.first
    val phaseLength = currentPhase.dayRange.last - currentPhase.dayRange.first + 1
    val phaseProgress = if (phaseLength > 1000) {
        ((currentDay - currentPhase.dayRange.first).toFloat() / 365f).coerceAtMost(1f)
    } else {
        dayInPhase.toFloat() / phaseLength.toFloat()
    }
    val dailyAdviceIndex = currentDay % currentPhase.dailyAdvice.size

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
                text = "Camino de Recuperación",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Current phase card
            item {
                CurrentPhaseCard(
                    phase = currentPhase,
                    currentDay = currentDay,
                    progress = phaseProgress
                )
            }

            // Brain science card
            item {
                BrainScienceCard(phase = currentPhase)
            }

            // Daily advice
            item {
                DailyAdviceCard(
                    advice = currentPhase.dailyAdvice[dailyAdviceIndex],
                    day = currentDay
                )
            }

            // Phase map
            item {
                PhaseMapCard(currentPhase = currentPhase, currentDay = currentDay)
            }

            // Help resources
            item {
                HelpResourcesCard()
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
private fun CurrentPhaseCard(
    phase: RecoveryPhase,
    currentDay: Int,
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
                            phase.color.copy(alpha = 0.25f),
                            CardBg,
                            phase.color.copy(alpha = 0.1f)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = phase.emoji, fontSize = 56.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Fase: ${phase.name}",
                    color = phase.color,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Día $currentDay",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = phase.description,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Progress in current phase
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Progreso en fase",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 13.sp
                        )
                        Text(
                            text = "${(progress * 100).toInt()}%",
                            color = phase.color,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = phase.color,
                        trackColor = Color.White.copy(alpha = 0.15f)
                    )
                }
            }
        }
    }
}

@Composable
private fun BrainScienceCard(phase: RecoveryPhase) {
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
                    text = "Neurociencia",
                    color = AccentBlue,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = phase.brainScience,
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 14.sp,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
private fun DailyAdviceCard(advice: String, day: Int) {
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
                Text(text = "\uD83D\uDCA1", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Consejo del día $day",
                    color = AccentGold,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = advice,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
private fun PhaseMapCard(currentPhase: RecoveryPhase, currentDay: Int) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardBg),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "\uD83D\uDDFA\uFE0F Mapa de Fases",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            phases.forEachIndexed { index, phase ->
                val isCurrentPhase = phase.id == currentPhase.id
                val isPastPhase = currentDay > phase.dayRange.last
                val isFuturePhase = currentDay < phase.dayRange.first

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Dot indicator
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(if (isCurrentPhase) 20.dp else 14.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isPastPhase -> phase.color
                                        isCurrentPhase -> phase.color
                                        else -> Color.White.copy(alpha = 0.2f)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isPastPhase) {
                                Text(text = "\u2713", color = Color.White, fontSize = 10.sp)
                            }
                        }
                        if (index < phases.size - 1) {
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(32.dp)
                                    .background(
                                        if (isPastPhase) phase.color.copy(alpha = 0.5f)
                                        else Color.White.copy(alpha = 0.1f)
                                    )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Phase info
                    Column(
                        modifier = Modifier.padding(bottom = if (index < phases.size - 1) 16.dp else 0.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = phase.emoji, fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = phase.name,
                                color = when {
                                    isCurrentPhase -> phase.color
                                    isPastPhase -> Color.White.copy(alpha = 0.8f)
                                    else -> Color.White.copy(alpha = 0.4f)
                                },
                                fontSize = 14.sp,
                                fontWeight = if (isCurrentPhase) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                        val rangeText = if (phase.dayRange.last > 1000) "Día ${phase.dayRange.first}+"
                        else "Días ${phase.dayRange.first}-${phase.dayRange.last}"
                        Text(
                            text = rangeText,
                            color = Color.White.copy(alpha = 0.4f),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 26.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HelpResourcesCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardBg),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "\uD83C\uDD98 Recursos de Ayuda",
                color = Color(0xFFEF5350),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Si necesitas ayuda profesional, no dudes en llamar",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(16.dp))

            helpResources.forEach { resource ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = resource.emoji, fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = resource.name,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = resource.description,
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 12.sp
                        )
                    }
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = AccentGreen.copy(alpha = 0.15f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Call,
                                contentDescription = null,
                                tint = AccentGreen,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = resource.phone,
                                color = AccentGreen,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                if (resource != helpResources.last()) {
                    HorizontalDivider(
                        color = Color.White.copy(alpha = 0.05f),
                        modifier = Modifier.padding(start = 32.dp)
                    )
                }
            }
        }
    }
}
