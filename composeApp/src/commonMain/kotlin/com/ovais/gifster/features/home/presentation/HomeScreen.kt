package com.ovais.gifster.features.home.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is HomeEffect.OnNavigateToSearch -> {}
                is HomeEffect.OnNavigateToGifDetail -> {}
                is HomeEffect.OnNavigateToRandom -> {}
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
            selectedCategory = null,
            onCategoryClick = {},
            onLoadMore = { viewModel.onIntent(HomeIntent.OnLoadMore) },
            columns = state.column
        )
    }
}

@Composable
fun HomeView(
    categories: List<Category>,
    trendingGifs: List<Gif>,
    isLoadingMore: Boolean,
    selectedCategory: String?,
    columns: Int,
    onCategoryClick: (Category) -> Unit,
    onLoadMore: () -> Unit
) {

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
            CategoriesSection(
                categories = categories,
                selected = selectedCategory,
                onCategoryClick = onCategoryClick
            )
        }

        items(trendingGifs, key = { it.id.toString() }) { gif ->
            TrendingGifItem(gif = gif)
        }

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

@Composable
fun CategoriesSection(
    categories: List<Category>,
    selected: String?,
    onCategoryClick: (Category) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = categories,
            key = { it.gif?.id ?: it.name.toString() }
        ) { category ->
            CategoryItem(category = category, isSelected = selected == category.nameEncoded) {
                onCategoryClick(category)
            }
        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val imageUrl = category.gif?.images?.original?.url

    Card(
        shape = RoundedCornerShape(16.dp),
        border = if (isSelected) BorderStroke(2.dp, Color.Magenta) else null,
        modifier = Modifier
            .size(width = 120.dp, height = 80.dp)
            .clickable { onClick() },
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
fun TrendingGifItem(gif: Gif) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier.fillMaxWidth().size(200.dp)
    ) {
        Box {
            CachedGifImage(
                gifUrl = gif.images?.original?.url,
                modifier = Modifier.fillMaxSize(),
                loaderSize = 40.dp
            )
        }
    }
}