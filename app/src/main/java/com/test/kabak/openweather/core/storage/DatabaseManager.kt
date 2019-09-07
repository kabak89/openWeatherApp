package com.test.kabak.openweather.core.storage

import android.content.Context
import androidx.room.Room

object DatabaseManager {
    private var database: LocalDatabase? = null

    fun init(context: Context) {
        if (database == null) {
            val applicationContext = context.applicationContext

            database = Room
                    .databaseBuilder(applicationContext, LocalDatabase::class.java, "database")
                    .fallbackToDestructiveMigration()
                    .build()
        }
    }

    fun getDb(): LocalDatabase {
        if (database == null) {
            throw RuntimeException("Call init() first")
        } else {
            return database!!
        }
    }
}