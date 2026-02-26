package com.ovais.gifster.features.random.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ovais.gifster.core.data.http.Gif
import com.ovais.gifster.utils.Callback
import com.ovais.gifster.utils.ParameterizedCallback
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RandomGifScreen(
    viewModel: RandomGifViewModel = koinViewModel(),
    onGifLoaded: ParameterizedCallback<Gif>,
    onCancelGeneration: Callback
) {
    var showDialog by remember { mutableStateOf(true) }
    var hasRequested by remember { mutableStateOf(false) } // prevent multiple API calls

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Generate Random GIF") },
            text = { Text("Do you want to generate a random GIF?") },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    hasRequested = true
                    viewModel.loadRandomGif()
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = {
                    showDialog = false
                    onCancelGeneration()
                }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Show loader only if API call has been requested
    if (hasRequested && state is RandomGifUiState.Loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }

    // Navigate when GIF is loaded
    LaunchedEffect(state, hasRequested) {
        if (hasRequested && state is RandomGifUiState.Success) {
            onGifLoaded((state as RandomGifUiState.Success).gif)
        }
    }

    // Error state
    if (hasRequested && state is RandomGifUiState.Error) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Failed to load random GIF", color = Color.Red)
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = { viewModel.loadRandomGif() }) {
                    Text("Retry")
                }
            }
        }
    }
}