package com.example.videocatalog.ui

import com.example.videocatalog.VideoInfo

sealed interface UiState {
    data object Loading : UiState
    data class Loaded(val videos: List<VideoInfo>) : UiState
    data class Error(val message: String) : UiState
}
