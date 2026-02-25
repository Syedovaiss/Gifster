package com.ovais.gifster.features.search.presentation

import com.ovais.gifster.core.data.http.Gif

sealed class SearchUiState {

    object Idle : SearchUiState()

    data class Loading(
        val query: String
    ) : SearchUiState()

    data class Success(
        val query: String,
        val results: List<Gif>,
        val isLoadingMore: Boolean = false
    ) : SearchUiState()

    data class Empty(
        val query: String,
        val isLoading: Boolean = false
    ) : SearchUiState()

    data class Error(
        val query: String,
        val message: String
    ) : SearchUiState()
}