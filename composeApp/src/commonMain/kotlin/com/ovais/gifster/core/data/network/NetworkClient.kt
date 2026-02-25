package com.ovais.gifster.core.data.network

import com.ovais.gifster.core.data.http.CategoryResponse
import com.ovais.gifster.core.data.http.GifResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

interface NetworkClient {
    suspend fun getCategories(): Result<CategoryResponse, DataError.Remote>
    suspend fun getTrendingGifs(
        limit: Int,
        offset: Int,
        rating: String
    ): Result<GifResponse, DataError.Remote>

    suspend fun searchGif(
        limit: Int,
        offset: Int,
        rating: String,
        query: String
    ): Result<GifResponse, DataError.Remote>
}

class DefaultNetworkClient(
    private val httpClient: HttpClient
) : NetworkClient {

    private companion object {
        private const val KEY_LIMIT = "limit"
        private const val KEY_OFFSET = "offset"
        private const val KEY_RATING = "rating"
        private const val KEY_BUNDLE = "bundle"
        private const val BUNDLE_MESSAGING_NON_CLIPS = "messaging_non_clips"
        private const val KEY_QUERY = "q"
    }

    override suspend fun getCategories(): Result<CategoryResponse, DataError.Remote> =
        safeCall<CategoryResponse> {
            httpClient.get(CATEGORY_ENDPOINT)
        }

    override suspend fun getTrendingGifs(
        limit: Int,
        offset: Int,
        rating: String
    ): Result<GifResponse, DataError.Remote> {
        return safeCall<GifResponse> {
            httpClient.get(TRENDING_GIFS_ENDPOINT) {
                parameter(KEY_LIMIT, limit)
                parameter(KEY_OFFSET, offset)
                parameter(KEY_RATING, rating)
                parameter(KEY_BUNDLE, BUNDLE_MESSAGING_NON_CLIPS)
            }
        }
    }

    override suspend fun searchGif(
        limit: Int,
        offset: Int,
        rating: String,
        query: String
    ): Result<GifResponse, DataError.Remote> {
        return safeCall<GifResponse> {
            httpClient.get(SEARCH_GIFS_ENDPOINT) {
                parameter(KEY_QUERY, query)
                parameter(KEY_LIMIT, limit)
                parameter(KEY_OFFSET, offset)
                parameter(KEY_RATING, rating)
                parameter(KEY_BUNDLE, BUNDLE_MESSAGING_NON_CLIPS)
            }
        }
    }
}