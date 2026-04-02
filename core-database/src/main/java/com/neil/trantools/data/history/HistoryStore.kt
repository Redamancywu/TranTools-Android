package com.neil.trantools.data.history

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.flow.Flow

object HistoryStore {
    @Volatile
    private var initialized = false
    private lateinit var db: AppDatabase

    fun init(context: Context) {
        if (initialized) return
        synchronized(this) {
            if (initialized) return
            db = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "tran_tools.db"
            ).addMigrations(
                AppDatabase.MIGRATION_1_2,
                AppDatabase.MIGRATION_2_3
            )
                .build()
            initialized = true
        }
    }

    fun observeAll(): Flow<List<TranslationHistoryEntity>> {
        check(initialized) { "HistoryStore not initialized" }
        return db.historyDao().observeAll()
    }

    suspend fun insert(item: TranslationHistoryEntity) {
        if (!initialized) return
        db.historyDao().insert(item)
    }

    suspend fun getRecent(limit: Int): List<TranslationHistoryEntity> {
        if (!initialized) return emptyList()
        return db.historyDao().getRecent(limit)
    }

    suspend fun clearAll() {
        if (!initialized) return
        db.historyDao().clearAll()
    }
}
