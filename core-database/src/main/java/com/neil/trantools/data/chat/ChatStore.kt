package com.neil.trantools.data.chat

import android.content.Context
import androidx.room.Room
import com.neil.trantools.data.history.AppDatabase
import kotlinx.coroutines.flow.Flow

object ChatStore {
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

    fun observeAll(): Flow<List<ChatMessageEntity>> {
        check(initialized) { "ChatStore not initialized" }
        return db.chatDao().observeAll()
    }

    suspend fun insert(message: ChatMessageEntity): Long {
        if (!initialized) return -1L
        return db.chatDao().insert(message)
    }

    suspend fun getAll(): List<ChatMessageEntity> {
        if (!initialized) return emptyList()
        return db.chatDao().getAll()
    }

    suspend fun clearAll() {
        if (!initialized) return
        db.chatDao().clearAll()
    }
}
