package com.ovais.gifster.features.random.domain

import com.ovais.gifster.core.data.http.GifResponse
import com.ovais.gifster.core.data.network.DataError
import com.ovais.gifster.core.data.network.NetworkClient
import com.ovais.gifster.core.data.network.Result
import com.ovais.gifster.features.random.data.RandomGifRepository
import com.ovais.gifster.features.random.data.RandomGifResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultRandomGifRepository(
    private val networkClient: NetworkClient,
    private val dispatcherIO: CoroutineDispatcher
): RandomGifRepository {
    override suspend fun generateRandom(): Result<RandomGifResponse, DataError> {
        return withContext(dispatcherIO) {
            networkClient.generateRandom()
        }

    }
}