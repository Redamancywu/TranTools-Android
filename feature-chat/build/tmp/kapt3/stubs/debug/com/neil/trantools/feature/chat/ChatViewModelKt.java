package com.neil.trantools.feature.chat;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import com.neil.trantools.core.ui.R;
import com.neil.trantools.data.chat.ChatMessageEntity;
import com.neil.trantools.data.chat.ChatSource;
import com.neil.trantools.data.chat.ChatSourceType;
import com.neil.trantools.data.chat.ChatStore;
import com.neil.trantools.data.gems.GemsRepository;
import com.neil.trantools.data.history.HistoryStore;
import com.neil.trantools.data.wiki.WikiRepository;
import com.neil.trantools.domain.chat.BuildLocalAssistantResponseUseCase;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.flow.StateFlow;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.concurrent.atomic.AtomicLong;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\u001e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u0016\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0002\u001a\u0016\u0010\u0005\u001a\u00020\u00042\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001H\u0002\u001a\f\u0010\u0007\u001a\u00020\b*\u00020\tH\u0002\u00a8\u0006\n"}, d2 = {"deserializeSources", "", "Lcom/neil/trantools/data/chat/ChatSource;", "json", "", "serializeSources", "sources", "toChatMessage", "Lcom/neil/trantools/feature/chat/ChatMessage;", "Lcom/neil/trantools/data/chat/ChatMessageEntity;", "feature-chat_debug"})
public final class ChatViewModelKt {
    
    private static final com.neil.trantools.feature.chat.ChatMessage toChatMessage(com.neil.trantools.data.chat.ChatMessageEntity $this$toChatMessage) {
        return null;
    }
    
    private static final java.lang.String serializeSources(java.util.List<com.neil.trantools.data.chat.ChatSource> sources) {
        return null;
    }
    
    private static final java.util.List<com.neil.trantools.data.chat.ChatSource> deserializeSources(java.lang.String json) {
        return null;
    }
}