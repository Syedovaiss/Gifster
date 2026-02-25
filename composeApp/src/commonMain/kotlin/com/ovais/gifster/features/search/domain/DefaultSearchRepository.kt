package com.ovais.gifster.features.search.domain

import com.ovais.gifster.core.data.http.GifResponse
import com.ovais.gifster.core.data.network.DataError
import com.ovais.gifster.core.data.network.NetworkClient
import com.ovais.gifster.core.data.network.Result
import com.ovais.gifster.features.search.data.SearchRepository
import com.ovais.gifster.features.search.data.SearchRequestParam
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultSearchRepository(
    private val networkClient: NetworkClient,
    private val dispatcherIO: CoroutineDispatcher
) : SearchRepository {
    override suspend fun searchGif(param: SearchRequestParam): Result<GifResponse, DataError.Remote> {
        return withContext(dispatcherIO) {
            networkClient.searchGif(
                limit = param.limit,
                offset = param.offset,
                rating = param.rating,
                query = param.query
            )
        }
    }
}