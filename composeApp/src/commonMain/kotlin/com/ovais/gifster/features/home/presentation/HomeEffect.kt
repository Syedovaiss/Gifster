package com.ovais.gifster.features.home.presentation

sealed interface HomeEffect {
    data class OnNavigateToSearch(val query: String) : HomeEffect
    object OnNavigateToRandom : HomeEffect
    object OnNavigateToGifDetail : HomeEffect
}