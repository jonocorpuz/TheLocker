package com.applock.nfc.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usage_statistics")
data class UsageStatistic(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val packageName: String,
    val appName: String,
    val eventType: EventType,
    val timestamp: Long = System.currentTimeMillis()
)

enum class EventType {
    LOCK_ENABLED,
    LOCK_DISABLED,
    APP_BLOCKED,
    MANUAL_UNLOCK,
    NFC_UNLOCK,
    AUTO_UNLOCK
}
