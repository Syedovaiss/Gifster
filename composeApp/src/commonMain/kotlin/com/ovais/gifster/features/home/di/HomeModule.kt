package com.ovais.gifster.features.home.di

import com.ovais.gifster.core.di.BACKGROUND_DISPATCHER
import com.ovais.gifster.features.home.data.DefaultHomeRepository
import com.ovais.gifster.features.home.domain.DefaultGetCategoriesUseCase
import com.ovais.gifster.features.home.domain.DefaultGetTrendingGifsUseCase
import com.ovais.gifster.features.home.domain.DefaultGridSizeProvider
import com.ovais.gifster.features.home.domain.GetCategoriesUseCase
import com.ovais.gifster.features.home.domain.GetTrendingGifsUseCase
import com.ovais.gifster.features.home.domain.GridSizeProvider
import com.ovais.gifster.features.home.domain.HomeRepository
import com.ovais.gifster.features.home.presentation.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val homeFactoryModule = module {
    factory<HomeRepository> {
        DefaultHomeRepository(
            get(),
            get(
                named(
                    BACKGROUND_DISPATCHER
                )
            )
        )
    }
    factory<GetCategoriesUseCase> {
        DefaultGetCategoriesUseCase(
            get(),
        )
    }
    factory<GetTrendingGifsUseCase> {
        DefaultGetTrendingGifsUseCase(
            get(),
        )
    }
    factory<GridSizeProvider> {
        DefaultGridSizeProvider()
    }

}

val homeViewModelModule = module {
    viewModel {
        HomeViewModel(
            get(),
            get(),
            get()
        )
    }
}
