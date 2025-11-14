package com.applock.nfc.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.applock.nfc.data.AppInfo
import com.applock.nfc.ui.theme.LockedRed
import com.applock.nfc.ui.theme.UnlockedGreen
import java.util.concurrent.TimeUnit

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val installedApps by viewModel.installedApps.collectAsState()
    val isLocked by viewModel.isLocked.collectAsState()
    val lockedAppsCount by viewModel.lockedAppsCount.collectAsState()
    val autoUnlockDuration by viewModel.autoUnlockDuration.collectAsState()
    val lockTimestamp by viewModel.lockTimestamp.collectAsState()

    // Calculate time remaining for auto-unlock
    var timeRemaining by remember { mutableStateOf("") }

    LaunchedEffect(isLocked, lockTimestamp, autoUnlockDuration) {
        if (isLocked && lockTimestamp > 0) {
            while (true) {
                val elapsed = System.currentTimeMillis() - lockTimestamp
                val duration = autoUnlockDuration * 60 * 1000L
                val remaining = duration - elapsed

                if (remaining <= 0) {
                    viewModel.toggleLockState()
                    break
                }

                val minutes = TimeUnit.MILLISECONDS.toMinutes(remaining)
                val seconds = TimeUnit.MILLISECONDS.toSeconds(remaining) % 60
                timeRemaining = String.format("%02d:%02d", minutes, seconds)

                kotlinx.coroutines.delay(1000)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "AppLock NFC",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Virtual NFC Toggle Button
                VirtualNFCToggle(
                    isLocked = isLocked,
                    lockedAppsCount = lockedAppsCount,
                    timeRemaining = if (isLocked) timeRemaining else null,
                    onClick = { viewModel.toggleLockState() }
                )

                if (lockedAppsCount > 0) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "$lockedAppsCount app${if (lockedAppsCount != 1) "s" else ""} selected",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // App List
        if (installedApps.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        text = "Select apps to lock",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(installedApps, key = { it.packageName }) { app ->
                    AppListItem(
                        appInfo = app,
                        onToggle = { viewModel.toggleAppLock(app) }
                    )
                }
            }
        }
    }
}

@Composable
fun VirtualNFCToggle(
    isLocked: Boolean,
    lockedAppsCount: Int,
    timeRemaining: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isLocked) LockedRed else UnlockedGreen,
        animationSpec = tween(300),
        label = "bgColor"
    )

    val icon = if (isLocked) Icons.Filled.Lock else Icons.Filled.LockOpen

    Card(
        modifier = modifier
            .size(160.dp)
            .clickable(enabled = lockedAppsCount > 0) { onClick() },
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = if (lockedAppsCount > 0) backgroundColor else MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isLocked) 12.dp else 8.dp
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = if (isLocked) "Locked" else "Unlocked",
                    modifier = Modifier.size(48.dp),
                    tint = if (lockedAppsCount > 0) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (isLocked) "LOCKED" else "UNLOCKED",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (lockedAppsCount > 0) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (isLocked && !timeRemaining.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = timeRemaining,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }
    }

    if (lockedAppsCount == 0) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Select apps to enable lock",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun AppListItem(
    appInfo: AppInfo,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle() }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            if (appInfo.isLocked)
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.surfaceVariant
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (appInfo.isLocked) Icons.Outlined.Lock else Icons.Outlined.LockOpen,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = if (appInfo.isLocked)
                            MaterialTheme.colorScheme.onPrimaryContainer
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = appInfo.appName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = appInfo.packageName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Checkbox(
                checked = appInfo.isLocked,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}
