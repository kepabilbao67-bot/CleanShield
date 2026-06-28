package com.cleanshield.app.ui.brain

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cleanshield.app.ui.theme.CleanShieldColors

data class BrainMilestone(
    val day: Int,
    val title: String,
    val description: String,
    val dopamineLevel: Float,
    val isReached: Boolean
)

@Composable
fun BrainRecoveryScreen(
    onNavigateBack: () -> Unit
) {
    val cleanDays = 7 // Would come from ViewModel in real app

    val milestones = listOf(
        BrainMilestone(0, "Día 0 - Inicio", "Receptores de dopamina saturados. Tolerancia máxima.", 0.2f, true),
        BrainMilestone(3, "Día 3 - Abstinencia", "Pico de abstinencia. El cerebro exige la sustancia.", 0.15f, true),
        BrainMilestone(7, "Día 7 - Adaptación", "El cerebro empieza a regularse. Menos ansiedad.", 0.3f, true),
        BrainMilestone(14, "Día 14 - Reconexión", "Los receptores de dopamina empiezan a recuperarse.", 0.45f, false),
        BrainMilestone(30, "Día 30 - Mejora notable", "Mejora en concentración, sueño y estado de ánimo.", 0.6f, false),
        BrainMilestone(60, "Día 60 - Neuroplasticidad", "Nuevas conexiones neuronales se fortalecen.", 0.75f, false),
        BrainMilestone(90, "Día 90 - Reboot completo", "Niveles de dopamina casi normalizados.", 0.9f, false),
        BrainMilestone(180, "Día 180 - Consolidación", "Hábitos sanos consolidados. Circuitos rewired.", 0.95f, false)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CleanShieldColors.DarkBackground)
    ) {
        SmallTopAppBar(
            title = { Text("Recuperación Cerebral", fontWeight = FontWeight.Bold) },
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
                .padding(16.dp)
        ) {
            // Current status
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = CleanShieldColors.GreenPrimary.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🧠", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Tu cerebro se está curando",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Día $cleanDays de recuperación",
                        style = MaterialTheme.typography.bodyLarge,
                        color = CleanShieldColors.GreenPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Dopamine Graph
            Text(
                "Nivel de Dopamina",
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
                    DopamineGraph(cleanDays = cleanDays)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("0 días", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.5f))
                        Text("90 días", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.5f))
                        Text("180 días", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.5f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Milestones
            Text(
                "Hitos de Recuperación",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(12.dp))

            milestones.forEach { milestone ->
                MilestoneCard(milestone, cleanDays)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Science section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = CleanShieldColors.AccentPurple.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "🔬 La ciencia detrás",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = CleanShieldColors.AccentPurple
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "La adicción reduce los receptores D2 de dopamina en el núcleo accumbens. " +
                        "Con abstinencia, tu cerebro regenera estos receptores gradualmente. " +
                        "A los 90 días, la mayoría de personas recuperan niveles normales de sensibilidad a la dopamina.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f),
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}

@Composable
fun DopamineGraph(cleanDays: Int) {
    val greenColor = CleanShieldColors.GreenPrimary
    val redColor = CleanShieldColors.AccentRed

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        val width = size.width
        val height = size.height
        val padding = 16.dp.toPx()

        // Draw grid lines
        for (i in 0..4) {
            val y = height - (height * i / 4f)
            drawLine(
                color = Color.White.copy(alpha = 0.1f),
                start = Offset(padding, y),
                end = Offset(width - padding, y),
                strokeWidth = 1f
            )
        }

        // Draw recovery curve (sigmoidal)
        val path = Path()
        val points = 100
        for (i in 0..points) {
            val x = padding + (width - 2 * padding) * i / points
            val day = (180f * i / points)
            // Sigmoid-like recovery curve
            val recovery = 1f / (1f + kotlin.math.exp(-(day - 45f) / 20f).toFloat())
            val y = height - (height * 0.1f + height * 0.8f * recovery)

            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }

        drawPath(
            path = path,
            color = greenColor.copy(alpha = 0.5f),
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )

        // Draw current position marker
        val currentX = padding + (width - 2 * padding) * (cleanDays / 180f).coerceAtMost(1f)
        val currentRecovery = 1f / (1f + kotlin.math.exp(-(cleanDays - 45f) / 20f).toFloat())
        val currentY = height - (height * 0.1f + height * 0.8f * currentRecovery)

        drawCircle(
            color = greenColor,
            radius = 8.dp.toPx(),
            center = Offset(currentX, currentY)
        )
        drawCircle(
            color = greenColor.copy(alpha = 0.3f),
            radius = 14.dp.toPx(),
            center = Offset(currentX, currentY)
        )
    }
}

@Composable
fun MilestoneCard(milestone: BrainMilestone, currentDays: Int) {
    val isCurrentMilestone = currentDays >= milestone.day &&
            (milestone.day == 180 || currentDays < milestone.day + 14)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isCurrentMilestone -> CleanShieldColors.GreenPrimary.copy(alpha = 0.15f)
                milestone.isReached -> CleanShieldColors.CardBackground
                else -> CleanShieldColors.CardBackground.copy(alpha = 0.5f)
            }
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Status icon
            Icon(
                imageVector = when {
                    milestone.isReached -> Icons.Default.CheckCircle
                    isCurrentMilestone -> Icons.Default.RadioButtonChecked
                    else -> Icons.Default.RadioButtonUnchecked
                },
                contentDescription = null,
                tint = when {
                    milestone.isReached -> CleanShieldColors.GreenPrimary
                    isCurrentMilestone -> CleanShieldColors.AccentOrange
                    else -> Color.White.copy(alpha = 0.3f)
                },
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    milestone.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (milestone.isReached) Color.White else Color.White.copy(alpha = 0.5f)
                )
                Text(
                    milestone.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (milestone.isReached) Color.White.copy(alpha = 0.7f) else Color.White.copy(alpha = 0.3f)
                )
            }
            // Dopamine level
            Text(
                "${(milestone.dopamineLevel * 100).toInt()}%",
                style = MaterialTheme.typography.labelMedium,
                color = if (milestone.isReached) CleanShieldColors.GreenPrimary else Color.White.copy(alpha = 0.3f)
            )
        }
    }
}
