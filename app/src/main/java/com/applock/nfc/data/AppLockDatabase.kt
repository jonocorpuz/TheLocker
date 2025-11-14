package com.applock.nfc.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Database(
    entities = [LockedApp::class, UsageStatistic::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppLockDatabase : RoomDatabase() {

    abstract fun appLockDao(): AppLockDao

    companion object {
        @Volatile
        private var INSTANCE: AppLockDatabase? = null

        fun getDatabase(context: Context): AppLockDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppLockDatabase::class.java,
                    "app_lock_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class Converters {
    @TypeConverter
    fun fromEventType(value: EventType): String {
        return value.name
    }

    @TypeConverter
    fun toEventType(value: String): EventType {
        return EventType.valueOf(value)
    }
}
