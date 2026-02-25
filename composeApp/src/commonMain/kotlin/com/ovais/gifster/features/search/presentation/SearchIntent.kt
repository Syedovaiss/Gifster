package com.ovais.gifster.features.search.presentation

sealed interface SearchIntent {
    data class OnQueryChanged(val query: String) : SearchIntent
    object Retry : SearchIntent
    object LoadMore : SearchIntent
}