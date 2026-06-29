package com.cleanshield.app.ui

import android.app.Activity
import android.net.VpnService
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cleanshield.app.ui.avatar.RecoveryAvatarScreen
import com.cleanshield.app.ui.brain.BrainRecoveryScreen
import com.cleanshield.app.ui.calculator.PriceCalculatorScreen
import com.cleanshield.app.ui.challenge.DailyChallengeScreen
import com.cleanshield.app.ui.dangerzones.DangerZonesScreen
import com.cleanshield.app.ui.firepact.FirePactScreen
import com.cleanshield.app.ui.impulse.ImpulseScannerScreen
import com.cleanshield.app.ui.navigation.NavRoutes
import com.cleanshield.app.ui.sos.SilentSosScreen
import com.cleanshield.app.ui.theme.CleanShieldColors
import com.cleanshield.app.ui.theme.CleanShieldTheme
import com.cleanshield.app.ui.timesafe.TimeSafeScreen
import com.cleanshield.app.ui.voice.VoiceAssistantScreen
import com.cleanshield.app.utils.Constants
import com.cleanshield.app.vpn.VpnController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CleanShieldTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = CleanShieldColors.DarkBackground
                ) {
                    CleanShieldApp()
                }
            }
        }
    }
}

@Composable
fun CleanShieldApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute == NavRoutes.HOME ||
            currentRoute == NavRoutes.PROTECTION ||
            currentRoute == NavRoutes.SETTINGS

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                CleanShieldBottomBar(navController, currentRoute)
            }
        },
        containerColor = CleanShieldColors.DarkBackground
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavRoutes.HOME,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(NavRoutes.HOME) {
                HomeScreen(navController)
            }
            composable(NavRoutes.PROTECTION) {
                ProtectionScreen()
            }
            composable(NavRoutes.SETTINGS) {
                SettingsScreen()
            }
            composable(NavRoutes.VOICE_ASSISTANT) {
                VoiceAssistantScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(NavRoutes.IMPULSE_SCANNER) {
                ImpulseScannerScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(NavRoutes.RECOVERY_AVATAR) {
                RecoveryAvatarScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(NavRoutes.DANGER_ZONES) {
                DangerZonesScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(NavRoutes.TIME_SAFE) {
                TimeSafeScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(NavRoutes.PRICE_CALCULATOR) {
                PriceCalculatorScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(NavRoutes.DAILY_CHALLENGE) {
                DailyChallengeScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(NavRoutes.FIRE_PACT) {
                FirePactScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(NavRoutes.SILENT_SOS) {
                SilentSosScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(NavRoutes.BRAIN_RECOVERY) {
                BrainRecoveryScreen(onNavigateBack = { navController.popBackStack() })
            }
        }
    }
}

@Composable
fun CleanShieldBottomBar(navController: NavHostController, currentRoute: String?) {
    NavigationBar(
        containerColor = CleanShieldColors.CardBackground,
        contentColor = Color.White
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, "Inicio") },
            label = { Text("Inicio", fontSize = 10.sp) },
            selected = currentRoute == NavRoutes.HOME,
            onClick = {
                navController.navigate(NavRoutes.HOME) {
                    popUpTo(NavRoutes.HOME) { inclusive = true }
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CleanShieldColors.GreenPrimary,
                selectedTextColor = CleanShieldColors.GreenPrimary,
                indicatorColor = CleanShieldColors.GreenPrimary.copy(alpha = 0.1f)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Shield, "Protección") },
            label = { Text("Protección", fontSize = 10.sp) },
            selected = currentRoute == NavRoutes.PROTECTION,
            onClick = {
                navController.navigate(NavRoutes.PROTECTION) {
                    popUpTo(NavRoutes.HOME)
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CleanShieldColors.GreenPrimary,
                selectedTextColor = CleanShieldColors.GreenPrimary,
                indicatorColor = CleanShieldColors.GreenPrimary.copy(alpha = 0.1f)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, "Ajustes") },
            label = { Text("Ajustes", fontSize = 10.sp) },
            selected = currentRoute == NavRoutes.SETTINGS,
            onClick = {
                navController.navigate(NavRoutes.SETTINGS) {
                    popUpTo(NavRoutes.HOME)
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CleanShieldColors.GreenPrimary,
                selectedTextColor = CleanShieldColors.GreenPrimary,
                indicatorColor = CleanShieldColors.GreenPrimary.copy(alpha = 0.1f)
            )
        )
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "CleanShield",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = CleanShieldColors.GreenPrimary
                )
                Text(
                    "Tu escudo contra la adicción",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(CleanShieldColors.GreenPrimary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Shield,
                    contentDescription = null,
                    tint = CleanShieldColors.GreenPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Status Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = CleanShieldColors.GreenPrimary.copy(alpha = 0.1f)
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "7",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    color = CleanShieldColors.GreenPrimary
                )
                Text(
                    "Días Limpio",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "🔥 ¡Sigue así, guerrero!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = CleanShieldColors.StreakGold
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Daily challenge quick card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate(NavRoutes.DAILY_CHALLENGE) },
            colors = CardDefaults.cardColors(
                containerColor = CleanShieldColors.AccentBlue.copy(alpha = 0.1f)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("⚡", fontSize = 32.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Reto del día",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        "Completa tu reto diario",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }
                Icon(
                    Icons.Default.ChevronRight,
                    null,
                    tint = CleanShieldColors.AccentBlue
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Features Grid
        Text(
            "Herramientas",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Row 1
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FeatureCard(
                emoji = "🎙️",
                title = "Asistente IA",
                color = CleanShieldColors.GreenPrimary,
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(NavRoutes.VOICE_ASSISTANT) }
            )
            FeatureCard(
                emoji = "🧠",
                title = "Modo Cerebro",
                color = CleanShieldColors.AccentPurple,
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(NavRoutes.IMPULSE_SCANNER) }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Row 2
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FeatureCard(
                emoji = "🔥",
                title = "Pacto Fuego",
                color = CleanShieldColors.AccentOrange,
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(NavRoutes.FIRE_PACT) }
            )
            FeatureCard(
                emoji = "📍",
                title = "Zonas Peligro",
                color = CleanShieldColors.AccentRed,
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(NavRoutes.DANGER_ZONES) }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Row 3
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FeatureCard(
                emoji = "👤",
                title = "Mi Avatar",
                color = CleanShieldColors.AccentBlue,
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(NavRoutes.RECOVERY_AVATAR) }
            )
            FeatureCard(
                emoji = "💸",
                title = "Precio Real",
                color = CleanShieldColors.StreakGold,
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(NavRoutes.PRICE_CALCULATOR) }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Row 4
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FeatureCard(
                emoji = "🆘",
                title = "SOS Silencioso",
                color = Color(0xFFE91E63),
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(NavRoutes.SILENT_SOS) }
            )
            FeatureCard(
                emoji = "🔐",
                title = "Caja Tiempo",
                color = Color(0xFF795548),
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(NavRoutes.TIME_SAFE) }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Row 5
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FeatureCard(
                emoji = "📊",
                title = "Cerebro",
                color = Color(0xFF9C27B0),
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(NavRoutes.BRAIN_RECOVERY) }
            )
            FeatureCard(
                emoji = "⚡",
                title = "Reto Diario",
                color = Color(0xFF00BCD4),
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(NavRoutes.DAILY_CHALLENGE) }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Motivational quote
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = CleanShieldColors.CardBackground
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "💬 Frase del día",
                    style = MaterialTheme.typography.labelLarge,
                    color = CleanShieldColors.GreenPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    Constants.MOTIVATIONAL_QUOTES.random(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.9f),
                    lineHeight = 22.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun FeatureCard(
    emoji: String,
    title: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(emoji, fontSize = 32.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ProtectionScreen() {
    val context = LocalContext.current
    var protectionActive by remember { mutableStateOf(VpnController.isEnabled(context)) }

    // Lanzador para el dialogo del sistema que concede el permiso de VPN.
    val vpnPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            VpnController.startProtection(context)
            protectionActive = true
        }
    }

    // Permiso de notificaciones (Android 13+). La VPN funciona igual sin el.
    val notifPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    fun activateProtection() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notifPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
        // VpnService.prepare devuelve un Intent si aun no se ha concedido el permiso.
        val prepareIntent = VpnService.prepare(context)
        if (prepareIntent != null) {
            vpnPermissionLauncher.launch(prepareIntent)
        } else {
            VpnController.startProtection(context)
            protectionActive = true
        }
    }

    fun deactivateProtection() {
        VpnController.stopProtection(context)
        protectionActive = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            if (protectionActive) "Protección Activa" else "Protección Desactivada",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = if (protectionActive) CleanShieldColors.GreenPrimary else CleanShieldColors.AccentRed
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            if (protectionActive) "Todos los sistemas funcionando"
            else "Pulsa el botón para activar el filtro y bloquear las webs",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Boton principal de activacion/desactivacion
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (protectionActive)
                    CleanShieldColors.GreenPrimary.copy(alpha = 0.12f)
                else
                    CleanShieldColors.AccentRed.copy(alpha = 0.12f)
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    if (protectionActive) Icons.Default.Shield else Icons.Default.GppBad,
                    contentDescription = null,
                    tint = if (protectionActive) CleanShieldColors.GreenPrimary else CleanShieldColors.AccentRed,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    if (protectionActive) "Estás protegido" else "Sin protección",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (protectionActive) deactivateProtection() else activateProtection()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (protectionActive)
                            CleanShieldColors.AccentRed
                        else
                            CleanShieldColors.GreenPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        if (protectionActive) "Desactivar protección" else "Activar protección",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        ProtectionCard(
            icon = Icons.Default.VpnKey,
            title = "VPN DNS Filter",
            description = "Bloqueando ${Constants.BLOCKED_DOMAINS.size} dominios peligrosos",
            isActive = protectionActive
        )
        Spacer(modifier = Modifier.height(8.dp))
        ProtectionCard(
            icon = Icons.Default.Block,
            title = "App Blocker",
            description = "${Constants.ALL_BLOCKED_PACKAGES.size} apps bloqueadas",
            isActive = protectionActive
        )
        Spacer(modifier = Modifier.height(8.dp))
        ProtectionCard(
            icon = Icons.Default.Accessibility,
            title = "Accessibility Service",
            description = "Detectando contenido por palabras clave",
            isActive = protectionActive
        )
        Spacer(modifier = Modifier.height(8.dp))
        ProtectionCard(
            icon = Icons.Default.AdminPanelSettings,
            title = "Device Admin",
            description = "Protección contra desinstalación activa",
            isActive = protectionActive
        )
        Spacer(modifier = Modifier.height(8.dp))
        ProtectionCard(
            icon = Icons.Default.GpsFixed,
            title = "Zonas de Peligro GPS",
            description = "Monitoreo de ubicación activo",
            isActive = protectionActive
        )
        Spacer(modifier = Modifier.height(8.dp))
        ProtectionCard(
            icon = Icons.Default.FilterAlt,
            title = "Keyword Filter",
            description = "${Constants.ALL_BLOCKED_KEYWORDS.size} palabras clave bloqueadas",
            isActive = protectionActive
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = CleanShieldColors.CardBackground
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Estadísticas de protección",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(12.dp))
                StatRow("Intentos bloqueados hoy", "0")
                StatRow("Total bloqueados", "0")
                StatRow("Días de protección", "7")
                StatRow("Última actualización", "Ahora")
            }
        }
    }
}

@Composable
fun ProtectionCard(icon: ImageVector, title: String, description: String, isActive: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive)
                CleanShieldColors.GreenPrimary.copy(alpha = 0.08f)
            else
                CleanShieldColors.AccentRed.copy(alpha = 0.08f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = if (isActive) CleanShieldColors.GreenPrimary else CleanShieldColors.AccentRed,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(if (isActive) CleanShieldColors.GreenPrimary else CleanShieldColors.AccentRed)
            )
        }
    }
}

