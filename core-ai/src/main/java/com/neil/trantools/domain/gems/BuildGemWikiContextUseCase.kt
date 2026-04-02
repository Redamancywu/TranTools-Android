package com.neil.trantools.domain.gems

import android.content.Context
import com.neil.trantools.data.gems.GemPoi
import com.neil.trantools.data.gems.GemWikiBridge
import com.neil.trantools.data.gems.GemWikiContext

class BuildGemWikiContextUseCase(
    private val context: Context,
    private val questionTemplate: (String) -> String,
    private val fallbackAnswer: String,
) {
    operator fun invoke(poi: GemPoi): GemWikiContext? {
        return GemWikiBridge.buildContext(
            context = context,
            poi = poi,
            questionTemplate = questionTemplate,
            fallbackAnswer = fallbackAnswer
        )
    }
}
