package com.ovais.gifster.features.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ovais.gifster.core.data.http.Category
import com.ovais.gifster.core.data.http.Gif
import com.ovais.gifster.utils.CachedGifImage
import com.ovais.gifster.utils.Callback
import com.ovais.gifster.utils.ParameterizedCallback
import gifster.composeapp.generated.resources.Res
import gifster.composeapp.generated.resources.ic_random
import gifster.composeapp.generated.resources.ic_search
import gifster.composeapp.generated.resources.ic_view
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onDetails: ParameterizedCallback<Gif>,
    onSearch: Callback,
    onRandom: Callback
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is HomeEffect.OnNavigateToSearch -> onSearch()
                is HomeEffect.OnNavigateToGifDetail -> onDetails(effect.item)
                is HomeEffect.OnNavigateToRandom -> onRandom()
            }
        }
    }

    when (val state = uiState) {
        is HomeUiState.Loading -> LoadingView()
        is HomeUiState.Error -> {
            ErrorDialogOverlay(
                state.message,
                onRetry = {
                    viewModel.onIntent(HomeIntent.Retry)
                }
            )
        }

        is HomeUiState.Success -> HomeView(
            categories = state.categories,
            trendingGifs = state.trendingGifs,
            isLoadingMore = state.isLoadingMore,
            onLoadMore = { viewModel.onIntent(HomeIntent.OnLoadMore) },
            columns = state.column,
            onGifClicked = {
                viewModel.onIntent(HomeIntent.OnGifClicked(it))
            },
            onRandomClick = {
                viewModel.onIntent(HomeIntent.OnGenerateRandom)
            },
            onSearchClick = {
                viewModel.onIntent(HomeIntent.OnSearchGif)
            }
        )
    }
}
@Composable
fun HomeView(
    categories: List<Category>,
    trendingGifs: List<Gif>,
    isLoadingMore: Boolean,
    columns: Int,
    onLoadMore: Callback,
    onGifClicked: ParameterizedCallback<Gif>,
    onRandomClick: Callback,
    onSearchClick: Callback
) {
    Column(modifier = Modifier.fillMaxSize()) {
        HomeTopBar(
            onSearchClick = onSearchClick,
            onRandomClick = onRandomClick
        )

        Spacer(modifier = Modifier.height(8.dp))

        val gridState = rememberLazyGridState()
        LaunchedEffect(gridState) {
            snapshotFlow { gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                .collect { lastVisible ->
                    val total = gridState.layoutInfo.totalItemsCount
                    if (lastVisible != null && lastVisible >= total - 4) {
                        onLoadMore()
                    }
                }
        }

        LazyVerticalGrid(
            state = gridState,
            columns = GridCells.Fixed(columns),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item(span = { GridItemSpan(columns) }) {
                CategoriesSection(categories)
            }

            itemsIndexed(
                items = trendingGifs,
                key = { index, gif -> "${gif.id}_$index" }
            ) { _, gif ->
                TrendingGifItem(
                    gif = gif,
                    onClick = onGifClicked,
                    onViewClick = onGifClicked
                )
            }
            /*items(trendingGifs, key = { it.id.toString() }) { gif ->
                TrendingGifItem(
                    gif = gif,
                    onClick = onGifClicked,
                    onViewClick = { *//* optional: view counter click *//* }
                )
            }*/

            if (isLoadingMore) {
                item(span = { GridItemSpan(columns) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
fun CategoriesSection(
    categories: List<Category>
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = categories,
            key = { it.gif?.id ?: it.name.toString() }
        ) { category ->
            CategoryItem(category = category)
        }
    }
}

@Composable
fun CategoryItem(
    category: Category
) {
    val imageUrl = category.gif?.images?.original?.url

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .size(width = 120.dp, height = 80.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box {
            CachedGifImage(
                gifUrl = imageUrl,
                modifier = Modifier.fillMaxSize(),
                loaderSize = 20.dp
            )

            // Gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                        )
                    )
            )

            Text(
                text = category.name?.replaceFirstChar { it.uppercase() } ?: "",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(6.dp)
            )
        }
    }
}
@Composable
fun TrendingGifItem(
    gif: Gif,
    onClick: ParameterizedCallback<Gif>,
    onViewClick: ParameterizedCallback<Gif>? = null
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .size(200.dp)
    ) {
        Box(
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current
            ) {
                onClick(gif)
            }
        ) {
            CachedGifImage(
                gifUrl = gif.images?.original?.url,
                modifier = Modifier.fillMaxSize(),
                loaderSize = 40.dp
            )

            // Top-right eye icon overlay
            onViewClick?.let {
                Image(
                    painter = painterResource(Res.drawable.ic_view),
                    contentDescription = "Views",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(28.dp)
                        .clickable { onViewClick(gif) }
                )
            }
        }
    }
}

@Composable
fun HomeTopBar(
    onSearchClick: Callback,
    onRandomClick: Callback,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Greeting
        Text(
            text = "Hello!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // Random button
            Image(
                painter = painterResource(Res.drawable.ic_random),
                contentDescription = "Random GIF",
                modifier = Modifier
                    .size(36.dp)
                    .clickable { onRandomClick() }
            )

            // Search button
            Image(
                painter = painterResource(Res.drawable.ic_search),
                contentDescription = "Search GIF",
                modifier = Modifier
                    .size(36.dp)
                    .clickable { onSearchClick() }
            )
        }
    }
}