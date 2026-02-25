package com.ovais.gifster.core.di

import com.ovais.gifster.core.data.network.NetworkFactory
import org.koin.dsl.module


val networkModule = module {
    single { NetworkFactory.create(get()) }
}