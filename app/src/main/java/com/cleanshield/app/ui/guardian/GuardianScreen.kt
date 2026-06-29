package com.cleanshield.app.ui.guardian

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cleanshield.app.ui.theme.CleanShieldColors
import com.cleanshield.app.utils.Constants
import com.cleanshield.app.utils.EncryptedPrefsManager
import com.cleanshield.app.vpn.DnsLockManager

@Composable
fun GuardianScreen(onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    val prefs = remember { EncryptedPrefsManager(context.applicationContext) }

    var lockEnabled by remember { mutableStateOf(prefs.isDnsLockEnabled) }
    var dnsHost by remember { mutableStateOf(prefs.dnsHost) }
    var guardianPhone by remember { mutableStateOf(prefs.guardianPhone ?: "") }
    var hasPassword by remember { mutableStateOf(prefs.hasPassword()) }

    // Estado en vivo del DNS
    var dnsCorrect by remember { mutableStateOf(DnsLockManager.isCorrect(context, dnsHost)) }
    val canWrite = remember { DnsLockManager.canWrite(context) }

    var showSetPasswordDialog by remember { mutableStateOf(false) }
    var showDisableDialog by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }

    fun refreshDns() {
        dnsCorrect = DnsLockManager.isCorrect(context, prefs.dnsHost)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onNavigateBack) { Text("< Volver") }
        }
        Text(
            "Modo Tutor",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = CleanShieldColors.GreenPrimary
        )
        Text(
            "Protección anti-impulso: bloquea el contenido en todo el móvil y avisa a tu persona de confianza si intentas desactivarlo.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.6f)
        )

        Spacer(Modifier.height(20.dp))

        // ---- Estado actual ----
        StatusCard(lockEnabled, dnsCorrect, canWrite)

        Spacer(Modifier.height(16.dp))

        // ---- DNS de filtro familiar ----
        SectionCard("DNS de filtro familiar") {
            Text(
                "Servidor DNS que bloquea el contenido. Déjalo como está si no sabes qué poner.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.6f)
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = dnsHost,
                onValueChange = { dnsHost = it.trim() },
                label = { Text("Host DNS") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors()
            )
            Text(
                "Recomendados: family.adguard-dns.com · family.cloudflare-dns.com",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.4f)
            )
        }

        Spacer(Modifier.height(16.dp))

        // ---- Aviso al tutor ----
        SectionCard("Aviso al tutor (SMS)") {
            Text(
                "Número de teléfono de tu persona de confianza. Recibirá un SMS si se intenta desactivar la protección.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.6f)
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = guardianPhone,
                onValueChange = { guardianPhone = it },
                label = { Text("Teléfono del tutor") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors()
            )
        }

        Spacer(Modifier.height(16.dp))

        // ---- Contraseña del tutor ----
        SectionCard("Contraseña del tutor") {
            Text(
                if (hasPassword)
                    "✅ Contraseña configurada. Se pedirá para desactivar la protección."
                else
                    "Aún no hay contraseña. Pide a tu tutor que ponga una que tú NO sepas: así no podrás desactivarlo en un impulso.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f)
            )
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = { showSetPasswordDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (hasPassword) "Cambiar contraseña" else "Establecer contraseña")
            }
        }

        Spacer(Modifier.height(20.dp))

        // ---- Botón principal activar/desactivar ----
        if (!lockEnabled) {
            Button(
                onClick = {
                    prefs.dnsHost = dnsHost.ifBlank { Constants.Dns.DEFAULT_HOST }
                    prefs.guardianPhone = guardianPhone.ifBlank { null }
                    prefs.isDnsLockEnabled = true
                    lockEnabled = true
                    if (canWrite) {
                        DnsLockManager.apply(context, prefs.dnsHost)
                    }
                    refreshDns()
                    message = if (canWrite)
                        "Protección activada. El DNS se aplicará y vigilará automáticamente."
                    else
                        "Protección activada. Configura ahora el DNS privado (botón de abajo) y, para el bloqueo automático, concede el permiso por ADB."
                },
                colors = ButtonDefaults.buttonColors(containerColor = CleanShieldColors.GreenPrimary),
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Activar protección", fontWeight = FontWeight.Bold, color = Color.White)
            }
        } else {
            Button(
                onClick = {
                    if (hasPassword) showDisableDialog = true
                    else {
                        prefs.isDnsLockEnabled = false
                        lockEnabled = false
                        message = "Protección desactivada."
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = CleanShieldColors.AccentRed),
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    if (hasPassword) "Desactivar (requiere contraseña)" else "Desactivar protección",
                    fontWeight = FontWeight.Bold, color = Color.White
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            onClick = {
                try {
                    context.startActivity(
                        Intent(Settings.ACTION_WIRELESS_SETTINGS)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                } catch (e: Exception) {
                    context.startActivity(
                        Intent(Settings.ACTION_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Abrir ajustes de red (DNS privado)")
        }

        Spacer(Modifier.height(16.dp))

        // ---- Instrucciones ADB para bloqueo automático ----
        if (!canWrite) {
            SectionCard("Bloqueo automático (avanzado)") {
                Text(
                    "Para que la app vuelva a poner el DNS sola si lo cambias, concede este permiso UNA vez conectando el móvil a un PC con ADB:",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "adb shell pm grant com.cleanshield.app.debug android.permission.WRITE_SECURE_SETTINGS",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace,
                    color = CleanShieldColors.GreenPrimary
                )
            }
        }

        message?.let {
            Spacer(Modifier.height(16.dp))
            Text(it, color = CleanShieldColors.GreenPrimary, style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(Modifier.height(24.dp))
    }

    // Diálogo: establecer/cambiar contraseña
    if (showSetPasswordDialog) {
        SetPasswordDialog(
            requireOld = hasPassword,
            onDismiss = { showSetPasswordDialog = false },
            onConfirm = { oldPass, newPass ->
                if (hasPassword && !prefs.verifyPassword(oldPass)) {
                    message = "Contraseña actual incorrecta."
                } else {
                    prefs.setPassword(newPass)
                    hasPassword = true
                    message = "Contraseña guardada."
                }
                showSetPasswordDialog = false
            }
        )
    }

    // Diálogo: desactivar con contraseña
    if (showDisableDialog) {
        VerifyPasswordDialog(
            onDismiss = { showDisableDialog = false },
            onConfirm = { pass ->
                if (prefs.verifyPassword(pass)) {
                    prefs.isDnsLockEnabled = false
                    lockEnabled = false
                    message = "Protección desactivada."
                    showDisableDialog = false
                } else {
                    message = "Contraseña incorrecta. La protección sigue activa."
                    showDisableDialog = false
                }
            }
        )
    }
}

@Composable
private fun StatusCard(lockEnabled: Boolean, dnsCorrect: Boolean, canWrite: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (lockEnabled && dnsCorrect)
                CleanShieldColors.GreenPrimary.copy(alpha = 0.12f)
            else CleanShieldColors.AccentRed.copy(alpha = 0.12f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            StatusRow(
                if (lockEnabled) "Protección activada" else "Protección desactivada",
                lockEnabled
            )
            StatusRow(
                if (dnsCorrect) "Filtro DNS funcionando" else "Filtro DNS NO activo",
                dnsCorrect
            )
            StatusRow(
                if (canWrite) "Bloqueo automático (ADB) concedido" else "Bloqueo automático: no concedido",
                canWrite
            )
        }
    }
}

@Composable
private fun StatusRow(text: String, ok: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            if (ok) Icons.Default.CheckCircle else Icons.Default.Error,
            contentDescription = null,
            tint = if (ok) CleanShieldColors.GreenPrimary else CleanShieldColors.AccentRed,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(text, color = Color.White, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CleanShieldColors.CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
private fun SetPasswordDialog(
    requireOld: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var oldPass by remember { mutableStateOf("") }
    var newPass by remember { mutableStateOf("") }
    var repeatPass by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Contraseña del tutor") },
        text = {
            Column {
                if (requireOld) {
                    OutlinedTextField(
                        value = oldPass,
                        onValueChange = { oldPass = it },
                        label = { Text("Contraseña actual") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                }
                OutlinedTextField(
                    value = newPass,
                    onValueChange = { newPass = it },
                    label = { Text("Nueva contraseña") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = repeatPass,
                    onValueChange = { repeatPass = it },
                    label = { Text("Repetir contraseña") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                error?.let { Text(it, color = CleanShieldColors.AccentRed) }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                when {
                    newPass.length < 4 -> error = "Mínimo 4 caracteres."
                    newPass != repeatPass -> error = "Las contraseñas no coinciden."
                    else -> onConfirm(oldPass, newPass)
                }
            }) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@Composable
private fun VerifyPasswordDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var pass by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Introduce la contraseña del tutor") },
        text = {
            OutlinedTextField(
                value = pass,
                onValueChange = { pass = it },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = { TextButton(onClick = { onConfirm(pass) }) { Text("Confirmar") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@Composable
private fun textFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedBorderColor = CleanShieldColors.GreenPrimary,
    unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
    focusedLabelColor = CleanShieldColors.GreenPrimary,
    unfocusedLabelColor = Color.White.copy(alpha = 0.5f)
)
