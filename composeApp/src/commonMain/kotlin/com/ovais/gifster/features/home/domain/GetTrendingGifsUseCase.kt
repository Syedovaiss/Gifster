package com.ovais.gifster.features.home.domain

import com.ovais.gifster.core.data.http.Gif
import com.ovais.gifster.core.data.network.DataError
import com.ovais.gifster.core.data.network.Result
import com.ovais.gifster.core.data.network.map
import com.ovais.gifster.features.home.data.TrendingGifParam
import com.ovais.gifster.utils.SuspendParameterizeUseCase

fun interface GetTrendingGifsUseCase :
    SuspendParameterizeUseCase<TrendingGifParam, Result<List<Gif>, DataError>>

class DefaultGetTrendingGifsUseCase(
    private val homeRepository: HomeRepository
) : GetTrendingGifsUseCase {
    override suspend fun invoke(params: TrendingGifParam): Result<List<Gif>, DataError> {
        return homeRepository.getTrendingGifs(
            limit = params.limit,
            offset = params.offset,
            rating = params.rating
        ).map { it.gifs }
    }
}