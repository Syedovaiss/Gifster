package com.ovais.gifster.features.home.domain

import com.ovais.gifster.utils.Platform
import com.ovais.gifster.utils.Provider
import com.ovais.gifster.utils.getPlatform

fun interface GridSizeProvider : Provider<Int>

class DefaultGridSizeProvider : GridSizeProvider {

    override fun get(): Int {
        return when (getPlatform()) {
            Platform.ANDROID -> 2
            Platform.DESKTOP -> 6
            Platform.IOS -> 3
        }
    }
}