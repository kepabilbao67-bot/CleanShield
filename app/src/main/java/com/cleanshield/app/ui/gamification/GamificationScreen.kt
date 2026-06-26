package com.cleanshield.app.ui.gamification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
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
import kotlin.math.pow

private val DarkBg = Color(0xFF0F1724)
private val CardBg = Color(0xFF1A2332)
private val AccentGold = Color(0xFFFFD54F)
private val AccentPurple = Color(0xFFBB86FC)
private val AccentBlue = Color(0xFF4FC3F7)
private val AccentGreen = Color(0xFF66BB6A)

data class Rank(
    val name: String,
    val emoji: String,
    val minLevel: Int,
    val color: Color
)

data class Skill(
    val name: String,
    val description: String,
    val emoji: String,
    val requiredLevel: Int
)

data class XPSource(
    val emoji: String,
    val description: String,
    val xp: Int
)

data class PlayerProfile(
    val level: Int,
    val xp: Int,
    val totalXp: Int,
    val rank: Rank,
    val unlockedSkills: List<Skill>
)

private val ranks = listOf(
    Rank("Novato", "\uD83C\uDF31", 1, Color(0xFF78909C)),
    Rank("Aprendiz", "\uD83D\uDCA1", 3, Color(0xFF66BB6A)),
    Rank("Guerrero", "\u2694\uFE0F", 7, Color(0xFF42A5F5)),
    Rank("Caballero", "\uD83D\uDEE1\uFE0F", 10, Color(0xFF5C6BC0)),
    Rank("Veterano", "\u2B50", 15, Color(0xFFAB47BC)),
    Rank("Maestro", "\uD83C\uDFC6", 20, Color(0xFFFF9800)),
    Rank("Campeón", "\uD83D\uDC51", 25, Color(0xFFFFD54F)),
    Rank("Héroe", "\uD83E\uDDB8", 30, Color(0xFFEF5350)),
    Rank("Leyenda", "\uD83C\uDF1F", 35, Color(0xFFE040FB)),
    Rank("Titán", "\u26A1", 40, Color(0xFF00E5FF)),
    Rank("Inmortal", "\uD83D\uDD25", 50, Color(0xFFFF6D00))
)

private val skills = listOf(
    Skill("Tema personalizado", "Desbloquea temas de colores personalizados", "\uD83C\uDFA8", 3),
    Skill("Respiración avanzada", "Técnicas de respiración extra", "\uD83C\uDF2C\uFE0F", 5),
    Skill("Estadísticas detalladas", "Acceso a gráficos avanzados", "\uD83D\uDCCA", 7),
    Skill("Sonidos nocturnos", "Sonidos ambiente para dormir", "\uD83C\uDF19", 10),
    Skill("Meditaciones premium", "Meditaciones guiadas profundas", "\uD83E\uDDD8", 12),
    Skill("Diario expandido", "Plantillas y análisis del diario", "\uD83D\uDCD6", 15),
    Skill("Escudo de racha", "Protege tu racha 1 vez al mes", "\uD83D\uDEE1\uFE0F", 20),
    Skill("Modo mentor", "Ayuda a otros usuarios anónimamente", "\uD83E\uDD1D", 25),
    Skill("Retos personalizados", "Crea tus propios desafíos", "\uD83C\uDFAF", 28),
    Skill("Insignias exclusivas", "Colección de insignias especiales", "\uD83C\uDFC5", 30),
    Skill("Modo oscuro pro", "Temas AMOLED y gradientes", "\uD83C\uDF0C", 35),
    Skill("Avatar legendario", "Marco dorado en tu perfil", "\uD83D\uDC51", 40)
)

private val xpSources = listOf(
    XPSource("\u23F0", "Cada hora protegido", 1),
    XPSource("\uD83D\uDEE1\uFE0F", "Usar botón de pánico", 5),
    XPSource("\uD83D\uDCD3", "Escribir en el diario", 10),
    XPSource("\uD83D\uDD25", "Racha diaria", 20),
    XPSource("\uD83C\uDFC6", "Completar desafío semanal", 50),
    XPSource("\uD83C\uDF1F", "Alcanzar un hito", 100)
)

