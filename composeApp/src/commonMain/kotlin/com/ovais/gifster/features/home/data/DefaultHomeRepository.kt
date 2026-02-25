package com.ovais.gifster.features.home.data

import com.ovais.gifster.core.data.http.CategoryResponse
import com.ovais.gifster.core.data.http.GifResponse
import com.ovais.gifster.core.data.network.DataError
import com.ovais.gifster.core.data.network.NetworkClient
import com.ovais.gifster.core.data.network.Result
import com.ovais.gifster.features.home.domain.HomeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultHomeRepository(
    private val networkClient: NetworkClient,
    private val dispatcherIO: CoroutineDispatcher
) : HomeRepository {

    override suspend fun getCategories(): Result<CategoryResponse, DataError.Remote> {
        return withContext(dispatcherIO) {
            networkClient.getCategories()
        }
    }

    override suspend fun getTrendingGifs(
        limit: Int,
        offset: Int,
        rating: String
    ): Result<GifResponse, DataError.Remote> {
        return withContext(dispatcherIO) {
            networkClient.getTrendingGifs(
                limit = limit,
                offset = offset,
                rating = rating
            )
        }
    }
}