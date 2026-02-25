package com.ovais.gifster.features.home.presentation

sealed interface HomeIntent {
    data class OnSearchGif(val query: String) : HomeIntent
    object OnGenerateRandom : HomeIntent
    object OnGifClicked : HomeIntent

    object OnLoadMore : HomeIntent
    object Retry : HomeIntent
}