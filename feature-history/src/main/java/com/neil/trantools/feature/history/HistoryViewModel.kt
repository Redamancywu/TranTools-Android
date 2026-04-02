package com.neil.trantools.feature.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neil.trantools.data.history.HistoryStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            runCatching {
                HistoryStore.observeAll().collect { list ->
                    _uiState.update { it.copy(items = list) }
                }
            }
        }
    }

    fun setFilter(filter: HistoryFilter) {
        _uiState.update { it.copy(filter = filter) }
    }

    fun clearAll() {
        viewModelScope.launch {
            HistoryStore.clearAll()
        }
    }
}
