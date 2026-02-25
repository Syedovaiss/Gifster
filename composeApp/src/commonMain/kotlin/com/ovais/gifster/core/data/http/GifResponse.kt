package com.ovais.gifster.core.data.http

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GifResponse(
    @SerialName("data")
    val gifs: List<Gif>
)
