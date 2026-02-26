package com.ovais.gifster.features.random.presentation

import com.ovais.gifster.core.data.http.Gif

sealed class RandomGifUiState {
    object Idle : RandomGifUiState()
    object Loading : RandomGifUiState()
    data class Success(val gif: Gif) : RandomGifUiState()
    data class Error(val message: String) : RandomGifUiState()
}
