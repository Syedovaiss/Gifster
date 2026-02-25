package com.ovais.gifster.features.search.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ovais.gifster.core.data.http.Gif
import com.ovais.gifster.features.home.presentation.TrendingGifItem
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel(),
    onGifClicked: (Gif) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        SearchBar(
            onQueryChanged = {
                viewModel.onIntent(SearchIntent.OnQueryChanged(it))
            }
        )

        when (state) {

            SearchUiState.Idle -> {
                IdleSearchView()
            }

            is SearchUiState.Loading -> {
                SearchLoadingView()
            }

            is SearchUiState.Success -> {
                SearchResultsGrid(
                    results = (state as SearchUiState.Success).results,
                    onGifClicked = onGifClicked
                )
            }

            is SearchUiState.Empty -> {
                EmptySearchView(query = (state as SearchUiState.Empty).query)
            }

            is SearchUiState.Error -> {
                SearchErrorView(
                    message = (state as SearchUiState.Error).message,
                    onRetry = {
                        viewModel.onIntent(SearchIntent.Retry)
                    }
                )
            }
        }
    }
}
@Composable
fun SearchResultsGrid(
    results: List<Gif>,
    onGifClicked: (Gif) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(results, key = { it.id.toString() }) { gif ->
            TrendingGifItem(
                gif = gif,
                onClick = onGifClicked
            )
        }
    }
}

@Composable
fun SearchLoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun EmptySearchView(query: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No results found for \"$query\"",
            color = Color.Gray,
            fontSize = 16.sp
        )
    }
}

@Composable
fun SearchErrorView(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                text = message,
                color = Color.Red,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}

@Composable
fun IdleSearchView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Search for your favorite GIFs",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Try searching for cats, memes, reactions...",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}