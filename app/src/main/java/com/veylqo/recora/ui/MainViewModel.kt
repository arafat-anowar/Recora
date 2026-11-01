package com.veylqo.recora.ui

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.veylqo.recora.data.MediaStoreHelper
import com.veylqo.recora.model.RecordingConfig
import com.veylqo.recora.model.RecordingItem
import com.veylqo.recora.model.RecordingState
import com.veylqo.recora.model.StorageStats
import com.veylqo.recora.ui.components.RecoraTab
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _selectedTab = MutableStateFlow(RecoraTab.RECORD)
    val selectedTab: StateFlow<RecoraTab> = _selectedTab.asStateFlow()

    private val _recordingConfig = MutableStateFlow(RecordingConfig())
    val recordingConfig: StateFlow<RecordingConfig> = _recordingConfig.asStateFlow()

    private val _recordingState = MutableStateFlow<RecordingState>(RecordingState.Idle)
    val recordingState: StateFlow<RecordingState> = _recordingState.asStateFlow()

    private val _recordingsList = MutableStateFlow<List<RecordingItem>>(emptyList())
    val recordingsList: StateFlow<List<RecordingItem>> = _recordingsList.asStateFlow()

    private val _storageStats = MutableStateFlow(StorageStats())
    val storageStats: StateFlow<StorageStats> = _storageStats.asStateFlow()

    fun selectTab(tab: RecoraTab) {
        _selectedTab.value = tab
    }

    fun updateConfig(config: RecordingConfig) {
        _recordingConfig.value = config
    }

    fun setRecordingState(state: RecordingState) {
        _recordingState.value = state
    }

    fun loadRecordings(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val items = MediaStoreHelper.queryRecordings(context)
            val stats = MediaStoreHelper.getStorageStats(context, items)
            _recordingsList.value = items
            _storageStats.value = stats
        }
    }

    fun deleteRecording(context: Context, uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val success = MediaStoreHelper.deleteRecording(context, uri)
            if (success) {
                loadRecordings(context)
            }
        }
    }
}
