package com.ovais.gifster.features.home.presentation

import com.ovais.gifster.core.data.http.Category
import com.ovais.gifster.core.data.http.Gif

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(
        val categories: List<Category>,
        val trendingGifs: List<Gif>,
        val isLoadingMore: Boolean = false,
        val column: Int
    ) : HomeUiState()

    data class Error(val message: String) : HomeUiState()
}