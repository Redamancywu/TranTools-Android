package com.neil.trantools.data.history

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.neil.trantools.data.chat.ChatDao
import com.neil.trantools.data.chat.ChatMessageEntity

@Database(
    entities = [TranslationHistoryEntity::class, ChatMessageEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(HistoryTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): TranslationHistoryDao
    abstract fun chatDao(): ChatDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `chat_message` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `role` TEXT NOT NULL,
                        `text` TEXT NOT NULL,
                        `sourcesJson` TEXT NOT NULL,
                        `createdAtEpochMs` INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }
    }
}

class HistoryTypeConverters {
    @TypeConverter
    fun fromMode(mode: HistoryMode): String = mode.name

    @TypeConverter
    fun toMode(raw: String): HistoryMode = runCatching { HistoryMode.valueOf(raw) }
        .getOrDefault(HistoryMode.OCR)
}
