package com.ovais.gifster.core.di

import com.ovais.gifster.features.home.di.homeFactoryModule
import com.ovais.gifster.features.home.di.homeViewModelModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration


fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            //Core
            singletonModule,
            platformModule,
            networkModule,
            // Home
            homeFactoryModule,
            homeViewModelModule
        )
    }
}