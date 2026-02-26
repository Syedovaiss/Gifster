package com.ovais.gifster.features.gif_detail.presentation

sealed class GifDetailUiState {
    object Idle : GifDetailUiState()
    object Downloading : GifDetailUiState()
    data class Error(val message: String) : GifDetailUiState()
    data class Downloaded(val filePath: String) : GifDetailUiState()
}