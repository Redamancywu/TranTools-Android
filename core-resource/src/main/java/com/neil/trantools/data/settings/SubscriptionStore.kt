package com.neil.trantools.data.settings

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.subscriptionDataStore by preferencesDataStore(name = "subscription_store")
private val subscriptionStateKey = stringPreferencesKey("subscription_state")

enum class LocalSubscriptionState {
    Free,
    Pro
}

object SubscriptionStore {
    fun observe(context: Context): Flow<LocalSubscriptionState> {
        return context.subscriptionDataStore.data.map { prefs ->
            runCatching { LocalSubscriptionState.valueOf(prefs[subscriptionStateKey] ?: LocalSubscriptionState.Free.name) }
                .getOrDefault(LocalSubscriptionState.Free)
        }
    }

    suspend fun set(context: Context, state: LocalSubscriptionState) {
        context.subscriptionDataStore.edit { prefs ->
            prefs[subscriptionStateKey] = state.name
        }
    }
}
