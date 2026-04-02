package com.neil.trantools.data.content

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LocalContentDao {
    @Query("SELECT COUNT(*) FROM wiki_article WHERE language = :language")
    suspend fun wikiCount(language: String): Int

    @Query("DELETE FROM wiki_article WHERE language = :language")
    suspend fun clearWikiArticles(language: String)

    @Query("DELETE FROM wiki_article_fts WHERE language = :language")
    suspend fun clearWikiArticleFts(language: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWikiArticles(items: List<WikiArticleEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWikiArticleFts(items: List<WikiArticleFtsEntity>)

    @Query("SELECT * FROM wiki_article WHERE language = :language ORDER BY title ASC")
    suspend fun getWikiArticles(language: String): List<WikiArticleEntity>

    @Query("SELECT articleId FROM wiki_article_fts WHERE language = :language AND wiki_article_fts MATCH :query LIMIT :limit")
    suspend fun searchWikiArticleIds(language: String, query: String, limit: Int): List<String>

    @Query("SELECT COUNT(*) FROM poi WHERE language = :language")
    suspend fun poiCount(language: String): Int

    @Query("DELETE FROM poi WHERE language = :language")
    suspend fun clearPois(language: String)

    @Query("DELETE FROM poi_fts WHERE language = :language")
    suspend fun clearPoiFts(language: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPois(items: List<PoiEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPoiFts(items: List<PoiFtsEntity>)

    @Query("SELECT * FROM poi WHERE language = :language ORDER BY title ASC")
    suspend fun getPois(language: String): List<PoiEntity>

    @Query("SELECT poiId FROM poi_fts WHERE language = :language AND poi_fts MATCH :query LIMIT :limit")
    suspend fun searchPoiIds(language: String, query: String, limit: Int): List<String>
}
