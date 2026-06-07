package com.example

import android.app.Application
import com.example.data.AppDatabase
import com.example.data.SmartLaunchRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class SmartLaunchApp : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { SmartLaunchRepository(database.dao()) }

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            repository.prepopulateIfEmpty()
        }
    }
}
