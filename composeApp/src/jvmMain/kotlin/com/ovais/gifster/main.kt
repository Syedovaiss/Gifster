package com.ovais.gifster

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.ovais.gifster.core.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Gifster",
        ) {
            App()
        }
    }
}