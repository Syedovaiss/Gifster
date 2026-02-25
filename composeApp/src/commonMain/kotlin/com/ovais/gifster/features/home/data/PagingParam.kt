package com.ovais.gifster.features.home.data

interface PagingParam {
    val limit: Int
    val offset: Int
}

data class TrendingGifParam(
    override val limit: Int = 25,
    override val offset: Int,
    val rating: String = "g"
) : PagingParam
