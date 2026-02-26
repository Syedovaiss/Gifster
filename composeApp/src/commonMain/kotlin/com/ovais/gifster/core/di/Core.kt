package com.ovais.gifster.core.di

import com.ovais.gifster.features.gif_detail.di.gifDetailFactoryModule
import com.ovais.gifster.features.gif_detail.di.gifDetailViewModelModule
import com.ovais.gifster.features.home.di.homeFactoryModule
import com.ovais.gifster.features.home.di.homeViewModelModule
import com.ovais.gifster.features.random.di.randomGifFactoryModule
import com.ovais.gifster.features.random.di.randomGifViewModelModule
import com.ovais.gifster.features.search.di.searchFactoryModule
import com.ovais.gifster.features.search.di.searchViewModelModule
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
            homeViewModelModule,
            // Search
            searchFactoryModule,
            searchViewModelModule,
            // Gif Detail
            gifDetailFactoryModule,
            gifDetailViewModelModule,
            // Random Gif
            randomGifFactoryModule,
            randomGifViewModelModule
        )
    }
}