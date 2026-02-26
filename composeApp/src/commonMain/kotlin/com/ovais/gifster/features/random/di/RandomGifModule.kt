package com.ovais.gifster.features.random.di

import com.ovais.gifster.core.di.BACKGROUND_DISPATCHER
import com.ovais.gifster.features.random.data.RandomGifRepository
import com.ovais.gifster.features.random.domain.DefaultGenerateRandomGifUseCase
import com.ovais.gifster.features.random.domain.DefaultRandomGifRepository
import com.ovais.gifster.features.random.domain.GenerateRandomGifUseCase
import com.ovais.gifster.features.random.presentation.RandomGifViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module


val randomGifFactoryModule = module {
    factory<RandomGifRepository> {
        DefaultRandomGifRepository(
            get(),
            get(
                named(BACKGROUND_DISPATCHER)
            )
        )
    }
    factory<GenerateRandomGifUseCase> {
        DefaultGenerateRandomGifUseCase(
            get()
        )
    }
}

val randomGifViewModelModule = module {
    viewModel {
        RandomGifViewModel(get())
    }
}