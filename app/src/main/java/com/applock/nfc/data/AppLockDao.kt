package com.applock.nfc.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppLockDao {

    // Locked Apps
    @Query("SELECT * FROM locked_apps ORDER BY appName ASC")
    fun getAllLockedApps(): Flow<List<LockedApp>>

    @Query("SELECT * FROM locked_apps WHERE packageName = :packageName")
    suspend fun getLockedApp(packageName: String): LockedApp?

    @Query("SELECT EXISTS(SELECT 1 FROM locked_apps WHERE packageName = :packageName LIMIT 1)")
    suspend fun isAppLocked(packageName: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLockedApp(app: LockedApp)

    @Delete
    suspend fun deleteLockedApp(app: LockedApp)

    @Query("DELETE FROM locked_apps WHERE packageName = :packageName")
    suspend fun deleteLockedAppByPackage(packageName: String)

    @Query("DELETE FROM locked_apps")
    suspend fun deleteAllLockedApps()

    // Usage Statistics
    @Query("SELECT * FROM usage_statistics ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentStatistics(limit: Int = 100): Flow<List<UsageStatistic>>

    @Query("SELECT * FROM usage_statistics WHERE packageName = :packageName ORDER BY timestamp DESC")
    fun getStatisticsForApp(packageName: String): Flow<List<UsageStatistic>>

    @Query("SELECT COUNT(*) FROM usage_statistics WHERE eventType = 'APP_BLOCKED'")
    fun getTotalBlockedAttempts(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStatistic(stat: UsageStatistic)

    @Query("DELETE FROM usage_statistics WHERE timestamp < :beforeTimestamp")
    suspend fun deleteOldStatistics(beforeTimestamp: Long)

    @Query("DELETE FROM usage_statistics")
    suspend fun deleteAllStatistics()
}
