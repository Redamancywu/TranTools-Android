package com.neil.trantools.data.wiki

enum class WikiCategory(
    val label: String,
) {
    Food("Food"),
    History("History"),
    Culture("Culture"),
    Architecture("Architecture"),
}

data class WikiArticle(
    val id: String,
    val title: String,
    val place: String,
    val category: WikiCategory,
    val summary: String,
    val fact: String,
    val content: List<String>,
    val tags: List<String>,
)
