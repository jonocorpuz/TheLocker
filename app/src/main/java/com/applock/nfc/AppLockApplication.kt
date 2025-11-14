package com.applock.nfc

import android.app.Application
import com.applock.nfc.data.AppLockDatabase
import com.applock.nfc.data.AppLockRepository
import com.applock.nfc.utils.PreferencesManager

class AppLockApplication : Application() {

    lateinit var database: AppLockDatabase
        private set

    lateinit var repository: AppLockRepository
        private set

    lateinit var preferencesManager: PreferencesManager
        private set

    override fun onCreate() {
        super.onCreate()
        database = AppLockDatabase.getDatabase(this)
        repository = AppLockRepository(database.appLockDao(), this)
        preferencesManager = PreferencesManager(this)
    }
}
