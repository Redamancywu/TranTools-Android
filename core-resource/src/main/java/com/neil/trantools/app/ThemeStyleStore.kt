package com.neil.trantools.core.resource

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.neil.trantools.ui.theme.AppThemeStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore by preferencesDataStore(name = "tran_tools_settings")
private val themeStyleKey = stringPreferencesKey("theme_style")

object ThemeStyleStore {
    fun observe(context: Context): Flow<AppThemeStyle> {
        return context.settingsDataStore.data.map { prefs ->
            AppThemeStyle.fromKey(prefs[themeStyleKey])
        }
    }

    suspend fun set(context: Context, style: AppThemeStyle) {
        context.settingsDataStore.edit { prefs ->
            prefs[themeStyleKey] = style.key
        }
    }
}
