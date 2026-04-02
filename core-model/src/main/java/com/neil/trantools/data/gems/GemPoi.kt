package com.neil.trantools.data.gems

enum class GemCategory {
    Food,
    Culture,
    Nature,
    Viewpoint,
}

data class GemPoi(
    val id: String,
    val title: String,
    val category: GemCategory,
    val summary: String,
    val tip: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val rating: Double,
    val tags: List<String>,
    val bestTime: String = "",
    val recommendedDuration: String = "",
    val budgetLevel: String = "",
)
