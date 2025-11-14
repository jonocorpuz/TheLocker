package com.applock.nfc.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import com.applock.nfc.AppLockApplication
import com.applock.nfc.data.EventType
import com.applock.nfc.data.UsageStatistic
import com.applock.nfc.ui.screens.LockOverlayActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first

class AppLockAccessibilityService : AccessibilityService() {

    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private lateinit var app: AppLockApplication
    private var lastPackageName: String? = null

    override fun onCreate() {
        super.onCreate()
        app = application as AppLockApplication
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                val packageName = event.packageName?.toString() ?: return

                // Ignore system UI and our own app
                if (packageName.startsWith("com.android") ||
                    packageName == app.packageName ||
                    packageName == lastPackageName
                ) {
                    return
                }

                lastPackageName = packageName
                checkAndLockApp(packageName)
            }
        }
    }

    private fun checkAndLockApp(packageName: String) {
        serviceScope.launch {
            try {
                // Check if lock is enabled
                val isLocked = app.preferencesManager.isLocked.first()
                if (!isLocked) return@launch

                // Check if this specific app should be locked
                val shouldLock = app.repository.isAppLocked(packageName)
                if (!shouldLock) return@launch

                // Get app name
                val pm = packageManager
                val appName = try {
                    val appInfo = pm.getApplicationInfo(packageName, 0)
                    pm.getApplicationLabel(appInfo).toString()
                } catch (e: Exception) {
                    packageName
                }

                // Log the blocked attempt
                app.repository.addStatistic(
                    UsageStatistic(
                        packageName = packageName,
                        appName = appName,
                        eventType = EventType.APP_BLOCKED
                    )
                )

                // Show lock overlay
                withContext(Dispatchers.Main) {
                    val intent = Intent(this@AppLockAccessibilityService, LockOverlayActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        putExtra("packageName", packageName)
                        putExtra("appName", appName)
                    }
                    startActivity(intent)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onInterrupt() {
        // Required by AccessibilityService
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}
