package com.cleanshield.app.ui.dangerzones

import android.Manifest
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
import com.cleanshield.app.ui.theme.CleanShieldColors
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

data class DangerZone(
    val name: String,
    val type: String,
    val icon: String,
    val isActive: Boolean = true
)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DangerZonesScreen(
    onNavigateBack: () -> Unit
) {
    val locationPermission = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    var isMonitoringActive by remember { mutableStateOf(true) }
    var alertRadius by remember { mutableFloatStateOf(500f) }

    val dangerZoneTypes = listOf(
        DangerZone("Casinos y casas de apuestas", "gambling", "🎰"),
        DangerZone("Salones de juego", "gambling", "🎲"),
        DangerZone("Zonas rojas / prostitución", "adult", "🚫"),
        DangerZone("Sex shops", "adult", "⚠️"),
        DangerZone("Puntos de venta de drogas", "drugs", "💊"),
        DangerZone("Bares de alto riesgo", "alcohol", "🍺")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CleanShieldColors.DarkBackground)
    ) {
        SmallTopAppBar(
            title = { Text("Zonas de Peligro", fontWeight = FontWeight.Bold) },
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
            // Status Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (isMonitoringActive)
                        CleanShieldColors.GreenPrimary.copy(alpha = 0.15f)
                    else
                        CleanShieldColors.AccentRed.copy(alpha = 0.15f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        if (isMonitoringActive) Icons.Default.GpsFixed else Icons.Default.GpsOff,
                        contentDescription = null,
                        tint = if (isMonitoringActive) CleanShieldColors.GreenPrimary else CleanShieldColors.AccentRed,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            if (isMonitoringActive) "Monitoreo GPS Activo" else "Monitoreo Desactivado",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            if (isMonitoringActive) "Detectando zonas de peligro cercanas"
                            else "Activa el monitoreo para tu protección",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                    Switch(
                        checked = isMonitoringActive,
                        onCheckedChange = { isMonitoringActive = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = CleanShieldColors.GreenPrimary,
                            checkedTrackColor = CleanShieldColors.GreenPrimary.copy(alpha = 0.3f)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Permission check
            if (!locationPermission.status.isGranted) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = CleanShieldColors.AccentOrange.copy(alpha = 0.15f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.LocationOff,
                            contentDescription = null,
                            tint = CleanShieldColors.AccentOrange,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Permiso de ubicación necesario",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Para detectar zonas de peligro necesitamos acceso a tu ubicación",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { locationPermission.launchPermissionRequest() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CleanShieldColors.AccentOrange
                            )
                        ) {
                            Text("Conceder Permiso")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Alert Radius
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = CleanShieldColors.CardBackground
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Radio de alerta: ${alertRadius.toInt()}m",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Slider(
                        value = alertRadius,
                        onValueChange = { alertRadius = it },
                        valueRange = 100f..2000f,
                        steps = 18,
                        colors = SliderDefaults.colors(
                            thumbColor = CleanShieldColors.GreenPrimary,
                            activeTrackColor = CleanShieldColors.GreenPrimary
                        )
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("100m", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.5f))
                        Text("2km", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.5f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Zone types
            Text(
                "Tipos de zona vigilados",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(12.dp))

            dangerZoneTypes.forEach { zone ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = CleanShieldColors.CardBackground
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(zone.icon, modifier = Modifier.padding(end = 12.dp))
                        Text(
                            zone.name,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = CleanShieldColors.GreenPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Alert message customization
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = CleanShieldColors.CardBackground
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Mensaje de alerta",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "\"⚠️ Estás cerca de una zona de riesgo. Recuerda por qué empezaste este camino. Tú eres más fuerte que este impulso.\"",
                        style = MaterialTheme.typography.bodyMedium,
                        color = CleanShieldColors.GreenPrimary.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}
