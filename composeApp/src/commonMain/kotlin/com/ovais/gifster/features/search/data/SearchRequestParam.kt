package com.ovais.gifster.features.search.data

data class SearchRequestParam(
    val limit: Int = 25,
    val offset: Int,
    val rating: String = "g",
    val query: String
)
