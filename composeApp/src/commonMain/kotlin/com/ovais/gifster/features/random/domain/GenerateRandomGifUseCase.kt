package com.ovais.gifster.features.random.domain

import com.ovais.gifster.core.data.http.Gif
import com.ovais.gifster.core.data.network.DataError
import com.ovais.gifster.core.data.network.Result
import com.ovais.gifster.core.data.network.map
import com.ovais.gifster.features.random.data.RandomGifRepository
import com.ovais.gifster.utils.SuspendUseCase

fun interface GenerateRandomGifUseCase : SuspendUseCase<Result<Gif?, DataError>>

class DefaultGenerateRandomGifUseCase(
    private val randomGifRepository: RandomGifRepository
) : GenerateRandomGifUseCase {
    override suspend fun invoke(): Result<Gif?, DataError> {
        return randomGifRepository.generateRandom().map { it.gif }
    }
}