@Composable
fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.7f))
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            "Ajustes",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(20.dp))

        SettingsSection("Cuenta") {
            SettingsItem(Icons.Default.Person, "Perfil de recuperación")
            SettingsItem(Icons.Default.Lock, "Contraseña de protección")
            SettingsItem(Icons.Default.Notifications, "Notificaciones")
        }

        Spacer(modifier = Modifier.height(16.dp))

        SettingsSection("Protección") {
            SettingsItem(Icons.Default.VpnKey, "Configurar VPN")
            SettingsItem(Icons.Default.Block, "Apps bloqueadas")
            SettingsItem(Icons.Default.NightsStay, "Modo nocturno estricto")
            SettingsItem(Icons.Default.GpsFixed, "Zonas de peligro")
        }

        Spacer(modifier = Modifier.height(16.dp))

        SettingsSection("Recuperación") {
            SettingsItem(Icons.Default.ContactPhone, "Contacto de emergencia")
            SettingsItem(Icons.Default.Refresh, "Reiniciar racha")
            SettingsItem(Icons.Default.Backup, "Copia de seguridad")
        }

        Spacer(modifier = Modifier.height(16.dp))

        SettingsSection("Sobre") {
            SettingsItem(Icons.Default.Info, "Versión 2.0.0")
            SettingsItem(Icons.Default.Policy, "Política de privacidad")
            SettingsItem(Icons.Default.Favorite, "Valorar la app")
        }
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable () -> Unit) {
    Text(
        title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = CleanShieldColors.GreenPrimary
    )
    Spacer(modifier = Modifier.height(8.dp))
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = CleanShieldColors.CardBackground
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            content()
        }
    }
}

@Composable
fun SettingsItem(icon: ImageVector, title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            title,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            modifier = Modifier.weight(1f)
        )
        Icon(
            Icons.Default.ChevronRight,
            null,
            tint = Color.White.copy(alpha = 0.3f),
            modifier = Modifier.size(20.dp)
        )
    }
}
