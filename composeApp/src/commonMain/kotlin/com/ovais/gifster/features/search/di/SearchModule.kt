package com.ovais.gifster.features.search.di

import com.ovais.gifster.core.di.BACKGROUND_DISPATCHER
import com.ovais.gifster.features.search.data.SearchRepository
import com.ovais.gifster.features.search.domain.DefaultSearchGifUseCase
import com.ovais.gifster.features.search.domain.DefaultSearchRepository
import com.ovais.gifster.features.search.domain.SearchGifUseCase
import com.ovais.gifster.features.search.presentation.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val searchFactoryModule = module {
    factory<SearchRepository> {
        DefaultSearchRepository(
            get(),
            get(
                named(BACKGROUND_DISPATCHER)
            )
        )
    }

    factory<SearchGifUseCase> { DefaultSearchGifUseCase(get()) }
}
val searchViewModelModule = module {
    viewModel {
        SearchViewModel(get())
    }
}