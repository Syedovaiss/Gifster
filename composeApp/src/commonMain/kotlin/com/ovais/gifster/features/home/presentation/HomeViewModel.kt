package com.ovais.gifster.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovais.gifster.core.data.network.onError
import com.ovais.gifster.core.data.network.onSuccess
import com.ovais.gifster.features.home.data.TrendingGifParam
import com.ovais.gifster.features.home.domain.GetCategoriesUseCase
import com.ovais.gifster.features.home.domain.GetTrendingGifsUseCase
import com.ovais.gifster.features.home.domain.GridSizeProvider
import com.ovais.gifster.utils.calculateOffset
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getTrendingGifsUseCase: GetTrendingGifsUseCase,
    private val gridSizeProvider: GridSizeProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    private val _uiEffect = MutableSharedFlow<HomeEffect>()
    val uiEffect: SharedFlow<HomeEffect> = _uiEffect.asSharedFlow()
    private val _currentPage = MutableStateFlow(1)
    private val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    init {
        fetchCategories()
        fetchTrendingGifs()
    }

    private fun fetchTrendingGifs() {
        viewModelScope.launch {
            getTrendingGifsUseCase(
                params = TrendingGifParam(
                    offset = calculateOffset(pageNumber = currentPage.value)
                )
            ).onSuccess {
                _uiState.update { state ->
                    when (state) {
                        is HomeUiState.Success -> {
                            state.copy(
                                trendingGifs = if (currentPage.value == 1) {
                                    it
                                } else {
                                    state.trendingGifs + it
                                },
                                isLoadingMore = false
                            )
                        }

                        else -> state
                    }
                }
            }.onError {
                updateError()
            }
        }
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            getCategoriesUseCase().onSuccess { categories ->
                _uiState.update {
                    HomeUiState.Success(
                        categories,
                        emptyList(),
                        column = gridSizeProvider.get()

                    )
                }
            }.onError {
                updateError()
            }
        }
    }

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.OnSearchGif -> {
                updateEffect(HomeEffect.OnNavigateToSearch(intent.query))
            }

            is HomeIntent.OnGenerateRandom -> {
                updateEffect(HomeEffect.OnNavigateToRandom)
            }

            is HomeIntent.OnGifClicked -> {
                updateEffect(HomeEffect.OnNavigateToGifDetail)
            }

            is HomeIntent.OnLoadMore -> {
                loadMore()
            }

            is HomeIntent.Retry -> {
                reInitialize()
            }
        }
    }

    private fun reInitialize() {
        _uiState.update { HomeUiState.Loading }
        _currentPage.value = 1
        fetchCategories()
        fetchTrendingGifs()
    }

    private fun loadMore() {
        _currentPage.value = currentPage.value + 1
        _uiState.update { state ->
            when (state) {
                is HomeUiState.Success -> {
                    state.copy(
                        isLoadingMore = true
                    )
                }

                else -> state
            }
        }
        fetchTrendingGifs()
    }

    private fun updateEffect(effect: HomeEffect) {
        viewModelScope.launch {
            _uiEffect.emit(effect)
        }
    }

    private fun updateError() {
        _uiState.update {
            HomeUiState.Error("Something went wrong")
        }
    }
}

