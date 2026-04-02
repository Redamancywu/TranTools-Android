package com.neil.trantools.feature.settings;

import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.material.icons.Icons;
import androidx.compose.material3.CardDefaults;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.tooling.preview.Preview;
import com.neil.trantools.core.ui.R;
import com.neil.trantools.data.settings.CityPackInfo;
import com.neil.trantools.data.settings.ModelPackInfo;
import com.neil.trantools.data.settings.StorageSummary;
import com.neil.trantools.data.translation.TranslationPackStatus;
import com.neil.trantools.feature.translate.TranslateLanguageOption;
import com.neil.trantools.ui.theme.AppThemeStyle;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0004\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004\u00a8\u0006\u0005"}, d2 = {"Lcom/neil/trantools/feature/settings/SubscriptionState;", "", "(Ljava/lang/String;I)V", "Free", "Pro", "feature-settings_release"})
public enum SubscriptionState {
    /*public static final*/ Free /* = new Free() */,
    /*public static final*/ Pro /* = new Pro() */;
    
    SubscriptionState() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.neil.trantools.feature.settings.SubscriptionState> getEntries() {
        return null;
    }
}