package com.ovais.gifster.features.gif_detail.domain


expect class GifFileDownloader() {
    suspend fun download(url: String): String
}
