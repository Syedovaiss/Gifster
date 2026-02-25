package com.ovais.gifster.features.home.domain

import com.ovais.gifster.core.data.http.CategoryResponse
import com.ovais.gifster.core.data.http.GifResponse
import com.ovais.gifster.core.data.network.DataError
import com.ovais.gifster.core.data.network.Result

interface HomeRepository {
    suspend fun getCategories(): Result<CategoryResponse, DataError.Remote>
    suspend fun getTrendingGifs(
        limit: Int,
        offset: Int,
        rating: String
    ): Result<GifResponse, DataError.Remote>
}