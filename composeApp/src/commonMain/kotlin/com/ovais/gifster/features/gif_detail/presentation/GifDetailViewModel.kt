package com.ovais.gifster.features.gif_detail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovais.gifster.features.gif_detail.domain.GifFileDownloader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GifDetailViewModel(
    private val fileDownloader: GifFileDownloader
) : ViewModel() {

    private val _uiState = MutableStateFlow<GifDetailUiState>(GifDetailUiState.Idle)
    val uiState: StateFlow<GifDetailUiState> = _uiState

    fun downloadGif(url: String) {
        viewModelScope.launch {
            _uiState.value = GifDetailUiState.Downloading

            try {
                val filePath = fileDownloader.download(url)
                _uiState.value = GifDetailUiState.Downloaded(filePath)
            } catch (e: Exception) {
                _uiState.value = GifDetailUiState.Error(
                    e.message ?: "Download failed"
                )
            }
        }
    }
}