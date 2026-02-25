package com.ovais.gifster.features.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ovais.gifster.utils.shimmerBrush

@Composable
fun LoadingView(columns: Int = 2) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Shimmer for categories row
        CategoriesShimmerRow()

        Spacer(modifier = Modifier.height(12.dp))

        // Shimmer for trending GIF grid
        TrendingShimmerGrid(columns = columns)
    }
}


@Composable
fun CategoriesShimmerRow(count: Int = 6) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(count) {
            Box(
                modifier = Modifier
                    .size(width = 120.dp, height = 80.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(shimmerBrush())
            )
        }
    }
}

@Composable
fun TrendingShimmerGrid(columns: Int = 2, itemCount: Int = 6) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(itemCount) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f) // make square
                    .clip(RoundedCornerShape(12.dp))
                    .background(shimmerBrush())
            )
        }
    }
}