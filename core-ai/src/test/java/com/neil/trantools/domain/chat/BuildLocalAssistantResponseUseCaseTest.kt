package com.neil.trantools.domain.chat

import com.neil.trantools.data.gems.GemCategory
import com.neil.trantools.data.gems.GemPoi
import com.neil.trantools.data.history.HistoryMode
import com.neil.trantools.data.history.TranslationHistoryEntity
import com.neil.trantools.data.wiki.WikiArticle
import com.neil.trantools.data.wiki.WikiCategory
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine

class BuildLocalAssistantResponseUseCaseTest {
    @Test
    fun `combine wiki gem and history sources into one answer`() {
        val useCase = BuildLocalAssistantResponseUseCase(
            wikiProvider = {
                listOf(
                    WikiArticle(
                        id = "kiyomizu",
                        title = "Kiyomizu-dera",
                        place = "Kyoto",
                        category = WikiCategory.Culture,
                        summary = "A hillside temple with a famous wooden stage.",
                        fact = "Go early for fewer crowds.",
                        content = listOf("The temple is known for city views and historic architecture."),
                        tags = listOf("Temple")
                    )
                )
            },
            gemsProvider = {
                listOf(
                    GemPoi(
                        id = "kiyomizu-dera",
                        title = "Kiyomizu-dera",
                        category = GemCategory.Culture,
                        summary = "A hillside temple complex.",
                        tip = "Arrive early.",
                        address = "Kyoto",
                        latitude = 0.0,
                        longitude = 0.0,
                        rating = 4.8,
                        tags = listOf("Temple")
                    )
                )
            },
            historyProvider = {
                listOf(
                    TranslationHistoryEntity(
                        mode = HistoryMode.TEXT,
                        sourceLanguage = "en",
                        targetLanguage = "ja",
                        sourceText = "Where is Kiyomizu-dera?",
                        translatedText = "清水寺はどこですか？"
                    )
                )
            },
            fallbackAnswer = "No match",
            translateHistoryLabel = "Translate history",
            photoHistoryLabel = "Photo history",
            voiceHistoryLabel = "Voice history"
        )

        val response = runSuspend {
            useCase(
                question = "What should I know before visiting Kiyomizu-dera?",
                previousUserTurns = emptyList()
            )
        }

        assertTrue(response.answer.contains("Kiyomizu-dera"))
        assertTrue(response.sources.any { it.title.contains("Kiyomizu-dera") })
        assertTrue(response.sources.any { it.subtitle == "Translate history" })
    }
}

private fun <T> runSuspend(block: suspend () -> T): T {
    var value: T? = null
    var error: Throwable? = null
    block.startCoroutine(
        object : Continuation<T> {
            override val context = EmptyCoroutineContext

            override fun resumeWith(result: Result<T>) {
                result
                    .onSuccess { value = it }
                    .onFailure { error = it }
            }
        }
    )
    error?.let { throw it }
    return requireNotNull(value)
}
