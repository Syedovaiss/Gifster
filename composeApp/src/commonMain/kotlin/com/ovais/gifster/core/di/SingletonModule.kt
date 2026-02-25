package com.ovais.gifster.core.di

import com.ovais.gifster.core.data.network.DefaultNetworkClient
import com.ovais.gifster.core.data.network.NetworkClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.math.sin

const val DEFAULT_DISPATCHER = "DEFAULT_DISPATCHER"
const val MAIN_DISPATCHER = "MAIN_DISPATCHER"
const val BACKGROUND_DISPATCHER = "BACKGROUND_DISPATCHER"

val singletonModule = module {
    // Dispatchers
    single<CoroutineDispatcher>(named(DEFAULT_DISPATCHER)) { Dispatchers.Default }
    single<CoroutineDispatcher>(named(MAIN_DISPATCHER)) { Dispatchers.Main }
    single<CoroutineDispatcher>(named(BACKGROUND_DISPATCHER)) { Dispatchers.IO }

    //Network Client
    single<NetworkClient> { DefaultNetworkClient(get()) }

}