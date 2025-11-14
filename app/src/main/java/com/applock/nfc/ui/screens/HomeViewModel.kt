package com.applock.nfc.ui.screens

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.applock.nfc.AppLockApplication
import com.applock.nfc.data.AppInfo
import com.applock.nfc.data.EventType
import com.applock.nfc.data.UsageStatistic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as AppLockApplication
    private val repository = app.repository
    private val preferencesManager = app.preferencesManager

    private val _installedApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val installedApps: StateFlow<List<AppInfo>> = _installedApps.asStateFlow()

    val isLocked: StateFlow<Boolean> = preferencesManager.isLocked
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val autoUnlockDuration: StateFlow<Int> = preferencesManager.autoUnlockDuration
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 20)

    val lockTimestamp: StateFlow<Long> = preferencesManager.lockTimestamp
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    val lockedAppsCount: StateFlow<Int> = repository.lockedApps
        .combine(_installedApps) { lockedApps, _ ->
            lockedApps.size
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    init {
        loadInstalledApps()
    }

    fun loadInstalledApps() {
        viewModelScope.launch {
            _installedApps.value = repository.getInstalledApps()
        }
    }

    fun toggleAppLock(appInfo: AppInfo) {
        viewModelScope.launch {
            repository.toggleAppLock(appInfo)
            loadInstalledApps()
        }
    }

    fun toggleLockState() {
        viewModelScope.launch {
            val currentLockState = isLocked.value
            preferencesManager.setLocked(!currentLockState)

            // Log the event
            repository.addStatistic(
                UsageStatistic(
                    packageName = "system",
                    appName = "System",
                    eventType = if (!currentLockState) EventType.LOCK_ENABLED else EventType.LOCK_DISABLED
                )
            )
        }
    }
}