/**
 * Calculate XP required for a specific level (exponential growth)
 */
fun xpForLevel(level: Int): Int {
    return (100 * level.toDouble().pow(1.5)).toInt()
}

/**
 * Calculate level progress as percentage (0.0 to 1.0)
 */
fun levelProgress(currentXp: Int, level: Int): Float {
    val currentLevelXp = xpForLevel(level)
    val nextLevelXp = xpForLevel(level + 1)
    val progressXp = currentXp - currentLevelXp
    val requiredXp = nextLevelXp - currentLevelXp
    return (progressXp.toFloat() / requiredXp.toFloat()).coerceIn(0f, 1f)
}

fun getRankForLevel(level: Int): Rank {
    return ranks.lastOrNull { level >= it.minLevel } ?: ranks.first()
}

fun getUnlockedSkills(level: Int): List<Skill> {
    return skills.filter { level >= it.requiredLevel }
}

@Composable
fun GamificationScreen(
    playerLevel: Int = 12,
    playerXp: Int = 1850,
    playerTotalXp: Int = 4200,
    onBack: () -> Unit = {}
) {
    val currentRank = getRankForLevel(playerLevel)
    val unlockedSkills = getUnlockedSkills(playerLevel)
    val progress = levelProgress(playerXp, playerLevel)
    val xpNeeded = xpForLevel(playerLevel + 1) - playerXp

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
                text = "Progresión RPG",
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
            // Profile Card
            item {
                ProfileCard(
                    level = playerLevel,
                    xp = playerXp,
                    totalXp = playerTotalXp,
                    rank = currentRank,
                    progress = progress,
                    xpNeeded = xpNeeded
                )
            }

            // XP Sources
            item {
                XPSourcesCard()
            }

            // Skills
            item {
                Text(
                    text = "\uD83D\uDD13 Habilidades",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${unlockedSkills.size}/${skills.size} desbloqueadas",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
            }

            items(skills) { skill ->
                SkillCard(
                    skill = skill,
                    isUnlocked = playerLevel >= skill.requiredLevel,
                    currentLevel = playerLevel
                )
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
private fun ProfileCard(
    level: Int,
    xp: Int,
    totalXp: Int,
    rank: Rank,
    progress: Float,
    xpNeeded: Int
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
                            rank.color.copy(alpha = 0.3f),
                            CardBg,
                            rank.color.copy(alpha = 0.1f)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Rank emoji
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(rank.color.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = rank.emoji, fontSize = 40.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Rank name
                Text(
                    text = rank.name,
                    color = rank.color,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                // Level
                Text(
                    text = "Nivel $level",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(16.dp))

                // XP Bar
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "$xp XP",
                            color = AccentGold,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Faltan $xpNeeded XP",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = AccentGold,
                        trackColor = Color.White.copy(alpha = 0.15f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "XP Total: $totalXp",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
private fun XPSourcesCard() {
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
                text = "\u2B50 Cómo ganar XP",
                color = AccentGold,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            xpSources.forEach { source ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = source.emoji, fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = source.description,
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = AccentGold.copy(alpha = 0.15f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "+${source.xp}",
                            color = AccentGold,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SkillCard(
    skill: Skill,
    isUnlocked: Boolean,
    currentLevel: Int
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) CardBg else CardBg.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        if (isUnlocked) AccentPurple.copy(alpha = 0.2f)
                        else Color.White.copy(alpha = 0.05f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isUnlocked) {
                    Text(text = skill.emoji, fontSize = 22.sp)
                } else {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.3f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = skill.name,
                    color = if (isUnlocked) Color.White else Color.White.copy(alpha = 0.4f),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = skill.description,
                    color = if (isUnlocked) Color.White.copy(alpha = 0.6f)
                    else Color.White.copy(alpha = 0.3f),
                    fontSize = 12.sp
                )
            }

            Text(
                text = if (isUnlocked) "\u2705" else "Nv.${skill.requiredLevel}",
                color = if (isUnlocked) AccentGreen else Color.White.copy(alpha = 0.4f),
                fontSize = if (isUnlocked) 18.sp else 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
