package com.ovais.gifster.features.gif_detail.domain

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.request.get
import kotlinx.cinterop.refTo
import platform.Foundation.NSData
import platform.Foundation.NSDate
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.create
import platform.Foundation.timeIntervalSince1970
import platform.Foundation.writeToFile

actual class GifFileDownloader {

    private val client = HttpClient(Darwin)

    actual suspend fun download(url: String): String {

        val bytes: ByteArray = client.get(url).body()

        val tempDir = NSTemporaryDirectory()
        val fileName = "gif_${NSDate().timeIntervalSince1970}.gif"
        val fullPath = tempDir + fileName

        val nsData = NSData.create(
            bytes = bytes.refTo(0),
            length = bytes.size.toULong()
        )

        nsData.writeToFile(fullPath, true)

        return fullPath
    }
}