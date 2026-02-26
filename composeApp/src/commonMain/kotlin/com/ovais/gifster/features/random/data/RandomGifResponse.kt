package com.ovais.gifster.features.random.data

import com.ovais.gifster.core.data.http.Gif
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class RandomGifResponse(
    @SerialName("data")
    val gif: Gif? = null
)
