package com.ovais.gifster.core

import android.app.Application
import com.ovais.gifster.core.di.initKoin

class Gifster : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}