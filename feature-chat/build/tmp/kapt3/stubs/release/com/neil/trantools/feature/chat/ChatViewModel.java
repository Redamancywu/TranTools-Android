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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016J\u000e\u0010\u0017\u001a\u00020\u0014H\u0082@\u00a2\u0006\u0002\u0010\u0018J\u0010\u0010\u0019\u001a\u00020\u00142\u0006\u0010\u001a\u001a\u00020\u001bH\u0002J\u0006\u0010\u001c\u001a\u00020\u0014J\u0010\u0010\u001d\u001a\u00020\u00142\u0006\u0010\u001e\u001a\u00020\u0016H\u0002J\u000e\u0010\u001f\u001a\u00020\u00142\u0006\u0010 \u001a\u00020\u0016R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\b\u001a\n \n*\u0004\u0018\u00010\t0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00070\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012\u00a8\u0006!"}, d2 = {"Lcom/neil/trantools/feature/chat/ChatViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/neil/trantools/feature/chat/ChatUiState;", "appContext", "Landroid/content/Context;", "kotlin.jvm.PlatformType", "buildAssistantResponse", "Lcom/neil/trantools/domain/chat/BuildLocalAssistantResponseUseCase;", "messageId", "Ljava/util/concurrent/atomic/AtomicLong;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "applyPrefillQuestion", "", "question", "", "loadPersistedMessages", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "persistMessage", "message", "Lcom/neil/trantools/feature/chat/ChatMessage;", "sendCurrentInput", "sendMessage", "raw", "setInput", "value", "feature-chat_release"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class ChatViewModel extends androidx.lifecycle.AndroidViewModel {
    private final android.content.Context appContext = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.concurrent.atomic.AtomicLong messageId = null;
    @org.jetbrains.annotations.NotNull()
    private final com.neil.trantools.domain.chat.BuildLocalAssistantResponseUseCase buildAssistantResponse = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.neil.trantools.feature.chat.ChatUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.neil.trantools.feature.chat.ChatUiState> uiState = null;
    
    @javax.inject.Inject()
    public ChatViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.neil.trantools.feature.chat.ChatUiState> getUiState() {
        return null;
    }
    
    private final java.lang.Object loadPersistedMessages(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    public final void setInput(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void applyPrefillQuestion(@org.jetbrains.annotations.NotNull()
    java.lang.String question) {
    }
    
    public final void sendCurrentInput() {
    }
    
    private final void sendMessage(java.lang.String raw) {
    }
    
    private final void persistMessage(com.neil.trantools.feature.chat.ChatMessage message) {
    }
}