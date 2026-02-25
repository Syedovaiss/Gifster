package com.ovais.gifster.features.search.data

import com.ovais.gifster.core.data.http.GifResponse
import com.ovais.gifster.core.data.network.DataError
import com.ovais.gifster.core.data.network.Result

fun interface SearchRepository {
    suspend fun searchGif(param: SearchRequestParam): Result<GifResponse, DataError.Remote>
}