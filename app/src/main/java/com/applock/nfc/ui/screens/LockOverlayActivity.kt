package com.applock.nfc.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Nfc
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.applock.nfc.AppLockApplication
import com.applock.nfc.data.EventType
import com.applock.nfc.data.UsageStatistic
import com.applock.nfc.ui.theme.AppLockNFCTheme
import kotlinx.coroutines.launch

class LockOverlayActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val packageName = intent.getStringExtra("packageName") ?: ""
        val appName = intent.getStringExtra("appName") ?: "App"

        setContent {
            AppLockNFCTheme {
                LockOverlayScreen(
                    appName = appName,
                    packageName = packageName,
                    onUnlocked = {
                        finish()
                    },
                    onDismiss = {
                        moveTaskToBack(true)
                    }
                )
            }
        }
    }
}

@Composable
fun LockOverlayScreen(
    appName: String,
    packageName: String,
    onUnlocked: () -> Unit,
    onDismiss: () -> Unit
) {
    val app = androidx.compose.ui.platform.LocalContext.current.applicationContext as AppLockApplication
    val scope = rememberCoroutineScope()

    var pin by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val isPinEnabled by app.preferencesManager.isPinEnabled.collectAsState(initial = false)

    // Pulsing animation for the lock icon
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    fun handleUnlock() {
        scope.launch {
            if (isPinEnabled && pin.isNotEmpty()) {
                val isCorrect = app.preferencesManager.verifyPin(pin)
                if (isCorrect) {
                    // Log unlock event
                    app.repository.addStatistic(
                        UsageStatistic(
                            packageName = packageName,
                            appName = appName,
                            eventType = EventType.MANUAL_UNLOCK
                        )
                    )
                    onUnlocked()
                } else {
                    errorMessage = "Incorrect PIN"
                    pin = ""
                }
            } else if (!isPinEnabled) {
                // No PIN required, just unlock
                onUnlocked()
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black.copy(alpha = 0.95f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Lock Icon
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = "Locked",
                modifier = Modifier
                    .size(120.dp)
                    .scale(scale),
                tint = Color.White
            )

            Spacer(modifier = Modifier.height(32.dp))

            // App Locked Message
            Text(
                text = "$appName is Locked",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // NFC Instruction
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.1f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Nfc,
                        contentDescription = "NFC",
                        modifier = Modifier.size(32.dp),
                        tint = Color.White.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Tap your NFC tag or use the app to unlock",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            if (isPinEnabled) {
                Spacer(modifier = Modifier.height(32.dp))

                // PIN Entry
                OutlinedTextField(
                    value = pin,
                    onValueChange = {
                        pin = it
                        errorMessage = null
                    },
                    label = { Text("Enter PIN", color = Color.White.copy(alpha = 0.7f)) },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    isError = errorMessage != null,
                    supportingText = {
                        errorMessage?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                        cursorColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Unlock Button
                Button(
                    onClick = { handleUnlock() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Unlock",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Go Back Button
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.White.copy(alpha = 0.7f)
                )
            ) {
                Text("Go Back")
            }
        }
    }
}
