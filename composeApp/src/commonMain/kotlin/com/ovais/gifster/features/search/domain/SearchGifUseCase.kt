package com.ovais.gifster.features.search.domain

import com.ovais.gifster.core.data.http.Gif
import com.ovais.gifster.core.data.network.DataError
import com.ovais.gifster.core.data.network.Result
import com.ovais.gifster.core.data.network.map
import com.ovais.gifster.features.search.data.SearchRepository
import com.ovais.gifster.features.search.data.SearchRequestParam
import com.ovais.gifster.utils.SuspendParameterizeUseCase

interface SearchGifUseCase :
    SuspendParameterizeUseCase<SearchRequestParam, Result<List<Gif>, DataError>>

class DefaultSearchGifUseCase(
    private val searchRepository: SearchRepository
) : SearchGifUseCase {
    override suspend fun invoke(params: SearchRequestParam): Result<List<Gif>, DataError> {
        return searchRepository.searchGif(
            params
        ).map { it.gifs }
    }
}