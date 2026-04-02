package com.neil.trantools.data.content

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Entity(tableName = "wiki_article")
data class WikiArticleEntity(
    @PrimaryKey
    val id: String,
    val language: String,
    val title: String,
    val place: String,
    val category: String,
    val summary: String,
    val fact: String,
    val contentBlob: String,
    val tagsBlob: String,
)

@Fts4
@Entity(tableName = "wiki_article_fts")
data class WikiArticleFtsEntity(
    @PrimaryKey
    @ColumnInfo(name = "rowid")
    val rowId: Int,
    val articleId: String,
    val language: String,
    val title: String,
    val place: String,
    val summary: String,
    val fact: String,
    val content: String,
    val tags: String,
)

@Entity(tableName = "poi")
data class PoiEntity(
    @PrimaryKey
    val id: String,
    val language: String,
    val title: String,
    val category: String,
    val summary: String,
    val tip: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val rating: Double,
    val tagsBlob: String,
    val bestTime: String,
    val recommendedDuration: String,
    val budgetLevel: String,
)

@Fts4
@Entity(tableName = "poi_fts")
data class PoiFtsEntity(
    @PrimaryKey
    @ColumnInfo(name = "rowid")
    val rowId: Int,
    val poiId: String,
    val language: String,
    val title: String,
    val category: String,
    val summary: String,
    val tip: String,
    val address: String,
    val tags: String,
)
