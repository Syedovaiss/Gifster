package com.ovais.gifster.core

import android.app.Application
import com.ovais.gifster.core.di.initKoin
import org.koin.android.ext.koin.androidContext

class Gifster : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoinAndroid(this)
    }
    fun initKoinAndroid(application: Application) {
        initKoin {
            androidContext(application)
        }
    }
}