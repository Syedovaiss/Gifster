package com.ovais.gifster.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.awt.image.BufferedImage
import java.net.URL
import javax.imageio.ImageIO
import javax.imageio.metadata.IIOMetadataNode
import javax.imageio.stream.ImageInputStream

@Composable
actual fun GifView(url: String, modifier: Modifier) {
    var frames by remember { mutableStateOf<List<ImageBitmap>>(emptyList()) }
    var delays by remember { mutableStateOf<List<Long>>(emptyList()) }
    var currentFrameIndex by remember { mutableStateOf(0) }

    // Load GIF frames
    LaunchedEffect(url) {
        withContext(Dispatchers.IO) {

            val input = URL(url).openStream()
            val stream: ImageInputStream = ImageIO.createImageInputStream(input)
            val reader = ImageIO.getImageReaders(stream).next()

            reader.input = stream
            val frameCount = reader.getNumImages(true)

            val tempFrames = mutableListOf<ImageBitmap>()
            val tempDelays = mutableListOf<Long>()

            for (i in 0 until frameCount) {

                val bufferedImage: BufferedImage = reader.read(i)
                tempFrames.add(bufferedImage.toComposeImageBitmap())

                // 🔥 Correct way to read delay
                val metadata = reader.getImageMetadata(i)
                val root = metadata.getAsTree(metadata.nativeMetadataFormatName)
                val nodes = (root as IIOMetadataNode)
                    .getElementsByTagName("GraphicControlExtension")

                var delayTime = 100L // default 100ms

                if (nodes.length > 0) {
                    val node = nodes.item(0) as IIOMetadataNode
                    val delay = node.getAttribute("delayTime").toIntOrNull()
                    if (delay != null) {
                        delayTime = delay * 10L // convert 1/100 sec to ms
                    }
                }

                tempDelays.add(delayTime)
            }

            reader.dispose()
            stream.close()
            input.close()

            frames = tempFrames
            delays = tempDelays
        }
    }

    // Animate
    LaunchedEffect(frames) {
        if (frames.isEmpty()) return@LaunchedEffect

        while (true) {
            delay(delays.getOrElse(currentFrameIndex) { 100L })
            currentFrameIndex = (currentFrameIndex + 1) % frames.size
        }
    }

    // Render
    if (frames.isNotEmpty()) {
        Box(modifier = modifier) {
            Image(
                bitmap = frames[currentFrameIndex],
                contentDescription = "GIF"
            )
        }
    }
}