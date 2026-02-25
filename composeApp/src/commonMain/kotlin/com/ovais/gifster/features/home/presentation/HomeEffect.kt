package com.ovais.gifster.features.home.presentation

import com.ovais.gifster.core.data.http.Gif

sealed interface HomeEffect {
    object OnNavigateToSearch : HomeEffect
    object OnNavigateToRandom : HomeEffect
    data class OnNavigateToGifDetail(val item: Gif) : HomeEffect
}