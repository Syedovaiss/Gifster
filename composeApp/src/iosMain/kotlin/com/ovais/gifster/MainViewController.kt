package com.ovais.gifster

import androidx.compose.ui.window.ComposeUIViewController
import com.ovais.gifster.core.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }