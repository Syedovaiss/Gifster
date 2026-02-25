package com.ovais.gifster.core.navigation

sealed interface Routes {
    data object Home : Routes
    data object Search : Routes
}