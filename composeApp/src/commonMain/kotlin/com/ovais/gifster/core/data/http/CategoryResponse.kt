package com.ovais.gifster.core.data.http

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryResponse(
    @SerialName("data")
    val categories: List<Category> = emptyList(),
)

@Serializable
data class Category(
    @SerialName("gif")
    val gif: Gif? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("name_encoded")
    val nameEncoded: String? = null,
    @SerialName("subcategories")
    val subcategories: List<Subcategory> = emptyList()
)

@Serializable
data class Gif(
    @SerialName("bitly_gif_url")
    val bitlyGifUrl: String,
    @SerialName("bitly_url")
    val bitlyUrl: String,
    @SerialName("content_url")
    val contentUrl: String? = null,
    @SerialName("embed_url")
    val embedUrl: String? = null,
    @SerialName("id")
    val id: String? = null,
    @SerialName("images")
    val images: Images? = null,
    @SerialName("is_sticker")
    val isSticker: Int? = null,
    @SerialName("rating")
    val rating: String? = null,
    @SerialName("title")
    val title: String? = null,
    val type: String? = null,
    @SerialName("url")
    val url: String? = null,
)

@Serializable
data class Subcategory(
    @SerialName("name")
    val name: String? = null,
    @SerialName("name_encoded")
    val nameEncoded: String? = null
)

@Serializable
data class Images(
    @SerialName("original")
    val original: Original? = null,
    @SerialName("original_mp4")
    val originalMp4: OriginalMp4? = null,
    @SerialName("original_still")
    val originalStill: OriginalStill? = null,
    @SerialName("preview")
    val preview: Preview? = null,
    @SerialName("preview_gif")
    val previewGif: PreviewGif? = null,
)

@Serializable
data class Original(
    @SerialName("frames")
    val frames: String? = null,
    @SerialName("hash")
    val hash: String? = null,
    @SerialName("height")
    val height: String? = null,
    @SerialName("mp4")
    val mp4: String? = null,
    @SerialName("mp4_size")
    val mp4Size: String? = null,
    @SerialName("size")
    val size: String? = null,
    @SerialName("url")
    val url: String? = null,
    @SerialName("webp")
    val webp: String? = null,
    @SerialName("webp_size")
    val webpSize: String? = null,
    @SerialName("width")
    val width: String? = null
)

@Serializable
data class OriginalMp4(
    @SerialName("height")
    val height: String? = null,
    @SerialName("mp4")
    val mp4: String? = null,
    @SerialName("mp4_size")
    val mp4Size: String? = null,
    @SerialName("width")
    val width: String? = null
)

@Serializable
data class OriginalStill(
    @SerialName("height")
    val height: String? = null,
    @SerialName("size")
    val size: String? = null,
    @SerialName("url")
    val url: String? = null,
    @SerialName("width")
    val width: String? = null
)

@Serializable
data class Preview(
    @SerialName("height")
    val height: String? = null,
    @SerialName("mp4")
    val mp4: String? = null,
    @SerialName("mp4_size")
    val mp4Size: String? = null,
    @SerialName("width")
    val width: String? = null
)

@Serializable
data class PreviewGif(
    @SerialName("height")
    val height: String? = null,
    @SerialName("size")
    val size: String? = null,
    @SerialName("url")
    val url: String? = null,
    @SerialName("width")
    val width: String? = null
)