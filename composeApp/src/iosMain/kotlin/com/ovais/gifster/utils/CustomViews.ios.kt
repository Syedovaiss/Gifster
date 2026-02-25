package com.ovais.gifster.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.reinterpret
import platform.CoreFoundation.CFDataCreate
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfURL
import platform.ImageIO.CGImageSourceCreateImageAtIndex
import platform.ImageIO.CGImageSourceCreateWithData
import platform.ImageIO.CGImageSourceGetCount
import platform.UIKit.UIImage
import platform.UIKit.UIImageView
import platform.UIKit.UIViewContentMode

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun GifView(url: String, modifier: Modifier) {

    UIKitView(
        factory = {
            val imageView = UIImageView()
            imageView.contentMode = UIViewContentMode.UIViewContentModeScaleAspectFit

            val nsUrl = NSURL.URLWithString(url)
            val data = nsUrl?.let { NSData.dataWithContentsOfURL(it) }

            if (data != null) {
                val bytes = data.bytes
                val length = data.length

                val cfData = CFDataCreate(
                    null,
                    bytes?.reinterpret(),
                    length.toLong()
                )
                val source = CGImageSourceCreateWithData(cfData, null)
                val count = CGImageSourceGetCount(source)

                val images = mutableListOf<UIImage>()
                var duration = 0.0

                for (i in 0 until count.toInt()) {

                    val cgImage =
                        CGImageSourceCreateImageAtIndex(source, i.toULong(), null)

                    if (cgImage != null) {
                        images.add(UIImage.imageWithCGImage(cgImage))
                        duration += 0.1 // fallback delay
                    }
                }

                imageView.animationImages = images
                imageView.animationDuration = duration
                imageView.startAnimating()
            }

            imageView
        },
        modifier = modifier
    )
}