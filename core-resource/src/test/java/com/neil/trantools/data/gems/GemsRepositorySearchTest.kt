package com.neil.trantools.data.gems

import org.junit.Assert.assertEquals
import org.junit.Test

class GemsRepositorySearchTest {
    @Test
    fun `search filters by category and sorts by distance`() {
        val pois = listOf(
            GemPoi(
                id = "far-food",
                title = "Far Food",
                category = GemCategory.Food,
                summary = "Food market",
                tip = "Share bites",
                address = "A",
                latitude = 35.02,
                longitude = 135.80,
                rating = 4.5,
                tags = listOf("Market")
            ),
            GemPoi(
                id = "near-food",
                title = "Near Food",
                category = GemCategory.Food,
                summary = "Temple snack",
                tip = "Go early",
                address = "B",
                latitude = 35.0117,
                longitude = 135.7682,
                rating = 4.8,
                tags = listOf("Temple")
            ),
            GemPoi(
                id = "culture",
                title = "Culture Spot",
                category = GemCategory.Culture,
                summary = "Museum",
                tip = "Visit at dusk",
                address = "C",
                latitude = 35.0119,
                longitude = 135.7683,
                rating = 4.9,
                tags = listOf("Museum")
            )
        )

        val result = GemsRepository.search(
            pois = pois,
            query = "food",
            category = GemCategory.Food,
            currentLocation = GeoPoint(35.0116, 135.7681, "center", true)
        )

        assertEquals(listOf("near-food", "far-food"), result.map { it.poi.id })
    }
}
