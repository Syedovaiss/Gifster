package com.ovais.gifster.features.random.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovais.gifster.core.data.network.onError
import com.ovais.gifster.core.data.network.onSuccess
import com.ovais.gifster.features.random.domain.GenerateRandomGifUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RandomGifViewModel(
    private val generateRandomGifUseCase: GenerateRandomGifUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<RandomGifUiState>(RandomGifUiState.Idle)
    val uiState: StateFlow<RandomGifUiState> = _uiState.asStateFlow()

    fun loadRandomGif() {
        viewModelScope.launch {
            _uiState.value = RandomGifUiState.Loading
            try {
                generateRandomGifUseCase().onSuccess { gif ->
                    gif?.let {
                        _uiState.value = RandomGifUiState.Success(it)
                    } ?: run {
                        _uiState.value = RandomGifUiState.Error("Something went wrong")
                    }
                }.onError {
                    _uiState.value = RandomGifUiState.Error("Something went wrong")
                }
            } catch (e: Exception) {
                _uiState.value = RandomGifUiState.Error(e.message ?: "Something went wrong")
            }
        }
    }
}
