package com.ovais.gifster.utils

enum class Platform {
    ANDROID, DESKTOP, IOS
}


expect fun getPlatform(): Platform