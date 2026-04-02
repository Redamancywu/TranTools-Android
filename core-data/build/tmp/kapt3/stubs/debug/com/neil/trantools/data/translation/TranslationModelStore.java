package com.neil.trantools.data.translation;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\rJ\u0016\u0010\u000e\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\rJ\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u000b\u001a\u00020\fH\u0002J\u0012\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u0012J\u000e\u0010\u0013\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u0014J\"\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0016\u001a\u00020\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0002R\u001a\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lcom/neil/trantools/data/translation/TranslationModelStore;", "", "()V", "_statuses", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "Lcom/neil/trantools/data/translation/TranslationPackStatus;", "remoteModelManager", "Lcom/google/mlkit/common/model/RemoteModelManager;", "delete", "", "language", "Lcom/neil/trantools/feature/translate/TranslateLanguageOption;", "(Lcom/neil/trantools/feature/translate/TranslateLanguageOption;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "download", "modelFor", "Lcom/google/mlkit/nl/translate/TranslateRemoteModel;", "observeStatuses", "Lkotlinx/coroutines/flow/StateFlow;", "refresh", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateStatus", "isBusy", "", "lastError", "", "core-data_debug"})
public final class TranslationModelStore {
    @org.jetbrains.annotations.NotNull()
    private static final com.google.mlkit.common.model.RemoteModelManager remoteModelManager = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.neil.trantools.data.translation.TranslationPackStatus>> _statuses = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.neil.trantools.data.translation.TranslationModelStore INSTANCE = null;
    
    private TranslationModelStore() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.neil.trantools.data.translation.TranslationPackStatus>> observeStatuses() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object refresh(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object download(@org.jetbrains.annotations.NotNull()
    com.neil.trantools.feature.translate.TranslateLanguageOption language, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object delete(@org.jetbrains.annotations.NotNull()
    com.neil.trantools.feature.translate.TranslateLanguageOption language, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final void updateStatus(com.neil.trantools.feature.translate.TranslateLanguageOption language, boolean isBusy, java.lang.String lastError) {
    }
    
    private final com.google.mlkit.nl.translate.TranslateRemoteModel modelFor(com.neil.trantools.feature.translate.TranslateLanguageOption language) {
        return null;
    }
}