package com.applock.nfc

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.lifecycleScope
import com.applock.nfc.data.EventType
import com.applock.nfc.data.UsageStatistic
import com.applock.nfc.ui.screens.HomeScreen
import com.applock.nfc.ui.screens.HomeViewModel
import com.applock.nfc.ui.screens.SettingsScreen
import com.applock.nfc.ui.screens.StatisticsScreen
import com.applock.nfc.ui.theme.AppLockNFCTheme
import com.applock.nfc.utils.NFCHandler
import kotlinx.coroutines.launch

sealed class Screen(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Home : Screen("home", "Home", Icons.Filled.Home, Icons.Outlined.Home)
    object Statistics : Screen("statistics", "Stats", Icons.Filled.BarChart, Icons.Outlined.BarChart)
    object Settings : Screen("settings", "Settings", Icons.Filled.Settings, Icons.Outlined.Settings)
}

class MainActivity : ComponentActivity() {

    private lateinit var nfcHandler: NFCHandler
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nfcHandler = NFCHandler(this)

        setContent {
            AppLockNFCTheme {
                MainScreen(
                    homeViewModel = homeViewModel,
                    nfcAvailable = nfcHandler.isNfcAvailable,
                    nfcEnabled = nfcHandler.isNfcEnabled
                )
            }
        }

        // Handle initial NFC intent
        handleNfcIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleNfcIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        nfcHandler.enableForegroundDispatch()
    }

    override fun onPause() {
        super.onPause()
        nfcHandler.disableForegroundDispatch()
    }

    private fun handleNfcIntent(intent: Intent?) {
        if (intent != null && nfcHandler.handleIntent(intent)) {
            // NFC tag detected - toggle lock state
            lifecycleScope.launch {
                val app = application as AppLockApplication
                val currentLockState = homeViewModel.isLocked.value

                app.preferencesManager.setLocked(!currentLockState)

                // Log the event
                app.repository.addStatistic(
                    UsageStatistic(
                        packageName = "system",
                        appName = "System",
                        eventType = if (!currentLockState) EventType.LOCK_ENABLED else EventType.NFC_UNLOCK
                    )
                )
            }
        }
    }
}

@Composable
fun MainScreen(
    homeViewModel: HomeViewModel,
    nfcAvailable: Boolean,
    nfcEnabled: Boolean
) {
    var selectedScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    val screens = listOf(Screen.Home, Screen.Statistics, Screen.Settings)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = if (selectedScreen == screen)
                                    screen.selectedIcon
                                else
                                    screen.unselectedIcon,
                                contentDescription = screen.title
                            )
                        },
                        label = { Text(screen.title) },
                        selected = selectedScreen == screen,
                        onClick = { selectedScreen = screen },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            selectedTextColor = MaterialTheme.colorScheme.onSurface,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            when (selectedScreen) {
                Screen.Home -> HomeScreen(viewModel = homeViewModel)
                Screen.Statistics -> StatisticsScreen()
                Screen.Settings -> SettingsScreen()
            }
        }
    }
}
