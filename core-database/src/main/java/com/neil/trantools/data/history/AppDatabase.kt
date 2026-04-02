package com.neil.trantools.data.history

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.neil.trantools.data.chat.ChatDao
import com.neil.trantools.data.chat.ChatMessageEntity
import com.neil.trantools.data.content.LocalContentDao
import com.neil.trantools.data.content.PoiEntity
import com.neil.trantools.data.content.PoiFtsEntity
import com.neil.trantools.data.content.WikiArticleEntity
import com.neil.trantools.data.content.WikiArticleFtsEntity

@Database(
    entities = [
        TranslationHistoryEntity::class,
        ChatMessageEntity::class,
        WikiArticleEntity::class,
        WikiArticleFtsEntity::class,
        PoiEntity::class,
        PoiFtsEntity::class,
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(HistoryTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): TranslationHistoryDao
    abstract fun chatDao(): ChatDao
    abstract fun localContentDao(): LocalContentDao

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

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `wiki_article` (
                        `id` TEXT NOT NULL,
                        `language` TEXT NOT NULL,
                        `title` TEXT NOT NULL,
                        `place` TEXT NOT NULL,
                        `category` TEXT NOT NULL,
                        `summary` TEXT NOT NULL,
                        `fact` TEXT NOT NULL,
                        `contentBlob` TEXT NOT NULL,
                        `tagsBlob` TEXT NOT NULL,
                        PRIMARY KEY(`id`)
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE VIRTUAL TABLE IF NOT EXISTS `wiki_article_fts`
                    USING fts4(`articleId` TEXT NOT NULL, `language` TEXT NOT NULL, `title` TEXT NOT NULL,
                    `place` TEXT NOT NULL, `summary` TEXT NOT NULL, `fact` TEXT NOT NULL,
                    `content` TEXT NOT NULL, `tags` TEXT NOT NULL)
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `poi` (
                        `id` TEXT NOT NULL,
                        `language` TEXT NOT NULL,
                        `title` TEXT NOT NULL,
                        `category` TEXT NOT NULL,
                        `summary` TEXT NOT NULL,
                        `tip` TEXT NOT NULL,
                        `address` TEXT NOT NULL,
                        `latitude` REAL NOT NULL,
                        `longitude` REAL NOT NULL,
                        `rating` REAL NOT NULL,
                        `tagsBlob` TEXT NOT NULL,
                        `bestTime` TEXT NOT NULL,
                        `recommendedDuration` TEXT NOT NULL,
                        `budgetLevel` TEXT NOT NULL,
                        PRIMARY KEY(`id`)
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE VIRTUAL TABLE IF NOT EXISTS `poi_fts`
                    USING fts4(`poiId` TEXT NOT NULL, `language` TEXT NOT NULL, `title` TEXT NOT NULL,
                    `category` TEXT NOT NULL, `summary` TEXT NOT NULL, `tip` TEXT NOT NULL,
                    `address` TEXT NOT NULL, `tags` TEXT NOT NULL)
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `wiki_article_new` (
                        `id` TEXT NOT NULL,
                        `language` TEXT NOT NULL,
                        `title` TEXT NOT NULL,
                        `place` TEXT NOT NULL,
                        `category` TEXT NOT NULL,
                        `summary` TEXT NOT NULL,
                        `fact` TEXT NOT NULL,
                        `contentBlob` TEXT NOT NULL,
                        `tagsBlob` TEXT NOT NULL,
                        PRIMARY KEY(`id`, `language`)
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    INSERT INTO `wiki_article_new` (`id`, `language`, `title`, `place`, `category`, `summary`, `fact`, `contentBlob`, `tagsBlob`)
                    SELECT `id`, `language`, `title`, `place`, `category`, `summary`, `fact`, `contentBlob`, `tagsBlob`
                    FROM `wiki_article`
                    """.trimIndent()
                )
                db.execSQL("DROP TABLE `wiki_article`")
                db.execSQL("ALTER TABLE `wiki_article_new` RENAME TO `wiki_article`")

                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `poi_new` (
                        `id` TEXT NOT NULL,
                        `language` TEXT NOT NULL,
                        `title` TEXT NOT NULL,
                        `category` TEXT NOT NULL,
                        `summary` TEXT NOT NULL,
                        `tip` TEXT NOT NULL,
                        `address` TEXT NOT NULL,
                        `latitude` REAL NOT NULL,
                        `longitude` REAL NOT NULL,
                        `rating` REAL NOT NULL,
                        `tagsBlob` TEXT NOT NULL,
                        `bestTime` TEXT NOT NULL,
                        `recommendedDuration` TEXT NOT NULL,
                        `budgetLevel` TEXT NOT NULL,
                        PRIMARY KEY(`id`, `language`)
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    INSERT INTO `poi_new` (`id`, `language`, `title`, `category`, `summary`, `tip`, `address`, `latitude`, `longitude`, `rating`, `tagsBlob`, `bestTime`, `recommendedDuration`, `budgetLevel`)
                    SELECT `id`, `language`, `title`, `category`, `summary`, `tip`, `address`, `latitude`, `longitude`, `rating`, `tagsBlob`, `bestTime`, `recommendedDuration`, `budgetLevel`
                    FROM `poi`
                    """.trimIndent()
                )
                db.execSQL("DROP TABLE `poi`")
                db.execSQL("ALTER TABLE `poi_new` RENAME TO `poi`")
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
