package com.cleanshield.app.ui.calculator

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cleanshield.app.ui.theme.CleanShieldColors

@Composable
fun PriceCalculatorScreen(
    onNavigateBack: () -> Unit
) {
    var monthlyGambling by remember { mutableStateOf("") }
    var monthlySubscriptions by remember { mutableStateOf("") }
    var hoursPerWeek by remember { mutableStateOf("") }
    var yearsAddicted by remember { mutableStateOf("") }
    var showResults by remember { mutableStateOf(false) }

    val gambling = monthlyGambling.toDoubleOrNull() ?: 0.0
    val subscriptions = monthlySubscriptions.toDoubleOrNull() ?: 0.0
    val hours = hoursPerWeek.toDoubleOrNull() ?: 0.0
    val years = yearsAddicted.toDoubleOrNull() ?: 0.0

    val totalMoneyMonth = gambling + subscriptions
    val totalMoneyYear = totalMoneyMonth * 12
    val totalMoneyLifetime = totalMoneyYear * years
    val totalHoursWeek = hours
    val totalHoursYear = hours * 52
    val totalHoursLifetime = totalHoursYear * years
    val daysStolen = (totalHoursLifetime / 24).toInt()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CleanShieldColors.DarkBackground)
    ) {
        SmallTopAppBar(
            title = { Text("El Precio Real", fontWeight = FontWeight.Bold) },
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
            // Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = CleanShieldColors.AccentRed.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("💸", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Calcula el precio REAL de tu adicción",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                    Text(
                        "Dinero, tiempo y relaciones perdidas",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Input fields
            Text(
                "Datos de tu adicción",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = monthlyGambling,
                onValueChange = { monthlyGambling = it },
                label = { Text("€ Dinero gastado al mes en juego/apuestas") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.AttachMoney, null) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CleanShieldColors.GreenPrimary,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = CleanShieldColors.GreenPrimary,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.5f)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = monthlySubscriptions,
                onValueChange = { monthlySubscriptions = it },
                label = { Text("€ Suscripciones (porno, casinos online)") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.CreditCard, null) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CleanShieldColors.GreenPrimary,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = CleanShieldColors.GreenPrimary,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.5f)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = hoursPerWeek,
                onValueChange = { hoursPerWeek = it },
                label = { Text("Horas semanales dedicadas a la adicción") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Timer, null) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CleanShieldColors.GreenPrimary,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = CleanShieldColors.GreenPrimary,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.5f)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = yearsAddicted,
                onValueChange = { yearsAddicted = it },
                label = { Text("Años con la adicción") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.CalendarMonth, null) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CleanShieldColors.GreenPrimary,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = CleanShieldColors.GreenPrimary,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.5f)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { showResults = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CleanShieldColors.AccentRed
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "Ver el precio real",
                    modifier = Modifier.padding(vertical = 8.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            // Results
            if (showResults && (gambling > 0 || hours > 0)) {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    "EL PRECIO REAL",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = CleanShieldColors.AccentRed
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Money lost
                ResultCard(
                    emoji = "💰",
                    title = "Dinero perdido",
                    values = listOf(
                        "Al mes: €${String.format("%.0f", totalMoneyMonth)}",
                        "Al año: €${String.format("%.0f", totalMoneyYear)}",
                        "Total vida: €${String.format("%.0f", totalMoneyLifetime)}"
                    ),
                    color = CleanShieldColors.AccentRed
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Time stolen
                ResultCard(
                    emoji = "⏰",
                    title = "Tiempo robado",
                    values = listOf(
                        "A la semana: ${String.format("%.0f", totalHoursWeek)} horas",
                        "Al año: ${String.format("%.0f", totalHoursYear)} horas",
                        "Total: $daysStolen DÍAS completos de tu vida"
                    ),
                    color = CleanShieldColors.AccentOrange
                )

                Spacer(modifier = Modifier.height(8.dp))

                // What could have been
                ResultCard(
                    emoji = "🎯",
                    title = "Lo que podrías haber hecho",
                    values = listOf(
                        "Con €${String.format("%.0f", totalMoneyLifetime)}: ${getMoneyComparison(totalMoneyLifetime)}",
                        "Con ${String.format("%.0f", totalHoursLifetime)}h: ${getTimeComparison(totalHoursLifetime)}",
                        "Relaciones dañadas: incalculable"
                    ),
                    color = CleanShieldColors.AccentBlue
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Motivational ending
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = CleanShieldColors.GreenPrimary.copy(alpha = 0.15f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Pero hoy es diferente",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = CleanShieldColors.GreenPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Cada día limpio recuperas tu tiempo, tu dinero y tu dignidad.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ResultCard(emoji: String, title: String, values: List<String>, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(emoji, fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            values.forEach { value ->
                Text(
                    value,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}

private fun getMoneyComparison(amount: Double): String = when {
    amount >= 100000 -> "un piso"
    amount >= 30000 -> "un coche nuevo"
    amount >= 10000 -> "viajes increíbles"
    amount >= 5000 -> "formación profesional"
    amount >= 1000 -> "experiencias con tu familia"
    else -> "pequeños lujos que te mereces"
}

private fun getTimeComparison(hours: Double): String = when {
    hours >= 5000 -> "aprender 3 idiomas"
    hours >= 2000 -> "un máster universitario"
    hours >= 1000 -> "dominar un instrumento"
    hours >= 500 -> "crear un negocio"
    hours >= 100 -> "leer 50 libros"
    else -> "mejorar tus relaciones"
}
