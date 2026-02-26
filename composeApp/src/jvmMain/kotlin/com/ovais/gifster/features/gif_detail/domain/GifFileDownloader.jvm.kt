package com.ovais.gifster.features.gif_detail.domain

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import java.io.File

actual class GifFileDownloader actual constructor() {
    private val client = HttpClient(CIO)

    actual suspend fun download(url: String): String {

        val bytes: ByteArray = client.get(url).body()

        val file = File(
            System.getProperty("java.io.tmpdir"),
            "gif_${System.currentTimeMillis()}.gif"
        )

        file.writeBytes(bytes)

        return file.absolutePath
    }
}