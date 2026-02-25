package com.ovais.gifster

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ovais.gifster.core.navigation.Gifster
import com.ovais.gifster.features.home.presentation.HomeScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        Scaffold { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                Gifster()
            }
        }
    }
}