package com.ovais.gifster.features.gif_detail.di

import com.ovais.gifster.features.gif_detail.domain.GifFileDownloader
import com.ovais.gifster.features.gif_detail.presentation.GifDetailViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val gifDetailFactoryModule = module {
    factory<GifFileDownloader> { GifFileDownloader() }
}

val gifDetailViewModelModule = module {
    viewModel {
        GifDetailViewModel(get())

    }
}