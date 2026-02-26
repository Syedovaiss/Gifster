package com.ovais.gifster.core.navigation

import com.ovais.gifster.core.data.http.Gif

sealed interface Routes {
    data object Home : Routes
    data object Search : Routes
    data class GifDetail(val gif: Gif) : Routes
}