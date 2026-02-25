package com.ovais.gifster.features.home.presentation

import com.ovais.gifster.core.data.http.Gif

sealed interface HomeIntent {
    object OnSearchGif : HomeIntent
    object OnGenerateRandom : HomeIntent
    data class OnGifClicked(val item: Gif) : HomeIntent
    object OnLoadMore : HomeIntent
    object Retry : HomeIntent
}