package com.ovais.gifster.features.gif_detail.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ovais.gifster.core.data.http.Gif
import com.ovais.gifster.utils.CachedGifImage
import com.ovais.gifster.utils.ifNullOrEmpty
import gifster.composeapp.generated.resources.Res
import gifster.composeapp.generated.resources.ic_download
import gifster.composeapp.generated.resources.ic_share
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GifDetailScreen(
    gif: Gif,
    viewModel: GifDetailViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        // Fullscreen GIF
        CachedGifImage(
            gifUrl = gif.images?.original?.url,
            modifier = Modifier.fillMaxSize(),
            loaderSize = 80.dp
        )

        // Top icons: Download
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black.copy(alpha = 0.6f))
                    .clickable {
                        val url = gif.images?.original?.url ?: return@clickable
                        viewModel.downloadGif(url)
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.ic_download),
                    contentDescription = "Download",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Bottom info panel
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                    )
                )
                .padding(16.dp)
        ) {

            // Title (bold)
            Text(
                text = gif.title.ifNullOrEmpty { "Untitled" },
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Other info
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Rating: ${gif.rating?.uppercase()}",
                    fontSize = 14.sp,
                    color = Color.White
                )
                Text(
                    text = "Type: ${gif.type}",
                    fontSize = 14.sp,
                    color = Color.White
                )
                Text(
                    text = "Sticker: ${gif.isSticker}",
                    fontSize = 14.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // State-based actions
            when (state) {
                is GifDetailUiState.Downloading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .size(50.dp),
                        color = Color.White
                    )
                }

                is GifDetailUiState.Error -> {
                    Text(
                        text = (state as GifDetailUiState.Error).message,
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }

                else -> Unit
            }
        }
    }
}