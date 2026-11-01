package com.veylqo.recora.model

sealed interface RecordingState {
    data object Idle : RecordingState
    data class CountingDown(val secondsRemaining: Int) : RecordingState
    data class Recording(val durationMs: Long) : RecordingState
    data class Paused(val durationMs: Long) : RecordingState
    data class Error(val message: String) : RecordingState
}
