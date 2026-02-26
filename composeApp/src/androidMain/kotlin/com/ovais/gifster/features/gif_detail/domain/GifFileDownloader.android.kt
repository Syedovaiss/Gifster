package com.ovais.gifster.features.gif_detail.domain

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.get
import org.koin.mp.KoinPlatform.getKoin
import java.io.File

actual class GifFileDownloader {

    private val context: Context = getKoin().get()
    private val client = HttpClient(OkHttp)

    actual suspend fun download(url: String): String {
        val bytes: ByteArray = client.get(url).body()
        val fileName = "gif_${System.currentTimeMillis()}.gif"

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Scoped storage (API 29+)
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/gif")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                ?: throw IllegalStateException("Failed to create file in Downloads")

            resolver.openOutputStream(uri)?.use { it.write(bytes) }

            uri.toString()
        } else {
            val downloadsDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!downloadsDir.exists()) downloadsDir.mkdirs()

            val file = File(downloadsDir, fileName)
            file.writeBytes(bytes)

            file.absolutePath
        }
    }
}