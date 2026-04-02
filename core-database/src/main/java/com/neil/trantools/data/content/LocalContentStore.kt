package com.neil.trantools.data.content

import android.content.Context
import androidx.room.Room
import androidx.room.withTransaction
import com.neil.trantools.data.history.AppDatabase

object LocalContentStore {
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
            ).build()
            initialized = true
        }
    }

    suspend fun wikiCount(language: String): Int {
        if (!initialized) return 0
        return db.localContentDao().wikiCount(language)
    }

    suspend fun replaceWikiArticles(
        language: String,
        entities: List<WikiArticleEntity>,
        ftsEntities: List<WikiArticleFtsEntity>,
    ) {
        if (!initialized) return
        db.withTransaction {
            db.localContentDao().clearWikiArticleFts(language)
            db.localContentDao().clearWikiArticles(language)
            db.localContentDao().insertWikiArticles(entities)
            db.localContentDao().insertWikiArticleFts(ftsEntities)
        }
    }

    suspend fun getWikiArticles(language: String): List<WikiArticleEntity> {
        if (!initialized) return emptyList()
        return db.localContentDao().getWikiArticles(language)
    }

    suspend fun searchWikiArticleIds(language: String, query: String, limit: Int): List<String> {
        if (!initialized) return emptyList()
        return db.localContentDao().searchWikiArticleIds(language, query, limit)
    }

    suspend fun poiCount(language: String): Int {
        if (!initialized) return 0
        return db.localContentDao().poiCount(language)
    }

    suspend fun replacePois(
        language: String,
        entities: List<PoiEntity>,
        ftsEntities: List<PoiFtsEntity>,
    ) {
        if (!initialized) return
        db.withTransaction {
            db.localContentDao().clearPoiFts(language)
            db.localContentDao().clearPois(language)
            db.localContentDao().insertPois(entities)
            db.localContentDao().insertPoiFts(ftsEntities)
        }
    }

    suspend fun getPois(language: String): List<PoiEntity> {
        if (!initialized) return emptyList()
        return db.localContentDao().getPois(language)
    }

    suspend fun searchPoiIds(language: String, query: String, limit: Int): List<String> {
        if (!initialized) return emptyList()
        return db.localContentDao().searchPoiIds(language, query, limit)
    }
}
