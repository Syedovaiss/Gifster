package com.ovais.gifster.features.random.data

import com.ovais.gifster.core.data.http.GifResponse
import com.ovais.gifster.core.data.network.DataError
import com.ovais.gifster.core.data.network.Result

fun interface RandomGifRepository {
    suspend fun generateRandom(): Result<RandomGifResponse, DataError>
}