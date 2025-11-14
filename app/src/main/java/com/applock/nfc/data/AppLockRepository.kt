package com.applock.nfc.data

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class AppInfo(
    val packageName: String,
    val appName: String,
    val isSystemApp: Boolean,
    val isLocked: Boolean = false
)

class AppLockRepository(private val dao: AppLockDao, private val context: Context) {

    val lockedApps: Flow<List<LockedApp>> = dao.getAllLockedApps()

    val recentStatistics: Flow<List<UsageStatistic>> = dao.getRecentStatistics()

    val totalBlockedAttempts: Flow<Int> = dao.getTotalBlockedAttempts()

    suspend fun getInstalledApps(): List<AppInfo> {
        val pm = context.packageManager
        val installedApps = pm.getInstalledApplications(PackageManager.GET_META_DATA)

        return installedApps
            .filter { app ->
                // Filter out system apps and this app
                (app.flags and ApplicationInfo.FLAG_SYSTEM == 0) &&
                app.packageName != context.packageName
            }
            .map { app ->
                AppInfo(
                    packageName = app.packageName,
                    appName = pm.getApplicationLabel(app).toString(),
                    isSystemApp = (app.flags and ApplicationInfo.FLAG_SYSTEM != 0),
                    isLocked = dao.isAppLocked(app.packageName)
                )
            }
            .sortedBy { it.appName.lowercase() }
    }

    suspend fun toggleAppLock(appInfo: AppInfo) {
        if (appInfo.isLocked) {
            dao.deleteLockedAppByPackage(appInfo.packageName)
        } else {
            dao.insertLockedApp(
                LockedApp(
                    packageName = appInfo.packageName,
                    appName = appInfo.appName,
                    isLocked = true
                )
            )
        }
    }

    suspend fun isAppLocked(packageName: String): Boolean {
        return dao.isAppLocked(packageName)
    }

    suspend fun addStatistic(stat: UsageStatistic) {
        dao.insertStatistic(stat)
    }

    suspend fun clearAllLocks() {
        dao.deleteAllLockedApps()
    }

    suspend fun clearOldStatistics(daysOld: Int = 30) {
        val cutoffTime = System.currentTimeMillis() - (daysOld * 24 * 60 * 60 * 1000L)
        dao.deleteOldStatistics(cutoffTime)
    }
}
