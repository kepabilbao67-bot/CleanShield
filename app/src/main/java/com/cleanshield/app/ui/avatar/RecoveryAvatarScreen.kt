package com.cleanshield.app.ui.avatar

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cleanshield.app.ui.theme.CleanShieldColors

data class AvatarState(
    val cleanDays: Int = 0,
    val level: Int = 1,
    val name: String = "Guerrero",
    val healthPercent: Float = 0.1f,
    val xp: Int = 0,
    val xpToNext: Int = 100
)

@Composable
fun RecoveryAvatarScreen(
    onNavigateBack: () -> Unit
) {
    // In a real app, this would come from a ViewModel with persisted data
    var avatarState by remember {
        mutableStateOf(AvatarState(cleanDays = 7, level = 2, healthPercent = 0.35f, xp = 35, xpToNext = 100))
    }

    val avatarEmoji = getAvatarEmoji(avatarState.cleanDays)
    val avatarTitle = getAvatarTitle(avatarState.cleanDays)
    val avatarColor = getAvatarColor(avatarState.cleanDays)

    val infiniteTransition = rememberInfiniteTransition(label = "avatar")
    val glow by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CleanShieldColors.DarkBackground)
    ) {
        SmallTopAppBar(
            title = { Text("Avatar de Recuperación", fontWeight = FontWeight.Bold) },
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
            Spacer(modifier = Modifier.height(16.dp))

            // Avatar Display
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .scale(glow)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                avatarColor.copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = avatarEmoji,
                    fontSize = 80.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = avatarTitle,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = avatarColor
            )

            Text(
                text = "Nivel ${avatarState.level} - ${avatarState.name}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Days Counter
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = CleanShieldColors.CardBackground
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${avatarState.cleanDays}",
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Bold,
                        color = CleanShieldColors.GreenPrimary
                    )
                    Text(
                        text = "Días Limpio",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // XP Progress
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = CleanShieldColors.CardBackground
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Experiencia",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                        Text(
                            "${avatarState.xp}/${avatarState.xpToNext} XP",
                            style = MaterialTheme.typography.bodyMedium,
                            color = CleanShieldColors.StreakGold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = avatarState.xp.toFloat() / avatarState.xpToNext,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = CleanShieldColors.StreakGold,
                        trackColor = Color.White.copy(alpha = 0.1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Health Bar
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = CleanShieldColors.CardBackground
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Salud Cerebral",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                        Text(
                            "${(avatarState.healthPercent * 100).toInt()}%",
                            style = MaterialTheme.typography.bodyMedium,
                            color = CleanShieldColors.GreenPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = avatarState.healthPercent,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = CleanShieldColors.GreenPrimary,
                        trackColor = Color.White.copy(alpha = 0.1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Evolution stages
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = CleanShieldColors.CardBackground
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Evolución del Avatar",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    EvolutionStage("💀", "Roto", "Día 0", avatarState.cleanDays >= 0)
                    EvolutionStage("😢", "Sufriendo", "Día 1-3", avatarState.cleanDays >= 1)
                    EvolutionStage("😐", "Resistiendo", "Día 4-7", avatarState.cleanDays >= 4)
                    EvolutionStage("🙂", "Mejorando", "Día 8-14", avatarState.cleanDays >= 8)
                    EvolutionStage("😊", "Fuerte", "Día 15-30", avatarState.cleanDays >= 15)
                    EvolutionStage("💪", "Guerrero", "Día 31-60", avatarState.cleanDays >= 31)
                    EvolutionStage("🦁", "Invencible", "Día 61-90", avatarState.cleanDays >= 61)
                    EvolutionStage("👑", "Leyenda", "Día 90+", avatarState.cleanDays >= 90)
                }
            }
        }
    }
}

@Composable
fun EvolutionStage(emoji: String, title: String, days: String, isUnlocked: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (isUnlocked) emoji else "🔒",
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = if (isUnlocked) Color.White else Color.White.copy(alpha = 0.4f)
            )
            Text(
                text = days,
                style = MaterialTheme.typography.bodySmall,
                color = if (isUnlocked) CleanShieldColors.GreenPrimary else Color.White.copy(alpha = 0.3f)
            )
        }
        if (isUnlocked) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = CleanShieldColors.GreenPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

private fun getAvatarEmoji(days: Int): String = when {
    days >= 90 -> "👑"
    days >= 61 -> "🦁"
    days >= 31 -> "💪"
    days >= 15 -> "😊"
    days >= 8 -> "🙂"
    days >= 4 -> "😐"
    days >= 1 -> "😢"
    else -> "💀"
}

private fun getAvatarTitle(days: Int): String = when {
    days >= 90 -> "LEYENDA"
    days >= 61 -> "INVENCIBLE"
    days >= 31 -> "GUERRERO"
    days >= 15 -> "FUERTE"
    days >= 8 -> "MEJORANDO"
    days >= 4 -> "RESISTIENDO"
    days >= 1 -> "COMENZANDO"
    else -> "RENACIENDO"
}

private fun getAvatarColor(days: Int): Color = when {
    days >= 90 -> Color(0xFFFFD700)
    days >= 61 -> Color(0xFFFF6B35)
    days >= 31 -> Color(0xFF00B894)
    days >= 15 -> Color(0xFF55EFC4)
    days >= 8 -> Color(0xFF74B9FF)
    else -> Color(0xFF636E72)
}
